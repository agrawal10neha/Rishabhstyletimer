package com.si.styletimer.adapters;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si.styletimer.R;
import com.si.styletimer.databinding.PaymentmethodCellBinding;
import com.si.styletimer.models.PaymentMethodModel;
import com.si.styletimer.utill.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Developed by : Kamal Patidar
 * Author : Shanti Infotech Pvt. Ltd.
 * Email : kamal.shantiinfotech@gmail.com
 * Website : https://shantiinfotech.com/
 * Created on : 26,November,2020
 */
public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {


    private final Context mContext;
    private List<PaymentMethodModel> mPaymentMethodModelList;
    private OnItemClickListener onItemClickListener;
    private int currentPos = -1;

    public PaymentMethodAdapter(Context mContext, List<PaymentMethodModel> mPaymentMethodModelList) {
        this.mPaymentMethodModelList = mPaymentMethodModelList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final PaymentmethodCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.paymentmethod_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PaymentMethodModel mPaymentMethodModel = this.mPaymentMethodModelList.get(position);
        holder.bind(mPaymentMethodModel);
        holder.binding.tvDay.setText(mPaymentMethodModel.getMethodName());
    }

    @Override
    public int getItemCount() {
        return mPaymentMethodModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<PaymentMethodModel> getPaymentMethodModel() {
        return mPaymentMethodModelList;
    }

    public void addChatMassgeModel(PaymentMethodModel mPaymentMethodModel) {
        try {
            this.mPaymentMethodModelList.add(mPaymentMethodModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPaymentMethodModelList(List<PaymentMethodModel> mPaymentMethodModelList) {
        this.mPaymentMethodModelList = mPaymentMethodModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final PaymentmethodCellBinding binding;

        public ViewHolder(final View view, final PaymentmethodCellBinding binding) {
            super(view);
            this.binding = binding;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                if (onItemClickListener != null) {
                    setCurrentPos(getAdapterPosition());
                    notifyItemRangeChanged(0, mPaymentMethodModelList.size());
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @UiThread
        public void bind(final PaymentMethodModel mPaymentMethodModel) {
            // this.binding.setPaymentMethod(mPaymentMethodModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void deleteitem(int position) {
        mPaymentMethodModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mPaymentMethodModelList.size());
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    public void clearAdapter() {
        int size = mPaymentMethodModelList.size();
        mPaymentMethodModelList.clear();
        notifyItemRangeRemoved(0, size);
    }

}