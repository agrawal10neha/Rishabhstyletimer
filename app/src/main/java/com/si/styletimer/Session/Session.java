package com.si.styletimer.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    public static final String MyPREFERENCES = "sessionHandling";

    private static final String token = "tokenToAccess";
    private static final String userId = "userId";
    private static final String stylername = "stylername";
    private static final String isadded = "isadded";
    private static final String salon_id = "salon_id";
    private static final String ServiceIds = "ServiceIds";
    private static final String salonBanner = "salonBanner";

    private static final String lat = "latt";
    private static final String lan = "lang";

    private static final String add = "address";
    private static final String passw = "passWord";
    private static final String emailid = "emailid";

    private static final String img_path = "imagePath";
    private static final String profile = "profile";
    private static final String online_booking = "online_booking";

    private static SharedPreferences sharedpreferences;
    private static SharedPreferences.Editor editor;
    private static final String LOGIN_WITH = "login_with";

    //-------
    public static String getLoginWith(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(Session.LOGIN_WITH, "");
    }

    public static void setLoginWith(Context context, String login_with) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(Session.LOGIN_WITH, login_with);
        editor.apply();
    }

    public static String getSalon_id(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(salon_id, "");
    }

    public static void setSalon_id(Context context, String token) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(salon_id, token);
        editor.apply();
    }

    public static String getSalonBanner(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(salonBanner, "");
    }

    public static void setSalonBanner(Context context, String token) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(salonBanner, token);
        editor.apply();
    }

    public static String getServiceIds(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(ServiceIds, "");
    }

    public static void setServiceIds(Context context, String token) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(ServiceIds, token);
        editor.apply();
    }

    public static String getOnlineBooking(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(online_booking, "");
    }

    public static void setOnlineBooking(Context context, String token) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(online_booking, token);
        editor.apply();
    }


    public static String getIsadded(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(isadded, "");
    }

    public static void setIsadded(Context context, String token) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(isadded, token);
        editor.apply();
    }


    public static String getStylername(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(stylername, "");
    }

    public static void settylername(Context context, String token) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(stylername, token);
        editor.apply();
    }

    public static String getAccessToken(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(Session.token, "");
    }

    public static void setAccessToken(Context context, String token) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(Session.token, token);
        editor.apply();
    }

    //-------

    public static String getUid(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(Session.userId, "");
    }

    public static void setUid(Context context, String userId) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(Session.userId, userId);
        editor.apply();
    }
    public static String getEmailID(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(Session.emailid, "");
    }

    public static void setEmailID(Context context, String emailid) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(Session.emailid, emailid);
        editor.apply();
    }
    //-------



    public static String getLat(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(Session.lat, "");
    }

    public static void setLat(Context context, String lat) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(Session.lat, lat);
        editor.apply();
    }

    //-------

    public static String getLang(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(Session.lan, "");
    }

    public static void setLang(Context context, String lan) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(Session.lan, lan);
        editor.apply();
    }

    //-------

    public static String getCurrentAdd(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(Session.add, "");
    }

    public static void setCurrentAdd(Context context, String add) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(Session.add, add);
        editor.apply();
    }


    //-------

    public static String getPassword(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(Session.passw, "");
    }

    public static void setPassword(Context context, String passw) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(Session.passw, passw);
        editor.apply();
    }

    //-------


    public static String getImagePath(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(Session.img_path, "");
    }

    public static void setImagePath(Context context, String img_path) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(Session.img_path, img_path);
        editor.apply();
    }

    public static String getProfile(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(Session.profile, "");
    }

    public static void setProfile(Context context, String profile) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(Session.profile, profile);
        editor.apply();
    }
    //-------



    public static void clearSession(Context context) {
        editor.clear();
        editor.apply();
        //getSharedPreferences(Session.MyPREFERENCES, Context.MODE_PRIVATE).edit().clear().apply();
    }
    public static String getPreferenceName(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("google", "");
    }

    public static void setPreferenceName(Context context, String uid) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString("google", uid);
        editor.apply();
    }

    public static String getPreferenceNameTwo(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("googletwo", "");
    }

    public static void setPreferenceNameTwo(Context context, String uid) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString("googletwo", uid);
        editor.apply();
    }
}
