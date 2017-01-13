# Carousal Notification

Carousal-Notification lets you make carousal type notification where user can navigate within the notification. Selected item can be obtained using a broadcast receiver.
**Features Existing**

1. Quick and easy set up with just a few lines of code.
2. Compatible with API levels 16 and above.
3. Creates three types of notifications – carousel with two images, carousel with text and an image and text only carousal
4. The images are automatically cached for subsequent lookups.
6. Supports deep linking. Each message click takes user to the respective landing page in the app.
7. Attach custom data with notification. It could be the campaign tracking data or anything else. This data will be delivered to the app when it is launched on notification click.

**Upcoming Features**

1. Slide animation on carousal
2. Schedule Carousal. As of now you have to write a job schedular/alarm manager to scedular to schedule Carousal.

# Screenshots

<img src="https://raw.githubusercontent.com/shaileshmamgain5/Carousal-Notification/master/app/screenshots/carousal.png" width="200" height="350" />
<img src="https://raw.githubusercontent.com/shaileshmamgain5/Carousal-Notification/master/app/screenshots/carousal-notification.gif" width="200" height="350" />


# Install

For gradle, go to your app.gradle file and inside ` dependencies{}` add :

         compile 'in.mamga:carousalnotification:1.0'

Thats it, you are good to go.

#  Usage

A correct flow to use this library would be :
1) A notification type that will tell you a carousal has to be shows.
2) Upon recieving this notification download the dadata to be shows. (view service for example)
3) Once you have the data, you can build a carousal notification and notify it.
Of course all this can be started from the notification receiver only. But that will make the notification data too bulky to be send over GCM/ FCM.
4) A reciever to recieve events from carousal notification need to written where the library will send carousal item's information whenever one is clicked.

Here is the **detailed steps**. It is assumed that you have fetched the data that need to be shown:

Step 1) Build Carousal instance.

         Carousal carousal = Carousal.with(this).beginTransaction();

Step 2) Set 'contentTitle' and 'contentText' which will be shown when notification is small. Its similar to normal notifications.

         carousal.setContentTitle("Your Title Here").setContentText("Your content description here");
         
Step 3) Build the 'CarousalItem's need to be shown. One way is to parse through the list of fetched data to be shown and build and add carousal items one by one. View documentation for more info.

          CarousalItem cItem = new CarousalItem("Item Id here", "Item Title", "Item Content","Picture Url");
          
          //Additionally we can set a type to it. It is useful if we are showing more than one type
          //of data in carousal. so that we know, where to go when an item is clicked.
          cItem.setType(TYPE_QUOTE);
            
           //Now add item to the carousal
           carousal.addCarousalItem(cItem);
           
   Now this is the interesting part. All of the parts of CarousalItems are optional, lets go through them one by one
   
   1)** Id **  : You may want to save unique id of the object so that you may know which object is clicked on carousal. You may use this id to further fetch details about the item. 
   Alternatively, you may save the entire object's json string into id if you don't have (or want to) individual item's detail api.
   
   2) ** Title ** : Visible below the image of the carousal. Leave it null if you don't want to show any title.
   3) ** Content ** : Visible below the title. Leave it null if you don't want to show any content.
