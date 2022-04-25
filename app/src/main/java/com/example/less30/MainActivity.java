package com.example.less30;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final private int COLOR = 1;
    final private int GRAVITY = 2;

    private TextView textView;
    Button btnColor, btnGravity, btnEn, btnPl;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);


        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        textView.setText(R.string.our_text);
        btnColor = findViewById(R.id.btnColor);
        btnGravity = findViewById(R.id.btnGravity);

        btnEn = findViewById(R.id.btn_en);
        btnPl = findViewById(R.id.btn_pl);
        btnColor.setOnClickListener(this);
        btnGravity.setOnClickListener(this);
        btnEn.setOnClickListener(this);
        btnPl.setOnClickListener(this);
        //    dr.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
        }
        else
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "shouldShowRequestPermissionRationale  - TRUE", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        permissions,1);
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        permissions,1);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("On touch event");
        return super.onTouchEvent(event);
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
            case R.id.btn_en:
                Locale myLocale = new Locale("en");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
                onConfigurationChanged(conf);
                //   Toast.makeText(this, "change to  " + "en", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_pl:
                Locale myLocale2 = new Locale("pl");
                Resources res2 = getResources();
                DisplayMetrics dm2 = res2.getDisplayMetrics();
                Configuration conf2 = res2.getConfiguration();
                conf2.locale = myLocale2;
                res2.updateConfiguration(conf2, dm2);
                onConfigurationChanged(conf2);
                //   Toast.makeText(this, "change to  " + "en", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        btnColor.setText(R.string.color);
        btnGravity.setText(R.string.gravity);
        textView.setText(R.string.our_text);
        super.onConfigurationChanged(newConfig);

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
