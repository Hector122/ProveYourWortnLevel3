package com.android.proveyourworth.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.proveyourworth.R;
import com.android.proveyourworth.repository.ServiceClient;
import com.android.proveyourworth.util.Util;

import java.io.FileInputStream;

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

       startServiceIntent();
    }

    /**
     * Initializer the principal variables of this class.
     */
    private void initializer() {
        mContainerView = findViewById(R.id.container);
        mContainerView.setVisibility(View.INVISIBLE);

        mSummitButton = findViewById(R.id.btn_summit);
        mSummitButton.setOnClickListener(this);

        mImageView = findViewById(R.id.img_load);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getString(R.string.loading)); // Setting Message
        mDialog.show();
    }

    /**
     * Start background services to show in view.
     */
    private void startServiceIntent() {
        final HandlerThread handlerThread = new HandlerThread("Tony_Stark");
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
                        mContainerView.setVisibility(View.VISIBLE);

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
     * @return
     */
    private Bitmap getImageFromInternalStorage() {
        FileInputStream fileInputStream = Util.loadImageFromStorage(Util.PATH_IMAGE, Util.IMAGE_NAME);
        return BitmapFactory.decodeStream(fileInputStream);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_summit) {
            mClient.summit();
        }
    }
}
