package com.si.styletimer.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.si.styletimer.R;
import com.si.styletimer.databinding.CatSearchCellBinding;
import com.si.styletimer.models.CatSearchModell;
import com.si.styletimer.models.search_treatment_model.TreatmentSearchModel;
import com.si.styletimer.utill.Utility;

import java.util.ArrayList;
import java.util.List;

public class CatSearchAdapter extends RecyclerView.Adapter<CatSearchAdapter.ViewHolder> {


    private final Context mContext;
    private List<CatSearchModell> mCatSearchModellList;
    private OnItemClickListener onItemClickListener;
    private List<CatSearchModell> mFiltered;

    public CatSearchAdapter(Context mContext, List<CatSearchModell> mCatSearchModellList) {
        this.mCatSearchModellList = mCatSearchModellList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final CatSearchCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.cat_search_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CatSearchModell mCatSearchModell = this.mCatSearchModellList.get(position);
        holder.bind(mCatSearchModell);
        holder.binding.tv.setText(mCatSearchModell.getCategoryName());
       // holder.binding.tv.setText(Utility.capitalize(mCatSearchModell.getCategoryName()));
    }

    @Override
    public int getItemCount() {
        return mCatSearchModellList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<CatSearchModell> getCatSearchModell() {
        return mCatSearchModellList;
    }

    public void addChatMassgeModel(CatSearchModell mCatSearchModell) {
        try {
            this.mCatSearchModellList.add(mCatSearchModell);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCatSearchModellList(List<CatSearchModell> mCatSearchModellList) {
        this.mCatSearchModellList = mCatSearchModellList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CatSearchCellBinding binding;

        public ViewHolder(final View view, final CatSearchCellBinding binding) {
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
        public void bind(final CatSearchModell mCatSearchModell) {
          //  this.binding.setCatSearchModell(mCatSearchModell);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mCatSearchModellList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCatSearchModellList.size());
    }

}