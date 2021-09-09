package com.si.styletimer.retrofit;


import com.si.styletimer.BuildConfig;

public class Environment {
    /**
     * change the environment of the application
     * for Development it is false
     * for Staging it is true
     * */
    public static boolean environment = true;

    public static String getBaseUrl(){
        String baseurl = "";
        if (environment){
            return baseurl = "https://dev.styletimer.de/";
        }else {
            //return baseurl = "http://34.223.228.22/styletimer/";
            return baseurl = "http://78.46.210.25/styletimer/";
        }
    }

    public static String getVerionName(){
        String version = "";

        if (environment){
            return  version = "Version : S :- "+ BuildConfig.VERSION_NAME;
        }else {
            return  version = "Version : D :- "+ BuildConfig.VERSION_NAME;
        }
    }
}

