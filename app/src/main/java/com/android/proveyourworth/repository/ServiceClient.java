package com.android.proveyourworth.repository;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.android.proveyourworth.R;
import com.android.proveyourworth.util.Util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class ServiceClient {
    private static final String SET_COOKIES = "Set-Cookie";
    private static final String STATEFUL_HASH = "statefulhash";
    private static final String VALUE_EQUAL = "value=";
    private static final String REGEX = "\"([^\"]*)\"";

    private static final String TAG = "RESPONSE:";
    private static final String TAG_ERROR = "error: ";
    private static final String TAG_PAYLOAD_URL = "X-Payload-URL";
    private static final String TAG_SESSION_ID = "session_id: ";
    private static final String TAG_HASH_TOKEN = "Hash_token:";
    private static final String TAG_BODY = "Body: ";

    private String mSessionId, mHashToken;
    private Context mContext;
    private TaskService mService;

    /**
     * Constructor.
     *
     * @param context Application Context.
     */
    public ServiceClient(Context context) {
        this.mContext = context;
        this.mService = ServiceGenerator.createService(TaskService.class);
    }

    /**
     * Initiated the async task to get the image payload.
     */
    public void starBackgroundTasks() {
        try {
            start();

            activateSession();

            payloadImageContent();

            summit();

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, TAG_ERROR + ex.getMessage());
            showTextInDialog(mContext.getString(R.string.error_request));
        }
    }

    /**
     * @throws IOException
     */
    private void start() throws IOException {
        Call<ResponseBody> call = mService.getStartPageContent();
        Response<ResponseBody> response = call.execute();

        //Get session id
        mSessionId = response.headers().get(SET_COOKIES);
        Log.i(TAG, TAG_SESSION_ID + mSessionId);

        //get and set hash token
        String body = readStream(response.body().byteStream());
        Log.i(TAG, TAG_HASH_TOKEN + mHashToken);
        Log.i(TAG, TAG_BODY + body);
    }

    /**
     * @throws IOException
     */
    private void activateSession() throws IOException {
        Call<ResponseBody> call = mService.getActivatePageContent(mSessionId, mHashToken);
        Response<ResponseBody> response = call.execute();

        Log.i(TAG, call.request().url().toString());

        String urlPayload = response.headers().get(TAG_PAYLOAD_URL);
        Log.i(TAG, TAG_PAYLOAD_URL + " : " + urlPayload);
    }

    /**
     * @throws IOException
     */
    private void payloadImageContent() throws IOException {
        Call<ResponseBody> call = mService.getPayload(mSessionId);
        Response<ResponseBody> response = call.execute();

        // X-Oh-Look A new session cookie...
        mSessionId = response.headers().get(SET_COOKIES);
        Log.i(TAG, TAG_SESSION_ID + mSessionId);

        Log.i(TAG, call.request().url().toString());

        InputStream inputStream = response.body().byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        Log.i(TAG, "------------- Big image come: " + bitmap.getByteCount());

        bitmap = Util.drawTextOnBitmap(mContext, bitmap, mHashToken);

        Util.saveToInternalStorage(bitmap, mContext, Util.IMAGE_NAME);

        createExternalStoragePrivateFile(Util.RESUME_NAME, null);
    }

    /**
     * POST the image and the resume data.
     */
    public void summit() {
        //add Image
        File imageFile = new File(Util.PATH_IMAGE, Util.IMAGE_NAME);
        MultipartBody.Part image =
                MultipartBody.Part.createFormData("image", imageFile.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), imageFile));

        //Resume
        File resumeFile = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), Util.RESUME_NAME);
        MultipartBody.Part resume = MultipartBody.Part.createFormData("resume", resumeFile.getName(),
                RequestBody.create(MediaType.parse("application/pdf"), resumeFile));

        //add code
        createExternalStoragePrivateFile(Util.CODE_NAME,
                new DataInputStream(mContext.getResources().openRawResource(R.raw.code)));

        File codeFile = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), Util.CODE_NAME);
        MultipartBody.Part code = MultipartBody.Part.createFormData("code", codeFile.getName(),
                RequestBody.create(MediaType.parse("application/java"), codeFile));

        //add email, name, about
        MultipartBody.Part email =
                MultipartBody.Part.createFormData("email", mContext.getString(R.string.email));

        MultipartBody.Part name = MultipartBody.Part.createFormData("name",
                mContext.getString(R.string.name));

        MultipartBody.Part about =
                MultipartBody.Part.createFormData("about", mContext.getString(R.string.about));


        Call<ResponseBody> call = mService.postReaperEEE(mSessionId, image, resume, code, email, name, about);

        Log.i(TAG, call.request().url().toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("Code: ", String.valueOf(response.code()));
                showTextInDialog("Done! ");
                try {
                    String body = readStream(response.body().byteStream());
                    Log.i(TAG, TAG_HASH_TOKEN + mHashToken);
                    Log.i(TAG, TAG_BODY + body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Code: ", t.getMessage());
                showTextInDialog(t.getMessage());
            }
        });
    }

    /**
     * Show text Dialog.
     *
     * @param text String text to show.
     */
    private void showTextInDialog(String text) {
        // Use dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("Prove you worth");

        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Continue with delete operation
                dialog.dismiss();
            }
        });
        builder.setMessage(text).show();
    }

    /**
     * Converts the contents of an InputStream to a String.
     */
    private String readStream(InputStream stream) throws IOException {
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

    /**
     * White PDF file into external storage.
     */
    void createExternalStoragePrivateFile(String fileName, InputStream inputStream) {
        // Create a path where we will place our private file on external storage.
        File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);

        try {
            // Very simple code.txt to copy a picture from the application's
            // resource into the external file. Note that if external storage is
            // not currently mounted this will silently fail.
            InputStream is = (inputStream != null) ? inputStream : mContext.getResources()
                    .openRawResource(R.raw.resume);
            OutputStream os = new FileOutputStream(file);
            byte[] data = new byte[is.available()];
            is.read(data);
            os.write(data);
            is.close();
            os.close();
        } catch (IOException e) {
            // Unable to create file, likely because external storage is
            // not currently mounted.
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
    }
}
