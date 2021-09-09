package com.si.styletimer.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.si.styletimer.R;
import com.si.styletimer.adapters.ReviewFilterAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivityFilterReviewBinding;
import com.si.styletimer.models.FilterModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterReviewActivity extends AppCompatActivity {

    private ActivityFilterReviewBinding binding;
    private Context context;
    private static final String TAG = "FilterReviewActivity";

    private ArrayList<String> FinalId = new ArrayList<>();
    private ArrayList<String> FinalId2 = new ArrayList<>();
    private String rat = "", cat = "", salonId = "";
    private Dialog dialog;
    private ArrayList<String> name = new ArrayList<>();
  //  private Set<String> nameSet = new LinkedHashSet<>();
    private ArrayList<String> nameSet = new ArrayList<>();
    private ArrayList<String> id = new ArrayList<>();
    private ReviewFilterAdapter adapter;

    public static Boolean click = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter_review);
        context = this;

        inIt();

        salonId = getIntent().getStringExtra("salonId");
        getCategory();
        cat = getIntent().getStringExtra("cat");
        rat = getIntent().getStringExtra("rat");

        Log.e(TAG, "onCreate: cat $" + cat);
        Log.e(TAG, "onCreate: rat $" + rat);

        if (cat.equals("")) {
            Log.e(TAG, "onCreate: Cat Empty --");
        }

        if (cat != null && !cat.equals("")) {
            String c[] = cat.split(",");

            for (int i = 0; i < c.length; i++) {
                FinalId2.add(c[i]);
            }
            binding.tvCatagory.setText(FinalId2.size() + " Filter ausgewählt");
        }

        if (rat != null && !rat.equals("")) {
            String r[] = rat.split(",");
            for (int i = 0; i < r.length; i++) {
                if (r[i].equals("1")) {
                    binding.cb1.setChecked(true);
                } else if (r[i].equals("2")) {
                    binding.cb2.setChecked(true);
                } else if (r[i].equals("3")) {
                    binding.cb3.setChecked(true);
                } else if (r[i].equals("4")) {
                    binding.cb4.setChecked(true);
                } else if (r[i].equals("5")) {
                    binding.cb5.setChecked(true);
                }
            }
        }

    }

    private void inIt() {
        onClick();
    }

    private void onClick() {

        binding.cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!FinalId.contains("1")) {
                        FinalId.add("1");
                    }
                } else {
                    FinalId.remove("1");
                }
            }
        });

        binding.cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!FinalId.contains("2")) {
                        FinalId.add("2");
                    }
                } else {
                    FinalId.remove("2");
                }
            }
        });

        binding.cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!FinalId.contains("3")) {
                        FinalId.add("3");
                    }
                } else {
                    FinalId.remove("3");
                }
            }
        });

        binding.cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!FinalId.contains("4")) {
                        FinalId.add("4");
                    }
                } else {
                    FinalId.remove("4");
                }
            }
        });

        binding.cb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!FinalId.contains("5")) {
                        FinalId.add("5");
                    }
                } else {
                    FinalId.remove("5");
                }
            }
        });

        binding.tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FinalId.isEmpty()) {
                    cat = "";
                } else {
                    cat = String.valueOf(FinalId).replace("[", "").replace("]", "").replace(" ", "");
                }

                if (FinalId2.isEmpty()) {
                    rat = "";
                } else {
                    if (FinalId2.size() == id.size()) {
                        rat = "";
                    } else {
                        rat = String.valueOf(FinalId2).replace("[", "").replace("]", "").replace(" ", "");
                    }
                }
                setIntent();

            }
        });

        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat = "";
                rat = "";
                FinalId.clear();
                setIntent();
            }
        });

        binding.tvCatagory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
            }
        });

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setIntent() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("rat", rat);
        returnIntent.putExtra("cat", cat);
        setResult(1, returnIntent);
        finish();
    }

    private void getCategory() {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);

        hashMap.put(AppConstants.SALON_ID, salonId);


        Log.e(TAG, "Category list " + hashMap);

        RetrofitServices.urlServices.service_forratefilter(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e(TAG, "onResponse:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {

                       JSONArray data = jsonObject.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {

                            JSONObject inData = data.getJSONObject(i);
                            id.add(inData.getString("id"));
                            if(inData.getString("name").equals(""))
                            {
                                nameSet.add(inData.getString("catname"));
                            }
                            else
                            {
                                nameSet.add(inData.getString("catname")+" - "+inData.getString("name"));

                            }
                            Log.e("Name Set",inData.getString("catname"));
                            Log.e("Name Set1", String.valueOf(nameSet.size()));
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
                       // Log.e(TAG, "onResponse: nameSet lllllllll " + nameSet.toString());
                       // Log.e(TAG, "onResponse: name lllllllll " + name.toString());

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


    private void popup() {

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.rating_filter_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView ok, cancel;
        CheckBox cb = dialog.findViewById(R.id.cb);
        RecyclerView rv = dialog.findViewById(R.id.rv);
        ok = dialog.findViewById(R.id.tvOk);
        cancel = dialog.findViewById(R.id.tvCancel);
        ImageView cross = dialog.findViewById(R.id.btnivclosedialog);


        rv.setLayoutManager(new LinearLayoutManager(context));

        adapter = new ReviewFilterAdapter(name, id, FinalId2, context, cb);
        rv.setAdapter(adapter);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FinalId2.isEmpty()) {
                    binding.tvCatagory.setText("Alle Bewertungen");
                } else {
                    binding.tvCatagory.setText(FinalId2.size() + " Filter ausgewählt");
                }
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "onClick: --> " + cb.isChecked());

                if (cb.isChecked()) {
                    cb.setChecked(true);
                    FinalId2.clear();
                    for (int i = 0; i < id.size(); i++) {
                        FinalId2.add(id.get(i));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    cb.setChecked(false);
                    FinalId2.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        if (binding.tvCatagory.getText().toString().equals("Alle Bewertungen")) {
            cb.setChecked(true);
            FinalId2.clear();
            for (int i = 0; i < id.size(); i++) {
                FinalId2.add(id.get(i));
            }
            adapter.notifyDataSetChanged();
        }

        Log.e(TAG, "popup: FindId-2 " + FinalId2);

    }
}
