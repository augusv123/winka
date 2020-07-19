package com.google.firebase.codelab.friendlychat.Calendario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalendarioViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public CalendarioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is calendario fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
