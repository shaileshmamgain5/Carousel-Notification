package in.mamga.carousalnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;

/**
 * Created by Shailesh on 06/01/17.
 */

public class Carousal {
    private static Carousal carousal;
    private Context context;
    private ArrayList<CarousalItem> carousalItems;
    private String contentTitle, contentText; //title and text while it is small
    private String bigContentTitle, bigContentText; //title and text when it becomes large
    public static final String TAG = "Carousal";
    private NotificationCompat.Builder mBuilder;
    private PendingIntent onItemClickPendingIntent;
    private int carousalNotificationId = 9873715; //Random id for notification. Will cancel any
    // notification that have existing same id.

    private RemoteViews bigView;
    private static int currentStartIndex = 0; //Variable that keeps track of where the startIndex is
    Notification foregroundNote;
    private RemoteViews pendingIntents;

    private Carousal(Context context) {
        this.context = context;
        mBuilder = new NotificationCompat.Builder(context);
    }

    /**
     * Doubly locked singleton pattern
     *
     * @param context : Required for Notifications
     * @return carousal instance of this class
     */
    public static Carousal getInstance(Context context) {
        if (carousal == null) {
            synchronized (Carousal.class) {
                if (carousal == null) {
                    carousal = new Carousal(context);
                }
            }
        }
        return carousal;
    }

    /**
     * function to add a carousal item to the array-list
     *
     * @param carousalItem : item to be added
     */
    public Carousal addCarousalItem(CarousalItem carousalItem) {
        if (carousalItem != null) {
            if (carousalItems == null) {
                carousalItems = new ArrayList<CarousalItem>();
            }
            carousalItems.add(carousalItem);
        } else {
            Log.e(TAG, "Null carousal can't be added!");
        }
        return this;
    }

    /**
     * sets title of notification
     *
     * @param title : Title need to be non null
     */
    public Carousal setContentTitle(String title) {
        if (title != null) {
            this.contentTitle = title;
        } else {
            Log.e(TAG, "Null parameter");
        }
        return this;
    }

    /**
     * sets content text of notification
     *
     * @param contentText : contentText need to be non null
     */
    public Carousal setContentText(String contentText) {
        if (contentText != null) {
            this.contentText = contentText;
        } else {
            Log.e(TAG, "Null parameter");
        }
        return this;
    }

    /**
     * sets big content text of notification
     *
     * @param bigContentText : bigContentText need to be non null
     */
    public Carousal setBigContentText(String bigContentText) {
        if (bigContentText != null) {
            this.bigContentText = bigContentText;
        } else {
            Log.e(TAG, "Null parameter");
        }
        return this;
    }

    /**
     * sets big content title of notification
     *
     * @param bigContentTitle : bigContentTitle need to be non null
     */
    public Carousal setBigContentTitle(String bigContentTitle) {
        if (bigContentTitle != null) {
            this.bigContentTitle = bigContentTitle;
        } else {
            Log.e(TAG, "Null parameter");
        }
        return this;
    }


    /**
     * sets pending intent that will be fired when the user clicks on a item. Consider it similar
     * to {@link new NotificationCompat.Builder.setContentIntent(PendingIntent)}
     *
     * @param pendingIntent
     * @return this instance
     */
    public Carousal setOnItemClickIntent(PendingIntent pendingIntent) {
        if (pendingIntent != null) {
            this.onItemClickPendingIntent = pendingIntent;
        } else {
            Log.e(TAG, "null parameter");
        }
        return this;
    }

    /**
     * final function which displays carousal. Make sure carousalItems and pending intents are
     * set before calling this method. Otherwise noting will happen.
     */
    public void showCarousal(int firstItemIntex, int SecondItemIndex) {
        bigView = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.carousal_notification_item);


        if (currentStartIndex == 0) {
            // Locate and set the Image into customnotificationtext.xml ImageViews
            bigView.setImageViewResource(R.id.ivImageLeft, R.drawable.how_it_works_host_manage_calender);
            bigView.setImageViewResource(R.id.ivImageRight, R.drawable.how_it_works_host_requests);
        } else {
            bigView.setImageViewResource(R.id.ivImageLeft, R.drawable.how_it_works_host_video);
            bigView.setImageViewResource(R.id.ivImageRight, R.drawable.how_its_works_icon);
        }

        // Locate and set the Text into customnotificationtext.xml TextViews
        bigView.setTextViewText(R.id.tvRightTitleText, "RightTitle");
        bigView.setTextViewText(R.id.tvLeftTitleText, "LeftTitle");


        setPendingIntents(bigView);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context);

        mBuilder.setContentTitle("Title string").setContentText("Some CustomText")
                .setSmallIcon(R.drawable.ic_carousal_right_icon).setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_carousal_left_arrow));
        foregroundNote = mBuilder.build();
        foregroundNote.bigContentView = bigView;

        // now show notification..
        NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.notify(carousalNotificationId, foregroundNote);

    }

    /**
     * creates pending intents for the clickable regions of the notification
     */
    public void setPendingIntents(RemoteViews bigView) {
        //right arrow
        PendingIntent rightArrowPendingIntent = getPendingIntent(CarousalConstants.EVENT_RIGHT_ARROW_CLICKED);
        bigView.setOnClickPendingIntent(R.id.ivArrowRight, rightArrowPendingIntent);
        //left arrow
        PendingIntent leftArrowPendingIntent = getPendingIntent(CarousalConstants.EVENT_LEFT_ARROW_CLICKED);
        bigView.setOnClickPendingIntent(R.id.ivArrowLeft, leftArrowPendingIntent);
        //right item
        PendingIntent rightItemPendingIntent = getPendingIntent(CarousalConstants.EVENT_RIGHT_ITEM_CLICKED);
        bigView.setOnClickPendingIntent(R.id.ivImageRight, rightItemPendingIntent);
        //left item
        PendingIntent leftItemPendingIntent = getPendingIntent(CarousalConstants.EVENT_LEFT_ITEM_CLICKED);
        bigView.setOnClickPendingIntent(R.id.ivImageLeft, leftItemPendingIntent);
    }

    /**
     * creates pending intents with added bundle information about the region clicked
     * @param eventClicked : integer id of the region clicked
     * @return pendingIntent for the same
     */
    private PendingIntent getPendingIntent(int eventClicked) {
        Intent carousalIntent = new Intent(CarousalConstants.CAROUSAL_EVENT_FIRED_INTENT_FILTER);
        Bundle bundle = new Bundle();
        bundle.putInt(CarousalConstants.EVENT_CAROUSAL_ITEM_CLICKED_KEY, eventClicked);
        carousalIntent.putExtras(bundle);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, eventClicked, carousalIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pIntent;
    }

    public void showCarousal() {
        showCarousal(0, 1);
    }

    /**
     * Handles all click events from the carousal notification.
     *
     * @param clickEvent
     */
    public void handleClickEvent(int clickEvent) {
        switch (clickEvent) {
            case CarousalConstants.EVENT_LEFT_ARROW_CLICKED:
                onLeftArrowClicked();
                break;
            case CarousalConstants.EVENT_RIGHT_ARROW_CLICKED:
                onRightArrowClicked();
                break;
            case CarousalConstants.EVENT_LEFT_ITEM_CLICKED:
                onLeftItemClicked();
                break;
            case CarousalConstants.EVENT_RIGHT_ITEM_CLICKED:
                onRightItemClicked();
                break;
            case CarousalConstants.EVENT_OTHER_REGION_CLICKED:
                break;
            default:
                break;
        }
    }

    private void onRightItemClicked() {

    }

    private void onLeftItemClicked() {

    }

    private void onLeftArrowClicked() {
        currentStartIndex = 0;
        showCarousal();
    }

    private void onRightArrowClicked() {
        currentStartIndex = 2;
        showCarousal();
    }

}
