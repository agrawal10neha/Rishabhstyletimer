package com.si.styletimer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivityReviewDetailBinding;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.DatabindingImageAdapter;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewDetailActivity extends AppCompatActivity {
    private static final String TAG = "ReviewDetailActivity";
    private ActivityReviewDetailBinding binding;
    private Context context;
    private String reviewId = "",services = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_review_detail);
        context=this;

        inIt();

    }

    private void inIt() {
        onClick();

        reviewId = getIntent().getStringExtra(AppConstants.Review_id);
        services = getIntent().getStringExtra(AppConstants.SERVICES);
        binding.tvServiceUsed.setText(services);
        if (!reviewId.equals("")){
            if (Utility.connectionStatus(context)){
                getDetail();
            }else {
                Utility.nointernettoast(context);
            }
        }

    }

    private void onClick() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
              //  Bungee.slideRight(context);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
       // Bungee.slideRight(context);
    }

    private void getDetail(){

        final ProgressDialog pd =new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();


        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);

        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.Review_id, reviewId );

        Log.e("-->", "review detail " + hashMap);

        RetrofitServices.urlServices.review_detail(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e(TAG, "onResponse: -->> "+respo );
                    JSONObject jsonObject = new JSONObject(respo);

                    if(jsonObject.getInt("status")==1){

                        JSONObject data =jsonObject.getJSONObject("data");

                        if(data.getString("anonymous").equals("1")){
                            binding.tvAno.setText("Anonym bewertet");
                            binding.tvCustomerName.setText("Anonym");
                            binding.tvAno.setVisibility(View.GONE);
                        }else {
                            binding.tvAno.setVisibility(View.GONE);
                            binding.tvCustomerName.setText(data.getString("first_name")/*+" " +data.getString("last_name")*/);
                            String url = RetrofitServices.USERS_IMAGE +  data.getString("user_id") + "/" + data.getString("profile_pic");
                            Log.e(TAG, "onResponse: "+url);
                            DatabindingImageAdapter.loadImage(binding.cv, url);
                        }

                        binding.ratingSaloon.setRating(Float.valueOf(data.getString("rate")));
                        binding.tvEmploye.setText("Behandelt von - "+data.getString("emp_fname")/*+" " +data.getString("emp_lname")*/);
                        binding.tvParlorsName.setText(data.getString("business_name"));
                        binding.tvSaloonName.setText("Kommentar von "+data.getString("business_name"));
                        binding.tvReview.setText(data.getString("review"));
                        String date[]=data.getString("created_on").split(" ");
                        binding.tvBookDate.setText(Utility.DateName(date[0])+" at "+date[1].substring(0,5));

                        JSONObject reply = data.getJSONObject("reply");

                        if(reply.getString("review").equals("")){
                            binding.llReply.setVisibility(View.GONE);
                        }else {
                            binding.llReply.setVisibility(View.VISIBLE);
                            String date2[]=reply.getString("created_on").split(" ");
                            binding.tvReplyDate.setText(Utility.DateName(date2[0])+" at "+date2[1].substring(0,5));
                        }

                        binding.tvReply.setText(reply.getString("review"));




                    }else if(jsonObject.getInt("status")==0){

                        //Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();

                    }

                }
                catch (Exception e){
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Log.e("-->","FALURE ");
            }
        });
    }

}
