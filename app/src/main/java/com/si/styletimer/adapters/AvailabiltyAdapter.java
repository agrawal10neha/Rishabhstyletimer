package com.si.styletimer.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si.styletimer.R;
import com.si.styletimer.databinding.AvailabiltyCellBinding;
import com.si.styletimer.models.venu_model.VenueModel;

import java.util.ArrayList;
import java.util.List;

public class AvailabiltyAdapter extends RecyclerView.Adapter<AvailabiltyAdapter.ViewHolder> {


    private final Context mContext;
    private List<VenueModel> mVenueModelList;
    private OnItemClickListener onItemClickListener;
    private ArrayList<String> Days;

    public AvailabiltyAdapter(Context mContext, List<VenueModel> mVenueModelList,ArrayList<String> Days) {
        this.mVenueModelList = mVenueModelList;
        this.Days=Days;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final AvailabiltyCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.availabilty_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VenueModel mVenueModel = this.mVenueModelList.get(position);
        holder.bind(mVenueModel);

        holder.binding.tvDay.setText(Days.get(position));
        if(mVenueModelList.get(position).getStarttime().equals("")){
            holder.binding.ivRing.setImageResource(R.drawable.ic_ring_gradient);
            holder.binding.tvTime.setText(mContext.getResources().getString(R.string.closed));
        }else {

            String s[]=mVenueModel.getStarttime().split(":");
            String e[]=mVenueModel.getEndtime().split(":");

            holder.binding.tvTime.setText(s[0]+":"+s[1]+" - "+e[0]+":"+e[1]);
            holder.binding.ivRing.setImageResource(R.drawable.ic_ring_filled_gradient);
        }



    }

    @Override
    public int getItemCount() {
        return mVenueModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<VenueModel> getVenueModel() {
        return mVenueModelList;
    }

    public void addChatMassgeModel(VenueModel mVenueModel) {
        try {
            this.mVenueModelList.add(mVenueModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVenueModelList(List<VenueModel> mVenueModelList) {
        this.mVenueModelList = mVenueModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final AvailabiltyCellBinding binding;

        public ViewHolder(final View view, final AvailabiltyCellBinding binding) {
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
        public void bind(final VenueModel mVenueModel) {
            this.binding.setVenueModel(mVenueModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mVenueModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mVenueModelList.size());
    }
}