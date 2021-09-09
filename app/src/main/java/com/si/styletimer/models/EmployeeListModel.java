
package com.si.styletimer.models;

import com.google.gson.annotations.SerializedName;

public class EmployeeListModel {

    @SerializedName("first_name")
    private String mFirstName;
    @SerializedName("id")
    private String mId;
    @SerializedName("last_name")
    private String mLastName;
    @SerializedName("profile_pic")
    private String mProfilePic;

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getProfilePic() {
        return mProfilePic;
    }

    public void setProfilePic(String profilePic) {
        mProfilePic = profilePic;
    }

}
