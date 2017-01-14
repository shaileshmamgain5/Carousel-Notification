# Carousel Notification

Carousel-Notification lets you make carousal type notification where user can navigate within the notification. Selected item can be obtained using a broadcast receiver.

**Features Existing**

1. Quick and easy set up with just a few lines of code.
2. Compatible with API levels 16 and above.
3. Creates three types of notifications – carousel with two images, carousel with text and an image and text only carousel
4. The images are automatically cached for subsequent lookups.
6. Supports deep linking. Each message click takes user to the respective landing page in the app.
7. Attach custom data with notification. It could be the campaign tracking data or anything else. This data will be delivered to the app when it is launched on notification click.

**Upcoming Features**

1. Slide animation on carousel
2. Schedule Carousel. As of now you have to write a job schedular/alarm manager to scedular to schedule Carousal.
3. Explore carousel notification using a service.

**Requirements**

1. "android.permission.INTERNET" required in manifest.

# Screenshots

<img src="https://raw.githubusercontent.com/shaileshmamgain5/Carousal-Notification/master/app/screenshots/carousal.png" width="200" height="350" />
<img src="https://raw.githubusercontent.com/shaileshmamgain5/Carousal-Notification/master/app/screenshots/carousal-notification.gif" width="200" height="350" />


# Install

For gradle, go to your app.gradle file and inside ` dependencies{}` add :

         compile 'in.mamga:carousalnotification:1.0'

Thats it, you are good to go.

#  Usage

A correct flow to use this library would be :
1) A notification type that will tell you a carousel has to be shows.
2) Upon recieving this notification download the dadata to be shows. (view service for example)
3) Once you have the data, you can build a carousel notification and notify it.
Of course all this can be started from the notification receiver only. But that will make the notification data too bulky to be send over GCM/ FCM.
4) A reciever to recieve events from carousel notification need to written where the library will send carousal item's information whenever one is clicked.

Here is the **detailed steps**. It is assumed that you have fetched the data that need to be shown:

**Step 1)** Build Carousel instance.

         Carousal carousal = Carousal.with(this).beginTransaction();

**Step 2)** Set 'contentTitle' and 'contentText' which will be shown when notification is small. Its similar to normal notifications.

         carousal.setContentTitle("Your Title Here").setContentText("Your content description here");
         
**Step 3)** Build the 'CarouselItem's need to be shown. One way is to parse through the list of fetched data to be shown and build and add carousel items one by one. View documentation for more info.

          CarousalItem cItem = new CarousalItem("Item Id here", "Item Title", "Item Content","Picture Url");
          
          //Additionally we can set a type to it. It is useful if we are showing more than one type
          //of data in carousal. so that we know, where to go when an item is clicked.
          cItem.setType(TYPE_QUOTE);
            
           //Now add item to the carousal
           carousal.addCarousalItem(cItem);
           
   Now this is the interesting part. All of the parts of CarousalItems are optional, lets go through them one by one
   
   **a) Id**  : You may want to save unique id of the object so that you may know which object is clicked on carousel. You may use this id to further fetch details about the item. 
   Alternatively, you may save the entire object's json string into id if you don't have (or want to) individual item's detail api.
   
  **b) Title** : Visible below the image of the carousel. Leave it null if you don't want to show any title.
    
  **c) Content** : Visible below the title. Leave it null if you don't want to show any content.
  **d) Image Url** : Url of the image to be shown in carousal item. leave it null if there is no image
  
  If your carousal have images only, leave title and content null (say display only top images of the day). Alternatively, if your notification have rich content and no image (say top news today), just set image_url to null and show as much as content you want.
  
  **e) Type** : If you have more than one type of carousel notification to be shown whose click event needs to be handled differently.Say one tell you top destinations to visit and other tells to top new offers. You me set a type to the item to know which type of carousal item is clicked inside the reciever.
  
  
  **Step 4)** Add big Notification title and content (optional)
  
         carousal.setBigContentTitle("Quotes from everywhere!").setBigContentText("Notice these random quotes from around the world");

  Optionally you may also want to let user click the notification outside the carousal itself. This will trigger an broadcast even when the user clicks other regions (Title and content of notifiction or Small notification.
  
          carousal.setOtherRegionClickable(true);  //by default it is false
         
  **Step 5)** Dispatch notification
  
         carousal.buildCarousal();
         
    
         


This is it!  A carousel with specified items will be show. Now if you want to handle the click of an item as well (which normally should be the case), you may add a broadcast reciever for that.
  
  **Step 6)** Make a broadcast receiver and handle carousel click. Whenever user clicks a carousel item(or other region, if it is enabled), a broadcast is sent to it which will contain the carouselItem data that is clicked. The bundle will be null if other region is clicked and not any carousel item itself.
  
         public class CarousalItemClickReceiver extends BroadcastReceiver {

         @Override
             public void onReceive(Context context, Intent intent) {
                 Bundle bundle = intent.getExtras();
                  if (bundle != null) {  //meaning some item is clicked
                    //Get the carousal item that is clicked . Use the same key.
                     CarousalItem item = bundle.getParcelable(Carousal.CAROUSAL_ITEM_CLICKED_KEY);
                     if (item != null) {
                         //Now we need to know where to redirect event
                           String id = item.getId();
                           //Now start an intent or anything else from here
                      
                     } else {  //Meaning other region is clicked and isOtherRegionClick is set to true.
                               //Again handle by anything suitable here.
                         Toast.makeText(context, "Other region clicked", Toast.LENGTH_LONG).show();
                     }

                 }
             }
         }
         
    **Step 7)** Register the broadcast receiver. Add the receiver with intent filter in the manifest file: 
    
         <?xml version="1.0" encoding="utf-8"?>
         <manifest ...>
             <application...>
                 ...
                 <receiver android:name=".CarousalItemClickReceiver">
                     <intent-filter>
                         <action android:name="in.mamga.CAROUSALNOTIFICATIONITEMCLICKED" />
                     </intent-filter>
                 </receiver>

                 ...
             </application>
             </manifest>
             
     
# Contributors

* Contribute to the library by 
 1. Forking it and enhancing it
 2. Report issues and bugs
 3. Fork and make a better library :)

**Contact Us**

Get in touch with me with your suggestions, thoughts and queries at shaileshmamgain5@gmail.com
