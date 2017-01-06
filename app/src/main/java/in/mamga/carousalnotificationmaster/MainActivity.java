package in.mamga.carousalnotificationmaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import in.mamga.carousalnotification.Carousal;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnCarousalWithoutAction = (Button) findViewById(R.id.btnCarousalWithoutAction);

        btnCarousalWithoutAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Carousal.getInstance(MainActivity.this).showCarousal();

            }
        });
    }
}
