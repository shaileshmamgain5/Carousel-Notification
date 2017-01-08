package in.mamga.carousalnotificationmaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import in.mamga.carousalnotification.Carousal;
import in.mamga.carousalnotification.CarousalItem;


/**
 * Created by Shailesh on 08/01/17.
 */

public class CarousalItemClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            //Intent intent1 = new Intent(context, MainActivity.class);
            //intent1.putExtras(bundle);
            //intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(intent1);
            CarousalItem item = bundle.getParcelable(Carousal.CAROUSAL_ITEM_CLICKED_KEY);
            if (item != null)
            Toast.makeText(context, item.getTitle() + item.getDescription(), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "Null Exception", Toast.LENGTH_LONG).show();
        }
    }
}
