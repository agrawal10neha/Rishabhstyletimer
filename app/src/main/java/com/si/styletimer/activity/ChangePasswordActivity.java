package com.si.styletimer.activity;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivityChangePasswordBinding;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private static final String TAG = "ChangePasswordActivity";
    private Context context;
    ActivityChangePasswordBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_change_password);
        
        context = this;

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
             //   Bungee.slideRight(context);
            }
        });

        initView();
    }

    private void initView() {

        onClick();

        textWatcher();

        if(Session.getPassword(context).equals("")){
            binding.etOldPass.setClickable(false);
            binding.etOldPass.setFocusable(false);
        }else {
            binding.etOldPass.setClickable(true);
            binding.etOldPass.setFocusable(true);
        }

    }

    private void textWatcher() {

        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->",String.valueOf(s));

                if(String.valueOf(s).startsWith(" ") || String.valueOf(s).endsWith(" ")){
                    binding.tiPassword.setError("Bitte gültiges Passwort eingeben");

                }else if(s.length()<8) {
                    binding.tiPassword.setError("Bitte mindestens 8 Zeichen verwenden");
                }else {
                    binding.tiPassword.setError(null);
                    binding.tiPassword.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.tiPassword.setHintTextAppearance(R.style.EditTextHintStyle);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

        });

        binding.etConPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->",String.valueOf(s));

                if(String.valueOf(s).equals(binding.etPassword.getText().toString())){

                    binding.tiConPass.setError(null);
                    binding.tiConPass.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.tiConPass.setHintTextAppearance(R.style.EditTextHintStyle);

                }else {

                    binding.tiConPass.setError(context.getResources().getString(R.string.confirm_password_not_match));
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

        });

        binding.etOldPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->",String.valueOf(s));

               if(s.length()<1)
                     {
                    binding.tiOldPass.setError(context.getResources().getString(R.string.please_enter_old_pass));
                     }
               else {
                    binding.tiOldPass.setError(null);
                    binding.tiOldPass.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.tiOldPass.setHintTextAppearance(R.style.EditTextHintStyle);
                    }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

        });
    }

    private void onTouch() {

        binding.etOldPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.tiOldPass.setError(null);
                return false;
            }
        });

        binding.etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.tiPassword.setError(null);
                return false;
            }
        });

        binding.etConPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.tiConPass.setError(null);
                return false;
            }
        });
    }

    private void onClick() {
        binding.btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        binding.llMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("-->","signup hide");
                Utility.hideSoftKeyboard(ChangePasswordActivity.this);
                return false;
            }

        });

    }

    private void validate() {

        if(binding.etOldPass.getText().toString().equals("")){
            binding.tiOldPass.setError(context.getResources().getString(R.string.please_enter_old_pass));

        }else if(binding.etPassword.getText().toString().equals("")){
            binding.tiPassword.setError(context.getResources().getString(R.string.please_enter_new_password));

        }else if(binding.etPassword.getText().toString().length()<8){
            binding.tiPassword.setError("Bitte mindestens 8 Zeichen verwenden");

        }else if(!binding.etConPass.getText().toString().equals(binding.etPassword.getText().toString())){
            binding.tiConPass.setError(context.getResources().getString(R.string.confirm_password_not_match));

        }else {
            change();
        }

    }

    private void change() {

        final ProgressDialog pd =new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.PASSWORD, binding.etPassword.getText().toString());
        hashMap.put(AppConstants.OLD_PASSWORD, binding.etOldPass.getText().toString());
        hashMap.put(AppConstants.CON_PASSWORD, binding.etConPass.getText().toString());

        Log.e("-->", "CHANGE P " + hashMap);

        RetrofitServices.urlServices.change_password(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("CHANGE P-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if(jsonObject.getInt("status")==1){

                        Toast.makeText(context, context.getResources().getString(R.string.password_successfully_changed), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                        Session.setPassword(context,binding.etConPass.getText().toString());
                        finish();
                    //    Bungee.slideRight(context);

                    }else if(jsonObject.getInt("status")==0){

                        if((jsonObject.getString("response_message")).equals("Old password does not matched"))
                        {
                            binding.tiOldPass.setError("Altes Passwort fehlerhaft");
                            Toast.makeText(context, "Altes Passwort fehlerhaft", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                catch (Exception e){
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("-->","FALURE ");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
