package com.si.styletimer.utill;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.Build;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.si.styletimer.R;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utility {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final String TAG = Utility.class.getSimpleName();
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static String PREFERENCES = "user";
    private static boolean hasImmersive;
    private static boolean cached = false;
    private static Dialog dialog;
    public static String DATEAFTER = "after";
    public static String DATEBEFORE = "before";
    public static String DATEEQUAL = "equal";
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    public static void hideKeyboard(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        View focusedView = ((Activity) mContext).getCurrentFocus();
        if (focusedView != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void fullScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    public static String getPriceSend(String price) {
        /*if(price != null && !price.equals("")) {
            Log.e(TAG, "getPriceReplaceDotWithComma: " + price );
            String sss[] = price.split("\\.");
            // String pp = "";
            Log.e(TAG, "getPriceReplaceDotWithComma: " + price + " "+sss.length );
            if (sss.length > 0) {
                if (sss[1].equals("0")) {
                    return sss[0].replaceAll("\\.", "\\,");
                } else {
                    return price.replaceAll("\\.", "\\,");
                }
            } else {
                return price.replaceAll("\\.", "\\,");
                // return pp;
            }
        }else {
            return "";
        }*/
         if(price != null && !price.equals("")) {
             return price.replaceAll("\\.", "\\,");
         }else {
             return "";
         }

    }
    public static String getPriceReplaceDotWithComma(String price) {
        //Log.e(TAG, "getPriceReplaceDotWithComma: given " + price );
        if (price != null && !price.equals("")) {
            double ddd = Double.parseDouble(price);
            String s = String.valueOf(ddd);
            String sss[] = s.split("\\.");
            if (sss.length > 0) {
                if (sss[1].equals("0") || sss[1].equals("00")) {
                    return sss[0];
                } else {
                    return separatorPrice(ddd);
                }
            } else {
                return separatorPrice(ddd);
            }
        }else {
            return "0";
        }
    }
    public static String separatorPrice(double dd) {
        if (dd>0) {
            //Log.e(TAG, "getPriceReplaceDotWithComma: return " + price );
            return new DecimalFormat("####0.00").format(dd).replaceAll("\\.", "\\,");
        } else {
            return "0";
        }
    }
    public static double returnDiscountDouble(String price) {
        if (price != null && !price.equals("")) {
            return Double.parseDouble(price);
        } else {
            return 0;
        }
    }

    public static String dateBuiilder(int year, int month, int day) {
        StringBuilder strMonth = new StringBuilder();
        StringBuilder strDay = new StringBuilder();
        month = month + 1;
        if (month < 10) {
            strMonth.append("0" + month);
        } else {
            strMonth.append(month);
        }
        if (day < 10) {
            strDay.append("0" + day);
        } else {
            strDay.append(day);
        }
        String myDate = year + "-" + strMonth.toString() + "-" + strDay.toString();
        return myDate;
    }


    public static String dateBuiilder1(int year, int month, int day) {
        StringBuilder strMonth = new StringBuilder();
        StringBuilder strDay = new StringBuilder();

       month = month + 1;
        if (month < 10) {
           // StrM="0"+String.valueOf(month);
            strMonth.append("0" + month);
        } else {
            //StrM= String.valueOf(month);
            strMonth.append(month);
        }
        if (day < 10) {
            strDay.append("0" + day);
        } else {
            strDay.append(day);
        }

        String myDate = strDay.toString() + "-" +strMonth.toString()+ "-" + year;
        return myDate;
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * Determines whether one Location reading is better than the current Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */
    public static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 6;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return false;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return false;
        }
        return false;
    }

    public static Date getDateTimeFromString(String time) {
        Date date = null;
        Date end = null;
        String fact = "";
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String comparedates(String startdate, String enddate) {
        Log.e(TAG, "comparedates() called with: startdate = [" + startdate + "], enddate = [" + enddate + "]");
        String type = "";
        Date start = null;
        Date end = null;

        // start is a present date and end is tomorrow date
        //  0 comes when two date are same,
        //  1 comes when date1 is higher then date2
        // -1 comes when date1 is lower then date2

        try {
            start = new SimpleDateFormat("yyyy-MM-dd").parse(startdate);
            end = new SimpleDateFormat("yyyy-MM-dd").parse(enddate);
            Log.e(TAG, "comparedates: >>> " + start.compareTo(end));
            switch (start.compareTo(end)) {
                case 0:
                    type = DATEEQUAL;
                    break;
                case 1:
                    type = DATEAFTER;
                    break;
                case -1:
                    type = DATEBEFORE;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }


    /**
     * Checks whether two providers are the same
     */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public static String getUUID() {
        return java.util.UUID.randomUUID().toString();
    }


    public static String timeLeft(int delivery_time, String updatedDate) {

        try {

            int minutes = delivery_time;
            long millis_minutes = minutes * 60 * 1000;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = sdf.parse(updatedDate);
            long totalMinutes = millis_minutes + date.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat();
            sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
            long leftMinutes = totalMinutes - getUTCDateTimeAslong();
            return leftMinutes + "";
        } catch (Exception e) {
            Log.e("Error in Utility 63", "" + e);
            e.printStackTrace();
        }
        return "";
    }

    public static String formatDate(String dddd) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dddd);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("d. MMM yyyy", Locale.GERMAN);     //todo---> ("EEEE d MMM") for(MONDAY 28 MAY)
        String date = df.format(date1);
        return date;
    }

    public static String formatDateWithDayName(String dddd) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd",Locale.GERMAN).parse(dddd);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("EEEE, d. MMM yyyy",Locale.GERMAN);     //todo---> ("EEEE d MMM") for(MONDAY 28 MAY)
       // String date = df.format(String.format(String.valueOf(Locale.GERMAN),date1));
        String date = df.format(date1);

        return date;
    }


    public static long getUTCDateTimeAslong() {

        Date dateTime1 = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("IST"));
            Date date = new Date();
            SimpleDateFormat dateParser = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            dateTime1 = null;
            try {
                dateTime1 = dateParser.parse(format.format(date));
            } catch (ParseException e) {
                Log.e("Error in Utility 84", "" + e);
                e.printStackTrace();
            }
        } catch (java.text.ParseException e) {
            Log.e("Error in Utility 88", "" + e);
            e.printStackTrace();
        }

        return dateTime1.getTime();
    }

    public static Integer rendomGenrate() {
        int random = (int) (Math.random() * 50 + 1);
        return random;
    }

    public static void hideKeyboardNew(AppCompatActivity activity) {
        setupParent(activity.getWindow().getDecorView().findViewById(android.R.id.content), activity);
    }

    //Hide Keyboard
    public static void setupParent(View view, Context context) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                Utility.hideKeyboard(context);
                //view.performClick();
                return false;
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupParent(innerView, context);
            }
        }
    }

    public static void logLargeString(String str) {

        if (str.length() > 3000) {

            System.out.print(str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            System.out.print(str);
        }
    }

    public static int getNavBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @SuppressLint("NewApi")
    public static boolean hasImmersive(Context ctx) {

        if (!cached) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                hasImmersive = false;
                cached = true;
                return false;
            }
            Display d = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);

            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            hasImmersive = (realWidth > displayWidth) || (realHeight > displayHeight);
            cached = true;
        }

        return hasImmersive;
    }

    public static String getDeviceType(Context mContext) {
        String ua = new WebView(mContext).getSettings().getUserAgentString();

        if (ua.contains("Mobile")) {

            System.out.println("Type:Mobile");
            return "ANDROID MOBILE";
            // Your code for Mobile
        } else {
            // Your code for TAB
            System.out.println("Type:TAB");
            return "ANDROID TAB";
        }

    }

    public static Boolean write(String fname, String fcontent) {
        try {

            String fpath = "/sdcard/" + fname + ".txt";

            File file = new File(fpath);

            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();

            Log.d("Suceess", "Sucess");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static Boolean writehtml(String fname, String fcontent) {
        try {

            String fpath = "/sdcard/" + fname + ".html";

            File file = new File(fpath);

            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();

            Log.d("Suceess", "Sucess");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static String getFormatedDate(String strDate, String sourceFormate,
                                         String destinyFormate) {
        SimpleDateFormat df;
        df = new SimpleDateFormat(sourceFormate);
        Date date = null;
        try {
            date = df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        df = new SimpleDateFormat(destinyFormate);
        return df.format(date);
    }

    public static String getFirstDayofWeek() {
        // String str=""+Calendar.getInstance().get(field);
        String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = new Date();
        try {
            myDate = sdf.parse(str);
        } catch (ParseException pe) {
            // Do Something
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        System.out.println("getFirstDayofWeek:" + sdf.format(cal.getTime()));
        return sdf.format(cal.getTime());
    }

    public static String DateName(String dddd) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dddd);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("MMMM d");     //todo---> ("EEEE d MMM") for(MONDAY 28 MAY)
        String date = df.format(date1);
        return date;
    }

    public static String getDBCurrentDate() {
        return new SimpleDateFormat("HHmmssZ").format(new Date());
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    public static String getCurrentDateMM() {
        return new SimpleDateFormat("MM").format(new Date());
    }

    public static String getCurrentDateYY() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String getCurrentYear() {
        return new SimpleDateFormat("yyyy").format(new Date());
    }

    public static String getTommDateYY() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String getCurrentMMddyy() {
        return new SimpleDateFormat("MM-dd-yy").format(new Date());
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    public static String getLastDayofWeek() {
        // String str="2015-05-12";
        String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = new Date();
        try {
            myDate = sdf.parse(str);
        } catch (ParseException pe) {
            // Do Something
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.set(Calendar.DAY_OF_WEEK, 7);
        return sdf.format(cal.getTime());
    }


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = "1755018674765832";
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public static void setBooleanPreferences(Context context, String key,
                                             boolean isCheck) {
        SharedPreferences setting = (SharedPreferences) context
                .getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean(key, isCheck);
        editor.commit();
    }

    public static boolean getBooleanPreferences(Context context, String key) {
        SharedPreferences setting = (SharedPreferences) context
                .getSharedPreferences(PREFERENCES, 0);
        return setting.getBoolean(key, false);
    }

    public static void setStringPreferences(Context context, String key,
                                            String value) {
        SharedPreferences setting = (SharedPreferences) context
                .getSharedPreferences(PREFERENCES, 0);

        SharedPreferences.Editor editor = setting.edit();

        editor.putString(key, value);
        editor.commit();

    }

    public static String getStringPreferences(Context context, String key) {

        SharedPreferences setting = (SharedPreferences) context
                .getSharedPreferences(PREFERENCES, 0);
        return setting.getString(key, "");

    }

    public static void setIntegerPreferences(Context context, String key,
                                             int value) {
        SharedPreferences setting = (SharedPreferences) context
                .getSharedPreferences(PREFERENCES, 0);

        SharedPreferences.Editor editor = setting.edit();

        editor.putInt(key, value);
        editor.commit();

    }

    public static void clearAllSharedPreferences(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                PREFERENCES, Context.MODE_PRIVATE).edit();


        editor.clear();
        editor.commit();
    }


    public static void clearStringFromclearAllSharedPreferences(Context context, String string) {


        SharedPreferences.Editor editor = context.getSharedPreferences(
                PREFERENCES, Context.MODE_PRIVATE).edit();


        if (string != null) {
            editor.putString(string, null);
        } else {
            editor.remove(string);
        }


        editor.commit();

    }


    public static int getIntegerPreferences(Context context, String key) {

        SharedPreferences setting = (SharedPreferences) context
                .getSharedPreferences(PREFERENCES, 0);
        return setting.getInt(key, 0);

    }

    public static void showAlert(Context mContext, String title, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(msg);
        // Set behavior of negative button

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });

        AlertDialog alert = builder.create();
        try {
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAlert(Context mContext, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle(mContext.getString(R.string.app_name));
        builder.setMessage(msg);
        // Set behavior of negative button

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });

        AlertDialog alert = builder.create();
        try {
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showProgressHUD(Context context, String title,
                                       String message) {
        try {

            if (title == null)
                title = "";
            if (message == null)
                message = "";
            dialog = ProgressDialog.show(context, title, message);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showProgressHUD(Context context) {
        try {
            dialog = ProgressDialog.show(context, "", "Please Wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showProgressHUD(Context context, String message) {
        try {
            if (message == null)
                message = "";
            dialog = ProgressDialog.show(context, context.getString(R.string.app_name), message);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideProgressHud() {
        try {
            if (dialog != null)
                dialog.cancel();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getCurrentMMddyyyy() {

        return new SimpleDateFormat("MM-dd-yyyy").format(new Date());
    }

    public static void Alert_NoFilter() {

    }

    public static boolean connectionStatus(Context mContext) {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {

            NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();

            if (activeNetwork != null) {

                Log.e(TAG, "activeNetwork.getTypeName(); " + activeNetwork.getTypeName());
                if (activeNetwork.getTypeName().equalsIgnoreCase("WIFI")) {


                    if (activeNetwork.isConnectedOrConnecting()) {
                        return true;
                    } else {
                        return false;
                    }


                } else if (activeNetwork.getTypeName().equalsIgnoreCase("MOBILE")) {

                    if (activeNetwork.isConnectedOrConnecting()) {
                        return true;
                    } else {
                        return false;
                    }

                } else {

                    return false;

                }
            }
        }
        return false;
    }

    /**
     * String myBase64Image = encodeToBase64(myBitmap, Bitmap.CompressFormat.JPEG, 100);
     * Bitmap myBitmapAgain = decodeBase64(myBase64Image);
     *
     * @param compressFormat
     * @param quality
     * @return
     */
    public static String encodeToBase64(Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP);
    }

    /**
     * String myBase64Image = encodeToBase64(myBitmap, Bitmap.CompressFormat.JPEG, 100);
     * Bitmap myBitmapAgain = decodeBase64(myBase64Image);
     *
     * @param input
     * @return
     */
    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static String getCompleteAddressthroughLatLngString(Context mContext, double LATITUDE, double LONGITUDE) {

        String strAdd = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
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

   /* public static HashMap<String, Object> getModeltoMap(Object object) {
        Gson gson = new Gson();
        String temp = gson.toJson(object);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = (HashMap<String, Object>) gson.fromJson(temp, map.getClass());
        return map;
    }*/

    public static boolean isValidEmailAddress(String emailAddress) {
        String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();


    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static boolean isValidPassword(String Password) {
        //String expression = "^(?=.*[a-zA-Z0-9@#$%^&+=])(?=.*[0-9@#$%^&+=]).{8,}$";
        String expression = "^(?=.*[a-zA-Z0-9]).{8,}$";  //--TOdo ws like -->  "^(?=.*[a-zA-Z])(?=.*[0-9@#$%^&+=]).{8,}$"
        CharSequence inputStr = Password;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();

    }

    /************website field validation***************/
    public static boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        if (m.matches())
            return true;
        else
            return false;
    }


    public static String roundoff(Double amount) {
        return String.format("%.2f", amount);
    }


    public static String makeSHA1Hash(String input)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        byte[] buffer = input.getBytes("UTF-8");
        md.update(buffer);
        byte[] digest = md.digest();

        String hexStr = "";
        for (int i = 0; i < digest.length; i++) {
            hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
        }
        return hexStr;
    }

    public static void fullscreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void nointernetmsg(final View v, final Activity activity) {
        Snackbar snackbar = Snackbar
                .make(v, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                        activity.startActivity(intent);
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public static String isValid(String passwordhere) {

        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
        String error = "";

        if (passwordhere.length() < 8) {
            error = "Password lenght must have alleast 8 character !!";
        }
        if (!specailCharPatten.matcher(passwordhere).find()) {
            error = "Password must have atleast one specail character !!";
        }
        if (!UpperCasePatten.matcher(passwordhere).find()) {
            error = "Password must have atleast one uppercase character !!";
        }
        if (!lowerCasePatten.matcher(passwordhere).find()) {
            error = "Password must have atleast one lowercase character !!";
        }
        if (!digitCasePatten.matcher(passwordhere).find()) {
            error = "Password must have atleast one digit character !!";
        }
        return error;
    }

    public static boolean isSpecailCharPatten(String paswword) {
        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        return !specailCharPatten.matcher(paswword).find();
    }

    public static boolean isUpperCasePatten(String paswword) {
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        return !UpperCasePatten.matcher(paswword).find();
    }

    public static boolean isLowerCasePatten(String password) {
        Pattern lowerCasePatten = Pattern.compile("[a-z]");
        return !lowerCasePatten.matcher(password).find();
    }

    public static boolean isDigitCasePatten(String password) {
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
        return !digitCasePatten.matcher(password).find();
    }

    public static boolean isContainspain(String password) {
        Pattern digitCasePatten = Pattern.compile("[ ^([6-9]{1})([0-9]{9})]");
        return !digitCasePatten.matcher(password).find();
    }

    public static void nointernettoast(Context context) {
        Toast.makeText(context, "Kein Internet, Bitte überprüfe deine Verbindung", Toast.LENGTH_LONG).show();
    }

    public static LinearLayoutManager linearLayoutManager(Context context) {
        LinearLayoutManager linearLayout = new LinearLayoutManager(context);
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayout;
    }

    public static boolean isFirst(Context context) {
        final SharedPreferences reader = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        final boolean first = reader.getBoolean("is_first", true);
        if (first) {
            final SharedPreferences.Editor editor = reader.edit();
            editor.putBoolean("is_first", false);
            editor.commit();
        }
        return first;
    }

    public static String round_off(String value) {
        double datavalue = Double.parseDouble(value);
        double newKB = Math.round(datavalue * 100D) / 100D;
        String correctvalue = String.valueOf(newKB);
        return correctvalue;
    }

    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }


    public static double round1(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        double value1= (value/1000);
        return (double) Math.round(value1 * scale) / scale;
    }
    public static String timezone() {
        Date date = new Date();
        Log.e(TAG, "timezone:date " + date);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        Log.e(TAG, "Date and time in Madrid: " + df.format(date));
        return df.format(date);
    }

    public static String changetime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ti = "";
        try {
            Date date1 = format.parse(time);
            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
            Log.e(TAG, "Date and time in Madrid: " + df.format(date1));
            ti = df.format(time);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ti;

    }

    public static String current_time() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }

    public static String current_time_informat(String format) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }


    public static boolean isontainspain(String password) {
        Pattern digitCasePatten = Pattern.compile("[/^(?:6\\,7\\,9)/]");
        return !digitCasePatten.matcher(password).find();
    }

    public static boolean checkChar(EditText editText) {
        char x = editText.getText().toString().charAt(0);

        if (x == '6' || x == '7' || x == '9')
            return true;

        return false;
    }

    public static String capitalize(String capString) {

        // String chars = capitalize("hello dream world");  Output: Hello Dream World

        if (capString != null && !capString.equals("")) {
            StringBuffer capBuffer = new StringBuffer();
            Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
            while (capMatcher.find()) {
                capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
            }

            return capMatcher.appendTail(capBuffer).toString();
        } else {
            return "";
        }
    }

    public static int days() {
        int correct_day = 0;
//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));

        Calendar calendar = Calendar.getInstance();
        int intdays = calendar.get(Calendar.DAY_OF_WEEK);
        Log.e(TAG, "days: " + intdays);
        switch (intdays) {
            case 1:
                correct_day = 6;
                break;
            case 2:
                correct_day = 0;
                break;
            case 3:
                correct_day = 1;
                break;
            case 4:
                correct_day = 2;
                break;
            case 5:
                correct_day = 3;
                break;
            case 6:
                correct_day = 4;
                break;
            case 7:
                correct_day = 5;
                break;
        }

        return correct_day;

    }

    public static String days_from_date(String datee) {
        String correct_day = "";
        Calendar calendar = Calendar.getInstance();
        String dobSplit[] = datee.split("-");
        calendar.set(Integer.parseInt(dobSplit[0]), Integer.parseInt(dobSplit[1]) - 1, Integer.parseInt(dobSplit[2]));
        Log.e(TAG, "days_from_date: " + Integer.parseInt(dobSplit[0]) + Integer.parseInt(dobSplit[1]) + Integer.parseInt(dobSplit[2]));
        int intdays = calendar.get(Calendar.DAY_OF_WEEK);
        switch (intdays) {
            case 1:
                correct_day = "7";
                break;
            case 2:
                correct_day = "1";
                break;
            case 3:
                correct_day = "2";
                break;
            case 4:
                correct_day = "3";
                break;
            case 5:
                correct_day = "4";
                break;
            case 6:
                correct_day = "5";
                break;
            case 7:
                correct_day = "6";
                break;
        }

        Log.e(TAG, "days_from_date: " + correct_day);
        return correct_day;

    }

    public static String getDay(String day, String month, String year) {
        String[] dates = new String[]{"SUNDAY", "MONDAY", "TUESDAY", //
                "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(year), //
                Integer.parseInt(month) - 1, // <-- add -1
                Integer.parseInt(day));
        int date_of_week = cal.get(Calendar.DAY_OF_WEEK);
        return dates[date_of_week - 1];
    }

    public static String comma(String data) {
        String abc = data;
        abc = abc.replace(".", ",") + " €";
        return abc;
    }

    public static String normal(String data) {
        String abc = data + " €";
        return abc;
    }

    public static String reverse_string(String string) {
        String str[] = string.split("\\s*,\\s*");
        String finalStr = "";
        for (int i = str.length - 1; i >= 0; i--) {
            finalStr += str[i] + ",";
        }
        return finalStr;
    }

    /**
     * This method returns true if the collection is null or is empty.
     *
     * @param collection
     * @return true | false
     */
    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * This method returns true of the map is null or is empty.
     *
     * @param map
     * @return true | false
     */
    public static boolean isEmpty(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * This method returns true if the objet is null.
     *
     * @param object
     * @return true | false
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        return false;
    }

    /**
     * This method returns true if the input array is null or its length is zero.
     *
     * @param array
     * @return true | false
     */
    public static boolean isEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * This method returns true if the input string is null or its length is zero.
     *
     * @param string
     * @return true | false
     */
    public static boolean isEmpty(String string) {
        if (string == null || string.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String toDate(String dateeeee) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateeeee);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(date1);

    }

    public static String toTime(String timeeee) {

        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeeee);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(date1);
    }

    public static String toDateTime(String timeeee) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(timeeee);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date1);
    }

    public static String change_format(String timeeee, String input_frotmat, String output_fromat) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat(input_frotmat).parse(timeeee);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat(output_fromat, Locale.GERMAN);
        //  DateFormat df = new SimpleDateFormat(output_fromat,Locale.ENGLISH);
        return df.format(date1);
    }

    public static Long change_format_long(String timeeee, String input_frotmat, String output_fromat) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat(input_frotmat).parse(timeeee);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat(output_fromat);
        //return df.format(date1);
        return date1.getTime();
    }


   /* public static String startdate(String dat, String lang) {
        Date date1 = null;
        String corretfroamt = "";

        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DateFormat monthformat = new SimpleDateFormat("MMM");
        DateFormat dateforamt = new SimpleDateFormat("dd, yyyy");
        String month = monthformat.format(date1);
        String dateff = dateforamt.format(date1);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<LanguageModelDatum> datumRealmResults = realm.where(LanguageModelDatum.class).findAll();
        LanguageModelDatum languageModelDatum = datumRealmResults .where().equalTo("mTagName",month).findFirst();

        String strmonth = "";
        if (languageModelDatum!=null){
            if (lang.equals(LanguageConstant.SPASISH)) {
                strmonth = languageModelDatum.getSp();
                corretfroamt = strmonth + " "+ dateff;
                return corretfroamt;
            } else {
                strmonth = languageModelDatum.getEn();
                corretfroamt = strmonth + " "+ dateff;
                return corretfroamt;
            }
        }

       return corretfroamt;
    }*/

 /*   public static String enddate(String end, String lang) {
        Date date1 = null;
        String corretfroamt = "";

        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        DateFormat monthformat = new SimpleDateFormat("MMM");
        DateFormat dateforamt = new SimpleDateFormat("dd, yyyy");
        String month = monthformat.format(date1);
        String dateff = dateforamt.format(date1);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<LanguageModelDatum> datumRealmResults = realm.where(LanguageModelDatum.class).findAll();
        LanguageModelDatum languageModelDatum = datumRealmResults .where().equalTo("mTagName",month).findFirst();

        String strmonth = "";
        if (languageModelDatum!=null){
            if (lang.equals(LanguageConstant.SPASISH)) {
                strmonth = languageModelDatum.getSp();
                corretfroamt = strmonth + " "+ dateff;
                return corretfroamt;
            } else {
                strmonth = languageModelDatum.getEn();
                corretfroamt = strmonth + " "+ dateff;
                return corretfroamt;
            }
        }

        return corretfroamt;

    }
*/

    public static String server_format(String dddd) {
        SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM");
        String date = df.format(Date.parse(dddd));
        return date;
    }

    public static String getDays() {
        Calendar c = new GregorianCalendar();
        c.add(Calendar.DATE, 90);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        Date d = c.getTime();
        df2.format(d);
        return df2.format(d);
    }

    public static List<Date> getDates() {

        // get the last date
        Calendar c = new GregorianCalendar();
        c.add(Calendar.DATE, 90);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        Date d = c.getTime();
        df2.format(d);

        // Get calendar set to current date and time
        Calendar catt = Calendar.getInstance();
        // Set the calendar to Sunday of the current week
        catt.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        // Print dates of the current week starting on Sunday
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(df.format(catt.getTime()));
//        for (int i = 0; i < 7; i++) {
//            System.out.println(df.format(catt.getTime()));
//            catt.add(Calendar.DATE, 1);
//        }

        //String dateString1 = getCurrentDateYY();
        String dateString1 = df.format(catt.getTime());
        String dateString2 = df2.format(d);

        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }

        String daata = dates.toString();
        return dates;
    }

    public static String app_format(String dddd) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dddd);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        DateFormat df = new SimpleDateFormat("EEE, d MMM", Locale.GERMAN); //todo.. use EEEE and MMMM 4 time for full spelling of month and day
        String date = df.format(date1);
        return date;
    }

    public static boolean checkGPSStatus(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return statusOfGPS;
    }

    ;

    public static String date_format(String dddd) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dddd);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        //DateFormat df = new SimpleDateFormat("d MMMM"); //todo.. use EEEE and MMMM 4 time for full spelling of month and day
        DateFormat df = new SimpleDateFormat("dd MMMM"); //todo.. use EEEE and MMMM 4 time for full spelling of month and day
        String date = df.format(date1);
        return date;
    }

    public static String cal_spanish_WeekDay(String s) {
        StringBuilder builder = new StringBuilder();
        if (s.contains("1")) {
            builder.append("Lun,");
        }
        if (s.contains("2")) {
            builder.append("Mar,");
        }
        if (s.contains("3")) {
            builder.append("Mié,");
        }
        if (s.contains("4")) {
            builder.append("Jue,");
        }
        if (s.contains("5")) {
            builder.append("Vie,");
        }
        if (s.contains("6")) {
            builder.append("Sáb,");
        }
        if (s.contains("7")) {
            builder.append("Dom");
        }
        return builder.toString().replaceAll(",$", "");
    }

    public static String calWeekDay(String s) {
        StringBuilder builder = new StringBuilder();
        if (s.contains("1")) {
            builder.append("Mon,");
        }
        if (s.contains("2")) {
            builder.append("Tue,");
        }
        if (s.contains("3")) {
            builder.append("Wed,");
        }
        if (s.contains("4")) {
            builder.append("Thu,");
        }
        if (s.contains("5")) {
            builder.append("Fri,");
        }
        if (s.contains("6")) {
            builder.append("Sat,");
        }
        if (s.contains("7")) {
            builder.append("Sun");
        }
        return builder.toString().replaceAll(",$", "");
    }

    public static boolean isSpecialCharacter(Character c) {
        return c.toString().matches("[^a-z A-Z0-9]");
    }

    public static boolean password(String c) {
        return c.matches("[^(?=.*@\\d)(?=.*@[a-z])(?=.*@[A-Z]).{4,8}$]");
    }

    public static boolean isSpanishnumber(Character c) {
        if (c.toString().equals("6")) {
            return true;
        } else if (c.toString().equals("7")) {
            return true;
        } else if (c.toString().equals("9")) {
            return true;
        } else {
            return false;
        }
    }

    public static String extractYoutubeVideoId(String ytUrl) {
        String vId = null;
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(ytUrl);

        if (matcher.find()) {
            vId = matcher.group();
        }
        Log.e(TAG, "extractYoutubeVideoId: " + vId);
        return vId;

    }

    public static String checktime(String startf, String endd) {
        Date start = null;
        Date end = null;
        String fact = "";
        try {
            start = new SimpleDateFormat("HH:mm").parse(startf);
            end = new SimpleDateFormat("HH:mm").parse(endd);

            if (start.after(end)) {
                return fact = "after";
            }

            if (start.before(end)) {
                return fact = "before";
            }

            if (start.equals(end)) {
                return fact = "equal";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fact;
    }

    public static Date stringtoTime(String time) {
        Date date = null;
        Date end = null;
        String fact = "";
        try {
            date = new SimpleDateFormat("HH:mm").parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDate(String dates) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dates);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**************wdullar timepicker and datepicker dialog *******************************************************/

  /*  public static Timepoint[] generateTimepoints(int minutesInterval) {
        List<Timepoint> timepoints = new ArrayList<>();

        for (int minute = 0; minute <= 1440; minute += minutesInterval) {
            int currentHour = minute / 60;
            int currentMinute = minute - (currentHour > 0 ? (currentHour * 60) : 0);
            if (currentHour == 24)
                continue;
            timepoints.add(new Timepoint(currentHour, currentMinute));
        }
        return timepoints.toArray(new Timepoint[timepoints.size()]);
    }*/
    public static Float set_rating(Float rate) {
        Float correct_rating = 0.0f;

        if (rate > 0 && rate < 1) {
            correct_rating = 0.5f;
        } else if (rate == 1) {
            correct_rating = 1f;
        } else if (rate > 1 && rate <= 1.99) {
            correct_rating = 1.5f;
        } else if (rate == 2) {
            correct_rating = 2f;
        } else if (rate > 2.00 && rate <= 2.99) {
            correct_rating = 2.5f;
        } else if (rate == 3.00) {
            correct_rating = 3f;
        } else if (rate > 3.00 && rate <= 3.99) {
            correct_rating = 3.5f;
        } else if (rate == 4.00) {
            correct_rating = 4f;
        } else if (rate > 4.00 && rate <= 4.99) {
            correct_rating = 4.5f;
        } else if (rate == 5) {
            correct_rating = 5f;
        }

        return correct_rating;
    }

    public static void setTimePickerInterval(TimePicker timePicker) {
        final Calendar c = Calendar.getInstance();
        final int minute = c.get(Calendar.MINUTE);
        Log.e(TAG, "setTimePickerInterval: " + minute);

        try {
            int TIME_PICKER_INTERVAL = 5;
            NumberPicker minutePicker = (NumberPicker) timePicker.findViewById(Resources.getSystem().getIdentifier(
                    "minute", "id", "android"));
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            minutePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            int cbc = setval(minute);
            minutePicker.setValue(cbc);
            List<String> displayedValues = new ArrayList<String>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minutePicker.setDisplayedValues(displayedValues.toArray(new String[0]));
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }
    }

    public static String getTomorrow() {
        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        Log.e(TAG, "initviews: " + tomorrow);
        return new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);
    }


    public static int setval(int min) {
        int correct_min = 0;

        if (min > 0 && min < 5) {
            correct_min = 0;
        } else if (min > 5 && min < 10) {
            correct_min = 1;
        } else if (min > 10 && min < 15) {
            correct_min = 2;
        } else if (min > 15 && min < 20) {
            correct_min = 3;
        } else if (min > 20 && min < 25) {
            correct_min = 4;
        } else if (min > 25 && min < 30) {
            correct_min = 5;
        } else if (min > 30 && min < 35) {
            correct_min = 6;
        } else if (min > 35 && min < 40) {
            correct_min = 7;
        } else if (min > 40 && min < 45) {
            correct_min = 8;
        } else if (min > 45 && min < 50) {
            correct_min = 9;
        } else if (min > 50 && min < 55) {
            correct_min = 10;
        } else if (min > 55 && min < 60) {
            correct_min = 11;
        }
        Log.e(TAG, "setval: " + correct_min);
        return correct_min;

    }

    // gson list
    //  parkTypeModelList = gson.fromJson(data.toString(), new TypeToken<ArrayList<ParkTypeModel>>() {}.getType());

  /*  using to stop spinner selection from selecting ******************************************
    private Boolean mIsSpinnerFirstCall = true;

     binding.toolbarimagebtn.spOrderStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if(!mIsSpinnerFirstCall) {

            }else {

                mIsSpinnerFirstCall = false;

            }*/



    /*  pointHistoryModelList = gson.fromJson(jsonObject.getJSONArray("data").toString(),
                                    new TypeToken<ArrayList<PointHistoryModel>>() {
                                    }.getType());

                                    */


    public static String getGooglePlayStoreUrl(Activity activity) {
        String id = activity.getApplicationInfo().packageName; // current google
        // play is using
        // package name
        // as id
        return "market://details?id=" + id;
    }

    public static String getCurrentVersion(Activity activity) {
        // int currentVersionCode = 0;
        String versionname = "";
        PackageInfo pInfo;
        try {
            pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            // currentVersionCode = pInfo.versionName;
            versionname = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // return 0
        }
        return versionname;
    }

    public static void onTextUnderLine(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }


    /*public static void loadImageView(ImageView view, String imageUrl) {
        Log.e(TAG, "loadImage() ImageView = [" + view + "], imageUrl = [" + imageUrl + "]");
        if (imageUrl.isEmpty()) {
            imageUrl = "https://share.okdothis.com/assets/defaultfood-avatar-c246f74ff56d382424ac2c750e40b0a6.png?w=64&h=64";
            imageUrl.replaceAll(" ", "%20");
            Picasso.get().load(imageUrl).placeholder(R.drawable.defaultus).fit().error(R.drawable.defaultus).into(view);
        } else {
            imageUrl.replaceAll(" ", "%20");
            Picasso.get().load(imageUrl).placeholder(R.drawable.defaultus).fit().error(R.drawable.defaultus).into(view);
        }
    }

    public static void loadCircleImageView(CircleImageView view, String imageUrl) {
        imageUrl.replaceAll(" ", "%20");
        // Log.e(TAG, "loadImage() CircleImageView  = [" + view + "], imageUrl = [" + imageUrl+ "]");
        Picasso.get().load(imageUrl).placeholder(R.drawable.defaultus).error(R.drawable.defaultus).into(view);

    }

    public static void loadfileImage(ImageView view, File imageUrl) {
        Log.e(TAG, "loadImage() ImageView = [" + view + "], imageUrl = [" + imageUrl + "]");
        if (imageUrl.exists()) {
            // imageUrl = "https://share.okdothis.com/assets/defaultfood-avatar-c246f74ff56d382424ac2c750e40b0a6.png?w=64&h=64";
            //  imageUrl.replaceAll(" ", "%20");
            Picasso.get().load(imageUrl).placeholder(R.drawable.defaultus).fit().error(R.drawable.defaultus).into(view);
        } else {
            // imageUrl.replaceAll(" ", "%20");
            Picasso.get().load(imageUrl).placeholder(R.drawable.defaultus).fit().error(R.drawable.defaultus).into(view);
        }
    }*/

    public static void show_loader(ConstraintLayout layout) {
        layout.setVisibility(View.VISIBLE);
    }

    public static void hide_loader(ConstraintLayout layout) {
        layout.setVisibility(View.GONE);
    }

    public static long timeleft(String creeteond) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(creeteond);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Date today = new Date();
        long abc = (date1.getTime() - today.getTime());
        //long newtask = (86400000 - abc);
        Log.e(TAG, "timeleft: " + abc + " " + today.getTime() + " " + date1.getTime());
        return abc;
    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = new Date().getTime();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return "vor " + diff / DAY_MILLIS + " Tagen";
        }
    }

}
