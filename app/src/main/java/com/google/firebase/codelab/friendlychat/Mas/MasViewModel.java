package com.google.firebase.codelab.friendlychat.Mas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MasViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public MasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is mas fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
