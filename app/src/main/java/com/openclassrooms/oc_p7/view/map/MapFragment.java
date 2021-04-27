package com.openclassrooms.oc_p7.view.map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.FragmentMapBinding;
import com.openclassrooms.oc_p7.view.LoginActivity;
import com.openclassrooms.oc_p7.view_model.LoginViewModel;

public class MapFragment extends Fragment {

    private static final String TAG = "MapFragment";

    private MapViewModel mapViewModel;
    private FragmentMapBinding fragmentMapBinding;

    private LoginViewModel loginViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fragmentMapBinding = FragmentMapBinding.inflate(LayoutInflater.from(this.getContext()), null, false);
        mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        final TextView textView = root.findViewById(R.id.text_map);

        fragmentMapBinding.testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginViewModel.isUserConnected())
                Log.d(TAG, loginViewModel.getUserDisplayName());
                else
                    Log.d(TAG, "User not connected");
            }
        });

        mapViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return fragmentMapBinding.getRoot();
    }


}