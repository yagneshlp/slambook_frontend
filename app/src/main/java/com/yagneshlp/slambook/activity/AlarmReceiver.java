package com.yagneshlp.slambook.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;


import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.yagneshlp.slambook.R;

import java.util.Random;

import static android.graphics.Color.BLUE;
import static android.support.v4.app.NotificationCompat.CATEGORY_REMINDER;

/**
 * Created by Yagnesh L P on 11-06-2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent k2) {
        Intent notificationIntent = new Intent(context, SplashActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent ci = PendingIntent.getActivity(context, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        NotificationCompat.Builder pubnotif =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Time to fill slambook!");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setContentTitle("Yagnesh's Slambook")
                        .setContentText("Hey, its time to fill the slambook!")
                        .setPriority(2)
                        .setSmallIcon(R.drawable.ic_reminder)
                        .setLargeIcon(largeIcon)

                        .setAutoCancel(true)
                        .setContentIntent(ci)
                        .setCategory(CATEGORY_REMINDER)
                        .setLights(BLUE,200,100)
                        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000, 1000, 1000 })
                        .setSound(alarmSound)
                        .setPublicVersion(pubnotif.build());

        Notification notification = mBuilder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(m, notification);

    }

}
