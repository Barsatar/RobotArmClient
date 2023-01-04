package com.example.robotarmclient.ui.programcontrol;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProgramControlViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ProgramControlViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is program control fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}