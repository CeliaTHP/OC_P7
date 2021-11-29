package com.openclassrooms.oc_p7.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.FragmentHomeBinding;
import com.openclassrooms.oc_p7.services.utils.OnDestinationChangedEvent;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding fragmentHomeBinding;
    private OnDestinationChangedEvent onDestinationChangedEvent = new OnDestinationChangedEvent();

    private String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);

        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBottomNavigation();
    }


    private void setBottomNavigation() {
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        if (getActivity() != null) {
            NavController navController = ((NavHostFragment) getChildFragmentManager().findFragmentById(R.id.bottom_nav_host_fragment)).getNavController();
            navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                    Log.d(TAG, "onDestinationChanged name : " + destination.getDisplayName() + "navName : " + destination.getNavigatorName() + navController.getClass());
                    onDestinationChangedEvent.setDestinationDisplayName(destination.getDisplayName());
                    EventBus.getDefault().post(onDestinationChangedEvent);
                }
            });
            NavigationUI.setupWithNavController(fragmentHomeBinding.bottomNavView, navController);

        }
    }

}
