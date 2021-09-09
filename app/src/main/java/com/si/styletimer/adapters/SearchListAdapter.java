package com.si.styletimer.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si.styletimer.R;
import com.si.styletimer.databinding.SearchListCellBinding;
import com.si.styletimer.models.SearchListModel;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {


    private final Context mContext;
    private List<SearchListModel> mSearchListModelList;
    private OnItemClickListener onItemClickListener;

    public SearchListAdapter(Context mContext, List<SearchListModel> mSearchListModelList) {
        this.mSearchListModelList = mSearchListModelList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final SearchListCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.search_list_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SearchListModel mSearchListModel = this.mSearchListModelList.get(position);
        holder.bind(mSearchListModel);

        holder.binding.ratingSaloon.setRating(Float.parseFloat(mSearchListModel.getRating()));

    }

    @Override
    public int getItemCount() {
        return mSearchListModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<SearchListModel> getSearchListModel() {
        return mSearchListModelList;
    }

    public void addChatMassgeModel(SearchListModel mSearchListModel) {
        try {
            this.mSearchListModelList.add(mSearchListModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSearchListModelList(List<SearchListModel> mSearchListModelList) {
        this.mSearchListModelList = mSearchListModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final SearchListCellBinding binding;

        public ViewHolder(final View view, final SearchListCellBinding binding) {
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
        public void bind(final SearchListModel mSearchListModel) {
            this.binding.setSearchListModel(mSearchListModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mSearchListModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mSearchListModelList.size());
    }

    public void clear() {
        int size = mSearchListModelList.size();
        mSearchListModelList.clear();
        notifyItemRangeRemoved(0, size);
    }

}