package com.si.styletimer.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import androidx.annotation.UiThread;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.si.styletimer.R;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.DashboardCellBinding;
import com.si.styletimer.models.dashboard.DashboardListModel;
import com.si.styletimer.models.dashboard.DashboardModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.DatabindingImageAdapter;
import com.si.styletimer.utill.Utility;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {


    private final Context mContext;
    private List<DashboardListModel> mDashboardModelList;
    private OnItemClickListener onItemClickListener;
    private String clicked;
    private Animation animationUp;
    private Animation animationDown;
    private static final String TAG = "DashboardAdapter";
    private LinearLayoutManager m;


    public DashboardAdapter(Context mContext, List<DashboardListModel> mDashboardModelList, String clicked, LinearLayoutManager m) {
        this.mDashboardModelList = mDashboardModelList;
        this.mContext = mContext;
        this.clicked = clicked;
        this.m = m;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final DashboardCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.dashboard_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String someFilepath = RetrofitServices.MENU_IMAGE + mDashboardModelList.get(position).getId() + "/" + mDashboardModelList.get(position).getImage();
        String extension = someFilepath.substring(someFilepath.lastIndexOf("."));
        String url;
       /* if (extension.equals(".webp"))
        {
            url = RetrofitServices.MENU_IMAGE + mDashboardModelList.get(position).getId() + "/" + mDashboardModelList.get(position).getImage();
        }else {
            url = RetrofitServices.MENU_IMAGE + mDashboardModelList.get(position).getId() + "/" + mDashboardModelList.get(position).getImage()+".webp";
        }*/
        url = RetrofitServices.MENU_IMAGE + mDashboardModelList.get(position).getId() + "/" + mDashboardModelList.get(position).getImage();

        //String url = RetrofitServices.MENU_IMAGE + mDashboardModelList.get(position).getId() + "/" + mDashboardModelList.get(position).getImage();
       // Log.e(TAG, "onBindViewHolder: imagesss ================ " + url );
        DatabindingImageAdapter.loadImage(holder.binding.iv, url);
        holder.binding.tvname.setText(mDashboardModelList.get(position).getCategoryName());

        holder.binding.rv.setLayoutManager(new LinearLayoutManager(mContext));
        holder.binding.rv.setAdapter(new DashboardCategoryAdapter(mContext, mDashboardModelList.get(position).getSubCategory(),holder.binding.rv,clicked,mDashboardModelList.get(position).getId()));

        if (clicked.equals(String.valueOf(position))) {
            expand(holder.binding.rv, position, mContext);
        } else {
            collapse(holder.binding.rv);
        }

        holder.binding.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clicked.equals(String.valueOf(position))) {
                    clicked = "";
                    notifyDataSetChanged();

                } else {
                    clicked = String.valueOf(position);
                    notifyDataSetChanged();
                }
                if (!Utility.checkGPSStatus(mContext)) {
                    Intent intent = new Intent(AppConstants.BROADCAST_KEY);
                    intent.putExtra(AppConstants.MAG1, "gps");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }

            }
        });

        onClick(holder.binding);


    }

    private void onClick(DashboardCellBinding binding) {

    }

    @Override
    public int getItemViewType(int position) {
        return getItemCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDashboardModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<DashboardListModel> getDashboardModel() {
        return mDashboardModelList;
    }

    public void addChatMassgeModel(DashboardListModel mDashboardModel) {
        try {
            this.mDashboardModelList.add(mDashboardModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDashboardModelList(List<DashboardListModel> mDashboardModelList) {
        this.mDashboardModelList = mDashboardModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final DashboardCellBinding binding;

        public ViewHolder(final View view, final DashboardCellBinding binding) {
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
        public void bind(final DashboardModel mDashboardModel) {
            this.binding.setDashboardModel(mDashboardModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mDashboardModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDashboardModelList.size());
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