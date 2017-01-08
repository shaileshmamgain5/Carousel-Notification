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

                Carousal caraousal = Carousal.getInstance(MainActivity.this).clearCarousalIfExists();
                caraousal.setBigContentTitle("Choose Among these");
                caraousal.addCarousalItem(new CarousalItem("id1", "Title 1", "description 1", "https://farm1.staticflickr.com/665/31344887084_21aaed9f86_n.jpg"));
                caraousal.addCarousalItem(new CarousalItem("id2","Title 2", "description 2", "https://farm1.staticflickr.com/334/31344889064_0da91a8325_n.jpg"));
                //caraousal.addCarousalItem(new CarousalItem("id2","Title 2", "description 2", null));
                caraousal.addCarousalItem(new CarousalItem("id3",null, "description 3", "https://farm1.staticflickr.com//641//31344889654_6f84ef8604_n.jpg"));
                caraousal.addCarousalItem(new CarousalItem("id4","Title 4", "description 4", "https://farm1.staticflickr.com//305//31344890444_5451c48ceb_n.jpg"));
                caraousal.addCarousalItem(new CarousalItem("id5","Title 5", "description 5", "https://farm1.staticflickr.com//373//31344892684_de564cc252_n.jpg"));
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
