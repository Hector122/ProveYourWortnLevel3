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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    /**
     * Save image into divice.
     *
     * @param bitmapImage
     * @param context
     * @param name
     * @return
     */
    public static String saveToInternalStorage(Bitmap bitmapImage, Context context, String name) {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir

        String name_ = "payload"; //Folder name in device android/data/
        File directory = cw.getDir(name_, Context.MODE_PRIVATE);

        // Create imageDir
        File path = new File(directory, name);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(path);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("absolute path:", directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }

    /**
     * Method to retrieve image from your device
     *
     * @param path
     * @param name
     * @return
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


    public static void saveImage(DataInputStream reader) throws FileNotFoundException, IOException {
        int byteRead = -1;
        DataOutputStream outFile = new DataOutputStream(
                new FileOutputStream(new File("/app/src/main/res/drawable/image_test.jpg")));

        while ((byteRead = reader.read()) != -1) {
            outFile.writeByte(byteRead);
        }
        outFile.flush();
        outFile.close();
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


        canvas.drawText(text[0], -0, bitmap.getHeight() - 500, paint);

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
