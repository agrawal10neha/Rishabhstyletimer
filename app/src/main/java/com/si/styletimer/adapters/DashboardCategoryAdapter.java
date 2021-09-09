package com.si.styletimer.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.si.styletimer.R;
import com.si.styletimer.activity.CategoryListingActivity;
import com.si.styletimer.activity.LoginActivity;
import com.si.styletimer.activity.SellectionActivity;
import com.si.styletimer.models.dashboard.DashboardListModel;
import com.si.styletimer.models.dashboard.SubCategory;
import com.si.styletimer.utill.Animatoo;

import java.util.ArrayList;
import java.util.List;



public class DashboardCategoryAdapter extends RecyclerView.Adapter<DashboardCategoryAdapter.ViewHolder> {
    private static final String TAG = "DashboardCategoryAdapte";
    private Context context;
    private List<SubCategory> mSubCategoryList;
    RecyclerView rv1;
    private String clicked;
    private String parenId = "";
    public DashboardCategoryAdapter(Context context, List<SubCategory> mSubCategoryList,RecyclerView rv, String clicked,String pid){
        this.context = context;
        this.mSubCategoryList= mSubCategoryList;
        this.clicked= clicked;
        this.rv1= rv;
        this.parenId= pid;

    }

    @NonNull
    @Override
    public DashboardCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_category_cell, viewGroup, false);
        return new DashboardCategoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DashboardCategoryAdapter.ViewHolder viewHolder, final int i) {


        viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.tvName.getText().toString().equals("All treatments") || viewHolder.tvName.getText().toString().equals("Alle Behandlungen")){
                  //  Bungee.slideLeft(context);
                    //collapse(rv1);
                    clicked = "";
                    if (mSubCategoryList.size() == 0) {
                        Intent intent = new Intent(context, CategoryListingActivity.class);
                        intent.putExtra("id",parenId);
                        intent.putExtra("tag", "main");
                        intent.putExtra("category_id", parenId);
                        intent.putExtra("cat_name", viewHolder.tvName.getText().toString());
                        context.startActivity(intent);
                    }else {
                        Intent intent = new Intent(context, CategoryListingActivity.class);
                        intent.putExtra("id", mSubCategoryList.get(i - 1).getParentId());
                        intent.putExtra("tag", "main");
                        intent.putExtra("category_id", mSubCategoryList.get(i - 1).getParentId());
                        intent.putExtra("cat_name", viewHolder.tvName.getText().toString());
                        context.startActivity(intent);
                    }
                   // Animatoo.animateSlideLeft(context);
/*
                    context.startActivity(new Intent(context, CategoryListingActivity.class).
                            putExtra("id",mSubCategoryList.get(i-1).getParentId()).
                            putExtra("tag","main").
                            putExtra("category_id",mSubCategoryList.get(i-1).getParentId()).
                            putExtra("cat_name",viewHolder.tvName.getText().toString()));
*/

                  //  Bungee.slideLeft(context);

                }else {
                   // collapse(rv1);
                  //  Bungee.slideLeft(context);
                    clicked = "";
                    Intent intent = new Intent(context,CategoryListingActivity.class);
                    intent.putExtra("id",mSubCategoryList.get(i).getId());
                    intent.putExtra("tag","sub");
                    intent.putExtra("category_id",mSubCategoryList.get(i).getParentId());
                    intent.putExtra("cat_name",viewHolder.tvName.getText().toString());
                    context.startActivity(intent);
                  //  Animatoo.animateSlideLeft(context);
                   /* context.startActivity(new Intent(context, CategoryListingActivity.class).
                            putExtra("id",mSubCategoryList.get(i).getId()).
                            putExtra("tag","sub").
                            putExtra("category_id",mSubCategoryList.get(i).getParentId()).
                            putExtra("cat_name",viewHolder.tvName.getText().toString()));
                    Animatoo.animateSlideLeft(context);*/
                 //   Bungee.slideLeft(context);

                }

            }
        });

        if(i==mSubCategoryList.size()){
           // viewHolder.tvName.setText("All treatments");
            viewHolder.tvName.setText(context.getResources().getString(R.string.all_treatments));
            viewHolder.tvName.setTextColor(context.getResources().getColor(R.color.blue));
        }else {
            viewHolder.tvName.setText(mSubCategoryList.get(i).getCategoryName());
        }

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

    @Override
    public int getItemCount() {
        return mSubCategoryList.size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName=itemView.findViewById(R.id.tvName);

        }
    }
}
