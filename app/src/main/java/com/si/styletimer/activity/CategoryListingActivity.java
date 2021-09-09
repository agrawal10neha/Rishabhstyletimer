package com.si.styletimer.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Handler;
/*import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;*/
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.innovattic.rangeseekbar.RangeSeekBar;
import com.si.styletimer.R;
import com.si.styletimer.Session.RealmController;
import com.si.styletimer.Session.Session;
import com.si.styletimer.adapters.CategoryListAdapter;
import com.si.styletimer.adapters.SubCatMapAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.controller.Controller;
import com.si.styletimer.databinding.ActivityCategoryListingBinding;
import com.si.styletimer.databinding.CategoryBottomSheetBinding;
import com.si.styletimer.databinding.DialogAddSalonBinding;
import com.si.styletimer.databinding.MapBotomsheetBinding;
import com.si.styletimer.databinding.PopupThankReviewBinding;
import com.si.styletimer.models.cartcount.CartCountModel;
import com.si.styletimer.models.categoryListing.CategoryListing;
import com.si.styletimer.models.categoryListing.Sercvice;
import com.si.styletimer.models.mapModel.MarkerModel;
import com.si.styletimer.retrofit.JsonUtil;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Animatoo;
import com.si.styletimer.utill.GPSTracker;
import com.si.styletimer.utill.MarshmallowPermission;
import com.si.styletimer.utill.Utility;
import com.si.styletimer.utill.ViewAnimatorSlideUpDown;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityCategoryListingBinding binding;
    private static final String TAG = "CategoryListingActivity";
    boolean isUp = false;
    private Context context;
    private BottomSheetDialog bsd;
    private Dialog dialog;
    private String tag = "", sub = "", main = "", LATITUDE = "", LONGITUDE = "", caten = "", category_id = "";
    private List<CategoryListing> categoryListingList = new ArrayList<>();
    private List<MarkerModel> markerModelList = new ArrayList<>();
    private int pastVisiblesItems = 0, visibleItemCount = 0, totalItemCount = 0;
    private Boolean loadingMore = false;
    private Boolean viewMore = false;
    private Boolean paginationCheck = false;
    private String api_flag = "normal";
    private int page = 1;
    private Gson gson;
    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private CategoryListAdapter categoryListAdapter;
    private Controller controller;
    private String left = "0", right = "1000", sort = "", offer = "", minPrice = "0", maxPrice = "1000", finalSort = "", finalOffer = "";
    private String startTime = "", endTime = "", selectedDate = "",selectedDate1 = "", finalTime = "anytime";
    private String selectedCatName = "", selectedCatId = "";
    private String selectedDistance = "20";
    private String selectedSort = "";
    private LatLngBounds.Builder builder;
    private boolean LocationEnabled;
    private Marker marker;
    private MarshmallowPermission marshmallowPermission;
    private int appbarstatus = 0;
    private String viewmaplist = "list";
    private LinearLayoutManager m;
    private boolean colax = true, colaxtwo = true;
    private RealmController realmController;
    int xx;
    String nn,ll,kk;
    String rDate;
    // private Animation slideEnter;
    // private Animation slideExit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category_listing);
        context = this;
        gson = new Gson();
        controller = (Controller) context.getApplicationContext();
        MapsInitializer.initialize(getApplicationContext());
        marshmallowPermission = new MarshmallowPermission(context);
        realmController = new RealmController(context);
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //slideEnter = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        //slideExit = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        controller.day = "";

        permission();

        inIt();

//todo.... if location is not active, send user to Location Setting

        if (!LocationEnabled) {

            binding.collapselayout.tvLocationDistance.setText("set Location");
            /*Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);*/
            //todo... it is to redirect him to settings
        }

/*
        binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    Log.e(TAG, "onOffsetChanged: 1 " );
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.ivBack.setRotation(0);
                    binding.collapselayout.llLocation.setVisibility(View.GONE);
                    binding.collapselayout.llTime.setVisibility(View.GONE);
                    binding.tvsubtitle.setVisibility(View.VISIBLE);
                    binding.tvsubtitle.startAnimation(slideEnter);
                    Log.e(TAG, "onOffsetChanged: 0 " );
                    isShow = true;
                } else if (isShow) {
                    binding.ivBack.setRotation(90);
                    binding.tvsubtitle.setVisibility(View.GONE);
                    binding.collapselayout.llLocation.setVisibility(View.VISIBLE);
                    binding.collapselayout.llTime.setVisibility(View.VISIBLE);
                    binding.tvsubtitle.startAnimation(slideExit);
                    Log.e(TAG, "onOffsetChanged: elsee " );
                    isShow = false;
                }
            }
        });
*/


    }

    private void permission() {


        if (marshmallowPermission.check_location_Permission()) {
            getLocation2();
        } else {
            getLocation2();
        }
    }

    private void getLocation2() {
        GPSTracker gps = new GPSTracker(context);

        if (gps.canGetLocation()) {

            Session.setLat(context, String.valueOf(gps.getLatitude()));
            Session.setLang(context, String.valueOf(gps.getLongitude()));

            Log.e("getLatitude(-=-----=>)", "" + gps.getLatitude());
            Log.e("getLongitude(-=-----=>)", "" + gps.getLongitude());

            LATITUDE = String.valueOf(gps.getLatitude());
            LONGITUDE = String.valueOf(gps.getLongitude());


            Session.setCurrentAdd(context, getAddress(gps.getLatitude(), gps.getLongitude()));
            Log.e("add  ---- ",Session.getCurrentAdd(context));
            binding.collapselayout.tvLocationDistance.setText(Session.getCurrentAdd(context));
        } else {
            gps.showSettingsAlert();
        }
    }

    public String getAddress(final double latitude, final double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {

                Address address = addresses.get(0);

                for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++) {
                    if (i == addresses.get(0).getMaxAddressLineIndex()) {
                        result.append(addresses.get(0).getAddressLine(i));
                    } else {
                        result.append(addresses.get(0).getAddressLine(i) + ",");
                    }
                }
                String addreshidden = result.toString();

                Session.setCurrentAdd(context, addreshidden);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private void getIntentData() {
        try {

            tag = getIntent().getStringExtra("tag");
            category_id = getIntent().getStringExtra("category_id");
            Log.e(TAG, "getIntentData: tag->" + tag);

            binding.tvTreatment.setText(getIntent().getStringExtra("cat_name"));
            caten = getIntent().getStringExtra("cat_name");
            if (tag.equals("main")) {
                sub = "";
                main = getIntent().getStringExtra("id");
                Log.e(TAG, "main getIntentData: id->" + main);
            } else {
                main = "";
                sub = getIntent().getStringExtra("id");
                Log.e(TAG, "sub getIntentData: id->" + sub);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inIt() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getIntentData();
        onClick();
        getCategoryList("normal");
        setupRecyclerview();

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appbarstatus == 2) {
                    binding.appBar.setExpanded(false, true);
                } else if (appbarstatus == 1) {
                    finish();
                    //Bungee.slideRight(context);
                }
            }
        });

        binding.appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int value = Math.abs(verticalOffset);
            int div = (int) (value / 2.63);
            int vale = (263 - value);

            makeCollapse(vale);

            if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                // Collapsed
                //Log.e(TAG, "onOffsetChanged: Collapsed");
                binding.ivBack.setRotation(0);
                appbarstatus = 1;
                colaxtwo = true;
            } else if (verticalOffset == 0) {
                //  Expanded
                //   Log.e(TAG, "onOffsetChanged: Expanded ");
                binding.ivBack.setRotation(90);
                appbarstatus = 2;
            } else {
                appbarstatus = 3;
                //  Log.e(TAG, "onOffsetChanged: between ");
                colaxtwo = false;

            }
        });

        binding.content.content.btnAddsalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddSalon();
            }
        });

        binding.content.content.btnAddsalontwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddSalon();
            }
        });

    }

    private void makeCollapse(int verticalOffset) {

        double value = Math.abs(verticalOffset);
        int divre = (int) (value / 2.63);

        double div = (value / 2.63);
        double val = (div / 10);
        double vall = (val / 10);
        double salary = Math.round(vall * 100.0) / 100.0;
        float dfsdf = (float) salary;

        binding.collapselayout.llLocation.setAlpha(dfsdf * 0.75f);
        binding.collapselayout.llTime.setAlpha(dfsdf);


//        if (divre > 0) {
//            // Scrolling up
//            Log.e(TAG, "onScrolled: >>>>>>>>>>>>> uppppppppppppppppp" );
//        } else {
//            Log.e(TAG, "onScrolled: >>>>>>>>>>>>> downnnnnnnnnnnnn" );
//
//            // Scrolling down
//        }


        if (divre >= 0 && divre <= 90) {
            binding.ivBack.setRotation(divre);
        }

        if (divre < 90) {
            binding.collapselayout.llLocation.setVisibility(View.GONE);
            binding.tvsubtitle.setVisibility(View.VISIBLE);
            // binding.tvTreatment.setSingleLine(true);
            if (binding.collapselayout.tvLocationDistance.getText().length() > 25) {
                binding.tvsubtitle.setText(binding.collapselayout.tvLocationDistance.getText().subSequence(0, 25) + ".. - " + binding.collapselayout.tvDateTime.getText().toString());
            } else {
                binding.tvsubtitle.setText(binding.collapselayout.tvLocationDistance.getText() + " - " + binding.collapselayout.tvDateTime.getText().toString());
            }

//            if (appbarstatus != 3) {
//                binding.appBar.setExpanded(false, true);
//            }
        }

        if (divre > 90) {
            binding.collapselayout.llLocation.setVisibility(View.VISIBLE);
            binding.tvsubtitle.setVisibility(View.GONE);
            //   binding.tvTreatment.setSingleLine(false);
            binding.tvsubtitle.setText(binding.collapselayout.tvLocationDistance.getText());
        }

        if (divre < 70) {
            binding.collapselayout.llLocation.setVisibility(View.GONE);
            binding.collapselayout.llTime.setVisibility(View.GONE);
            if (binding.collapselayout.tvLocationDistance.getText().length() > 25) {
                binding.tvsubtitle.setText(binding.collapselayout.tvLocationDistance.getText().subSequence(0, 25) + ".. - " + binding.collapselayout.tvDateTime.getText().toString());
            } else {
                binding.tvsubtitle.setText(binding.collapselayout.tvLocationDistance.getText() + " - " + binding.collapselayout.tvDateTime.getText().toString());
            }
            binding.ivBack.setRotation(0);

//            Log.e(TAG, "makeCollapse:=================== "+appbarstatus );

        }

        if (divre > 70) {
            binding.collapselayout.llTime.setVisibility(View.VISIBLE);
            binding.tvsubtitle.setVisibility(View.GONE);
            //  binding.tvTreatment.setSingleLine(false);
        }

      /*  if (divre <= 80 ){

        if (divre <= 80 || divre >= 60){

            if (colax){
                Log.e(TAG, "makeCollapse: >>>>>>>>>>>>>>>>>>>>>> " );
                binding.appBar.setExpanded(false,true);
                binding.ivBack.setRotation(0);
                colax = false;
            }

        }

        if (divre >= 40 ){
            if (!colax){
               Log.e(TAG, "makeCollapse: <<<<<<<<<<<<<<<<<<<<<<<< " );
                binding.appBar.setExpanded(true,true);
                binding.ivBack.setRotation(90);
                colax = true;
            }
        }*/
    }

    private void setupRecyclerview() {
        m = new LinearLayoutManager(context);
        binding.content.content.rv.setLayoutManager(m);
        categoryListAdapter = new CategoryListAdapter(context, categoryListingList);
        binding.content.content.rv.setAdapter(categoryListAdapter);
        categoryListAdapter.notifyDataSetChanged();

        /* CategoryListAdapter Listener By Deepak */
        categoryListAdapter.setOnItemClickListener((view, position) -> {
            String strSalonId = categoryListAdapter.getCategoryListing().get(position).getId();
            List<Sercvice> sevicesLists = categoryListAdapter.getCategoryListing().get(position).getSercvices();
            switch (view.getId()) {
                case R.id.mainLayout:
                    Log.e(TAG, "setupRecyclerview: " + returnServiceIds(sevicesLists));
                    realmController.clearServices();
                    Session.setSalon_id(context, strSalonId);
                    clear_cart_Count();
                   Session.setServiceIds(context, returnServiceIds(sevicesLists));
                    Intent intent = new Intent(context, SalonDetailActivity.class);
                    intent.putExtra(AppConstants.SERVICEIDS, returnServiceIds(sevicesLists));
                   //intent.putExtra(AppConstants.SERVICEIDS, " ");
                    startActivity(intent);
                    Animatoo.animateSlideLeft(context);
                    break;
            }
        });



        binding.content.content.rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = m.getChildCount();
                    totalItemCount = m.getItemCount();
                    pastVisiblesItems = m.findFirstVisibleItemPosition();
                    if (!loadingMore) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            viewMore = true;
                        }
                        if (viewMore) {
                            loadingMore = true;
                            page++;
                            paginationCheck = true;
                            getCategoryList("pagination");
                            //  Log.e(TAG, "onScrolled: ?>:<{}+_DAFCBHBCBHCB---HERE");
                        }
                    }
                }


                if (dy > 0) {
                    // Scrolling up
                    //Log.e(TAG, "onScrolled: >>>>>>>>>>>>> uppppppppppppppppp" );
                } else {
                    //  Log.e(TAG, "onScrolled: >>>>>>>>>>>>> downnnnnnnnnnnnn" );

                    // Scrolling down
                }

            }
        });


/*
        binding.content.content.rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)){ //1 for down
                    if (categoryListingList.size()>9) {
                        addDataToListTopRes();
                    }
                }
            }
        });
*/




    }
/*
    private void addDataToListTopRes() {
        new Handler().postDelayed(() -> {
            page++;
            getCategoryList("pagination");
        }, 1000);
    }
*/

    private void onClick() {

        binding.content.tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet();
            }
        });

//todo-- top filters----
        binding.llTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = "";
                if (sub.equals("") || sub.equals("0")) {
                    id = main;
                } else {
                    id = sub;
                }

                if (appbarstatus == 1 || appbarstatus == 3) {

                    binding.appBar.setExpanded(true);
                    m.scrollToPosition(0);

                } else if (appbarstatus == 2) {
                    Intent i = new Intent(context, TreatmentSearchActivity.class);
                    i.putExtra("catId", id);
                    i.putExtra("category_id", category_id);
                    i.putExtra("caten", caten);
                    startActivityForResult(i, 1);
                }

            }
        });

        binding.collapselayout.llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LocationSearchActivity.class);
                i.putExtra("address", binding.collapselayout.tvLocationDistance.getText().toString());
                i.putExtra("lat", LATITUDE);
                i.putExtra("lang", LONGITUDE);
                startActivityForResult(i, 2);
            }
        });

        binding.collapselayout.llTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: status- " + appbarstatus);
                if (appbarstatus == 1) {
                    binding.appBar.setExpanded(true, true);
                } else {
                    Intent i = new Intent(context, DayTimePickActivity.class);
                    i.putExtra("date", selectedDate);
                    i.putExtra("st", startTime);
                    i.putExtra("et", endTime);
                    startActivityForResult(i, 3);

                }
            }
        });
//todo-- close top filters----

        binding.content.content.mapCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e(TAG, "onCheckedChanged: 1");
                    mMap.getUiSettings().setScrollGesturesEnabled(true);
                } else {
                    Log.e(TAG, "onCheckedChanged: 2");
                    mMap.getUiSettings().setScrollGesturesEnabled(false);
                }
            }
        });


        binding.content.llChangeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.content.tvChangeView.getText().toString().equals(context.getResources().getString(R.string.map))&&(xx == 0)) {
                    viewmaplist = "list";
                    binding.content.content.llMap.setVisibility(View.GONE);
                    binding.content.content.llList.setVisibility(View.GONE);
                    binding.content.tvChangeView.setText(context.getResources().getString(R.string.list));
                    binding.content.ivChangeView.setImageResource(R.drawable.ic_list);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // binding.appBar.setExpanded(false,true);
                            lockAppBar(true);
                        }
                    }, 1000);

                }
                else if (binding.content.tvChangeView.getText().toString().equals(context.getResources().getString(R.string.map))) {
                    viewmaplist = "list";
                    binding.content.content.llMap.setVisibility(View.VISIBLE);
                    binding.content.content.llList.setVisibility(View.GONE);
                    binding.content.tvChangeView.setText(context.getResources().getString(R.string.list));
                    binding.content.ivChangeView.setImageResource(R.drawable.ic_list);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // binding.appBar.setExpanded(false,true);
                            lockAppBar(true);
                        }
                    }, 1000);

                } else {
                    viewmaplist = "map";
                    binding.content.content.llMap.setVisibility(View.GONE);
                    binding.content.content.llList.setVisibility(View.VISIBLE);
                    binding.content.tvChangeView.setText(context.getResources().getString(R.string.map));
                    binding.content.ivChangeView.setImageResource(R.drawable.ic_map_pin);
                    lockAppBar(false);
                }
            }
        });

    }

    public void lockAppBar(boolean locked) {
        if (locked) {
            binding.appBar.setExpanded(false, true);
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) binding.appBar.getLayoutParams();
            lp.height = px;
            binding.appBar.setLayoutParams(lp);
            binding.toolbarLayout.setTitleEnabled(false);
        } else {
            binding.appBar.setExpanded(true, true);
            binding.appBar.setActivated(true);
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) binding.appBar.getLayoutParams();
            lp.height = (int) getResources().getDimension(R.dimen.app_bar_height);
            binding.toolbarLayout.setTitleEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "onActivityResult: " + requestCode);
        if (requestCode == 1) {

//--CATEGORY FILTER--
            if (data.getStringExtra("result").equals("yes")) {
                try {
                    String resultName = data.getStringExtra("result_name");
                    String resultId = data.getStringExtra("result_id");
                    String mainId = data.getStringExtra("main_id");

                    selectedCatName = resultName;
                    sub = resultId;
                    main = mainId;

                    if (resultId != null || resultName != null) {

                        binding.tvTreatment.setText(resultName);
                        page = 1;
                        getCategoryList("normal");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }

        } else if (requestCode == 2) {

//--DISTANCE FILTER--
            String result = data.getStringExtra("resultDistance");
            String Name = data.getStringExtra("resultName");
            String lat = data.getStringExtra("lat");

            Log.e(TAG, "onActivityResult: 1"+ Name);

            if (!Name.equals("") && !lat.equals("")) {
                Log.e(TAG, "onActivityResult: 2");

                if (result.equals("set Location") || result.equals("")) {

                }

                LATITUDE = data.getStringExtra("lat");
                LONGITUDE = data.getStringExtra("lang");
                selectedDistance = result;
                binding.collapselayout.tvLocationDistance.setText(Name);
                page = 1;
                getCategoryList("normal");

            } else {
                Log.e(TAG, "onActivityResult: 3");
                Log.e("LAT--", LATITUDE);
                Log.e("LAN--", LONGITUDE);

            }


        } else if (requestCode == 3) {

//--TIME FILTER--

//todo--- Clearing the express filter --
            offer = "";

            try {
                String resultStime = data.getStringExtra("result_start_time");
                String resultEtime = data.getStringExtra("result_end_time");
                String resultDate = data.getStringExtra("result_date");
                rDate = resultDate.replace(" ", ".");
               // ll=a[1];
                if(resultDate.equals("Morgen"))
                {
                    kk=resultDate;
                }else if(resultDate.equals("Heute"))
                {
                    kk=resultDate;
                }
                else {

                    String[] a = resultDate.split(" ");
                    String  nn = a[0];
                    if (a[1] != null || a[1].equals("")) {
                        ll = a[1];
                    }
                    if (ll.equals("Juni")) {
                        ll = "June";
                    } else if (ll.equals("Juli")) {
                        ll = "July";
                    } else if (ll.equals("Jan.")) {
                        ll = "Jan";
                    } else if (ll.equals("Aug.")) {
                        ll = "Aug";
                    } else if (ll.equals("Sept.")) {
                        ll = "Sep";
                    } else if (ll.equals("Okt.")) {
                        ll = "Oct";
                    } else if (ll.equals("Nov.")) {
                        ll = "Nov";
                    } else if (ll.equals("Dez.")) {
                        ll = "Dec";
                    } else if (ll.equals("Feb.")) {
                        ll = "Feb";
                    } else if (ll.equals("Mär.")) {
                        ll = "Mar";
                    } else if (ll.equals("Apr.")) {
                        ll = "Apr";
                    } else if (ll.equals("Kann")) {
                        ll = "May";
                    }
                    if (ll.equals("null")) {
                        kk = nn;
                    } else {
                        kk = nn + " " + ll;
                    }
                }
               // kk= nn+" "+ll;
                Log.e("resultDate",kk);


                if (resultStime != null && resultStime.equals("") && resultDate != null && !resultDate.equals("")) {


                binding.collapselayout.tvDateTime.setText("Am " + rDate);

                    if (resultDate.equals("Heute")) {
                        controller.day = resultDate;
                    } else if (resultDate.equals("Morgen")) {
                        controller.day = resultDate;
                    } else {
                        controller.day = controller.datecalendar;
                    }

                    selectedDate1= resultDate;
                    selectedDate = kk;
                    startTime = "";
                    controller.startTime = "";

                    endTime = "";
                    controller.endTime = "";

                    finalTime = "anytime";
                    controller.anyTime = "anytime";

                    page = 1;
                    getCategoryList("normal");

                    Log.e(TAG, "onActivityResult: 1");

                } else if (resultDate.equals("") && !resultEtime.equals("")) {

                    binding.collapselayout.tvDateTime.setText("von " + resultStime + "Uhr bis " + resultEtime + "Uhr");

                    Log.e(TAG, "onActivityResult: >>>11111>>>>>>>>>>>>>>>>>");
                    selectedDate1 = "";
                    selectedDate = "";
                    controller.day = "";

                    startTime = resultStime;
                    controller.startTime = resultStime;

                    endTime = resultEtime;
                    controller.endTime = resultEtime;

                    finalTime = "";
                    controller.anyTime = "";

                    getCategoryList("normal");

                    Log.e(TAG, "onActivityResult: 2");

                } else if (!resultDate.equals("") && !resultEtime.equals("")) {

                   // binding.collapselayout.tvDateTime.setText("Von " + resultStime + " Uhr bis " + resultEtime + " Uhr " + resultDate);
                    binding.collapselayout.tvDateTime.setText(rDate+" von " + resultStime + "Uhr bis " + resultEtime + "Uhr");

                    Log.e(TAG, "onActivityResult: >>>222222>>>>>>>>>>>>>>>>>");
                    selectedDate1=resultDate;
                    selectedDate = kk;
                    controller.day = resultDate;

                    startTime = resultStime;
                    controller.startTime = resultStime;

                    endTime = resultEtime;
                    controller.endTime = resultEtime;

                    finalTime = "";
                    controller.anyTime = "";

                    getCategoryList("normal");

                    Log.e(TAG, "onActivityResult: 3");

                } else if (resultDate.equals("") && resultEtime.equals("")) {

                    binding.collapselayout.tvDateTime.setText("Any time");
                    selectedDate1 = "";
                    selectedDate = "";
                    controller.day = "";

                    startTime = "";
                    controller.startTime = "";

                    finalTime = "anytime";
                    controller.anyTime = "anytime";

                    endTime = "";
                    controller.endTime = "";

                    getCategoryList("normal");

                    Log.e(TAG, "onActivityResult: 4");
                }
            } catch (Exception e) {

                e.printStackTrace();

            }

        }
    }

    private void bottomSheet() {

        final CategoryBottomSheetBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.category_bottom_sheet, null, false);
        bsd = new BottomSheetDialog(context);
        bsd.setContentView(binding.getRoot());
        bsd.setCanceledOnTouchOutside(true);
        bsd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (offer.equals("yes")) {
            binding.Switch.setChecked(true);
        } else {
            binding.Switch.setChecked(false);
        }

        binding.Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    offer = "yes";
                } else {
                    offer = "";
                }
            }
        });

        binding.llSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup(binding.tvSorted);
            }
        });

        //  binding.seekbar.setIndicatorTextDecimalFormat("0");
        switch (selectedSort) {
            case "rating desc":
                binding.tvSorted.setText("Höchste Bewertung");
                break;
            case "rating asc":
                binding.tvSorted.setText("Niedrigste Bewertung");
                break;
            case "distance asc":
                binding.tvSorted.setText("Geringste Entfernung");
                break;
            case "total_review desc":
                binding.tvSorted.setText("Meiste Bewertungen");
                break;
            case "":
                binding.tvSorted.setText("Alle");
                break;
        }


/*
        switch (selectedSort) {
            case "rating desc":
                binding.tvSorted.setText("Highest Rated");
                break;
            case "rating asc":
                binding.tvSorted.setText("Lowest Price");
                break;
            case "distance asc":
                binding.tvSorted.setText("Closest to location");
                break;
            case "total_review desc":
                binding.tvSorted.setText("Most reviews");
                break;
            case "":
                binding.tvSorted.setText("All");
                break;
        }
*/


        if (minPrice.equals("0") && maxPrice.equals("1000")) {
            binding.tvPrice.setText("0 € bis 1000 €");
            // binding.seekbar.setValue(0, 1000);
            binding.seekbar.setMinRange(0);
            binding.seekbar.setMax(1000);
        } else if (!minPrice.equals("") || !maxPrice.equals("")) {
            Log.e(TAG, "bottomSheet:--> " + minPrice);
            try {
                // binding.seekbar.se(Integer.valueOf(minPrice), Integer.valueOf(maxPrice));
                // binding.seekbar.setMinThumbValue(Integer.valueOf(minPrice));
                binding.seekbar.setMinRange(Integer.valueOf(minPrice));
                binding.seekbar.setMax(Integer.valueOf(maxPrice));
                binding.tvPrice.setText(Integer.valueOf(minPrice) + "€ bis " + Integer.valueOf(maxPrice) + "€");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        binding.seekbar.setSeekBarChangeListener(new RangeSeekBar.SeekBarChangeListener() {
            @Override
            public void onStartedSeeking() {

            }

            @Override
            public void onStoppedSeeking() {

            }

            @Override
            public void onValueChanged(int i, int i1) {
                int min = ((int) i);
                int rightint = ((int) i1);
                left = returnValue(min);
                right = returnValue(rightint);
                binding.tvPrice.setText(returnValue(min) + "€ bis " + returnValue(rightint) + "€");
            }
        });

/*
        binding.seekbar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                int min = ((int) leftValue);
                int rightint = ((int) rightValue);
                left = returnValue(min);
                right = returnValue(rightint);
                binding.tvPrice.setText("€ " + returnValue(min) + " to €" + returnValue(rightint));
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
*/

        binding.btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                minPrice = left;
                maxPrice = right;
                finalSort = sort;
                finalOffer = offer;

                if (binding.tvSorted.getText().toString().equals("Alle rate")) {
                    selectedSort = "";
                } else if (binding.tvSorted.getText().toString().equals("Höchste Bewertung")) {
                    selectedSort = "rating desc";
                } else if (binding.tvSorted.getText().toString().equals("Niedrigste Bewehrung")) {
                    selectedSort = "rating asc";
                } else if (binding.tvSorted.getText().toString().equals("Geringste Entfernung")) {
                    selectedSort = "distance asc";
                } else if (binding.tvSorted.getText().toString().equals("Meiste Bewertungen")) {
                    selectedSort = "total_review desc";
                }

                bsd.cancel();

                getCategoryList("normal");

            }
        });

/*
        binding.btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                minPrice = left;
                maxPrice = right;
                finalSort = sort;
                finalOffer = offer;

                if (binding.tvSorted.getText().toString().equals("All rate")) {
                    selectedSort = "";
                } else if (binding.tvSorted.getText().toString().equals("Highest Rated")) {
                    selectedSort = "rating desc";
                } else if (binding.tvSorted.getText().toString().equals("Lowest Price")) {
                    selectedSort = "rating asc";
                } else if (binding.tvSorted.getText().toString().equals("Closest to location")) {
                    selectedSort = "distance asc";
                } else if (binding.tvSorted.getText().toString().equals("Most reviews")) {
                    selectedSort = "total_review desc";
                }

                bsd.cancel();

                getCategoryList("normal");

            }
        });
*/


        bsd.show();
    }

    private String returnValue(int min) {
        String val = "";

        if (min < 5) {
            val = "0";
        } else if (min > 5 && min <= 10) {
            val = "5";
        } else if (min >= 10 && min <= 15) {
            val = "10";
        } else if (min >= 15 && min <= 20) {
            val = "15";
        } else if (min >= 20 && min <= 30) {
            val = "20";
        } else if (min >= 30 && min <= 40) {
            val = "30";
        } else if (min >= 40 && min <= 50) {
            val = "40";
        } else if (min >= 50 && min <= 60) {
            val = "50";
        } else if (min >= 60 && min <= 70) {
            val = "60";
        } else if (min >= 70 && min <= 80) {
            val = "70";
        } else if (min >= 80 && min <= 90) {
            val = "80";
        } else if (min >= 90 && min <= 100) {
            val = "90";
        } else if (min >= 100 && min <= 125) {
            val = "100";
        } else if (min >= 125 && min <= 150) {
            val = "125";
        } else if (min >= 150 && min <= 175) {
            val = "150";
        } else if (min >= 175 && min <= 200) {
            val = "175";
        } else if (min >= 200 && min <= 300) {
            val = "200";
        } else if (min >= 300 && min <= 400) {
            val = "300";
        } else if (min >= 400 && min <= 500) {
            val = "400";
        } else if (min >= 500 && min <= 600) {
            val = "500";
        } else if (min >= 600 && min <= 700) {
            val = "600";
        } else if (min >= 700 && min <= 800) {
            val = "700";
        } else if (min >= 800 && min <= 900) {
            val = "800";
        } else if (min >= 900 && min <= 985) {
            val = "900";
        } else if (min >= 985 && min <= 1000) {
            val = "1000";
        }
        return val;
    }

    private void popup(final TextView tvSorted) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.category_filter_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final RadioGroup rdGroup = dialog.findViewById(R.id.rdGroup);
        RadioButton high, low, close, review;
        high = dialog.findViewById(R.id.high);
        low = dialog.findViewById(R.id.low);
        close = dialog.findViewById(R.id.close);
        review = dialog.findViewById(R.id.review);


        String selectedrb = selectedSort;

        Log.e(TAG, "popup: " + selectedrb);

        switch (selectedrb) {
            case "rating desc":
                high.setChecked(true);
                break;
            case "rating asc":
                low.setChecked(true);
                break;
            case "distance asc":
                close.setChecked(true);
                break;
            case "total_review desc":
                review.setChecked(true);
                break;
            case "desc":
                break;
        }

        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rdGroup.getCheckedRadioButtonId();
                RadioButton rb = dialog.findViewById(selectedId);
                tvSorted.setText(rb.getText());
                sort = String.valueOf(rb.getText());
                selectedSort = "rating desc";
            }
        });

        low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = rdGroup.getCheckedRadioButtonId();
                RadioButton rb = dialog.findViewById(selectedId);
                tvSorted.setText(rb.getText());
                sort = String.valueOf(rb.getText());
                selectedSort = "rating asc";
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = rdGroup.getCheckedRadioButtonId();
                RadioButton rb = dialog.findViewById(selectedId);
                tvSorted.setText(rb.getText());
                sort = String.valueOf(rb.getText());
                selectedSort = "distance asc";
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = rdGroup.getCheckedRadioButtonId();
                RadioButton rb = dialog.findViewById(selectedId);
                tvSorted.setText(rb.getText());
                sort = String.valueOf(rb.getText());
                selectedSort = "total_review desc";
            }
        });


        dialog.show();
    }

    private void getCategoryList(String flag) {
//        LATITUDE = "29.702345";
//        LONGITUDE = "-98.124152";


        if (flag.equals("normal")) {
            page = 1;
            categoryListingList.clear();
        }

        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        if (page == 1) {
            pd.show();
        }

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);

        hashMap.put(AppConstants.PAGE, String.valueOf(page));

        hashMap.put(AppConstants.LAT, LATITUDE);
        hashMap.put(AppConstants.LNG, LONGITUDE);

        hashMap.put(AppConstants.SUB_CATEGORY, sub);
        hashMap.put(AppConstants.MAIN_CATEGORY, main);

        hashMap.put(AppConstants.DISTANCE, selectedDistance);

        hashMap.put(AppConstants.START_RANGE, minPrice);
        hashMap.put(AppConstants.END_RANGE, maxPrice);
        if (selectedSort != null && !selectedSort.equals("")) {
            hashMap.put(AppConstants.ORDER_BY, selectedSort);
        }
        //hashMap.put(AppConstants.ORDER_BY, selectedSort);
        hashMap.put(AppConstants.DATE, selectedDate1);
        hashMap.put(AppConstants.START_TIME, startTime);
        hashMap.put(AppConstants.END_TIME, endTime);
        hashMap.put(AppConstants.TIME, finalTime);

        hashMap.put("expess_offer", offer);

        Log.e(TAG, "getCategoryList() " + hashMap);

        RetrofitServices.urlServices.salon_listing(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e(TAG, "onResponse:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {

                        binding.content.content.rv.setVisibility(View.VISIBLE);
                        binding.content.content.layoutNoData.setVisibility(View.GONE);

                        if (page == 1) {
                            categoryListingList = gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<CategoryListing>>() {
                            }.getType());
                            categoryListAdapter.setCategoryListingList(categoryListingList);
                            categoryListAdapter.notifyDataSetChanged();
                        } else if (page > 1) {
                            List<CategoryListing> categoryListingList2 = gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<CategoryListing>>() {
                            }.getType());
                            categoryListingList.addAll(categoryListingList2);
                            categoryListAdapter.notifyDataSetChanged();
                        }

                        //categoryListingList = gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<CategoryListing>>() {
                        //}.getType());
                        //categoryListAdapter.setCategoryListingList(categoryListingList);
                        //categoryListAdapter.notifyDataSetChanged();

                        //String count = categoryListingList.size() + "";
                        String count = jsonObject.getString("total_count");
                        if (count.equals("1")) {
                            binding.content.tvsaloonfound.setText(count + " " + context.getResources().getString(R.string.salon_found));
                        } else if (count.equals("0")) {
                            binding.content.tvsaloonfound.setText("Keine Salons gefunden ");
                        } else {
                            binding.content.tvsaloonfound.setText(count + " " + context.getResources().getString(R.string.salon_found));
                        }

//                        adapter.setOnItemClickListener(new CategoryListAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//                                if (view.getId() == R.id.mainLayout){
//                                    CategoryListing categoryListing = adapter.getCategoryListing().get(position);
//                                    String myJson = gson.toJson(categoryListing);
//                                    Intent intent = new Intent(context,ShowMapActivity.class);
//                                    intent.putExtra("hi",myJson);
//                                    startActivity(intent);
//                                }else {
//                                    Session.setSalon_id(context,categoryListingList.get(position).getId());
//                                    Intent intent = new Intent(context,SalonDetailActivity.class);
//                                    intent.putExtra("id",categoryListingList.get(position).getId());
//                                    controller.sercviceList =  categoryListingList.get(position).getSercvices();
//                                    String dsdf = categoryListingList.get(position).getSercvices().toString();
//                                    context.startActivity(intent);
//                                }
//
//
//                            }
//                        });

                        if (flag.equals("normal")) {
                            mMap.clear();
                            getMarkerList(LATITUDE, LONGITUDE, sub, main);
                        }

                        paginationCheck = false;

                    } else if (jsonObject.getInt("status") == 0) {
                        binding.content.tvsaloonfound.setText("Keine Salons gefunden ");
                        if (flag.equals("normal")) {
                            mMap.clear();
                            getMarkerList(LATITUDE, LONGITUDE, sub, main);
                        }else{
                            mMap.clear();
                            getMarkerList(LATITUDE, LONGITUDE, sub, main);
                        }
                        if (page == 1) {
                            binding.content.content.rv.setVisibility(View.GONE);
                            binding.content.content.layoutNoData.setVisibility(View.VISIBLE);

                        } else if (page > 1) {
                            binding.content.content.rv.setVisibility(View.VISIBLE);
                            binding.content.content.layoutNoData.setVisibility(View.GONE);
                        }
                    }


                } catch (Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                t.printStackTrace();
                Log.e(TAG, "FALURE ");
            }
        });
    }

    private void getMarkerList(String lat, String lang, String subb, String mainn) {

        mMap.clear();
        markerModelList.clear();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);

        hashMap.put(AppConstants.LAT, lat);
        hashMap.put(AppConstants.LNG, lang);

        hashMap.put(AppConstants.SUB_CATEGORY, subb);
        hashMap.put(AppConstants.MAIN_CATEGORY, mainn);
//todo--- filters starts from here...
        hashMap.put(AppConstants.DISTANCE, selectedDistance);
        hashMap.put(AppConstants.START_RANGE, minPrice);
        hashMap.put(AppConstants.END_RANGE, maxPrice);
        if (selectedSort != null && !selectedSort.equals("")) {
            hashMap.put(AppConstants.ORDER_BY, selectedSort);
        }
        hashMap.put(AppConstants.DATE, selectedDate);
        hashMap.put(AppConstants.START_TIME, startTime);
        hashMap.put(AppConstants.END_TIME, endTime);
        hashMap.put(AppConstants.TIME, finalTime);

        Log.e("Marker SalonValueModel", String.valueOf(hashMap));


        RetrofitServices.urlServices.map_marker(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    /*pd.dismiss();*/

                    String respo = response.body().string().trim();
                    Log.e("marker list-->", "onResponse4:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);
                    xx=jsonObject.getInt("status");
                    Log.e("marker list-->", "onResponse4:- " + xx);

                    if (jsonObject.getInt("status") == 1) {

                        binding.content.content.rv.setVisibility(View.VISIBLE);
                        binding.content.content.layoutNoData.setVisibility(View.GONE);

                        markerModelList = gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<MarkerModel>>() {
                        }.getType());

                        onMapReady(mMap);
                        mMap.setOnMarkerClickListener(null);
                        mMap.clear();

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();

//todo Setup markers....start..
                        for (int i = 0; i < markerModelList.size(); i++) {
                            Log.e(TAG, "onResponse: MARKERS" + String.valueOf(i));
                            setUpMarker(Float.valueOf(markerModelList.get(i).getLatitude()), Float.valueOf(markerModelList.get(i).getLongitude()), markerModelList.get(i).getId(), i);
                        }
//todo Setup markers....ends..


//todo for auto zoom Map......start..
                        for (MarkerModel a : markerModelList) {
                            builder.include(new LatLng(Float.valueOf(a.getLatitude()), Float.valueOf(a.getLongitude())));
                        }

                        LatLngBounds bounds = builder.build();
                        int padding = 300; // offset from edges of the map in pixels
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                            @Override
                            public void onMapLoaded() {
                                //mMap.moveCamera(cu);
                                mMap.animateCamera(cu);
                            }
                        });
//todo for auto zoom Map......end..


                    } else if (jsonObject.getInt("status") == 0) {
                        binding.content.content.rv.setVisibility(View.INVISIBLE);
                        binding.content.content.llMap.setVisibility(View.GONE);
                        binding.content.content.layoutNoData.setVisibility(View.VISIBLE);
                        //Toast.makeText(context, "0", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    /*pd.dismiss();*/
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("-->", "FALURE ");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        mMap.setOnMarkerClickListener(null);

        mUiSettings = mMap.getUiSettings();
        mUiSettings.setScrollGesturesEnabled(true);
    }

    private void mapBottonSheet(JSONObject obj) {
        try {
            final MapBotomsheetBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                    R.layout.map_botomsheet, null, false);
            bsd = new BottomSheetDialog(context);
            bsd.setCancelable(true);
            bsd.setContentView(binding.getRoot());
            bsd.setCanceledOnTouchOutside(true);
            bsd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View view = binding.getRoot();

            binding.tvName.setText(obj.getString("business_name"));
            String add = obj.getString("address") + "\n" + obj.getString("zip") + " " + obj.getString("city");
            binding.tvAddress.setText(add);
            binding.tvReview.setText(obj.getString("total_review") + " Bewertungen");

            Picasso.get()
                    .load(RetrofitServices.BANNERS + obj.getString("id") + "/" + obj.getString("image"))
                    .fit()
                    .placeholder(R.drawable.no_image_available1)
                    .centerCrop()
                    .into(binding.ivSalon);

            if (obj.getString("rating").equals("")) {
                binding.ratingSaloon.setRating(0);
            } else {
                binding.ratingSaloon.setRating(Float.valueOf(obj.getString("rating")));
            }

            List<Sercvice> sevicesLists = gson.fromJson(obj.getJSONArray("sercvices").toString(), new TypeToken<ArrayList<Sercvice>>() {
            }.getType());

            binding.rvSubCat.setLayoutManager(new LinearLayoutManager(context));
            binding.rvSubCat.setNestedScrollingEnabled(false);
            SubCatMapAdapter adapter = new SubCatMapAdapter(context, sevicesLists);
            binding.rvSubCat.setAdapter(adapter);

            binding.llMapMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // Log.e(TAG, "onClick: ------------>>>>>>>>>>>>>>>>>   " );
                        realmController.clearServices();
                        Session.setSalon_id(context, obj.getString("id"));
                        clear_cart_Count();
                        Session.setServiceIds(context, returnServiceIds(sevicesLists));
                        Intent intent = new Intent(context, SalonDetailActivity.class);
                        intent.putExtra(AppConstants.SERVICEIDS, returnServiceIds(sevicesLists));
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            binding.ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //view.animate().translationY(view.getHeight()).setDuration(3000);
                    bsd.dismiss();
                }
            });

            bsd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                     //   CartCountModel cartCountModel = gson.fromJson(resp,CartCountModel.class);
                        //  Log.e(TAG, "onResponse: "+cartCountModel.toString() );
                       /* if (cartCountModel.getStatus() == 1){
                            cartCountData = cartCountModel.getData();
                            // Log.e(TAG, "onResponse: "+cartCountData.toString() );
                            if(cartCountData.getBlock().equals("0")){
                                if (cartCountData.getTotalService().equals("0")){

                                    ViewAnimatorSlideUpDown.slideUp(binding.bottom12);
                                    //slideDown(binding.bottom12);

                                    // toggle(false);
                                }else {
                                    binding.tvitemcount.setText(cartCountData.getTotalService());
                                    // slideUp(binding.bottom12);
                                    // toggle(true);
                                    if (!binding.bottom12.isShown())
                                    {
                                        ViewAnimatorSlideUpDown.slideDown(binding.bottom12);
                                    }
                                    if (cartCountData.getTotalService().equals("1")){

                                    }
                                    // binding.bottomSheetCheckAvailability.tvitemcount.setText(cartCountData.getTotalService());

                                }
                            }


                        }*/
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
    private void setUpMarker(float lat, float lang, final String idd, final int position) {
        LatLng sydney = new LatLng(lat, lang);
        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)).snippet(String.valueOf(position)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                binding.content.content.cvSalonDetail.setVisibility(View.VISIBLE);
                marker.getId();

                Log.e(TAG, "marker Id -- " + String.valueOf(marker.getId()).replace("m", ""));
                Log.e(TAG, "list size -- " + String.valueOf(markerModelList.size()));
                Log.e(TAG, "POSITION -- " + String.valueOf(marker.getSnippet()));
                //mapBottonSheet(Integer.valueOf(String.valueOf(marker.getId()).replace("m","")));
                // todo bottom sheet ws redirected from here, now we calling Api from here
                getMarkerDetail(markerModelList.get(Integer.valueOf(String.valueOf(marker.getSnippet()))).getId());
                return false;
            }
        });

    }

    private void getMarkerDetail(String id) {

        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);

        hashMap.put(AppConstants.SALON_ID, id);

        hashMap.put(AppConstants.SUB_CATEGORY, sub);
        hashMap.put(AppConstants.MAIN_CATEGORY, main);


        Log.e("-->", "Marker SalonDetailModel " + hashMap);

        RetrofitServices.urlServices.salon_mapdetail(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    JSONObject jsonObject = new JSONObject(respo);
                    if (jsonObject.getInt("status") == 1) {
                        mapBottonSheet(jsonObject.getJSONObject("data"));
                    } else if (jsonObject.getInt("status") == 0) {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //   Bungee.slideRight(context);
    }

    private String returnServiceIds(List<Sercvice> sercviceList) {
        Log.e(TAG, "returnServiceIds: " + sercviceList.toString());
        String ids = "";
        StringBuilder stringBuilder = new StringBuilder();
        if (sercviceList.size() > 0) {
            for (int i = 0; i < sercviceList.size(); i++) {
                sercviceList.get(i).getSubcategoryId();
                stringBuilder.append(sercviceList.get(i).getSubcategoryId() + ",");
            }
            ids = stringBuilder.toString().substring(0, stringBuilder.length() - ",".length());
            return ids;
        }
        return ids;
    }


    private void dialogAddSalon() {


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

                if (name.isEmpty()) {
                    dialogbinding.tilName.setError("Bitte Name eingeben");
                    return;
                }
                if (salonName.isEmpty()) {
                    dialogbinding.tilSalon.setError("Bitte Salonname eingeben");
                    return;
                }
                if (city.isEmpty()) {
                    dialogbinding.tilCity.setError("Stadt wird benötigt");
                    return;
                }

                dialog.dismiss();
                addSalon(name, city, salonName);
            }
        });


        dialog.show();
    }

    private void addSalon(String name, String city, String salonName) {

        Utility.hideSoftKeyboard(CategoryListingActivity.this);

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
                    if (jsonObject.getInt("status") == 1) {
                        popup();
                    } else {
                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //  binding.loader.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                //  binding.loader.setVisibility(View.GONE);
            }
        });
    }


    private void popup() {

        Utility.hideSoftKeyboard(CategoryListingActivity.this);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        PopupThankReviewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.popup_thank_review, null, false);
        builder.setView(binding.getRoot());
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();

        binding.tvtitle.setText("Wir haben deinen Vorschlag erhalten.Vielen Dank!");
        binding.tvOk.setText("ZURÜCK ZUR STARTSEITE");
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
