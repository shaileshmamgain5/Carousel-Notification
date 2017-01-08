package in.mamga.carousalnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

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
    private String leftItemTitle, leftItemDescription;
    private String rightItemTitle, rightItemDescription;
    public static final String TAG = "Carousal";
    private NotificationCompat.Builder mBuilder;
    private int carousalNotificationId = 9873715; //Random id for notification. Will cancel any
    // notification that have existing same id.

    private RemoteViews bigView;
    private static int currentStartIndex = 0; //Variable that keeps track of where the startIndex is
    private static int notificationPriority = NotificationCompat.PRIORITY_DEFAULT;
    Notification foregroundNote;

    static Bitmap appIcon;
    static Bitmap smallIcon;
    static int smallIconResourceId = -1; //check before setting it that it does exists
    static Bitmap largeIcon;
    static Bitmap caraousalPlaceholder;

    private CarousalItem leftItem, rightItem;

    private CarousalSetUp carousalSetUp;
    private String smallIconPath, largeIconPath, placeHolderImagePath; //Stores path of these images if set by user


    public static final String CAROUSAL_ITEM_CLICKED_KEY = "CarousalNotificationItemClickedKey";

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
                    try {
                        appIcon = CarousalUtilities.carousalDrawableToBitmap(context.getPackageManager().getApplicationIcon(context.getPackageName()));
                    } catch (PackageManager.NameNotFoundException e) {
                        appIcon = null;
                        Log.e(TAG, "Unable to retrieve app Icon");
                    }

                }
            }
        }
        return carousal;
    }


    //=========================================CAROUSAL SETTERS ===================================//

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
     * sets priority of the carousal notificaition. By default it is NotificationCompat.PRIORITY_DEFAULT
     *
     * @param priority : needs to be in permissible range (-2 to 2)
     * @return
     */
    public Carousal setNotificationPriority(int priority) {
        if (priority >= NotificationCompat.PRIORITY_MIN && priority <= NotificationCompat.PRIORITY_MAX) {
            notificationPriority = priority;
        } else {
            Log.i(TAG, "Invalid priority");
        }
        return this;
    }

    /**
     * sets small Icon based on the resource id provided
     *
     * @param resourceId : like R.drawable.smallIcon
     * @return
     */
    public Carousal setSmallIconResource(int resourceId) {
        //First we need to check it if it is a valid resource.
        try {
            smallIcon = BitmapFactory.decodeResource(context.getResources(), resourceId);
        } catch (Exception e) {
            smallIcon = null;
            Log.e(TAG, "Unable to decode resource");
        }

        if (smallIcon != null) {  //meaning a valid resource
            smallIconResourceId = resourceId;
        }
        return this;
    }


    /**
     * sets largeIcon based on the resource id provided
     *
     * @param resourceId : like R.drawable.smallIcon
     * @return
     */
    public Carousal setLargeIcon(int resourceId) {
        try {
            largeIcon = BitmapFactory.decodeResource(context.getResources(), resourceId);
        } catch (Exception e) {
            Log.e(TAG, "Unable to decode resource");
        }
        return this;
    }

    public Carousal setLargeIcon(Bitmap large) {
        if (large != null) {
            largeIcon = large;
        } else {
            largeIcon = null;
            Log.i(TAG, "Null parameter");
        }
        return this;
    }

    /**
     * sets largeIcon based on the resource id provided
     *
     * @param resourceId : like R.drawable.smallIcon
     * @return
     */
    public Carousal setCarousalPlaceHolder(int resourceId) {
        try {
            caraousalPlaceholder = BitmapFactory.decodeResource(context.getResources(), resourceId);
        } catch (Exception e) {
            caraousalPlaceholder = null;
            Log.e(TAG, "Unable to decode resource");
        }
        return this;
    }

    public Carousal setCarousalPlaceHolder(Bitmap placeholder) {
        if (placeholder != null) {
            placeholder = placeholder;
        } else {
            placeholder = null;
            Log.i(TAG, "Null parameter");
        }
        return this;
    }


   /* *//**
     * sets pending intent that will be fired when the user clicks on a item. Consider it similar
     * to {@link new NotificationCompat.Builder.setContentIntent(PendingIntent)}
     *
     * @param intent     : Activity, service or receiver you want to invoke upon click of an item
     * @param intentType : type of intent --> is it calling service, reciever or activity
     * @return this instance
     *//*
    public Carousal setOnItemClickIntent(Intent intent, IntentType intentType) {
        if (intent != null && intentType != null) {
            this.carousalIntent = intent;
            this.carousalIntentType = intentType;
        } else {
            Log.e(TAG, "null parameter");
        }
        return this;
    }*/


    //=======================================SETTING UP CAROUSAL ===================================//

    /**
     * Function to be called by user.
     * 1) A carousal items will be set up
     * 2) An image download thread will kick in.
     */
    public void buildCarousal() {
        currentStartIndex = 0;
        if (carousalItems != null && carousalItems.size() > 0) {
            if (carousalItems.size() == 1) {
                prepareVariablesForCarousalAndShow(carousalItems.get(currentStartIndex), null);
            } else {
                prepareVariablesForCarousalAndShow(carousalItems.get(currentStartIndex), carousalItems.get(currentStartIndex + 1));
            }
        }
    }

    /**
     * All Item variables are set here. After this showCarousal is hit.
     *
     * @param leftItem
     * @param rightItem
     */
    private void prepareVariablesForCarousalAndShow(CarousalItem leftItem, CarousalItem rightItem) {
        if (this.leftItem == null) {
            this.leftItem = new CarousalItem();
        }
        if (this.rightItem == null) {
            this.rightItem = new CarousalItem();
        }
        if (leftItem != null) {
            this.leftItem = leftItem;
            leftItemTitle = leftItem.getTitle();
            leftItemDescription = leftItem.getDescription();
            // ToDo : Bitmap leftItemBitmap = ......
        }
        if (rightItem != null) {
            this.rightItem = rightItem;
            rightItemTitle = rightItem.getTitle();
            rightItemDescription = rightItem.getDescription();
            // ToDo : Bitmap rightItemBitMap = ......
        }
        showCarousal();
    }

    /**
     * final function which displays carousal. Make sure carousalItems and pending intents are
     * set before calling this method. Otherwise noting will happen.
     */
    private void showCarousal() {

        if (carousalItems != null && carousalItems.size() > 0) {

            if (carousalSetUp == null || carousalSetUp.carousalNotificationId != carousalNotificationId) {
                //First save this set up into a carousal setup item
                carousalSetUp = saveCarousalSetUp();
            } else {
                carousalSetUp.currentStartIndex = currentStartIndex;
                carousalSetUp.leftItem = leftItem;
                carousalSetUp.rightItem = rightItem;
            }
            //First set up all the icons
            setUpCarousalIcons();
            setUpCarousalTitles();

            bigView = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.carousal_notification_item);

            //set up what needs to be visible and what not in the carousal
            setUpCarousalVisibilities(bigView);


            //For dummy purposes
            if (currentStartIndex % 2 == 0) {
                // Locate and set the Image into customnotificationtext.xml ImageViews
                bigView.setImageViewResource(R.id.ivImageLeft, R.drawable.how_it_works_host_manage_calender);
                bigView.setImageViewResource(R.id.ivImageRight, R.drawable.how_it_works_host_requests);
            } else {
                bigView.setImageViewResource(R.id.ivImageLeft, R.drawable.how_it_works_host_video);
                bigView.setImageViewResource(R.id.ivImageRight, R.drawable.how_its_works_icon);
            }

            bigView.setImageViewBitmap(R.id.ivCarousalAppIcon, largeIcon);

            // Locate and set the Text into customnotificationtext.xml TextViews
            bigView.setTextViewText(R.id.tvCarousalTitle, bigContentTitle);
            bigView.setTextViewText(R.id.tvCarousalContent, bigContentText);
            bigView.setTextViewText(R.id.tvRightTitleText, rightItemTitle);
            bigView.setTextViewText(R.id.tvRightDescriptionText, rightItemDescription);
            bigView.setTextViewText(R.id.tvLeftTitleText, leftItemTitle);
            bigView.setTextViewText(R.id.tvLeftDescriptionText, leftItemDescription);


            setPendingIntents(bigView);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context);


            mBuilder.setContentTitle(contentTitle).setContentText(contentText)
                    .setSmallIcon(smallIconResourceId).setLargeIcon(largeIcon)
                    .setPriority(notificationPriority);

            foregroundNote = mBuilder.build();
            foregroundNote.bigContentView = bigView;

            // now show notification..
            NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyManager.notify(carousalNotificationId, foregroundNote);
        } else {
            Log.e(TAG, "Empty item array or of length less than 2");
        }

    }


    /**
     * Handles visibilities of different items based upon availability of content
     *
     * @param bigView
     */
    private void setUpCarousalVisibilities(RemoteViews bigView) {
        if (carousalItems.size() < 3) {
            bigView.setViewVisibility(R.id.ivArrowLeft, View.GONE);
            bigView.setViewVisibility(R.id.ivArrowRight, View.GONE);
        } else {
            bigView.setViewVisibility(R.id.ivArrowLeft, View.VISIBLE);
            bigView.setViewVisibility(R.id.ivArrowRight, View.VISIBLE);
        }
        if (carousalItems.size() < 2) {
            bigView.setViewVisibility(R.id.llRightItemLayout, View.GONE);
        } else {
            bigView.setViewVisibility(R.id.llRightItemLayout, View.VISIBLE);
        }
        if (TextUtils.isEmpty(bigContentText)) {
            bigView.setViewVisibility(R.id.tvCarousalContent, View.GONE);
        } else {
            bigView.setViewVisibility(R.id.tvCarousalContent, View.VISIBLE);
        }
        if (TextUtils.isEmpty(bigContentTitle)) {
            bigView.setViewVisibility(R.id.tvCarousalTitle, View.GONE);
        } else {
            bigView.setViewVisibility(R.id.tvCarousalTitle, View.VISIBLE);
        }
        if (TextUtils.isEmpty(leftItemTitle)) {
            bigView.setViewVisibility(R.id.tvLeftTitleText, View.GONE);
        } else {
            bigView.setViewVisibility(R.id.tvLeftTitleText, View.VISIBLE);
        }
        if (TextUtils.isEmpty(leftItemDescription)) {
            bigView.setViewVisibility(R.id.tvLeftDescriptionText, View.GONE);
        } else {
            bigView.setViewVisibility(R.id.tvLeftDescriptionText, View.VISIBLE);
        }
        if (TextUtils.isEmpty(rightItemTitle)) {
            bigView.setViewVisibility(R.id.tvRightTitleText, View.GONE);
        } else {
            bigView.setViewVisibility(R.id.tvRightTitleText, View.VISIBLE);
        }
        if (TextUtils.isEmpty(rightItemDescription)) {
            bigView.setViewVisibility(R.id.tvRightDescriptionText, View.GONE);
        } else {
            bigView.setViewVisibility(R.id.tvRightDescriptionText, View.VISIBLE);
        }

    }

    /**
     * Sets all titles/texts if they are null
     * They are set to app Icon if that is available. Otherwise at last are left as they are
     */
    private void setUpCarousalTitles() {
        if (TextUtils.isEmpty(contentTitle)) {
            setContentTitle(CarousalUtilities.carousalGetApplicationName(context));
        }

        if (bigContentTitle == null)
            bigContentTitle = "";
        if (bigContentText == null)
            bigContentText = "";
    }

    /**
     * Sets all bitmaps if they are null
     * They are set to app Icon if that is available. Otherwise at last are left as they are
     */
    private void setUpCarousalIcons() {
        if (appIcon != null) {
            if (largeIcon == null) {
                largeIcon = appIcon;
            }
            if (caraousalPlaceholder == null) {
                caraousalPlaceholder = appIcon;
            }
        } else {
            appIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_carousal_icon);
            if (largeIcon == null) {
                largeIcon = appIcon;
            }
            if (caraousalPlaceholder == null) {
                caraousalPlaceholder = appIcon;
            }
        }
        if (smallIconResourceId < 0) {
            smallIconResourceId = CarousalUtilities.carousalGetAppIconResourceId(context);
        }
        if (smallIconResourceId < 0) {
            smallIconResourceId = R.drawable.ic_carousal_icon;
        }
    }

    /**
     * creates pending intents for the clickable regions of the notification
     */
    private void setPendingIntents(RemoteViews bigView) {
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
     *
     * @param eventClicked : integer id of the region clicked
     * @return pendingIntent for the same
     */
    private PendingIntent getPendingIntent(int eventClicked) {
        Intent carousalIntent = new Intent(CarousalConstants.CAROUSAL_EVENT_FIRED_INTENT_FILTER);
        Bundle bundle = new Bundle();
        bundle.putInt(CarousalConstants.EVENT_CAROUSAL_ITEM_CLICKED_KEY, eventClicked);
        bundle.putParcelable(CarousalConstants.CAROUSAL_SET_UP_KEY, carousalSetUp);
        carousalIntent.putExtras(bundle);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, eventClicked, carousalIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pIntent;
    }

    /**
     * saves current set up into a {@link CarousalSetUp} object and returns it
     * this object is passed between broadcast receiver and this instance
     * @return
     */
    private CarousalSetUp saveCarousalSetUp() {
        setUpfilePathOfImages();
        CarousalSetUp cr = new CarousalSetUp (carousalItems, contentTitle, contentText,
                 bigContentTitle, bigContentText, carousalNotificationId,
         currentStartIndex, smallIconPath,  smallIconResourceId,
         largeIconPath,  placeHolderImagePath, leftItem, rightItem);
        return cr;
    }

    /**
     * If exists, it saves files into external directory and saves corresponding file path.
     */
    private void setUpfilePathOfImages() {
        if (smallIcon != null) {
            smallIconPath = CarousalUtilities.carousalSaveBitmapToInternalStorage(context, smallIcon,
                    CarousalConstants.CAROUSAL_SMALL_ICON_FILE_NAME);
        }
        if (largeIcon != null) {
            largeIconPath = CarousalUtilities.carousalSaveBitmapToInternalStorage(context, largeIcon,
                    CarousalConstants.CAROUSAL_LARGE_ICON_FILE_NAME);
        }
        if (caraousalPlaceholder != null) {
            placeHolderImagePath = CarousalUtilities.carousalSaveBitmapToInternalStorage(context, caraousalPlaceholder,
                    CarousalConstants.CAROUSAL_PLACEHOLDER_ICON_FILE_NAME);
        }
    }


    /**
     * Clears notification and empty's references if exists. Important to clear previous carousal
     * before starting a new one.
     *
     * @return
     */
    public Carousal clearCarousalIfExists() {
        if (carousalItems != null) {
            carousalItems.clear();
            // now cancel notification..
            NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyManager.cancel(carousalNotificationId);
        }
        //ToDo :  delete all cache files
        return this;
    }


    //========================================= HANDLING EVENTS ===================================//

    /**
     * Handles all click events from the carousal notification.
     *
     * @param clickEvent
     */
    public void handleClickEvent(int clickEvent, CarousalSetUp setUp) {
        //First we need to make sure that the set up is there. If the original instance is
        // killed and it is a new one, we need to instantiate all the values from setUp object
        verifyAndSetUpVariables(setUp);

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

    /**
     * It first checks if it is a new instance and not the old one. If its the case, it just resets all
     * the values using the setup object
     * @param setUp : {@link CarousalSetUp} object and carries the original set data.
     */
    private void verifyAndSetUpVariables(CarousalSetUp setUp) {
        //If it a new instance carousal stUp will be null
        if (carousalSetUp == null) {
            carousalItems = setUp.carousalItems;
            contentTitle = setUp.contentTitle;
            contentText = setUp.contentText;
            bigContentTitle = setUp.bigContentTitle;
            bigContentText = setUp.bigContentText;
            carousalNotificationId = setUp.carousalNotificationId;
            currentStartIndex = setUp.currentStartIndex;
            notificationPriority = setUp.notificationPriority;
            smallIconPath = setUp.smallIcon;
            largeIconPath = setUp.largeIcon;
            placeHolderImagePath = setUp.caraousalPlaceholder;
            leftItem = setUp.leftItem;
            rightItem = setUp.rightItem;


            setUpBitCarousalBitmapsFromSetUp();

        } else if (carousalSetUp != null && carousalNotificationId != setUp.carousalNotificationId) {
            carousalSetUp = null;
            verifyAndSetUpVariables(setUp);
        }
    }

    /**
     * If exists it loads bitmaps from file directory and saves them.
     */
    private void setUpBitCarousalBitmapsFromSetUp() {
        if (smallIconPath != null) {
            smallIcon = CarousalUtilities.carousalLoadImageFromStorage(smallIconPath, CarousalConstants.CAROUSAL_SMALL_ICON_FILE_NAME);
        }
        if (largeIconPath != null) {
            largeIcon = CarousalUtilities.carousalLoadImageFromStorage(largeIconPath, CarousalConstants.CAROUSAL_LARGE_ICON_FILE_NAME);
        }
        if (placeHolderImagePath != null) {
            caraousalPlaceholder = CarousalUtilities.carousalLoadImageFromStorage(placeHolderImagePath, CarousalConstants.CAROUSAL_PLACEHOLDER_ICON_FILE_NAME);
        }
    }

    private void onRightItemClicked() {
       sendItemClickedBroadcast(rightItem);
    }

    private void onLeftItemClicked() {
        sendItemClickedBroadcast(leftItem);

    }

    private void sendItemClickedBroadcast (CarousalItem cItem) {
        Intent i = new Intent();
        i.setAction(CarousalConstants.CAROUSAL_ITEM_CLICKED_INTENT_FILTER);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CAROUSAL_ITEM_CLICKED_KEY, cItem);
        i.putExtras(bundle);

        context.getApplicationContext().sendBroadcast(i);


            try {

                clearCarousalIfExists();
            } catch ( Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Unable To send notification's pendingIntent");
            }


    }

    /**
     * Here we choose the items for left and right an call prepareVariablesForCarousalAndShow() thereafter
     */
    private void onLeftArrowClicked() {

        if (carousalItems != null && carousalItems.size() > currentStartIndex) {

            switch (currentStartIndex) {

                case 1:
                    currentStartIndex = carousalItems.size() - 1;
                    prepareVariablesForCarousalAndShow(carousalItems.get(currentStartIndex), carousalItems.get(0));
                    break;
                case 0:
                    currentStartIndex = carousalItems.size() - 2;
                    prepareVariablesForCarousalAndShow(carousalItems.get(currentStartIndex), carousalItems.get(currentStartIndex + 1));
                    break;
                default:
                    currentStartIndex -= 2;
                    prepareVariablesForCarousalAndShow(carousalItems.get(currentStartIndex), carousalItems.get(currentStartIndex + 1));
                    break;
            }
        } else {
            Toast.makeText(context, "CLicked" + "Empty Array" , Toast.LENGTH_LONG).show();
        }
    }

    private void onRightArrowClicked() {
        if (carousalItems != null && carousalItems.size() > currentStartIndex) {

            int difference = carousalItems.size() - currentStartIndex;
            switch (difference) {
                case 3:
                    currentStartIndex += 2;
                    prepareVariablesForCarousalAndShow(carousalItems.get(currentStartIndex), carousalItems.get(0));
                    break;
                case 2:
                    currentStartIndex = 0;
                    prepareVariablesForCarousalAndShow(carousalItems.get(0), carousalItems.get(1));
                    break;
                case 1:
                    currentStartIndex = 1;
                    prepareVariablesForCarousalAndShow(carousalItems.get(currentStartIndex), carousalItems.get(currentStartIndex + 1));
                    break;
                default:
                    currentStartIndex += 2;
                    prepareVariablesForCarousalAndShow(carousalItems.get(currentStartIndex), carousalItems.get(currentStartIndex + 1));
                    break;
            }
        }
    }

}
