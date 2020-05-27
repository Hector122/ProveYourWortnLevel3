package com.android.proveyourworth.repository;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Init okHttpClient
 */
public class ServiceGenerator {
    private static String BASE_URL = "https://www.proveyourworth.net/level3/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .writeTimeout(10, TimeUnit.SECONDS);

    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL);

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
