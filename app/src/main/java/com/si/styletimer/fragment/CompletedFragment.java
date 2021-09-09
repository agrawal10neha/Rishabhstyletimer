package com.si.styletimer.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.activity.ChooseStylistActivity;
import com.si.styletimer.activity.WriteReviewActivity;
import com.si.styletimer.adapters.CompletedBookindAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.controller.Controller;
import com.si.styletimer.databinding.FragmentCompletedBinding;
import com.si.styletimer.databinding.PopupThankReviewBinding;
import com.si.styletimer.models.mybooking_list_model.completed_booking_model.CompletedModel;
import com.si.styletimer.models.salonDetails.SalonDetailModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.ScrollListener;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedFragment extends Fragment {
    private static final String TAG = "CompletedFragment";
    FragmentCompletedBinding binding;
    private Context context;
    private View view;
    private List<CompletedModel> completedList = new ArrayList<>();
    private Gson gson;
    private Dialog dialog;
    private CompletedBookindAdapter adapter;

    private int pastVisiblesItems = 0, visibleItemCount = 0, totalItemCount = 0;
    private Boolean loadingMore = false;
    private Boolean viewMore = false;
    private Boolean paginationCheck = false;

    private Controller controller;
    private LinearLayoutManager m;

    private float ratingg=0;
    private String employeId="",cbAnonymous="";

    private ScrollListener scrollListener;
    private boolean mLoading = false;
    private int page=1;

    public CompletedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_completed, container, false);
        context = getActivity();
        gson=new Gson();
        initView();
        controller = (Controller) context.getApplicationContext();


        view = binding.getRoot();
        return view;
    }

    private void initView() {
        setUpPagination();
    }

    private void setUpPagination() {
         m = new LinearLayoutManager(context);

        binding.rvCompleted.setLayoutManager(m);
        binding.rvCompleted.setNestedScrollingEnabled(false);

        adapter=new CompletedBookindAdapter(context, completedList);
        binding.rvCompleted.setAdapter(adapter);

        binding.rvCompleted.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if ((m.getChildCount() + m.findFirstVisibleItemPosition()) >= m.getItemCount()) {
                        if (!mLoading ){
                            Log.e(TAG, "onLoadMore: ----------------------" );
                            // Scrolled to bottom. Do something here.
                            if (Utility.connectionStatus(context)) {
                                page = page+1;
                                getCompleted();
                                mLoading = true;
                            } else {
                                Utility.nointernettoast(context);
                            }

                        }
                    }
                }
            }
        });



       /* binding.swipyrefreshlayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                try {

                    if (Utility.connectionStatus(context)) {
                        page = page+1;
                        getCompleted();
                    } else {
                        Toast.makeText(context, "No Internet, Check connection status", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
*/

    }


    private void getCompleted(){
        Log.e(TAG, "getCompleted: --------------------->>>>>>>>>>>>>> page "+page );

        final ProgressDialog pd =new ProgressDialog(context);
         pd.setTitle(context.getResources().getString(R.string.please_wait));
         pd.setCancelable(false);
         pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);

        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.ACTION, "completed");
        hashMap.put(AppConstants.PAGE, String.valueOf(page));

        //Log.e("-->", "completed booking list " + hashMap);

        RetrofitServices.urlServices.booking_listing(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();
                    String respo = response.body().string().trim();
                   Log.e("completed booking-->", "onResponse:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);
                    Log.e("frag   -----   ",respo);
                    if(jsonObject.getInt("status")==1){

                        if(page==1){

                            completedList = gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<CompletedModel>>() {}.getType());
                            adapter.setCompletedModelList(completedList);
                            adapter.notifyDataSetChanged();

                            if(completedList.size()<1){
                                binding.rvCompleted.setVisibility(View.GONE);
                                binding.ivNoData.setVisibility(View.VISIBLE);
                            }else {
                                binding.rvCompleted.setVisibility(View.VISIBLE);
                                binding.ivNoData.setVisibility(View.GONE);
                            }

                        }else if (page>1){
                            List<CompletedModel> completedList_2 = new ArrayList<>();
                            completedList_2 =gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<CompletedModel>>() {}.getType());
                            completedList.addAll(completedList_2);
                            adapter.notifyDataSetChanged();

                            mLoading = false;
                        }

                        adapter.setOnItemClickListener((view, position) -> {

                            if(view.getId()==R.id.tvRebook){
                                Session.setSalon_id(context,completedList.get(position).getMerchantId());
                                rebook(completedList.get(position).getId(),completedList.get(position).getMerchantId());
                               // popup(completedList.get(position).getId(), completedList.get(position).getMerchantId(),"rebook");
                            }else if(view.getId()==R.id.tvReView){
                             //   popup(completedList.get(position).getId(), completedList.get(position).getMerchantId(),"rating");

                                /*Intent intent = new Intent(context, RateAndReviewActivity.class);
                                intent.putExtra("bookId",completedList.get(position).getId());
                                intent.putExtra("saloonId",completedList.get(position).getMerchantId());
                                startActivity(intent);*/

                                Intent intent = new Intent(context, WriteReviewActivity.class);
                                intent.putExtra(AppConstants.BOOK_ID, completedList.get(position).getId());
                                intent.putExtra(AppConstants.SALON_ID, completedList.get(position).getMerchantId());
                                intent.putExtra(AppConstants.EMPLOYEEID, completedList.get(1).getEmployeId());
                                intent.putExtra(AppConstants.RES_STATUS, completedList.get(position).getResheduleStatus());
                                intent.putExtra(AppConstants.FLAG, "list");
                                startActivity(intent);

                            }else if (view.getId() == R.id.btnCancel){
                                popup(completedList.get(position).getId(), completedList.get(position).getMerchantId(),"");
                            }
                        });

                    }else if(jsonObject.getInt("status")==0){

                        if(page==1){
                            //Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                            binding.swipyrefreshlayout.setVisibility(View.GONE);
                            binding.ivNoData.setVisibility(View.VISIBLE);

                        }else if(page>1){
                            binding.swipyrefreshlayout.setVisibility(View.VISIBLE);
                            binding.ivNoData.setVisibility(View.GONE);
                        }

                    }

                  //  pd.dismiss();
                    binding.swipyrefreshlayout.setRefreshing(false);
                }
                catch (Exception e){
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                pd.dismiss();Log.e("-->","FALURE1 ");
            }
        });
    }

    private void popup(String bookingId,String salonId,String flag){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.booking_cancel_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RadioGroup rg = dialog.findViewById(R.id.rg);
        LinearLayout llOther = dialog.findViewById(R.id.llOther);
        RelativeLayout rlRating = dialog.findViewById(R.id.rlRating);

        TextView yes = dialog.findViewById(R.id.yes);
        TextView no = dialog.findViewById(R.id.no);

        TextView head = dialog.findViewById(R.id.head);
        TextView sHead = dialog.findViewById(R.id.sHead);

        RatingBar RatingBar = dialog.findViewById(R.id.ratingbar);
        TextInputLayout ilReview =  dialog.findViewById(R.id.ilReview);
        TextInputEditText etReview = dialog.findViewById(R.id.etReview);
        Button btSubmit = dialog.findViewById(R.id.btSubmit);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        CheckBox cb = dialog.findViewById(R.id.cbanonymous);

        Button btCancel = dialog.findViewById(R.id.btCancel);
        ImageView icClose = dialog.findViewById(R.id.icClose);

        if(flag.equals("rebook")){
            rg.setVisibility(View.GONE);
            llOther.setVisibility(View.VISIBLE);
            rlRating.setVisibility(View.GONE);

            //--Rating--
            reviewSetup(RatingBar,ilReview,etReview,btSubmit,bookingId,salonId,dialog,cb);
            yes.setText("NOCHMAL BUCHEN");
            no.setText("SCHLIESSEN");
            head.setText(context.getResources().getString(R.string.rebook));
            sHead.setText("Bist du sicher, dass du diese Services erneut buchen möchtest?");

        }else if(flag.equals("rating")){

            llOther.setVisibility(View.GONE);
            rlRating.setVisibility(View.VISIBLE);

            RatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    ratingg=rating;
                }
            });


            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String review = etReview.getText().toString();
                    int ratonng = (int) ratingg;
                    Log.e(TAG, "onClick: "+ratonng );

                    if (ratonng == 0){
                        Toast.makeText(context, "Bitte wähle eine Bewertung", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (ratonng < 4){
                        if (TextUtils.isEmpty(review)){
                            ilReview.setError("Bitte schreibe eine Bewertung");
                            ilReview.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                            ilReview.setHintTextAppearance(R.style.EditTextHintStyle);
                        }else {
                            dialog.dismiss();
                            review = "";
                            postReview(bookingId,salonId,String.valueOf(ratingg),review);
                        }
                    }else {
                        dialog.dismiss();
                        postReview(bookingId,salonId,String.valueOf(ratingg),review);
                    }
                }
            });


        }else if (flag.equals("cancel")){
            yes.setText(context.getResources().getString(R.string.yes));
            no.setText(context.getResources().getString(R.string.no));
            head.setText(context.getResources().getString(R.string.cancel_booking));
            sHead.setText(context.getResources().getString(R.string.are_you_sure_cancel_booking));

            yes.setVisibility(View.GONE);
            no.setVisibility(View.GONE);
            btCancel.setVisibility(View.VISIBLE);
            icClose.setVisibility(View.GONE);
        }



        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.setSalon_id(context,salonId);
                rebook(bookingId,salonId);
                dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    } // todo call Rebook Api----

    private void reviewSetup(RatingBar ratingBar, TextInputLayout ilReview, TextInputEditText etReview, Button btSubmit,String bookId,String salonId,Dialog dialog, CheckBox cb) {

        etReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()>0){

                    ilReview.setError(null);
                    ilReview.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    ilReview.setHintTextAppearance(R.style.EditTextHintStyle);

                }else {

                    ilReview.setError("Bitte schreibe eine Bewertung");

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingg=rating;
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "onClick: "+ratingg);

                if(cb.isChecked()){
                    cbAnonymous="1";
                }else {
                    cbAnonymous="0";
                }

                if(ratingg==0){
                    Toast.makeText(context, "Bitte Sterne-Bewertung wählen", Toast.LENGTH_SHORT).show();
                }else {

                    if(ratingg<4 ){
                        if(etReview.getText().toString().equals("")){
                            ilReview.setError("Bitte Bewertung schreiben");
                        }else {
                            postReview(bookId,salonId,String.valueOf(ratingg),etReview.getText().toString());
                        }
                    }else {
                        dialog.dismiss();
                        postReview(bookId,salonId,String.valueOf(ratingg),etReview.getText().toString());
                    }

                }
            }
        });

    }

    private void rebook(String bookingId,String salonId){

        final ProgressDialog pd =new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);

        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.BOOK_ID, bookingId);
        hashMap.put(AppConstants.SALON_ID, salonId);

        Log.e("-->", "Rebook-->" + hashMap);

        RetrofitServices.urlServices.rebook_service(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("Rebook --> ", "onResponse:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if(jsonObject.getInt("status")==1){

                        JSONObject data = jsonObject.getJSONObject("data");
                        controller.detailModel = gson.fromJson(data.getJSONArray("details").getJSONObject(0).toString(), SalonDetailModel.class);
                        controller.day="Today";

                        startActivity(new Intent(context, ChooseStylistActivity.class));
                    //    Bungee.slideLeft(context);
                       // Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();

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
                Log.e("-->","FALURE ");
            }
        });
    }

    private void postReview(String bookId,String salonId,String rating,String review) {

        final ProgressDialog pd =new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));

        hashMap.put(AppConstants.BOOK_ID, bookId);
        hashMap.put(AppConstants.SALON_ID, salonId);

        hashMap.put(AppConstants.RATING, rating);
        hashMap.put(AppConstants.REVIEW, review);
        hashMap.put(AppConstants.EMPLOYEEID,completedList.get(1).getEmployeId() );
        hashMap.put(AppConstants.ANONYMOUS,cbAnonymous );


        Log.e("-->", "Review " + hashMap);

        RetrofitServices.urlServices.addreview_service(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("Review-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if(jsonObject.getInt("status")==1){

                        /*finish();*/
                        popup();


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
                Log.e("-->","FALURE ");
            }
        });
    }

    private void recallApi(){
        if (Utility.connectionStatus(context)){
            completedList.clear();
            getCompleted();
        }else {
            Utility.nointernettoast(context);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: >>>>>>>>>>>>>>>>>>>>>>" );
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: >>>>>>>>>>>>>>>>>>>>>>" );
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: >>>>>>>>>>>>>>>>>>>>" );

        if (Utility.connectionStatus(context)){
            page=1;
            completedList.clear();
            getCompleted();
        }else {
            Utility.nointernettoast(context);
        }
    }

    private void popup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        PopupThankReviewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout. popup_thank_review, null, false);
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                recallApi();
            }
        });

    }

}
