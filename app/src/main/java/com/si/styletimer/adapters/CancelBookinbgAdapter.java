package com.si.styletimer.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si.styletimer.R;
import com.si.styletimer.databinding.BookingCancelCellBinding;
import com.si.styletimer.models.mybooking_list_model.cancelled_booking_model.CancelModel;
import com.si.styletimer.utill.Utility;

import java.util.List;

public class CancelBookinbgAdapter extends RecyclerView.Adapter<CancelBookinbgAdapter.ViewHolder> {


    private final Context mContext;
    private List<CancelModel> mCancelModelList;
    private OnItemClickListener onItemClickListener;

    public CancelBookinbgAdapter(Context mContext, List<CancelModel> mCancelModelList) {
        this.mCancelModelList = mCancelModelList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final BookingCancelCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.booking_cancel_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        /*final CancelModel mCancelModel = this.mCancelModelList.get(position);
        holder.bind(mCancelModel);*/
        //holder.binding.tvDate.setText(mCancelModelList.get(position).getBookingTime());
        String a[]=mCancelModelList.get(position).getBookingTime().split(" ");
        String b[]=a[1].split(":");
        holder.binding.tvDate.setText(Utility.app_format(a[0])+" / "+b[0]+":"+b[1]);

        holder.binding.tvPrice.setText("€"+Utility.getPriceReplaceDotWithComma(mCancelModelList.get(position).getTotalPrice()));

        holder.binding.tvName.setText(mCancelModelList.get(position).getServiceName());
        holder.binding.tvAddress.setText(mCancelModelList.get(position).getBusinessName()+", "+mCancelModelList.get(position).getAddress());

        holder.binding.tvRebook.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return mCancelModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<CancelModel> getCancelModel() {
        return mCancelModelList;
    }

    public void addChatMassgeModel(CancelModel mCancelModel) {
        try {
            this.mCancelModelList.add(mCancelModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCancelModelList(List<CancelModel> mCancelModelList) {
        this.mCancelModelList = mCancelModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final BookingCancelCellBinding binding;

        public ViewHolder(final View view, final BookingCancelCellBinding binding) {
            super(view);
            this.binding = binding;
            view.setOnClickListener(this);
            binding.tvRebook.setOnClickListener(this);
            binding.llDetail.setOnClickListener(this);
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
        public void bind(final CancelModel mCancelModel) {
            this.binding.setCancelModel(mCancelModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mCancelModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCancelModelList.size());
    }
}