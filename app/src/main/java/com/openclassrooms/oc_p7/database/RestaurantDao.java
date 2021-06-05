package com.openclassrooms.oc_p7.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.oc_p7.model.Restaurant;

import java.util.List;

@Dao
public interface RestaurantDao {

    @Query("SELECT * FROM restaurant")
    List<Restaurant> getAll();

    @Query("SELECT * FROM restaurant WHERE placeId = :placeId")
    Restaurant getRestaurant(String placeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createRestaurant(Restaurant restaurant);


}
