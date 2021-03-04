package in.mamga.carousalnotificationmaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import in.mamga.carousalnotification.Carousal;
import in.mamga.carousalnotification.CarousalItem;

public class MainActivity extends AppCompatActivity {
    Button btnQuote;
    Button btnNasa;
    TextView tvStatus;
    TextView tvResponse;
    ProgressDialog progressDialog;

    private final String FETCHING = "Status = fetching data";
    private final String BUILDING_CAROUSAL = "Status = Data detched. building carousal";
    private final String CAROUSALPREPARING = "Status = Caraousal preparing. will be be up in few seconds!";
    private final String FETCHING_ERROR = "Status = Seems like this url is not working. Should try others.";
    private final String PARSING_ERROR = "Status = Seems like data format of this spi has changed. Should try others.";


    public static final String TYPE_QUOTE = "Quote";
    public static final String TYPE_IMAGE_MARS = "MarsImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpLayout();

        onNewIntent(getIntent());
    }

    private void setUpLayout() {
        btnQuote  = (Button) findViewById(R.id.btnQuote);
        btnNasa = (Button) findViewById(R.id.btnMovies);
        tvStatus = (TextView) findViewById(R.id.tvCurrentStatus);
        tvResponse = (TextView) findViewById(R.id.tvResponse);
        btnQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchQuotes();
            }
        });

        btnNasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchMarsPhotos();
            }
        });
    }

    private void fetchMarsPhotos() {
        tvStatus.setText( FETCHING);
        showDialog("Fetching Photos");
        JSONParser.OnConnectionListener onConnectionListener = new JSONParser.OnConnectionListener() {
            @Override
            public void onResponse(String s) {
                hideDialog();
                tvStatus.setText( BUILDING_CAROUSAL);
                tvResponse.setText("Fetched Result = " + s);
                buildMarsCarousal(s);
            }

            @Override
            public void onError() {
                hideDialog();
                tvStatus.setText(FETCHING_ERROR);

            }
        };
        JSONParser jsonParser = new JSONParser(onConnectionListener);
        jsonParser.fetchJson("https://mars-photos.herokuapp.com/api/v1/rovers/opportunity/photos?earth_date=2015-6-3&camera=pancam");

    }

    /**
     * once we receive data from api, we start building the carousal
     * @param s : response string
     */
    private void buildMarsCarousal(String s) {

        ArrayList<RoverItem> photoList = new ArrayList<RoverItem>();
        try {
            JSONObject photoOb = new JSONObject(s);
            JSONArray photos = photoOb.getJSONArray("photos");
            Type type = new TypeToken<RoverItem>() {
            }.getType();
            for (int i = 0; i < photos.length() && i < 10 ; i = i + 4 ) {

                RoverItem item = new Gson().fromJson(photos.get(i).toString(), type);
                photoList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            tvStatus.setText(PARSING_ERROR);
        }

        if (photoList.size() > 0) {
            tvStatus.setText(CAROUSALPREPARING);
            //Here we build the carousal
            Carousal carousal = Carousal.with(this).beginTransaction();
            carousal.setContentTitle("Opportunity on Mars!").setContentText("Check out these photographs by Opportunity on Mars.");
            for ( RoverItem photo : photoList) {

                //Notice here. In case you want to preserve data of each item, you can save a gson string
                // of the object in carousal item's id.  Though not much recommended.

                CarousalItem cItem = new CarousalItem(new Gson().toJson(photo).toString(), photo.getEarth_date(), null, photo.getImg_src());

                //Additionally we can set a type to it. It is useful if we are showing more than one type
                //of data in carousal. so that we know, where to go when an item is clicked.
                cItem.setType(TYPE_IMAGE_MARS);
                carousal.addCarousalItem(cItem);
            }
            carousal.buildCarousal();
        }
    }

    private void fetchQuotes() {
        tvStatus.setText( FETCHING);
        showDialog("Fetching Quotes");
        JSONParser.OnConnectionListener onConnectionListener = new JSONParser.OnConnectionListener() {
            @Override
            public void onResponse(String s) {
                hideDialog();
                tvStatus.setText( BUILDING_CAROUSAL);
                tvResponse.setText("Fetched Result = " + s);
                buildQuoteCarousal(s);
            }

            @Override
            public void onError() {
                hideDialog();
                tvStatus.setText(FETCHING_ERROR);

            }
        };
        JSONParser jsonParser = new JSONParser(onConnectionListener);
        jsonParser.fetchJson("http://quotesondesign.com/wp-json/posts?filter[orderby]=rand&filter[posts_per_page]=10");

    }

    /**
     * once we receive data from api, we start building the carousal
     * @param s : response string
     */
    private void buildQuoteCarousal(String s) {
        ArrayList<QuoteItem> quotesList = new ArrayList<QuoteItem>();
        try {
            JSONArray quotes = new JSONArray(s);
            for (int i = 0; i < quotes.length() ; i++) {
                JSONObject quote = (JSONObject) quotes.get(i);
                quotesList.add(new QuoteItem(quote.getInt("ID"), quote.getString("title"), quote.getString("content")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            tvStatus.setText(PARSING_ERROR);
        }

        if (quotesList.size() > 0) {
            tvStatus.setText(CAROUSALPREPARING);
            //Here we build the carousal
            Carousal carousal = Carousal.with(this).beginTransaction();
            carousal.setContentTitle("Quotes from everywhere!").setContentText("Notice these random quotes from around the world")
                    .setBigContentTitle("Quotes from everywhere!").setBigContentText("Notice these random quotes from around the world");
            for ( QuoteItem item : quotesList) {
                CarousalItem cItem = new CarousalItem(item.getID()+"", item.getTitle(), item.getContent(),null);

                //Additionally we can set a type to it. It is useful if we are showing more than one type
                //of data in carousal. so that we know, where to go when an item is clicked.
                cItem.setType(TYPE_QUOTE);
                carousal.addCarousalItem(cItem);
            }
            carousal.setOtherRegionClickable(true);
            carousal.buildCarousal();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(Carousal.CAROUSAL_ITEM_CLICKED_KEY)) {
                CarousalItem carousalItem = (CarousalItem) extras.getParcelable(Carousal.CAROUSAL_ITEM_CLICKED_KEY);
                Toast.makeText(this, carousalItem.getTitle() + carousalItem.getDescription(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
            progressDialog = null;
        }
    }
}
