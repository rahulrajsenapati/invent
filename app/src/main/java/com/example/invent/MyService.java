package com.example.invent;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class MyService extends Service {
    public MyService() {
    }
    private WindowManager mWindowManager;
    private View mFloatingView;
    private WindowManager.LayoutParams params;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        while(true){
         break;
        }
        return Service.START_STICKY;
    }
}
