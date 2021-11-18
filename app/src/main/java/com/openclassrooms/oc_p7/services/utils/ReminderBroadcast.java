package com.openclassrooms.oc_p7.services.utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.services.firestore_database.UserHelper;
import com.openclassrooms.oc_p7.services.firestore_database.WorkmateHelper;
import com.openclassrooms.oc_p7.views.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class ReminderBroadcast extends BroadcastReceiver {

    private final static String TAG = "ReminderBroadcast";
    private final static String CANAL = "myCanal";

    public static String RESTAURANT_NAME;
    public static String RESTAURANT_ID;

    public static String RESTAURANT_ADDRESS;
    public static String RESTAURANT_PIC;
    public static String RESTAURANT_WORKMATES;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        prepareToCreateNotification(context);
    }

    public static void prepareToCreateNotification(Context context) {
        getUserInfos(context);


    }

    public static void getUserInfos(Context context) {

        UserHelper.getUser(FirebaseAuth.getInstance().getUid())

                .addOnSuccessListener(snapshot -> {
                    Log.d(TAG, "onSuccess");
                    if (snapshot.get("restaurantName") != null) {
                        RESTAURANT_NAME = (String) snapshot.get("restaurantName");
                        Log.d(TAG, "restaurant NAME : " + RESTAURANT_NAME);
                    }
                    if (snapshot.get("restaurantId") != null) {
                        RESTAURANT_ID = (String) snapshot.get("restaurantId");
                        Log.d(TAG, "restaurant ID: " + RESTAURANT_ID);
                        getRestaurantWorkmates((String) snapshot.get("restaurantId"), context);
                        Log.d(TAG, "restaurant Workmates : " + RESTAURANT_WORKMATES);


                    }
                    if (snapshot.get("restaurantAddress") != null) {
                        RESTAURANT_ADDRESS = (String) snapshot.get("restaurantAddress");
                        Log.d(TAG, "restaurant ADDRESS: " + RESTAURANT_ADDRESS);
                    }
                });

    }

    public static void createNotification(Context context) {
        String notificationContent = null;

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String notificationTitle = context.getString(R.string.notification_title);
        if (RESTAURANT_WORKMATES.isEmpty()) {
            Log.d(TAG, "alone");

            notificationContent = context.getString(R.string.notification_content_no_workmates, RESTAURANT_NAME, RESTAURANT_ADDRESS);

        } else {
            Log.d(TAG, "going with " + RESTAURANT_WORKMATES);

            notificationContent = context.getString(R.string.notification_content, RESTAURANT_NAME, RESTAURANT_ADDRESS, RESTAURANT_WORKMATES);

        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CANAL)
                .setSmallIcon(R.drawable.ic_cutlery)
                .setContentTitle(notificationTitle)
                .setContentText(notificationContent)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Log.d(TAG, "content : " + notificationContent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(0, notificationBuilder.build());
    }

    public static void getRestaurantWorkmates(String restaurantId, Context context) {
        WorkmateHelper workmateHelper = new WorkmateHelper();

        List<String> workmateList = new ArrayList<>();

        Task<QuerySnapshot> task = workmateHelper.getWorkmatesForRestaurant(FirebaseFirestore.getInstance(), restaurantId);
        task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot documentSnapshot : snapshotList) {
                    Workmate workmateToAdd = documentSnapshot.toObject(Workmate.class);
                    workmateList.add(workmateToAdd.getName());
                    Log.d(TAG, "added : " + workmateToAdd.getName() + " to " + workmateToAdd.getRestaurantName());

                }
                //RESTAURANT_WORKMATES = workmateList.stream().collect(Collectors.joining(","));
                RESTAURANT_WORKMATES = String.join(", ", workmateList);
                createNotification(context);
            }
        });
    }

}


