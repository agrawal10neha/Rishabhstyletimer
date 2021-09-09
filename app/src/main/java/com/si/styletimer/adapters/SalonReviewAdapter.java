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
import com.si.styletimer.activity.SalonReviewActivity;
import com.si.styletimer.databinding.SalonreviewCellBinding;
import com.si.styletimer.models.review.SalonReviewModel;
import com.si.styletimer.utill.Utility;

import java.util.ArrayList;
import java.util.List;

public class SalonReviewAdapter extends RecyclerView.Adapter<SalonReviewAdapter.ViewHolder> implements Filterable {


    private final Context mContext;
    private List<SalonReviewModel> mSalonReviewModelList;
    private List<SalonReviewModel> Filtered;
    private OnItemClickListener onItemClickListener;
    private static final String TAG = "SalonReviewAdapter";

    public SalonReviewAdapter(Context mContext, List<SalonReviewModel> mSalonReviewModelList) {
        this.mSalonReviewModelList = mSalonReviewModelList;
        this.Filtered = mSalonReviewModelList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final SalonreviewCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.salonreview_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.binding.ratingbar.setRating(Float.valueOf(Filtered.get(position).getRate()));
        String dateee = Utility.change_format(Filtered.get(position).getCreatedOn().split(" ")[0],"yyyy-MM-dd","MM/dd/yyyy");
        holder.binding.tvDate.setText(mSalonReviewModelList.get(position).getAgo());
        if(Filtered.get(position).getReview().toString().trim().equals("")){
            holder.binding.tvReview.setVisibility(View.GONE);
        }else {
            holder.binding.tvReview.setVisibility(View.VISIBLE);
            holder.binding.tvReview.setText(Filtered.get(position).getReview().trim());
        }

        if(Filtered.get(position).getAnonymous().equals("0")){
            holder.binding.tvName.setText(Utility.capitalize(Filtered.get(position).getFirstName())+"");
        }else {
            holder.binding.tvName.setText("Anonym - ");
        }


        holder.binding.tvTreatedBy.setText(" behandelt von "+Utility.capitalize(Filtered.get(position).getmEmpFirstname()));
        holder.binding.tvBookedService.setText("Gebuchte Services : "+Filtered.get(position).getmServiceName());

    }

    @Override
    public int getItemCount() {
        return Filtered.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<SalonReviewModel> getSalonReviewModel() {
        return Filtered;
    }

    public void addChatMassgeModel(SalonReviewModel mSalonReviewModel) {
        try {
            this.Filtered.add(mSalonReviewModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSalonReviewModelList(List<SalonReviewModel> Filtered) {
        this.Filtered = Filtered;
        this.mSalonReviewModelList=Filtered;

        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                Log.e(TAG, "performFiltering: --> "+charString);

                if (charString.isEmpty()) {
                    Filtered = mSalonReviewModelList;

                } else {
                    List<SalonReviewModel> filteredList = new ArrayList<>();

                    for (SalonReviewModel row : mSalonReviewModelList) {

                        if (row.getRate().contains(charString)) {
                            filteredList.add(row);
                        }else {

                        }
                    }

                    Filtered = filteredList;

                    if(filteredList.isEmpty()) {
                        SalonReviewActivity.iv.setVisibility(View.VISIBLE);
                    }

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = Filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Filtered = (ArrayList<SalonReviewModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final SalonreviewCellBinding binding;

        public ViewHolder(final View view, final SalonreviewCellBinding binding) {
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
        public void bind(final SalonReviewModel mSalonReviewModel) {
            //this.binding.setSalonReview(mSalonReviewModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mSalonReviewModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mSalonReviewModelList.size());
    }
}