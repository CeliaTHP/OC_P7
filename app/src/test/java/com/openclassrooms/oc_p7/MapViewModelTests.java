package com.openclassrooms.oc_p7;

import android.content.Context;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;
import com.openclassrooms.oc_p7.view_models.MapViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class MapViewModelTests {


    private MapViewModel mapViewModel;

    private PlaceRepository placeRepositoryMock = Mockito.mock(PlaceRepository.class);
    private WorkmateRepository workmateRepositoryMock = Mockito.mock(WorkmateRepository.class);
    private FusedLocationProviderClient fusedLocationProviderClientMock = Mockito.mock(FusedLocationProviderClient.class);
    private LifecycleOwner lifecycleOwnerMock = Mockito.mock(LifecycleOwner.class);

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();


    @Before
    public void setup() {
        mapViewModel = new MapViewModel(placeRepositoryMock, workmateRepositoryMock, fusedLocationProviderClientMock, lifecycleOwnerMock);

    }


    @Test
    public void constructorTest() {
        //Assert.assertEquals(mapViewModel.currentLocationLiveData, placeRepositoryMock.currentLocationLiveData);
        //Assert.assertEquals(mapViewModel.restaurantLiveData, placeRepositoryMock.restaurantLiveData);

    }

    @Test
    public void getLocationInformations() {
        //Verify that vm calls method from repo
        mapViewModel.getLocationInformations(Mockito.mock(Context.class));

        Mockito.verify(placeRepositoryMock).getNearbyPlaces(Mockito.mock(Location.class));

        Mockito.verifyNoMoreInteractions(placeRepositoryMock);
    }

    @Test
    public void loadTest() {

        //MutableLiveData<List<Restaurant>> restaurantLiveDataMock = Mockito.mock(MutableLiveData.class);
        //Mockito.when(restaurantLiveDataMock.observe(Mockito.any(), Mockito.any())).thenAnswer()
        MutableLiveData<List<Restaurant>> restaurantLiveData = new MutableLiveData<>();
        Mockito.when(placeRepositoryMock.getRestaurantLiveData()).thenReturn(restaurantLiveData);

        mapViewModel.loadMap();

        List<Restaurant> expectedRestaurantList = new ArrayList<>();
        //restaurantLiveData.postValue(expectedRestaurantList);

        Mockito.verify(placeRepositoryMock.getRestaurantLiveData()).observe(lifecycleOwnerMock, Mockito.any());

    }


}
