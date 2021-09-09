package com.si.styletimer.firebase;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;

import com.si.styletimer.R;
import com.si.styletimer.activity.BookingDetailActivity;
import com.si.styletimer.utill.Utility;

import java.util.List;

public class NotificationHelper {
    private static final String TAG = "NotificationHelper";

    public static final String PRIMARY_CHANNEL = "default";
    private Context context;
    private NotificationManagerCompat managerCompat;
    private NotificationCompat.Builder mBuilder;
    private Intent intent;
    private  Uri defaultSoundUri ;
    public NotificationHelper(Context context) {
        this.context = context;
        createNotificationChannel();


    }

    private void createNotificationChannel() {
        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            CharSequence name = "Default";
            String description = "Default";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.CYAN);
            channel.setShowBadge(true);
            channel.setSound(defaultSoundUri,attributes);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            mBuilder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL);
        } else {
            mBuilder = new NotificationCompat.Builder(context);
        }
    }

    public void make_notifiocation(String notificationTitle,
                                   String notificationBody, String sId, String bId, String status) {
        playNotificationSound();

        int res = Utility.rendomGenrate();
        if(bId!=null && !bId.equals("")) {
            Log.e(TAG, "make_notifiocation: bId = " + bId);
            String x="";

            if(status.equals("completed")){
                x="CompletedFragment";

            }else if(status.equals("confirmed")){
                x="ConfirmedFragment";

            }else if(status.equals("cancelled")){
                x="CancelledFragment";

            }
            intent = new Intent(context, BookingDetailActivity.class);
            intent.putExtra("Fragment",x);
            intent.putExtra("id",bId);
            intent.putExtra("from","notif");
            intent.putExtra("sId","");
            intent.putExtra("salonId",sId);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      //  PendingIntent pendingIntent = PendingIntent.getActivity(context, res, intent, 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_ONE_SHOT);


        mBuilder
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationBody))
                .setNumber(1)
                .setSmallIcon(R.drawable.notifican_new_new)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_main_round))
                .setAutoCancel(true)
                .setLights(Color.CYAN, 3000, 3000)
                .setSound(defaultSoundUri)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentIntent(pendingIntent);
        getManager().notify(res, mBuilder.build());
      //  Settings.System.DEFAULT_NOTIFICATION_URI
    }


    public void notifyFromBackground(String notificationTitle, String notificationBody,
                                     String sId, String bId, String status,String clickAction) {

        Log.e(TAG, "notifyFromBackground() called with: notificationTitle = [" + notificationTitle + "], notificationBody = [" + notificationBody + "], sId = [" + sId + "], bId = [" + bId + "], status = [" + status + "], clickAction = [" + clickAction + "]");

        playNotificationSound();

        int res = Utility.rendomGenrate();

        if(bId!=null && !bId.equals("")) {
            Log.e(TAG, "notifyFromBackground: bId = " + bId);
            String x="";

            if(status.equals("completed")){
                x="CompletedFragment";

            }else if(status.equals("confirmed")){
                x="ConfirmedFragment";

            }else if(status.equals("cancelled")){
                x="CancelledFragment";

            }
            intent = new Intent(clickAction);
            intent.putExtra("Fragment",x);
            intent.putExtra("id",bId);
            intent.putExtra("from","notif");
            intent.putExtra("sId","");
            intent.putExtra("salonId",sId);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, res, intent, 0);

        mBuilder
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationBody))
                .setNumber(1)
                .setSmallIcon(R.drawable.notifican_new_new)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_main_round))
                .setAutoCancel(true)
                .setLights(Color.CYAN, 3000, 3000)
                .setSound(defaultSoundUri)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentIntent(pendingIntent);
        getManager().notify(res, mBuilder.build());
        //  Settings.System.DEFAULT_NOTIFICATION_URI
    }

    public static void clearNotifications(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private NotificationManagerCompat getManager() {
        if (managerCompat == null) {
            managerCompat = NotificationManagerCompat.from(context);
        }
        return managerCompat;
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public void playNotificationSound() {
        try {
            Ringtone r = RingtoneManager.getRingtone(context, defaultSoundUri);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
