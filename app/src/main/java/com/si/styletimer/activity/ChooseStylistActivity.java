package com.si.styletimer.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.adapters.ChooseStyleAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.controller.Controller;
import com.si.styletimer.databinding.ActivityChooseStylistBinding;
import com.si.styletimer.models.ChooseStyleModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseStylistActivity extends AppCompatActivity {
    private static final String TAG = "ChooseStylistActivity";
    private Context context;
    private ActivityChooseStylistBinding binding;
    private List<ChooseStyleModel> chooseStyleModelList;
    private ChooseStyleAdapter chooseStyleAdapter;
    private Gson gson;
    private String empId="" , date="";
    private Controller controller;
    private JSONArray jsonArrayEmployee = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_choose_stylist);
        context = this;
        controller = (Controller)context.getApplicationContext();
        gson = new Gson();
        initviews();
    }

    private void initviews() {
        set_adapter();

        if(controller.day.equals("")){
            date="";
        }else if(controller.day.equals("Today")){
            date=Utility.getCurrentDateYY();
        }else if(controller.day.equals("Tomorrow")){
            date = Utility.getTomorrow();
        }else {
            date= controller.datecalendar;
        }

        if (Utility.connectionStatus(context)){
            book_employee();
        }else {
            Utility.nointernettoast(context);
        }


        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
              //  Bungee.slideLeft(context);
            }
        });

        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseStyleAdapter.getmSelectedItem() == -1){
                    Toast.makeText(context, "Bitte wähle einen Mitarbeiter aus\n", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        String iddd = chooseStyleAdapter.getChooseStyleModel().get(chooseStyleAdapter.getmSelectedItem()).getId();
                        Log.e(TAG, "onClick: "+iddd +" "+ chooseStyleAdapter.getChooseStyleModel().get(chooseStyleAdapter.getmSelectedItem()));
                        Intent intent  =new Intent( context,SelectDateActivity.class);
                        intent.putExtra(AppConstants.FLAG,iddd);
                        intent.putExtra(AppConstants.SALON_ID,Session.getSalon_id(context));
                        intent.putExtra(AppConstants.DATE,Utility.getCurrentDateYY());
                        intent.putExtra(AppConstants.FLAGTWO,"");
                        intent.putExtra(AppConstants.AVAILABLE_EMPLOYEE,jsonArrayEmployee.toString());

                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
    }

    private void set_adapter(){
        chooseStyleModelList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recystyle.setLayoutManager(linearLayoutManager);
        binding.recystyle.setHasFixedSize(true);
        chooseStyleAdapter = new ChooseStyleAdapter(context,chooseStyleModelList,empId,Session.getSalon_id(context));
        binding.recystyle.setAdapter(chooseStyleAdapter);
        chooseStyleAdapter.notifyDataSetChanged();
    }

    private void book_employee() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.SALON_ID, Session.getSalon_id(context));
        hashMap.put(AppConstants.DATE, date);

        Log.e(TAG, "book_employee: "+hashMap );

        RetrofitServices.urlServices.book_employee(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res = response.body().string().trim();
                    JSONObject jsonObject = new JSONObject(res);

                    Log.e(TAG, "book_employee: "+res );

                    if (jsonObject.getInt("status") == 1){
                        JSONArray data  = jsonObject.getJSONArray("data");
                        jsonArrayEmployee = data;

                        if (data.length() >1){
                            ChooseStyleModel chooseStyleModel = new ChooseStyleModel();
                            chooseStyleModel.setFirstName("any");
                            chooseStyleModel.setLastName("any");
                            chooseStyleModel.setId("any");
                            chooseStyleModel.setProfilePic("any");
                            chooseStyleModelList.add(chooseStyleModel);
                            List<ChooseStyleModel> chooseStyleModelListtwo  =new ArrayList<>();
                            chooseStyleModelListtwo = gson.fromJson(data.toString(), new TypeToken<ArrayList<ChooseStyleModel>>() {}.getType());
                            chooseStyleModelList.addAll(chooseStyleModelListtwo);
                            chooseStyleAdapter.setChooseStyleModelList(chooseStyleModelList,chooseStyleModelList.size()+"");
                            chooseStyleAdapter.notifyDataSetChanged();
                        }else {
                            chooseStyleModelList = gson.fromJson(data.toString(), new TypeToken<ArrayList<ChooseStyleModel>>() {}.getType());
                            chooseStyleAdapter.setChooseStyleModelList(chooseStyleModelList,chooseStyleModelList.size()+"");
                            chooseStyleAdapter.notifyDataSetChanged();
                        }
                        binding.tvolisting.setVisibility(View.GONE);
                    }else {
                        binding.tvolisting.setVisibility(View.VISIBLE);
                        if(jsonObject.getString("response_message").equalsIgnoreCase("No employee available"))
                        {
                            binding.tvolisting.setText("Kein Mitarbeiter verfügbar");
                        }

                    }
                } catch (Exception e) {
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
