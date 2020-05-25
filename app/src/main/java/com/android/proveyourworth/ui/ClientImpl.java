package com.android.proveyourworth.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.android.proveyourworth.reposiroty.DataServices;
import com.android.proveyourworth.reposiroty.HttpClientInstance;
import com.android.proveyourworth.util.Util;

import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ClientImpl {
    private static final String TAG = "RESPONSE:";
    private static final String SET_COOKIES = "Set-Cookie";


    private String mSessionId, mHashToken;
    private Context mContext;
    private DataServices mService;

    public ClientImpl(Context context) {
        this.mContext = context;
        this.mService = HttpClientInstance.getInstance().create(DataServices.class);
    }

    /***
     *
     */
    public void start() {
        Call<ResponseBody> call = mService.getStartPageContent();

        // print headers
        Log.i(TAG, call.request().url().toString());

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                //Get session id
                for (String name : response.headers().names()) {
                    //----- Print
                    Log.i(TAG, name + " " + response.headers().values(name));

                    if (name.equals(SET_COOKIES)) {
                        mSessionId = response.headers().values(name).toString();
                        //----- Print
                        Log.i(TAG, "session_id: " + mSessionId);
                    }
                }

                try {
                    //get hash token
                    mHashToken = Util.getHashFromInputStream(response.body().byteStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    // Do nothing.
                }

                //Activate
                mService.getActivatePageContent(mHashToken);
                Log.i(TAG, mHashToken);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

        //  activate();

        // -------------------
    }

//    public void activate() {
//        //TODO: token session
//        Call<ResponseBody> response = mService.getActivatePageContent(mHashToken);
//        Log.i(TAG, mHashToken);
//
//        response.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.i(TAG, response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//    }

    /**
     *
     */
    public void getImagePayload(final ImageView imageView) {

        Call<ResponseBody> call = mService.getPayload();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStream inputStream = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


//                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
//                    Util.saveToInternalStorage(bitmap, mContext, "ic_image.jpg");
//
//                    imageView.setImageBitmap(Util.loadImageFromStorage(
//                            "foldername", "ic_image.jpg"));


//                    Bitmap theBitmap = null;
//                    theBitmap = Glide.with(mContext)
//                            .asBitmap()
//                            .load("http://www.proveyourworth.net/level3/payload")
//                            .submit().get();
//
//                    Glide.with(mContext)
//                            .load(Uri.parse("http://www.proveyourworth.net/level3/payload"))
//                            .into(imageView);

//                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
//                    Bitmap bitmap = drawable.getBitmap();

//                    imageView
//                    imageView.setImageBitmap(Util.loadImageFromStorage("foldername", "ic_image.jpg"));
    }


    private void summit() {
        Call<ResponseBody> response = mService.postReaper(mSessionId);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
