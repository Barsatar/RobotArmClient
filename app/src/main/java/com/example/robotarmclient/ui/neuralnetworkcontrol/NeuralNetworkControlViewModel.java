package com.example.robotarmclient.ui.neuralnetworkcontrol;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NeuralNetworkControlViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public NeuralNetworkControlViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is neural network control fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}