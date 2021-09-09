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
import com.si.styletimer.databinding.TreatmentSearchCellBinding;
import com.si.styletimer.models.search_treatment_model.TreatmentSearchModel;

import java.util.ArrayList;
import java.util.List;

public class TreatmentSearchAdapter extends RecyclerView.Adapter<TreatmentSearchAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "TreatmentSearchAdapter";
    private final Context mContext;
    private List<TreatmentSearchModel> mTreatmentSearchModelList;
    private OnItemClickListener onItemClickListener;
    private List<TreatmentSearchModel> mFiltered;


    public TreatmentSearchAdapter(Context mContext, List<TreatmentSearchModel> mTreatmentSearchModelList) {
        this.mTreatmentSearchModelList = mTreatmentSearchModelList;
        this.mFiltered = mTreatmentSearchModelList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final TreatmentSearchCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.treatment_search_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        /*final TreatmentSearchModel mTreatmentSearchModel = this.mTreatmentSearchModelList.get(position);
        holder.bind(mTreatmentSearchModel);*/

        holder.binding.tvName.setText(mTreatmentSearchModelList.get(position).getCategoryName());

    }

    @Override
    public int getItemCount() {
        return mTreatmentSearchModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<TreatmentSearchModel> getTreatmentSearchModel() {
        return mTreatmentSearchModelList;
    }

    public void addChatMassgeModel(TreatmentSearchModel mTreatmentSearchModel) {
        try {
            this.mTreatmentSearchModelList.add(mTreatmentSearchModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTreatmentSearchModelList(List<TreatmentSearchModel> mTreatmentSearchModelList) {
        this.mTreatmentSearchModelList = mTreatmentSearchModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TreatmentSearchCellBinding binding;

        public ViewHolder(final View view, final TreatmentSearchCellBinding binding) {
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
        public void bind(final TreatmentSearchModel mTreatmentSearchModel) {
            this.binding.setTreatmentSearchModel(mTreatmentSearchModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mTreatmentSearchModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mTreatmentSearchModelList.size());
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                Log.e(TAG, "performFiltering: --> "+charString);

                if (charString.isEmpty()) {
                   // mFiltered = mTreatmentSearchModelList;

                    mTreatmentSearchModelList = mFiltered;

                } else {
                    List<TreatmentSearchModel> filteredList = new ArrayList<>();

                    for (TreatmentSearchModel row : mFiltered) {
                        Log.e(TAG, "performFiltering: "+row.getCategoryName() );
                        if (row.getCategoryName().toLowerCase().contains(charString.toLowerCase())) {
                            Log.e(TAG, "================================== " );
                            filteredList.add(row);
                        }
                    }
                    mTreatmentSearchModelList = filteredList;
                    Log.e(TAG, "performFiltering: "+filteredList.toString() );
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mTreatmentSearchModelList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mTreatmentSearchModelList = (ArrayList<TreatmentSearchModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}