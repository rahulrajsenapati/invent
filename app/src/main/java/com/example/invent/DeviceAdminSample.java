package com.example.invent;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class DeviceAdminSample extends DeviceAdminReceiver {
    /*
    void showToast(Context context, String msg) {
        String status = context.getString(R.string.admin_receiver_status, msg);
        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
    }*/
    SharedPreferences pf;
    @Override
    public void onEnabled(Context context, Intent intent) {
        Toast.makeText(context, "Device Admin : enabled", Toast.LENGTH_SHORT).show();
        pf= (SharedPreferences) context.getSharedPreferences("hello",MODE_PRIVATE);
        pf.edit().putBoolean("devadmin",true).apply();
    }



    @Override
    public void onDisabled(Context context, Intent intent) {
        Toast.makeText(context, "Device Admin : disabled", Toast.LENGTH_SHORT).show();
        pf= (SharedPreferences) context.getSharedPreferences("hello",MODE_PRIVATE);
        pf.edit().putBoolean("devadmin",false).apply();
    }


/*
    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return context.getString(R.string.admin_receiver_status_disable_warning);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, context.getString(R.string.admin_receiver_status_disabled));
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent, userHandle: UserHandle) {
        showToast(context, context.getString(R.string.admin_receiver_status_pw_changed));
    }

*/



}
