package com.si.styletimer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.si.styletimer.R;
import com.si.styletimer.activity.CategoryListingActivity;
import com.si.styletimer.models.dashboard.SubCategory;
import com.si.styletimer.models.mybooking_list_model.confirm_booking_model.AllCategory;

import java.util.ArrayList;
import java.util.List;

public class ConfirmBookingServiceAdapter extends RecyclerView.Adapter<ConfirmBookingServiceAdapter.ViewHolder> {
    private static final String TAG = "ConfirmBookingServiceAdapter";
    private Context context;
    private List<AllCategory> mSubCategoryList= new ArrayList<>();;
    RecyclerView rv1;
    private String clicked;
    private String parenId = "";
    public ConfirmBookingServiceAdapter(Context context, List<AllCategory> mSubCategoryList, RecyclerView rv, String pid){
        this.context = context;
        this.mSubCategoryList= mSubCategoryList;
        this.rv1= rv;
        this.parenId= pid;

    }

    @NonNull
    @Override
    public ConfirmBookingServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.confirm_booking_service_cell, viewGroup, false);
        return new ConfirmBookingServiceAdapter.ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ConfirmBookingServiceAdapter.ViewHolder viewHolder, final int i) {

        for (int i1=0; i1<mSubCategoryList.size(); i1++) {

            if(mSubCategoryList.get(i).getmServiceName().equals(" ")||mSubCategoryList.get(i).getmServiceName().equals(""))
            {
                viewHolder.tvName.setText(mSubCategoryList.get(i).getmCategoryName());
            }
            else{
                viewHolder.tvName.setText(mSubCategoryList.get(i).getmCategoryName()+" - "+mSubCategoryList.get(i).getmServiceName());
            }
        }

    }
  /*  @SuppressLint("LongLogTag")
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
    }*/

    @Override
    public int getItemCount() {
        return mSubCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName=itemView.findViewById(R.id.tvName);

        }
    }
}