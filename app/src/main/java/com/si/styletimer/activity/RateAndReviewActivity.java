package com.si.styletimer.activity;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivityRateAndReviewBinding;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateAndReviewActivity extends AppCompatActivity {

    private ActivityRateAndReviewBinding binding;
    private String bookId="",salonId="";
    private Context context;
    private float ratingg=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_rate_and_review);
        context=this;
        getIntentData();

        inIt();

    }

    private void getIntentData() {
        bookId=getIntent().getStringExtra("bookId");
        salonId=getIntent().getStringExtra("saloonId");
    }

    private void inIt() {

        watcherText();

        onClick();

    }

    private void onClick() {
        binding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!binding.etReview.getText().toString().equals("")){

                    if(ratingg<1){
                        Toast.makeText(context, "Bitte Sterne-Bewertung wählen", Toast.LENGTH_SHORT).show();
                    }else {
                        postReview();
                    }
                }else {
                    binding.ilReview.setError("Bitte Bewertung schreiben");
                }
            }
        });

        binding.ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingg=rating;
            }
        });

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void watcherText() {

        binding.etReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->",String.valueOf(s));

                if(s.length()>0){
                    Log.e("---->","in");
                    binding.ilReview.setError(null);
                    binding.ilReview.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.ilReview.setHintTextAppearance(R.style.EditTextHintStyle);
                    //binding.etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tic_yelow, 0);
                }else {
                    Log.e("---->","out");
                    binding.ilReview.setError("Bitte Bewertung schreiben");
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

    private void postReview() {

        final ProgressDialog pd =new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.BOOK_ID, bookId);
        hashMap.put(AppConstants.SALON_ID, salonId);

        hashMap.put(AppConstants.RATING, String.valueOf(binding.ratingbar.getRating()));
        hashMap.put(AppConstants.REVIEW, binding.etReview.getText().toString());


        Log.e("-->", "Review " + hashMap);

        RetrofitServices.urlServices.addreview_service(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("Review-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if(jsonObject.getInt("status")==1){

                        finish();

                    }else if(jsonObject.getInt("status")==0){
                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
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
