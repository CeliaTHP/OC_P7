package com.openclassrooms.oc_p7.views.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.services.utils.ReminderBroadcast;

import java.util.Calendar;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    public static FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        initAlarmManager();

    }


    @Override
    protected void onResume() {
        super.onResume();
        verifyAuth();
    }

    private void createNotificationChannel() {
        //TEST UNDER O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(
                            getString(R.string.notification_channel_id),
                            getString(R.string.notification_channel_title), NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription(getString(R.string.notification_channel_desc));
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }

    private void initAlarmManager() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(this, ReminderBroadcast.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, myIntent, 0);

        // Triggers every day at 12:00 AM
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 13);
        calendar.set(Calendar.HOUR_OF_DAY, 12);

        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        Log.d(TAG, calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        Log.d(TAG, Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE));

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Log.d(TAG, "actual timeInMillis : " + Calendar.getInstance().getTimeInMillis());
        Log.d(TAG, "timeInMillis : " + calendar.getTimeInMillis());

    }

    private void verifyAuth() {
        //if not authenticated, go to LogInActivity
        //else go to MainActivity
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Log.d("USER_SERVICE", "synchUser : " + user.getDisplayName());
            goToActivity(new DashboardActivity());
            Log.d(TAG, "User Logged : " + user.getDisplayName());
        } else {
            goToActivity(new LoginActivity());
            Log.d(TAG, "User Not Found");

        }
    }

    private void goToActivity(Activity activity) {
        Intent intent = new Intent(this, activity.getClass());
        startActivity(intent);
    }

}