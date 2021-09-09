package com.si.styletimer.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.IntentCompat;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivitySettingBinding;
import com.si.styletimer.retrofit.Environment;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity implements TextWatcher {
    private static final String TAG = "SettingActivity";
    private Context context;
    ActivitySettingBinding binding;
    private GoogleSignInClient signInClient;
    private String newsletter = "0";
    private String salonnewsletter = "0";
    private Dialog dialog;
    private String otpSave = "";
    private EditText edt1;
    private EditText edt2;
    private EditText edt3;
    private EditText edt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        context = this;

//----for google logout
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        signInClient = GoogleSignIn.getClient(this, gso);

        onClick();

        binding.version.setText("copyright @styletimer 2021  |  alle Rechte vorbehalten\n " + Environment.getVerionName());

        if (Utility.connectionStatus(context)) {
            NotificationStatus("");
            update_salonenewslatter("view");
            update_newslatter("view");
        } else {
            Utility.nointernettoast(context);
        }


    }

    private void onClick() {

        binding.llmybooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyBookingActivity.class);
                startActivity(intent);
                // Bungee.slideLeft(context);
            }
        });
        binding.llmyprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyProfileActivity.class);
                startActivity(intent);
                //  Bungee.slideLeft(context);
            }
        });
        binding.llupdatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangePasswordActivity.class);
                startActivity(intent);
                //   Bungee.slideLeft(context);
            }
        });

        binding.llconditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(AppConstants.FLAG, "1");
                intent.putExtra(AppConstants.URL, RetrofitServices.USER_TERM);
                startActivity(intent);
                //   Bungee.slideLeft(context);
            }
        });

        binding.lltermsofuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(AppConstants.FLAG, "2");
                intent.putExtra(AppConstants.URL, RetrofitServices.USER_POLICY);
                startActivity(intent);
                //   Bungee.slideLeft(context);
            }
        });
        binding.llImprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(AppConstants.FLAG, "3");
                intent.putExtra(AppConstants.URL, RetrofitServices.USER_INPRINT);
                startActivity(intent);
                //   Bungee.slideLeft(context);
            }
        });


        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //  Bungee.slideRight(context);
            }
        });

        binding.tvDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_account_confirmation_popup("delete");
            }
        });
        binding.btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // delete_account_confirmation_popup("logout");
                if (Session.getLoginWith(context).equals("fb")) {
                    LoginManager.getInstance().logOut();
                    if (Utility.connectionStatus(context)) {
                        logOut();
                    } else {
                        Utility.nointernettoast(context);
                    }

                } else if (Session.getLoginWith(context).equals("gmail")) {
                    signOut();
                    if (Utility.connectionStatus(context)) {
                        logOut();
                    } else {
                        Utility.nointernettoast(context);
                    }
                } else {
                    if (Utility.connectionStatus(context)) {
                        logOut();
                    } else {
                        Utility.nointernettoast(context);
                    }
                }
            }
        });

        binding.sswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "onClick: " + binding.sswitch.isChecked());

                if (binding.sswitch.isChecked()) {
                    binding.sswitch.setChecked(true);
                    NotificationStatus("1");
                }

                if (!binding.sswitch.isChecked()) {
                    binding.sswitch.setChecked(false);
                    NotificationStatus("0");
                }

            }
        });

        binding.sswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.tvStatus.setText("(An)");
                    binding.tvStatus.setTextColor(getResources().getColor(R.color.quantum_googgreen));
                } else {
                    binding.tvStatus.setText("(Aus)");
                    binding.tvStatus.setTextColor(getResources().getColor(R.color.red_error));
                }
            }
        });
        binding.newsLettersSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "onClick: " + binding.newsLettersSwitch.isChecked());

                if (binding.newsLettersSwitch.isChecked()) {
                    binding.newsLettersSwitch.setChecked(true);
                    newsletter = "1";
                    if (Utility.connectionStatus(context)) {
                        update_newslatter("edit");
                    } else {
                        Utility.nointernettoast(context);
                    }

                }

                if (!binding.newsLettersSwitch.isChecked()) {
                    binding.newsLettersSwitch.setChecked(false);
                    newsletter = "0";
                    if (Utility.connectionStatus(context)) {
                        update_newslatter("edit");
                    } else {
                        Utility.nointernettoast(context);
                    }

                }

            }
        });
        binding.SalonnewsLettersSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: " + binding.SalonnewsLettersSwitch.isChecked());
                if (binding.SalonnewsLettersSwitch.isChecked()) {
                    binding.SalonnewsLettersSwitch.setChecked(true);
                    salonnewsletter = "1";
                    if (Utility.connectionStatus(context)) {
                        update_salonenewslatter("edit");
                    } else {
                        Utility.nointernettoast(context);
                    }

                }
                if (!binding.SalonnewsLettersSwitch.isChecked()) {
                    binding.SalonnewsLettersSwitch.setChecked(false);
                    salonnewsletter = "0";
                    if (Utility.connectionStatus(context)) {
                        update_salonenewslatter("edit");
                    } else {
                        Utility.nointernettoast(context);
                    }

                }

            }
        });

    }

    private void delete_account_confirmation_popup(String x) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.delete_account_confirmation_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView yes = dialog.findViewById(R.id.yes);
        TextView no = dialog.findViewById(R.id.no);

        TextView head = dialog.findViewById(R.id.head);
        TextView sHead = dialog.findViewById(R.id.sHead);
        if (x.equals("delete")) {
            head.setText(context.getResources().getString(R.string.confirmation1));
            sHead.setText(context.getResources().getString(R.string.are_you_sure_account_delete));
        } else {
            head.setText(context.getResources().getString(R.string.confirmation));
            sHead.setText(context.getResources().getString(R.string.are_you_sure_want_to_logout));
        }
        ImageView icClose = dialog.findViewById(R.id.icClose);
        yes.setText(context.getResources().getString(R.string.yes));
        no.setText(context.getResources().getString(R.string.no));

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (x.equals("delete")) {
                    Random random = new Random();
                    otpSave = String.format("%04d", random.nextInt(10000));
                    Log.e(TAG, "otp : " + otpSave);
                    if (Utility.connectionStatus(context)) {
                        send_otp_for_delete_account(otpSave,"first");
                    } else {
                        Utility.nointernettoast(context);
                    }

                } else {
                    if (Session.getLoginWith(context).equals("fb")) {
                        LoginManager.getInstance().logOut();
                        logOut();
                    } else if (Session.getLoginWith(context).equals("gmail")) {
                        signOut();
                        logOut();
                    } else {
                        logOut();
                    }
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void send_otp_for_delete_account(String otp,String timee) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.OTP, otp);
        Log.e(TAG, "send_otp_for_delete_account " + hashMap);
        RetrofitServices.urlServi.send_otp_for_delete_account(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String respo = response.body().string().trim();
                    Log.e(TAG, "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {

                        if(timee.equals("resend"))
                        {
                            Toast.makeText(context, "Code erneut gesendet", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(context, "Code gesendet", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                        delete_account_otp_popup(otp);

                    } else if (jsonObject.getInt("status") == 0) {
                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
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

    private void delete_account_otp_popup(String x) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.delete_account_otp_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button yes = dialog.findViewById(R.id.yes);
        TextView head = dialog.findViewById(R.id.head);
        TextView sHead = dialog.findViewById(R.id.sHead);
        sHead.setText("Bitte gebe zur Löschung den 4stelligen Bestätigungscode ein, den wir gerade an"+" " + Session.getEmailID(context)+" "+"gesendet haben.");
        TextView tvResendOtp = dialog.findViewById(R.id.tvResendOtp);
        ImageView icClose = dialog.findViewById(R.id.icClosesss);

        edt1 = dialog.findViewById(R.id.edt1);
        edt2 = dialog.findViewById(R.id.edt2);
        edt3 = dialog.findViewById(R.id.edt3);
        edt4 = dialog.findViewById(R.id.edt4);

        edt1.addTextChangedListener(this);
        edt2.addTextChangedListener(this);
        edt3.addTextChangedListener(this);
        edt4.addTextChangedListener(this);
       yes.setText(context.getResources().getString(R.string.delete_this_account));

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edt1.getText().toString().isEmpty()) {
                    if (!edt2.getText().toString().isEmpty()) {
                        if (!edt3.getText().toString().isEmpty()) {
                            if (!edt4.getText().toString().isEmpty()) {
                                String opp = edt1.getText().toString().trim() + "" + edt2.getText().toString().trim() + "" + edt3.getText().toString().trim() + "" + edt4.getText().toString().trim();
                                if (opp.equals(otpSave)) {
                                    dialog.dismiss();
                                    delete_account(opp);
                                } else {
                                    Toast.makeText(context, context.getResources().getString(R.string.invalid_otp), Toast.LENGTH_LONG).show();

                                }

                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.otp_not_empty), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, context.getResources().getString(R.string.otp_not_empty), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.otp_not_empty), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.otp_not_empty), Toast.LENGTH_LONG).show();
                }
            }
        });
        tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                otpSave = String.format("%04d", random.nextInt(10000));
                Log.e(TAG, "otp : " + otpSave);
                dialog.dismiss();
                if (Utility.connectionStatus(context)) {
                    send_otp_for_delete_account(otpSave,"resend");
                } else {
                    Utility.nointernettoast(context);
                }

            }
        });
        icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        edt1.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        {
            if (editable.length() == 1) {
                if (edt1.length() == 1) {
                    edt2.requestFocus();
                }
                if (edt2.length() == 1) {
                    edt3.requestFocus();
                }
                if (edt3.length() == 1) {
                    edt4.requestFocus();
                }
            } else if (editable.length() == 0) {
                if (edt4.length() == 0) {
                    edt3.requestFocus();
                }
                if (edt3.length() == 0) {
                    edt2.requestFocus();
                }
                if (edt2.length() == 0) {
                    edt1.requestFocus();
                }
            }
        }

    }

    private void delete_account(String otp) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.OTP, otp);
        Log.e(TAG, "delete_account " + hashMap);
        RetrofitServices.urlServi.delete_account(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String respo = response.body().string().trim();
                    Log.e(TAG, "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("styletimer_" + Session.getUid(context));
                        context.getSharedPreferences(Session.MyPREFERENCES, Context.MODE_PRIVATE).edit().clear().apply();
                        Intent in = new Intent(getApplicationContext(), Splash.class);
                         in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in);
                    //    Intent mainIntent = IntentCompat.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_LAUNCHER);
                     //   mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      //  context.getApplicationContext().startActivity(mainIntent);
                      //  System.exit(0);

                    } else if (jsonObject.getInt("status") == 0) {

                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
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

    //todo-- Calling logout here....
    private void logOut() {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.UID, Session.getUid(context));

        Log.e("-->", "LOGOUT " + hashMap);

        RetrofitServices.urlServices.logout(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("LOGOUT-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("styletimer_" + Session.getUid(context));
                        context.getSharedPreferences(Session.MyPREFERENCES, Context.MODE_PRIVATE).edit().clear().apply();
                       /* if (Session.getLoginWith(context).equals("fb")) {
                            LoginManager.getInstance().logOut();
                        } else if (Session.getLoginWith(context).equals("gmail")) {
                            signOut();
                        }*/
                        Intent in = new Intent(getApplicationContext(), Splash.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in);

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

    private void signOut() {
        signInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        revokeAccess();
                        Log.e(TAG, "onComplete: >>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    }
                });
    }

    private void revokeAccess() {
        signInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e(TAG, "onComplete: >>>>>>>>>>revokeAccess>>>>>>>>>>>>>>>>>");
                    }
                });
    }

    private void NotificationStatus(String status) {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.NOTIFICATION, status);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));

        Log.e("-->", "Notification " + hashMap);

        RetrofitServices.urlServices.notification_status(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("Notification-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {

                        if (jsonObject.getString("data").equals("0")) {
                            binding.sswitch.setChecked(false);
                        } else if (jsonObject.getString("data").equals("1")) {
                            binding.sswitch.setChecked(true);
                        }

                    } else if (jsonObject.getInt("status") == 0) {
                        context.getSharedPreferences(Session.MyPREFERENCES, Context.MODE_PRIVATE).edit().clear().apply();
                        Intent in = new Intent(getApplicationContext(), Splash.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in);
                        //Toast.makeText(context, "Something went Wrong..", Toast.LENGTH_SHORT).show();
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

    private void update_newslatter(String action) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.ACTION, action);
        if (action.equals("edit")) {
            pd.show();
            hashMap.put("newslatter", newsletter);
        }
        Log.e("-->", "update_newslatter " + hashMap);

        RetrofitServices.urlServi.update_newslatter(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("Notification-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        if (data.getString("newsletter").equals("0")) {
                            newsletter = data.getString("newsletter");
                            binding.newsLettersSwitch.setChecked(false);
                        } else if (data.getString("newsletter").equals("1")) {
                            newsletter = data.getString("newsletter");
                            binding.newsLettersSwitch.setChecked(true);
                        }

                    } else if (jsonObject.getInt("status") == 0) {
                        //Toast.makeText(context, "Something went Wrong..", Toast.LENGTH_SHORT).show();
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

    private void update_salonenewslatter(String action) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.ACTION, action);
        if (action.equals("edit")) {
            pd.show();
            hashMap.put("newslatter", salonnewsletter);
        }
        Log.e(TAG, "update_salonenewslatter " + hashMap);

        RetrofitServices.urlServi.update_salonenewslatter(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e(TAG, "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        if (data.getString("newsletter").equals("0")) {
                            salonnewsletter = data.getString("newsletter");
                            binding.SalonnewsLettersSwitch.setChecked(false);
                        } else if (data.getString("newsletter").equals("1")) {
                            salonnewsletter = data.getString("newsletter");
                            binding.SalonnewsLettersSwitch.setChecked(true);
                        }

                    } else if (jsonObject.getInt("status") == 0) {
                        //Toast.makeText(context, "Something went Wrong..", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  Bungee.slideRight(context);
    }


}
