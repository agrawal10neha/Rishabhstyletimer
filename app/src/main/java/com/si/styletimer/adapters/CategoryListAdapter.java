package com.si.styletimer.adapters;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.si.styletimer.R;
import com.si.styletimer.controller.Controller;
import com.si.styletimer.databinding.CategoryCellBinding;
import com.si.styletimer.models.categoryListing.CategoryListing;
import com.si.styletimer.models.categoryListing.Sercvice;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private final Context mContext;
    private List<CategoryListing> mCategoryListingList;
    private OnItemClickListener onItemClickListener;
    private static final String TAG = "CategoryListAdapter";
    private Controller controller;
    private boolean isClicked = false;


    public CategoryListAdapter(Context mContext, List<CategoryListing> mCategoryListingList) {
        this.mCategoryListingList = mCategoryListingList;
        this.mContext = mContext;
        controller = (Controller) mContext.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final CategoryCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.category_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

//--RV
       /* holder.binding.rvSubCat.setClickable(false);
        holder.binding.rvSubCat.setEnabled(false);
        holder.binding.rvSubCat.setLayoutManager(new LinearLayoutManager(mContext));
        holder.binding.rvSubCat.setNestedScrollingEnabled(false);
        SubCatMapAdapter adapter = new SubCatMapAdapter(mContext,mCategoryListingList.get(position).getSercvices());
        holder.binding.rvSubCat.setAdapter(adapter);*/
        renderServiceCell(mCategoryListingList.get(position).getSercvices(), holder);
//--Name
        holder.binding.tvNameSaloon.setText(mCategoryListingList.get(position).getBusinessName());
//--Address
        // holder.binding.tvAddressSaloon.setText(Utility.capitalize(mCategoryListingList.get(position).getAddress()+", "+mCategoryListingList.get(position).getCity()));
        holder.binding.tvAddressSaloon.setText(mCategoryListingList.get(position).getAddress() + ", " + mCategoryListingList.get(position).getCity());
//--Review
        holder.binding.tvavgrating.setText(mCategoryListingList.get(position).getRating());
        holder.binding.tvReview.setText(mCategoryListingList.get(position).getTotalReview() + " Bewertungen");
//--Rating
        if (mCategoryListingList.get(position).getRating().equals("")) {
            holder.binding.ratingSaloon.setRating(0);
        } else {
            holder.binding.ratingSaloon.setRating(Float.valueOf(mCategoryListingList.get(position).getRating()));
        }
        String id = mCategoryListingList.get(position).getId();
        // String url = RetrofitServices.BANNERS +id+"/"+mCategoryListingList.get(position).getImage();
        String someFilepath = RetrofitServices.BANNERS + id + "/" + mCategoryListingList.get(position).getImage();
        String extension = someFilepath.substring(someFilepath.lastIndexOf("."));
        String url;
        if (extension.equals(".webp")) {
            url = RetrofitServices.BANNERS + id + "/crop_" + mCategoryListingList.get(position).getImage();
            // url = RetrofitServices.BANNERS + id + "/prof_" + mCategoryListingList.get(position).getImage();
        } else {
            url = RetrofitServices.BANNERS + id + "/crop_" + mCategoryListingList.get(position).getImage() + ".webp";
            //url = RetrofitServices.BANNERS + id + "/prof_" + mCategoryListingList.get(position).getImage() + ".webp";
        }

        Picasso.get()
                .load(url).fit()
                .placeholder(R.drawable.no_image_available1)
                //.centerCrop()
                .into(holder.binding.ivTop);

        Utility.onTextUnderLine(holder.binding.tvshowonmap);

        if (mCategoryListingList.get(position).getmDistance() != null && !mCategoryListingList.get(position).getmDistance().equals("")) {
            holder.binding.tvDistance.setVisibility(View.VISIBLE);
            double dis = Double.parseDouble(mCategoryListingList.get(position).getmDistance());
            holder.binding.tvDistance.setText(mContext.getResources().getString(R.string.disstance) + " " + Utility.round1(dis, 1) + " km");
        } else {
            holder.binding.tvDistance.setVisibility(View.GONE);
        }

        
        /*if (Session.getUid(mContext).equals("")){
            holder.binding.ivfav.setVisibility(View.GONE);
        }else {
            holder.binding.ivfav.setVisibility(View.VISIBLE);

        }*/

        String fav = mCategoryListingList.get(position).getmFavourite();
        /*if (fav.equals("0")){
            holder.binding.ivfav.setImageDrawable(mContext.getDrawable(R.drawable.ic_fav_border));
        }else if(fav.equals("1")){
            holder.binding.ivfav.setImageDrawable(mContext.getDrawable(R.drawable.ic_fav_filled));
        }*/


        /*holder.binding.ivfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClicked){
                    isClicked = true;
                    setFavourite(mCategoryListingList.get(position).getId(),holder);
                }else {
                    isClicked = false;
                    setFavourite(mCategoryListingList.get(position).getId(),holder);
                }

            }
        });*/

        /*holder.binding.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: ---====>>> "+returnServiceIds( mCategoryListingList.get(position).getSercvices()));
                Toast.makeText(mContext, "Hi-----------", Toast.LENGTH_SHORT).show();
               *//* Session.setSalon_id(mContext,mCategoryListingList.get(position).getId());
                Intent intent = new Intent(mContext,SalonDetailActivity.class);
                intent.putExtra("id",mCategoryListingList.get(position).getId());
                controller.sercviceList =  mCategoryListingList.get(position).getSercvices();
                mContext.startActivity(intent);
                Bungee.slideLeft(mContext);*//*

            }
        });*/
/*
        adapter.setOnItemClickListener(new SubCatMapAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                holder.binding.mainLayout.performClick();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mCategoryListingList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<CategoryListing> getCategoryListing() {
        return mCategoryListingList;
    }

    public void addChatMassgeModel(CategoryListing mCategoryListing) {
        try {
            this.mCategoryListingList.add(mCategoryListing);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCategoryListingList(List<CategoryListing> mCategoryListingList) {
        this.mCategoryListingList = mCategoryListingList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CategoryCellBinding binding;
        RelativeLayout main;

        public ViewHolder(final View view, final CategoryCellBinding binding) {
            super(view);
            this.binding = binding;
            view.setOnClickListener(this::onClick);
            binding.mainLayout.setOnClickListener(this);

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
        public void bind(final CategoryListing mCategoryListing) {
            this.binding.setCategoryListing(mCategoryListing);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    private void deleteitem(int position) {
        mCategoryListingList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCategoryListingList.size());
    }

    /*private void setFavourite(String str_salon_id,ViewHolder holder) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(mContext));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(mContext));
        hashMap.put(AppConstants.UID, Session.getUid(mContext));
        hashMap.put(AppConstants.SALON_ID, str_salon_id);

        Log.e(TAG, "get_cart_Count: "+hashMap );

        RetrofitServices.urlServices.favourite_salon(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (JsonUtil.mainresp(mContext, response)) {
                    try {
                        JSONObject jsonObject = new JSONObject(JsonUtil.resp(response));
                        if (jsonObject.getInt("status") == 1) {
                            int fav = jsonObject.getInt("favourite");
                            if (fav == 1){
                                isClicked = true;
                                holder.binding.ivfav.setImageDrawable(mContext.getDrawable(R.drawable.ic_fav_filled));
                            }else {
                                isClicked = false;
                                holder.binding.ivfav.setImageDrawable(mContext.getDrawable(R.drawable.ic_fav_border));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }*/


    private String returnServiceIds(List<Sercvice> sercviceList) {
        String ids = "";
        StringBuilder stringBuilder = new StringBuilder();
        if (sercviceList.size() > 0) {
            for (int i = 0; i < sercviceList.size(); i++) {
                sercviceList.get(i).getSubcategoryId();
                stringBuilder.append(sercviceList.get(i).getSubcategoryId() + ",");
            }
            ids = stringBuilder.toString().substring(0, stringBuilder.length() - ",".length());
            return ids;
        }
        return ids;
    }

    // by Deepak 31-07-2019
    private void renderServiceCell(List<Sercvice> sercviceList, final ViewHolder holder) {
        int size = sercviceList.size();
        if (size > 0) {

            for (int i = 0; i < size; i++) {

                if (i == 0) {
                    // service list one
                    holder.binding.slOne.setVisibility(View.VISIBLE);

                    Sercvice sercvice = sercviceList.get(0);

                    //    holder.binding.slOnetvCatName.setText(sercvice.getMCategory()+" - "+sercvice.getSCategory());
                    holder.binding.slOnetvCatName.setText(sercvice.getSCategory());
                    holder.binding.slOnetvCatDistance.setText(sercvice.getDuration() + " Min.");

                    if (Utility.returnDiscountDouble(sercvice.getDiscountPrice()) > 0) {
                        if (sercvice.getPriceStartOption() != null && sercvice.getPriceStartOption().equals("ab")) {
                            holder.binding.slOnetvCatPrice.setText(mContext.getResources().getString(R.string.from) + " " + Utility.getPriceReplaceDotWithComma(sercvice.getPrice()) + " €");
                           // holder.binding.slOnetvCatPrice.setText(mContext.getResources().getString(R.string.from) + " " + Utility.getPriceReplaceDotWithComma(sercvice.getDiscountPrice()) + " €");
                        } else {
                            if (sercvice.getTotalService() >= 2) {
                                holder.binding.slOnetvCatPrice.setText(mContext.getResources().getString(R.string.from) + " " +Utility.getPriceReplaceDotWithComma(sercvice.getPrice()) + " €");

                               // holder.binding.slOnetvCatPrice.setText(mContext.getResources().getString(R.string.from) + " " + Utility.getPriceReplaceDotWithComma(sercvice.getDiscountPrice()) + " €");
                            } else {
                                holder.binding.slOnetvCatPrice.setText(Utility.getPriceReplaceDotWithComma(sercvice.getPrice()) + " €");

                               // holder.binding.slOnetvCatPrice.setText(Utility.getPriceReplaceDotWithComma(sercvice.getDiscountPrice()) + " €");
                            }
                        }
                    } else {
                        if (sercvice.getPriceStartOption() != null && sercvice.getPriceStartOption().equals("ab")) {
                            holder.binding.slOnetvCatPrice.setText(mContext.getResources().getString(R.string.from) + " " + Utility.getPriceReplaceDotWithComma(sercvice.getPrice()) + " €");
                        } else {
                            if (sercvice.getTotalService() >= 2) {
                                holder.binding.slOnetvCatPrice.setText(mContext.getResources().getString(R.string.from) + " " +Utility.getPriceReplaceDotWithComma(sercvice.getPrice()) + " €");
                            } else {
                                holder.binding.slOnetvCatPrice.setText(Utility.getPriceReplaceDotWithComma(sercvice.getPrice()) + " €");
                            }
                        }
                    }

                    if (Utility.returnDiscountDouble(sercvice.getDiscountPrice()) > 0) {
                        holder.binding.slOnetvsaveupto.setVisibility(View.VISIBLE);
                        holder.binding.slOnetvsaveupto.setText(mContext.getResources().getString(R.string.save_up_to) + " " + Utility.getPriceReplaceDotWithComma(discount_per(sercvice.getPrice(), sercvice.getDiscountPrice())) + "%");
                    } else {
                        holder.binding.slOnetvsaveupto.setVisibility(View.GONE);
                    }

                } else if (i == 2) {

                    // service list Two
                    holder.binding.slTwo.setVisibility(View.VISIBLE);

                    Sercvice sercvice = sercviceList.get(1);

                    /// holder.binding.slTwotvCatName.setText(sercvice.getMCategory()+" - "+sercvice.getSCategory());
                    holder.binding.slTwotvCatName.setText(sercvice.getSCategory());
                    holder.binding.slTwotvCatDistance.setText(sercvice.getDuration() + " Min.");

                    if (Utility.returnDiscountDouble(sercvice.getDiscountPrice()) > 0) {
                        if (sercvice.getPriceStartOption() != null && sercvice.getPriceStartOption().equals("ab")) {
                            holder.binding.slTwotvCatPrice.setText(mContext.getResources().getString(R.string.from) + " " + Utility.getPriceReplaceDotWithComma(sercvice.getDiscountPrice()) + " €");
                        } else {
                            if (sercvice.getTotalService() >= 2) {
                                holder.binding.slTwotvCatPrice.setText(mContext.getResources().getString(R.string.from) + " " +Utility.getPriceReplaceDotWithComma(sercvice.getDiscountPrice()) + " €");
                            } else {
                                holder.binding.slTwotvCatPrice.setText(Utility.getPriceReplaceDotWithComma(sercvice.getDiscountPrice()) + " €");
                            }
                        }

                    } else {
                        if (sercvice.getPriceStartOption() != null && sercvice.getPriceStartOption().equals("ab")) {
                            holder.binding.slTwotvCatPrice.setText(mContext.getResources().getString(R.string.from) + " " + Utility.getPriceReplaceDotWithComma(sercvice.getPrice()) + " €");
                        } else {
                            if (sercvice.getTotalService() >= 2) {
                                holder.binding.slTwotvCatPrice.setText(mContext.getResources().getString(R.string.from) + " " +Utility.getPriceReplaceDotWithComma(sercvice.getPrice()) + " €");
                            } else {
                                holder.binding.slTwotvCatPrice.setText(Utility.getPriceReplaceDotWithComma(sercvice.getPrice()) + " €");
                            }
                        }
                    }

                    if (Utility.returnDiscountDouble(sercvice.getDiscountPrice()) > 0) {
                        holder.binding.slTwotvsaveupto.setVisibility(View.VISIBLE);
                        holder.binding.slTwotvsaveupto.setText(mContext.getResources().getString(R.string.save_up_to) + " " + Utility.getPriceReplaceDotWithComma(discount_per(sercvice.getPrice(), sercvice.getDiscountPrice())) + "%");
                    } else {
                        holder.binding.slTwotvsaveupto.setVisibility(View.GONE);
                    }

                } else if (i == 3) {

                    // service list Three
                    holder.binding.slThree.setVisibility(View.VISIBLE);
                    Sercvice sercvice = sercviceList.get(2);

                    //    holder.binding.slThreetvCatName.setText(sercvice.getMCategory()+" - "+sercvice.getSCategory());
                    holder.binding.slThreetvCatName.setText(sercvice.getSCategory());
                    holder.binding.slThreetvCatDistance.setText(sercvice.getDuration() + " Min.");

                    if (Utility.returnDiscountDouble(sercvice.getDiscountPrice()) > 0) {
                        if (sercvice.getPriceStartOption() != null && sercvice.getPriceStartOption().equals("ab")) {
                            holder.binding.slThreetvCatPrice.setText(mContext.getResources().getString(R.string.from) + " " + Utility.getPriceReplaceDotWithComma(sercvice.getDiscountPrice()) + " €");
                        } else {
                            if (sercvice.getTotalService() >= 2) {
                                holder.binding.slThreetvCatPrice.setText(mContext.getResources().getString(R.string.from) + " " + Utility.getPriceReplaceDotWithComma(sercvice.getDiscountPrice()) + " €");
                            } else {
                                holder.binding.slThreetvCatPrice.setText(Utility.getPriceReplaceDotWithComma(sercvice.getDiscountPrice()) + " €");
                            }
                        }
                    } else {
                        if (sercvice.getPriceStartOption() != null && sercvice.getPriceStartOption().equals("ab")) {
                            holder.binding.slThreetvCatPrice.setText(mContext.getResources().getString(R.string.from) + " " + Utility.getPriceReplaceDotWithComma(sercvice.getPrice()) + " €");
                        } else {
                            if (sercvice.getTotalService() >= 2) {
                                holder.binding.slThreetvCatPrice.setText(mContext.getResources().getString(R.string.from) + " " +Utility.getPriceReplaceDotWithComma(sercvice.getPrice()) + " €");
                            } else {
                                holder.binding.slThreetvCatPrice.setText(Utility.getPriceReplaceDotWithComma(sercvice.getPrice()) + " €");
                            }
                        }
                    }

                    if (Utility.returnDiscountDouble(sercvice.getDiscountPrice()) > 0) {
                        holder.binding.slThreetvsaveupto.setVisibility(View.VISIBLE);
                        holder.binding.slThreetvsaveupto.setText(mContext.getResources().getString(R.string.save_up_to) + " " + Utility.getPriceReplaceDotWithComma(discount_per(sercvice.getPrice(), sercvice.getDiscountPrice())) + "%");
                    } else {
                        holder.binding.slThreetvsaveupto.setVisibility(View.GONE);
                    }

                }
            }
        }
    }

    private String discount_per(String price, String disprice) {
        Float intprice = Float.valueOf(price);
        Float intdiscprcie = Float.valueOf(disprice);
        Float per = ((intprice - intdiscprcie) * 100 / intprice);
        int result2 = Math.round(per);
        String disc = "0";
        return disc = String.valueOf(result2);
    }

}