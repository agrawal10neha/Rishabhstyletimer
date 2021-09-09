
package com.si.styletimer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentMethodModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("method_name")
    @Expose
    private String methodName;
    @SerializedName("defualt")
    @Expose
    private String defualt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDefualt() {
        return defualt;
    }

    public void setDefualt(String defualt) {
        this.defualt = defualt;
    }

}
