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
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;
import com.openclassrooms.oc_p7.services.firestore_helpers.WorkmateHelper;
import com.openclassrooms.oc_p7.utils.WorkmateUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;


public class WorkmateRepositoryTests {


    private FirebaseFirestore firebaseFirestoreMock = Mockito.mock(FirebaseFirestore.class);
    private CollectionReference collectionReferenceMock = Mockito.mock(CollectionReference.class);
    private DocumentReference documentReferenceMock;


    private final Executor executor = MoreExecutors.newDirectExecutorService();
    private WorkmateRepository workmateRepository;

    private final MutableLiveData<List<Workmate>> workmateMutableLiveDataListMock = (MutableLiveData<List<Workmate>>) Mockito.mock(MutableLiveData.class);


    private final MutableLiveData<ErrorCode> errorCodeMutableLiveDataMock = (MutableLiveData<ErrorCode>) Mockito.mock(MutableLiveData.class);

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        workmateRepository = new WorkmateRepository(firebaseFirestoreMock, executor, workmateMutableLiveDataListMock);
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(workmateMutableLiveDataListMock, errorCodeMutableLiveDataMock);
    }

    //TODO Mockito.mockStatic
    //https://frontbackend.com/java/how-to-mock-static-methods-with-mockito or POWERMOCKITO
    @Test
    public void getWorkmateListTestsSuccess() throws IOException {
        List<Workmate> expectedWorkmateList = WorkmateUtils.getWorkmateList();

        Mockito.mockStatic(Tasks.class);
        Task<QuerySnapshot> taskMock = WorkmateUtils.getTaskMock(true, WorkmateUtils.getDocumentSnapshotList());
        Mockito.when(WorkmateHelper.getWorkmatesCollection(firebaseFirestoreMock)).thenReturn(collectionReferenceMock);
        Mockito.when(WorkmateHelper.getAllWorkmates(firebaseFirestoreMock)).thenReturn(taskMock);


        //TODO make DocumentSnapshot from object
        workmateRepository.getWorkmateList();

        //can't verify the post value

        Mockito.verify(workmateRepository.workmateListLiveData).postValue(expectedWorkmateList);


    }

}
