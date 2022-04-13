package com.example.less30;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final private int COLOR = 1;
    final private int GRAVITY = 2;

    private TextView textView;
    Button btnColor, btnGravity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);


        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        btnColor = findViewById(R.id.btnColor);
        btnGravity = findViewById(R.id.btnGravity);
        btnColor.setOnClickListener(this);
        btnGravity.setOnClickListener(this);

        DrawerLayout dr = findViewById(R.id.dr);
        dr.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);





        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        decorView.setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnColor:
                intent = new Intent(this, activity_color.class);
                startActivityForResult(intent, COLOR);
                break;
            case R.id.btnGravity:
                intent = new Intent(this, activity_Gravity.class);
                startActivityForResult(intent, GRAVITY);
                break;
        }
    }

    @SuppressLint("RtlHardcoded")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case COLOR:
                    textView.setTextColor(data.getIntExtra("color", Color.WHITE));
                    break;
                case GRAVITY:
                    textView.setGravity(data.getIntExtra("gravity", Gravity.LEFT));
                    break;
            }

        }
        if (resultCode == RESULT_CANCELED) {
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);
            Toast.makeText(MainActivity.this, "Any button", Toast.LENGTH_SHORT).show();
        }
    }
}
