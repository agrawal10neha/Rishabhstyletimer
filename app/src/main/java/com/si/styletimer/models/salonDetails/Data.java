
package com.si.styletimer.models.salonDetails;
import java.util.List;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Data  {

    @SerializedName("all_service")
    private List<SalonAllServiceModel> mAllService;
    @SerializedName("details")
    private List<SalonDetailModel> mDetails;
    @SerializedName("main_category")
    private List<SalonMainCategoryModel> mMainCategory;
    @SerializedName("matchcatsubcat")
    private List<SalonMatchcatsubcatModel> mMatchcatsubcat;

    public List<SalonAllServiceModel> getAllService() {
        return mAllService;
    }

    public void setAllService(List<SalonAllServiceModel> allService) {
        mAllService = allService;
    }

    public List<SalonDetailModel> getDetails() {
        return mDetails;
    }

    public void setDetails(List<SalonDetailModel> details) {
        mDetails = details;
    }

    public List<SalonMainCategoryModel> getMainCategory() {
        return mMainCategory;
    }

    public void setMainCategory(List<SalonMainCategoryModel> mainCategory) {
        mMainCategory = mainCategory;
    }

    public List<SalonMatchcatsubcatModel> getMatchcatsubcat() {
        return mMatchcatsubcat;
    }

    public void setMatchcatsubcat(List<SalonMatchcatsubcatModel> matchcatsubcat) {
        mMatchcatsubcat = matchcatsubcat;
    }

}
