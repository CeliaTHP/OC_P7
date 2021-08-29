package com.openclassrooms.oc_p7;

import com.google.common.util.concurrent.MoreExecutors;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.pojo_models.general.NearbyPlaceResponse;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.services.apis.PlacesApi;
import android.location.Location;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;

public class PlaceRepositoryTests {

    private final PlacesApi placesApiMock = Mockito.mock(PlacesApi.class);
    private final MutableLiveData<List<Restaurant>> restaurantLiveDataMock = (MutableLiveData<List<Restaurant>>) Mockito.mock(MutableLiveData.class);
    private final Executor executor = MoreExecutors.newDirectExecutorService();
    private final String expectedRadiusQuery = "2000";
    private final String expectedRestaurantQuery = "restaurant";
    private PlaceRepository placeRepository;
    private final Location expectedLocation = Mockito.mock(Location.class);

    @Before
    public void setUp() {
        placeRepository = new PlaceRepository(placesApiMock, executor, restaurantLiveDataMock, expectedRadiusQuery, expectedRestaurantQuery);
        Mockito.when(expectedLocation.getLatitude()).thenReturn(49.0249d);
        Mockito.when(expectedLocation.getLongitude()).thenReturn(2.4640d);
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(placesApiMock, restaurantLiveDataMock);
    }

    @Test
    public void getNearbyPlacesTestsSuccess() throws IOException {
        Call<NearbyPlaceResponse> call = APIUtils.getCallMock(true, APIUtils.getNearbyPlaceResponse());
        Mockito.when(placesApiMock.getNearbyPlaces(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(call);
        String expectedLocationStringQuery = expectedLocation.getLatitude() + "," + expectedLocation.getLongitude();

        placeRepository.getNearbyPlaces(expectedLocation);

        Mockito.verify(placesApiMock).getNearbyPlaces(BuildConfig.GoogleMapApiKey, expectedLocationStringQuery, expectedRadiusQuery, expectedRestaurantQuery);
        Mockito.verify(call).execute();
        Mockito.verify(restaurantLiveDataMock).postValue(RepositoryUtils.getRestaurantList());
    }

    @Test
    public void getNearbyPlacesTestsFailure() throws IOException {
        Call<NearbyPlaceResponse> call = APIUtils.getCallMock(false, APIUtils.getNearbyPlaceResponse());
        Mockito.when(placesApiMock.getNearbyPlaces(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(call);
        String expectedLocationStringQuery = expectedLocation.getLatitude() + "," + expectedLocation.getLongitude();

        placeRepository.getNearbyPlaces(expectedLocation);

        Mockito.verify(placesApiMock).getNearbyPlaces(BuildConfig.GoogleMapApiKey, expectedLocationStringQuery, expectedRadiusQuery, expectedRestaurantQuery);
        Mockito.verify(call).execute();
        //TODO check postValue error
    }
}
