package com.si.styletimer.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.si.styletimer.R;
import com.si.styletimer.Session.RealmController;
import com.si.styletimer.Session.Session;
import com.si.styletimer.adapters.SearchListAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivitySearchSalonBinding;
import com.si.styletimer.databinding.DialogAddSalonBinding;
import com.si.styletimer.databinding.PopupThankReviewBinding;
import com.si.styletimer.models.SearchListModel;
import com.si.styletimer.retrofit.Environment;
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

public class SearchSalonActivity extends AppCompatActivity {

    private ActivitySearchSalonBinding binding;
    private Context context;
    private static final String TAG = "SearchSalonActivity";
    private List<SearchListModel> searchListModel;
    private SearchListAdapter adapter;
    private boolean isLoading = false;
    private Gson gson;
    private String uid = "";
    private RealmController ergg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_salon);
        context = this;
        gson = new Gson();
        Utility.hideKeyboardNew(this);
        ergg=new RealmController(context);
        inIt();

    }

    private void inIt() {
        uid = Session.getUid(context);

        binding.etSearchKey.requestFocus();

        setupRv();
        onClick();
       // binding.tvRedirect.setPaintFlags( binding.tvRedirect.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);


        binding.etSearchKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String key = s.toString();
                if (isLoading) {
                    return;
                } else {
                    if (Utility.connectionStatus(context)) {
                        searchSalon(key);
                    } else {
                        Utility.nointernettoast(context);
                    }
                }
            }
        });

    }

    private void onClick() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.tvRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Environment.getBaseUrl() + "styletimer/user/registration"));
                startActivity(browserIntent);
            }
        });

        binding.tvRedirect.setOnClickListener(v ->dialogAddSalon());
    }

    private void setupRv() {
        searchListModel = new ArrayList<>();
        binding.rvList.setLayoutManager(new LinearLayoutManager(context));
        adapter = new SearchListAdapter(context, searchListModel);
        binding.rvList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener((view, position) -> {

            ergg.clearServices();
            String strSalonId = adapter.getSearchListModel().get(position).getId();
           // realmController.clearServices();
            Session.setSalon_id(context, strSalonId);
           // Session.setServiceIds(context,  returnServiceIds(sevicesLists));
            clear_cart_Count();
            Intent intent = new Intent(context, SalonDetailActivity.class);
          //  intent.putExtra(AppConstants.SERVICEIDS, returnServiceIds(sevicesLists));
            startActivity(intent);
        });

    }

    private void clear_cart_Count() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.SALON_ID, Session.getSalon_id(context));

        Log.e(TAG, "clear_cart_Count: --------->>>>>>>>>>>> "+hashMap );

        RetrofitServices.urlServices.clear_cart(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (JsonUtil.mainresp(context, response)) {

                        String resp = response.body().string().trim();
                        Log.e(TAG, "clear_cart_Count: "+resp );

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void searchSalon(String key) {
        isLoading = true;
        binding.rlNolisting.setVisibility(View.GONE);

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.KEY, key);

        RetrofitServices.urlServi.salon_list(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res = response.body().string().trim();
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.getInt("status")==1){

                        searchListModel = gson.fromJson(jsonObject.getJSONArray("data").toString(),
                                new TypeToken<ArrayList<SearchListModel>>(){}.getType());
                        adapter.setSearchListModelList(searchListModel);
                        adapter.notifyDataSetChanged();

                        if (searchListModel.size()>0){
                            binding.rlNolisting.setVisibility(View.GONE);
                        }else {
                            binding.rlNolisting.setVisibility(View.VISIBLE);
                        }
                    }else {
                        binding.rlNolisting.setVisibility(View.VISIBLE);
                        adapter.clear();
                    }

                    //Log.e(TAG, "onResponse: "+res );
                    isLoading = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoading = false;
            }
        });
    }

    private void dialogAddSalon(){


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogAddSalonBinding dialogbinding = DataBindingUtil.inflate(LayoutInflater.from(context),
               R.layout.dialog_add_salon, null, false);
        builder.setView(dialogbinding.getRoot());
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();

        dialogbinding.btnivclosedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogbinding.btnDetailResche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogbinding.tilSalon.setError(null);
                dialogbinding.tilCity.setError(null);
                dialogbinding.tilName.setError(null);

                String name = dialogbinding.etName.getText().toString();
                String city = dialogbinding.etCity.getText().toString();
                String salonName = dialogbinding.etSalonName.getText().toString();
                if (name.isEmpty()){
                    dialogbinding.tilName.setError("Bitte Name eingeben");
                    return;
                }

                if (salonName.isEmpty()){
                    dialogbinding.tilSalon.setError("Bitte Salonname eingeben");
                    return;
                }
                if (city.isEmpty()){
                    dialogbinding.tilCity.setError("Stadt wird benötigt");
                    return;
                }

                dialog.dismiss();
                addSalon(name,city,salonName);
            }
        });


        dialog.show();
    }
    private void addSalon(String name,String city,String salonName) {

        Utility.hideSoftKeyboard(SearchSalonActivity.this);
        binding.loader.setVisibility(View.VISIBLE);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put("name", name);
        hashMap.put("salon_city", city);
        hashMap.put("salon_name", salonName);

        RetrofitServices.urlServi.contact_for_salon(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res = response.body().string().trim();
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.getInt("status")==1) {
                        binding.rlNolisting.setVisibility(View.GONE);
                        Utility.hideSoftKeyboard(SearchSalonActivity.this);
                        popup();
                    }else {
                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                binding.loader.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
                binding.loader.setVisibility(View.GONE);
            }
        });
    }


    private void popup() {

        Utility.hideSoftKeyboard(SearchSalonActivity.this);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        PopupThankReviewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.popup_thank_review, null, false);
        builder.setView(binding.getRoot());
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();

        binding.tvtitle.setText("Deine Vorschlag wurde eingereicht");
        binding.tvOk.setText("Zurück zur Startseite");
        binding.btnivclosedialog.setVisibility(View.GONE);

        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        binding.btnivclosedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


}
