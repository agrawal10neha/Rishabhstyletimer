package com.si.styletimer.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.si.styletimer.R;
import com.si.styletimer.databinding.ActivityWalk4Binding;
import com.si.styletimer.databinding.TermAndCondiPopupBinding;
import com.si.styletimer.retrofit.Environment;
import com.si.styletimer.utill.Animatoo;
import com.si.styletimer.utill.Utility;

public class Walk4Activity extends AppCompatActivity {

    private static final String TAG = "Walk4Activity";
    private Context context;
    private ActivityWalk4Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.fullScreen(Walk4Activity.this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_walk4);
        context = this;
        initView();
    }

    private void initView() {

        binding.tvContent1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                term_and_condi_popup();
            }
        });
        binding.btSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, DashboardActivity.class);
                startActivity(in);
                finishAffinity();
                Animatoo.animateSlideLeft(context);
            }
        });
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(context);
    }

}