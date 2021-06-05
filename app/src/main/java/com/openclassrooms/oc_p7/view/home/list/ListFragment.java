package com.openclassrooms.oc_p7.view.home.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.openclassrooms.oc_p7.databinding.FragmentListBinding;
import com.openclassrooms.oc_p7.injection.Injection;
import com.openclassrooms.oc_p7.model.Restaurant;

public class ListFragment extends Fragment {

    private final static String TAG = "ListFragment";
    private ListViewModel listViewModel;
    private FragmentListBinding fragmentListBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentListBinding = FragmentListBinding.inflate(LayoutInflater.from(this.getContext()));

        initViewModels();
        getRestaurants();

        return fragmentListBinding.getRoot();
    }

    private void initViewModels() {
        ListViewModelFactory listViewModelFactory = Injection.provideListViewModelFactory(getContext());
        listViewModel =
                ViewModelProviders.of(this, listViewModelFactory).get(ListViewModel.class);

    }

    private void getRestaurants() {
        for (Restaurant restaurant : listViewModel.getAllRestaurant())
            Log.d(TAG, restaurant.getName());
    }

}