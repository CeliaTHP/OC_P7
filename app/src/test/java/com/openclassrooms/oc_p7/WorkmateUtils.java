package com.openclassrooms.oc_p7;

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

    public static List<Workmate> getWorkmateList() {
        List<Workmate> workmateList = new ArrayList<>();
        workmateList.add(new Workmate("testId1", "firstWorkmate", "firstEmail@mock.fr", "randomPicUrl1"));
        workmateList.add(new Workmate("testId2", "secondWorkmate", "secondEmail@mock.fr", "randomPicUrl2"));
        workmateList.add(new Workmate("testId3", "thirdWorkmate", "thirdEmail@mock.fr", "randomPicUrl3"));
        return workmateList;

    }
}