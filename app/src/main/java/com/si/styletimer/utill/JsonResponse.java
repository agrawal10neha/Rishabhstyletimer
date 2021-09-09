package com.si.styletimer.utill;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonResponse {
    private static final String STATUS = "status";
    private static final String RESPONSE_MESSAGE = "response_message";

    public static String status(String response) {
        String respo = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString(STATUS).equals("1")){
                respo = "1";
            }else {
                respo = "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  respo;
    }
}
