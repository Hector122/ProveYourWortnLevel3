package com.android.proveyourworth.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.android.proveyourworth.R;
import com.android.proveyourworth.ui.WebViewActivity;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mSummitButton;
    private TextInputEditText mUsername;

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
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_summit) {
            Intent intent = new Intent(this, WebViewActivity.class);
            startActivity(intent);
        }
    }
}
