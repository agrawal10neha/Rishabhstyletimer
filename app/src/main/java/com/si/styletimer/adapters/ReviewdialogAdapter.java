package com.si.styletimer.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si.styletimer.R;
import com.si.styletimer.databinding.ReviewdialogCellBinding;
import com.si.styletimer.models.showdatarating.Review;
import com.si.styletimer.utill.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReviewdialogAdapter extends RecyclerView.Adapter<ReviewdialogAdapter.ViewHolder> {
    private static final String TAG = "ReviewdialogAdapter";
    private final Context mContext;
    private List<Review> mReviewList;
    private OnItemClickListener onItemClickListener;
    private int currentPos = -1;

    public ReviewdialogAdapter(Context mContext, List<Review> mReviewList) {
        this.mReviewList = mReviewList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final ReviewdialogCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.reviewdialog_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Review mReview = this.mReviewList.get(position);
        holder.bind(mReview);

        holder.binding.ratingbar.setRating(Float.valueOf(mReview.getRate()));
        String dateee = Utility.change_format(mReview.getCreatedOn().split(" ")[0],"yyyy-MM-dd","MM/dd/yyyy");
        holder.binding.tvDate.setText(dateee);
        if(mReview.getReview().equals("")){
            holder.binding.tvReview.setVisibility(View.GONE);
        }else {
            holder.binding.tvReview.setVisibility(View.VISIBLE);
            holder.binding.tvReview.setText(mReview.getReview().trim());
        }

        if(mReview.getAnonymous().equals("0")){
            if (mReview.getUsername() == null){
                holder.binding.tvName.setText("");
            }else {
                holder.binding.tvName.setText(mReview.getUsername().split(" ")[0]);
            }
        }else {
            holder.binding.tvName.setText(mContext.getResources().getString(R.string.anonymous));
        }

        holder.binding.tvTreatedBy.setText("Behandelt von "+mReview.getEmployee().split(" ")[0]);

        try {
           // Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mReview.getCreatedOn());
           // holder.binding.tvdaysago.setText(Utility.getTimeAgo(date1.getTime()));
            holder.binding.tvdaysago.setText(mReview.getTimeAgo());
        } catch (Exception e) {
            e.printStackTrace();
        }
     

    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<Review> getReview() {
        return mReviewList;
    }

    public void addChatMassgeModel(Review mReview) {
        try {
            this.mReviewList.add(mReview);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setReviewList(List<Review> mReviewList) {
        this.mReviewList = mReviewList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ReviewdialogCellBinding binding;

        public ViewHolder(final View view, final ReviewdialogCellBinding binding) {
            super(view);
            this.binding = binding;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                if (onItemClickListener != null) {
                    setCurrentPos(getAdapterPosition());
                    notifyItemRangeChanged(0, mReviewList.size());
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @UiThread
        public void bind(final Review mReview) {
            this.binding.setReview(mReview);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void deleteitem(int position) {
        mReviewList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mReviewList.size());
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    public void clearAdapter() {
        int size = mReviewList.size();
        mReviewList.clear();
        notifyItemRangeRemoved(0, size);
    }

}