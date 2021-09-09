package com.si.styletimer.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.si.styletimer.R;
import com.si.styletimer.Session.RealmController;
import com.si.styletimer.Session.Session;
import com.si.styletimer.activity.SellectionActivity;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.MainserviceCellBinding;
import com.si.styletimer.models.salonDetails.SalonSubServiceModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainServiceAdapter extends RecyclerView.Adapter<MainServiceAdapter.ViewHolder> {
    private static final String TAG = "MainServiceAdapter";
    private final Context mContext;
  //  private List<SalonSubServiceModel> mSalonSubServiceModelList;
    private OnItemClickListener onItemClickListener;
    private RealmController realmController;
    private List<SalonSubServiceModel> mSalonSubServiceModelList;


    public MainServiceAdapter(Context mContext, List<SalonSubServiceModel> mSalonSubServiceModelList) {
        this.mSalonSubServiceModelList = mSalonSubServiceModelList;
        this.mContext = mContext;
        realmController = new RealmController(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final MainserviceCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.mainservice_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
      //  final SalonValueModel mValueModel = this.mSalonSubServiceModelList.get(position);

        final SalonSubServiceModel mValueModel = this.mSalonSubServiceModelList.get(position);
        holder.bind(mValueModel);

        Log.e(TAG, "onBindViewHolder:;;;;;;;;;;;; "+ mValueModel.getId() );
        if(mValueModel.getName().equals("")){
            holder.binding.tvItemName.setText(mValueModel.getCategoryName());
        }else {
            holder.binding.tvItemName.setText(mValueModel.getName());
        }


        holder.binding.tttt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Session.getUid(mContext).equals("")){
                    Intent intent = new Intent(mContext, SellectionActivity.class);
                    intent.putExtra("destination","finish");
                    intent.putExtra("flag","");
                    mContext.startActivity(intent);
                }else {
                    add_service(mValueModel.getId(),mValueModel.getPrice(),holder);

                }
            }
        });

        //Log.e(TAG, "onBindViewHolder:getInCart --------- >>>  "+mValueModel.getInCart() );

        if (mValueModel.getInCart().equals("0")){
            holder.binding.btnservieitemadd.setImageResource(R.drawable.ic_add_circle);
        }else {
            holder.binding.btnservieitemadd.setImageResource(R.drawable.ic_right);
        }

        if (Utility.returnDiscountDouble(mValueModel.getDiscountPrice()) > 0){
            if (mValueModel.getPriceStartOption() != null && mValueModel.getPriceStartOption().equals("ab")) {
              //  holder.binding.tvitemprice.setText(mContext.getResources().getString(R.string.from) + " " + Utility.getPriceReplaceDotWithComma(mValueModel.getDiscountPrice()) + AppConstants.EURO);

                holder.binding.tvitemprice.setText(mContext.getResources().getString(R.string.from) + " " + Utility.getPriceReplaceDotWithComma(mValueModel.getPrice()) + AppConstants.EURO);
            }else {
                holder.binding.tvitemprice.setText(Utility.getPriceReplaceDotWithComma(mValueModel.getPrice()) + AppConstants.EURO);

               // holder.binding.tvitemprice.setText(Utility.getPriceReplaceDotWithComma(mValueModel.getDiscountPrice()) + AppConstants.EURO);

            }
           // holder.binding.tvitemprice.setText(AppConstants.EURO+mValueModel.getDiscountPrice());
          //  holder.binding.tvitemprice.setText(""+AppConstants.EURO+mValueModel.getDiscountPrice());
            holder.binding.tvsaveupto.setVisibility(View.GONE);
           // holder.binding.tvstarting.setVisibility(View.VISIBLE);
            holder.binding.tvsaveupto.setText(Utility.getPriceReplaceDotWithComma(discount_per(mValueModel.getPrice(),mValueModel.getDiscountPrice()))+ "%");
           // holder.binding.tvsaveupto.setText(mContext.getResources().getString(R.string.save_up_to)+" "+discount_per(mValueModel.getPrice(),mValueModel.getDiscountPrice())+ "%");
           // holder.binding.tvsaveupto.setText(discount_per(mValueModel.getPrice(),mValueModel.getDiscountPrice())+ "%");
        }else {
            if (mValueModel.getPriceStartOption() != null && mValueModel.getPriceStartOption().equals("ab")) {

                holder.binding.tvitemprice.setText(mContext.getResources().getString(R.string.from) + " " + Utility.getPriceReplaceDotWithComma(mValueModel.getPrice())+AppConstants.EURO);
            }else {
                holder.binding.tvitemprice.setText(Utility.getPriceReplaceDotWithComma(mValueModel.getPrice())+AppConstants.EURO);

            }
            holder.binding.tvsaveupto.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mSalonSubServiceModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<SalonSubServiceModel> getValueModel() {
        return mSalonSubServiceModelList;
    }

    public void addChatMassgeModel(SalonSubServiceModel mValueModel) {
        try {
            this.mSalonSubServiceModelList.add(mValueModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setValueModelList(List<SalonSubServiceModel> mSalonSubServiceModelList) {
        this.mSalonSubServiceModelList = mSalonSubServiceModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final MainserviceCellBinding binding;

        public ViewHolder(final View view, final MainserviceCellBinding binding) {
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
        public void bind(final SalonSubServiceModel mValueModel) {
            this.binding.setValueModel(mValueModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    private void add_service(String idd, String price,ViewHolder holder) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(mContext));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(mContext));
        hashMap.put(AppConstants.UID, Session.getUid(mContext));
        hashMap.put(AppConstants.SALON_ID,Session.getSalon_id(mContext));
        hashMap.put(AppConstants.SERVICE_ID,idd);
        hashMap.put(AppConstants.PRICE,price);

        Log.e(TAG, "get_salon_details: "+hashMap );

        RetrofitServices.urlServices.add_booking(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                boolean token = false;
                 int tto = 0;
                try {

                    String rees = response.body().string().trim();
                    Log.e(TAG, "onResponse: 4 "+rees );
                    JSONObject jsonObject  =new JSONObject(rees);
                    if (jsonObject.getString("status").equals("1")){
                        int delete_sid = jsonObject.getInt("delete_sid");
                        if (delete_sid>0)
                        {

                            listenDeselect(String.valueOf(delete_sid),holder);
                        }
                        listen(idd,holder);
                        send_broadcast("1");
                        //Toast.makeText(mContext, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(mContext, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    }
                   // notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void send_broadcast(String count){
        Intent itent = new Intent(AppConstants.BC_UPDATE_SERVICE);
        itent.putExtra(AppConstants.UPDATE_SERVICE, count);
        LocalBroadcastManager.getInstance(mContext.getApplicationContext()).sendBroadcast(itent);
    }

    private String discount_per (String price,String disprice){
        Float intprice = Float.valueOf(price);
        Float intdiscprcie = Float.valueOf(disprice);
        Float per = ((intprice-intdiscprcie)*100/intprice);
        int result2 =  Math.round(per);
        String disc = "0";
        return disc = String.valueOf(result2);

    }


    private void listen(String cat, ViewHolder holder){
        Log.e(TAG, "listen: "+cat );
        SalonSubServiceModel subServiceModel = realmController.getRealm().where(SalonSubServiceModel.class)
                .equalTo("mId",cat).findFirst();

        if (subServiceModel == null){
            return;
        }
        String incar = subServiceModel.getInCart();

        String value = "";
        if (incar.equals("1")){
            value = "0";
        }else {
            value = "1";
        }

        String open = subServiceModel.getIsOpen();
        String op = "";
        if (open.equals("0")){
            op = "1";
        }else {
            op = "0";
        }

        realmController.getRealm().beginTransaction();
        subServiceModel.setInCart(value);
       // subServiceModel.setIsOpen(op);
        realmController.getRealm().copyToRealmOrUpdate(subServiceModel);
        realmController.getRealm().commitTransaction();

    }
    private void listenDeselect(String cat, ViewHolder holder){
        Log.e(TAG, "listenDeselect: "+cat );
        SalonSubServiceModel subServiceModel = realmController.getRealm().where(SalonSubServiceModel.class)
                .equalTo("mId",cat).findFirst();

        if (subServiceModel == null){
            return;
        }
        String incar = subServiceModel.getInCart();

        String value = "";
        if (incar.equals("1")){
            value = "0";
        }else {
            value = "1";
        }
        realmController.getRealm().beginTransaction();
        subServiceModel.setInCart(value);
        realmController.getRealm().copyToRealmOrUpdate(subServiceModel);
        realmController.getRealm().commitTransaction();
    }

    public void clearAdapter() {
        int size = mSalonSubServiceModelList.size();
        mSalonSubServiceModelList.clear();
        notifyItemRangeRemoved(0, size);
    }

}