package com.openclassrooms.oc_p7.tests_repositories;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.MoreExecutors;
import com.openclassrooms.oc_p7.models.ErrorCode;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;
import com.openclassrooms.oc_p7.services.firestore_helpers.WorkmateHelper;
import com.openclassrooms.oc_p7.utils.WorkmateUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

public class WorkmateRepositoryTests {

    private final MutableLiveData<List<Workmate>> workmateListLiveDataMock = (MutableLiveData<List<Workmate>>) Mockito.mock(MutableLiveData.class);


    private final Executor executor = MoreExecutors.newDirectExecutorService();
    private WorkmateRepository workmateRepository;

    private final MutableLiveData<Workmate> workmateMutableLiveData = (MutableLiveData<Workmate>) Mockito.mock(MutableLiveData.class);


    private final MutableLiveData<ErrorCode> errorCodeMutableLiveDataMock = (MutableLiveData<ErrorCode>) Mockito.mock(MutableLiveData.class);

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        workmateRepository = new WorkmateRepository(executor);
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(workmateRepository);
    }

    @Test
    public void getWorkmateListTestsSuccess() throws IOException {


        //TODO Mockito.mockStatic
        //https://frontbackend.com/java/how-to-mock-static-methods-with-mockito

        Task taskMock = WorkmateUtils.getTaskMock(true, WorkmateUtils.getDocumentSnapshotList());

        Mockito.when(WorkmateHelper.getAllWorkmates()).thenReturn(taskMock);

        //TODO make DocumentSnapshot from object
        workmateRepository.getWorkmateList();

        Mockito.verify(workmateListLiveDataMock).postValue(WorkmateUtils.getWorkmateList());


    }

    @Test
    public void getNearbyPlacesTestsUnsuccessfullResponse() throws IOException {
        /*
        Call<NearbyPlaceResponse> call = APIUtils.getCallMock(false, APIUtils.getNearbyPlaceResponse());
        Mockito.when(placesApiMock.getNearbyPlaces(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(call);
        String expectedLocationStringQuery = expectedLocation.getLatitude() + "," + expectedLocation.getLongitude();

        placeRepository.getNearbyPlaces(expectedLocation);

        Mockito.verify(placesApiMock).getNearbyPlaces(BuildConfig.GoogleMapApiKey, expectedLocationStringQuery, expectedRadiusQuery, expectedRestaurantQuery);
        Mockito.verify(call).execute();
        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.UNSUCCESSFUL_RESPONSE);


         */
    }

    @Test
    public void getNearbyPlacesTestIOException() throws IOException {
        /*

        Call<NearbyPlaceResponse> call = APIUtils.getCallMock(true, APIUtils.getNearbyPlaceResponse());
        String expectedLocationStringQuery = expectedLocation.getLatitude() + "," + expectedLocation.getLongitude();
        Mockito.when(placesApiMock.getNearbyPlaces(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(call);
        Mockito.doThrow(new IOException()).when(call).execute();

        placeRepository.getNearbyPlaces(expectedLocation);

        Mockito.verify(placesApiMock).getNearbyPlaces(BuildConfig.GoogleMapApiKey, expectedLocationStringQuery, expectedRadiusQuery, expectedRestaurantQuery);
        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.CONNECTION_ERROR);


         */
    }
}
