package com.google.firebase.codelab.friendlychat.Inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class InicioViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InicioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Inicio fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}