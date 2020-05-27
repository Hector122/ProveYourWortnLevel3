package com.android.proveyourworth.ui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.proveyourworth.R;
import com.android.proveyourworth.repository.ServiceClient;
import com.android.proveyourworth.util.Util;

import java.io.FileInputStream;

/**
 * Main activity that handler the ui.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mSummitButton;
    private ImageView mImageView;
    private View mContainerView;
    private ProgressDialog mDialog;
    private ServiceClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializer();
    }

    /**
     * Initializer the principal variables of this class.
     */
    private void initializer() {
        mContainerView = findViewById(R.id.container);

        mSummitButton = findViewById(R.id.btn_summit);
        mSummitButton.setOnClickListener(this);

        mImageView = findViewById(R.id.img_load);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getString(R.string.loading)); // Setting Message
    }

    /**
     * Start background services to show in view.
     */
    private void startServiceIntent() {
        mDialog.show();

        final HandlerThread handlerThread = new HandlerThread(getString(R.string.thread_name));
        handlerThread.start();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mClient = new ServiceClient(MainActivity.this);
                mClient.starBackgroundTasks();

                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(getImageFromInternalStorage());
                        mImageView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                        mDialog.dismiss();


                    }
                });
            }
        };

        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(runnable);
    }

    /**
     * Get image bitmap from internal storage.
     *
     * @return Bitmap.
     */
    private Bitmap getImageFromInternalStorage() {
        FileInputStream fileInputStream = Util.loadImageFromStorage(Util.PATH_IMAGE, Util.IMAGE_NAME);
        return BitmapFactory.decodeStream(fileInputStream);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_summit) {
            startServiceIntent();
        }
    }
}
