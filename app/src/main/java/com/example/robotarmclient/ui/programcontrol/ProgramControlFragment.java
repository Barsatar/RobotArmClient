package com.example.robotarmclient.ui.programcontrol;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.robotarmclient.databinding.FragmentProgramControlBinding;

public class ProgramControlFragment extends Fragment {

    private FragmentProgramControlBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProgramControlViewModel programControlViewModel =
                new ViewModelProvider(this).get(ProgramControlViewModel.class);

        binding = FragmentProgramControlBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProgramControl;
        programControlViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}