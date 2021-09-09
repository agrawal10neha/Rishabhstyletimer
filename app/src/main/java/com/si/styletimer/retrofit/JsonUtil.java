package com.si.styletimer.retrofit;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class JsonUtil {
    private static final String TAG = "JsonUtil";
    public static String STATUS = "status";
    public static String MESSAGE = "response_message";

    public static String jsonstatus(Context context, String response) {
        String stat = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            Toast.makeText(context, jsonObject.getString(MESSAGE), Toast.LENGTH_SHORT).show();
            if (jsonObject.getInt(STATUS) == 0) {
                return stat = "0";
            }

            if (jsonObject.getInt(STATUS) == 1) {
                return stat = "1";
            }

            if (jsonObject.getInt(STATUS) == 2) {
                return stat = "2";
            }

            if (jsonObject.getInt(STATUS) == 3) {
                return stat = "3";
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return stat ="error";
        }
        return stat;
    }

    public static boolean mainresp(Context context, Response<ResponseBody> response){
        Log.e(TAG, "mainresp: "+response.code()+" bollena "+ response.isSuccessful());
      if (response.isSuccessful()) {
        return true;
        // todo display the data instead of just a toast
      } else {
        // error case
        switch (response.code()) {
          case 404:
            Toast.makeText(context, "Url not found", Toast.LENGTH_SHORT).show();
            return false;

//          Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
//            break;
          case 500:
            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
            return false;
//            break;
          case 504:
            Toast.makeText(context, "server broken", Toast.LENGTH_SHORT).show();
            return false;
//            break;
          default:
            Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();

            return false;
//            break;
        }
      }


    }

    public static JSONObject mainjson(Response<ResponseBody> response){
      JSONObject jsonObject = null;
      try {
        String resp =  response.body().string().trim();
        jsonObject = new JSONObject(resp);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return  jsonObject;
    }

    public static String resp(Response<ResponseBody> response){
        String res = "";
        try {
            res =  response.body().string().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  res;
    }


//    public static List<> listfromjson(Class<?> cls, String data) {
//        Gson gson = new Gson();
//        List<cls>  clsArrayList = new ArrayList<>();
//        gson.fromJson(data, new TypeToken<ArrayList<cls>>() {}.getType());
//        return  clsArrayList;
//    }

}
