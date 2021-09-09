package com.si.styletimer.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.si.styletimer.R;
import com.si.styletimer.databinding.ActivityWalkthroughBinding;
import com.si.styletimer.databinding.TermAndCondiPopupBinding;
import com.si.styletimer.retrofit.Environment;
import com.si.styletimer.utill.Animatoo;
import com.si.styletimer.utill.PersmissionUtils;
import com.si.styletimer.utill.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

public class WalkthroughActivity extends AppCompatActivity {
    private static final String TAG = "WalkthroughActivity";
    ActivityWalkthroughBinding binding;
    private Context context;
    private static Integer[] img;
    private ArrayList<Integer> ImgArray = new ArrayList<Integer>();
    private int currentPage = 0;
    private boolean isFirst = false;
    private Timer swipeTimer;
    private ArrayList<String> heading;
    private ArrayList<String> contents;
    private static final int NOTIFICATION_PERMISSION_CODE = 123;
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private PersmissionUtils persmissionUtils;
    private Boolean mLocationPermissionGranted = false;
    private Boolean mLocationPermissionNotification = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.fullScreen(WalkthroughActivity.this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_walkthrough);
        context = this;
        persmissionUtils = new PersmissionUtils(context, WalkthroughActivity.this);
        heading = new ArrayList<String>(Arrays.asList(getResources().getString(R.string.sliderh_text1), getResources().getString(R.string.sliderh_text2), getResources().getString(R.string.sliderh_text3), getResources().getString(R.string.sliderh_text4)));
        contents = new ArrayList<String>(Arrays.asList(getResources().getString(R.string.sliderb_text1), getResources().getString(R.string.sliderb_text2), getResources().getString(R.string.sliderb_text3), getResources().getString(R.string.sliderb_text4)));
      //  img = new Integer[]{R.drawable.slider1, R.drawable.slider2, R.drawable.slider3, R.drawable.slider4};

        initView();
    }

    private void initView() {
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        Log.e(TAG, "initView: w = " + width+"   h = "+height );
        binding.btSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, Walk2Activity.class);
                startActivity(in);
                Animatoo.animateSlideLeft(context);
            }
        });

        //  sliderSetup();
    }
/*
    private void sliderSetup() {

        for (int i = 0; i < img.length; i++)
            ImgArray.add(img[i]);
        SliderAdapter sliderAdapter = new SliderAdapter(context, ImgArray, heading, contents);
        binding.pager.setAdapter(sliderAdapter);
        sliderAdapter.setOnItemClickListener(new SliderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.e(TAG, "onItemClick: " + position);
                switch (view.getId()) {
                    case R.id.tvSkip:
                        Intent in = new Intent(context, DashboardActivity.class);
                        startActivity(in);
                        finishAffinity();
                        break;
                    case R.id.tvHeir:
                        term_and_condi_popup();
                        break;
                    case R.id.btSlider:
                        if (position == 1) {
                            binding.pager.setCurrentItem(1, true);
                        } else if (position == 2) {
                            gps_permission_popup();
                        } else if (position == 3) {
                            notification_permission_popup();
                        } else if (position == 4) {
                            Intent in2 = new Intent(context, DashboardActivity.class);
                            startActivity(in2);
                            finishAffinity();
                        }
                        break;

                }
            }
        });

    }
*/

    private void term_and_condi_popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        TermAndCondiPopupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.term_and_condi_popup, null, false);
        builder.setView(binding.getRoot());

        AlertDialog dialog = builder.create();

        String url = Environment.getBaseUrl() + "user/get_staticpage/policy";
        Log.e(TAG, "term_and_condi_popup: " + url);

        Utility.showProgressHUD(context);
        binding.webview.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView webView1, int newProgress) {
                Utility.hideProgressHud();
            }
        });
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.loadUrl(url);
        binding.webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Utility.hideProgressHud();
                return true;
            }
        });


        binding.ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();


    }


/*
    private void notification_permission_popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        NotificationPermissionPopupBinding permissionPopupBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.notification_permission_popup, null, false);
        builder.setView(permissionPopupBinding.getRoot());

        AlertDialog dialog1 = builder.create();

        permissionPopupBinding.icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        permissionPopupBinding.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                binding.pager.setCurrentItem(3, true);
               */
/* if (mLocationPermissionNotification) {
                    binding.pager.setCurrentItem(4, true);
                }
                {
                    requestNotificationPermission();
                }*//*

            }
        });
        permissionPopupBinding.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                binding.pager.setCurrentItem(3, true);
            }
        });

        dialog1.show();
    }
*/

/*
    private void gps_permission_popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        GpsPermissionPopupBinding gpsPermissionPopupBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.gps_permission_popup, null, false);
        builder.setView(gpsPermissionPopupBinding.getRoot());
        AlertDialog dialog2 = builder.create();
        gpsPermissionPopupBinding.icClose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });
        gpsPermissionPopupBinding.yes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
                getLocationPermission();
               */
/* if (mLocationPermissionGranted) {
                    binding.pager.setCurrentItem(2, true);
                }else {
                    binding.pager.setCurrentItem(2, true);
                }*//*


            }
        });

        gpsPermissionPopupBinding.yesApp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
                getLocationPermission();
               */
/* if (mLocationPermissionGranted) {
                    binding.pager.setCurrentItem(2, true);
                }else {
                    binding.pager.setCurrentItem(2, true);
                }*//*

            }
        });
        gpsPermissionPopupBinding.no1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
                binding.pager.setCurrentItem(2, true);
            }
        });

        dialog2.show();

    }
*/

    private void getLocationPermission() {
        String permissions[] = {android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        Log.d(TAG, "getLocationPermission: before if condition");
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                Log.e(TAG, "getLocationPermission: calling init");

            } else {

                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {

        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, NOTIFICATION_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    Toast.makeText(this, "Permission Granted : OnRequest", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onRequestPermissionsResult: calling init");
                    //binding.pager.setCurrentItem(2, true);
                } else {
                   // binding.pager.setCurrentItem(2, true);
                }
                break;
            case NOTIFICATION_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Displaying a toast
                    Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
                   // binding.pager.setCurrentItem(4, true);
                    mLocationPermissionNotification = true;
                } else {
                    mLocationPermissionNotification = false;
                    // Displaying another toast if permission is not granted
                    // Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
                   // binding.pager.setCurrentItem(4, true);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(context);
    }
}
