package com.hapq.alarmclock;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("Toi trong Receiver", "Hello");

        Bundle bundle = intent.getBundleExtra("extra");
        String chuoi = bundle.getString("Mode");
        Log.e("Ban truyen Key", chuoi);

        Intent myIntent = new Intent(context, Music.class);
        myIntent.putExtra("extra", bundle);
        context.startService(myIntent);
    }

}
