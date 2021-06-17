package com.openclassrooms.oc_p7.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.oc_p7.databinding.FragmentListWorkmatesBinding;
import com.openclassrooms.oc_p7.services.dummies.DummyWorkmateGenerator;
import com.openclassrooms.oc_p7.view_models.WorkmateListViewModel;
import com.openclassrooms.oc_p7.views.adapters.WorkmateAdapter;

public class WorkmateListFragment extends Fragment {

    private final String TAG = "WorkmatesFragment";
    private FragmentListWorkmatesBinding fragmentListWorkmatesBinding;

    private WorkmateListViewModel workmateListViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentListWorkmatesBinding = FragmentListWorkmatesBinding.inflate(LayoutInflater.from(getContext()));

        workmateListViewModel =
                new ViewModelProvider(this).get(WorkmateListViewModel.class);

        initRecyclerView();

        workmateListViewModel.initWorkmateList();


        return fragmentListWorkmatesBinding.getRoot();
    }

    public void initRecyclerView() {
        Log.d(TAG, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        fragmentListWorkmatesBinding.workmateRecyclerView.setAdapter(new WorkmateAdapter(DummyWorkmateGenerator.generateWorkmates()));
        fragmentListWorkmatesBinding.workmateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}