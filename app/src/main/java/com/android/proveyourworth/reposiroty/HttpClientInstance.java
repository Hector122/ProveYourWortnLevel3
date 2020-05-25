package com.android.proveyourworth.reposiroty;

import retrofit2.Retrofit;

public class HttpClientInstance {

    private static String BASE_URL = "https://www.proveyourworth.net/level3/";

    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).build();
        }

        return retrofit;
    }
}
