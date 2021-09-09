package com.si.styletimer.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.widget.TextViewCompat;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.si.styletimer.R;
import com.si.styletimer.databinding.ActivityWalk2Binding;
import com.si.styletimer.utill.Animatoo;
import com.si.styletimer.utill.PersmissionUtils;
import com.si.styletimer.utill.Utility;

public class Walk2Activity extends AppCompatActivity {
    private static final String TAG = "Walk2Activity";
    private Context context;
    private ActivityWalk2Binding binding;
    private PersmissionUtils persmissionUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.fullScreen(Walk2Activity.this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_walk2);
        context = this;
        persmissionUtils = new PersmissionUtils(context, Walk2Activity.this);

        initView();
    }

    private void initView() {

        binding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, DashboardActivity.class);
                startActivity(in);
                Animatoo.animateSlideLeft(context);
                finishAffinity();
            }
        });
        binding.btSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  getLocationPermission();
                if (persmissionUtils.checkLocationPermissions()) {
                    Intent in = new Intent(context, Walk3Activity.class);
                    startActivity(in);
                    Animatoo.animateSlideLeft(context);
                }else {
                    persmissionUtils.askLocationPermissions();
                }
            }
        });
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
                    Intent in = new Intent(context, Walk3Activity.class);
                    startActivity(in);
                    Animatoo.animateSlideLeft(context);
                   // Toast.makeText(context, "Zugriff verweigert", Toast.LENGTH_SHORT).show();
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
                       // Toast.makeText(context, "Zugriff gewährt", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(context, Walk3Activity.class);
                        startActivity(in);
                        Animatoo.animateSlideLeft(context);
                    }
                    break;
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(context);
    }

}