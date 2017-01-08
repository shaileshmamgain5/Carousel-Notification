package in.mamga.carousalnotificationmaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import in.mamga.carousalnotification.Carousal;
import in.mamga.carousalnotification.CarousalItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnCarousalWithoutAction = (Button) findViewById(R.id.btnCarousalWithoutAction);

        btnCarousalWithoutAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Carousal caraousal = Carousal.getInstance(MainActivity.this);
                caraousal.setBigContentTitle("Choose Among these");
                caraousal.addCarousalItem(new CarousalItem("Title 1", "description 1", "photourl 1"));
                caraousal.addCarousalItem(new CarousalItem("Title 2", "description 2", "photourl 2"));
                caraousal.addCarousalItem(new CarousalItem(null, "description 3", "photourl 3"));
                caraousal.addCarousalItem(new CarousalItem("Title 4", "description 4", "photourl 4"));
                caraousal.addCarousalItem(new CarousalItem("Title 5", "description 5", "photourl 5"));
                caraousal.buildCarousal();

            }
        });

        onNewIntent(getIntent());
    }

    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(Carousal.CAROUSAL_ITEM_CLICKED_KEY)) {
                CarousalItem carousalItem = (CarousalItem) extras.getParcelable(Carousal.CAROUSAL_ITEM_CLICKED_KEY);
                Toast.makeText(this, carousalItem.getTitle() + carousalItem.getDescription(), Toast.LENGTH_LONG).show();
            }
        }


    }
}
