package com.example.less30;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class activity_color extends AppCompatActivity implements View.OnClickListener {

    private Button btnRed, btnGreen, btnBlue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        btnRed = findViewById(R.id.btnRed);
        btnGreen = findViewById(R.id.btnGreen);
        btnBlue = findViewById(R.id.btnBlue);
        btnRed.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btnRed:
                int z = Integer.parseInt("zzz");
                intent.putExtra("color", Color.RED);
                setResult(RESULT_OK, intent);
                break;
            case R.id.btnBlue:
                intent.putExtra("color", Color.BLUE);
                setResult(RESULT_OK, intent);
                break;
            case R.id.btnGreen:
                intent.putExtra("color", Color.GREEN);
                setResult(RESULT_OK, intent);
                break;
        }
        finish();
    }
}
