package com.si.styletimer.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.si.styletimer.R;
import com.si.styletimer.databinding.ActivityWalk3Binding;
import com.si.styletimer.databinding.NotificationPermissionPopupBinding;
import com.si.styletimer.utill.Animatoo;
import com.si.styletimer.utill.PersmissionUtils;
import com.si.styletimer.utill.Utility;

public class Walk3Activity extends AppCompatActivity {
    private static final String TAG = "Walk3Activity";
    private Context context;
    private ActivityWalk3Binding binding;
    private static final int NOTIFICATION_PERMISSION_CODE = 123;
    private PersmissionUtils persmissionUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.fullScreen(Walk3Activity.this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_walk3);
        context = this;
        persmissionUtils = new PersmissionUtils(context, Walk3Activity.this);
        initView();
    }

    private void initView() {
        binding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, DashboardActivity.class);
                startActivity(in);
                finishAffinity();
                Animatoo.animateSlideLeft(context);
            }
        });
        binding.btSlider.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
               // persmissionUtils.requestNotificationPermissions();
                Intent in = new Intent(context, Walk4Activity.class);
                startActivity(in);
                Animatoo.animateSlideLeft(context);
               // notification_permission_popup();

            }
        });
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

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
                Intent in = new Intent(context, Walk4Activity.class);
                startActivity(in);
                Animatoo.animateSlideLeft(context);
                //requestNotificationPermission();

            }
        });
        permissionPopupBinding.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent in = new Intent(context, Walk4Activity.class);
                startActivity(in);
                Animatoo.animateSlideLeft(context);
            }
        });

        dialog1.show();
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
        switch (requestCode) {
            case NOTIFICATION_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Displaying a toast
                    Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
                    Intent in = new Intent(context, Walk4Activity.class);
                    startActivity(in);
                    Animatoo.animateSlideLeft(context);
                } else {
                    Intent in = new Intent(context, Walk4Activity.class);
                    startActivity(in);
                    Animatoo.animateSlideLeft(context);
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