package com.openclassrooms.oc_p7;

import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;

import org.junit.Before;
import org.junit.Test;

public class PlaceRepositoryTests {

    private PlaceRepository placeRepository = new PlaceRepository(Injection.provideApiClient());


    @Before
    private void setUp() {
/*
        MockitoAnnotations.initMocks(this);

        place.getDetail().observeForever(apiEmployeeObserver)

        mockWebServer = MockWebServer()
        mockWebServer.start()
        apiHelper = ApiHelperImpl(RetrofitBuilder.apiInterface)


 */
    }

    @Test
    private void getNearbyPlacesTests() {
        String locationString = "49.0249, 2.4640";

        String radiusQuery = MyApplication.getInstance().getApplicationContext().getString(R.string.query_radius);
        String restaurantQuery = MyApplication.getInstance().getApplicationContext().getString(R.string.query_restaurant);


    }


}
