
package com.si.styletimer.models.venu_model;

import com.google.gson.annotations.SerializedName;

public class VenueModel {

    @SerializedName("endtime")
    private String mEndtime;
    @SerializedName("id")
    private String mId;
    @SerializedName("starttime")
    private String mStarttime;

    public String getEndtime() {
        return mEndtime;
    }

    public void setEndtime(String endtime) {
        mEndtime = endtime;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getStarttime() {
        return mStarttime;
    }

    public void setStarttime(String starttime) {
        mStarttime = starttime;
    }

}
