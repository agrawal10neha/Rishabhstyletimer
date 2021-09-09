package com.si.styletimer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.si.styletimer.models.mybooking_list_model.confirm_booking_model.AllCategory;

import java.util.List;

public class CompletedBookingServiceAdapter extends RecyclerView.Adapter<CompletedBookingServiceAdapter.ViewHolder> {
    private static final String TAG = "CompletedBookingServiceAdapter";
    private Context context;
    private List<AllCategory> mSubCategoryList;
    RecyclerView rv1;
    private String clicked;
    private String parenId = "";
    public CompletedBookingServiceAdapter(Context context, List<AllCategory> mSubCategoryList, RecyclerView rv, String pid){
        this.context = context;
        this.mSubCategoryList= mSubCategoryList;
        this.rv1= rv;
        this.parenId= pid;

    }

    @NonNull
    @Override
    public CompletedBookingServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.completed_booking_service_cell, viewGroup, false);
        return new CompletedBookingServiceAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompletedBookingServiceAdapter.ViewHolder viewHolder, final int i) {

        for (int i1=0; i1<mSubCategoryList.size(); i1++) {

            viewHolder.tvName.setText(mSubCategoryList.get(i).getmCategoryName()+" - "+mSubCategoryList.get(i).getmServiceName());
        }

    }
    @SuppressLint("LongLogTag")
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