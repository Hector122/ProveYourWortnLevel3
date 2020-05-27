package com.android.proveyourworth.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.android.proveyourworth.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Util class to handle the I/O method needed.
 */
public class Util {
    public static final String PATH_IMAGE = "/data/user/0/com.android.proveyourworth/app_payload";
    public static final String IMAGE_NAME = "ic_image.jpg";
    public static final String RESUME_NAME = "resume.pdf";
    public static final String CODE_NAME = "code.txt";

    /**
     * Save image into device.
     *
     * @param bitmapImage image to save.
     * @param context     application context.
     * @param name        file name eje: test.jpg
     * @return String with the absolute path.
     */
    public static String saveToInternalStorage(Bitmap bitmapImage, Context context, String name) {
        ContextWrapper cw = new ContextWrapper(context);

        String name_ = "payload"; //Folder name in device android/data/
        File directory = cw.getDir(name_, Context.MODE_PRIVATE);

        // Create imageDir
        File path = new File(directory, name);

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(path);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("absolute path:", directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }

    /**
     * Method to retrieve image from your device
     *
     * @param path file dir path
     * @param name file name eje: test.jpg
     * @return bitmap of the file
     */
    public static FileInputStream loadImageFromStorage(String path, String name) {
        try {
            File file = new File(path, name);

            Log.i("absolute path:", file.getAbsolutePath());
            return new FileInputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Draw bitmap data in the bitmap image need.
     *
     * @param context   Application Context.
     * @param bitmap    Bitmap image.
     * @param hashToken hash access token.
     *
     * @return Bitmap with draw information.
     */
    public static Bitmap drawTextOnBitmap(Context context, Bitmap bitmap, String hashToken) {
        Bitmap copyBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(copyBitmap);

        // Paint object to style the drawing
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(context.getResources().getColor(android.R.color.holo_purple)); // Text Color
        paint.setTextSize(context.getResources().getDimension(R.dimen.text_size));

        canvas.drawText("Name:" + context.getResources().getString(R.string.name), -0, copyBitmap.getHeight() - 400, paint);
        canvas.drawText("Token: " + hashToken, 0, copyBitmap.getHeight() - 300, paint);
        canvas.drawText("Email: " + context.getResources().getString(R.string.email), 0, copyBitmap.getHeight() - 200, paint);

        return copyBitmap;
    }
}
