package com.si.styletimer.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si.styletimer.R;
import com.si.styletimer.databinding.ServicesCellBinding;
import com.si.styletimer.models.bookind_detail_service.ServicesModel;
import com.si.styletimer.utill.Utility;

import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {


    private final Context mContext;
    private List<ServicesModel> mServicesModelList;
    private OnItemClickListener onItemClickListener;

    public ServicesAdapter(Context mContext, List<ServicesModel> mServicesModelList) {
        this.mServicesModelList = mServicesModelList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final ServicesCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.services_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        /*final ServicesModel mServicesModel = this.mServicesModelList.get(position);
        holder.bind(mServicesModel);*/

        if(mServicesModelList.get(position).getServiceName().equals("")){
            holder.binding.tvName.setText(mServicesModelList.get(position).getCatName());
        }else {
            holder.binding.tvName.setText(mServicesModelList.get(position).getCatName()+" - "+mServicesModelList.get(position).getServiceName());

        }

        holder.binding.tvTime.setText(mServicesModelList.get(position).getDuration()+" Min.");
        if (mServicesModelList.get(position).getPriceStartOption() != null && mServicesModelList.get(position).getPriceStartOption().equals("ab"))
        {
            holder.binding.tvPrice.setText("ab "+ Utility.getPriceReplaceDotWithComma(mServicesModelList.get(position).getPrice()) + "€");
        }else {
            holder.binding.tvPrice.setText(Utility.getPriceReplaceDotWithComma(mServicesModelList.get(position).getPrice()) + "€");
        }

    }

    @Override
    public int getItemCount() {
        return mServicesModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<ServicesModel> getServicesModel() {
        return mServicesModelList;
    }

    public void addChatMassgeModel(ServicesModel mServicesModel) {
        try {
            this.mServicesModelList.add(mServicesModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setServicesModelList(List<ServicesModel> mServicesModelList) {
        this.mServicesModelList = mServicesModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ServicesCellBinding binding;

        public ViewHolder(final View view, final ServicesCellBinding binding) {
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
        public void bind(final ServicesModel mServicesModel) {
            this.binding.setServicesModel(mServicesModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mServicesModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mServicesModelList.size());
    }
}