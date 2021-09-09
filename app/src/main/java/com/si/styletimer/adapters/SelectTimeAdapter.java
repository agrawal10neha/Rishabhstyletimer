package com.si.styletimer.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Paint;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si.styletimer.R;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.SelecttimeCellBinding;
import com.si.styletimer.models.SelectTimeModel;
import com.si.styletimer.utill.Utility;

import java.util.List;

public class SelectTimeAdapter extends RecyclerView.Adapter<SelectTimeAdapter.ViewHolder> {
    private final Context mContext;
    private static final String TAG = "SelectTimeAdapter";
    private List<SelectTimeModel> mSelectTimeModelList;
    private OnItemClickListener onItemClickListener;
    public int mSelectedItem = -1;


    public SelectTimeAdapter(Context mContext, List<SelectTimeModel> mSelectTimeModelList) {
        this.mSelectTimeModelList = mSelectTimeModelList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final SelecttimeCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.selecttime_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SelectTimeModel mSelectTimeModel = this.mSelectTimeModelList.get(position);
        holder.bind(mSelectTimeModel);

        holder.binding.rb.setChecked(position == mSelectedItem);

        if (mSelectTimeModel.getPriceStartOption() != null && mSelectTimeModel.getPriceStartOption().equals("ab")) {
            holder.binding.tvPricddd.setText("ab "+ Utility.getPriceReplaceDotWithComma(mSelectTimeModel.getPrice()) + AppConstants.EURO);
        }else {
            holder.binding.tvPricddd.setText(Utility.getPriceReplaceDotWithComma(mSelectTimeModel.getPrice()) + AppConstants.EURO);
        }
        if (mSelectTimeModel.getDiscount() != null && !mSelectTimeModel.getDiscount().equals("")) {
            holder.binding.tvdiscount.setText(Utility.getPriceReplaceDotWithComma(mSelectTimeModel.getDiscount())+ AppConstants.EURO);
        }else {
            holder.binding.tvPricddd.setText("");
        }


//        if (mSelectTimeModel.getDiscount()==null){
//            holder.binding.tvoffpeak.setVisibility(View.GONE);
//            holder.binding.tvdiscount.setVisibility(View.GONE);
//            holder.binding.tvPricddd.setVisibility(View.GONE);
//            return;
//        }


        if (!mSelectTimeModel.getDiscount().equals("0")){
            holder.binding.tvoffpeak.setVisibility(View.VISIBLE);
            holder.binding.tvdiscount.setVisibility(View.VISIBLE);
            holder.binding.tvdiscount.setPaintFlags(holder.binding.tvdiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            holder.binding.tvoffpeak.setVisibility(View.GONE);
            holder.binding.tvdiscount.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mSelectTimeModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<SelectTimeModel> getSelectTimeModel() {
        return mSelectTimeModelList;
    }

    public void addChatMassgeModel(SelectTimeModel mSelectTimeModel) {
        try {
            this.mSelectTimeModelList.add(mSelectTimeModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSelectTimeModelList(List<SelectTimeModel> mSelectTimeModelList) {
        this.mSelectTimeModelList = mSelectTimeModelList;
        notifyDataSetChanged();
    }

    public int getmSelectedItem() {
        return mSelectedItem;
    }


    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final SelecttimeCellBinding binding;

        public ViewHolder(final View view, final SelecttimeCellBinding binding) {
            super(view);
            this.binding = binding;
            view.setOnClickListener(this);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, mSelectTimeModelList.size());
                }
            };
            itemView.setOnClickListener(clickListener);
            binding.rb.setOnClickListener(clickListener);
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
        public void bind(final SelectTimeModel mSelectTimeModel) {
            this.binding.setSelectTimeModel(mSelectTimeModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setmSelectedItem(int mSelectedItem) {
        this.mSelectedItem = mSelectedItem;
    }

}