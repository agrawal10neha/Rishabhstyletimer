package com.si.styletimer.models;

/**
 * Developed by : Kamal Patidar
 * Author : Shanti Infotech Pvt. Ltd.
 * Email : kamal.shantiinfotech@gmail.com
 * Website : https://shantiinfotech.com/
 * Created on : 25,January,2021
 */
public class FilterModel {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
