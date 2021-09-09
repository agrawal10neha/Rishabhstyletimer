package com.si.styletimer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.activity.BookingDetailActivity;
import com.si.styletimer.activity.SelectDateActivity;
import com.si.styletimer.activity.WriteReviewActivity;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.CompletedBookingCellBinding;
import com.si.styletimer.models.SelectTimeModel;
import com.si.styletimer.models.bookind_detail_service.ServicesModel;
import com.si.styletimer.models.categoryListing.Sercvice;
import com.si.styletimer.models.mybooking_list_model.completed_booking_model.CompletedModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedBookindAdapter extends RecyclerView.Adapter<CompletedBookindAdapter.ViewHolder> {


    private final Context mContext;
    private List<CompletedModel> mCompletedModelList;
    private OnItemClickListener onItemClickListener;
    private static final String TAG = "CompletedBookindAdapter";

    public CompletedBookindAdapter(Context mContext, List<CompletedModel> mCompletedModelList) {
        this.mCompletedModelList = mCompletedModelList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final CompletedBookingCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.completed_booking_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        /*final CompletedModel mCompletedModel = this.mCompletedModelList.get(position);
        holder.bind(mCompletedModel);*/

        if(mCompletedModelList.get(position).getStatus().equals("cancelled") || mCompletedModelList.get(position).getStatus().equals("confirmed")){
            //holder.binding.llBtCell.setVisibility(View.GONE);
            holder.binding.llBtCell.setVisibility(View.VISIBLE);
        }else {
            holder.binding.llBtCell.setVisibility(View.VISIBLE);
        }

        String a[]=mCompletedModelList.get(position).getBookingTime().split(" ");
        String b[]=a[1].split(":");
        holder.binding.tvDate.setText(Utility.app_format(a[0])+" / "+b[0]+":"+b[1]+" Uhr");




        holder.binding.rv.setLayoutManager(new LinearLayoutManager(mContext));
        holder.binding.rv.setAdapter(new ConfirmBookingServiceAdapter(mContext, mCompletedModelList.get(position).getAllCategory(),holder.binding.rv,mCompletedModelList.get(position).getId()));


       /* if(mCompletedModelList.get(position).getServiceName().equals("")){
            holder.binding.tvName.setText(mCompletedModelList.get(position).getCatName());
        }else {
            holder.binding.tvName.setText(mCompletedModelList.get(position).getCatName()+" - "+mCompletedModelList.get(position).getServiceName());
        }*/

        holder.binding.tvSalonName.setText(mCompletedModelList.get(position).getBusinessName());
        holder.binding.tvAddress.setText(mCompletedModelList.get(position).getAddress()+"\n"
        +mCompletedModelList.get(position).getZip()+" "+ mCompletedModelList.get(position).getCity());

        Log.e(TAG, "onBindViewHolder: "+mCompletedModelList.get(position).getBusinessName()+" "+ mCompletedModelList.get(position).getStatus());


        if(mCompletedModelList.get(position).getStatus().equals("confirmed")){
            holder.binding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.new_orange));
            holder.binding.tvStatus.setText(mContext.getResources().getString(R.string.confirmed));
            holder.binding.tvReView.setVisibility(View.GONE);
            if (mCompletedModelList.get(position).getPriceStartOption() != null && mCompletedModelList.get(position).getPriceStartOption().equals("ab")) {
                holder.binding.tvPrice.setText("ab "+ Utility.getPriceReplaceDotWithComma(mCompletedModelList.get(position).getTotalPrice())+"€");
            }else {
                holder.binding.tvPrice.setText(Utility.getPriceReplaceDotWithComma(mCompletedModelList.get(position).getTotalPrice()) + "€");
            }
        }else if(mCompletedModelList.get(position).getStatus().equals("cancelled")){
            holder.binding.tvStatus.setText(mContext.getResources().getString(R.string.cancelled));
            holder.binding.tvReView.setVisibility(View.GONE);
            holder.binding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.new_red));
            if (mCompletedModelList.get(position).getPriceStartOption() != null && mCompletedModelList.get(position).getPriceStartOption().equals("ab")) {
                holder.binding.tvPrice.setText("ab "+ Utility.getPriceReplaceDotWithComma(mCompletedModelList.get(position).getTotalPrice())+"€");
            }else {
                holder.binding.tvPrice.setText(Utility.getPriceReplaceDotWithComma(mCompletedModelList.get(position).getTotalPrice()) + "€");
            }
        }else if(mCompletedModelList.get(position).getStatus().equals("completed")){
            holder.binding.tvStatus.setText(mContext.getResources().getString(R.string.completed));
            holder.binding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.new_gren));
            if (mCompletedModelList.get(position).getPriceStartOption() != null && mCompletedModelList.get(position).getPriceStartOption().equals("ab")) {
                holder.binding.tvPrice.setText( Utility.getPriceReplaceDotWithComma(mCompletedModelList.get(position).getTotalPrice())+"€");
            }else {
                holder.binding.tvPrice.setText(Utility.getPriceReplaceDotWithComma(mCompletedModelList.get(position).getTotalPrice()) + "€");
            }
            if(mCompletedModelList.get(position).getIsReview().equals("1")){
                holder.binding.tvReView.setVisibility(View.GONE);
            }else if (mCompletedModelList.get(position).getIsReview().equals("0")){
                holder.binding.tvReView.setVisibility(View.VISIBLE);
            }
        }else if(mCompletedModelList.get(position).getStatus().equals("no show")){
            holder.binding.tvStatus.setText(mContext.getResources().getString(R.string.no_show));
            holder.binding.tvReView.setVisibility(View.GONE);
        }else {
            holder.binding.tvStatus.setText(mCompletedModelList.get(position).getStatus().toUpperCase());
        }

        holder.binding.lldetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BookingDetailActivity.class);
                intent.putExtra("Fragment","CompletedFragment");
                intent.putExtra("id",mCompletedModelList.get(position).getId());
                intent.putExtra("from","main");
                intent.putExtra("sId",mCompletedModelList.get(position).getMerchantId());
                intent.putExtra("selected","1");
                intent.putExtra(AppConstants.RES_STATUS,mCompletedModelList.get(position).getResheduleStatus());

                //intent.putExtra("salonId",mCompletedModelList.get(position).getMerchantId());
                mContext.startActivity(intent);
               // ((Activity)mContext).finish();
            }
        });

        String date[]=mCompletedModelList.get(position).getEndDate().split(" ");

        if(Utility.comparedates(Utility.getCurrentDateYY(),date[0]).equals("before")){
           if (mCompletedModelList.get(position).getmOnlineBooking().equals("0")) {
                holder.binding.tvRebook.setVisibility(View.GONE);
            } else {
                holder.binding.tvRebook.setVisibility(View.VISIBLE);
            }


        }else if(Utility.comparedates(Utility.getCurrentDateYY(),date[0]).equals("after")) {
            if (mCompletedModelList.get(position).getmOnlineBooking().equals("0")) {
                holder.binding.tvRebook.setVisibility(View.GONE);
            } else {
                holder.binding.tvRebook.setVisibility(View.VISIBLE);
            }


        }else if(Utility.comparedates(Utility.getCurrentDateYY(),date[0]).equals("equal")) {
            if (mCompletedModelList.get(position).getmOnlineBooking().equals("0")) {
                holder.binding.tvRebook.setVisibility(View.GONE);
            } else {
                holder.binding.tvRebook.setVisibility(View.VISIBLE);
            }

        }


        holder.binding.btnReschedule.setOnClickListener(v -> {
            Session.setSalon_id(mContext,mCompletedModelList.get(position).getMerchantId());
            Intent intent = new Intent(mContext, SelectDateActivity.class);
            intent.putExtra(AppConstants.SALON_ID,mCompletedModelList.get(position).getMerchantId());
            intent.putExtra(AppConstants.FLAG,mCompletedModelList.get(position).getEmployeId());
            intent.putExtra(AppConstants.DATE,Utility.getCurrentDateYY());
            intent.putExtra(AppConstants.FLAGTWO,AppConstants.RESCHEDULE);
            intent.putExtra(AppConstants.BOOK_ID,mCompletedModelList.get(position).getId());
            mContext.startActivity(intent);
        });

        if (Utility.timeleft(mCompletedModelList.get(position).getBookingTime()) > 86400000){

            String status = mCompletedModelList.get(position).getStatus();
            String reschedule  = mCompletedModelList.get(position).getResheduleStatus();
            if (status.equals("confirmed")){
                if (reschedule.equals("0")){
                    holder.binding.tlReschedule.setVisibility(View.VISIBLE);
                }else {
                    holder.binding.tlReschedule.setVisibility(View.GONE);
                }
            }else if (status.equals("completed")){
                holder.binding.tlReschedule.setVisibility(View.GONE);
            }

        }else {
            holder.binding.tlReschedule.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return mCompletedModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<CompletedModel> getCompletedModel() {
        return mCompletedModelList;
    }

    public void addChatMassgeModel(CompletedModel mCompletedModel) {
        try {
            this.mCompletedModelList.add(mCompletedModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCompletedModelList(List<CompletedModel> mCompletedModelList) {
        this.mCompletedModelList = mCompletedModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CompletedBookingCellBinding binding;

        public ViewHolder(final View view, final CompletedBookingCellBinding binding) {
            super(view);
            this.binding = binding;
            view.setOnClickListener(this);
            binding.tvRebook.setOnClickListener(this);
            binding.tvReView.setOnClickListener(this);
            binding.btnCancel.setOnClickListener(this);
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
        public void bind(final CompletedModel mCompletedModel) {
            this.binding.setCompletedModel(mCompletedModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mCompletedModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCompletedModelList.size());
    }

}