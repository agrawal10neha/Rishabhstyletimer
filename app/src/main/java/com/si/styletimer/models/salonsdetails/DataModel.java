
package com.si.styletimer.models.salonsdetails;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class DataModel {

    @SerializedName("all_service")
    private List<AllServiceModel> mAllServiceModel;
    @SerializedName("details")
    private List<DetailModel> mDetails;
    @SerializedName("main_category")
    private List<MainCategoryModel> mMainCategory;
    @SerializedName("match_subcat")
    private List<MatchSubcatModel> mMatchSubcat;

    public List<AllServiceModel> getAllService() {
        return mAllServiceModel;
    }

    public void setAllService(List<AllServiceModel> allServiceModel) {
        mAllServiceModel = allServiceModel;
    }

    public List<DetailModel> getDetails() {
        return mDetails;
    }

    public void setDetails(List<DetailModel> details) {
        mDetails = details;
    }

    public List<MainCategoryModel> getMainCategory() {
        return mMainCategory;
    }

    public void setMainCategory(List<MainCategoryModel> mainCategory) {
        mMainCategory = mainCategory;
    }

    public List<MatchSubcatModel> getMatchSubcat() {
        return mMatchSubcat;
    }

    public void setMatchSubcat(List<MatchSubcatModel> matchSubcat) {
        mMatchSubcat = matchSubcat;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "mAllServiceModel=" + mAllServiceModel +
                ", mDetails=" + mDetails +
                ", mMainCategory=" + mMainCategory +
                ", mMatchSubcat=" + mMatchSubcat +
                '}';
    }
}
