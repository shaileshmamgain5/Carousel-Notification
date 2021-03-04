package in.mamga.carousalnotification;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

/**
 * It contains basic information abour carousal once it is built.
 * Purpose is to reset implements Parcelable {@link Carousal} instance in case it is killed during the process
 * Created by Shailesh on 08/01/17.
 */

public class CarousalSetUp implements Parcelable {

    public ArrayList<CarousalItem> carousalItems;

    public String contentTitle;
    public String contentText; //title and text while it is small
    public String bigContentTitle;
    public String bigContentText; //title and text when it becomes large
    public int carousalNotificationId = 9873715; //Random id for notification. Will cancel any
    // notification that have existing same id.

    public  int currentStartIndex = 0; //Variable that keeps track of where the startIndex is
    public int notificationPriority = NotificationCompat.PRIORITY_DEFAULT;
    public String smallIcon;
    public int smallIconResourceId = -1; //check before setting it that it does exists
    public String largeIcon;
    public String caraousalPlaceholder;
    public CarousalItem leftItem;
    public CarousalItem rightItem;
    public boolean isOtherRegionClickable = false;
    public boolean isImagesInCarousal = true;

    public CarousalSetUp() {
        //default set up
    }

    public CarousalSetUp (ArrayList<CarousalItem> carousalItems, String contentTitle, String contentText,
                          String bigContentTitle, String bigContentText, int carousalNotificationId,
                          int currentStartIndex,String smallIcon, int smallIconResourceId,
                          String largeIcon, String caraousalPlaceholder, CarousalItem leftItem,
                          CarousalItem rightItem, boolean isOtherRegionClickable, boolean isImagesInCarousal) {
        this.carousalItems = carousalItems;
        this.contentTitle = contentTitle;
        this.contentText = contentText;
        this.bigContentTitle = bigContentTitle;
        this.bigContentText = bigContentText;
        this.carousalNotificationId = carousalNotificationId;
        this.currentStartIndex = currentStartIndex;
        this.smallIcon = smallIcon;
        this.smallIconResourceId = smallIconResourceId;
        this.largeIcon = largeIcon;
        this.caraousalPlaceholder = caraousalPlaceholder;
        this.leftItem = leftItem;
        this.rightItem = rightItem;
        this.isOtherRegionClickable = isOtherRegionClickable;
        this.isImagesInCarousal = isImagesInCarousal;
    }



    protected CarousalSetUp(Parcel in) {
        if (in.readByte() == 0x01) {
            carousalItems = new ArrayList<CarousalItem>();
            in.readList(carousalItems, CarousalItem.class.getClassLoader());
        } else {
            carousalItems = null;
        }
        contentTitle = in.readString();
        contentText = in.readString();
        bigContentTitle = in.readString();
        bigContentText = in.readString();
        carousalNotificationId = in.readInt();
        currentStartIndex = in.readInt();
        notificationPriority = in.readInt();
        smallIcon = in.readString();
        smallIconResourceId = in.readInt();
        largeIcon = in.readString();
        caraousalPlaceholder = in.readString();
        leftItem = (CarousalItem) in.readValue(CarousalItem.class.getClassLoader());
        rightItem = (CarousalItem) in.readValue(CarousalItem.class.getClassLoader());
        isOtherRegionClickable = in.readByte() != 0x00;
        isImagesInCarousal = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (carousalItems == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(carousalItems);
        }
        dest.writeString(contentTitle);
        dest.writeString(contentText);
        dest.writeString(bigContentTitle);
        dest.writeString(bigContentText);
        dest.writeInt(carousalNotificationId);
        dest.writeInt(currentStartIndex);
        dest.writeInt(notificationPriority);
        dest.writeString(smallIcon);
        dest.writeInt(smallIconResourceId);
        dest.writeString(largeIcon);
        dest.writeString(caraousalPlaceholder);
        dest.writeValue(leftItem);
        dest.writeValue(rightItem);
        dest.writeByte((byte) (isOtherRegionClickable ? 0x01 : 0x00));
        dest.writeByte((byte) (isImagesInCarousal ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CarousalSetUp> CREATOR = new Parcelable.Creator<CarousalSetUp>() {
        @Override
        public CarousalSetUp createFromParcel(Parcel in) {
            return new CarousalSetUp(in);
        }

        @Override
        public CarousalSetUp[] newArray(int size) {
            return new CarousalSetUp[size];
        }
    };
}