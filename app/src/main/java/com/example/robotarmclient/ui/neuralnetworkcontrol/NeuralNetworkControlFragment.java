package com.example.robotarmclient.ui.neuralnetworkcontrol;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.robotarmclient.databinding.FragmentNeuralNetworkControlBinding;

public class NeuralNetworkControlFragment extends Fragment {

    private FragmentNeuralNetworkControlBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NeuralNetworkControlViewModel neuralNetworkControlViewModel =
                new ViewModelProvider(this).get(NeuralNetworkControlViewModel.class);

        binding = FragmentNeuralNetworkControlBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNeuralNetworkControl;
        neuralNetworkControlViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}