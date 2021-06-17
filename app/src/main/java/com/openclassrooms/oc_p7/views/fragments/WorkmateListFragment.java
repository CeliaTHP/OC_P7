package com.openclassrooms.oc_p7.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.oc_p7.databinding.FragmentListWorkmatesBinding;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.view_models.WorkmateListViewModel;
import com.openclassrooms.oc_p7.views.adapters.WorkmateAdapter;

import java.util.List;

public class WorkmateListFragment extends Fragment {

    private final String TAG = "WorkmatesFragment";
    private FragmentListWorkmatesBinding fragmentListWorkmatesBinding;

    private WorkmateListViewModel workmateListViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentListWorkmatesBinding = FragmentListWorkmatesBinding.inflate(LayoutInflater.from(getContext()));

        workmateListViewModel =
                new ViewModelProvider(this).get(WorkmateListViewModel.class);

        initObservers();

        workmateListViewModel.initWorkmateList();


        return fragmentListWorkmatesBinding.getRoot();
    }


    private void initObservers() {
        workmateListViewModel.workmateListLiveData.observe(getViewLifecycleOwner(), new Observer<List<Workmate>>() {
            @Override
            public void onChanged(List<Workmate> workmates) {
                initRecyclerView(workmates);

            }
        });
    }

    private void initRecyclerView(List<Workmate> workmateList) {
        Log.d(TAG, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        fragmentListWorkmatesBinding.workmateRecyclerView.setAdapter(new WorkmateAdapter(workmateList));
        fragmentListWorkmatesBinding.workmateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}