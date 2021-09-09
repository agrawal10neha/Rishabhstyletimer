package com.si.styletimer.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si.styletimer.R;
import com.si.styletimer.databinding.SubCatListCellBinding;
import com.si.styletimer.models.categoryListing.Sercvice;

import java.util.List;

public class SubCatListingAdapter extends RecyclerView.Adapter<SubCatListingAdapter.ViewHolder> {


    private final Context mContext;
    private List<Sercvice> mSercviceList;
    private OnItemClickListener onItemClickListener;

    public SubCatListingAdapter(Context mContext, List<Sercvice> mSercviceList) {
        this.mSercviceList = mSercviceList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final SubCatListCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.sub_cat_list_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Sercvice mSercvice = this.mSercviceList.get(position);
        holder.bind(mSercvice);



        String name = mSercviceList.get(position).getMCategory()+"-"+mSercviceList.get(position).getSCategory();
        holder.binding.tvCatName.setText(name );
        holder.binding.tvCatDistance.setText(mSercviceList.get(position).getDuration()+" Min.");


        int dcountprive = Integer.parseInt(mSercvice.getDiscountPrice());
        if (dcountprive >0 ){
            holder.binding.tvCatPrice.setText(mContext.getResources().getString(R.string.from)+" € "+mSercvice.getPrice());

           // holder.binding.tvCatPrice.setText(mContext.getResources().getString(R.string.from)+" € "+mSercvice.getDiscountPrice());
        }else {
            holder.binding.tvCatPrice.setText(mContext.getResources().getString(R.string.from)+" € "+mSercvice.getPrice());
        }

        int disc = Integer.parseInt(mSercvice.getDiscountPrice());
        if (disc > 0){
            holder.binding.tvsaveupto.setVisibility(View.VISIBLE);
            holder.binding.tvsaveupto.setText("Du sparst "+discount_per(mSercvice.getPrice(),mSercvice.getDiscountPrice())+ "%");
        }else {
            holder.binding.tvsaveupto.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount() {
        return mSercviceList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<Sercvice> getSercvice() {
        return mSercviceList;
    }

    public void addChatMassgeModel(Sercvice mSercvice) {
        try {
            this.mSercviceList.add(mSercvice);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSercviceList(List<Sercvice> mSercviceList) {
        this.mSercviceList = mSercviceList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final SubCatListCellBinding binding;

        public ViewHolder(final View view, final SubCatListCellBinding binding) {
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
        public void bind(final Sercvice mSercvice) {
            this.binding.setSercvice(mSercvice);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mSercviceList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mSercviceList.size());
    }

    private String discount_per (String price,String disprice){
        Float intprice = Float.valueOf(price);
        Float intdiscprcie = Float.valueOf(disprice);
        Float per = ((intprice-intdiscprcie)*100/intprice);
        int result2 =  Math.round(per);
        String disc = "0";
        return disc = String.valueOf(result2);
    }

}