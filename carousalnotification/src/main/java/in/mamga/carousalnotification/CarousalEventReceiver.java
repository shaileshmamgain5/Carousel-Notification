package in.mamga.carousalnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class CarousalEventReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int carousalEvent = bundle.getInt(CarousalConstants.EVENT_CAROUSAL_ITEM_CLICKED_KEY);
            Carousal.getInstance(context).handleClickEvent(carousalEvent);
        }
    }
}