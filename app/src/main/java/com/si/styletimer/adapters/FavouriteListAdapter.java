package com.si.styletimer.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si.styletimer.R;
import com.si.styletimer.databinding.FavouriteListCellBinding;
import com.si.styletimer.models.fav_list_model.FavouriteListModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteListAdapter extends RecyclerView.Adapter<FavouriteListAdapter.ViewHolder> {


    private final Context mContext;
    private List<FavouriteListModel> mFavouriteListModelList;
    private OnItemClickListener onItemClickListener;
    private static final String TAG = "FavouriteListAdapter";

    public FavouriteListAdapter(Context mContext, List<FavouriteListModel> mFavouriteListModelList) {
        this.mFavouriteListModelList = mFavouriteListModelList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final FavouriteListCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.favourite_list_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FavouriteListModel mFavouriteListModel = this.mFavouriteListModelList.get(position);
        holder.bind(mFavouriteListModel);

        holder.binding.tvName.setText(mFavouriteListModel.getBusinessName());
        holder.binding.tvAddress.setText(mFavouriteListModel.getAddress()+"\n"+mFavouriteListModel.getZip()+" "+mFavouriteListModel.getCity());

        String url = RetrofitServices.BANNERS +mFavouriteListModel.getSalonId()+"/"+mFavouriteListModel.getImage();
        Log.e(TAG, "onBindViewHolder: - "+url );

        Picasso.get().load(url).fit().placeholder(R.drawable.no_image_available1).centerCrop().into(holder.binding.iv);

        holder.binding.ratingbar.setRating(Float.valueOf(mFavouriteListModel.getRating()));

        /*holder.binding.llFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFavourite(mFavouriteListModel.getSalonId(),position);

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mFavouriteListModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<FavouriteListModel> getFavouriteListModel() {
        return mFavouriteListModelList;
    }

    public void addChatMassgeModel(FavouriteListModel mFavouriteListModel) {
        try {
            this.mFavouriteListModelList.add(mFavouriteListModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFavouriteListModelList(List<FavouriteListModel> mFavouriteListModelList) {
        this.mFavouriteListModelList = mFavouriteListModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final FavouriteListCellBinding binding;

        public ViewHolder(final View view, final FavouriteListCellBinding binding) {
            super(view);
            this.binding = binding;
            binding.llFav.setOnClickListener(this);
            binding.rlMain.setOnClickListener(this);
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
        public void bind(final FavouriteListModel mFavouriteListModel) {
            this.binding.setFavouriteListModel(mFavouriteListModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mFavouriteListModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mFavouriteListModelList.size());
    }



}