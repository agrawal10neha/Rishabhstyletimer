package com.si.styletimer.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.si.styletimer.R;
import com.si.styletimer.databinding.ActivityFirstLoginBinding;

public class FirstLoginActivity extends AppCompatActivity {
    private static final String TAG = "FirstLoginActivity";
    private Context context;
    ActivityFirstLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_first_login);

        context = this;

        initView();
    }

    private void initView() {

        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SettingActivity.class);
                startActivity(intent);
            }
        });
    }
}
