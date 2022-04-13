package com.example.less30;

import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

public class activity_Gravity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLeft, btnCenter, btnRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__gravity);

        btnLeft = findViewById(R.id.btnLeft);
        btnCenter = findViewById(R.id.btnCenter);
        btnRight = findViewById(R.id.btnRight);
        btnLeft.setOnClickListener(this);
        btnCenter.setOnClickListener(this);
        btnRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btnLeft:
                intent.putExtra("gravity", Gravity.START);
                setResult(RESULT_OK, intent);
                break;
            case R.id.btnCenter:
                intent.putExtra("gravity", Gravity.CENTER);
                setResult(RESULT_OK, intent);
                break;
            case R.id.btnRight:
                intent.putExtra("gravity", Gravity.END);
                setResult(RESULT_OK, intent);
                break;
        }
        finish();
    }
}