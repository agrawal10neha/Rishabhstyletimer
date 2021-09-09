package com.si.styletimer.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.si.styletimer.databinding.ActivityForgotBinding;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends AppCompatActivity {

    private ActivityForgotBinding binding;
    private Context context;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_forgot);
        context=this;
        onClick();
        inIt();

    }

    private void inIt() {

        TextWatcher();

    }

    private void TextWatcher() {

        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->",String.valueOf(s));

                if(Utility.isValidEmailAddress(String.valueOf(s))){
                    Log.e("---->","in");
                    binding.tiEmail.setError(null);
                    binding.tiEmail.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.tiEmail.setHintTextAppearance(R.style.EditTextHintStyle);
                    //binding.etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tic_yelow, 0);
                }else {
                    Log.e("---->","out");
                    binding.tiEmail.setError(context.getResources().getString(R.string.please_enter_valid_email));
                    //binding.ilEmail.setBoxStrokeColor(context.getResources().getColor(R.color.red));
                    //binding.etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tic_red, 0);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {




                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {



                // TODO Auto-generated method stub
            }
        });

    }

    private void onClick() {

        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.connectionStatus(context)){
                    if(Utility.isValidEmailAddress(binding.etEmail.getText().toString())){
                        forgetPassword();

                    }else if(binding.etEmail.getText().toString().equals("")){
                        binding.tiEmail.setError(context.getResources().getString(R.string.please_enter_valid_email));
                        Toast.makeText(context, "Bei Anmeldung über Google oder Apple-ID ist ein Zurücksetzen des Passworts leider nicht möglich", Toast.LENGTH_SHORT).show();

                    }else {
                        binding.tiEmail.setError("Bitte gültige E-Mail Adresse eingeben");

                    }
                }else {
                    Toast.makeText(context, "Bitte überprüfe deine Internetverbindung", Toast.LENGTH_SHORT).show();

                }

            }
        });

        binding.llForgotMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utility.hideSoftKeyboard(ForgotActivity.this);
                return false;
            }
        });

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showDialog() {

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.forgot_allert_popup);
        //dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.findViewById(R.id.tvLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void forgetDialog() {

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.forget_password_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.findViewById(R.id.tvLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }

    private void forgetPassword() {

        final ProgressDialog pd =new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.EMAIL, binding.etEmail.getText().toString());


        Log.e("-->", "forget " + hashMap);

        RetrofitServices.urlServices.forgot_password(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("forget-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if(jsonObject.getInt("status")==1){

                        forgetDialog();
                        //Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();


                    }else if(jsonObject.getInt("status")==0){

                        Toast.makeText(context,"Bitte überprüfe die eingegebene E-Mail Adresse", Toast.LENGTH_SHORT).show();
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

}
