package com.openclassrooms.oc_p7.utils;

import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.pojo_models.restaurant_pojo.RestaurantPojo;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;

import java.util.List;

public class RepositoryUtils {

    public static List<Restaurant> getRestaurantList() {
        List<RestaurantPojo> restaurantPojoList = APIUtils.getRestaurantPojoList();
        return PlaceRepository.getRestaurantList(restaurantPojoList);
    }


}