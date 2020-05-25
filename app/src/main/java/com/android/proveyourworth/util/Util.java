package com.android.proveyourworth.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.android.proveyourworth.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Util class to handle the I/O method needed.
 */
public class Util {

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
    public static Bitmap loadImageFromStorage(String path, String name) {
        Bitmap bitmap;
        path = "/data/user/0/com.android.proveyourworth/app_payload";
        try {
            File file = new File(path, name);

            Log.i("absolute path:", file.getAbsolutePath());

            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            return bitmap;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Bitmap drawTextOnBitmap(Context context, ImageView imageView, String... text) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_image_for_page).copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(bitmap);

        // Paint object to style the drawing
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(context.getResources().getColor(android.R.color.holo_purple)); // Text Color
        paint.setTextSize(context.getResources().getDimension(R.dimen.text_size));


        canvas.drawText("Nombre:" + "hector", -0, bitmap.getHeight() - 600, paint);
        canvas.drawText("Token: " + "98909898989", 0, bitmap.getHeight() - 400, paint);
        canvas.drawText("Email: " + "hector122@gmail.com", 0, bitmap.getHeight() - 200, paint);

        imageView.setImageBitmap(bitmap);

        return bitmap;
    }


    public void onDraw(Context context, ImageView imageView) {
        //Bitmap bitmap = Util.loadImageFromStorage("foldername", "ic_image.jpg");
        //bitmap.prepareToDraw();

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Util.drawTextOnBitmap(context, imageView, "esto es una prueba \n" + "Token:" + "asjkfjksafjkaskf");
    }
}
