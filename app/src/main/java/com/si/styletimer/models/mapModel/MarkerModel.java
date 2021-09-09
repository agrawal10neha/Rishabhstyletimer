
package com.si.styletimer.models.mapModel;

import com.google.gson.annotations.Expose;

public class MarkerModel {

    @Expose
    private String id;
    @Expose
    private String latitude;
    @Expose
    private String longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
