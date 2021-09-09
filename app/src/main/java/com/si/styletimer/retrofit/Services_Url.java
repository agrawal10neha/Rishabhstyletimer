package com.si.styletimer.retrofit;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Services_Url {

    @FormUrlEncoded
    @POST("generate_access_token")
    Call<ResponseBody> generate_access_token (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("user_registration")
    Call<ResponseBody> user_registration_token (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("user_login")
    Call<ResponseBody> user_login (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("socialLogin")
    Call<ResponseBody> socialLogin (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<ResponseBody> forgot_password (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("category")
    Call<ResponseBody> category (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("logout")
    Call<ResponseBody> logout (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("update_newslatter")
    Call<ResponseBody> update_newslatter (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("update_salonenewslatter")
    Call<ResponseBody> update_salonenewslatter (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("change_password")
    Call<ResponseBody> change_password (@FieldMap HashMap<String, String> hashMap);

    @POST("user_profile")
    Call<ResponseBody> user_profile (@Body RequestBody files);

    @FormUrlEncoded
    @POST("salon_listing")
    Call<ResponseBody> salon_listing (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("map_marker")
    Call<ResponseBody> map_marker (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("subcategory_list")
    Call<ResponseBody> subcategory_list (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("category_subcategory_search")
    Call<ResponseBody> category_subcategory_search (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("salon_profile_detail")
    Call<ResponseBody> salon_profile_detail (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("booking_listing")
    Call<ResponseBody> booking_listing (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("booking_detail")
    Call<ResponseBody> booking_detail (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("booking_cancel")
    Call<ResponseBody> booking_cancel (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("salon_profile")
    Call<ResponseBody> salon_profile (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("salon_profile_page")
    Call<ResponseBody> salon_profile_page  (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("add_booking")
    Call<ResponseBody> add_booking (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("category_allservice")
    Call<ResponseBody> category_allservice (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("book_employee")
    Call<ResponseBody> book_employee (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("employee_time")
    Call<ResponseBody> employee_time (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("book_detailforconfirm")
    Call<ResponseBody> book_detailforconfirm (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("booking_confirm")
    Call<ResponseBody> booking_confirm (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("addreview_service")
    Call<ResponseBody> addreview_service (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("review_listing")
    Call<ResponseBody> review_listing (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("remove_service")
    Call<ResponseBody> remove_service (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("rebook_service")
    Call<ResponseBody> rebook_service (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("salon_mapdetail")
    Call<ResponseBody> salon_mapdetail (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("total_service_count")
    Call<ResponseBody> total_service_count (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("clear_cart")
    Call<ResponseBody> clear_cart (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("service_forratefilter")
    Call<ResponseBody> service_forratefilter (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("favourite_salon")
    Call<ResponseBody> favourite_salon (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("review_detail")
    Call<ResponseBody> review_detail (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("allsalon_opentime")
    Call<ResponseBody> allsalon_opentime (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("booking_receipt_new")
    Call<ResponseBody> booking_receipt (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("notification_status")
    Call<ResponseBody> notification_status (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("remove_profilepic")
    Call<ResponseBody> remove_profilepic (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("myfavourite_salon")
    Call<ResponseBody> myfavourite_salon (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("salon_list")
    Call<ResponseBody> salon_list (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("booking_reshedule")
    Call<ResponseBody> booking_reshedule (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("employee_reschedule_time")
    Call<ResponseBody> employee_reschedule_time (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("contact_for_salon")
    Call<ResponseBody> contact_for_salon (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("getsalon_servicedetail")
    Call<ResponseBody> getsalon_servicedetail (@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("salon_calendar_slots")
    Call<ResponseBody> salon_calendar_slots(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("send_otp_for_delete_account")
    Call<ResponseBody> send_otp_for_delete_account(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("update_app_review_status")
    Call<ResponseBody> update_app_review_status(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("delete_account")
    Call<ResponseBody> delete_account(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("api/siteverify")
    Call<ResponseBody> siteverify(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("resend_activation_mail")
    Call<ResponseBody> resend_activation_mail(@FieldMap HashMap<String, String> hashMap);

}
