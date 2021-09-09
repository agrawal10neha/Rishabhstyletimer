
package com.si.styletimer.models;

import com.google.gson.annotations.SerializedName;

public class WeekCalendarModel {

    @SerializedName("dates")
    private String mDates;

    @SerializedName("date")
    private String mDate;

    private boolean isClosed = false;

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getDates() {
        return mDates;
    }

    public void setDates(String dates) {
        mDates = dates;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    @Override
    public String toString() {
        return "WeekCalendarModel{" +
                "mDates='" + mDates + '\'' +
                '}';
    }

    public String getDate(){
        String data[] = mDates.split(" ");
        return  data[2];
    }

    public String getDay(){
        String data[] = mDates.split(" ");
        return  data[0];
    }
}
