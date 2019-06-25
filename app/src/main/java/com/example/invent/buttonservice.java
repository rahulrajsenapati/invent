package com.example.invent;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.opengl.Visibility;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.Size;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.w3c.dom.Node;

import java.io.IOException;
import java.util.List;

public class buttonservice extends AccessibilityService {
    static int noo=0;
    SharedPreferences pf;


    ComponentName deviceAdminSample;
    DevicePolicyManager dpm;
    AudioManager am;
    WifiManager wifim;
    private WindowManager mWindowManager;
    private View mFloatingView;
    private View lv;
    private WindowManager.LayoutParams params;
    BluetoothAdapter mBluetoothAdapter;
    LocationManager manager;
    public buttonservice() {
    }
    @Override
    public void onCreate(){
        super.onCreate();
        pf = this.getSharedPreferences("hello",MODE_PRIVATE);
        dpm=(DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);;
        deviceAdminSample =new ComponentName(this,DeviceAdminSample.class);
        am=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        wifim=(WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        //cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mFloatingView= LayoutInflater.from(this).inflate(R.layout.blank,null);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        mWindowManager=(WindowManager) getSystemService(WINDOW_SERVICE);
    }
    @Override
    public void onServiceConnected(){
        super.onServiceConnected();

        //mWindowManager.addView(mFloatingView,params);
        //lv=mFloatingView.findViewById(R.id.layoutCollapsed);
        //lv.setVisibility(View.GONE);


    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String data="";
        if(intent!=null){
        if(intent.getExtras().containsKey("data")){
            data = intent.getStringExtra("data");
            if(data.equals("start")){
                mainaction();
    }}}
        return START_STICKY;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void mainaction(){
        final String[] options = {"Wifi", "Bluetooth", "Camera","Microphone", "Internet", "GPS"};
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
        if(pf.getBoolean(options[2]+"o",true)) {
            dpm.setCameraDisabled(deviceAdminSample, true);
        }
        if(pf.getBoolean(options[0]+"o",true)) {
            pf.edit().putBoolean("wifi", (wifim.isWifiEnabled())).apply();
            wifim.setWifiEnabled(false);
        }


        //lv.setVisibility(View.VISIBLE);
        if(pf.getBoolean(options[3]+"o",true)) {
            am.setMode(AudioManager.MODE_IN_COMMUNICATION);
            Log.e(":::::", String.valueOf(am.getMode()));
            am.setMicrophoneMute(true);
        }
        if(pf.getBoolean(options[1]+"o",true)) {
            pf.edit().putBoolean("blue", (mBluetoothAdapter.isEnabled())).apply();
            mBluetoothAdapter.disable();
        }

        //mWindowManager.addView(mFloatingView,params);
        //Intent service = new Intent(getBaseContext(), MyService.class);
        //startService(service);
        if(pf.getBoolean(options[5]+"o",true)) {
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AccessibilityNodeInfo node = getRootInActiveWindow();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (node == null)
                        Log.e(":::::::", "null ");
                    Nodeprinter(node, "");
                }
                //node.getChild(0).getChild(0).getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                List<AccessibilityNodeInfo> lis = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (node != null) {
                        lis = node.findAccessibilityNodeInfosByViewId("com.android.settings:id/switch_bar");
                        for (AccessibilityNodeInfo l : lis) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                Log.e("::::", (l.getViewIdResourceName() == null ? "null" : l.getViewIdResourceName()));
                            }
                        }
                        if(lis.isEmpty()==false)
                          for(AccessibilityNodeInfo l:lis)
                            l.performAction(AccessibilityNodeInfo.ACTION_CLICK);

                        lis = node.findAccessibilityNodeInfosByViewId("android:id/switch_widget");
                        for (AccessibilityNodeInfo l : lis) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                Log.e("::::", (l.getViewIdResourceName() == null ? "null" : l.getViewIdResourceName()));
                            }
                        }
                        if(lis.isEmpty()==false)
                            for(AccessibilityNodeInfo l:lis)
                                l.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }


            }
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e("::::::","started turning off"+ String.valueOf(am.isMicrophoneMute()));
        if(pf.getBoolean(options[4]+"o",true)) {
            Intent intent2 = new Intent();
            intent2.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent2);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AccessibilityNodeInfo node2 = getRootInActiveWindow();
            //node2.getChild(0).getChild(0).getChild(2).getChild(5).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            if (node2 != null) {
                AccessibilityNodeInfo node = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2&&node2.findAccessibilityNodeInfosByText("android:id/switch_widget").isEmpty()==false) {
                    node = node2.findAccessibilityNodeInfosByViewId("android:id/switch_widget").get(0).getParent();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && node!=null) {
                    if (node.canOpenPopup() == true) {
                        Log.e("inseide this", "::::::::");
                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        node2 = getRootInActiveWindow();
                        Nodeprinter(node2, "");
                        node2.findAccessibilityNodeInfosByText("OK").get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    } else {
                        node2.findAccessibilityNodeInfosByText("Mobile data").get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }

                }
            }
                    /*
                    node2=getRootInActiveWindow();
                    if(node2.getChild(0)!=null){
                        if(node2.getChild(0).getText()!=null){
                              if(node2.getChild(0).getText().equals("Mobile data")) {

                              }
                        }
                    }*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                Nodeprinter(node2, "");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



        performGlobalAction(GLOBAL_ACTION_HOME);
        performGlobalAction(GLOBAL_ACTION_HOME);
        //mWindowManager.removeView(mFloatingView);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onKeyEvent(KeyEvent event) {

        int action = event.getAction();
        int keycode = event.getKeyCode();
        if (action == KeyEvent.ACTION_UP){
            if ( keycode == KeyEvent.KEYCODE_VOLUME_DOWN)
            {
                Log.e(":::::", "keydown");
                noo++;
                if(noo==3) {
                    mainaction();
                    //lv.setVisibility(View.GONE);
                    noo=0;

                   // mRecordingStatus = false;
                    //mServiceCamera = CameraRecorder.mCamera;
                   // mServiceCamera = Camera.open(1);
                   // mSurfaceView = MainActivity.mSurfaceView;
                   // mSurfaceHolder = MainActivity.mSurfaceHolder;
                    //starMediaRecording();
                }


            }
            return  false;

        }else {
            return  false;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void Nodeprinter(AccessibilityNodeInfo mNodeInfo, String logu){
        if(mNodeInfo == null) return ;
        String log = "";
        log= logu+ "("+mNodeInfo.getText()+"=="+((mNodeInfo.getViewIdResourceName() != null)?mNodeInfo.getViewIdResourceName():"NO VIEW ID")+"("+((mNodeInfo.isClickable())?"CLICKABLE":"")+")"+ "<--"+((mNodeInfo.getParent() != null)?mNodeInfo.getParent().getViewIdResourceName():"NO PARENT")+")";
        Log.d("::::", log);
        if(mNodeInfo.getChildCount()<1) return ;

        for(int i = 0; i < mNodeInfo.getChildCount(); i++){
            Nodeprinter(mNodeInfo.getChild(i),logu+"."+String.valueOf(i));
        }

    }

    private static void executeCommandViaSu(Context context, String option, String command) {
        boolean success = false;
        String su = "su";
        for (int i=0; i < 3; i++) {
            // Default "su" command executed successfully, then quit.
            if (success) {
                break;
            }
            // Else, execute other "su" commands.
            if (i == 1) {
                su = "/system/xbin/su";
            } else if (i == 2) {
                su = "/system/bin/su";
            }
            try {
                // Execute command as "su".
                Runtime.getRuntime().exec(new String[]{su, option, command});
            } catch (IOException e) {
                success = false;
                // Oops! Cannot execute `su` for some reason.
                // Log error here.
            } finally {
                success = true;
            }
        }
    }






}
