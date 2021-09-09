
package com.si.styletimer.models.cartcount;

import com.google.gson.annotations.SerializedName;


public class CartCountData {

    @SerializedName("total_service")
    private String mTotalService;

    @SerializedName("user_block")
    private String mBlock;

    public String getTotalService() {
        return mTotalService;
    }
    public void setTotalService(String totalService) {
        mTotalService = totalService;
    }

    public String getBlock() {
        return mBlock;
    }
    public void setBlock(String block) {
        mBlock = block;
    }

    @Override
    public String toString() {
        return "CartCountData{" +
                "mTotalService='" + mTotalService + '\'' +
                '}';
    }
}
