package com.openclassrooms.oc_p7.services.utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.views.activities.MainActivity;

public class ReminderBroadcast extends BroadcastReceiver {

    private final static String TAG = "ReminderBroadcast";
    private final static String CANAL = "myNotifCanal";

    public static String RESTAURANT_NAME;
    public static String RESTAURANT_ADDRESS;
    public static String RESTAURANT_PIC;
    public static String RESTAURANT_WORKMATES;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        createNotification(context, RESTAURANT_NAME, RESTAURANT_ADDRESS, RESTAURANT_PIC, RESTAURANT_WORKMATES);
    }

    public static void createNotification(Context context, String restaurantName, String restaurantPic, String restaurantAddress, String restaurantWorkmates) {


        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String notificationTitle = context.getString(R.string.notification_title);
        String notificationContent = context.getString(R.string.notification_content, restaurantName, restaurantAddress, restaurantWorkmates);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CANAL)
                .setSmallIcon(R.drawable.ic_cutlery)
                .setContentTitle(notificationTitle)
                .setContentText(notificationContent)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Log.d(TAG, notificationContent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(0, notificationBuilder.build());

    }

}
