package com.si.styletimer.firebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService  extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";

    private Context context;
    NotificationHelper helper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        context = getApplicationContext();
        helper=new NotificationHelper(context);

      //  Log.e(TAG, "onMessageReceived: not "+ remoteMessage.getNotification() );

        Log.e(TAG, "onMessageReceived: "+ remoteMessage.getData().toString() );

        if (NotificationHelper.isAppIsInBackground(context)) {
         //   Log.e(TAG, "onMessageReceived: >>>>>>>>>>>>>>>>>>>>>>>>>");
            String body = remoteMessage.getNotification().getBody();
            String title = remoteMessage.getNotification().getTitle();
             String clickAction = remoteMessage.getNotification().getClickAction();

           // String body =  remoteMessage.getData().get("title");
          // String title = remoteMessage.getData().get("title");
            String salonId = remoteMessage.getData().get("salon_id");
            String bookId = remoteMessage.getData().get("book_id");
            String bookingStatus = remoteMessage.getData().get("booking_status");
           // String clickAction =  remoteMessage.getData().get("click_action");
            helper.notifyFromBackground(title, body, salonId, bookId, bookingStatus, clickAction);
        } else {
           // Log.e(TAG, "onMessageReceived: <<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            try {
                Log.e(TAG, remoteMessage.getData().toString());
                helper.make_notifiocation(
                        remoteMessage.getData().get("title"),
                        remoteMessage.getData().get("body"),
                        remoteMessage.getData().get("salon_id"),
                        remoteMessage.getData().get("book_id"),
                        remoteMessage.getData().get("booking_status"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
