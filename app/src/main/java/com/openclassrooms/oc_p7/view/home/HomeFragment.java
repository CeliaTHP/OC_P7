package com.openclassrooms.oc_p7.view.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding fragmentHomeBinding;
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
        //Verifnonull
        if (getActivity() != null) {
            NavController navController = ((NavHostFragment) getChildFragmentManager().findFragmentById(R.id.bottom_nav_host_fragment)).getNavController();

            NavigationUI.setupWithNavController(fragmentHomeBinding.bottomNavView, navController);
        }
    }
}
