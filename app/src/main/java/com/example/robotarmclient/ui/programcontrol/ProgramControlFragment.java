package com.example.robotarmclient.ui.programcontrol;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.robotarmclient.MainActivity;
import com.example.robotarmclient.RobotArmManager;
import com.example.robotarmclient.databinding.FragmentProgramControlBinding;

public class ProgramControlFragment extends Fragment {

    private FragmentProgramControlBinding binding;
    RobotArmManager __robotArmManager__;

    MainActivity activity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProgramControlViewModel programControlViewModel =
                new ViewModelProvider(this).get(ProgramControlViewModel.class);

        binding = FragmentProgramControlBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProgramControl;
        programControlViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        activity = (MainActivity) getActivity();

        binding.buttonReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.test.run();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}