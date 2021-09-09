
package com.si.styletimer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChooseStyleModel {

    @SerializedName("first_name")
    private String firstName;
    @Expose
    private String id;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("profile_pic")
    private String profilePic;
    @SerializedName("rating")
    private String rating;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }


    @Override
    public String toString() {
        return "ChooseStyleModel{" +
                "firstName='" + firstName + '\'' +
                ", id='" + id + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePic='" + profilePic + '\'' +
                '}';
    }
}
