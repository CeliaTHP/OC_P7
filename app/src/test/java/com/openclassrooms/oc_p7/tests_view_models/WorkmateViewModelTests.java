package com.openclassrooms.oc_p7.tests_view_models;

import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;
import com.openclassrooms.oc_p7.view_models.WorkmateViewModel;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WorkmateViewModelTests {

    private WorkmateViewModel workmateViewModel;

    private final WorkmateRepository workmateRepositoryMock = Mockito.mock(WorkmateRepository.class);
    MutableLiveData<List<Restaurant>> restaurantListLiveDataMock = (MutableLiveData<List<Restaurant>>) Mockito.mock(MutableLiveData.class);

    @Before
    public void setup() {
        workmateViewModel = new WorkmateViewModel(workmateRepositoryMock);

    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(workmateRepositoryMock);
    }

    @Test
    public void getWorkmateListTest() {
        //Verify that vm calls method from repo
        workmateViewModel.getWorkmateList();

        Mockito.verify(workmateRepositoryMock).getWorkmateList();

    }

    @Test
    public void getWorkmateForRestaurantListTest() {

        //Verify that vm calls method from repo
        workmateViewModel.getWorkmateForRestaurantList(restaurantListLiveDataMock);

        Mockito.verify(workmateRepositoryMock).getWorkmatesForRestaurantList(restaurantListLiveDataMock);

    }

    @Test
    public void constructorTest() {
        Assert.assertEquals(workmateViewModel.workmateListLiveData, workmateRepositoryMock.getWorkmateListLiveData());

    }


}
