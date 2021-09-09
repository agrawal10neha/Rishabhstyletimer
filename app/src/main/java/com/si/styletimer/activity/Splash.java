package com.si.styletimer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import androidx.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivitySplashBinding;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Animatoo;
import com.si.styletimer.utill.GPSTracker;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {
    private Context context;
    private static final String TAG = "Splash";
    private LocationManager locationManager;
    Boolean check = true;
    public static String latitude = "";
    private ActivitySplashBinding binding;
    public static String longitude = "";
    SharedPreferences settings;
    boolean firstRun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.fullscreen(Splash.this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        context = this;
        settings = getSharedPreferences("prefs", 0);
        firstRun = settings.getBoolean("firstRun", false);
        initViews();
    }

    private void initViews(){

        //Log.e(TAG, "initView: "+ Utility.getPriceSend("2000.04"));

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

       // Log.e(TAG, "initViews: hhhhhhhhhhhhh " + dpHeight+" wwwwwwwwwwwwwwwww "+ dpWidth );
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        Locale locale = new Locale("de");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

       //permission();
       if (Session.getUid(context).equals("")){
           if (Utility.connectionStatus(context)) {
               genrate_accessToken();
           } else {
               Toast.makeText(context, "Kein Internet, Bitte überprüfe deine Verbindung", Toast.LENGTH_LONG).show();

           }
        }else {

           new Handler().postDelayed(this::redirectToDashboard, 3000);
       }
    }

    private void genrate_accessToken() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);

        RetrofitServices.urlServices.generate_access_token(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    String respo = response.body().string().trim();
                    Log.e("-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {
                        Session.setAccessToken(context, jsonObject.getString("access_token"));
                        if (firstRun == false) {
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("firstRun", true);
                            editor.commit();
                            Intent intent = new Intent(context, WalkthroughActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            Animatoo.animateSlideLeft(context);
                        } else {
                            redirectToDashboard();
                        }

                    } else if (jsonObject.getInt("status") == 0) {
                        Toast.makeText(context, "Something went Wrong..", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("-->", "FALURE " + t);
            }
        });
    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

            getLocation2();
        } else {

            getLocation2();
        }
    }

    private void getLocation2() {
        GPSTracker gps = new GPSTracker(context);

        if (gps.canGetLocation()) {

            Session.setLat(context, String.valueOf(gps.getLatitude()));
            Session.setLang(context, String.valueOf(gps.getLongitude()));

            Log.e("getLatitude(-=-----=>)", "" + gps.getLatitude());
            Log.e("getLongitude(-=-----=>)", "" + gps.getLongitude());

            getAddress(gps.getLatitude(), gps.getLongitude());

        } else {
            gps.showSettingsAlert();
        }
    }

    public String getAddress(final double latitude, final double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {

                Address address = addresses.get(0);

                for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++) {
                    if (i == addresses.get(0).getMaxAddressLineIndex()) {
                        result.append(addresses.get(0).getAddressLine(i));
                    } else {
                        result.append(addresses.get(0).getAddressLine(i)).append(",");
                    }
                }
                String addreshidden = result.toString();

                Session.setCurrentAdd(context, addreshidden);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }


    private void redirectToDashboard() {
     /*   Intent intent = new Intent(context, WalkthroughActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Animatoo.animateSlideLeft(context);*/
        Intent in = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(in);
        finish();
    }
}
