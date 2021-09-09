package com.si.styletimer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import androidx.databinding.DataBindingUtil;

import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.adapters.DashboardAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivityDashboardBinding;
import com.si.styletimer.firebase.NotificationHelper;
import com.si.styletimer.models.dashboard.DashboardListModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.DatabindingImageAdapter;
import com.si.styletimer.utill.GPSTracker;
import com.si.styletimer.utill.GpsUtils;
import com.si.styletimer.utill.PersmissionUtils;
import com.si.styletimer.utill.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private static final String TAG = "DashboardActivity";
    private Context context;
    private String clicked = "";
    private List<DashboardListModel> dashboardList = new ArrayList<>();
    private Gson gson;

    private int pastVisiblesItems = 0, visibleItemCount = 0, totalItemCount = 0;
    private Boolean loadingMore = false;
    private Boolean viewMore = false;

    private DashboardAdapter adapter;
    private LinearLayoutManager m;
    private GPSTracker gps;
    //  private MarshmallowPermission permission;
    private BroadcastReceiver broadcastReceiver;
    private PersmissionUtils persmissionUtils;

    private Boolean GpsStatus = false;
    private Boolean isGPS;
    private PlacesClient placesClient;
    private List<Place.Field> fields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        context = this;
        gson = new Gson();
        persmissionUtils = new PersmissionUtils(context, DashboardActivity.this);
        persmissionUtils.askLocationPermissions();
        // gps= new GPSTracker(context);
        //   permission = new MarshmallowPermission(context);
        NotificationHelper.clearNotifications(context);

        Log.e("FCMToken", "token " + FirebaseInstanceId.getInstance().getToken());

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.font_grey_light));
        }

        inIt();
        brodCast();


    }

    private void inIt() {

        Locale locale = new Locale("de");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        if (Session.getUid(context) != null && !Session.getUid(context).equals("")) {
            notificationStatus();
        }
        checkGps();
        /*if (permission.check_location_Permission()){
            getLocation2();
        }*/
        if (persmissionUtils.checkLocationPermissions()) {
            gps = new GPSTracker(context);
            if (gps.canGetLocation()) {
                Session.setLat(context, String.valueOf(gps.getLatitude()));
                Session.setLang(context, String.valueOf(gps.getLongitude()));
                Log.e("getLatitude(-=-----=>)", "" + gps.getLatitude());
                Log.e("getLongitude(-=-----=>)", "" + gps.getLongitude());
                getAddress(gps.getLatitude(), gps.getLongitude());
            } else {
                gps.showSettingsAlert();
            }
        } else {
            Log.e(TAG, "onClick: elseee ");
        }

        //setUpPagination();
       /* if (Utility.connectionStatus(context)){
            getDashboardList();
        }else {
            Utility.nointernettoast(context);
        }*/


        binding.llmybookinds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Session.getUid(context).equals("")) {
                    Intent intent = new Intent(context, SellectionActivity.class).putExtra("flag", "dashboard").putExtra("destination", "booking");
                    startActivity(intent);
                    // Bungee.slideLeft(context);
//                    finish();
                } else {
                    startActivity(new Intent(context, MyBookingActivity.class).putExtra("selected", "0"));
                    // Bungee.slideLeft(context);
                }
            }
        });

        binding.ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Session.getUid(context).equals("")) {
                    Intent intent = new Intent(context, SellectionActivity.class).putExtra("flag", "dashboard").putExtra("destination", "setting");
                    startActivity(intent);
                    //   Bungee.slideLeft(context);
//                    finish();
                } else {
                    startActivity(new Intent(context, SettingActivity.class));
                    //  Bungee.slideLeft(context);
                }
            }
        });

        binding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SearchSalonActivity.class));
            }
        });
        binding.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Session.getUid(context).equals("")) {
                    Intent intent = new Intent(context, SellectionActivity.class).putExtra("flag", "dashboard").putExtra("destination", "setting");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, MyProfileActivity.class);
                    startActivity(intent);

                }
            }
        });

        binding.llFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Session.getUid(context).equals("")) {
                    Intent intent = new Intent(context, SellectionActivity.class).putExtra("flag", "dashboard").putExtra("destination", "fav");
                    startActivity(intent);
                    //   Bungee.slideLeft(context);
//                    finish();
                } else {
                    startActivity(new Intent(context, FavouriteActivity.class));
                    // Bungee.slideLeft(context);
                }
            }
        });

    }

    private void brodCast() {

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e(TAG, "onReceive:::::::::::::::::::::::::::::::::::: " + intent.getStringExtra(AppConstants.MAG1));
                if (intent.getStringExtra(AppConstants.MAG1).equals("abc")) {
                    if (intent.getStringExtra(AppConstants.MAG2) != null) {
                        Log.e(TAG, "onReceive:## " + intent.getStringExtra(AppConstants.MAG2));
                        int scrollpos = Integer.parseInt(intent.getStringExtra(AppConstants.MAG2));

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context) {

                                    @Override
                                    protected int getVerticalSnapPreference() {
                                        return LinearSmoothScroller.SNAP_TO_START;
                                    }

                                    private static final float SPEED = 100f;// Change this value (default=25f)

                                    @Override
                                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                                        return SPEED / displayMetrics.densityDpi;
                                    }
                                };
                                smoothScroller.setTargetPosition(scrollpos);
                                m.startSmoothScroll(smoothScroller);

                                //m.smoothScrollToPosition(binding.rv,binding.rv.State(),scrollpos);
                            }
                        }, 200);
                    }
                } else if (intent.getStringExtra(AppConstants.MAG1).equals("gps")) {
                    if (persmissionUtils.checkLocationPermissions()) {
                        if (!Utility.checkGPSStatus(context)) {
                            checkGps();
                        }
                    }
                } else if (intent.getStringExtra(AppConstants.MAG1).equals("profile")) {
                    Log.e(TAG, "onReceive:## " + intent.getStringExtra(AppConstants.MAG1) + "   " + Session.getImagePath(context) + Session.getUid(context) + "/" + Session.getProfile(context));
                    String someFilepath = Session.getImagePath(context) + Session.getUid(context) + "/" + Session.getProfile(context);
                    String extension = someFilepath.substring(someFilepath.lastIndexOf("."));
                    String url;
                    if (extension.equals(".webp")) {
                        url = Session.getImagePath(context) + Session.getUid(context) + "/" + Session.getProfile(context);
                    } else {
                        url = Session.getImagePath(context) + Session.getUid(context) + "/" + Session.getProfile(context) + ".webp";
                    }
                    Picasso.get().load(url).placeholder(R.drawable.ic_pro).error(R.drawable.ic_pro).into(binding.ivProfile);
                }
            }
        };

    }

    private void setUpPagination() {

        m = new LinearLayoutManager(this) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(context) {

                    private static final float SPEED = 1000f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }

                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

        };

        binding.rv.setLayoutManager(m);
        adapter = new DashboardAdapter(context, dashboardList, clicked, m);
        binding.rv.setAdapter(adapter);

    }

    private void getDashboardList() {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);

        RetrofitServices.urlServices.category(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("category-->", "onResponse:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {

                        dashboardList = gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<DashboardListModel>>() {
                        }.getType());
                        adapter.setDashboardModelList(dashboardList);
                        adapter.notifyDataSetChanged();

                        m.scrollToPosition(0);

                    } else if (jsonObject.getInt("status") == 0) {
                        Toast.makeText(context, "Something went Wrong..", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                pd.dismiss();
                Log.e("-->", "FALURE ");
            }
        });
    }

    private void getLocation2() {
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
                        result.append(addresses.get(0).getAddressLine(i) + ",");
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

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(AppConstants.BROADCAST_KEY));
        setUpPagination();
        if (Utility.connectionStatus(context)) {
            getDashboardList();
        } else {
            Utility.nointernettoast(context);
        }
        if (Session.getUid(context).equals("")) {
            binding.rlProfile.setVisibility(View.GONE);
        } else {
            binding.rlProfile.setVisibility(View.VISIBLE);
            profile();
        }

    }

    private void profile() {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        builder.addFormDataPart(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        builder.addFormDataPart(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        builder.addFormDataPart(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        builder.addFormDataPart(AppConstants.UID, Session.getUid(context));
        builder.addFormDataPart(AppConstants.ACTION, "view");
        builder.addFormDataPart(AppConstants.GENDER, "");
        builder.addFormDataPart(AppConstants.POST_CODE, "");
        builder.addFormDataPart(AppConstants.CITY, "");
        builder.addFormDataPart(AppConstants.COUNTRY, "");
        builder.addFormDataPart(AppConstants.LATTITUDE, "123.123");
        builder.addFormDataPart(AppConstants.LONGITUDE, "123.123");
        builder.addFormDataPart(AppConstants.LOCATION, "");
        builder.addFormDataPart(AppConstants.TELEPHONE, "");
        builder.addFormDataPart(AppConstants.LAST_NAME, "");
        builder.addFormDataPart(AppConstants.FIRST_NAME, "");
        builder.addFormDataPart(AppConstants.OLD_IMAGE, "abc");
        builder.addFormDataPart("birth_date", "");
        builder.addFormDataPart("newsletter", "news");

        final MultipartBody requestbody = builder.build();
        Log.e("profile-->", "onResponse: " + Session.getAccessToken(context));
        RetrofitServices.urlServices.user_profile(requestbody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String respo = response.body().string().trim();
                    Log.e("profile-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {
                        JSONObject in_data = jsonObject.getJSONObject("data");
                        Session.setImagePath(context, jsonObject.getString("image_path"));
                        String imgP = in_data.getString("profile_pic");
                        binding.loader.setVisibility(View.VISIBLE);

                        String someFilepath = Session.getImagePath(context) + Session.getUid(context) + "/" + imgP;
                        String extension = someFilepath.substring(someFilepath.lastIndexOf("."));
                        String url;
                        if (extension.equals(".webp")) {
                            url = Session.getImagePath(context) + Session.getUid(context) + "/" + imgP;
                        } else {
                            url = Session.getImagePath(context) + Session.getUid(context) + "/" + imgP + ".webp";
                        }

                        Picasso.get().load(url).placeholder(R.drawable.ic_pro).error(R.drawable.ic_pro).into(binding.ivProfile, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                binding.loader.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                binding.loader.setVisibility(View.GONE);
                            }
                        });


                    } else if (jsonObject.getInt("status") == 0) {
                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                Log.e("-->", "FALURE ");
            }
        });
    }

    private void checkGps() {
        //todo | For places.........
        String apiKey = getString(R.string.google_maps_key);
        if (!Places.isInitialized()) {
            Places.initialize(context, apiKey);
        }
        placesClient = Places.createClient(context);
        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        if (Utility.checkGPSStatus(context)) {
            GpsStatus = true;
        } else {
            // Toast.makeText(context, getResources().getString(R.string.seems_your_gps), Toast.LENGTH_LONG).show();
        }
        if (!GpsStatus) {
            new GpsUtils(context).turnGPSOn(new GpsUtils.onGpsListener() {
                @Override
                public void gpsStatus(boolean isGPSEnable) {
                    // turn on GPS
                    isGPS = isGPSEnable;
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 11) {
                isGPS = true; // flag maintain before get location
                if (persmissionUtils.checkLocationPermissions()) {
                    gps = new GPSTracker(context);
                    Session.setLat(context, String.valueOf(gps.getLatitude()));
                    Session.setLang(context, String.valueOf(gps.getLongitude()));
                    Log.e("getLatitude(-=-----=>)", "" + gps.getLatitude());
                    Log.e("getLongitude(-=-----=>)", "" + gps.getLongitude());
                    getAddress(gps.getLatitude(), gps.getLongitude());
                    Log.e(TAG, "onActivityResult: iffff 1 ");
                }
            } else {
                if (persmissionUtils.checkLocationPermissions()) {
                    gps = new GPSTracker(context);
                    Session.setLat(context, String.valueOf(gps.getLatitude()));
                    Session.setLang(context, String.valueOf(gps.getLongitude()));
                    Log.e("getLatitude(-=-----=>)", "" + gps.getLatitude());
                    Log.e("getLongitude(-=-----=>)", "" + gps.getLongitude());
                    getAddress(gps.getLatitude(), gps.getLongitude());
                }
                Log.e(TAG, "onActivityResult: elsee 1 ");
            }
        } else {
            gps = new GPSTracker(context);
            Session.setLat(context, String.valueOf(gps.getLatitude()));
            Session.setLang(context, String.valueOf(gps.getLongitude()));
            Log.e("getLatitude(-=-----=>)", "" + gps.getLatitude());
            Log.e("getLongitude(-=-----=>)", "" + gps.getLongitude());
            getAddress(gps.getLatitude(), gps.getLongitude());
            Log.e(TAG, "onActivityResult: elsee 2 " + gps.getLatitude());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        onRequestPermissionsRes(requestCode, permissions, grantResults);
    }

    public void onRequestPermissionsRes(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (permissions.length == 0) {
            return;
        }
        boolean allPermissionsGranted = true;
        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
        }
        if (!allPermissionsGranted) {
            boolean somePermissionsForeverDenied = false;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    //denied
                    Log.e("denied", permission);
                    Toast.makeText(context, "Zugriff verweigert", Toast.LENGTH_SHORT).show();
                } else {
                    if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.e("allowed", permission);
                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", permission);
                        somePermissionsForeverDenied = true;
                    }
                }
            }
            if (somePermissionsForeverDenied) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Zugriffsrechte erforderlich")
                        .setMessage(context.getString(R.string.permission_message))

                        .setPositiveButton(context.getString(R.string.settings), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        } else {
            switch (requestCode) {
                case PersmissionUtils.REQUEST_LOCATION_PERMISSION:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, "Zugriff gewährt", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

    }

    private void notificationStatus() {

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.NOTIFICATION, "");
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));

        Log.e("-->", "Notification " + hashMap);

        RetrofitServices.urlServices.notification_status(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    String respo = response.body().string().trim();
                    Log.e("Notification-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {

                    } else if (jsonObject.getInt("status") == 0) {
                        context.getSharedPreferences(Session.MyPREFERENCES, Context.MODE_PRIVATE).edit().clear().apply();
                        Intent in = new Intent(getApplicationContext(), Splash.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("-->", "FALURE ");
            }
        });
    }

}
