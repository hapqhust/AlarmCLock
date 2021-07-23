
package com.hapq.alarmclock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

public class Music extends Service{
    MediaPlayer mediaPlayer;
    int id;

    int vitri;
    String nhankey;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Toi trong Music", "Hello");

        Bundle bundle = intent.getBundleExtra("extra");
        nhankey = bundle.getString("Mode");
        vitri = bundle.getInt("Vitri");
        Alarm_Infor alarm = (Alarm_Infor) bundle.getSerializable("DoiTuong");
        String TITLE = alarm.getLabel();
        String CONTENT = alarm.getContent();
        if (TITLE.equals("")){
            TITLE += "Báo thức";
        }

        Log.e("Music nhan key", nhankey);
        Log.e("Music nhan key", "" + vitri);

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(nhankey.equals("on")){
            id = 1;
        }
        else if (nhankey.equals("off")){
            id = -1;
        }

        if(id == 1){
            // Create an Intent for the activity you want to start
            Intent resultIntent = new Intent(Music.this, CancelAlarmActivity.class);
            resultIntent.putExtra("extra", bundle);
            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            // Get the PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(getNotificationID(), PendingIntent.FLAG_UPDATE_CURRENT);

            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                    resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bell);


            long[] pattern = {0, 3000, 1000};

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // API 26 and above
                v.vibrate(VibrationEffect.createWaveform(pattern, 0));
            } else {
                // Below API 26
                v.vibrate(pattern, 0);
            }
            if (v.hasVibrator()) {
                Log.e("Can Vibrate", "YES");
            } else {
                Log.e("Can Vibrate", "NO");
            }

            if(alarm.getContent().equals("")){
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Music.this, MyApplication.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_access_time_24)
                        .setContentTitle(TITLE)
                        //.setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setFullScreenIntent(fullScreenPendingIntent, true)
                        .setVibrate(pattern);
                //.setContentText(message)
                //.setPriority(NotificationCompat.PRIORITY_HIGH)
                //.setCategory(NotificationCompat.CATEGORY_MESSAGE)

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(getNotificationID(), mBuilder.build());
            }
            else {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Music.this, MyApplication.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_access_time_24)
                        .setContentTitle(TITLE)
                        .setContentText(CONTENT)
                        .setLargeIcon(bitmap)
                        //.setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setFullScreenIntent(fullScreenPendingIntent, true)
                        .setVibrate(pattern);
                //.setContentText(message)
                //.setPriority(NotificationCompat.PRIORITY_HIGH)
                //.setCategory(NotificationCompat.CATEGORY_MESSAGE)

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(getNotificationID(), mBuilder.build());
            }

            PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "WakeLockLauncher");
            wakeLock.acquire(2*60*1000L /*2 minutes*/);


            mediaPlayer = MediaPlayer.create(this, R.raw.baothuc);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

            id = 0;
        }
        else if(id == -1){
            mediaPlayer.stop();
            mediaPlayer.reset();
            v.cancel();
        }

        return START_NOT_STICKY;
    }

    private int getNotificationID() {
        return vitri;
    }
}
