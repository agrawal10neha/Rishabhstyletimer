package com.si.styletimer.retrofit;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by anupamchugh on 29/03/17.
 */

public interface ApiInterface {
   /* @GET("place/autocomplete/json?")
    Call<ResponseBody> doPlaces(@Query(value = "type", encoded = true) String type, @Query(value = "location", encoded = true) String location, @Query(value = "name", encoded = true) String name, @Query(value = "opennow", encoded = true) boolean opennow, @Query(value = "rankby", encoded = true) String rankby, @Query(value = "key", encoded = true) String key);*/

    /*@GET("place/autocomplete/json?")
    Call<ResponseBody> doPlaces( @QueryMap Map<String, String> options);
      @GET("https://eu1.locationiq.com/v1/search.php?")*/

    @GET("https://api.locationiq.com/v1/autocomplete.php?")
    Call<ResponseBody> doPlaces( @QueryMap Map<String, String> options);

   // ?key=%3CYour_API_Access_Token%3E&q=wetzlar&limit=5

//    @GET("https://eu1.locationiq.com/v1/search.php?key=5eaae41f648ada&q={q}&format=json")
//    Call<ResponseBody> doPlaces( @Query("q") String search);
}
