package in.mamga.carousalnotificationmaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;

public class MarsImageActivity extends AppCompatActivity {

    ImageView marsImage;
    TextView sol, earthDate, roverInfoObject;
    ProgressDialog progressDialog;
    public static final String PHOTO_KEY = "photoKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mars_image);
        marsImage = (ImageView) findViewById(R.id.marsImage);
        sol = (TextView) findViewById(R.id.sol);
        earthDate = (TextView) findViewById(R.id.earthDate);
        roverInfoObject = (TextView) findViewById(R.id.roverInfoObject);

        onNewIntent(getIntent());
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(PHOTO_KEY)) {
                String photoString = extras.getString(PHOTO_KEY);
                if (photoString != null)
                    fillViews(photoString);
            }
        }
    }

    private void fillViews(String photoString) {
        Type type = new TypeToken<RoverItem>() {
        }.getType();
        RoverItem item = new Gson().fromJson(photoString, type);

        Picasso.get().load(item.getImg_src()).into(marsImage);
        sol.setText("Sol : " + item.getSol());
        earthDate.setText("EarthDate : " + item.getEarth_date());
        roverInfoObject.setText("Rover info object : " +  new Gson().toJson(item.getRover()).toString());
    }
}
