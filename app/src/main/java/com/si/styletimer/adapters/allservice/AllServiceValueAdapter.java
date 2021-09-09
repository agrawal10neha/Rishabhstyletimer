package com.si.styletimer.adapters.allservice;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.activity.SellectionActivity;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.AllservicevalueCellBinding;
import com.si.styletimer.models.allservice.AllServiceValueModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllServiceValueAdapter extends RecyclerView.Adapter<AllServiceValueAdapter.ViewHolder> {
    private static final String TAG = "AllServiceValueAdapter";
    private final Context mContext;
    private List<AllServiceValueModel> mAllServiceValueModelList;
    private OnItemClickListener onItemClickListener;

    public AllServiceValueAdapter(Context mContext, List<AllServiceValueModel> mAllServiceValueModelList) {
        this.mAllServiceValueModelList = mAllServiceValueModelList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final AllservicevalueCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.allservicevalue_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AllServiceValueModel mAllServiceValueModel = this.mAllServiceValueModelList.get(position);
        holder.bind(mAllServiceValueModel);

        holder.binding.btnservieitemadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Session.getUid(mContext).equals("")){
                    Intent intent = new Intent(mContext, SellectionActivity.class);
                    intent.putExtra(AppConstants.FLAG,"");
                    mContext.startActivity(intent);
                }else {
                    add_service(mAllServiceValueModel.getId(),mAllServiceValueModel.getPrice(),holder);

                }

            }
        });

        if (mAllServiceValueModel.getInCart().equals("0")) {
            holder.binding.btnservieitemadd.setImageResource(R.drawable.ic_add_circle);
        } else {
            holder.binding.btnservieitemadd.setImageResource(R.drawable.ic_right);

        }

        int disc = Integer.parseInt(mAllServiceValueModel.getDiscountPrice());
        if (disc > 0){
            holder.binding.tvitemprice.setText(AppConstants.EURO+mAllServiceValueModel.getPrice());
          //  holder.binding.tvitemprice.setText(AppConstants.EURO+mAllServiceValueModel.getDiscountPrice());

            holder.binding.tvsaveupto.setVisibility(View.VISIBLE);
            holder.binding.tvstarting.setVisibility(View.VISIBLE);
            holder.binding.tvsaveupto.setText("Du sparst "+discount_per(mAllServiceValueModel.getPrice(),mAllServiceValueModel.getDiscountPrice())+ "%");
        }else {
            holder.binding.tvitemprice.setText(AppConstants.EURO+mAllServiceValueModel.getPrice());
            holder.binding.tvsaveupto.setVisibility(View.GONE);
        }
        
    }

    @Override
    public int getItemCount() {
        return mAllServiceValueModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<AllServiceValueModel> getAllServiceValueModel() {
        return mAllServiceValueModelList;
    }

    public void addChatMassgeModel(AllServiceValueModel mAllServiceValueModel) {
        try {
            this.mAllServiceValueModelList.add(mAllServiceValueModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAllServiceValueModelList(List<AllServiceValueModel> mAllServiceValueModelList) {
        this.mAllServiceValueModelList = mAllServiceValueModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final AllservicevalueCellBinding binding;

        public ViewHolder(final View view, final AllservicevalueCellBinding binding) {
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
        public void bind(final AllServiceValueModel mAllServiceValueModel) {
            this.binding.setAllServiceValueModel(mAllServiceValueModel);
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
                    JSONObject jsonObject  =new JSONObject(rees);
                    if (jsonObject.getString("status").equals("1")){
                        //Toast.makeText(mContext, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                        JSONObject data = jsonObject.getJSONObject("data");
                        String totservie = data.getString("tot_service");
                        String delete_sid = jsonObject.getString("delete_sid");
                        Log.e(TAG, "onResponse: +++2 " );
                        send_broadcast(totservie);
                        String resmsg = jsonObject.getString("response_message");
                        if (resmsg.equals("Service removed from cart")){
                            holder.binding.btnservieitemadd.setImageResource(R.drawable.ic_add_circle);
                        }else {
                            holder.binding.btnservieitemadd.setImageResource(R.drawable.ic_right);
                        }
                    }else {
                        Toast.makeText(mContext, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
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

    private void send_broadcast(String count){
        Intent itent = new Intent(AppConstants.BC_UPDATE_SERVICE);
        itent.putExtra(AppConstants.UPDATE_SERVICE, count);
        LocalBroadcastManager.getInstance(mContext.getApplicationContext()).sendBroadcast(itent);
    }


//    private String discount_per (String price,String disprice){
//        int intprice = Integer.parseInt(price);
//        int intdiscprcie = Integer.parseInt(disprice);
//        String disc = "0";
//        return disc = String.valueOf(((intprice-intdiscprcie)*100/intprice));
//
//    }
    private String discount_per (String price,String disprice){
        Log.e(TAG, "discount_per() called with: price = [" + price + "], disprice = [" + disprice + "]");
        Float intprice = Float.valueOf(price);
        Float intdiscprcie = Float.valueOf(disprice);
        Float per = ((intprice-intdiscprcie)*100/intprice);
        int result2 =  Math.round(per);
        String disc = "0";
        return disc = String.valueOf(result2);

    }


}