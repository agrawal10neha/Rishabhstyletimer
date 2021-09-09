package com.si.styletimer.adapters.salonDetail;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si.styletimer.R;
import com.si.styletimer.Session.RealmController;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.SalonmaincategoryCellBinding;
import com.si.styletimer.models.salonDetails.SalonMainCategoryModel;
import com.si.styletimer.models.salonDetails.SalonSubServiceModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.DatabindingImageAdapter;

import java.util.List;

public class SalonMainCategoryAdapter extends RecyclerView.Adapter<SalonMainCategoryAdapter.ViewHolder> {
    private static final String TAG = "SalonMainCategoryAdapte";

    private final Context mContext;
    private List<SalonMainCategoryModel> mSalonMainCategoryModelList;
    private OnItemClickListener onItemClickListener;
    public int mSelectedItem = 0;
    private RealmController realmController;
    public SalonMainCategoryAdapter(Context mContext, List<SalonMainCategoryModel> mSalonMainCategoryModelList) {
        this.mSalonMainCategoryModelList = mSalonMainCategoryModelList;
        this.mContext = mContext;
        realmController = new RealmController(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder( final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final SalonmaincategoryCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.salonmaincategory_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SalonMainCategoryModel mSalonMainCategoryModel = this.mSalonMainCategoryModelList.get(position);
        holder.bind(mSalonMainCategoryModel);

        if (position == mSelectedItem){
            holder.binding.layyout.setBackground(mContext.getDrawable(R.drawable.category_selected));
        }else {
            holder.binding.layyout.setBackground(mContext.getDrawable(R.drawable.bg_salon_detail_menu));
        }

        String someFilepath = RetrofitServices.MENU_IMAGE_ICON+mSalonMainCategoryModel.getId()+"/"+mSalonMainCategoryModel.getIcon();
        String extension = someFilepath.substring(someFilepath.lastIndexOf("."));
        String url;
       /* if (extension.equals(".webp"))
        {
            url = RetrofitServices.MENU_IMAGE_ICON+mSalonMainCategoryModel.getId()+"/"+mSalonMainCategoryModel.getIcon();
        }else {
            url = RetrofitServices.MENU_IMAGE_ICON+mSalonMainCategoryModel.getId()+"/"+mSalonMainCategoryModel.getIcon()+".webp";
        }*/
        url = RetrofitServices.MENU_IMAGE_ICON+mSalonMainCategoryModel.getId()+"/"+mSalonMainCategoryModel.getIcon();

        // String url = RetrofitServices.MENU_IMAGE_ICON+mSalonMainCategoryModel.getId()+"/"+mSalonMainCategoryModel.getIcon();
        Log.e(TAG, "onBindViewHolder: " + url );

        if (mSalonMainCategoryModel.getCategoryName().equals("Alle")){
            holder.binding.ivcat.setImageResource(R.drawable.icon_alltreatments);
        }else {
            DatabindingImageAdapter.loadImage(holder.binding.ivcat,url);
        }

        List<SalonSubServiceModel> selectedList = realmController.getSelectedInCartModelMainCategoryid(mSalonMainCategoryModel.getId());
        //Log.e(TAG, "onBindViewHolder: iff " + selectedList.toString() );
        if (selectedList.size()>0)
        {
            holder.binding.ivRight.setVisibility(View.VISIBLE);
        }else {
            holder.binding.ivRight.setVisibility(View.GONE);
        }

       /* if (mSalonMainCategoryModel.getTotalInCart()>0)
        {
            holder.binding.ivRight.setVisibility(View.VISIBLE);
        }else {
            holder.binding.ivRight.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return mSalonMainCategoryModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<SalonMainCategoryModel> getSalonMainCategoryModel() {
        return mSalonMainCategoryModelList;
    }

    public void addChatMassgeModel(SalonMainCategoryModel mSalonMainCategoryModel) {
        try {
            this.mSalonMainCategoryModelList.add(mSalonMainCategoryModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSalonMainCategoryModelList(List<SalonMainCategoryModel> mSalonMainCategoryModelList) {
        this.mSalonMainCategoryModelList = mSalonMainCategoryModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final SalonmaincategoryCellBinding binding;

        public ViewHolder(final View view, final SalonmaincategoryCellBinding binding) {
            super(view);
            this.binding = binding;
           // view.setOnClickListener(this);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyDataSetChanged();
                    Log.e(TAG, "onClick: here "+mSelectedItem);
                   // notifyItemRangeChanged(0, mSalonMainCategoryModelList.size());
                    Intent itent = new Intent(AppConstants.BC_UPDATE_SERVICE_TWO);
                    itent.putExtra(AppConstants.FLAG, mSelectedItem+"");
                    LocalBroadcastManager.getInstance(mContext.getApplicationContext()).sendBroadcast(itent);
                }
            };
            itemView.setOnClickListener(clickListener);
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
        public void bind(final SalonMainCategoryModel mSalonMainCategoryModel) {
            this.binding.setSalonMainCategoryModel(mSalonMainCategoryModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mSalonMainCategoryModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mSalonMainCategoryModelList.size());
    }
}