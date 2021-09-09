package com.si.styletimer.activity;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.si.styletimer.R;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivityWebViewBinding;


public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";
    private Context context;
    private ActivityWebViewBinding binding;
    private String url = "", flag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        context = this;

        initviews();
    }

    private void initviews() {
        flag = getIntent().getStringExtra(AppConstants.FLAG);
        if (flag != null && flag.equals("1")) {
            binding.tvTitle.setText("Nutzungsbedingungen");
            url = getIntent().getStringExtra(AppConstants.URL);
        }else if (flag != null && flag.equals("2")) {
            binding.tvTitle.setText("Datenschutzbestimmungen");
            url = getIntent().getStringExtra(AppConstants.URL);
        } else {
            binding.tvTitle.setText("Impressum");
            url = getIntent().getStringExtra(AppConstants.URL);
        }
        setup_webview(url);

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //    Bungee.slideRight(context);
            }
        });
    }

    private void setup_webview(String url) {
        Log.e(TAG, "setup_webview: " + url );
        binding.progressbarr.setVisibility(View.VISIBLE);
        binding.progressbarr.setProgress(0);
        binding.webview.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView webView1, int newProgress) {
                binding.progressbarr.setProgress(newProgress);
                if (newProgress == 100) {
                    binding.progressbarr.setVisibility(View.GONE);
                }
            }
        });
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.loadUrl(url);
        binding.webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //   Bungee.slideRight(context);
    }
}
