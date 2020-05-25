package com.android.proveyourworth.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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
    private static final String STATEFUL_HASH = "statefulhash";
    private static final String VALUE_EQUAL = "value=";
    private static final String REGEX = "\"([^\"]*)\"";


    /**
     * TODO: move to utils class
     * Converts the contents of an InputStream to a String.
     */
    public static String getHashFromInputStream(InputStream stream)
            throws IOException, UnsupportedEncodingException {

        String hashToken = null;

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
                    hashToken = matcher.group(1);
                    break;
                }
            }
        }
        return hashToken;
    }


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

        String name_ = "foldername"; //Folder name in device android/data/
        File directory = cw.getDir(name_, Context.MODE_PRIVATE);

        // Create imageDir
        File mypath = new File(directory, name);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("absolutepath ", directory.getAbsolutePath());
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
        Bitmap b;
        String name_ = "foldername";
        try {
            File f = new File(path, name_);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void saveImage(DataInputStream reader) throws FileNotFoundException, IOException {
        int byteRead = -1;
        DataOutputStream outFile = new DataOutputStream(new FileOutputStream(new File("/app/src/main/res/drawable/image_test.jpg")));

        while ((byteRead = reader.read()) != -1) {
            outFile.writeByte(byteRead);
        }
        outFile.flush();
        outFile.close();
    }
}
