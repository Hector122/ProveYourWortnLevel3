package com.android.proveyourworth.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.proveyourworth.R;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mSummitButton;
    private TextInputEditText mUsername;
    private ImageView mImageView;

    private TextView textViewTest;


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
        mSummitButton = findViewById(R.id.btn_summit);
        mSummitButton.setOnClickListener(this);

        mUsername = findViewById(R.id.txt_username);

        mImageView = findViewById(R.id.img_load);

        textViewTest = findViewById(R.id.txt_test);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_summit) {
            ClientImpl client = new ClientImpl(this);
            client.start();
            client.getImagePayload(mImageView);


//            Intent intent = new Intent(this, WebViewActivity.class);
//            startActivity(intent);
        }
    }
}
