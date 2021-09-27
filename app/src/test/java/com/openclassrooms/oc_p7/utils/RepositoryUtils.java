package com.openclassrooms.oc_p7.utils;

import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.models.pojo_models.general.RestaurantPojo;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;

import java.util.List;

public class RepositoryUtils {

    public static List<Restaurant> getRestaurantList() {
        List<RestaurantPojo> restaurantPojoList = APIUtils.getRestaurantPojoList();
        return PlaceRepository.getRestaurantList(restaurantPojoList);
    }

    static List<Workmate> getWorkmateList() {
        return WorkmateUtils.getWorkmateList();
    }
}