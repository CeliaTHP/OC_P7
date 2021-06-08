package com.openclassrooms.oc_p7.view.home.workmates;

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
import com.openclassrooms.oc_p7.databinding.FragmentWorkmatesBinding;
import com.openclassrooms.oc_p7.service.DummyWorkmateGenerator;

public class WorkmatesFragment extends Fragment {

    private final String TAG = "WorkmatesFragment";
    private FragmentWorkmatesBinding fragmentWorkmatesBinding;

    private WorkmatesViewModel workmatesViewModel;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentWorkmatesBinding = FragmentWorkmatesBinding.inflate(LayoutInflater.from(getContext()));

        workmatesViewModel =
                new ViewModelProvider(this).get(WorkmatesViewModel.class);

        initRecyclerView();


        return fragmentWorkmatesBinding.getRoot();
    }

    public void initRecyclerView() {

        Log.d(TAG, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        fragmentWorkmatesBinding.workmateRecyclerView.setAdapter(new WorkmateAdapter(DummyWorkmateGenerator.generateWorkmates()));
        fragmentWorkmatesBinding.workmateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}