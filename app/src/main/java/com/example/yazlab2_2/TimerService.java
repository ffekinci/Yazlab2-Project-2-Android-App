package com.example.yazlab2_2;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class TimerService  extends IntentService {

    public  TimerService(){
        super("Timer Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v("timer", "Its started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    protected void onHandleIntent(Intent intent) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        DownloadData downloadData = new DownloadData(DataType.latest, null,null,null);
        downloadData.pref = sp;

        String link = MainActivity.address+"/api/news/lastNews";
        try {
            downloadData.execute(link).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(5000);

        } catch (Exception e) {

        }
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setSmallIcon(R.drawable.ic_launcher_background);
//        builder.setContentTitle("BasicNotifications Sample");
//        builder.setContentText("Time to learn about notifications!");
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(1, builder.build());

        Intent intentt = new Intent(this, TimerService.class);
        intentt.putExtra("time", 10);

        startService(intentt);

    }

    }
