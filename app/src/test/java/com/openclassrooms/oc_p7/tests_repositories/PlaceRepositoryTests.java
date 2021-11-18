package com.openclassrooms.oc_p7.tests_repositories;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.util.concurrent.MoreExecutors;
import com.openclassrooms.oc_p7.models.ErrorCode;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.pojo_models.responses.AutocompleteResponse;
import com.openclassrooms.oc_p7.models.pojo_models.responses.DetailsPlaceResponse;
import com.openclassrooms.oc_p7.models.pojo_models.responses.NearbyPlaceResponse;
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
import java.util.Locale;
import java.util.concurrent.Executor;

import retrofit2.Call;

public class PlaceRepositoryTests {

    private final PlacesApi placesApiMock = Mockito.mock(PlacesApi.class);
    private final MutableLiveData<List<Restaurant>> restaurantListLiveDataMock = (MutableLiveData<List<Restaurant>>) Mockito.mock(MutableLiveData.class);
    private final MutableLiveData<Restaurant> restaurantLiveDataMock = (MutableLiveData<Restaurant>) Mockito.mock(MutableLiveData.class);
    private final MutableLiveData<List<Restaurant>> requestedRestaurantList = (MutableLiveData<List<Restaurant>>) Mockito.mock(MutableLiveData.class);


    private final Executor executor = MoreExecutors.newDirectExecutorService();
    private final String expectedRadiusQuery = "2000";
    private final String expectedRestaurantQuery = "restaurant";
    private final String language = Locale.getDefault().getLanguage();

    private PlaceRepository placeRepository;
    private final Location expectedLocation = Mockito.mock(Location.class);

    private final String fakeApiKey = "fakeApiKey";

    private final MutableLiveData<ErrorCode> errorCodeMutableLiveDataMock = (MutableLiveData<ErrorCode>) Mockito.mock(MutableLiveData.class);

    @Before
    public void setUp() {
        placeRepository = new PlaceRepository(placesApiMock, fakeApiKey, executor, restaurantListLiveDataMock, restaurantLiveDataMock, requestedRestaurantList, expectedRadiusQuery, expectedRestaurantQuery, errorCodeMutableLiveDataMock);
        Mockito.when(expectedLocation.getLatitude()).thenReturn(49.0249d);
        Mockito.when(expectedLocation.getLongitude()).thenReturn(2.4640d);
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(placesApiMock, restaurantListLiveDataMock);
    }


    //region getNearbyPlaces
    @Test
    public void getNearbyPlacesTestsSuccess() throws IOException {
        Call<NearbyPlaceResponse> call = APIUtils.getCallMock(true, APIUtils.getNearbyPlaceResponse());
        Mockito.when(placesApiMock.getNearbyPlaces(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(call);
        String expectedLocationStringQuery = expectedLocation.getLatitude() + "," + expectedLocation.getLongitude();

        placeRepository.getNearbyPlaces(expectedLocation);

        Mockito.verify(placesApiMock).getNearbyPlaces(fakeApiKey, expectedLocationStringQuery, expectedRadiusQuery, expectedRestaurantQuery);
        Mockito.verify(call).execute();
        Mockito.verify(restaurantListLiveDataMock).postValue(RepositoryUtils.getRestaurantList());
    }

    @Test
    public void getNearbyPlacesTestsUnsuccessfulResponse() throws IOException {
        Call<NearbyPlaceResponse> call = APIUtils.getCallMock(false, APIUtils.getNearbyPlaceResponse());
        Mockito.when(placesApiMock.getNearbyPlaces(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(call);
        String expectedLocationStringQuery = expectedLocation.getLatitude() + "," + expectedLocation.getLongitude();

        placeRepository.getNearbyPlaces(expectedLocation);

        Mockito.verify(placesApiMock).getNearbyPlaces(fakeApiKey, expectedLocationStringQuery, expectedRadiusQuery, expectedRestaurantQuery);
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

        Mockito.verify(placesApiMock).getNearbyPlaces(fakeApiKey, expectedLocationStringQuery, expectedRadiusQuery, expectedRestaurantQuery);
        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.CONNECTION_ERROR);

    }

    //endregion

    //region getRequestedPlaces

    @Test
    public void getRequestedPlacesTestsSuccess() throws IOException {
        String input = "input";
        Call<AutocompleteResponse> call = APIUtils.getCallMock(true, APIUtils.getRequestedPlaceResponse());
        String expectedLocationStringQuery = expectedLocation.getLatitude() + "," + expectedLocation.getLongitude();
        Mockito.when(placesApiMock.getRequestedPlaces(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(call);
        LatLng latLng = new LatLng(expectedLocation.getLatitude(), expectedLocation.getLongitude());

        placeRepository.getRequestedRestaurants(input, latLng);

        Mockito.verify(placesApiMock).getRequestedPlaces(fakeApiKey, language, expectedLocationStringQuery, expectedRadiusQuery, input);
        Mockito.verify(call).execute();
        Mockito.verify(requestedRestaurantList).postValue(RepositoryUtils.getRequestedRestaurantList());
    }


    @Test
    public void getRequestedPlacesTestsUnsuccessfulResponse() throws IOException {
        String input = "input";
        Call<AutocompleteResponse> call = APIUtils.getCallMock(false, APIUtils.getRequestedPlaceResponse());
        String expectedLocationStringQuery = expectedLocation.getLatitude() + "," + expectedLocation.getLongitude();
        Mockito.when(placesApiMock.getRequestedPlaces(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(call);
        LatLng latLng = new LatLng(expectedLocation.getLatitude(), expectedLocation.getLongitude());

        placeRepository.getRequestedRestaurants(input, latLng);

        Mockito.verify(placesApiMock).getRequestedPlaces(fakeApiKey, language, expectedLocationStringQuery, expectedRadiusQuery, input);
        Mockito.verify(call).execute();
        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.UNSUCCESSFUL_RESPONSE);

    }

    @Test
    public void getRequestedPlacesTestIOException() throws IOException {
        String input = "input";
        Call<AutocompleteResponse> call = APIUtils.getCallMock(false, APIUtils.getRequestedPlaceResponse());
        String expectedLocationStringQuery = expectedLocation.getLatitude() + "," + expectedLocation.getLongitude();
        LatLng latLng = new LatLng(expectedLocation.getLatitude(), expectedLocation.getLongitude());
        Mockito.when(placesApiMock.getRequestedPlaces(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(call);
        Mockito.doThrow(new IOException()).when(call).execute();


        placeRepository.getRequestedRestaurants(input, latLng);

        Mockito.verify(placesApiMock).getRequestedPlaces(fakeApiKey, language, expectedLocationStringQuery, expectedRadiusQuery, input);
        Mockito.verify(call).execute();
        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.CONNECTION_ERROR);
    }

    //endregion

    //region updateRestaurantDetails
    @Test
    public void updateRestaurantDetailsTestSuccess() throws IOException {

        Call<DetailsPlaceResponse> call = APIUtils.getCallMock(true, APIUtils.getDetailsPlaceResponse());
        String expectedRestaurantId = "42";
        List<Restaurant> expectedRestaurantList = RepositoryUtils.getRestaurantList();
        Mockito.when(placesApiMock.getDetailsById(fakeApiKey, expectedRestaurantId)).thenReturn(call);
        Mockito.when(restaurantListLiveDataMock.getValue()).thenReturn(expectedRestaurantList);


        placeRepository.updateRestaurantDetails(expectedRestaurantId);

        Mockito.verify(placesApiMock).getDetailsById(fakeApiKey, expectedRestaurantId);
        Mockito.verify(restaurantListLiveDataMock).getValue();
        Mockito.verify(call).execute();
        Mockito.verify(restaurantListLiveDataMock).postValue(expectedRestaurantList);


    }

    @Test
    public void updateRestaurantDetailsUnsuccessfulResponse() throws IOException {

        Call<DetailsPlaceResponse> call = APIUtils.getCallMock(false, APIUtils.getDetailsPlaceResponse());
        String expectedRestaurantId = "42";
        List<Restaurant> expectedRestaurantList = RepositoryUtils.getRestaurantList();
        Mockito.when(placesApiMock.getDetailsById(fakeApiKey, expectedRestaurantId)).thenReturn(call);
        Mockito.when(restaurantListLiveDataMock.getValue()).thenReturn(expectedRestaurantList);

        placeRepository.updateRestaurantDetails(expectedRestaurantId);

        Mockito.verify(placesApiMock).getDetailsById(fakeApiKey, expectedRestaurantId);
        Mockito.verify(restaurantListLiveDataMock).getValue();
        Mockito.verify(call).execute();
        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.UNSUCCESSFUL_RESPONSE);

    }

    @Test
    public void updateRestaurantDetailsIOException() throws IOException {

        Call<DetailsPlaceResponse> call = APIUtils.getCallMock(true, APIUtils.getDetailsPlaceResponse());
        String expectedRestaurantId = "42";
        List<Restaurant> expectedRestaurantList = RepositoryUtils.getRestaurantList();
        Mockito.when(placesApiMock.getDetailsById(fakeApiKey, expectedRestaurantId)).thenReturn(call);
        Mockito.when(restaurantListLiveDataMock.getValue()).thenReturn(expectedRestaurantList);
        Mockito.doThrow(new IOException()).when(call).execute();

        placeRepository.updateRestaurantDetails(expectedRestaurantId);

        Mockito.verify(placesApiMock).getDetailsById(fakeApiKey, expectedRestaurantId);
        Mockito.verify(restaurantListLiveDataMock).getValue();
        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.CONNECTION_ERROR);

    }

    //endregion

    //region getRestaurantDetails
    @Test
    public void getRestaurantDetailsTestSuccess() throws IOException {

        Call<DetailsPlaceResponse> call = APIUtils.getCallMock(true, APIUtils.getDetailsPlaceResponse());
        String expectedRestaurantId = "42";
        Restaurant expectedRestaurant = RepositoryUtils.getRestaurantList().get(0);
        Mockito.when(placesApiMock.getDetailsById(fakeApiKey, expectedRestaurantId)).thenReturn(call);

        placeRepository.getRestaurantDetails(expectedRestaurantId);
        expectedRestaurant.setHasDetails(true);

        Mockito.verify(placesApiMock).getDetailsById(fakeApiKey, expectedRestaurantId);
        Mockito.verify(call).execute();
        Mockito.verify(restaurantLiveDataMock).postValue(expectedRestaurant);

    }

    @Test
    public void getRestaurantDetailsUnsuccessfulResponse() throws IOException {

        Call<DetailsPlaceResponse> call = APIUtils.getCallMock(false, APIUtils.getDetailsPlaceResponse());
        String expectedRestaurantId = "42";
        Mockito.when(placesApiMock.getDetailsById(fakeApiKey, expectedRestaurantId)).thenReturn(call);

        placeRepository.getRestaurantDetails(expectedRestaurantId);

        Mockito.verify(placesApiMock).getDetailsById(fakeApiKey, expectedRestaurantId);
        Mockito.verify(call).execute();
        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.UNSUCCESSFUL_RESPONSE);

    }


    @Test
    public void getRestaurantDetailsIOException() throws IOException {

        Call<DetailsPlaceResponse> call = APIUtils.getCallMock(true, APIUtils.getDetailsPlaceResponse());
        String expectedRestaurantId = "42";
        Mockito.when(placesApiMock.getDetailsById(fakeApiKey, expectedRestaurantId)).thenReturn(call);
        Mockito.doThrow(new IOException()).when(call).execute();

        placeRepository.getRestaurantDetails(expectedRestaurantId);

        Mockito.verify(placesApiMock).getDetailsById(fakeApiKey, expectedRestaurantId);
        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.CONNECTION_ERROR);

    }

    //endregion

    //region updateRequestedRestaurant
    @Test
    public void updateRequestedRestaurantTestSuccess() throws IOException {

        Call<DetailsPlaceResponse> detailsCall = APIUtils.getCallMock(true, APIUtils.getDetailsPlaceResponse());
        String expectedRestaurantId = "42";
        List<Restaurant> expectedRestaurantList = RepositoryUtils.getRestaurantList();
        Mockito.when(placesApiMock.getDetailsById(fakeApiKey, expectedRestaurantId)).thenReturn(detailsCall);
        Mockito.when(requestedRestaurantList.getValue()).thenReturn(expectedRestaurantList);

        placeRepository.updateRequestedRestaurantDetails(expectedRestaurantId);

        Mockito.verify(placesApiMock).getDetailsById(fakeApiKey, expectedRestaurantId);
        Mockito.verify(requestedRestaurantList).getValue();
        Mockito.verify(detailsCall).execute();
        Mockito.verify(requestedRestaurantList).postValue(expectedRestaurantList);

    }

    @Test
    public void updateRequestedRestaurantTestUnsuccessfulResponse() throws IOException {

        Call<DetailsPlaceResponse> detailsCall = APIUtils.getCallMock(false, APIUtils.getDetailsPlaceResponse());
        String expectedRestaurantId = "42";
        Mockito.when(placesApiMock.getDetailsById(fakeApiKey, expectedRestaurantId)).thenReturn(detailsCall);

        placeRepository.updateRequestedRestaurantDetails(expectedRestaurantId);

        Mockito.verify(placesApiMock).getDetailsById(fakeApiKey, expectedRestaurantId);
        Mockito.verify(requestedRestaurantList).getValue();
        Mockito.verify(detailsCall).execute();
        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.UNSUCCESSFUL_RESPONSE);

    }

    @Test
    public void updateRequestedRestaurantTestIOException() throws IOException {

        Call<DetailsPlaceResponse> detailsCall = APIUtils.getCallMock(false, APIUtils.getDetailsPlaceResponse());
        String expectedRestaurantId = "42";
        Mockito.when(placesApiMock.getDetailsById(fakeApiKey, expectedRestaurantId)).thenReturn(detailsCall);
        Mockito.doThrow(new IOException()).when(detailsCall).execute();

        placeRepository.updateRequestedRestaurantDetails(expectedRestaurantId);

        Mockito.verify(placesApiMock).getDetailsById(fakeApiKey, expectedRestaurantId);
        Mockito.verify(requestedRestaurantList).getValue();
        Mockito.verify(detailsCall).execute();
        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.CONNECTION_ERROR);

    }

    //endregion


}