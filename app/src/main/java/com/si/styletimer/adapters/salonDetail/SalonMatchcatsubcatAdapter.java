package com.si.styletimer.adapters.salonDetail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.graphics.Point;
import android.os.Build;

import androidx.annotation.UiThread;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.si.styletimer.R;
import com.si.styletimer.Session.RealmController;
import com.si.styletimer.Session.Session;
import com.si.styletimer.activity.SellectionActivity;
import com.si.styletimer.activity.Splash;
import com.si.styletimer.adapters.MainServiceAdapter;
import com.si.styletimer.adapters.ReviewdialogAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.controller.Controller;
import com.si.styletimer.databinding.DialogDiscountPriceBindingImpl;
import com.si.styletimer.databinding.DialogShowDetailsBinding;
import com.si.styletimer.databinding.SalonmatchcatsubcatCellBinding;
import com.si.styletimer.models.salonDetails.SalonDetailMainModel;
import com.si.styletimer.models.salonDetails.SalonDetailModel;
import com.si.styletimer.models.salonDetails.SalonMatchcatsubcatModel;
import com.si.styletimer.models.salonDetails.SalonSubServiceModel;
import com.si.styletimer.models.salonDetails.SalonValueModel;
import com.si.styletimer.models.showdatarating.Data;
import com.si.styletimer.models.showdatarating.RatingREviewDataModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmQuery;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalonMatchcatsubcatAdapter extends RecyclerView.Adapter<SalonMatchcatsubcatAdapter.ViewHolder> {
    private static final String TAG = "SalonMatchcatsubcatA";
    private final Context mContext;
    private List<SalonMatchcatsubcatModel> mSalonMatchcatsubcatModelList;
    private OnItemClickListener onItemClickListener;
    private List<Integer> priceList = new ArrayList<>();
    private List<Integer> disCountMaxList = new ArrayList<>();
    private RealmController realmController;
    private SalonSubServiceModel subServiceModel;
    private Controller controller;
    private Gson gson;
    SalonDetailModel salonDetailModel;
    private SalonDetailMainModel salonDetailMainModel;


    public SalonMatchcatsubcatAdapter(Context mContext, List<SalonMatchcatsubcatModel> mSalonMatchcatsubcatModelList) {
        this.mSalonMatchcatsubcatModelList = mSalonMatchcatsubcatModelList;
        this.mContext = mContext;
        realmController = new RealmController(mContext);
        controller = (Controller) mContext.getApplicationContext();
        gson = new Gson();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final SalonmatchcatsubcatCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.salonmatchcatsubcat_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SalonMatchcatsubcatModel mSalonMatchcatsubcatModel = this.mSalonMatchcatsubcatModelList.get(position);
        holder.bind(mSalonMatchcatsubcatModel);
        String strSalonSubIds = mSalonMatchcatsubcatModel.getmSubId();
        holder.binding.tvitemmin.setText(mSalonMatchcatsubcatModel.getServiceDuration());
        SalonSubServiceModel subServiceModel = realmController.getSalonSubServiceModelList(strSalonSubIds);

        if (mSalonMatchcatsubcatModel.getServiceDetail() != null && !mSalonMatchcatsubcatModel.getServiceDetail().equals("")) {
            holder.binding.tvShowDetails.setVisibility(View.VISIBLE);
        } else if (mSalonMatchcatsubcatModel.getReviewcount() != null && !mSalonMatchcatsubcatModel.getReviewcount().equals("0")) {
            holder.binding.tvShowDetails.setVisibility(View.VISIBLE);
        } else {
            holder.binding.tvShowDetails.setVisibility(View.GONE);
        }

        if (mSalonMatchcatsubcatModel.getStatus() == 0) {

            LinearLayoutManager linearLayoutanager = new LinearLayoutManager(mContext);
            linearLayoutanager.setOrientation(LinearLayoutManager.VERTICAL);
            linearLayoutanager.setAutoMeasureEnabled(true);
            holder.binding.recymainservice.setLayoutManager(linearLayoutanager);
            holder.binding.recymainservice.setHasFixedSize(true);

           /* List<SalonSubServiceModel> salonSubServiceModelList = new ArrayList<>();
            salonSubServiceModelList.add(subServiceModel);*/

            //  Log.e(TAG, "onBindViewHolder sizeeeeeee value : " + mSalonMatchcatsubcatModel.getValue().size());

           //kamal List<SalonSubServiceModel> salonSubServiceModelt = realmController.getSalonSubServiceModel(mSalonMatchcatsubcatModel.getmSubId());
           // List<SalonSubServiceModel> salonSubServiceModel = realmController.getSalonSubServiceModelmId(mSalonMatchcatsubcatModel.getmId());
           // Log.e(TAG, "onBindViewHolder:SSSSSSSSSSSSSSSSSSSS " + salonSubServiceModel.toString() );
            // List<SalonSubServiceModel> salonSubServiceModel = realmController.getSalonSubServiceModelmId(mSalonMatchcatsubcatModel.getmId());

            //  Log.e(TAG, "onBindViewHolder sizeeeeeee: " + mSalonMatchcatsubcatModel.getmSubId());
            //  Log.e(TAG, "onBindViewHolder sizeeeeeee: " + salonSubServiceModel.size());

       //     Log.e(TAG, "onBindViewHolder:999999 "+salonSubServiceModel.size() );

            List<String> idddd = new ArrayList<>();
            List<SalonSubServiceModel> salonSubServiceModel = new ArrayList<>();
            for (int i = 0;i<mSalonMatchcatsubcatModel.getValue().size();i++)
            {
               idddd.add(mSalonMatchcatsubcatModel.getValue().get(i).getId());
                salonSubServiceModel.add(realmController.getSalonSubServiceModelmId(mSalonMatchcatsubcatModel.getValue().get(i).getId()));
               // List<SalonSubServiceModel> salonSubServiceModel = realmController.getSalonSubServiceModelmId(mSalonMatchcatsubcatModel.getValue().get(i).getId());
            }

           // List<SalonSubServiceModel> salonSubServiceModel = realmController.getSalonSubServiceModelmId(mSalonMatchcatsubcatModel.getValue().get(position).getId());
            Log.e(TAG, "onBindViewHolder: ideeeee " + idddd.toString() +"  "+ salonSubServiceModel.toString());
            MainServiceAdapter mainServiceAdapter = new MainServiceAdapter(mContext, salonSubServiceModel);
            holder.binding.recymainservice.setAdapter(mainServiceAdapter);
            mainServiceAdapter.notifyDataSetChanged();

            holder.binding.tttt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.binding.recymainservice.getVisibility() == View.GONE) {
                      //  holder.binding.recymainservice.setVisibility(View.VISIBLE);
                        holder.binding.drop.setRotation(0);
                        expand(holder.binding.recymainservice, position, mContext);
                    } else {
                      //    holder.binding.recymainservice.setVisibility(View.GONE);
                        holder.binding.drop.setRotation(270);
                        collapse(holder.binding.recymainservice);
                    }
                }
            });


            holder.binding.drop.setVisibility(View.VISIBLE);
            holder.binding.btnservieitemadd.setVisibility(View.GONE);
            // String minutes = mSalonMatchcatsubcatModel.getValue().get(0).getDuration() + " min";
            // holder.binding.tvitemmin.setText(minutes);

/*
            for (int i = 0; i < mSalonMatchcatsubcatModel.getValue().size(); i++) {
                if (mSalonMatchcatsubcatModel.getValue().get(i).getDiscountPrice().equals("0")) {
                    priceList.add(Integer.valueOf(mSalonMatchcatsubcatModel.getValue().get(i).getPrice()));
                } else {
                    priceList.add(Integer.valueOf(mSalonMatchcatsubcatModel.getValue().get(i).getDiscountPrice()));
                }
            }
*/
            /*for (int i = 0; i < mSalonMatchcatsubcatModel.getValue().size(); i++) {
                if (mSalonMatchcatsubcatModel.getValue().get(i).getDiscountPrice().equals("0")) {
                    holder.binding.tvitemprice.setText(mSalonMatchcatsubcatModel.getValue().get(i).getPrice()+AppConstants.EURO);
                } else {
                    holder.binding.tvsaveupto.setVisibility(View.VISIBLE);
                    priceList.add(Integer.valueOf(mSalonMatchcatsubcatModel.getValue().get(i).getDiscountPrice()));
                    disCountMaxList.add(Integer.valueOf(discount_per(mSalonMatchcatsubcatModel.getValue().get(i).getPrice(), mSalonMatchcatsubcatModel.getValue().get(i).getDiscountPrice())));
                    holder.binding.tvitemprice.setText(mContext.getResources().getString(R.string.from)+" "+Collections.min(priceList)+AppConstants.EURO);
                    holder.binding.tvsaveupto.setText(mContext.getResources().getString(R.string.save_up_to)+" " + Collections.max(disCountMaxList) + "%");

                }
            }
*/
            if (!mSalonMatchcatsubcatModel.getMinPrice().equals("")) {
                String aa[] = mSalonMatchcatsubcatModel.getMinPrice().split("\\s+");
                String ss = mSalonMatchcatsubcatModel.getMinPrice();
                Log.e("min price",ss);
                if (aa.length>1)
                {
                    String ab = aa[0];
                    Log.i("min price 1",ab);
                    String pp = aa[1];
                    Log.i("min price 1",pp);
                    holder.binding.tvitemprice.setText(ab + " " + Utility.getPriceReplaceDotWithComma(pp) + AppConstants.EURO);
                }else {
                    String ab = aa[0];
                   // String pp = aa[1];

                    if (salonSubServiceModel != null && salonSubServiceModel.size()>1)
                    {
                        holder.binding.tvitemprice.setText("ab " +Utility.getPriceReplaceDotWithComma(ab) + AppConstants.EURO);
                    }else {
                        holder.binding.tvitemprice.setText(Utility.getPriceReplaceDotWithComma(ab) + AppConstants.EURO);
                    }
                }

            }

            if (!mSalonMatchcatsubcatModel.getMaxDiscount().equals("")) {
                holder.binding.tvsaveupto.setVisibility(View.VISIBLE);
                holder.binding.tvsaveupto.setText(mContext.getResources().getString(R.string.save_up_to) + " " + Utility.getPriceReplaceDotWithComma(mSalonMatchcatsubcatModel.getMaxDiscount()) + "% ");
            } else {
                holder.binding.tvsaveupto.setVisibility(View.GONE);
            }

            holder.binding.tvsaveupto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dialogShowDetails(mSalonAllServiceModel.getServiceDetail(),mSalonAllServiceModel.getCategoryName());
                    String discount= (String) holder.binding.tvsaveupto.getText();
                    String discountPrice=discount.replace("spare bis","");

                   // String Business=salonDetailModel.getBusinessName();
                   // Log.e("text",salonDetailModel.getBusinessName());
                    //String mid = mSalonAllServiceModel.getServicedetailid();
                    //String allids = returnServiceIds(mSalonAllServiceModel.getSubServices());
                    dialogShowDiscount(discountPrice);
                   // dialogShowDiscount(discountPrice,Business);

                }
            });
           // Log.e(TAG, "onBindViewHolder:3333 "+mSalonMatchcatsubcatModel.getmSubId()+" "+salonSubServiceModel.size() );

            if (salonSubServiceModel != null){
                for (int i = 0; i < salonSubServiceModel.size(); i++) {
                //    Log.e(TAG, "onBindViewHolder:6666 "+salonSubServiceModel.get(i).getInCart());

                    if (salonSubServiceModel.get(i).getInCart().equals("1")) {
                        holder.binding.recymainservice.setVisibility(View.VISIBLE);
                        holder.binding.drop.setRotation(0);

                        holder.binding.tvSelected.setVisibility(View.VISIBLE);
                        List<SalonSubServiceModel> selectedList = realmController.getSelectedInCartModel(salonSubServiceModel.get(i).getSubcatid());
                        holder.binding.tvSelected.setText(selectedList.size() + " ausgewählt");
                        holder.binding.llpriceshowhide.setVisibility(View.GONE);
                        return;
                    } else {
                        holder.binding.recymainservice.setVisibility(View.GONE);
                        holder.binding.drop.setRotation(270);
                        holder.binding.tvSelected.setVisibility(View.GONE);
                        holder.binding.llpriceshowhide.setVisibility(View.VISIBLE);
                    }
                }
            }


            holder.binding.tvShowDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mid = mSalonMatchcatsubcatModel.getServicedetailid();
                    String allids = returnServiceIds(mSalonMatchcatsubcatModel.getValue());
                    getratingdetails(mid, allids);
                }
            });



        } else {
            holder.binding.recymainservice.setVisibility(View.GONE);
            holder.binding.drop.setVisibility(View.GONE);
            holder.binding.btnservieitemadd.setVisibility(View.VISIBLE);
//            holder.binding.tvstarting.setVisibility(View.GONE);
            // String minutes = mSalonMatchcatsubcatModel.getValue().get(0).getDuration() + " min";
            //holder.binding.tvitemmin.setText(minutes);
            SalonValueModel valueModel = mSalonMatchcatsubcatModel.getValue().get(0);


/*
            if (subServiceModel != null) {
                if (subServiceModel.getInCart() != null && subServiceModel.getInCart().equals("0")) {
                    holder.binding.btnservieitemadd.setImageResource(R.drawable.ic_add_circle);
                    //holder.binding.btnservieitemadd.setBackground(mContext.getResources().getDrawable(R.drawable.ic_add_circle));
                } else {
                    holder.binding.btnservieitemadd.setImageResource(R.drawable.ic_right);
                    //holder.binding.btnservieitemadd.setBackground(mContext.getResources().getDrawable(R.drawable.ic_right));

                }
            }
*/

            if (Utility.returnDiscountDouble(valueModel.getDiscountPrice()) > 0) {
                // holder.binding.tvitemprice.setText(AppConstants.EURO + valueModel.getDiscountPrice());
                if (valueModel.getPriceStartOption() != null && valueModel.getPriceStartOption().equals("ab"))
                {
                    holder.binding.tvitemprice.setText(mContext.getResources().getString(R.string.from) + " " + Utility.getPriceReplaceDotWithComma(valueModel.getPrice()) + AppConstants.EURO);

                  //  holder.binding.tvitemprice.setText(mContext.getResources().getString(R.string.from) + " " + Utility.getPriceReplaceDotWithComma(valueModel.getDiscountPrice()) + AppConstants.EURO);
                }else {
                    holder.binding.tvitemprice.setText(Utility.getPriceReplaceDotWithComma(valueModel.getPrice()) + AppConstants.EURO);

                   // holder.binding.tvitemprice.setText(Utility.getPriceReplaceDotWithComma(valueModel.getDiscountPrice()) + AppConstants.EURO);
                }
                holder.binding.tvsaveupto.setVisibility(View.VISIBLE);
                //  holder.binding.tvstarting.setVisibility(View.VISIBLE);
                holder.binding.tvsaveupto.setText(mContext.getResources().getString(R.string.save_up_to) + " " + Utility.getPriceReplaceDotWithComma(discount_per(valueModel.getPrice(), valueModel.getDiscountPrice())) + "% ");

            } else {
                if (valueModel.getPriceStartOption() != null && valueModel.getPriceStartOption().equals("ab"))
                {
                    holder.binding.tvitemprice.setText(mContext.getResources().getString(R.string.from) + " " + Utility.getPriceReplaceDotWithComma(valueModel.getPrice()) + AppConstants.EURO);
                }else {
                    holder.binding.tvitemprice.setText(Utility.getPriceReplaceDotWithComma(valueModel.getPrice()) + AppConstants.EURO);
                }

                holder.binding.tvsaveupto.setVisibility(View.GONE);
            }

            holder.binding.tvsaveupto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dialogShowDetails(mSalonAllServiceModel.getServiceDetail(),mSalonAllServiceModel.getCategoryName());
                    String discount= (String) holder.binding.tvsaveupto.getText();
                    String discountPrice=discount.replace("spare bis","");
                  //  String Business=salonDetailModel.getBusinessName();
                    //String mid = mSalonAllServiceModel.getServicedetailid();
                    //String allids = returnServiceIds(mSalonAllServiceModel.getSubServices());
                    dialogShowDiscount(discountPrice);
                   // dialogShowDiscount(discountPrice,Business);

                }
            });

            holder.binding.tttt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSalonMatchcatsubcatModel.getStatus() != 0) {
                        if (controller.detailModel.getOnlineBooking().equals("1")) {
                            if (Session.getUid(mContext).equals("")) {
                                Intent intent = new Intent(mContext, SellectionActivity.class);
                                intent.putExtra("destination", "finish");
                                intent.putExtra("flag", "");
                                mContext.startActivity(intent);
                            } else {
                               // Log.e(TAG, "onClick: " + subServiceModel.getId()+" "+subServiceModel.getCategoryName() );
                               // Log.e(TAG, "onClick: " + valueModel.getId()+" "+valueModel.getCategoryName() );
                               // add_service(valueModel.getId(), valueModel.getPrice(), holder);
                                add_service(subServiceModel.getId(), subServiceModel.getPrice(), holder);
                            }
                        } else {
                            Toast.makeText(mContext, "Dieser Salon steht derzeit nicht für Buchungen zur Verfügung", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });


            if (subServiceModel != null) {
                if (subServiceModel.getInCart() != null && !subServiceModel.getInCart().equals("0")) {
                    //Log.e(TAG, "onBindViewHolder:::::::::::: " + subServiceModel.getInCart() );
                    //Log.e(TAG, "onBindViewHolder:::::::::::: " + subServiceModel.getId() );
                    //Log.e(TAG, "onBindViewHolder:::::::::::: " + mSalonMatchcatsubcatModel.getmId() );
                    if (subServiceModel.getId().equals(mSalonMatchcatsubcatModel.getmId())) {
                        holder.binding.btnservieitemadd.setImageResource(R.drawable.ic_right);
                    }
                   // holder.binding.tvSelected.setVisibility(View.VISIBLE);
                   // List<SalonSubServiceModel> selectedList = realmController.getSelectedInCartModel(valueModel.getSubcatid());
                  //  holder.binding.tvSelected.setText(selectedList.size() + " ausgewählt");
                  //  holder.binding.llpriceshowhide.setVisibility(View.GONE);
                    return;
                } else {
                    holder.binding.btnservieitemadd.setImageResource(R.drawable.ic_add_circle);
                   // holder.binding.tvSelected.setVisibility(View.GONE);
                    //holder.binding.llpriceshowhide.setVisibility(View.VISIBLE);
                }
            }

            if (subServiceModel != null) {
                if (subServiceModel.getInCart() != null && subServiceModel.getInCart().equals("0")) {
                    holder.binding.btnservieitemadd.setImageResource(R.drawable.ic_add_circle);
                    holder.binding.tvSelected.setVisibility(View.GONE);
                    holder.binding.llpriceshowhide.setVisibility(View.VISIBLE);
                } else {
                    holder.binding.btnservieitemadd.setImageResource(R.drawable.ic_right);
                    holder.binding.tvSelected.setVisibility(View.VISIBLE);
                    List<SalonSubServiceModel> selectedList = realmController.getSelectedInCartModel(valueModel.getSubcatid());
                    holder.binding.tvSelected.setText(selectedList.size() + " ausgewählt");
                    holder.binding.llpriceshowhide.setVisibility(View.GONE);
                }
            }

/*
            holder.binding.btnservieitemadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (controller.detailModel.getOnlineBooking().equals("1")){
                        if (Session.getUid(mContext).equals("")) {
                            Intent intent = new Intent(mContext, SellectionActivity.class);
                            intent.putExtra("destination", "finish");
                            intent.putExtra("flag", "");
                            mContext.startActivity(intent);
                        } else {
                            add_service(valueModel.getId(), valueModel.getPrice(), holder);
                        }
                    }else {
                        Toast.makeText(mContext, "Dieser Salon steht derzeit nicht für Buchungen zur Verfügung", Toast.LENGTH_SHORT).show();
                    }
                }
            });
*/

            holder.binding.tvShowDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mid = mSalonMatchcatsubcatModel.getServicedetailid();
                    String allids = returnServiceIds(mSalonMatchcatsubcatModel.getValue());
                    getratingdetails(mid, allids);
                }
            });
        }
    }

    private String returnServiceIds(List<SalonValueModel> sercviceList) {
       // Log.e(TAG, "returnServiceIds: " + sercviceList.toString());
        String ids = "";
        StringBuilder stringBuilder = new StringBuilder();
        if (sercviceList.size() > 0) {
            for (int i = 0; i < sercviceList.size(); i++) {
                stringBuilder.append(sercviceList.get(i).getId() + ",");
            }
            ids = stringBuilder.toString().substring(0, stringBuilder.length() - ",".length());
            return ids;
        }
        return ids;
    }


    @Override
    public int getItemCount() {
        return mSalonMatchcatsubcatModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<SalonMatchcatsubcatModel> getSalonMatchcatsubcatModel() {
        return mSalonMatchcatsubcatModelList;
    }

    public void addChatMassgeModel(SalonMatchcatsubcatModel mSalonMatchcatsubcatModel) {
        try {
            this.mSalonMatchcatsubcatModelList.add(mSalonMatchcatsubcatModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSalonMatchcatsubcatModelList(List<SalonMatchcatsubcatModel> mSalonMatchcatsubcatModelList) {
        this.mSalonMatchcatsubcatModelList = mSalonMatchcatsubcatModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final SalonmatchcatsubcatCellBinding binding;

        public ViewHolder(final View view, final SalonmatchcatsubcatCellBinding binding) {
            super(view);
            this.binding = binding;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @UiThread
        public void bind(final SalonMatchcatsubcatModel mSalonMatchcatsubcatModel) {
            this.binding.setSalonMatchcatsubcatModel(mSalonMatchcatsubcatModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mSalonMatchcatsubcatModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mSalonMatchcatsubcatModelList.size());
    }

    private void add_service(String idd, String price, ViewHolder holder) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(mContext));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(mContext));
        hashMap.put(AppConstants.UID, Session.getUid(mContext));
        hashMap.put(AppConstants.SALON_ID, Session.getSalon_id(mContext));
        hashMap.put(AppConstants.SERVICE_ID, idd);
        hashMap.put(AppConstants.PRICE, price);
        Log.e(TAG, "add_booking:::::::::::::::::::= " + hashMap);
        RetrofitServices.urlServices.add_booking(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String rees = response.body().string().trim();
                    Log.e(TAG, "onResponse: 1 " + rees);
                    JSONObject jsonObject = new JSONObject(rees);
                    if (jsonObject.getString("status").equals("1")) {
                        int delete_sid = jsonObject.getInt("delete_sid");
                        if (delete_sid > 0) {

                            listenDeselect(String.valueOf(delete_sid), holder);
                        }
                        listen(idd, holder);
                        send_broadcast("1");
                        //Toast.makeText(mContext, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    } else {

                       if (jsonObject.getString("response_message").equals("invalid_access_token"))
                        {
                            logOut();
                           // mContext.getSharedPreferences(Session.MyPREFERENCES, Context.MODE_PRIVATE).edit().clear().apply();
                         //   Intent in = new Intent(mContext, Splash.class);
                         //   in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         //   mContext.startActivity(in);
                        }
                        Toast.makeText(mContext, "Die ausgewählten Services müssen von unterschiedlichen Mitarbeitern ausgeführt werden. Du musst daher für diese Behandlung eine separate Buchung erstellen.", Toast.LENGTH_LONG).show();
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
    private void logOut() {

        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setTitle(mContext.getResources().getString(R.string.please_wait));
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(mContext));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.UID, Session.getUid(mContext));

        Log.e("-->", "LOGOUT " + hashMap);

        RetrofitServices.urlServices.logout(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("LOGOUT-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("styletimer_" + Session.getUid(mContext));
                        mContext.getSharedPreferences(Session.MyPREFERENCES, Context.MODE_PRIVATE).edit().clear().apply();
                       /* if (Session.getLoginWith(context).equals("fb")) {
                            LoginManager.getInstance().logOut();
                        } else if (Session.getLoginWith(context).equals("gmail")) {
                            signOut();
                        }*/
                        Intent in = new Intent(mContext, Splash.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(in);

                    } else if (jsonObject.getInt("status") == 0) {
                        Toast.makeText(mContext, "Something went Wrong..", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("-->", "FALURE ");
            }
        });
    }
    private String discount_per(String price, String disprice) {
        Float intprice = Float.valueOf(price);
        Float intdiscprcie = Float.valueOf(disprice);
        Float per = ((intprice - intdiscprcie) * 100 / intprice);
        int result2 = Math.round(per);
        String disc = "0";
        return disc = String.valueOf(result2);

    }

    private void send_broadcast(String count) {
        Intent itent = new Intent(AppConstants.BC_UPDATE_SERVICE);
        itent.putExtra(AppConstants.UPDATE_SERVICE, count);
        LocalBroadcastManager.getInstance(mContext.getApplicationContext()).sendBroadcast(itent);
    }

    private void listen(String cat, ViewHolder holder) {
        SalonSubServiceModel subServiceModel = realmController.getRealm()
                .where(SalonSubServiceModel.class)
                .equalTo("mId", cat)
                .findFirst();

        if (subServiceModel == null) {
            return;
        }
        String incar = subServiceModel.getInCart();

        String value = "";
        if (incar.equals("1")) {
            value = "0";
        } else {
            value = "1";
        }
        realmController.getRealm().beginTransaction();
        subServiceModel.setInCart(value);
        realmController.getRealm().copyToRealmOrUpdate(subServiceModel);
        realmController.getRealm().commitTransaction();
    }

    private void listenDeselect(String cat, ViewHolder holder) {
        SalonSubServiceModel subServiceModel = realmController.getRealm().where(SalonSubServiceModel.class)
                .equalTo("mId", cat).findFirst();

        if (subServiceModel == null) {
            return;
        }
        String incar = subServiceModel.getInCart();

        String value = "";
        if (incar.equals("1")) {
            value = "0";
        } else {
            value = "1";
        }
        realmController.getRealm().beginTransaction();
        subServiceModel.setInCart(value);
        realmController.getRealm().copyToRealmOrUpdate(subServiceModel);
        realmController.getRealm().commitTransaction();
    }


    private void dialogShowDetails(Data data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        DialogShowDetailsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.dialog_show_details, null, false);
        builder.setView(binding.getRoot());


        if (data.getReviews() != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            binding.recysdfd.setLayoutManager(linearLayoutManager);
            binding.recysdfd.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
            ReviewdialogAdapter reviewdialogAdapter = new ReviewdialogAdapter(mContext, data.getReviews());
            binding.recysdfd.setAdapter(reviewdialogAdapter);
            reviewdialogAdapter.notifyDataSetChanged();

            binding.btnshowmore.setOnClickListener(view -> {
                // binding.nesstedddd.fullScroll(View.FOCUS_DOWN);
                scrollToView(binding.nesstedddd, binding.tvssssss);
            });
            binding.tvServiceName.setText(data.getCatName());
            if (data.getServiceDetail() != null && !data.getServiceDetail().equals("")) {
                binding.tvMessage.setVisibility(View.VISIBLE);
                binding.tvitemname.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.tvMessage.setText(Html.fromHtml(data.getServiceDetail(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.tvMessage.setText(Html.fromHtml(data.getServiceDetail()));
                }
                // binding.tvMessage.setText(Html.fromHtml(data.getServiceDetail()));
                // binding.tvMessage.setText("Lorem ipsum, or lipsum as it is sometimes known, is dummy text used in laying out print, graphic or web designs. The passage is attributed to an unknown typesetter in the 15th century who is thought to have scrambled parts of Cicero's De Finibus Bonorum et Malorum for use in a type specimen book mm.");
            } else {
                binding.tvMessage.setVisibility(View.GONE);
                binding.tvitemname.setVisibility(View.GONE);
            }

            if (data.getReviews().size() == 0) {
                binding.btnshowmore.setVisibility(View.GONE);
                binding.tvssssss.setVisibility(View.GONE);
            } else {
                binding.btnshowmore.setVisibility(View.VISIBLE);
                binding.tvssssss.setVisibility(View.VISIBLE);
                binding.btnshowmore.setText("Zeige " + data.getReviews().size() + " Service Bewertungen..");
            }
        }


        if (data.getAvgrateCount() != null) {
            float daafa = Float.parseFloat(data.getAvgrateCount().getAvgrage());
            binding.ratingbar.setRating(daafa);
            binding.tvreviewtotal.setText(data.getAvgrateCount().getTotalcount() + " Bewertungen");

            float myra = Float.parseFloat(data.getAvgrateCount().getAvgrage());
            if (myra < 5) {
                binding.tvRating.setText(Utility.round_off(myra + "").substring(0, 3));
            } else {
                String dfdfd = String.valueOf(myra);
                binding.tvRating.setText(dfdfd.substring(0, 1));
            }
        }

        AlertDialog dialog = builder.create();
        dialog.show();

        binding.btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        binding.ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void scrollToView(final NestedScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y);
    }

    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

    private void dialogShowDiscount(String discount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        DialogDiscountPriceBindingImpl binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.dialog_discount_price, null, false);
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();
        //binding.nesstedddd.fullScroll(View.FOCUS_UP);

        binding.tvServiceName.setText("Dieser Salon bietet für diesen Service Rabattzeiten an. Dies bedeutet, dass du auf diese Behandlung an bestimmten Tagen, oder zu bestimmten Zeiten einen Rabatt von bis zu"+discount+" bekommen kannst. Um zu sehen, zu welchen Zeiten der Rabatt verfügbar ist durchsuche einfach den Kaldender von "+ controller.detailModel.getBusinessName()+".");
        dialog.show();

        binding.btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        binding.ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void getratingdetails(String idd, String mainid) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(mContext));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(mContext));
        hashMap.put(AppConstants.UID, Session.getUid(mContext));
        hashMap.put("id", idd);
        hashMap.put("allid", mainid);

        Log.e(TAG, "getratingdetails: " + hashMap);

        RetrofitServices.urlServi.getsalon_servicedetail(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String rees = response.body().string().trim();
                    RatingREviewDataModel ratingREviewDataModel = gson.fromJson(rees, RatingREviewDataModel.class);
                    if (ratingREviewDataModel.getStatus() == 1) {
                        dialogShowDetails(ratingREviewDataModel.getData());
                    }
                    Log.e(TAG, "onResponse:=======" + rees);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public static void expand(final View v, int position, Context context) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration(250);
        v.startAnimation(a);

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(AppConstants.BROADCAST_KEY);
                intent.putExtra(AppConstants.MAG1, "abc");
                intent.putExtra(AppConstants.MAG2, position + "");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        Log.e(TAG, "collapse: --- " + (int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(250);
        v.startAnimation(a);
    }
}