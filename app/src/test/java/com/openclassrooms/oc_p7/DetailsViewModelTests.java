package com.openclassrooms.oc_p7;

import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;
import com.openclassrooms.oc_p7.view_models.DetailsViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)

public class DetailsViewModelTests {

    private DetailsViewModel detailsViewModel;

    private WorkmateRepository workmateRepositoryMock = Mockito.mock(WorkmateRepository.class);
    private PlaceRepository placeRepositoryMock = Mockito.mock(PlaceRepository.class);

    @Before
    public void setup() {
        detailsViewModel = new DetailsViewModel(workmateRepositoryMock, placeRepositoryMock);
    }

    @Test
    public void getWorkmateForRestaurantTests() {
        Restaurant expectedRestaurant = Mockito.mock(Restaurant.class);

        detailsViewModel.getWorkmatesForRestaurant(expectedRestaurant);

        Mockito.verify(workmateRepositoryMock).getWorkmatesForRestaurant(expectedRestaurant);
        Mockito.verifyNoMoreInteractions(workmateRepositoryMock);
    }

    @Test
    public void getRestaurantDetails() {
        String expectedRestaurantId = "expectedRestaurantId";

        detailsViewModel.getRestaurantDetails(expectedRestaurantId);

        Mockito.verify(placeRepositoryMock).getRestaurantDetails(expectedRestaurantId);
        Mockito.verifyNoMoreInteractions(placeRepositoryMock);
    }


}
