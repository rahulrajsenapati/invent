package com.example.invent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    private static final String TAG = "Recorder";
    public static SurfaceView mSurfaceView;
    public static SurfaceHolder mSurfaceHolder;
    public static Camera mCamera ;
    public static boolean mPreviewRunning;
    WifiManager wifim;
    AudioManager am;
    ComponentName deviceAdminSample;
    DevicePolicyManager dpm;
    SharedPreferences pf;
    BluetoothAdapter mBluetoothAdapter;
    final String[] options = {"Wifi", "Bluetooth", "Camera","Microphone", "Internet", "GPS"};
    @SuppressLint("WifiManagerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        am=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        dpm=(DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);;
        deviceAdminSample =new ComponentName(this,DeviceAdminSample.class);
        wifim=(WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        pf =getSharedPreferences("hello",MODE_PRIVATE);
        if(pf.getBoolean("activate",false)){
            Button b=findViewById(R.id.button);
            b.setVisibility(View.GONE);
            Button button = (Button) findViewById(R.id.button2);
            button.setVisibility(View.VISIBLE);
        }
        for(int i=0;i<options.length;i++){
            pf.edit().putBoolean(options[i]+"o",pf.getBoolean(options[i]+"o",true)).apply();
        }
        //mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
        //mSurfaceHolder = mSurfaceView.getHolder();
        //mSurfaceHolder.addCallback(this);
        //mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
public void list(View v){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Choose your selected");

    boolean[] checkedItems = {true, false, false, true, false,true};
    for(int i=0;i< options.length;i++){
        checkedItems[i]=pf.getBoolean(options[i]+"o",true);
    }

    builder.setMultiChoiceItems(options, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            pf.edit().putBoolean(options[which]+"o",isChecked).apply();
        }
    });

// add OK and Cancel buttons
  /*  builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // user clicked OK
        }
    });*/
  builder.setPositiveButton("OK",null);
   // builder.setNegativeButton("Cancel", null);
    AlertDialog dialog = builder.create();
    dialog.show();
}
    public void start(View v){
        if((!isAccessibilitySettingsOn(getApplicationContext()))||(!pf.getBoolean("devadmin",false))){
            Toast.makeText(getApplicationContext(),"Permission denied",Toast.LENGTH_LONG).show();
            return;
        }
        Log.e("::::","started");
        pf.edit().putBoolean("activate",true).apply();
        v.setVisibility(View.GONE);
        Button button = (Button) findViewById(R.id.button2);
        button.setVisibility(View.VISIBLE);
        /*
        Vibrator ve = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ve.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            ve.vibrate(500);
        }
        dpm.setCameraDisabled(deviceAdminSample, true);
        pf.edit().putBoolean("wifi", (wifim.isWifiEnabled())).apply();
        wifim.setWifiEnabled(false);


        //lv.setVisibility(View.VISIBLE);
        int originalMode = am.getMode();
        am.setMode(AudioManager.MODE_IN_COMMUNICATION);
        Log.e(":::::", String.valueOf(am.getMode()));
        am.setMicrophoneMute(true);
        pf.edit().putBoolean("blue", (mBluetoothAdapter.isEnabled())).apply();
        mBluetoothAdapter.disable();*/
        Intent intent = new Intent(getApplicationContext(), buttonservice.class);
        intent.putExtra("data","start");
        startService(intent);
        Toast.makeText(getApplicationContext(),"Started it....",Toast.LENGTH_SHORT).show();



        //dpm.setCameraDisabled(deviceAdminSample, true);

    }
    public void stop(View v){
        pf.edit().putBoolean("activate",false).apply();
        v.setVisibility(View.GONE);
        Button b=findViewById(R.id.button);
        b.setVisibility(View.VISIBLE);
        Vibrator ve = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ve.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            ve.vibrate(500);
        }

        am.setMode(AudioManager.MODE_NORMAL);
        am.setMicrophoneMute(false);
        dpm.setCameraDisabled(deviceAdminSample, false);
        if(pf.getBoolean("wifi",false)){
            wifim.setWifiEnabled(true);
        }
        if(pf.getBoolean("blue",false)){
            mBluetoothAdapter.enable();
        }
        Toast.makeText(getApplicationContext(),"Stopped it....",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void sett(View v){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.PROCESS_OUTGOING_CALLS},
                1);




     /*   Toast.makeText(getApplicationContext(),"Going to settings...",Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0);
            }
        }
        */
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.button_dialog);
        dialog.setTitle("Permissions");
        Button b1=dialog.findViewById(R.id.accbut);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAccessibilitySettingsOn(getApplicationContext())){
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivityForResult(intent, 0);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Permission granted already",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button b2=dialog.findViewById(R.id.devbut);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pf.getBoolean("devadmin",false)) {
                    startActivity(new Intent().setComponent(new ComponentName("com.android.settings", "com.android.settings.DeviceAdminSettings")));


                }
                else{
                    Toast.makeText(getApplicationContext(),"Permission granted already",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();

        /*
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);*/




/*
       if(!pf.getBoolean("devadmin",false)) {
           startActivity(new Intent().setComponent(new ComponentName("com.android.settings", "com.android.settings.DeviceAdminSettings")));


       }*/
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    private boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + buttonservice.class.getCanonicalName();
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            //Timber.v("accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            //Timber.e("Error finding setting, default accessibility to not found: "
                 //   + e.getMessage());
        }

        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
           // Timber.v("***ACCESSIBILIY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();

                   // Timber.v("-------------- > accessabilityService :: " + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase(service)) {
                       // Timber.v("We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            //Timber.v("***ACCESSIBILIY IS DISABLED***");
        }

        return accessibilityFound;
    }
}
