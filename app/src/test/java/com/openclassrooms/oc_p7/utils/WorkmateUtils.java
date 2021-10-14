package com.openclassrooms.oc_p7.utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.oc_p7.models.Workmate;

import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class WorkmateUtils {

    @SuppressWarnings("unchecked")
    public static <T> Call<T> getCallMock(Boolean isSuccessFull, T response) throws IOException {
        Call<T> callMock = Mockito.mock(Call.class);
        Response<T> responseMock = Mockito.mock(Response.class);
        Mockito.when(callMock.execute()).thenReturn(responseMock);
        Mockito.when(responseMock.isSuccessful()).thenReturn(isSuccessFull);
        Mockito.when(responseMock.body()).thenReturn(response);
        return callMock;
    }

    public static <T> Task<QuerySnapshot> getTaskMock(Boolean isSuccessFull, List<DocumentSnapshot> response) throws IOException {
        Task<QuerySnapshot> taskMock = Mockito.mock(Task.class);

        QuerySnapshot querySnapshotMock = Mockito.mock(QuerySnapshot.class);
        Mockito.when(taskMock.getResult()).thenReturn(querySnapshotMock);
        Mockito.when(querySnapshotMock.getDocuments()).thenReturn(response);
        //Mockito.when(Tasks.await(Mockito.any()). do nothing

        return taskMock;
    }


    public static List<DocumentSnapshot> getDocumentSnapshotList() {
        List<DocumentSnapshot> documentSnapshotList = new ArrayList<>();

        DocumentSnapshot documentSnapshotMock = Mockito.mock(DocumentSnapshot.class);

        Mockito.when(documentSnapshotMock.get("uid")).thenReturn("testId");
        Mockito.when(documentSnapshotMock.get("name")).thenReturn("testName");
        Mockito.when(documentSnapshotMock.get("email")).thenReturn("testEmail");
        Mockito.when(documentSnapshotMock.get("picUrl")).thenReturn("testPicUrl");
        Mockito.when(documentSnapshotMock.get("restaurantId")).thenReturn("testRestaurantId");
        Mockito.when(documentSnapshotMock.get("restaurantName")).thenReturn("testRestaurantName");


        documentSnapshotList.add(documentSnapshotMock);

        return documentSnapshotList;

    }

    public static List<Workmate> getWorkmateList() {
        List<Workmate> workmateList = new ArrayList<>();
        Workmate workmateTest = new Workmate("testId", "testName", "testEmail",
                "testPicUrl");
        workmateTest.setRestaurantId("testRestaurantId");
        workmateTest.setRestaurantName("testRestaurantName");

        workmateList.add(workmateTest);

        return workmateList;
    }
}