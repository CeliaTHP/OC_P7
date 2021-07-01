package com.openclassrooms.oc_p7.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.oc_p7.databinding.FragmentListWorkmatesBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.services.factories.WorkmateViewModelFactory;
import com.openclassrooms.oc_p7.view_models.WorkmateViewModel;
import com.openclassrooms.oc_p7.views.adapters.WorkmateAdapter;

import java.util.List;

public class WorkmateListFragment extends Fragment {

    private final String TAG = "WorkmatesFragment";
    private FragmentListWorkmatesBinding fragmentListWorkmatesBinding;

    private WorkmateViewModel workmateViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentListWorkmatesBinding = FragmentListWorkmatesBinding.inflate(LayoutInflater.from(getContext()));

        initViewModels();
        initObservers();

        workmateViewModel.getWorkmateList();


        return fragmentListWorkmatesBinding.getRoot();
    }

    private void initViewModels() {
        WorkmateViewModelFactory workmateViewModelFactory = Injection.provideWorkmateViewModelFactory(getContext());
        workmateViewModel = ViewModelProviders.of(this, workmateViewModelFactory).get(WorkmateViewModel.class);
    }


    private void initObservers() {
        workmateViewModel.workmateListLiveData.observe(getViewLifecycleOwner(), workmates -> initRecyclerView(workmates));
    }

    private void initRecyclerView(List<Workmate> workmateList) {
        Log.d(TAG, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        fragmentListWorkmatesBinding.workmateRecyclerView.setAdapter(new WorkmateAdapter(workmateList, false));
        fragmentListWorkmatesBinding.workmateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}