package com.openclassrooms.oc_p7.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.callbacks.OnWorkmateClickListener;
import com.openclassrooms.oc_p7.databinding.FragmentListWorkmatesBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.services.factories.WorkmateViewModelFactory;
import com.openclassrooms.oc_p7.view_models.WorkmateViewModel;
import com.openclassrooms.oc_p7.views.activities.DetailsActivity;
import com.openclassrooms.oc_p7.views.adapters.WorkmateAdapter;

import java.util.Collections;

public class WorkmateListFragment extends Fragment {

    private final String TAG = "WorkmatesFragment";
    private FragmentListWorkmatesBinding fragmentListWorkmatesBinding;

    private WorkmateViewModel workmateViewModel;
    private WorkmateAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentListWorkmatesBinding = FragmentListWorkmatesBinding.inflate(LayoutInflater.from(getContext()));

        initViewModels();
        initObservers();
        initRecyclerView();

        workmateViewModel.getWorkmateList();


        return fragmentListWorkmatesBinding.getRoot();
    }

    private void initViewModels() {
        WorkmateViewModelFactory workmateViewModelFactory = Injection.provideWorkmateViewModelFactory(getContext());
        workmateViewModel = ViewModelProviders.of(this, workmateViewModelFactory).get(WorkmateViewModel.class);
    }


    private void initObservers() {
        workmateViewModel.workmateListLiveData.observe(getViewLifecycleOwner(), workmates -> {
            Log.d(TAG, "workmateList observer from Fragment");
            for (Workmate workmate : workmates) {
                Log.d(TAG, workmate.getName() + " " + workmate.getPicUrl());
            }
            adapter.setData(workmates);
            adapter.notifyDataSetChanged();
        });
    }


    private void initRecyclerView() {
        adapter = new WorkmateAdapter(Collections.emptyList(), false, new OnWorkmateClickListener() {
            @Override
            public void onWorkmateClick(Workmate workmate) {
                if ((workmate.getRestaurantId() != null)) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra("restaurantId", workmate.getRestaurantId());
                    startActivity(intent);
                } else {
                    Toast.makeText(fragmentListWorkmatesBinding.getRoot().getContext(), getString(R.string.workmate_text_no_lunch, workmate.getName()), Toast.LENGTH_LONG).show();
                }

            }


        });
        fragmentListWorkmatesBinding.workmateRecyclerView.setAdapter(adapter);
        fragmentListWorkmatesBinding.workmateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


}