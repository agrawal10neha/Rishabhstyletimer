package com.si.styletimer.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jinatonic.confetti.CommonConfetti;
import com.github.jinatonic.confetti.ConfettiManager;

import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivitySignupBinding;
import com.si.styletimer.databinding.DialogSignedupBinding;
import com.si.styletimer.databinding.TermAndCondiPopupBinding;
import com.si.styletimer.retrofit.Environment;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private ActivitySignupBinding binding;
    private Context context;
    String flag = "", news = "0", tickToAllow = "0";
    private SupportedDatePickerDialog datePickerDialog;
    String isheard = "";
    String[] country = {"Wie hast du von styletimer erfahren?", "In einem Salon", "Von einem anderen Kunden", "Facebook", "Instagram", "Google", "Magazin/ Printwerbung", "Außenwerbung", "TV/ Kino", "Events", "Andere"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        context = this;
        inIt();
        onClick();

    }

    private void inIt() {
        customTextView(binding.tvTerms);
        setupSpinner();
        flag = getIntent().getStringExtra("flag");
        Log.e(TAG, "inIt: " + flag);

        binding.etDob.setOnClickListener(view -> {
            Utility.hideSoftKeyboard(this);
            openDatePicker();
        });

        binding.cbTickToAllow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tickToAllow = "1";
                } else {
                    tickToAllow = "0";
                }
            }
        });
    }

    private void setupSpinner() {
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.spinnnerHeard.setAdapter(aa);

        binding.spinnnerHeard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isheard = country[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onClick() {

        textWatcher();

        binding.btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.etFirstName.getText().toString().equals("")) {

                    binding.ilFirstName.setError(context.getResources().getString(R.string.please_enter_firstname));
                    binding.ilFirstName.setBoxStrokeColor(context.getResources().getColor(R.color.red_error));

                } else if (binding.etLastName.getText().toString().equals("")) {

                    binding.ilLastName.setError(context.getResources().getString(R.string.please_enter_lastname));
                    binding.ilLastName.setBoxStrokeColor(context.getResources().getColor(R.color.red_error));

                } else if (binding.etEmail.getText().toString().equals("")) {

                    binding.ilEmail.setError(context.getResources().getString(R.string.please_enter_valid_email));
                    binding.ilEmail.setBoxStrokeColor(context.getResources().getColor(R.color.red_error));

                } else if (!Utility.isValidEmailAddress(binding.etEmail.getText().toString())) {

                    binding.ilEmail.setError("Bitte gültige E-Mail Adresse eingeben");
                    binding.ilEmail.setBoxStrokeColor(context.getResources().getColor(R.color.red_error));

                } else if (binding.etDob.getText().toString().equals("")) {
                    binding.tilDob.setError(context.getResources().getString(R.string.please_enter_dob));
                    binding.tilDob.setBoxStrokeColor(context.getResources().getColor(R.color.red_error));

                } else if (binding.etPassword.getText().toString().equals("")) {

                    binding.ilPassword.setError(context.getResources().getString(R.string.please_enter_password));
                    binding.ilPassword.setBoxStrokeColor(context.getResources().getColor(R.color.red_error));

                } else if (!binding.etPassword.getText().toString().equals(binding.etCnfPassword.getText().toString())) {

                    binding.ilCnfPassword.setError(context.getResources().getString(R.string.confirm_password_not_match));
                    binding.ilCnfPassword.setBoxStrokeColor(context.getResources().getColor(R.color.red_error));

                }

//                else if (isheard.equals("")) {
//                    Toast.makeText(context, "Please select how did you heard about ", Toast.LENGTH_SHORT).show();
//                    return;
//                } else if (isheard.equals("How did you find out about Styletimer?*")) {
//                    Toast.makeText(context, "Please select how did you heard about ", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                else if (!binding.cb.isChecked()) {

                    Toast.makeText(context, context.getResources().getString(R.string.please_check_term_and_condition), Toast.LENGTH_SHORT).show();

                } else if (binding.etFirstName.getText().toString().startsWith(" ") || binding.etFirstName.getText().toString().endsWith(" ")) {
                    binding.ilFirstName.setError("Bitte Vorname eingeben");

                } else if (binding.etLastName.getText().toString().startsWith(" ") || binding.etLastName.getText().toString().endsWith(" ")) {
                    binding.ilFirstName.setError("Bitte Nachname eingeben");

                } else {

                    if (binding.cbNews.isChecked()) {
                        news = "1";
                    } else {
                        news = "0";
                    }

                    if (Utility.connectionStatus(context)) {
                        sighnUp();
                    } else {
                        Toast.makeText(context, "Es besteht keine Internetverbindung", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.llSignMain.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override

            public boolean onTouch(View v, MotionEvent event) {

                Log.e("-->", "signup hide");
                Utility.hideSoftKeyboard(SignupActivity.this);
                return false;
            }

        });


    }

    private void dialogSingedUp() {

        Dialog alertDialog = new Dialog(context);
        DialogSignedupBinding pauseBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_signedup, null, false);
        alertDialog.setContentView(pauseBinding.getRoot());

       pauseBinding.tvResendMail.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               resend_activation_mail();
           }
       });


        pauseBinding.btSignup.setOnClickListener(view -> {
            alertDialog.dismiss();
            Intent in = new Intent(getApplicationContext(), LoginActivity.class);
            //  in.putExtra("flag",flag);
            in.putExtra("destination", flag);
            startActivity(in);
            finish();
        });

        alertDialog.show();

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
                        //Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_LONG).show();
                        Toast.makeText(context, "Aktivierungslink erneut gesendet", Toast.LENGTH_LONG).show();

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

    private void openDatePicker() {
        final Calendar newCalendar = Calendar.getInstance();
        Locale.setDefault(Locale.GERMAN);

        datePickerDialog = new SupportedDatePickerDialog(this,R.style.SpinnerDatePickerDialogTheme,(view, year, monthOfYear, dayOfMonth) -> {
            Log.e(TAG, "openDatePicker: " + Utility.dateBuiilder1(year, monthOfYear, dayOfMonth));
            binding.etDob.setText(Utility.dateBuiilder1(year, monthOfYear, dayOfMonth));
            binding.tilDob.setError(null);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private void textWatcher() {

        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->", String.valueOf(s));

                if (Utility.isValidEmailAddress(String.valueOf(s))) {
                    Log.e("---->", "in");
                    binding.ilEmail.setError(null);
                    binding.ilEmail.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.ilEmail.setHintTextAppearance(R.style.EditTextHintStyle);
                    //binding.etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tic_yelow, 0);
                } else {
                    Log.e("---->", "out");
                    binding.ilEmail.setError("Bitte gültige E-Mail Adresse eingeben");
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

        binding.etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->", String.valueOf(s));

                if (s.length() > 0) {

                    if (String.valueOf(s).startsWith(" ") || String.valueOf(s).endsWith(" ")) {
                        binding.ilFirstName.setError("Bitte Vorname eingeben");

                    } else {

                        binding.ilFirstName.setError(null);
                        binding.ilFirstName.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                        binding.ilFirstName.setHintTextAppearance(R.style.EditTextHintStyle);
                    }


                } else {

                    binding.ilFirstName.setError(context.getResources().getString(R.string.please_enter_firstname));
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->", String.valueOf(s));

                if (s.length() > 0) {

                    if (String.valueOf(s).startsWith(" ") || String.valueOf(s).endsWith(" ")) {
                        binding.ilLastName.setError("Bitte Nachname eingeben");

                    } else {

                        binding.ilLastName.setError(null);
                        binding.ilLastName.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                        binding.ilLastName.setHintTextAppearance(R.style.EditTextHintStyle);
                    }

                } else {

                    binding.ilLastName.setError(context.getResources().getString(R.string.please_enter_lastname));
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

                Log.e("---->", String.valueOf(s));

                /*if(String.valueOf(s).startsWith(" ") || String.valueOf(s).endsWith(" ")){
                    binding.ilPassword.setError("Enter a valid password");
                    *//*binding.tiPassword.setError(null);
                    binding.tiPassword.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.tiPassword.setHintTextAppearance(R.style.EditTextHintStyle);*//*

                }else */
                if (s.length() < 8) {
                    binding.ilPassword.setError("Bitte mindestens 8 Zeichen verwenden");

                } else {
                    if (String.valueOf(s).startsWith(" ") || String.valueOf(s).endsWith(" ")) {
                        binding.ilPassword.setError("Bitte gültiges Passwort eingeben");
                    } else {
                        binding.ilPassword.setError(null);
                        binding.ilPassword.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                        binding.ilPassword.setHintTextAppearance(R.style.EditTextHintStyle);

                    }

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.etCnfPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->", String.valueOf(s));

                if (String.valueOf(s).equals(binding.etPassword.getText().toString())) {

                    binding.ilCnfPassword.setError(null);
                    binding.ilCnfPassword.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.ilCnfPassword.setHintTextAppearance(R.style.EditTextHintStyle);

                } else {
                    binding.ilCnfPassword.setError(context.getResources().getString(R.string.confirm_password_not_match));
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

    private void sighnUp() {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.FIRST_NAME, binding.etFirstName.getText().toString());
        hashMap.put(AppConstants.LAST_NAME, binding.etLastName.getText().toString());
        hashMap.put(AppConstants.EMAIL, binding.etEmail.getText().toString());
        hashMap.put(AppConstants.PASSWORD, binding.etPassword.getText().toString());
        hashMap.put(AppConstants.CON_PASSWORD, binding.etCnfPassword.getText().toString());
        hashMap.put(AppConstants.NEWSLATTERS, news);
        hashMap.put("birth_date", binding.etDob.getText().toString());
        hashMap.put("how_toknow", isheard);
        hashMap.put("service_email", tickToAllow);

        Log.e("-->", "REGISTRATION " + hashMap);

        RetrofitServices.urlServices.user_registration_token(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("REGISTRATION-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {

                       // Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_LONG).show();


                        dialogSingedUp();
                        //   Bungee.slideLeft(context);

                    } else if (jsonObject.getInt("status") == 0) {
                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_LONG).show();
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
        finish();
        // Bungee.slideRight(context);
    }
/*
    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "I agree to the ");
        spanTxt.append("term and conditions");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                term_and_condi_popup("terms");
            }
        }, spanTxt.length() - "term and conditions".length(), spanTxt.length(), 0);
        spanTxt.append(" &");
        spanTxt.setSpan(new ForegroundColorSpan(Color.BLACK), 34, spanTxt.length(), 0);
        spanTxt.append(" privacy policy");
        spanTxt.append(" of styletimer.");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                term_and_condi_popup("policy");
            }
        }, spanTxt.length() - " privacy policy".length()-14, spanTxt.length()-15, 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }
*/
    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "Ich stimme den ");
        spanTxt.append("Nutzungsbedingungen");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                term_and_condi_popup("terms");
            }
        }, spanTxt.length() - "nutzungsbedingungen".length(), spanTxt.length(), 0);
        spanTxt.append(" und");
       //spanTxt.setSpan(new ForegroundColorSpan(Color.BLACK), 34, spanTxt.length(), 0);
        spanTxt.append(" Datenschutzbestimmungen");
        spanTxt.append(" von styletimer zu.");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                term_and_condi_popup("policy");
            }
        }, spanTxt.length() - " datenschutzbestimmungen".length()-18, spanTxt.length()-19, 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    private void term_and_condi_popup(String lastname){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        TermAndCondiPopupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.term_and_condi_popup, null, false);
        builder.setView(binding.getRoot());

        AlertDialog dialog = builder.create();

        String url = Environment.getBaseUrl()+"user/get_staticpage/"+lastname;
        Log.e(TAG, "term_and_condi_popup: "+ url );

        Utility.showProgressHUD(context);
        binding.webview.setWebChromeClient(new WebChromeClient(){

            public void onProgressChanged(WebView webView1, int newProgress){
                Utility.hideProgressHud();
            }
        });
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.loadUrl(url);
        binding.webview.setWebViewClient(new WebViewClient(){
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

}
