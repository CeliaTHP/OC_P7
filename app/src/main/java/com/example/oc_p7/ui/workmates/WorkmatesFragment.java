package com.example.oc_p7.ui.workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.oc_p7.R;

public class WorkmatesFragment extends Fragment {

    private WorkmatesViewModel workmatesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        workmatesViewModel =
                new ViewModelProvider(this).get(WorkmatesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_workmates, container, false);
        final TextView textView = root.findViewById(R.id.text_workmates);
        workmatesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}