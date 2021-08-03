package com.openclassrooms.oc_p7;

import com.openclassrooms.oc_p7.repositories.WorkmateRepository;
import com.openclassrooms.oc_p7.view_models.WorkmateViewModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkmateViewModelTests {

    private WorkmateViewModel workmateViewModel;

    private WorkmateRepository workmateRepositoryMock = Mockito.mock(WorkmateRepository.class);

    @Before
    public void setup() {
        workmateViewModel = new WorkmateViewModel(workmateRepositoryMock);

    }

    @Test
    public void getWorkmateListTest() {
        //Verify that vm calls method from repo
        workmateViewModel.getWorkmateList();

        Mockito.verify(workmateRepositoryMock).getWorkmateList();

        Mockito.verifyNoMoreInteractions(workmateRepositoryMock);
    }

    @Test
    public void constructorTest() {
        Assert.assertEquals(workmateViewModel.workmateListLiveData, workmateRepositoryMock.workmateListLiveData);


    }


}
