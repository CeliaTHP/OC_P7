package com.openclassrooms.oc_p7.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassrooms.oc_p7.databinding.FragmentListRestaurantsBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.services.factories.ListViewModelFactory;
import com.openclassrooms.oc_p7.view_models.ListViewModel;

public class RestaurantListFragment extends Fragment {

    private final static String TAG = "ListFragment";
    private ListViewModel listViewModel;
    private FragmentListRestaurantsBinding fragmentListRestaurantsBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentListRestaurantsBinding = FragmentListRestaurantsBinding.inflate(LayoutInflater.from(this.getContext()));

        initViewModels();
        initRecyclerView();
        searchById();

        return fragmentListRestaurantsBinding.getRoot();
    }

    private void initViewModels() {
        ListViewModelFactory listViewModelFactory = Injection.provideListViewModelFactory(getContext());
        listViewModel =
                ViewModelProviders.of(this, listViewModelFactory).get(ListViewModel.class);

    }


    private void initRecyclerView() {
        // fragmentListBinding.restaurantRecyclerView.setAdapter(new RestaurantAdapter(listViewModel.getAllRestaurant()));
        fragmentListRestaurantsBinding.restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void searchById() {
        listViewModel.getDetailsById();
    }

}