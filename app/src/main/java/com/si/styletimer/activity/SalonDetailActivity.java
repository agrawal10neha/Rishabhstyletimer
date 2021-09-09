package com.si.styletimer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si.styletimer.R;
import com.si.styletimer.Session.RealmController;
import com.si.styletimer.Session.Session;
import com.si.styletimer.adapters.SliderViewPagerAdapter;
import com.si.styletimer.adapters.salonDetail.SalonAllServiceAdapter;
import com.si.styletimer.adapters.salonDetail.SalonMainCategoryAdapter;
import com.si.styletimer.adapters.salonDetail.SalonMatchcatsubcatAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.controller.Controller;
import com.si.styletimer.databinding.ActivitySalonDetailBinding;
import com.si.styletimer.models.cartcount.CartCountData;
import com.si.styletimer.models.cartcount.CartCountModel;
import com.si.styletimer.models.salonDetails.SalonAllServiceModel;
import com.si.styletimer.models.salonDetails.SalonDetailMainModel;
import com.si.styletimer.models.salonDetails.SalonMainCategoryModel;
import com.si.styletimer.models.salonDetails.SalonMatchcatsubcatModel;
import com.si.styletimer.models.salonDetails.SalonSubServiceModel;
import com.si.styletimer.retrofit.JsonUtil;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Animatoo;
import com.si.styletimer.utill.Utility;
import com.si.styletimer.utill.ViewAnimatorSlideUpDown;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalonDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SalonDetailActivity";
    private Context context;
    ActivitySalonDetailBinding binding;
    private String str_salon_id = "";
    String str_service_id = "";
    private Gson gson;
    private Controller controller;
  //  private CartBottomsheetDialogBinding cartBottomsheetDialogBinding;
    // all service adapter
    private List<String> imageList;
    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver broadcastReceivertwo;
    private int currentpos = 0;
    private int appbarstatus = 0;
    private boolean once = true;
    private RealmController realmController;

//    public static CardView btncheckavail;
//    public static TextView tvitemcount;
    private boolean isClicked = false;
    private String salonId = "";
    private String Blocked = "0",URL="";

    // new models
    private SalonDetailMainModel salonDetailMainModel;

    /************All Adapter ******************************************************/
    // service adapter
    private SalonMatchcatsubcatAdapter salonMatchcatsubcatAdapter;
    private List<SalonMatchcatsubcatModel> salonMatchcatsubcatModelList;
    // menu adapter
    private List<SalonMainCategoryModel> salonMainCategoryModelList;
    private SalonMainCategoryAdapter salonMainCategoryAdapter;
    // all service adapter
    private List<SalonAllServiceModel> salonAllServiceModelList;
    private SalonAllServiceAdapter salonAllServiceAdapter;
    Animation in;
   // private BottomSheetDialog bottomSheetDialog;
   // private BottomSheetBehavior sheetBehavior;
    /* Realm Listeners */
    private RealmResults<SalonSubServiceModel> subServiceModelRealmResults;
    private RealmChangeListener<RealmResults<SalonSubServiceModel>> realmChangeListener = (SalonSubServiceModel) -> {
        salonMatchcatsubcatAdapter.notifyDataSetChanged();
        salonAllServiceAdapter.notifyDataSetChanged();
        salonMainCategoryAdapter.notifyDataSetChanged();

    };
    private CartCountData cartCountData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_salon_detail);
        context = this;
        gson = new Gson();
        controller = (Controller) context.getApplicationContext();
       // clear_cart_Count();
        imageList = new ArrayList<>();
        salonDetailMainModel = new SalonDetailMainModel();
        realmController = new RealmController(context);

        /*  Initiate realm change listner */
        subServiceModelRealmResults = realmController.getRealm().where(SalonSubServiceModel.class).findAllAsync();
        subServiceModelRealmResults.addChangeListener(realmChangeListener);
        Log.e(TAG, "onCreate:  ========== list "+subServiceModelRealmResults.toString() );

        initView();
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
    private void initView() {
        // showBottomSheetDialog();
        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }*/

        set_adapter();
        setup_recevier();
       // refreshList();
       realmController.clearServices();
        setup_recevier_two();
        //  bottomSheetCheckAvailability();
        get_service_ids();

        /*if(Session.getUid(context).equals("")){
            binding.Detail.ivfav.setVisibility(View.GONE);
        }else {
            binding.Detail.ivfav.setVisibility(View.VISIBLE);
        }*/

        binding.ivBack.setOnClickListener(this);
        binding.Detail.llVenue.setOnClickListener(this);
        binding.Detail.rlreview.setOnClickListener(this);
        binding.Detail.ivfav.setOnClickListener(this);
        binding.ivShare.setOnClickListener(this);

        binding.appBar.addOnOffsetChangedListener(new  AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    appbarstatus = 1;
                    binding.tvHeading.animate().alpha(1.0f);
                    binding.rlShadow.setVisibility(View.VISIBLE);
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
        binding.btncheckavailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ChooseStylistActivity.class));
            }
        });
    }

    private void setup_recevier() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    int count = Integer.parseInt(intent.getStringExtra(AppConstants.UPDATE_SERVICE));
                    if (count == 1){
                        if (Utility.connectionStatus(context)) {
                            get_cart_Count();
                        } else {
                            Utility.nointernettoast(context);
                        }
                    }
                }
            }
        };
    }

    private void setup_recevier_two() {
        broadcastReceivertwo = new BroadcastReceiver() {
            int count = 0;
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    int position = Integer.parseInt(intent.getStringExtra(AppConstants.FLAG));
                    Log.e(TAG, "onReceive: POSITION "+ position);
                    if (position == 0) {
                        if (currentpos != 0) {
                            salonAllServiceModelList.clear();
                            salonAllServiceModelList.addAll(controller.salonAllServiceModelList);
                            salonAllServiceAdapter.setSalonAllServiceModelList(salonAllServiceModelList);
                            salonAllServiceAdapter.notifyDataSetChanged();
                            currentpos = 0;

                            binding.nestedview.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.nestedview.fullScroll(View.FOCUS_DOWN);
                                }
                            });

                        }
                    } else {
                        Log.e(TAG, "onReceive: "+salonMainCategoryAdapter.getSalonMainCategoryModel().get(position).getId() );
                    //    salonAllServiceAdapter.getSalonAllServiceModel().get(position).getId()
                        String id = salonMainCategoryAdapter.getSalonMainCategoryModel().get(position).getId();
                        currentpos = position;
                        call_category(id);
                    }
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rlreview:
                Intent intent = new Intent(context, SalonReviewActivity.class).putExtra("salonId", str_salon_id);
                intent.putExtra(AppConstants.RATEME,salonDetailMainModel.getData().getDetails().get(0).rateme());
                intent.putExtra(AppConstants.TOTALREVIEW,salonDetailMainModel.getData().getDetails().get(0).getTotalReview());
                startActivity(intent);
              //  Bungee.slideLeft(context);
                break;
            case R.id.llVenue:
                startActivity(new Intent(context, VenueDetailsActivity.class).putExtra("salonId", str_salon_id));
              //  Bungee.slideLeft(context);
                break;
            case R.id.ivBack:
                finish();
                Animatoo.animateSlideRight(context);

                break;
            case R.id.ivfav:
                if(!Session.getUid(context).equals("")){
                    if (!isClicked){
                        isClicked = true;
                        setFavourite(str_salon_id);
                    }else {
                        isClicked = false;
                        setFavourite(str_salon_id);
                    }
                }else {
                    Intent in = new Intent(context, SellectionActivity.class);
                    in.putExtra("destination", "finish");
                    startActivity(in);
                  //  Bungee.slideLeft(context);
                }
                break;

            case R.id.ivShare:
                if(URL==null || URL.equals("")){
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }else {
                    Log.e(TAG, "onClick: URL "+URL );
                    Intent myIntent = new Intent(Intent.ACTION_SEND);
                    myIntent.setType("text/plain");
                    String shareBody = URL;
                    String shareSub = URL;
                    myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                    myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(myIntent, "Share using"));
                }
                break;
        }
    }

/*
    private void bottomSheetCheckAvailability(){

        binding.bottomSheetCheckAvailability.btncheckavailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ChooseStylistActivity.class));
              //  Bungee.slideLeft(context);
            }
        });

        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheetCheckAvailability.getRoot());
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
    }
*/

    private void get_service_ids() {
        try {
            str_salon_id = Session.getSalon_id(context);
           // str_service_id = Session.getServiceIds(context);
           String s= getIntent().getStringExtra(AppConstants.SERVICEIDS);
           // Log.e(TAG, "get_service_ids:---- "+getIntent().getStringExtra(AppConstants.SERVICEIDS) );
            if (getIntent().getStringExtra(AppConstants.SERVICEIDS)!=null){
                str_service_id  = getIntent().getStringExtra(AppConstants.SERVICEIDS);
               Log.e(TAG, "get_service_ids: "+str_service_id );
                if (!str_service_id.equals("")) {
                    if (Utility.connectionStatus(context)) {
                        getSalonDetailsPage();
                    } else {
                        Utility.nointernettoast(context);
                    }
                }
            }

            if (Utility.connectionStatus(context)) {
                getSalonDetailsPage();
            } else {
                Utility.nointernettoast(context);
            }

            Log.e(TAG, "get_service_ids: "+str_salon_id+ " "+str_service_id );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void show_loader() {
        binding.loader.setVisibility(View.VISIBLE);
    }

    private void hide_loader() {
        binding.loader.setVisibility(View.GONE);
    }

    private void set_adapter() {

//        // main service adapter
        salonMatchcatsubcatModelList = new ArrayList<>();
        LinearLayoutManager linearLayoutManagerone = new LinearLayoutManager(context);
        linearLayoutManagerone.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManagerone.setAutoMeasureEnabled(true);
        binding.Detail.recymainservice.setLayoutManager(linearLayoutManagerone);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.Detail.recymainservice.getContext(), ((LinearLayoutManager) linearLayoutManagerone).getOrientation());
        binding.Detail.recymainservice.addItemDecoration(dividerItemDecoration);
        binding.Detail.recymainservice.setHasFixedSize(true);
        binding.Detail.recymainservice.setNestedScrollingEnabled(false);
        salonMatchcatsubcatAdapter = new SalonMatchcatsubcatAdapter(context, salonMatchcatsubcatModelList);
        binding.Detail.recymainservice.setAdapter(salonMatchcatsubcatAdapter);
        salonMatchcatsubcatAdapter.notifyDataSetChanged();

        // menu adapter
        salonMainCategoryModelList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setAutoMeasureEnabled(true);
        binding.Detail.recymenucategory.setHasFixedSize(true);
        binding.Detail.recymenucategory.setNestedScrollingEnabled(false);
        binding.Detail.recymenucategory.setLayoutManager(linearLayoutManager);
        salonMainCategoryAdapter = new SalonMainCategoryAdapter(context, salonMainCategoryModelList);
        binding.Detail.recymenucategory.setAdapter(salonMainCategoryAdapter);
        salonMainCategoryAdapter.notifyDataSetChanged();

        // all service adapter
        salonAllServiceModelList = new ArrayList<>();
        LinearLayoutManager linearLayoutManagerthree = new LinearLayoutManager(context);
        linearLayoutManagerthree.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManagerthree.setAutoMeasureEnabled(true);
        binding.Detail.recyallservice.setLayoutManager(linearLayoutManagerthree);
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(binding.Detail.recyallservice.getContext(), ((LinearLayoutManager) linearLayoutManagerthree).getOrientation());
        binding.Detail.recyallservice.addItemDecoration(dividerItemDecoration2);
        binding.Detail.recyallservice.setHasFixedSize(true);
        binding.Detail.recyallservice.setNestedScrollingEnabled(false);
        salonAllServiceAdapter = new SalonAllServiceAdapter(context, salonAllServiceModelList);
        binding.Detail.recyallservice.setAdapter(salonAllServiceAdapter);
        salonAllServiceAdapter.notifyDataSetChanged();
    }

    private void call_category(String catid) {

       // binding.loader.setVisibility(View.VISIBLE);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.SALON_ID, str_salon_id);
        hashMap.put(AppConstants.CATEGORY_ID, catid);

        Log.e(TAG, "call_category: " + hashMap);

        RetrofitServices.urlServices.category_allservice(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String rees = response.body().string().trim();
                    JSONObject jsonObject = new JSONObject(rees);

                    binding.loader.setVisibility(View.GONE);

                    if (jsonObject.getInt("status") == 1) {

                        JSONArray data = jsonObject.getJSONObject("data").getJSONArray("all_service");
                        // all service data
                        salonAllServiceModelList.clear();
                                salonAllServiceModelList = gson.fromJson(data.toString(),
                                new TypeToken<ArrayList<SalonAllServiceModel>>() {
                                }.getType());


                        salonAllServiceAdapter.setSalonAllServiceModelList(salonAllServiceModelList);
                        salonAllServiceAdapter.notifyDataSetChanged();

                        binding.nestedview.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.nestedview.fullScroll(View.FOCUS_DOWN);
                            }
                        });


                    } else {

                        salonAllServiceModelList.clear();
                     //   Log.e(TAG, "onResponse: "+salonAllServiceModelList.size() );

                        salonAllServiceAdapter.setSalonAllServiceModelList(salonAllServiceModelList);
                        salonAllServiceAdapter.notifyDataSetChanged();
                        Log.e(TAG, "onResponse: "+"-----------   "+jsonObject.getString("response_message"));
                       // Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.loader.setVisibility(View.GONE);

            }
        });
    }

    private void refreshList() {
        salonMatchcatsubcatModelList.clear();
        salonMainCategoryModelList.clear();;
        salonAllServiceModelList.clear();;
        controller.salonAllServiceModelList.clear();

        salonMatchcatsubcatAdapter.notifyDataSetChanged();;
        salonMainCategoryAdapter.notifyDataSetChanged();
        salonAllServiceAdapter.notifyDataSetChanged();

        if(Utility.connectionStatus(context)){
            getSalonDetailsPage();;
        }else {
            Utility.nointernettoast(context);
        }
    }

    private void get_cart_Count() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.SALON_ID, Session.getSalon_id(context));

       Log.e(TAG, "get_cart_Count: --------->>>>>>>>>>>> "+hashMap );

        RetrofitServices.urlServices.total_service_count(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (JsonUtil.mainresp(context, response)) {

                        String resp = response.body().string().trim();
                        Log.e(TAG, "get_cart_Count: "+resp );
                        CartCountModel cartCountModel = gson.fromJson(resp,CartCountModel.class);
                      //  Log.e(TAG, "onResponse: "+cartCountModel.toString() );
                        if (cartCountModel.getStatus() == 1){
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


                        }
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

    private void toggle(boolean show) {
        View redLayout = findViewById(R.id.bottom12);
        ViewGroup parent = findViewById(R.id.rlll);

        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(200);
        transition.addTarget(R.id.bottom12);

        TransitionManager.beginDelayedTransition(parent, transition);
        redLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(230);
        //animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        view.setVisibility(View.GONE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(230);
        //animate.setFillAfter(true);
        view.startAnimation(animate);
    }


    private void getSalonDetailsPage() {

        show_loader();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.SALON_ID, str_salon_id);
        hashMap.put(AppConstants.SERVICIDS, str_service_id);
        hashMap.put(AppConstants.UID, Session.getUid(context));

        Log.e(TAG, "getSalonDetailsPage: Hash "+ hashMap);

        RetrofitServices.urlServices.salon_profile_page(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                  //  Log.e(TAG, "getSalonDetailsPage onResponse: ---------->>>>>>   "+JsonUtil.resp(response));
                    hide_loader();
                    if (JsonUtil.mainresp(context, response)) {
                        String respo = response.body().string().trim();
                        salonDetailMainModel = gson.fromJson(respo, SalonDetailMainModel.class);
                        if (salonDetailMainModel.getStatus() == 1) {

                            Blocked = salonDetailMainModel.getData().getDetails().get(0).getBlock();
                            URL = salonDetailMainModel.getData().getDetails().get(0).getUrl();

                            List<SalonSubServiceModel> salonValueModelList = new ArrayList<>();
                            salonValueModelList.clear();
                            for (int i = 0; i<salonDetailMainModel.getData().getAllService().size();i++){
                                List<SalonSubServiceModel> salonSubServiceModelList = salonDetailMainModel.getData().getAllService().get(i).getSubServices();
                                for (SalonSubServiceModel salonValueModel : salonSubServiceModelList){
                                    salonValueModelList.add(salonValueModel);
                                }
                            }
                            realmController.getRealm().beginTransaction();
                            realmController.getRealm().copyToRealmOrUpdate(salonValueModelList);
                            realmController.getRealm().commitTransaction();

                            // set main details
                            binding.setDetailModel(salonDetailMainModel.getData().getDetails().get(0));
                            binding.Detail.setDetailModel(salonDetailMainModel.getData().getDetails().get(0));
                            controller.detailModel = salonDetailMainModel.getData().getDetails().get(0);
                            controller.detailModel.setBusinessName(salonDetailMainModel.getData().getDetails().get(0).getBusinessName());

                            setData();

                            // set main service adapter

                            salonMatchcatsubcatAdapter.setSalonMatchcatsubcatModelList(salonDetailMainModel.getData().getMatchcatsubcat());
                            salonMatchcatsubcatAdapter.notifyDataSetChanged();

                            // set menu adapter
                            salonMainCategoryModelList.clear();
                            SalonMainCategoryModel salonMainCategoryModel = new SalonMainCategoryModel();
                            salonMainCategoryModel.setCategoryName("Alle");
                            salonMainCategoryModel.setImage("All");
                            salonMainCategoryModel.setIcon("All");
                            salonMainCategoryModel.setId("All");
                            salonMainCategoryModelList.add(salonMainCategoryModel);
                            salonMainCategoryModelList.addAll(salonDetailMainModel.getData().getMainCategory());
                            salonMainCategoryAdapter.setSalonMainCategoryModelList(salonMainCategoryModelList);
                            salonMainCategoryAdapter.notifyDataSetChanged();

                            Log.e(TAG, "onResponse: "+salonMainCategoryModelList.toString() );

                            // set allservice adapter
                            controller.salonAllServiceModelList = salonDetailMainModel.getData().getAllService();
                            salonAllServiceAdapter.setSalonAllServiceModelList(salonDetailMainModel.getData().getAllService());
                            salonAllServiceAdapter.notifyDataSetChanged();

                            // Log.e(TAG, "onResponse: -Y-Y- "+ salonDetailMainModel.getData().getDetails().get(0).getFavorite());
                            if(salonDetailMainModel.getData().getDetails().get(0).getFavorite().equals("0")){
                                binding.Detail.ivfav.setImageDrawable(context.getDrawable(R.drawable.ic_fav_border));
                            }else if(salonDetailMainModel.getData().getDetails().get(0).getFavorite().equals("1")){
                                binding.Detail.ivfav.setImageDrawable(context.getDrawable(R.drawable.ic_fav_filled));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    hide_loader();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_loader();
            }
        });
    }

    private void setData() {

        if (controller.detailModel.getRating().equals("0")) {
            binding.Detail.tvreview.setText("0.0");
        } else {
            binding.Detail.tvreview.setText(controller.detailModel.getRating());
        }

        if (controller.detailModel.getImage().equals("")) {
            binding.ivsalon.setVisibility(View.VISIBLE);
            binding.viewPager.setVisibility(View.GONE);
        } else {
            binding.viewPager.setVisibility(View.VISIBLE);
            binding.ivsalon.setVisibility(View.GONE);
            if (!controller.detailModel.getImage().equals("")) {
                imageList.add(controller.detailModel.getImage());
            }
            if (!controller.detailModel.getImage1().equals("")) {
                imageList.add(controller.detailModel.getImage1());
            }
            if (!controller.detailModel.getImage2().equals("")) {
                imageList.add(controller.detailModel.getImage2());
            }
            if (!controller.detailModel.getImage3().equals("")) {
                imageList.add(controller.detailModel.getImage3());
            }
            if (!controller.detailModel.getImage4().equals("")) {
                imageList.add(controller.detailModel.getImage4());
            }
            SliderViewPagerAdapter adapter = new SliderViewPagerAdapter(context, imageList, controller.detailModel.getId());
            binding.viewPager.setAdapter(adapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceivertwo);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Utility.connectionStatus(context)) {
            get_cart_Count();
        } else {
            Utility.nointernettoast(context);
        }

        /*get_service_ids();*/
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceivertwo, new IntentFilter(AppConstants.BC_UPDATE_SERVICE_TWO));
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(AppConstants.BC_UPDATE_SERVICE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subServiceModelRealmResults.removeAllChangeListeners();
        realmController.closeRealm();
    }

    private void setFavourite(String str_salon_id) {
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
                            int fav = jsonObject.getInt("favourite");
                            if (fav == 1){
                                isClicked = true;
                                binding.Detail.ivfav.setImageDrawable(context.getDrawable(R.drawable.ic_fav_filled));
                            }else {
                                isClicked = false;
                                binding.Detail.ivfav.setImageDrawable(context.getDrawable(R.drawable.ic_fav_border));
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

/*
    private void showBottomSheetDialog() {
        cartBottomsheetDialogBinding  = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cart_bottomsheet_dialog,null, false);
        bottomSheetDialog = new BottomSheetDialog(context,R.style.com_facebook_auth_dialog);
        bottomSheetDialog.setContentView(cartBottomsheetDialogBinding.getRoot());
        if (cartCountData != null) {
            cartBottomsheetDialogBinding.tvitemcount.setText(cartCountData.getTotalService());
        }
        cartBottomsheetDialogBinding.btncheckavailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ChooseStylistActivity.class));
            }
        });

    }
*/

}
