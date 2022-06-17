package in.mamga.carousalnotificationmaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class QuoteDetailActivity extends AppCompatActivity {
    TextView quoteId, quoteTitle, quoteContent, quoteLink;

    ProgressDialog progressDialog;
    public static final String QUOTE_ID_KEY = "QuoteId";
    String url = "http://quotesondesign.com/wp-json/posts/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_detail);
        quoteId = (TextView) findViewById(R.id.quoteId);
        quoteTitle = (TextView) findViewById(R.id.quoteTitle);
        quoteContent = (TextView) findViewById(R.id.quoteContent);
        quoteLink = (TextView) findViewById(R.id.quoteLink);

        onNewIntent(getIntent());
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(QUOTE_ID_KEY)) {
                String quoteId = extras.getString(QUOTE_ID_KEY);
                if (quoteId != null)
                    fetchQuote(quoteId);
            }
        }
    }


    private void fetchQuote(String id) {
        showDialog("Fetching Quote");
        JSONParser.OnConnectionListener onConnectionListener = new JSONParser.OnConnectionListener() {
            @Override
            public void onResponse(String s) {
                hideDialog();
                setUpTexts(s);
            }

            @Override
            public void onError() {
                hideDialog();


            }
        };
        JSONParser jsonParser = new JSONParser(onConnectionListener);
        jsonParser.fetchJson(url + id);

    }


    private void setUpTexts(String s) {
        try {
            JSONObject quote = new JSONObject(s);
            quoteId.setText(quote.getInt("ID") + "");
            quoteTitle.setText(quote.getString("title"));
            quoteContent.setText(quote.getString("content"));
            quoteLink.setText(quote.getString("link"));

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private void showDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog != null) {
            progressDialog.hide();
            progressDialog = null;
        }
    }
}
