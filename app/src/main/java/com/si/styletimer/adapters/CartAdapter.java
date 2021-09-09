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
import com.si.styletimer.databinding.CartCellBinding;
import com.si.styletimer.models.CartModel;
import com.si.styletimer.utill.Utility;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final Context mContext;
    private List<CartModel> mCartModelList;
    private OnItemClickListener onItemClickListener;

    public CartAdapter(Context mContext, List<CartModel> mCartModelList) {
        this.mCartModelList = mCartModelList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final CartCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.cart_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CartModel mCartModel = this.mCartModelList.get(position);
        holder.bind(mCartModel);

        if(!mCartModel.getName().equalsIgnoreCase("")){
            holder.binding.tvmyname.setText(mCartModel.getCategoryName()+" - "+mCartModel.getName());
        }else {
            holder.binding.tvmyname.setText(mCartModel.getCategoryName());

        }
        if (Utility.returnDiscountDouble(mCartModel.getDiscountPrice())>0){
            double tot = Double.parseDouble(mCartModel.getPrice()) - Double.parseDouble(mCartModel.getDiscountPrice());
            if (mCartModel.getPriceStartOption() != null && mCartModel.getPriceStartOption().equals("ab")) {

                holder.binding.tvpriceddd.setText("ab "+ Utility.getPriceReplaceDotWithComma(String.valueOf(tot))+" "+ AppConstants.EURO);
            }else {
                holder.binding.tvpriceddd.setText(Utility.getPriceReplaceDotWithComma(String.valueOf(tot))+" "+ AppConstants.EURO);
            }
        }else {
            if (mCartModel.getPriceStartOption() != null && mCartModel.getPriceStartOption().equals("ab")) {
                holder.binding.tvpriceddd.setText("ab "+Utility.getPriceReplaceDotWithComma(mCartModel.getPrice())+" "+ AppConstants.EURO);
            }else {
                holder.binding.tvpriceddd.setText(Utility.getPriceReplaceDotWithComma(mCartModel.getPrice())+" "+ AppConstants.EURO);
            }

        }

        int disc = Integer.parseInt(mCartModel.getDiscountPercent());
        if (disc > 0){
            holder.binding.tvsaveupto.setVisibility(View.VISIBLE);
            holder.binding.tvsaveupto.setText("Du sparst "+mCartModel.getDiscountPercent()+ "%");
        }else {

            holder.binding.tvsaveupto.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mCartModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<CartModel> getCartModel() {
        return mCartModelList;
    }

    public void addChatMassgeModel(CartModel mCartModel) {
        try {
            this.mCartModelList.add(mCartModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCartModelList(List<CartModel> mCartModelList) {
        this.mCartModelList = mCartModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CartCellBinding binding;

        public ViewHolder(final View view, final CartCellBinding binding) {
            super(view);
            this.binding = binding;
            view.setOnClickListener(this);
            binding.btnivclose.setOnClickListener(this::onClick);
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
        public void bind(final CartModel mCartModel) {
            this.binding.setCartModel(mCartModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void removeItem(int position) {
        mCartModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCartModelList.size());
    }
}