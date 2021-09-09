
package com.si.styletimer.models.salonDetails;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;


public class SalonMatchcatsubcatModel {

    @SerializedName("subid")
    private String mSubId;
    @SerializedName("id")
    private String mId;
    @SerializedName("is_open")
    private Long mIsOpen;
    @SerializedName("key")
    private String mKey;
    @SerializedName("start_price")
    private String mStartPrice;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("value")
    private List<SalonValueModel> mValue;
    @SerializedName("isopen")
    private String isOpen;
    @SerializedName("service_detail")
    private String serviceDetail;
    @SerializedName("review_count")
    private String reviewcount;
    @SerializedName("service_detail_id")
    private String servicedetailid;
    @SerializedName("service_duration")
    private String serviceDuration;
    private List<Double> priceList = new ArrayList<>();
    private List<Double> priceList1 = new ArrayList<>();
    private List<Double> disCountMaxList = new ArrayList<>();

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

    public void setMyOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getMyOpen() {
        return isOpen;
    }

    public String getmSubId() {
        return mSubId;
    }

    public void setmSubId(String mSubId) {
        this.mSubId = mSubId;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public Long getIsOpen() {
        return mIsOpen;
    }

    public void setIsOpen(Long isOpen) {
        mIsOpen = isOpen;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getStartPrice() {
        return mStartPrice;
    }

    public void setStartPrice(String startPrice) {
        mStartPrice = startPrice;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    public List<SalonValueModel> getValue() {
        return mValue;
    }

    public void setValue(List<SalonValueModel> value) {
        mValue = value;
    }
    public String getMinPrice() {
        String pricetot = "";
        for (int i = 0; i < mValue.size(); i++) {

            priceList1.add(Double.valueOf(mValue.get(i).getPrice()));
            Log.e("array value", String.valueOf(priceList1));
            if (mValue.get(i).getPriceStartOption() != null && mValue.get(i).getPriceStartOption().equals("ab")) {
                pricetot = "ab " + String.valueOf(Collections.min(priceList1));
                //pricetot = "ab " +mValue.get(i).getPrice();
            }else {
                //  pricetot = "" +mValue.get(i).getPrice();
                pricetot = "" + String.valueOf(Collections.min(priceList1));
                Log.e("getMinPrice", "getMinPrice if  : " + pricetot );
            }

            /*if (mValue.get(i).getDiscountPrice().equals("0")) {
                priceList1.add(Double.valueOf(mValue.get(i).getPrice()));
                Log.e("array value", String.valueOf(priceList1));
                if (mValue.get(i).getPriceStartOption() != null && mValue.get(i).getPriceStartOption().equals("ab")) {
                    pricetot = "ab " + String.valueOf(Collections.min(priceList1));
                //pricetot = "ab " +mValue.get(i).getPrice();
                  }else {
              //  pricetot = "" +mValue.get(i).getPrice();
                    pricetot = "" + String.valueOf(Collections.min(priceList1));
                    Log.e("getMinPrice", "getMinPrice if  : " + pricetot );
                 }
                //   Log.e(TAG, "getMinPrice if  : " + pricetot );
            } else {
              //  priceList.add(Double.valueOf(mValue.get(i).getDiscountPrice()));
                priceList.add(Double.valueOf(mValue.get(i).getPrice()));
                if (mValue.get(i).getPriceStartOption() != null && mValue.get(i).getPriceStartOption().equals("ab")) {

                    pricetot = "ab " + String.valueOf(Collections.min(priceList));
                }else {
                    pricetot = String.valueOf(Collections.min(priceList));
                }
                // Log.e(TAG, "getMinPrice else : " + pricetot );
                //  Log.e(TAG, "getMinPrice else Collections.min(priceList) : " + Collections.min(priceList) );
            }*/
        }

        return pricetot;
    }

    public String getMaxDiscount() {
        String pricetot = "";
        for (int i = 0; i < mValue.size(); i++) {
            if (!mValue.get(i).getDiscountPrice().equals("0")) {
                disCountMaxList.add(Double.valueOf(discount_per(mValue.get(i).getPrice(), mValue.get(i).getDiscountPrice())));
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


    @Override
    public String toString() {
        return "SalonMatchcatsubcatModel{" +
                "mSubId='" + mSubId + '\'' +
                ", mId='" + mId + '\'' +
                ", mIsOpen=" + mIsOpen +
                ", mKey='" + mKey + '\'' +
                ", mStartPrice='" + mStartPrice + '\'' +
                ", mStatus=" + mStatus +
                ", mValue=" + mValue +
                '}';
    }
}
