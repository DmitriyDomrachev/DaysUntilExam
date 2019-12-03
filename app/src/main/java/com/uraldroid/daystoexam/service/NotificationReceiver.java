package com.uraldroid.daystoexam.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.RemoteException;
import android.util.Log;

import com.uraldroid.daystoexam.activity.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("notifTag","NotificationReceiver принял");
        Intent intent1 = new Intent(context, NotificationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                context.startForegroundService(intent1);
            } catch (Exception e){
                context.startService(intent1);
            }
            Log.d("notifTag","Запуск сервиса");
        } else {
            context.startService(intent1);
            Log.d("notifTag","Запуск сервиса");
        }

    }
}
