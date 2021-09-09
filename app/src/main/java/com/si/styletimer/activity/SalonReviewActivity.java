package com.si.styletimer.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si.styletimer.R;
import com.si.styletimer.adapters.SalonReviewAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivitySalonReviewBinding;
import com.si.styletimer.models.review.SalonReviewModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalonReviewActivity extends AppCompatActivity {
    private static final String TAG = "SalonReviewActivity";
    ActivitySalonReviewBinding binding;
    private Context context;
    private SalonReviewAdapter adapter;
    private List<SalonReviewModel> salonReviewModels;
    private String salonId="";
    private Gson gson;

    private int pastVisiblesItems = 0, visibleItemCount = 0, totalItemCount = 0;
    private Boolean loadingMore = false;
    private Boolean viewMore = false;
    private Boolean paginationCheck = false;
    private int page=1;
    private Dialog dialog;
    public static ImageView iv;
    private ArrayList<String> name =new ArrayList<>();
    private Set<String> nameSet = new LinkedHashSet<>();
    private ArrayList<String> id =new ArrayList<>();
    private ArrayList<String> FinalId =new ArrayList<>();
    private String cat="",rat="",totalreview = "";
    private float rateme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_salon_review);
        gson=new Gson();
        context = this;
        salonReviewModels = new ArrayList<>();
        rateme = getIntent().getFloatExtra(AppConstants.RATEME,0);
        totalreview = getIntent().getStringExtra(AppConstants.TOTALREVIEW);
        binding.tvreview.setText(""+rateme);
        binding.ratingbar.setRating(rateme);
        binding.tvtotalRevi.setText(totalreview+" bewertungen");
        initView();
        setUpPagination();
        getReviewList();
        getCategory();
        iv=findViewById(R.id.ivNoData);
    }

    private void setUpPagination() {

        LinearLayoutManager m = new LinearLayoutManager(context);
        binding.rvReview.setLayoutManager(m);
        binding.rvReview.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        adapter=new SalonReviewAdapter(context,salonReviewModels);
        binding.rvReview.setAdapter(adapter);

        binding.rvReview.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0)
                {
                    visibleItemCount = m.getChildCount();
                    totalItemCount =m.getItemCount();
                    pastVisiblesItems = m.findFirstVisibleItemPosition();

                    if (!loadingMore)
                    {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            viewMore = true;
                        }
                        if (viewMore)
                        {
                            loadingMore = true;

                            page++;
                            paginationCheck=true;
                            getReviewList();
                            Log.e(TAG, "onScrolled: ?>:<{}+_DAFCBHBCBHCB---HERE" );

                        }
                    }
                }
            }
        });

    }

    private void initView() {

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
             //   Bungee.slideRight(context);
            }
        });

        salonId = getIntent().getStringExtra("salonId");

        onClick();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
      //  Bungee.slideRight(context);
    }

    private void onClick() {

        binding.tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, FilterReviewActivity.class);

                i.putExtra("salonId", salonId);
                i.putExtra("cat", cat);
                i.putExtra("rat", rat);

                startActivityForResult(i, 1);
            }
        });
    }

    private void getReviewList(){
        final ProgressDialog pd =new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);

        hashMap.put(AppConstants.PAGE, String.valueOf(page));
        hashMap.put(AppConstants.SALON_ID, salonId );
        hashMap.put(AppConstants.RATING_POINT, rat );
        hashMap.put(AppConstants.CATEGORY, cat );

        Log.e(TAG, "getReviewList: "+hashMap );

        RetrofitServices.urlServices.review_listing(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();
                    String respo = response.body().string().trim();
                    Log.e(TAG, "onResponse: "+respo );

                    JSONObject jsonObject = new JSONObject(respo);

                    if(jsonObject.getInt("status")==1){

                        binding.layoutNodata.setVisibility(View.GONE);
                        binding.rvReview.setVisibility(View.VISIBLE);

                        if(page==1){

                            salonReviewModels = gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<SalonReviewModel>>() {}.getType());
                            adapter.setSalonReviewModelList(salonReviewModels);
                            adapter.notifyDataSetChanged();

                        }else if(page>1){

                            List<SalonReviewModel> salonReviewModels_2 = new ArrayList<>();
                            salonReviewModels_2  = gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<SalonReviewModel>>() {}.getType());
                            salonReviewModels.addAll(salonReviewModels_2);
                            adapter.notifyDataSetChanged();

                        }

/*
                        adapter.setOnItemClickListener(new SalonReviewAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                String services = adapter.getSalonReviewModel().get(position).getmServiceName();
                                Intent intent = new Intent(context,ReviewDetailActivity.class);
                                intent.putExtra(AppConstants.Review_id,salonReviewModels.get(position).getId());
                                intent.putExtra(AppConstants.SERVICES,services);
                                startActivity(intent);
                            }
                        });
*/


                    }else if(jsonObject.getInt("status")==0){

                        if(page==1){
                            //Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                            binding.layoutNodata.setVisibility(View.VISIBLE);
                            binding.rvReview.setVisibility(View.GONE);

                        }else if(page>1) {

                            binding.layoutNodata.setVisibility(View.GONE);
                            binding.rvReview.setVisibility(View.VISIBLE);
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
                pd.dismiss();
                Log.e("-->","FALURE ");
            }
        });
    }

    private void getCategory(){
        final ProgressDialog pd =new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);

        hashMap.put(AppConstants.SALON_ID, salonId );


        Log.e("-->", "Category list " + hashMap);

        RetrofitServices.urlServices.service_forratefilter(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("Category list -->", "onResponse:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if(jsonObject.getInt("status")==1){

                        JSONArray data = jsonObject.getJSONArray("data");

                        for(int i = 0;i<data.length(); i++){

                            JSONObject inData = data.getJSONObject(i);
                            id.add(inData.getString("id"));
                            nameSet.add(inData.getString("catname"));
                           /* if (inData.getString("name").equals("")) {
                                nameSet.add(inData.getString("catname"));
                            } else {
                                nameSet.add(inData.getString("name"));
                            }*/

                        }
                        name.addAll(nameSet);
                        Log.e(TAG, "onResponse: data " + data.length());
                        Log.e(TAG, "onResponse: id " + id.size());
                        Log.e(TAG, "onResponse: nameSet " + nameSet.size());
                        Log.e(TAG, "onResponse: name " + name.size());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            rat=data.getStringExtra("cat");
            cat=data.getStringExtra("rat");

            salonReviewModels.clear();
            page=1;
            getReviewList();

        }
    }

}
