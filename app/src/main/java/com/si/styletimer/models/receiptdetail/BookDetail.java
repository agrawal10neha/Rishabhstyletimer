
package com.si.styletimer.models.receiptdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookDetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("setuptime_start")
    @Expose
    private String setuptimeStart;
    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("discount_price")
    @Expose
    private String discountPrice;
    @SerializedName("service_id")
    @Expose
    private String serviceId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getSetuptimeStart() {
        return setuptimeStart;
    }

    public void setSetuptimeStart(String setuptimeStart) {
        this.setuptimeStart = setuptimeStart;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

}
