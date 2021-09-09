package com.si.styletimer.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivityLoginBinding;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Context context;
    private String flag = "", s_media = "", destination = "";
    private static final String TAG = "LoginActivity";

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient signInClient;
    private static final int REQ_CODE = 100;
    private String Id = "", Name = "", Email = "";
    private LoginButton loginButton;
    //CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private CallbackManager callbackManager;
    private String profile_image = "";
    String SITE_KEY = "6LdmX9AZAAAAAMNtXUfM7LxdgHFVV1azRhbINA-p";
    String SECRET_KEY = "6LdmX9AZAAAAAHVdz6WdHAWnfy4OFC69gRoPI82M";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        context = this;
        inIt();

        onClick123();

    }

    private void inIt() {

        Locale locale = new Locale("de");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        flag = getIntent().getStringExtra("flag");
        destination = getIntent().getStringExtra("destination");
        Log.e(TAG, "inIt: ---> " + flag + "  " + destination);

//Google sign in
        inIt_facebook();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        signInClient = GoogleSignIn.getClient(this, gso);
        textWatcher();

    }


    private void textWatcher() {

        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Log.e("---->",String.valueOf(s));

                if (Utility.isValidEmailAddress(String.valueOf(s))) {
                    //   Log.e("---->","in");
                    binding.tiEmail.setError(null);
                    binding.tiEmail.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.tiEmail.setHintTextAppearance(R.style.EditTextHintStyle);
                    //binding.etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tic_yelow, 0);
                } else {
                    //  Log.e("---->","out");
                    binding.tiEmail.setError("Bitte gültige E-Mail Adresse eingeben");
                    //binding.ilEmail.setBoxStrokeColor(context.getResources().getColor(R.color.red));
                    //binding.etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tic_red, 0);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Log.e("---->",String.valueOf(s));

                /*if(String.valueOf(s).startsWith(" ") || String.valueOf(s).endsWith(" ")){
                    binding.tiPassword.setError("Enter a valid password");
                    *//*binding.tiPassword.setError(null);
                    binding.tiPassword.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.tiPassword.setHintTextAppearance(R.style.EditTextHintStyle);*//*

                }else*/
                if (s.length() < 1) {
                    binding.tiPassword.setError("Bitte Passwort eingeben");

                } /*else if (s.length() < 8) {
                    binding.tiPassword.setError("Bitte mindestens 8 Zeichen verwenden");
                }*/ else {

                    if (String.valueOf(s).startsWith(" ") || String.valueOf(s).endsWith(" ")) {
                        binding.tiPassword.setError("Bitte gültiges Passwort eingeben");
                    } else {
                        binding.tiPassword.setError(null);
                        binding.tiPassword.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                        binding.tiPassword.setHintTextAppearance(R.style.EditTextHintStyle);

                    }


                    /*binding.tiPassword.setError(null);
                    binding.tiPassword.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.tiPassword.setHintTextAppearance(R.style.EditTextHintStyle);*/

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void onClick123() {
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.etEmail.getText().toString().equals("")) {
                    binding.tiEmail.setError("Bitte E-Mail Adresse eingeben");

                } else if (!Utility.isValidEmailAddress(binding.etEmail.getText().toString())) {
                    binding.tiEmail.setError("Bitte gültige E-Mail Adresse eingeben");

                } else if (binding.etPassword.getText().toString().equals("")) {
                    binding.tiPassword.setError("Bitte Passwort eingeben");

                } else if (String.valueOf(binding.etPassword.getText().toString()).startsWith(" ")
                        ||
                        String.valueOf(binding.etPassword.getText().toString()).endsWith(" ")) {

                    binding.tiPassword.setError("Bitte gültiges Passwort eingeben");

                } else {
                    Utility.hideSoftKeyboard(LoginActivity.this);
                    login();
                }
            }
        });

        binding.tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ForgotActivity.class));
                //   Bungee.slideLeft(context);
            }
        });

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // Bungee.slideRight(context);
            }
        });

        binding.llLoginMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utility.hideSoftKeyboard(LoginActivity.this);
                return false;
            }
        });

        binding.btGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: here 111");
                Intent signInIntent = signInClient.getSignInIntent();
                startActivityForResult(signInIntent, 100);
            }
        });

        binding.btFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
                Log.e(TAG, "onClick: FB ");
            }
        });

        binding.or.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        binding.tvResendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend_activation_mail();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
            //  Log.e("Google login result---",String.valueOf(task));
        } else {
            if (data != null) {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void handleSignInResult(/*GoogleSignInResult result*/Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Id = account.getId();
            Name = account.getDisplayName();
            Email = account.getEmail();
            if (account.getPhotoUrl() != null) {
                profile_image = account.getPhotoUrl().toString();
                Log.e(TAG, "handleSignInResult: " + profile_image);
            }
            SocialLogin("gmail");

        } catch (ApiException e) {

        }
    }

    private void inIt_facebook() {
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button1);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "onSuccess: " + loginResult.getAccessToken());
                getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
                // App code
                Log.e(TAG, "fb ---> Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e(TAG, "fb ---> Error");
            }
        });

    }

    protected void getUserDetails(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.e(TAG, "onCompleted:getUserDetails " + object.toString());
                try {
                    Name = object.getString("name");
                    Email = object.getString("email");
                    Id = object.getString("id");
                    if (object.has("picture")) {
                        profile_image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                        Log.e(TAG, "onCompleted: " + profile_image);
                    }
                    SocialLogin("fb");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void SocialLogin(String type) {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));

        hashMap.put(AppConstants.EMAIL, Email);
        hashMap.put(AppConstants.NAME, Name);
        hashMap.put(AppConstants.UNIQUE_ID, Id);
        hashMap.put(AppConstants.TYPE, type);
        if (profile_image != null && !profile_image.equals("")) {
            hashMap.put(AppConstants.SOCIAL_IMAGEURL, profile_image);
        } else {
            hashMap.put(AppConstants.SOCIAL_IMAGEURL, "");
        }
        RetrofitServices.urlServices.socialLogin(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {
                        Locale locale = new Locale("de");
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config,
                                getBaseContext().getResources().getDisplayMetrics());

                        //Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                        Session.setLoginWith(context, type);
                        JSONObject data = jsonObject.getJSONObject("data");

                        Session.setUid(context, data.getString("id"));
                        Session.setPassword(context, data.getString("password"));
                        Session.setEmailID(context, data.getString("email"));

                        FirebaseMessaging.getInstance().subscribeToTopic("styletimer_" + data.getString("id"));
                        Session.setProfile(context,data.getString("profile_pic"));

                        Log.e(TAG, "onResponse: " + destination);

                        if (destination.equals("booking")) {

                            Intent in = new Intent(getApplicationContext(), MyBookingActivity.class);
                            in.putExtra("selected", "0");
                            startActivity(in);
                            finish();
                            //      Bungee.slideLeft(context);

                        } else if (destination.equals("setting")) {

                            Intent in = new Intent(getApplicationContext(), SettingActivity.class);
                            startActivity(in);
                            finish();
                            //  Bungee.slideLeft(context);

                        } else if (destination.equals("dashboard")) {
                            Log.e(TAG, "onResponse:--------------------------- ");
                            Intent in = new Intent(getApplicationContext(), DashboardActivity.class);
                            startActivity(in);
                            finish();
                        } else {
                            finish();
                            //  Bungee.slideLeft(context);
                        }

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
                // Log.e("-->","FALURE ");
            }
        });

    }

    private void resend_activation_mail() {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.IDENTITY, binding.etEmail.getText().toString());

        Log.e(TAG, "resend_activation_mail " + hashMap);

        RetrofitServices.urlServices.resend_activation_mail(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String respo = response.body().string().trim();
                    Log.e(TAG, "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {
                        binding.tvResendMail.setVisibility(View.GONE);
                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_LONG).show();
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
                Log.e("-->", "FALURE ");
            }
        });
    }

    private void login() {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.IDENTITY, binding.etEmail.getText().toString());
        hashMap.put(AppConstants.PASSWORD, binding.etPassword.getText().toString());


        Log.e("-->", "LOGIN " + hashMap);

        RetrofitServices.urlServices.user_login(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("LOGIN-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {

                        //Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();

                        JSONObject data = jsonObject.getJSONObject("data");

                        Session.setUid(context, data.getString("id"));
                        Session.setPassword(context, data.getString("password"));
                        Session.setEmailID(context, data.getString("email"));
                        FirebaseMessaging.getInstance().subscribeToTopic("styletimer_" + data.getString("id"));
                        Session.setProfile(context,data.getString("profile_pic"));
                        if (destination != null && destination.equals("booking")) {

                            Intent in = new Intent(getApplicationContext(), MyBookingActivity.class);
                            in.putExtra("selected", "0");
                            startActivity(in);
                            finish();
                            //   Bungee.slideLeft(context);

                        } else if (destination.equals("setting")) {

                            Intent in = new Intent(getApplicationContext(), SettingActivity.class);
                            startActivity(in);
                            finish();
                            //  Bungee.slideLeft(context);

                        } else if (destination.equals("fav")) {

                            Intent in = new Intent(getApplicationContext(), FavouriteActivity.class);
                            startActivity(in);
                            finish();
                            //   Bungee.slideLeft(context);

                        } else if (destination.equals("finish")) {

                            finish();
                            //   Bungee.slideLeft(context);
                        } else if (destination.equals("dashboard")) {
                            Log.e(TAG, "onResponse:--------------------------- ");
                            Intent in = new Intent(getApplicationContext(), DashboardActivity.class);
                            startActivity(in);
                            finish();
                        }

                    } else if (jsonObject.getInt("status") == 0) {
                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                        String dada = jsonObject.getString("data");
                        String inactive = jsonObject.getString("inactive");
                        if (inactive != null && !inactive.equals("") && inactive.equals("1")) {
                            binding.tvResendMail.setVisibility(View.VISIBLE);
                        } else {
                            binding.tvResendMail.setVisibility(View.GONE);
                        }
                        if (dada != null && !dada.equals("") && !dada.equals("0")) {
                            int ss = Integer.parseInt(dada);
                            if (ss>2) {
                                recapcha();
                            }
                        }

                    }

                } catch (Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("-->", "FALURE ");
            }
        });
    }

    private void recapcha() {
        SafetyNet.getClient(this).verifyWithRecaptcha(SITE_KEY)
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        if (!response.getTokenResult().isEmpty()) {
                            handleSiteVerify(response.getTokenResult());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            Log.e(TAG, "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                        } else {
                            Log.e(TAG, "Unknown type of error: " + e.getMessage());
                        }
                    }
                });
    }

    private void signOut() {
        signInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        revokeAccess();
                        //   Log.e(TAG, "onComplete: >>>>>>>>>>>>>>>>>>>>>>>>>>>" );
                    }
                });
    }

    private void revokeAccess() {
        signInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //  Log.e(TAG, "onComplete: >>>>>>>>>>revokeAccess>>>>>>>>>>>>>>>>>" );
                    }
                });
    }

    ////////////////capcha///////////////
    private void handleSiteVerify(final String responseToken) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("secret", SECRET_KEY);
        hashMap.put("response", responseToken);
        Log.e(TAG, "handleSiteVerify " + hashMap);
        RetrofitServices.urlServiReCapcha.siteverify(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String respo = response.body().string().trim();
                    Log.e(TAG, "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getBoolean("success")) {
                        Log.e(TAG, "onResponse: " + jsonObject.getBoolean("success"));
                        //code logic when captcha returns true Toast.makeText(getApplicationContext(),String.valueOf(jsonObject.getBoolean("success")),Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), String.valueOf(jsonObject.getString("error-codes")), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "FALURE "+ t.toString());
            }
        });
    }

}
