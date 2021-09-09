package com.si.styletimer.utill;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.si.styletimer.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class FileDownloader {

    private static final String TAG = "FileDownloader";

    //private static final String TAG = "Download Task";
    private Context context;

    private String downloadUrl = "", downloadFileName = "";
    private ProgressDialog progressDialog;
    private static final String CHANNEL_ID = "3000";
    private static final int DOWNLOAD_NOTIFICATION_ID_DONE = 911;
    private String orderid="";


    public FileDownloader(Context context, String downloadUrl) {
        this.context = context;
        this.downloadUrl = downloadUrl;
        downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf( '/' ),downloadUrl.length());//Create file name by picking download file name from URL
        Log.e(TAG, downloadFileName);

        //Start Downloading Task
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(context);
            progressDialog.setMessage("Downloading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Downloaded Successfully", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            openFile(outputFile);
                        }
                    },2000);

                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }


            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }


                //Get File if SD card is present
                if (new CheckForSDCard().isSDCardPresent()) {

                    apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + "Borobear_Invoice");
                } else
                    Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created."+"--->"+downloadFileName + "  -->"+apkStorage);
                }

               // outputFile = new File(apkStorage, downloadFileName+Utility.getCurrentTime()+".pdf");//Create Output file in Main File
                outputFile = new File(apkStorage, "Borobearinvoice.pdf");//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e(TAG, "File Created  -->" +outputFile);

                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }

    private void openFile(File outputFile) {
        showNotification(outputFile);
    }


    public class CheckForSDCard {
        //Check If SD Card is present or not method
        public boolean isSDCardPresent() {
            if (Environment.getExternalStorageState().equals(

                    Environment.MEDIA_MOUNTED)) {
                return true;
            }
            return false;
        }
    }

    private void showNotification(@NonNull File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri fileUri = FileProvider.getUriForFile(
                context,
                context.getString(R.string.file_provider_authorities),
                file);

        intent.setDataAndType(fileUri, "application/pdf");

        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info : resInfoList) {
            context.grantUriPermission(
                    info.activityInfo.packageName,
                    fileUri, FLAG_GRANT_WRITE_URI_PERMISSION | FLAG_GRANT_READ_URI_PERMISSION);
        }

        NotificationCompat.Builder notificationBuilder;
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(createChannel());
            }
            notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            notificationBuilder = new NotificationCompat.Builder(context);
        }

        notificationBuilder
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.notification_image_saved_click_to_preview))
                .setTicker(context.getString(R.string.notification_image_saved))
                .setSmallIcon(R.drawable.ic_notification)
                .setOngoing(false)
                .setContentIntent(PendingIntent.getActivity(context, 0, intent, 0))
                .setAutoCancel(true);
        if (notificationManager != null) {
            notificationManager.notify(DOWNLOAD_NOTIFICATION_ID_DONE, notificationBuilder.build());
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public NotificationChannel createChannel() {
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, context.getString(R.string.channel_name), importance);
        channel.setDescription(context.getString(R.string.channel_description));
        channel.enableLights(true);
        channel.setLightColor(Color.YELLOW);
        return channel;
    }


}
