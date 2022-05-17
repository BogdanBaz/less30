package com.example.less30;

import static java.util.concurrent.TimeUnit.SECONDS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
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


import com.bosphere.filelogger.FL;
import com.bosphere.filelogger.FLConfig;
import com.bosphere.filelogger.FLConst;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_READ_PHONE_STATE = 1;
    final private int COLOR = 1;
    final private int GRAVITY = 2;

    private TextView textView;
    Button btnColor, btnGravity, btnEn, btnPl, btnUa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;


//Percentage can be calculated for API 16+
        double percentAvail = mi.availMem / (double) mi.totalMem * 100.0;

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        // System.out.println("avaliable: " + df.format(availableMegs/1024) + ", percent: " + df.format(percentAvail));

       /* int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
        }
*/

        //  System.out.println( "!!! Signal: " + getSignalStrength(this));

       /* Device device = new Device(this);
        System.out.println("!!DEVICE: " +"\n" +
                device.getModel() +  "\n" +
                device.getBuildBrand()  +"\n" +
                device.getHardware() +"\n" +
                device.getSdkVersion() +"\n" +
                device.getOsVersion() +"\n" +
                device.getSerial()+"\n" +"\n" +
                device.getDevice()+"\n"
                );

        Memory memory = new Memory(this);
        System.out.println("MEMORY: " +  "\n" +
                memory.isHasExternalSDCard()+  "\n" +
                memory.getTotalRAM()+  "\n" +
                memory.getAvailableExternalMemorySize()+  "\n" +
                memory.getAvailableInternalMemorySize()+  "\n" +
                ""
        );

        System.out.println("FORMATED IN GB: " + Formatter.formatShortFileSize(this, memory.getTotalRAM()));

       Network network = new Network(this);
        System.out.println("NETWORK: " +  "\n" +
                network.getIMEI() +  "\n" +
                network.getNetworkType() +  "\n" +
                network.isWifiEnabled() +  "\n" +
                network.isNetworkAvailable()
                );


        Battery battery = new Battery(this);
        Toast.makeText(this, "Is Bat? : " + battery.getBatteryTechnology(), Toast.LENGTH_SHORT).show();

        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                FL.e("this is a log from THREAD ", e);
            }
        });*/

        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);

        //initSftp();

        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        textView.setText(R.string.our_text);
        btnColor = findViewById(R.id.btnColor);
        btnGravity = findViewById(R.id.btnGravity);

        btnEn = findViewById(R.id.btn_en);
        btnPl = findViewById(R.id.btn_pl);
        btnUa = findViewById(R.id.btn_ua);


        btnColor.setOnClickListener(this);
        btnGravity.setOnClickListener(this);
        btnEn.setOnClickListener(this);
        btnPl.setOnClickListener(this);
        btnUa.setOnClickListener(this);


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

            //TODO write LOGs
            //  writeLogs();

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

        startRx();

    }

    private void startRx() {
       /* Observable.timer(5000, TimeUnit.MILLISECONDS)
                .repeat() //to perform your task every 5 seconds
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(aLong -> System.out.println("adfegs"));*/
/*
       Observable.interval(5, TimeUnit.SECONDS)
                .flatMap(new Function<Long, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Long aLong) throws Throwable {
                        System.out.println("!!! print1");
                        return getDataObservable();
                    }
                })
                .repeat()
                .subscribe();*/

       /* Observable
                .interval(5, TimeUnit.SECONDS)
                .doOnNext(n -> getSomething())
                .subscribe();*/
        startHandler();
    }

    private ObservableSource<?> getDataObservable() {
        return new Observable<Object>() {
            @Override
            protected void subscribeActual(@NonNull Observer<? super Object> observer) {

            }
        };

    }

    private void startHandler() {

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                initSftp();
             //   System.out.println("! in scheduler " + System.currentTimeMillis());
            }
        },0,4, SECONDS);

        //scheduledExecutorService.shutdown();
    }

    private void printTask() {
        try {
            Thread.sleep(2000);
            System.out.println("! in sleep");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void initSftp() {
        String fileName = "task.json";
        new AsyncTask<Void, Void, List<String>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected List<String> doInBackground(Void... params) {
                try {
                    Downloader(fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

        }.execute();
    }


    public static void Downloader(String fileName) {

        String user = "root";
        String pass = "Ven123!@#";
        String host = "dashboard.vendim.pl";
        int portNum = 22;

        JSch jsch = new JSch();
        // TODO *.Pem certificate?
        // jsch.addIdentity();

        Session session = null;

        String knownHostsDir = "/root/es/pax/im30/FFFFFFFFFFFF/";

       /* try {
            jsch.setKnownHosts(knownHostsDir);
        } catch (JSchException e) {
            e.printStackTrace();
        }*/

        try {

            session = jsch.getSession(user, host, portNum);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(pass);
            session.connect();
          //  System.out.println("!!! ISCONESCTED sesion " + session.isConnected());

            Channel channel = session.openChannel("sftp");
            channel.connect();
         //   System.out.println("!!! ISCONESCTED channel" + channel.isConnected());
            ChannelSftp sftpChannel = (ChannelSftp) channel;


            //sftpChannel.get( fileName);

// DOWNLOAD
            InputStream stream = sftpChannel.get("/root/es/pax/im30/FFFFFFFFFFFF/task.json");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                String result = new BufferedReader(new InputStreamReader(stream))
                        .lines().collect(Collectors.joining("\n"));
                System.out.println("RES - " + result);
            }


// PUT
          //  sftpChannel.put(new ByteArrayInputStream("Halo halo ".getBytes()), "/root/es/pax/im30/FFFFFFFFFFFF/11.txt");

// DELETE
         //   sftpChannel.rm("/root/es/pax/im30/FFFFFFFFFFFF/11.txt");


            //Log.d(fileName, "has been downloaded");

            sftpChannel.exit();
            session.disconnect();



        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;

            default:
                break;
        }
    }

    TelephonyManager telephonyManager;

    private String getSignalStrength(Context context) throws SecurityException {
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String strength = null;
        System.out.println("NET TYPE: " + telephonyManager.getNetworkType()/* + "Loc: " +        telephonyManager.getCellLocation()*/);
        //   telephonyManager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();   //This will give info of all sims present inside your mobile
        if (cellInfos != null) {
            for (int i = 0; i < cellInfos.size(); i++) {
                if (cellInfos.get(i).isRegistered()) {
                    if (cellInfos.get(i) instanceof CellInfoWcdma) {
                        System.out.println("WCDMA");
                        CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfos.get(i);
                        CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthWcdma.getDbm());
                    } else if (cellInfos.get(i) instanceof CellInfoGsm) {
                        System.out.println("GSM");
                        CellInfoGsm cellInfogsm = (CellInfoGsm) cellInfos.get(i);
                        CellSignalStrengthGsm cellSignalStrengthGsm = cellInfogsm.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthGsm.getDbm());
                    } else if (cellInfos.get(i) instanceof CellInfoLte) {
                        System.out.println("LTE");
                        CellInfoLte cellInfoLte = (CellInfoLte) cellInfos.get(i);
                        CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                        System.out.println("LEVEL: " + cellSignalStrengthLte.getLevel());
                        strength = String.valueOf(cellSignalStrengthLte.getDbm());
                    } else if (cellInfos.get(i) instanceof CellInfoCdma) {
                        System.out.println("CDMA");
                        CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfos.get(i);
                        CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthCdma.getDbm());
                    }
                }
            }
            System.out.println("info= " + cellInfos + " , size: " + cellInfos.size());
        } else System.out.println("infos == null");
        return strength;
    }


    private class MyPhoneStateListener extends PhoneStateListener {
        public int singalStenths = 0;

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            //  singalStenths = signalStrength.getGsmSignalStrength();
            System.out.println("DB: " + getSignalStrength(signalStrength));
            System.out.println("-----  strength: " + signalStrength);
            //  System.out.println("-----int  gsm strength: " + /*signalStrength.getGsmSignalStrength()*/signalStrength.getCdmaDbm());
            // System.out.println("signalStrength.isGsm(): " + signalStrength.isGsm());
        }

    }

    protected int getSignalStrength(SignalStrength signal) {
        String ssignal = signal.toString();
        String[] parts = ssignal.split(" ");
        // No Signal Measured when returning -120 dB
        int dB = -120;
        // If LTE
        if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
            int ltesignal = Integer.parseInt(parts[10]);
            // check to see if it get's the right signal in dB, a signal below -2
            if (ltesignal < -2) {
                dB = ltesignal;
            }
        } else // Else 3G
        {
            if (signal.getGsmSignalStrength() != 99) {
                int strengthInteger = -113 + 2 * signal.getGsmSignalStrength();
                dB = strengthInteger;
            }
        }
        return dB;
    }


    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private void writeLogs() {/*
File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "file_logger_demo.txt");
if (file.exists()){
    System.out.println("Exists!");
}
else     System.out.println("NOT KURWA Exists!");
*/

        FL.init(new FLConfig.Builder(this)
                .defaultTag("Default Tag")   // customise default tag
                .minLevel(FLConst.Level.V)   // customise minimum logging level
                .logToFile(true)   // enable logging to file
                .dir(new File(Environment.getExternalStorageDirectory(), "FILE_logger_DEMO"))    // customise directory to hold log files
                .retentionPolicy(FLConst.RetentionPolicy.FILE_COUNT) // customise retention strategy
                .maxFileCount(FLConst.DEFAULT_MAX_FILE_COUNT)    // customise how many log files to keep if retention by file count
                .maxTotalSize(FLConst.DEFAULT_MAX_TOTAL_SIZE)    // customise how much space log files can occupy if retention by total size
                .build());
        FL.setEnabled(true);

        FL.v("this is a log");
        FL.d("this is a log");
        FL.i("this is a log");
        FL.w("this is a log");
        FL.e("this is a log");
        FL.e("this is a log with exception", new RuntimeException("dummy exception"));
    }

    private void generateException() {
      /*  try{
            Integer.parseInt("zzz");
        }
        catch (Exception e){
            FL.e("this is a log with exception", e);
        }*/
        Integer.parseInt("zzz");

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
            case R.id.btn_ua:
                System.out.println("!!! " + getSignalStrength(this));


                System.out.println("!! IP:  " + getLocalIpAddress());

                Log.d("TAG", "some UA log");
                Locale myLocale21 = new Locale("uk");
                Resources res21 = getResources();
                DisplayMetrics dm21 = res21.getDisplayMetrics();
                Configuration conf21 = res21.getConfiguration();
                conf21.locale = myLocale21;
                res21.updateConfiguration(conf21, dm21);
                onConfigurationChanged(conf21);
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
