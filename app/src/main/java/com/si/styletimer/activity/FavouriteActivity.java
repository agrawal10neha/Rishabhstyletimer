package com.si.styletimer.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si.styletimer.R;
import com.si.styletimer.Session.RealmController;
import com.si.styletimer.Session.Session;
import com.si.styletimer.adapters.FavouriteListAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.controller.Controller;
import com.si.styletimer.databinding.ActivityFavouriteBinding;
import com.si.styletimer.models.fav_list_model.FavouriteListModel;
import com.si.styletimer.models.fav_list_model.Sercvice;
import com.si.styletimer.retrofit.JsonUtil;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteActivity extends AppCompatActivity {

    private ActivityFavouriteBinding binding;
    private Context context;
    private static final String TAG = "FavouriteActivity";
    private List<FavouriteListModel> favouriteListModel = new ArrayList<>();
    private FavouriteListAdapter adapter;
    private List<Sercvice> sercviceList;
    private Controller controller;
    private Gson gson;
    private RealmController realmController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_favourite);
        context=this;
        gson = new Gson();
        controller = (Controller) context.getApplicationContext();
        realmController = new RealmController(context);

        inIt();

    }

    private void inIt() {
        onClick();
        setUpRv();
        getList();
    }

    private void setUpRv() {
        binding.rv.setLayoutManager(new LinearLayoutManager(context));
        adapter=new FavouriteListAdapter(context,favouriteListModel);
        binding.rv.setAdapter(adapter);

        adapter.setOnItemClickListener(new FavouriteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                sercviceList = new ArrayList<>();
                String strSalonId = adapter.getFavouriteListModel().get(position).getSalonId();
                sercviceList = adapter.getFavouriteListModel().get(position).getSercvices();

                if(view.getId()==R.id.llFav){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    removeFavourite(favouriteListModel.get(position).getSalonId(),position);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getResources().getString(R.string.are_you_remove_favourite)).setPositiveButton("Ja", dialogClickListener).setNegativeButton("Nein", dialogClickListener).show();

                }else if(view.getId()==R.id.rlMain) {
                    realmController.clearServices();
                    Session.setSalon_id(context,strSalonId);
                    Session.setServiceIds(context,returnServiceIds(sercviceList));
                    Intent intent = new Intent(context, SalonDetailActivity.class);
                    intent.putExtra(AppConstants.SERVICEIDS,returnServiceIds(sercviceList));
                    startActivity(intent);

                }
            }
        });


    }

    private void onClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
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
      //  Bungee.slideRight(context);
    }

    private void getList() {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.PAGE, "");

        Log.e(TAG, "getList: "+hashMap);

        RetrofitServices.urlServices.myfavourite_salon(hashMap).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e(TAG, "onResponse: "+respo );
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {
                        favouriteListModel = gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<FavouriteListModel>>() {}.getType());
                        adapter.setFavouriteListModelList(favouriteListModel);
                        adapter.notifyDataSetChanged();

                    } else if (jsonObject.getInt("status") == 0) {
                        binding.layoutNoData.setVisibility(View.VISIBLE);
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

    private void removeFavourite(String str_salon_id,int position) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.SALON_ID, str_salon_id);

        Log.e(TAG, "get_cart_Count: "+hashMap );

        RetrofitServices.urlServices.favourite_salon(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (JsonUtil.mainresp(context, response)) {
                    try {
                        JSONObject jsonObject = new JSONObject(JsonUtil.resp(response));

                        if (jsonObject.getInt("status") == 1) {

                            //notifyItemRemoved(position);
                            favouriteListModel.remove(position);
                            adapter.notifyDataSetChanged();

                            if(favouriteListModel.isEmpty()){
                                binding.layoutNoData.setVisibility(View.VISIBLE);
                            }else binding.layoutNoData.setVisibility(View.GONE);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("-->", "FALURE ");
            }
        });
    }

    private String returnServiceIds(List<Sercvice> sercviceList){
        Log.e(TAG, "returnServiceIds: "+sercviceList.toString() );
        String ids = "";
        StringBuilder stringBuilder = new StringBuilder();
        if (sercviceList.size()>0){
            for (int i = 0; i <sercviceList.size(); i++) {
                sercviceList.get(i).getSubcategoryId();
                stringBuilder.append(sercviceList.get(i).getSubcategoryId() + ",");
            }
            ids = stringBuilder.toString().substring(0, stringBuilder.length() - ",".length());
            return ids;
        }
        return ids;
    }


    @Override
    protected void onResume() {
        super.onResume();
        favouriteListModel.clear();
        adapter.notifyDataSetChanged();
        getList();
    }
}
