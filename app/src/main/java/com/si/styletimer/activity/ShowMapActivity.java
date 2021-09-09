package com.si.styletimer.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.os.Handler;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.adapters.ServiceMapAdapter;
import com.si.styletimer.controller.Controller;
import com.si.styletimer.databinding.ActivityShowMapBinding;
import com.si.styletimer.models.categoryListing.CategoryListing;
import com.si.styletimer.models.categoryListing.Sercvice;
import com.si.styletimer.retrofit.RetrofitServices;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShowMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "ShowMapActivity";
    private Context context;
    private ActivityShowMapBinding binding;
    private GoogleMap mMap;
    private Gson gson;
    private CategoryListing categoryListing;
    private BottomSheetDialog bottomSheetDialog;
    private Controller controller;
    private BottomSheetBehavior sheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_map);
        context = this;
        gson = new Gson();
        controller = (Controller) context.getApplicationContext();



        setupGoogleMap();

        initViews();
    }

    private void initViews() {

        if (getIntent() != null) {
            sheetBehavior = BottomSheetBehavior.from(binding.bottomsheet.getRoot());
            sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState) {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED: {
                        }
                        break;
                        case BottomSheetBehavior.STATE_COLLAPSED: {
                        }
                        break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });

            categoryListing = gson.fromJson(getIntent().getStringExtra("hi"), CategoryListing.class);
            if (categoryListing != null) {
                showSalonDetails();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // binding.appBar.setExpanded(false,true);
                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    } else {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
            }, 1000);


        }

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setupGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        double lat = Double.parseDouble(categoryListing.getLatitude());
        double lon = Double.parseDouble(categoryListing.getLongitude());
        LatLng newlatlong = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(newlatlong).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newlatlong, 11));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                return false;
            }
        });
    }

    private void showSalonDetails() {
        try {

            binding.bottomsheet.ivClose.setVisibility(View.VISIBLE);

            List<Sercvice> sevicesLists = new ArrayList<>();
            binding.bottomsheet.rvSubCat.setLayoutManager(new LinearLayoutManager(context));
            binding.bottomsheet.rvSubCat.setNestedScrollingEnabled(false);
            ServiceMapAdapter serviceMapAdapter = new ServiceMapAdapter(context,sevicesLists);
            binding.bottomsheet.rvSubCat.setAdapter(serviceMapAdapter);
            serviceMapAdapter.notifyDataSetChanged();


            binding.bottomsheet.tvName.setText(categoryListing.getBusinessName());
            binding.bottomsheet.tvAddress.setText(categoryListing.getAddress());
            binding.bottomsheet.tvReview.setText(categoryListing.getTotalReview() + " bewertungs");

            String url = RetrofitServices.BANNERS + categoryListing.getId() + "/" + categoryListing.getImage();
            Log.e(TAG, "showSalonDetails: " + url);
            Picasso.get()
                    .load(url)
                    .fit()
                    .placeholder(R.drawable.no_image_available1)
                    .centerCrop()
                    .into(binding.bottomsheet.ivSalon);

            if (categoryListing.getRating().equals("")) {
                binding.bottomsheet.ratingSaloon.setRating(0);
            } else {
                binding.bottomsheet.ratingSaloon.setRating(Float.valueOf(categoryListing.getRating()));
            }

            serviceMapAdapter.setSercviceList(categoryListing.getSercvices());
            serviceMapAdapter.notifyDataSetChanged();
            Log.e(TAG, "showSalonDetails: " + categoryListing.getSercvices());

            binding.bottomsheet.llMapMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.e(TAG, "onClick: ## "+categoryListing.getSercvices().toString() );
                        Session.setSalon_id(context, categoryListing.getId());
                        Intent intent = new Intent(context, SalonDetailActivity.class);
                        intent.putExtra("id",categoryListing.getId());
                        controller.sercviceList = categoryListing.getSercvices();
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            binding.bottomsheet.ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
