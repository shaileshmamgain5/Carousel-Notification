package in.mamga.carousalnotificationmaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
            if (item != null) {
                //Now we need to know where to redirect event
                if (!TextUtils.isEmpty(item.getType())) {
                    switch (item.getType()) {
                        case MainActivity.TYPE_QUOTE :
                            Intent detailIntent = new Intent(context, QuoteDetailActivity.class);
                            Bundle b = new Bundle();
                            b.putString(QuoteDetailActivity.QUOTE_ID_KEY, item.getId());
                            detailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            detailIntent.putExtras(b);
                            context.startActivity(detailIntent);
                            break;
                        case MainActivity.TYPE_IMAGE_MARS :
                            Intent photoIntent = new Intent(context, MarsImageActivity.class);
                            Bundle b1 = new Bundle();
                            b1.putString(MarsImageActivity.PHOTO_KEY, item.getId());
                            photoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            photoIntent.putExtras(b1);
                            context.startActivity(photoIntent);
                            break;
                    }
                }
            } else {  //Meaning other region is clicked and isOtherRegionClick is set to true.
                Toast.makeText(context, "Other region clicked", Toast.LENGTH_LONG).show();
            }

        }
    }
}
