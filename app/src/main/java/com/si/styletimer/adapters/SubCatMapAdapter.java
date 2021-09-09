package com.si.styletimer.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si.styletimer.R;
import com.si.styletimer.databinding.MapCellBinding;
import com.si.styletimer.models.categoryListing.Sercvice;
import com.si.styletimer.models.mapListmodel.MapSubCatListModel;

import java.util.List;

public class SubCatMapAdapter extends RecyclerView.Adapter<SubCatMapAdapter.ViewHolder> {

    private final Context mContext;
    private List<Sercvice> mMapSubCatListModelList;
    private OnItemClickListener onItemClickListener;

    public SubCatMapAdapter(Context mContext, List<Sercvice> mMapSubCatListModelList) {
        this.mMapSubCatListModelList = mMapSubCatListModelList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final MapCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.map_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        final MapSubCatListModel mMapSubCatListModel = this.mMapSubCatListModelList.get(position);
//        holder.bind(mMapSubCatListModel);

        String trim="";
        if(mMapSubCatListModelList.get(position).getSCategory().length()>40){
            trim = mMapSubCatListModelList.get(position).getSCategory().substring(0,38);
        }else {
            trim = mMapSubCatListModelList.get(position).getSCategory();
        }

        holder.binding.tvCatName.setText(mMapSubCatListModelList.get(position).getMCategory()+" - "+mMapSubCatListModelList.get(position).getSCategory());
        holder.binding.tvCatDistance.setText(mMapSubCatListModelList.get(position).getDuration()+" Min.");

        int dcountprive = Integer.parseInt(mMapSubCatListModelList.get(position).getDiscountPrice());
        if (dcountprive >0 ){
          //  holder.binding.tvCatPrice.setText(mContext.getResources().getString(R.string.from)+" € "+mMapSubCatListModelList.get(position).getDiscountPrice());
            holder.binding.tvCatPrice.setText(mContext.getResources().getString(R.string.from)+" € "+mMapSubCatListModelList.get(position).getPrice());

        }else {
            holder.binding.tvCatPrice.setText(mContext.getResources().getString(R.string.from)+" € "+mMapSubCatListModelList.get(position).getPrice());
        }

        //holder.binding.tvCatPrice.setText("from €"+mMapSubCatListModelList.get(position).getPrice());

        int disc = Integer.parseInt(mMapSubCatListModelList.get(position).getDiscountPrice());
        if (disc > 0){
            holder.binding.tvsaveupto.setVisibility(View.VISIBLE);
            holder.binding.tvsaveupto.setText(mContext.getResources().getString(R.string.save_up_to)+discount_per(mMapSubCatListModelList.get(position).getPrice(),mMapSubCatListModelList.get(position).getDiscountPrice())+ "%");
        }else {
            holder.binding.tvsaveupto.setVisibility(View.GONE);
        }

    }

    private String discount_per (String price,String disprice){
        /*int intprice = Integer.parseInt(price);
        int intdiscprcie = Integer.parseInt(disprice);
        String disc = "0";
        return disc = String.valueOf(((intprice-intdiscprcie)*100/intprice));*/


        Float intprice = Float.valueOf(price);
        Float intdiscprcie = Float.valueOf(disprice);
        Float per = ((intprice-intdiscprcie)*100/intprice);
        int result2 =  Math.round(per);
        String disc = "0";
        return disc = String.valueOf(result2);


    }

    @Override
    public int getItemCount() {
        return mMapSubCatListModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<Sercvice> getMapSubCatListModel() {
        return mMapSubCatListModelList;
    }

    /*public void addChatMassgeModel(MapSubCatListModel mMapSubCatListModel) {
        try {
            this.mMapSubCatListModelList.add(mMapSubCatListModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void setMapSubCatListModelList(List<Sercvice> mMapSubCatListModelList) {
        this.mMapSubCatListModelList = mMapSubCatListModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final MapCellBinding binding;

        public ViewHolder(final View view, final MapCellBinding binding) {
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
        public void bind(final MapSubCatListModel mMapSubCatListModel) {
            this.binding.setMapSubCatListModel(mMapSubCatListModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mMapSubCatListModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mMapSubCatListModelList.size());
    }

    private String discount_percent (String price,String disprice){

        Float intprice = Float.valueOf(price);
        Float intdiscprcie = Float.valueOf(disprice);
        Float per = ((intprice-intdiscprcie)*100/intprice);
        int result2 =  Math.round(per);
        String disc = "0";
        return disc = String.valueOf(result2);

    }

}