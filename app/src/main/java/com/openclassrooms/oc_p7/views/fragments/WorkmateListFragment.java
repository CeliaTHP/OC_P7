package com.openclassrooms.oc_p7.views.fragments;

import android.content.Context;
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

import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.callbacks.OnWorkmateClickListener;
import com.openclassrooms.oc_p7.databinding.FragmentListWorkmatesBinding;
import com.openclassrooms.oc_p7.injections.Injection;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.services.factories.WorkmateViewModelFactory;
import com.openclassrooms.oc_p7.services.utils.OnWorkmateQueryEvent;
import com.openclassrooms.oc_p7.view_models.WorkmateViewModel;
import com.openclassrooms.oc_p7.views.activities.DetailsActivity;
import com.openclassrooms.oc_p7.views.adapters.WorkmateAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkmateListFragment extends Fragment {

    private final String TAG = "WorkmatesFragment";

    private FragmentListWorkmatesBinding fragmentListWorkmatesBinding;

    private List<Workmate> workmateList = new ArrayList<>();
    private List<Workmate> filteredList = new ArrayList<>();

    private WorkmateViewModel workmateViewModel;
    private WorkmateAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentListWorkmatesBinding = FragmentListWorkmatesBinding.inflate(LayoutInflater.from(getContext()));

        EventBus.getDefault().register(this);

        initViewModels();
        initObservers();
        initRecyclerView();

        workmateViewModel.getWorkmateList();


        return fragmentListWorkmatesBinding.getRoot();
    }

    @Subscribe
    public void onQueryEvent(OnWorkmateQueryEvent onWorkmateQueryEvent) {
        if (onWorkmateQueryEvent.getQueryForWorkmate() != null) {
            String query = onWorkmateQueryEvent.getQueryForWorkmate();
            filteredList.clear();
            for (Workmate workmate : workmateList) {
                if (workmate.getName().toLowerCase().contains(query.toLowerCase()) ||
                        workmate.getRestaurantName() != null && workmate.getRestaurantName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(workmate);
                }
            }
            if (adapter != null) {
                adapter.setData(filteredList);
            }
        }
    }


    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

    }

    private void initViewModels() {
        WorkmateViewModelFactory workmateViewModelFactory = Injection.provideWorkmateViewModelFactory(FirebaseFirestore.getInstance());
        workmateViewModel = ViewModelProviders.of(this, workmateViewModelFactory).get(WorkmateViewModel.class);
    }


    private void initObservers() {
        workmateViewModel.workmateListLiveData.observe(getViewLifecycleOwner(), workmateList -> {
            this.workmateList = workmateList;
            for (Workmate workmate : workmateList) {
                Log.d(TAG, workmate.getName() + " " + workmate.getPicUrl());
            }
            adapter.setData(workmateList);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}