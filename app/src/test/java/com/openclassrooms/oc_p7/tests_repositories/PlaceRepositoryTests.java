package com.openclassrooms.oc_p7.tests_repositories;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import com.google.common.util.concurrent.MoreExecutors;
import com.openclassrooms.oc_p7.BuildConfig;
import com.openclassrooms.oc_p7.models.ErrorCode;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.pojo_models.general.NearbyPlaceResponse;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.services.apis.PlacesApi;
import com.openclassrooms.oc_p7.utils.APIUtils;
import com.openclassrooms.oc_p7.utils.RepositoryUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;

public class PlaceRepositoryTests {

    private final PlacesApi placesApiMock = Mockito.mock(PlacesApi.class);
    private final MutableLiveData<List<Restaurant>> restaurantListLiveDataMock = (MutableLiveData<List<Restaurant>>) Mockito.mock(MutableLiveData.class);
    private final MutableLiveData<Restaurant> restaurantLiveDataMock = (MutableLiveData<Restaurant>) Mockito.mock(MutableLiveData.class);

    private final Executor executor = MoreExecutors.newDirectExecutorService();
    private final String expectedRadiusQuery = "2000";
    private final String expectedRestaurantQuery = "restaurant";
    private PlaceRepository placeRepository;
    private final Location expectedLocation = Mockito.mock(Location.class);

    private final MutableLiveData<ErrorCode> errorCodeMutableLiveDataMock = (MutableLiveData<ErrorCode>) Mockito.mock(MutableLiveData.class);

    @Before
    public void setUp() {
        placeRepository = new PlaceRepository(placesApiMock, executor, restaurantListLiveDataMock, restaurantLiveDataMock, expectedRadiusQuery, expectedRestaurantQuery, errorCodeMutableLiveDataMock);
        Mockito.when(expectedLocation.getLatitude()).thenReturn(49.0249d);
        Mockito.when(expectedLocation.getLongitude()).thenReturn(2.4640d);
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(placesApiMock, restaurantListLiveDataMock);
    }

    @Test
    public void getNearbyPlacesTestsSuccess() throws IOException {
        Call<NearbyPlaceResponse> call = APIUtils.getCallMock(true, APIUtils.getNearbyPlaceResponse());
        Mockito.when(placesApiMock.getNearbyPlaces(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(call);
        String expectedLocationStringQuery = expectedLocation.getLatitude() + "," + expectedLocation.getLongitude();

        placeRepository.getNearbyPlaces(expectedLocation);

        Mockito.verify(placesApiMock).getNearbyPlaces(BuildConfig.GoogleMapApiKey, expectedLocationStringQuery, expectedRadiusQuery, expectedRestaurantQuery);
        Mockito.verify(call).execute();
        Mockito.verify(restaurantListLiveDataMock).postValue(RepositoryUtils.getRestaurantList());
    }

    @Test
    public void getNearbyPlacesTestsUnsuccessfullResponse() throws IOException {
        Call<NearbyPlaceResponse> call = APIUtils.getCallMock(false, APIUtils.getNearbyPlaceResponse());
        Mockito.when(placesApiMock.getNearbyPlaces(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(call);
        String expectedLocationStringQuery = expectedLocation.getLatitude() + "," + expectedLocation.getLongitude();

        placeRepository.getNearbyPlaces(expectedLocation);

        Mockito.verify(placesApiMock).getNearbyPlaces(BuildConfig.GoogleMapApiKey, expectedLocationStringQuery, expectedRadiusQuery, expectedRestaurantQuery);
        Mockito.verify(call).execute();
        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.UNSUCCESSFUL_RESPONSE);

    }

    @Test
    public void getNearbyPlacesTestIOException() throws IOException {

        Call<NearbyPlaceResponse> call = APIUtils.getCallMock(true, APIUtils.getNearbyPlaceResponse());
        String expectedLocationStringQuery = expectedLocation.getLatitude() + "," + expectedLocation.getLongitude();
        Mockito.when(placesApiMock.getNearbyPlaces(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(call);
        Mockito.doThrow(new IOException()).when(call).execute();

        placeRepository.getNearbyPlaces(expectedLocation);

        Mockito.verify(placesApiMock).getNearbyPlaces(BuildConfig.GoogleMapApiKey, expectedLocationStringQuery, expectedRadiusQuery, expectedRestaurantQuery);
        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.CONNECTION_ERROR);

    }

    @Test
    public void getRestaurantDetailsTestSuccess() throws IOException {

    }
}
