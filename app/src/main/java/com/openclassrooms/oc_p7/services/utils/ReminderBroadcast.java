package com.openclassrooms.oc_p7.services.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.openclassrooms.oc_p7.R;

public class ReminderBroadcast extends BroadcastReceiver {

    private final static String TAG = "ReminderBroadcast";

    private final static String CANAL = "myNotifCanal";


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "TIME'S TO SEND NOTIF + " + intent.getAction());


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CANAL)
                .setSmallIcon(R.drawable.ic_cutlery)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(200, notificationBuilder.build());


    }

}
