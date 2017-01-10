package in.mamga.carousalnotificationmaster;

import android.os.AsyncTask;
import android.text.TextUtils;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    private OnConnectionListener onConnectionListener;

    // constructor
    public JSONParser( OnConnectionListener onConnectionListener) {
        this.onConnectionListener = onConnectionListener;
    }

    public interface OnConnectionListener {
        void onResponse (String s);
        void onError();
    }

    public void fetchJson (final String urlString) {
       new AsyncTask<Void, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(Void... params) {
                StringBuilder builder = new StringBuilder();
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

                    String inputString;
                    while ((inputString = bufferedReader.readLine()) != null) {
                        builder.append(inputString);
                    }
                    urlConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    onConnectionListener.onError();
                }
                return builder.toString();
            }

            protected void onPostExecute(String result) {
                if (TextUtils.isEmpty(result)) {
                    onConnectionListener.onError();
                } else {
                    onConnectionListener.onResponse(result);
                }

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}