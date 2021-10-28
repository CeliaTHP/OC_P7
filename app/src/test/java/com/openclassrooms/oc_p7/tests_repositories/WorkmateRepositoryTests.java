package com.openclassrooms.oc_p7.tests_repositories;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.oc_p7.models.ErrorCode;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;
import com.openclassrooms.oc_p7.services.firestore_helpers.WorkmateHelper;
import com.openclassrooms.oc_p7.utils.WorkmateUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;


public class WorkmateRepositoryTests {


    private FirebaseFirestore firebaseFirestoreMock = Mockito.mock(FirebaseFirestore.class);
    private CollectionReference collectionReferenceMock = Mockito.mock(CollectionReference.class);
    private DocumentReference documentReferenceMock;


    private final Executor executor = MoreExecutors.newDirectExecutorService();
    private WorkmateRepository workmateRepository;

    private final MutableLiveData<List<Workmate>> workmateMutableLiveDataListMock = (MutableLiveData<List<Workmate>>) Mockito.mock(MutableLiveData.class);
    private final MutableLiveData<Restaurant> restaurantMutableLiveDataMock = (MutableLiveData<Restaurant>) Mockito.mock(MutableLiveData.class);


    private final MutableLiveData<ErrorCode> errorCodeMutableLiveDataMock = (MutableLiveData<ErrorCode>) Mockito.mock(MutableLiveData.class);

    private final WorkmateHelper workmateHelper = Mockito.mock(WorkmateHelper.class);

    private static MockedStatic mockedSettings;

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();


    @Before
    public void setUp() {
        mockedSettings = Mockito.mockStatic(Tasks.class);
        workmateRepository = new WorkmateRepository(firebaseFirestoreMock, workmateHelper, executor, workmateMutableLiveDataListMock, errorCodeMutableLiveDataMock);
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(workmateMutableLiveDataListMock, restaurantMutableLiveDataMock, errorCodeMutableLiveDataMock);
        mockedSettings.close();
    }

    //TODO Mockito.mockStatic
    //https://frontbackend.com/java/how-to-mock-static-methods-with-mockito or POWERMOCKITO

    @Test
    public void getWorkmateListTestsSuccess() throws IOException {

        List<Workmate> expectedWorkmateList = WorkmateUtils.getWorkmateList();
        Task<QuerySnapshot> taskMock = WorkmateUtils.getTaskMock(true, WorkmateUtils.getDocumentSnapshotList());
        Mockito.when(workmateHelper.getWorkmatesCollection(firebaseFirestoreMock)).thenReturn(collectionReferenceMock);
        Mockito.when(workmateHelper.getAllWorkmates(firebaseFirestoreMock)).thenReturn(taskMock);

        workmateRepository.getWorkmateList();

        Mockito.verify(workmateMutableLiveDataListMock).postValue(expectedWorkmateList);

    }

    @Test
    public void getWorkmateListTestExecutionException() throws IOException, ExecutionException, InterruptedException {

        Task<QuerySnapshot> taskMock = WorkmateUtils.getTaskMock(false, WorkmateUtils.getDocumentSnapshotList());
        Mockito.when(workmateHelper.getWorkmatesCollection(firebaseFirestoreMock)).thenReturn(collectionReferenceMock);
        Mockito.when(workmateHelper.getAllWorkmates(firebaseFirestoreMock)).thenReturn(taskMock);
        Mockito.when(Tasks.await(Mockito.any())).thenThrow(new ExecutionException("testException", new Throwable()));

        workmateRepository.getWorkmateList();

        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.EXECUTION_EXCEPTION);

    }

    @Test
    public void getWorkmateListTestInterruptedException() throws IOException, ExecutionException, InterruptedException {

        Task<QuerySnapshot> taskMock = WorkmateUtils.getTaskMock(false, WorkmateUtils.getDocumentSnapshotList());
        Mockito.when(workmateHelper.getWorkmatesCollection(firebaseFirestoreMock)).thenReturn(collectionReferenceMock);
        Mockito.when(workmateHelper.getAllWorkmates(firebaseFirestoreMock)).thenReturn(taskMock);
        Mockito.when(Tasks.await(Mockito.any())).thenThrow(new InterruptedException());

        workmateRepository.getWorkmateList();

        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.INTERRUPTED_EXCEPTION);


    }


    @Test
    public void getWorkmateForRestaurantTestsSuccess() throws IOException {

        Restaurant expectedRestaurant = Mockito.mock(Restaurant.class);
        Task<QuerySnapshot> taskMock = WorkmateUtils.getTaskMock(true, WorkmateUtils.getDocumentSnapshotList());
        Mockito.when(workmateHelper.getWorkmatesForRestaurant(Mockito.any(), Mockito.any())).thenReturn(taskMock);
        Mockito.when(restaurantMutableLiveDataMock.getValue()).thenReturn(expectedRestaurant);

        workmateRepository.getWorkmatesForRestaurant(restaurantMutableLiveDataMock);

        Mockito.verify(restaurantMutableLiveDataMock).getValue();
        Mockito.verify(restaurantMutableLiveDataMock).postValue(expectedRestaurant);

    }

    @Test
    public void getWorkmateForRestaurantTestsExecutionException() throws IOException, ExecutionException, InterruptedException {

        Restaurant expectedRestaurant = Mockito.mock(Restaurant.class);
        Task<QuerySnapshot> taskMock = WorkmateUtils.getTaskMock(false, WorkmateUtils.getDocumentSnapshotList());
        Mockito.when(workmateHelper.getWorkmatesForRestaurant(Mockito.any(), Mockito.any())).thenReturn(taskMock);
        Mockito.when(restaurantMutableLiveDataMock.getValue()).thenReturn(expectedRestaurant);
        Mockito.when(Tasks.await(taskMock)).thenThrow(new ExecutionException("testExecutionException", new Throwable()));

        workmateRepository.getWorkmatesForRestaurant(restaurantMutableLiveDataMock);

        Mockito.verify(restaurantMutableLiveDataMock).getValue();
        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.EXECUTION_EXCEPTION);

    }


    @Test
    public void getWorkmateForRestaurantTestsInterruptedException() throws IOException, ExecutionException, InterruptedException {

        Restaurant expectedRestaurant = Mockito.mock(Restaurant.class);
        Task<QuerySnapshot> taskMock = WorkmateUtils.getTaskMock(false, WorkmateUtils.getDocumentSnapshotList());
        Mockito.when(workmateHelper.getWorkmatesForRestaurant(Mockito.any(), Mockito.any())).thenReturn(taskMock);
        Mockito.when(restaurantMutableLiveDataMock.getValue()).thenReturn(expectedRestaurant);
        Mockito.when(Tasks.await(taskMock)).thenThrow(new InterruptedException());

        workmateRepository.getWorkmatesForRestaurant(restaurantMutableLiveDataMock);

        Mockito.verify(restaurantMutableLiveDataMock).getValue();
        Mockito.verify(errorCodeMutableLiveDataMock).postValue(ErrorCode.INTERRUPTED_EXCEPTION);

    }




}
