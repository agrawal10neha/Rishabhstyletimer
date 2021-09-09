package com.si.styletimer.adapters;

import android.content.Context;

import androidx.databinding.DataBindingUtil;


import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si.styletimer.R;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.TaxesCellBinding;
import com.si.styletimer.models.receiptdetail.TaxesModel;
import com.si.styletimer.utill.Utility;

import java.util.List;

/**
 * Developed by : Kamal Patidar
 * Author : Shanti Infotech Pvt. Ltd.
 * Email : kamal.shantiinfotech@gmail.com
 * Website : https://shantiinfotech.com/
 * Created on : 25,April,2020
 */
public class TaxesAdapter extends RecyclerView.Adapter<TaxesAdapter.ViewHolder> {


    private final Context mContext;
    private List<TaxesModel> mTaxesModelList;
    private OnItemClickListener onItemClickListener;
    private int currentPos = -1;

    public TaxesAdapter(Context mContext, List<TaxesModel> mTaxesModelList) {
        this.mTaxesModelList = mTaxesModelList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final TaxesCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.taxes_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TaxesModel mTaxesModel = this.mTaxesModelList.get(position);
        holder.bind(mTaxesModel);
        holder.binding.tvName.setText(mTaxesModel.getName());
        holder.binding.tvPrice.setText(Utility.getPriceReplaceDotWithComma(mTaxesModel.getValue())+" " +AppConstants.EURO);
    }

    @Override
    public int getItemCount() {
        return mTaxesModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<TaxesModel> getTaxesModel() {
        return mTaxesModelList;
    }

    public void addChatMassgeModel(TaxesModel mTaxesModel) {
        try {
            this.mTaxesModelList.add(mTaxesModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTaxesModelList(List<TaxesModel> mTaxesModelList) {
        this.mTaxesModelList = mTaxesModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TaxesCellBinding binding;

        public ViewHolder(final View view, final TaxesCellBinding binding) {
            super(view);
            this.binding = binding;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                if (onItemClickListener != null) {
                    setCurrentPos(getAdapterPosition());
                    notifyItemRangeChanged(0, mTaxesModelList.size());
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @UiThread
        public void bind(final TaxesModel mTaxesModel) {
            // this.binding.setTaxes(mTaxesModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void deleteitem(int position) {
        mTaxesModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mTaxesModelList.size());
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    public void clearAdapter() {
        int size = mTaxesModelList.size();
        mTaxesModelList.clear();
        notifyItemRangeRemoved(0, size);
    }

}