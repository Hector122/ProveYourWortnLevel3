package com.android.proveyourworth.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.proveyourworth.R;
import com.android.proveyourworth.repository.BackgroundService;
import com.android.proveyourworth.util.Util;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mSummitButton;
    private TextInputEditText mUsername;
    private ImageView mImageView;

    private BackgroundService mClient;

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
        mSummitButton = findViewById(R.id.btn_summit);
        mSummitButton.setOnClickListener(this);

        mUsername = findViewById(R.id.txt_username);

        mImageView = findViewById(R.id.img_load);
    }

    /**
     * Start background services to show in view.
     */
    private void startServiceIntent(){
        Intent intent = new Intent(this, BackgroundService.class);
        startService(intent);

        Util.drawTextOnBitmap(this, mImageView, "");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_summit) {

            //client.getImagePayload(mImageView);
            // client.onDraw(mImageView);

//            Intent intent = new Intent(this, WebViewActivity.class);
//            startActivity(intent);
        }
    }
}
