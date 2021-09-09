package com.si.styletimer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.net.Uri;

import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.adapters.AvailabiltyAdapter;
import com.si.styletimer.adapters.EmployeeListModelAdapter;
import com.si.styletimer.adapters.PaymentMethodAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivityVenueDetailsBinding;
import com.si.styletimer.models.EmployeeListModel;
import com.si.styletimer.models.PaymentMethodModel;
import com.si.styletimer.models.venu_model.VenueModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.GPSTracker;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VenueDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "VenueDetailsActivity";
    private Context context;
    ActivityVenueDetailsBinding binding;
    private String salonId = "", addre = "";
    private Gson gson;
    private GoogleMap mMap;
    private float latt = 0, langg = 0;
    private int appbarstatus = 0;
    String a = "";
    List<VenueModel> venueModelList = new ArrayList<>();
    List<EmployeeListModel> employeeListModels = new ArrayList<>();
    List<PaymentMethodModel> paymentMethodModels = new ArrayList<>();
    private ArrayList<String> days;
    private String fb_link;
    private String web_link;
    private String insta_link;

    // private ArrayList<String> Days = new ArrayList<String>(Arrays.asList(this.getResources().getString(R.string.monday),this.getResources().getString(R.string.tuesday),this.getResources().getString(R.string.wednesday),this.getResources().getString(R.string.thursday),this.getResources().getString(R.string.friday),this.getResources().getString(R.string.saturday),this.getResources().getString(R.string.sunday)));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_venue_details);
        context = this;
        gson = new Gson();

        salonId = getIntent().getStringExtra("salonId");

        inIt();

    }

    private void inIt() {
        days = new ArrayList<String>(Arrays.asList(this.getResources().getString(R.string.monday), this.getResources().getString(R.string.tuesday), this.getResources().getString(R.string.wednesday), this.getResources().getString(R.string.thursday), this.getResources().getString(R.string.friday), this.getResources().getString(R.string.saturday), this.getResources().getString(R.string.sunday)));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        salon_profile_detail();
        onClick();

        binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    appbarstatus = 1;
                    binding.tvHeading.animate().alpha(1.0f);
                    binding.rlShadow.setVisibility(View.GONE);
                } else if (verticalOffset == 0) {
                    appbarstatus = 2;
                    binding.tvHeading.animate().alpha(0.0f);
                    binding.rlShadow.setVisibility(View.GONE);
                } else {
                    appbarstatus = 3;
                    binding.tvHeading.animate().alpha(1.0f);
                    binding.rlShadow.setVisibility(View.GONE);

                }

            }
        });
        binding.appBar.setElevation(8.0f);
    }

    private void onClick() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //    Bungee.slideRight(context);
            }
        });

        binding.Detail.tvYourSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!web_link.startsWith("https://") && !web_link.startsWith("http://")) {
                    web_link = "http://" + web_link;
                }
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web_link));
                startActivity(openUrlIntent);
            }
        });
        binding.Detail.tvInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!insta_link.startsWith("https://") && !insta_link.startsWith("http://")) {
                    insta_link = "http://" + insta_link;
                }
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(insta_link));
                startActivity(openUrlIntent);
            }
        });
        binding.Detail.tvFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fb_link.startsWith("https://") && !fb_link.startsWith("http://")) {
                    fb_link = "http://" + fb_link;
                }
                Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fb_link));
                startActivity(openUrlIntent);
            }
        });

        binding.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        binding.Detail.llMapReDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latt != 0 && langg != 0) {

                    /*a="google.navigation:q="+String.valueOf(latt)+","+String.valueOf(langg)+"&mode=d";
                    Uri gmmIntentUri = Uri.parse(a);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                    Bungee.slideLeft(context);*/
                    Uri mapUri = Uri.parse("geo:" + latt + "," + langg + "?q=" + Uri.encode(addre));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);

                  /*  String strUri = "http://maps.google.com/maps?q=loc:" + latt + "," + langg + " (" + addre + ")";
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(Intent.createChooser(intent, "Select an application"));*/


                  /*  String uri = "http://maps.google.com/maps?f=d&hl=en&daddr="+String.valueOf(latt)+","+String.valueOf(langg);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(Intent.createChooser(intent, "Select an application"));*/

                }
            }
        });
        binding.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latt != 0 && langg != 0) {

                    Uri mapUri = Uri.parse("geo:" + latt + "," + langg + "?q=" + Uri.encode(addre));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);

                }
            }
        });
    }

    private void salon_profile_detail() {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.SALON_ID, salonId);
        hashMap.put(AppConstants.LATTITUDE, Session.getLat(context));
        hashMap.put(AppConstants.LONGITUDE, Session.getLang(context));

        Log.e("-->", "available list " + hashMap);

        RetrofitServices.urlServices.salon_profile_detail(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("available list-->", "onResponse:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {

                        JSONObject data = jsonObject.getJSONObject("data");

                        venueModelList = gson.fromJson(data.getJSONArray("availability").toString(), new TypeToken<ArrayList<VenueModel>>() {
                        }.getType());
                        employeeListModels = gson.fromJson(data.getJSONArray("employee").toString(), new TypeToken<ArrayList<EmployeeListModel>>() {
                        }.getType());

                        binding.Detail.rvAvailability.setLayoutManager(new LinearLayoutManager(context));
                        binding.Detail.rvAvailability.setAdapter(new AvailabiltyAdapter(context, venueModelList, days));

                        LinearLayoutManager llm = new LinearLayoutManager(context);
                        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                        binding.Detail.rvEmployee.setLayoutManager(llm);
                        binding.Detail.rvEmployee.setAdapter(new EmployeeListModelAdapter(context, employeeListModels));

                        binding.Detail.tvSalonname.setText(jsonObject.getJSONObject("data").getString("business_name"));
                        binding.Detail.tvSalonAddress.setText(jsonObject.getJSONObject("data").getString("address") + "\n"
                                + jsonObject.getJSONObject("data").getString("zip") + " " + jsonObject.getJSONObject("data").getString("city"));

                        addre = jsonObject.getJSONObject("data").getString("address") + " " + jsonObject.getJSONObject("data").getString("zip") + " " + jsonObject.getJSONObject("data").getString("city");
                        String descp = jsonObject.getJSONObject("data").getString("about_salon");
                        binding.Detail.tvDescription.setText(Html.fromHtml(descp));

                        setMarker(Float.valueOf(jsonObject.getJSONObject("data").getString("latitude")), Float.valueOf(jsonObject.getJSONObject("data").getString("longitude")));

                        String distance = jsonObject.getJSONObject("data").getString("distance");
                        if (distance != null && !distance.equals("")) {
                            binding.Detail.tvDistance.setText(Utility.round_off(distance) + " km");
                        }
                        paymentMethodModels.clear();
                        paymentMethodModels = gson.fromJson(data.getJSONArray("payment_methods").toString(), new TypeToken<ArrayList<PaymentMethodModel>>() {
                        }.getType());
                        binding.Detail.rvPayment.setLayoutManager(new LinearLayoutManager(context));
                        binding.Detail.rvPayment.setAdapter(new PaymentMethodAdapter(context, paymentMethodModels));

                        if (paymentMethodModels.size() > 0) {
                            binding.Detail.rvPayment.setVisibility(View.VISIBLE);
                            binding.Detail.vv.setVisibility(View.VISIBLE);
                            binding.Detail.tvv.setVisibility(View.VISIBLE);
                        } else {
                            binding.Detail.rvPayment.setVisibility(View.GONE);
                            binding.Detail.vv.setVisibility(View.GONE);
                            binding.Detail.tvv.setVisibility(View.GONE);
                        }


                        fb_link = data.getString("fb_link");
                        web_link = data.getString("web_link");
                        insta_link = data.getString("insta_link");

                        if (web_link.equals("") && fb_link.equals("") && insta_link.equals("")) {
                            binding.Detail.llLink.setVisibility(View.GONE);
                        } else {
                            binding.Detail.llLink.setVisibility(View.VISIBLE);
                        }
                        if (!web_link.equals("")) {
                            binding.Detail.tvYourSite.setText(web_link);
                            binding.Detail.tvYourSite.setVisibility(View.VISIBLE);
                        }
                        if (!fb_link.equals("")) {
                            binding.Detail.tvFb.setText(fb_link);
                            binding.Detail.tvFb.setVisibility(View.VISIBLE);
                        }  if (!insta_link.equals("")) {
                            binding.Detail.tvInsta.setText(insta_link);
                            binding.Detail.tvInsta.setVisibility(View.VISIBLE);
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

    private void setMarker(float lat, float lang) {
        latt = lat;
        langg = lang;
        LatLng sydney = new LatLng(lat, lang);
        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
       // mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), 16));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  Bungee.slideRight(context);
    }

}
