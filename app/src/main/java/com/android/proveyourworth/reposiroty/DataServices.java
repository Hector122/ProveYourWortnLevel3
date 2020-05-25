package com.android.proveyourworth.reposiroty;

import androidx.annotation.ContentView;

import java.io.InputStream;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DataServices {
    @GET("start")
    Call<ResponseBody> getStartPageContent();

    @GET("activate?statefulhash=")
    Call<ResponseBody> getActivatePageContent(@Query("token") String token);

    @GET("payload")
    Call<ResponseBody> getPayload();

    @POST("reaper")
    Call<ResponseBody> postReaper(@Header("Cookie") String sessionId);
}
