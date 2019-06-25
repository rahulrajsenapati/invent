package com.example.invent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.provider.Telephony;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class IncomingCall extends BroadcastReceiver {
    AudioManager am;
    Context ct;
    SharedPreferences pf;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            // TELEPHONY MANAGER class object to register one listner
            ct=context;
            TelephonyManager tmgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            pf=(SharedPreferences) ct.getSharedPreferences("hello",MODE_PRIVATE);
            //Create Listner
            Log.e("::::::::","call intent");
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();

            // Register listener for LISTEN_CALL_STATE
            tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        } catch (Exception e) {
            Log.e("Phone Receive Error", " " + e);
        }
    }
    private class MyPhoneStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {

            Log.d("MyPhoneListener",state+"   incoming no:"+incomingNumber);

            if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                am = (AudioManager) ct.getSystemService(Context.AUDIO_SERVICE);
                Toast.makeText(ct,"Mirophone unmuted",Toast.LENGTH_SHORT).show();
                am.setMicrophoneMute(false);
            }
            else if (state==TelephonyManager.CALL_STATE_IDLE){
                am = (AudioManager)ct.getSystemService(Context.AUDIO_SERVICE);
                Log.e(":::", String.valueOf(pf.getBoolean("Microphoneo",false)));
                if(pf.getBoolean("Microphoneo",false) &&(pf.getBoolean("activate",true))){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    am.setMode(AudioManager.MODE_IN_COMMUNICATION);
                    Log.e(":::::", String.valueOf(am.getMode()));
                    Toast.makeText(ct,"Mirophone muted",Toast.LENGTH_SHORT).show();
                    am.setMicrophoneMute(true);
                }

        }
    }
    }
}
