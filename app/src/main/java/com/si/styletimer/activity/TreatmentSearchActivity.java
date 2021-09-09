package com.si.styletimer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si.styletimer.R;
import com.si.styletimer.adapters.CatSearchAdapter;
import com.si.styletimer.adapters.TreatmentSearchAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivityTreatmentSearchBinding;
import com.si.styletimer.models.CatSearchModell;
import com.si.styletimer.models.search_treatment_model.TreatmentSearchModel;
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

public class TreatmentSearchActivity extends AppCompatActivity {
    private static final String TAG = "TreatmentSearchActivity";
    private ActivityTreatmentSearchBinding binding;
    private Context context;
    private List<TreatmentSearchModel> searchModelList = new ArrayList<>();
    private Gson gson;
    private String catId="",caten = "",category_id = "";
    private TreatmentSearchAdapter adapter;
    private List<CatSearchModell> catSearchModell = new ArrayList<>();
    private CatSearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_treatment_search);
        context=this;
        gson=new Gson();

        inIt();

    }

    private void inIt() {
        catId=getIntent().getStringExtra("catId");
        category_id=getIntent().getStringExtra("category_id");
        caten=getIntent().getStringExtra("caten");
        binding.etSearch.setText(caten);
        binding.etSearch.requestFocus();

        getSubCategoryList();
        onClick();

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*adapter.getFilter().filter(s.toString());*/
                if(s.length()==0){
                    binding.ivClear.setVisibility(View.GONE);
                    binding.etSearch.setHint("Behandlung suchen");
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.rvSearch.setVisibility(View.VISIBLE);
                    binding.tvNoList.setVisibility(View.GONE);
                   // binding.tvNoList.setText("Leider konnten wir kein passendes\nErgebnis für , "+ s +" finden");
                    getSearchReasult(String.valueOf(s));
                } else if(s.length()>0){
                    binding.ivClear.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.rvSearch.setVisibility(View.VISIBLE);
                    binding.tvNoList.setVisibility(View.GONE);
                    getSearchReasult(String.valueOf(s));
                }
            }
        });
    }
    private void onClick() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","no");
                setResult(1, returnIntent);
                finish();
              //  Bungee.slideRight(context);
            }
        });

        binding.ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etSearch.setText("");
                binding.etSearch.setHint("Behandlung suchen");
                binding.rvSearch.setVisibility(View.GONE);
                binding.tvNoList.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result","no");
        setResult(1, returnIntent);
        finish();
      //  Bungee.slideRight(context);
    }

    private void setUpRecyclerView() {

        adapter = new TreatmentSearchAdapter(context,searchModelList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setAdapter(adapter);

        if (searchModelList.size()>0)
        {
            binding.tvNoList.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }else {
            binding.recyclerView.setVisibility(View.GONE);
            binding.tvNoList.setVisibility(View.VISIBLE);
        }
        
        
        
        adapter.setOnItemClickListener(new TreatmentSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if(view.getId()==R.id.llTreatment){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result","yes");

                    if(searchModelList.get(position).getParentId().equals("0")){
                        returnIntent.putExtra("main_id",searchModelList.get(position).getId());
                        returnIntent.putExtra("result_id","0");
                    }else {
                        returnIntent.putExtra("main_id","0");
                        returnIntent.putExtra("result_id",searchModelList.get(position).getId());
                    }

                    returnIntent.putExtra("result_name",searchModelList.get(position).getCategoryName());
                    //returnIntent.putExtra("result_id",searchModelList.get(position).getId());
                    setResult(1,returnIntent);
                    finish();
                 //   Bungee.slideRight(context);
                }
            }
        });

        searchAdapter = new CatSearchAdapter(context, catSearchModell);
        binding.rvSearch.setLayoutManager(new LinearLayoutManager(context));
        binding.rvSearch.setAdapter(searchAdapter);

        searchAdapter.setOnItemClickListener(new CatSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent returnIntent = new Intent();

                if(catSearchModell.get(position).getParentId().equals("0")){
                    returnIntent.putExtra("main_id",catSearchModell.get(position).getId());
                    returnIntent.putExtra("result_id","0");
                }else {
                    returnIntent.putExtra("main_id","0");
                    returnIntent.putExtra("result_id",catSearchModell.get(position).getId());
                }

                returnIntent.putExtra("result","yes");
                returnIntent.putExtra("result_name",catSearchModell.get(position).getCategoryName());

                setResult(1,returnIntent);
                finish();

            }
        });

    }

    private void getSubCategoryList(){
        final ProgressDialog pd =new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put("cid", catId );


        Log.e("-->", "Sub category list " + hashMap);

        RetrofitServices.urlServices.subcategory_list(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("Sub category list-->", "onResponse:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if(jsonObject.getInt("status")==1){

                        searchModelList = gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<TreatmentSearchModel>>() {}.getType());

                        setUpRecyclerView();

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

                pd.dismiss();
                Log.e("-->","FALURE ");
            }
        });
    }

    private void getSearchReasult(String keyword){

        /** Clearing the list */
        catSearchModell.clear();

        final ProgressDialog pd =new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put("keys",  keyword);
        hashMap.put("category_id", category_id);


        Log.e(TAG, "search list " + hashMap);

        RetrofitServices.urlServi.category_subcategory_search(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("search list-->", "onResponse:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if(jsonObject.getInt("status")==1){

                        catSearchModell = gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<CatSearchModell>>() {}.getType());
                        searchAdapter.setCatSearchModellList(catSearchModell);
                        searchAdapter.notifyDataSetChanged();

                    }else if(jsonObject.getInt("status")==0){
                        catSearchModell.clear();
                        searchAdapter.notifyDataSetChanged();
                       // Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    }

                    if (catSearchModell.size()>0)
                    {
                        binding.tvNoList.setVisibility(View.GONE);
                        binding.rvSearch.setVisibility(View.VISIBLE);
                    }else {
                        binding.tvNoList.setText("Leider konnten wir kein passendes\nErgebnis für "+ '\"'+ keyword +'\"'+" finden");
                        binding.rvSearch.setVisibility(View.GONE);
                        binding.tvNoList.setVisibility(View.VISIBLE);
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
