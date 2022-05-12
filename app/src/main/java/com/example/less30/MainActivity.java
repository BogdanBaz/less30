package com.example.less30;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final private int COLOR = 1;
    final private int GRAVITY = 2;

    private TextView textView;
    Button btnColor, btnGravity, btnEn, btnPl,overlayPowerBtn;
    WindowManager windowManager;

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
        } else
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "shouldShowRequestPermissionRationale  - TRUE", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        permissions, 1);
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        permissions, 1);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            CharSequence text = "Please grant the access to the application.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.fromParts("package", getPackageName(), null)));
        } else {
            startPowerOverlay();
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void startPowerOverlay() {
        // Starts the button overlay.
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        overlayPowerBtn = new Button(this);
        overlayPowerBtn.setText("OVERLAY");

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // APPLICATION_OVERLAY FOR ANDROID 26+ AS THE PREVIOUS VERSION RAISES ERRORS
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            // FOR PREVIOUS VERSIONS USE TYPE_PHONE AS THE NEW VERSION IS NOT SUPPORTED
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;
        params.height = 110;
        params.width = 110;

        windowManager.addView(overlayPowerBtn, params);

        overlayPowerBtn.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private long latestPressTime = 0;
            private int CLICK_ACTION_THRESHOLD = 1;
            private float startX;
            private float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        // Save current x/y
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        // Check for double clicks.
                       /* if (latestPressTime == 0 || latestPressTime + 500 < System.currentTimeMillis()) {
                            latestPressTime = System.currentTimeMillis();
                        } else {
                            Toast.makeText(MainActivity.this, "2x click", Toast.LENGTH_SHORT).show();
                            // Doubleclicked. Do any action you'd like
                        }*/
                        return true;
                    case MotionEvent.ACTION_UP:
                        float endX = event.getX();
                        float endY = event.getY();
                        if (isAClick(startX, endX, startY, endY)) {
                            takeScreenshot();
                            Toast.makeText(MainActivity.this, "CLICK ??? ", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(overlayPowerBtn, params);
                        return true;
                }
                return false;
            }

            private boolean isAClick(float startX, float endX, float startY, float endY) {
                float differenceX = Math.abs(startX - endX);
                float differenceY = Math.abs(startY - endY);
                return !(differenceX > CLICK_ACTION_THRESHOLD/* =5 */ || differenceY > CLICK_ACTION_THRESHOLD);
            }

        });
        overlayPowerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "CLICK", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void takeScreenshot() {
        // create bitmap screen capture
        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);
        try {
            store(bitmap, "newFUCKING_SCREEN");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (overlayPowerBtn != null)
            windowManager.removeView(overlayPowerBtn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (overlayPowerBtn != null)
            windowManager.removeView(overlayPowerBtn);
    }

    private void store(Bitmap bitmap, String s)throws IOException {
        boolean saved;
        OutputStream fos;
        File image = null;
        Uri imageUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            ContentResolver resolver = MainActivity.this.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, s);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" );
             imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        } else {
            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator ;

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

             image = new File(imagesDir, s + ".png");
            fos = new FileOutputStream(image);
        }

        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
        openScreenshot(imageUri);
       // openScreenshot(image);
    }
    
    private void openScreenshot(Uri imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
       // Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(imageFile, "image/*");
        startActivity(intent);
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
