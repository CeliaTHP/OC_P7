package com.openclassrooms.oc_p7.view.home.workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassrooms.oc_p7.databinding.FragmentWorkmatesBinding;
import com.openclassrooms.oc_p7.injection.Injection;

public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding fragmentWorkmatesBinding;

    private WorkmatesViewModel workmatesViewModel;

    private WorkmateAdapter workmateAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentWorkmatesBinding = FragmentWorkmatesBinding.inflate(LayoutInflater.from(getContext()));

        workmatesViewModel =
                new ViewModelProvider(this).get(WorkmatesViewModel.class);

        initRecyclerView();


        return fragmentWorkmatesBinding.getRoot();
    }

    public void initRecyclerView() {

        fragmentWorkmatesBinding.workmateRecyclerView.setAdapter(new WorkmateAdapter(Injection.getWorkmates()));
        fragmentWorkmatesBinding.workmateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}