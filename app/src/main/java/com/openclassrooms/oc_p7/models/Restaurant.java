package com.openclassrooms.oc_p7.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "restaurant")
public class Restaurant {

    @PrimaryKey
    @NonNull
    private String placeId;

    @ColumnInfo(name = "name")
    private String name;

    public Restaurant(String placeId, String name) {
        this.placeId = placeId;
        this.name = name;

    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
