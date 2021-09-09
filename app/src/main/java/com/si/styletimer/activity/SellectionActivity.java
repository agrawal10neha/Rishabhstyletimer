package com.si.styletimer.activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivitySellectionBinding;
import com.si.styletimer.databinding.TermAndCondiPopupBinding;
import com.si.styletimer.retrofit.Environment;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellectionActivity extends AppCompatActivity {

    private ActivitySellectionBinding binding;
    private Context context;
    private static final String TAG = "SellectionActivity";
    private String flag = "", destination = "";
    private String fronPage = "";

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient signInClient;

    private String Id = "", Name = "", Email = "", loginWith = "";
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    private static final String EMAIL = "email";
    private String profile_image = "";
    private WeakReference<SellectionActivity> weakAct = new WeakReference<>(this);
    private GoogleSignInAccount account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sellection);
        context = this;

        inIt();
    }

    private void inIt() {

        onClick();
        getIntentData();
        inIt_facebook();
       // setup_google();
//Google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        signInClient = GoogleSignIn.getClient(this, gso);

    }

    private void getIntentData() {
        flag = getIntent().getStringExtra("flag");
        Log.e(TAG, "getIntentData: " + flag);
        destination = getIntent().getStringExtra("destination");
    }


    private void onClick() {
        binding.tvTermAndCondi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                term_and_condi_popup();
            }
        });
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LoginActivity.class).putExtra("flag", flag).putExtra("destination", destination));
                finish();
                //  Bungee.slideLeft(context);
            }
        });
        binding.tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SignupActivity.class)
                        .putExtra("flag", flag)
                        .putExtra("destination", destination));
                finish();
                //  Bungee.slideLeft(context);
            }
        });

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag != null && flag.equals("dashboard")) {
                    finish();
                    //  Bungee.slideRight(context);
                } else {
                    finish();
                    //  Bungee.slideRight(context);
                }

            }
        });

        binding.btGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: here 111");
               // signIn();
                Intent signInIntent = signInClient.getSignInIntent();
                startActivityForResult(signInIntent, 100);
            }
        });

        binding.btFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onClick: here requestCode = " + requestCode+" resultCode= "+ resultCode+" data = "+data);
        if (data != null) {
            if (requestCode == 100) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                Log.e("Google login result---", String.valueOf(task.isSuccessful()));
                Log.e("Google login result---", String.valueOf(task.isCanceled()));
                handleSignInResult(task);
            } else {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {


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


/*
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            new GetProfileDetails(account, weakAct, TAG).execute();
        } catch (ApiException e) { //cancel choose acc will come here with status code 12501 if not check RESULT_OK
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
*/


    private void inIt_facebook() {
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
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
                        Log.e(TAG, "onCompleted: " + profile_image );
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


/*
    public void setup_google() {

        // Scope myScope = new Scope("https://www.googleapis.com/auth/user.birthday.read");
        Scope myScope = new Scope("https://www.googleapis.com/auth/user.birthday.read");
        Scope myScope2 = new Scope(Scopes.PLUS_ME);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(myScope, myScope2)
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this, gso);


        //  GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            signInClient.signOut();
        }

        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            Log.e(TAG, "onCreate: null ");
        } else {
            Log.e(TAG, "onCreate: not  null ");
            SharedPreferences sharedPref = getSharedPreferences(account.getId(), MODE_PRIVATE);
            if (sharedPref.contains("gender")) {
                Log.e(TAG, "onCreate: print   ");
                printBasic();
                printAdvanced();
            } else {
                new GetProfileDetails(account, weakAct, TAG).execute();
                Log.e(TAG, "onCreate: print   account");
            }
        }
    }
*/






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
            hashMap.put(AppConstants.SOCIAL_IMAGEURL, profile_image );
        } else {
            hashMap.put(AppConstants.SOCIAL_IMAGEURL, "");
        }

        Log.e("-->", "SOCIAL LOGIN " + hashMap);

        RetrofitServices.urlServices.socialLogin(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("SOCIAL LOGIN-->", "onResponse: " + respo);
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

                        if (flag.equals("dashboard")) {
                            Intent in = new Intent(getApplicationContext(), MyBookingActivity.class);
                            startActivity(in);
                            finish();

                        } else {

                            finish();
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

                pd.dismiss();
                Log.e("-->", "FALURE ");
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (flag.equals("dashboard")) {
            finish();
            //  Bungee.slideRight(context);
        } else {
            finish();
            //   Bungee.slideRight(context);
        }
    }


    private void term_and_condi_popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        TermAndCondiPopupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.term_and_condi_popup, null, false);
        builder.setView(binding.getRoot());

        AlertDialog dialog = builder.create();
        dialog.show();

        String url = Environment.getBaseUrl() + "user/get_staticpage/terms";
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
    }

    private void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, 100);
    }

/*
    static class GetProfileDetails extends AsyncTask<Void, Void, com.google.api.services.people.v1.model.Person> {

        private PeopleService ps;
        private int authError = -1;
        private WeakReference<SellectionActivity> weakAct;
        private String TAG;

        GetProfileDetails(GoogleSignInAccount account, WeakReference<SellectionActivity> weakAct, String TAG) {
            this.TAG = TAG;
            this.weakAct = weakAct;
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    this.weakAct.get(), Collections.singleton(Scopes.PROFILE));
            credential.setSelectedAccount(new Account(account.getEmail(), "com.google"));
            HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
            JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
            ps = new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("Google Sign In Quickstart")
                    .build();
        }

        @Override
        protected com.google.api.services.people.v1.model.Person doInBackground(Void... params) {
            com.google.api.services.people.v1.model.Person meProfile = null;
            try {
                meProfile = ps
                        .people()
                        .get("people/me")
                        .setPersonFields("genders,birthdays")
                        .execute();
            } catch (UserRecoverableAuthIOException e) {
                e.printStackTrace();
                authError = 0;
            } catch (GoogleJsonResponseException e) {
                e.printStackTrace();
                authError = 1;
            } catch (IOException e) {
                e.printStackTrace();
                authError = 2;
            }
            return meProfile;
        }

        @Override
        protected void onPostExecute(com.google.api.services.people.v1.model.Person meProfile) {
            SellectionActivity mainAct = weakAct.get();
            if (mainAct != null) {
                mainAct.printBasic();
                if (authError == 0) { //app has been revoke, re-authenticated required.
                    mainAct.signIn();
                } else if (authError == 1) {
                    Log.e(TAG, "People API might not enable at" +
                            " https://console.developers.google.com/apis/library/people.googleapis.com/?project=<project name>");
                } else if (authError == 2) {
                    Log.e(TAG, "API io error");
                } else {
                    if (meProfile != null) {
                        mainAct.saveAdvanced(meProfile);
                        mainAct.printAdvanced();

                    }
                }
            }
        }
    }
*/
   /* private void printBasic() {
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Log.e(TAG, "latest sign in: "
                    + "\n\tPhoto url:" + account.getPhotoUrl()
                    + "\n\tEmail:" + account.getEmail()
                    + "\n\tDisplay name:" + account.getDisplayName()
                    + "\n\tFamily(last) name:" + account.getFamilyName()
                    + "\n\tGiven(first) name:" + account.getGivenName()
                    + "\n\tId:" + account.getId()
                    + "\n\tIdToken:" + account.getIdToken()
            );

            Id = account.getId();
            Name = account.getDisplayName();
            Email = account.getEmail();
            profile_image = account.getPhotoUrl().toString();
            Log.e(TAG, "handleSignInResult: "+ profile_image );

        } else {
            Log.w(TAG, "basic info is null");
        }
    }

    private void saveAdvanced(com.google.api.services.people.v1.model.Person meProfile) {
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            SharedPreferences sharedPref = getSharedPreferences(account.getId(), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            List<Gender> genders = meProfile.getGenders();
            if (genders != null && genders.size() > 0) {
                String gender = genders.get(0).getValue();
                Log.e(TAG, "onPostExecute gender: " + gender);
                editor.putString("gender", gender);
            } else {
                Log.e(TAG, "onPostExecute no gender if set to private ");
                editor.putString("gender", ""); //save as main key to know pref saved
            }
            List<Birthday> birthdays = meProfile.getBirthdays();
            if (birthdays != null && birthdays.size() > 0) {
                for (Birthday b : birthdays) { //birthday still able to get even private, unlike gender
                    Date bdate = b.getDate();
                    if (bdate != null) {
                        String bday, bmonth, byear;
                        if (bdate.getDay() != null) bday = bdate.getDay().toString();
                        else bday = "";
                        if (bdate.getMonth() != null) bmonth = bdate.getMonth().toString();
                        else bmonth = "";
                        if (bdate.getYear() != null) byear = bdate.getYear().toString();
                        else byear = "";
                        editor.putString("bday", bday);
                        editor.putString("bmonth", bmonth);
                        editor.putString("byear", byear);
                    }
                }
            } else {
                Log.w(TAG, "saveAdvanced no birthday");
            }
            editor.commit();  //next instruction is print from pref, so don't use apply()
        } else {
            Log.w(TAG, "saveAdvanced no acc");
        }
    }

    private void printAdvanced() {

        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            SharedPreferences sharedPref = getSharedPreferences(account.getId(), MODE_PRIVATE);
            if (sharedPref.contains("gender")) { //this checking works since null still saved
                String gender = sharedPref.getString("gender", "");
                Log.e(TAG, "gender: " + gender);
                String strgender = gender;
                if (sharedPref.contains("bday")) { //this checking works since null still saved
                    String bday = sharedPref.getString("bday", "");
                    String bmonth = sharedPref.getString("bmonth", "");
                    String byear = sharedPref.getString("byear", "");
                    Log.e(TAG, bday + "/" + bmonth + "/" + byear);
                    String strdateofbirth = byear + "/" + bmonth + "/" + bday;

                    Log.e(TAG, "printAdvanced: " + strdateofbirth +"   "+strgender);

                    SocialLogin("gmail");
                } else {
                    SocialLogin("gmail");
                    Log.w(TAG, "failed ot get birthday from pref");
                }
            } else {
                SocialLogin("gmail");
                Log.w(TAG, "failed ot get data from pref -2");
            }

        } else {
            Log.w(TAG, "failed ot get data from pref -1");
        }
    }*/
}
