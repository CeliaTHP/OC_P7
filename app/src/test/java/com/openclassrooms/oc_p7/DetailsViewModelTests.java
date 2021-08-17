package com.openclassrooms.oc_p7;

import com.google.android.gms.tasks.OnSuccessListener;
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
        OnSuccessListener<Restaurant> expectedOnSuccessListener = Mockito.mock(OnSuccessListener.class);

        detailsViewModel.getWorkmatesForRestaurant(expectedRestaurant, expectedOnSuccessListener);

        Mockito.verify(workmateRepositoryMock).getWorkmatesForRestaurant(expectedRestaurant, expectedOnSuccessListener);
        Mockito.verifyNoMoreInteractions(workmateRepositoryMock);
    }

    @Test
    public void getRestaurantDetails() {
        String expectedRestaurantId = "expectedRestaurantId";
        Restaurant expectedRestaurant = Mockito.mock(Restaurant.class);
        OnSuccessListener<Restaurant> expectedOnSuccessListener = Mockito.mock(OnSuccessListener.class);

        detailsViewModel.getRestaurantDetails(expectedRestaurantId, expectedRestaurant, expectedOnSuccessListener);

        Mockito.verify(placeRepositoryMock).getRestaurantDetails(expectedRestaurantId, expectedRestaurant, expectedOnSuccessListener);
        Mockito.verifyNoMoreInteractions(placeRepositoryMock);
    }


}
