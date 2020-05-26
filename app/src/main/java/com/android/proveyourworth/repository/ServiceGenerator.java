package com.android.proveyourworth.repository;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class ServiceGenerator {
    private static String BASE_URL = "https://www.proveyourworth.net/level3/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL);

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
   }
}
