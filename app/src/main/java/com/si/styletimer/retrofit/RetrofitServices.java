package com.si.styletimer.retrofit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServices {

    public static final String BASE = Environment.getBaseUrl();
    public static final String BASE_URL = BASE+"api/";
    public static final String UPLOADS = BASE+"assets/uploads/";
    public static final String MENU_IMAGE = UPLOADS+"category/";
    public static final String MENU_IMAGE_ICON = UPLOADS+"category_icon/";
    public static final String BANNERS = UPLOADS+"banners/";
    public static final String USERS_IMAGE = UPLOADS+"users/";
    public static final String USER_POLICY = BASE+"home/appstatic/userpolicy";
    public static final String USER_TERM = BASE+"home/appstatic/userterms";
    public static final String USER_INPRINT = BASE+"home/appstatic/imprint";

    public static OkHttpClient getinfo() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .addInterceptor(logging)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        // todo deal with the issues the way you need to
                        if (response.code() == 500) {
                            throw new GenericException(new InternalServerError());
                        }

                        if (response.code() == 401) {
                            return response;
                        }

                        return response;
                    }
                })
                .build();
        return okHttpClient;
    }


    public static final Services_Url urlServices = new Retrofit.Builder()
            .baseUrl(BASE_URL+"api/").addConverterFactory(GsonConverterFactory.create())
            .client(getinfo()).build().create(Services_Url.class);

    public static final Services_Url urlServi = new Retrofit.Builder()
            .baseUrl(BASE_URL+"api2/").addConverterFactory(GsonConverterFactory.create())
            .client(getinfo()).build().create(Services_Url.class);
    public static final Services_Url urlServiReCapcha = new Retrofit.Builder()
            .baseUrl("https://www.google.com/recaptcha/").addConverterFactory(GsonConverterFactory.create())
            .client(getinfo()).build().create(Services_Url.class);

}
