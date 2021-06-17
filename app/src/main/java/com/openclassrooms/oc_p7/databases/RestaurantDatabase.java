package com.openclassrooms.oc_p7.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.openclassrooms.oc_p7.models.Restaurant;

@Database(entities = {Restaurant.class}, version = 1)
public abstract class RestaurantDatabase extends RoomDatabase {

    private static volatile RestaurantDatabase INSTANCE;

    public abstract RestaurantDao getRestaurantDao();

    public static RestaurantDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RestaurantDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RestaurantDatabase.class, "RestaurantDatabase.db")
                            .allowMainThreadQueries() //Delete this when executor set
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}

