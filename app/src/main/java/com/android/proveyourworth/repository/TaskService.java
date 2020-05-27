package com.android.proveyourworth.repository;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Interface that implement the method need for the test.
 */
public interface TaskService {
    @GET("start")
    Call<ResponseBody> getStartPageContent();

    @GET("activate?")
    Call<ResponseBody> getActivatePageContent(@Header("Cookie") String sessionId,
                                              @Query("statefulhash=") String hashToken);

    @Streaming
    @GET("payload")
    Call<ResponseBody> getPayload(@Header("Cookie") String sessionId);

    @Multipart
    @POST("reaper")
    Call<ResponseBody> postReaper(@Header("Cookie") String sessionId,
                                  @Part MultipartBody.Part image,
                                  @PartMap Map<String, RequestBody> partMap);
}
