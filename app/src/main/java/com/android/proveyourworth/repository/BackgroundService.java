package com.android.proveyourworth.repository;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.proveyourworth.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundService extends IntentService {
    private static final String STATEFUL_HASH = "statefulhash";
    private static final String VALUE_EQUAL = "value=";
    private static final String REGEX = "\"([^\"]*)\"";

    private static final String TAG = "RESPONSE:";
    private static final String SET_COOKIES = "Set-Cookie";

    private String mSessionId, mHashToken;
    private Context mContext;
    private TaskService mService;

    String urlPayload;

    public BackgroundService() {
        super("BackgroudService");
    }
//    public BackgroundService(Context context) {
//        this.mContext = context;
//        this.mService = ClientService.createService(TaskService.class);
//    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        this.mService = ClientService.createService(TaskService.class);
        tonyStark();
    }

    public void tonyStark() {
        try {
            startTonyStark();

            activateTonyStark();

            payloadTonyStark();

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "Error " + ex.getMessage());
        }
    }

    public void startTonyStark() throws IOException {
        Call<ResponseBody> call = mService.getStartPageContent();
        Response<ResponseBody> response = call.execute();

        //Get session id
        mSessionId = response.headers().get(SET_COOKIES).substring(0, 36);
        Log.i(TAG, "session_id: " + mSessionId);

        //get and set hash token
        String body = readStream(response.body().byteStream());
        Log.i(TAG, "Hash_token:" + mHashToken);
        Log.i(TAG, "Body: " + body);
    }


    public void activateTonyStark() throws IOException {
        Call<ResponseBody> call = mService.getActivatePageContent(mSessionId, mHashToken);
        Response<ResponseBody> response = call.execute();

        Log.i(TAG, call.request().url().toString());

        urlPayload = response.headers().get("X-Payload-URL");
        Log.i(TAG, "X-Payload-URL" + " : " + urlPayload);

        String body = readStream(response.body().byteStream());
        Log.i(TAG, "Body: " + body);
    }

    public void payloadTonyStark() throws IOException {
        Call<ResponseBody> call = mService.getPayload(mSessionId);
        Response<ResponseBody> response = call.execute();

        Log.i(TAG, call.request().url().toString());

        InputStream inputStream = response.body().byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        Log.i(TAG, "------------- Big image come: " + bitmap.getByteCount());

        Util.saveToInternalStorage(bitmap, getApplicationContext(), "ic_image.jpg");

        Bitmap bitmap1 = Util.loadImageFromStorage("","ic_image.jpg" );
        Log.i(TAG, "------------- GET form path: " + bitmap1.getByteCount());

//                imageView.setImageBitmap(Util.loadImageFromStorage(
//                        "foldername", "ic_image.jpg"));
    }

    private void summitTonyStark() {
        File file = new File("/storage/emulated/0/Download/Corrections 6.jpg");
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        Call<ResponseBody> call = mService.postReaper(mSessionId, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    /**
     * Converts the contents of an InputStream to a String.
     */
    public String readStream(InputStream stream) throws IOException {
        Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(reader);
        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = in.readLine()) != null) {
            builder.append(line);

            //Find the token hash
            if (line.contains(STATEFUL_HASH)) {
                String[] values = line.split(VALUE_EQUAL);

                Pattern pattern = Pattern.compile(REGEX);
                Matcher matcher = pattern.matcher(values[1]);

                if (matcher.find()) {
                    mHashToken = matcher.group(1);
                    break;
                }
            }
        }

        return builder.toString();
    }
}
