
package com.si.styletimer.models.salonDetails;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.si.styletimer.R;
import com.si.styletimer.Session.RealmController;
import com.si.styletimer.app_constants.AppConstants;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class SalonAllServiceModel{
    private static final String TAG = "SalonAllServiceModel";
    @SerializedName("category_name")
    private String mCategoryName;
    @SerializedName("count")
    private String mCount;
    @SerializedName("id")
    private String mId;
    @SerializedName("is_open")
    private String mIsOpen;
    @SerializedName("start_price")
    private String mStartPrice;
    @SerializedName("sub_services")
    private List<SalonSubServiceModel> mSubServices;
    @SerializedName("subid")
    private String mSubid;
    @SerializedName("myOpen")
    private String myOpen;
    @SerializedName("service_detail")
    private String serviceDetail;
    @SerializedName("review_count")
    private String reviewcount;
    @SerializedName("service_detail_id")
    private String servicedetailid;
    private boolean isChecked = false;
    @SerializedName("service_duration")
    private String serviceDuration;
    private List<Double> priceList = new ArrayList<>();
    private List<Double> priceList1 = new ArrayList<>();
    private List<Double> disCountMaxList = new ArrayList<>();
    @SerializedName("total_in_cart")
    private long totalInCart;

    public long getTotalInCart() {
        return totalInCart;
    }

    public void setTotalInCart(long totalInCart) {
        this.totalInCart = totalInCart;
    }

    public String getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(String serviceDuration) {
        this.serviceDuration = serviceDuration;
    }

    public String getServicedetailid() {
        return servicedetailid;
    }

    public void setServicedetailid(String servicedetailid) {
        this.servicedetailid = servicedetailid;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getReviewcount() {
        return reviewcount;
    }

    public void setReviewcount(String reviewcount) {
        this.reviewcount = reviewcount;
    }

    public String getServiceDetail() {
        return serviceDetail;
    }

    public void setServiceDetail(String serviceDetail) {
        this.serviceDetail = serviceDetail;
    }

    public String getMyOpen() {
        return myOpen;
    }

    public void setMyOpen(String myOpen) {
        this.myOpen = myOpen;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public String getCount() {
        return mCount;
    }

    public void setCount(String count) {
        mCount = count;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }


    public String getStartPrice() {
        return mStartPrice;
    }

    public void setStartPrice(String startPrice) {
        mStartPrice = startPrice;
    }

    public List<SalonSubServiceModel> getSubServices() {
        return mSubServices;
    }

    public void setSubServices(List<SalonSubServiceModel> subServices) {
        mSubServices = subServices;
    }

    public String getSubid() {
        return mSubid;
    }

    public void setSubid(String subid) {
        mSubid = subid;
    }

    public String getMinPrice() {
        String pricetot = "";
        for (int i = 0; i < mSubServices.size(); i++) {
            priceList1.add(Double.valueOf(mSubServices.get(i).getPrice()));
            if (mSubServices.get(i).getDiscountPrice().equals("0")) {
                if (mSubServices.get(i).getPriceStartOption() != null && mSubServices.get(i).getPriceStartOption().equals("ab")) {
                    pricetot = "ab " + String.valueOf(Collections.min(priceList1));
                 //   pricetot = "ab " +mSubServices.get(i).getPrice();
                }else {
                    pricetot = "" + String.valueOf(Collections.min(priceList1));
                   // pricetot = "" +mSubServices.get(i).getPrice();
                }

             //   Log.e(TAG, "getMinPrice if  : " + pricetot );
            } else {
                priceList.add(Double.valueOf(mSubServices.get(i).getPrice()));
                //priceList.add(Double.valueOf(mSubServices.get(i).getDiscountPrice()));
                if (mSubServices.get(i).getPriceStartOption() != null && mSubServices.get(i).getPriceStartOption().equals("ab")) {
                    pricetot = "ab " + String.valueOf(Collections.min(priceList));
                }else {
                    pricetot = String.valueOf(Collections.min(priceList));
                }

               // Log.e(TAG, "getMinPrice else : " + pricetot );
              //  Log.e(TAG, "getMinPrice else Collections.min(priceList) : " + Collections.min(priceList) );
            }
        }

        return pricetot;
    }

    public String getMaxDiscount() {
        String pricetot = "";
        for (int i = 0; i < mSubServices.size(); i++) {
            if (!mSubServices.get(i).getDiscountPrice().equals("0")) {
                disCountMaxList.add(Double.valueOf(discount_per(mSubServices.get(i).getPrice(), mSubServices.get(i).getDiscountPrice())));
                pricetot = String.valueOf(Collections.max(disCountMaxList));
            }
        }
        return pricetot;
    }


    private String discount_per(String price, String disprice) {
        Float intprice = Float.valueOf(price);
        Float intdiscprcie = Float.valueOf(disprice);
        Float per = ((intprice - intdiscprcie) * 100 / intprice);
        int result2 = Math.round(per);
        String disc = "0";
        return disc = String.valueOf(result2);

    }

/*
    public List<SalonSubServiceModel> getValueListData(Context context)
    {
        RealmController realmController = new RealmController(context);
        List<SalonSubServiceModel> salonSubServiceModel = new ArrayList<>();
        for (int i = 0;i<mSubServices.size();i++)
        {
            salonSubServiceModel.add(realmController.getSalonSubServiceModelmId(mSubServices.get(i).getId()));

        }
        return salonSubServiceModel;
    }
*/

    @Override
    public String toString() {
        return "SalonAllServiceModel{" +
                "mCategoryName='" + mCategoryName + '\'' +
                ", mCount='" + mCount + '\'' +
                ", mId='" + mId + '\'' +
                ", mIsOpen='" + mIsOpen + '\'' +
                ", mStartPrice='" + mStartPrice + '\'' +
                ", mSubServices=" + mSubServices +
                ", mSubid='" + mSubid + '\'' +
                '}';
    }
}
