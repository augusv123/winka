package com.google.firebase.codelab.friendlychat.MiCuenta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class MiCuentaViewModal extends ViewModel {

    private MutableLiveData<String> mText;

    public MiCuentaViewModal() {
        mText = new MutableLiveData<>();
        mText.setValue("segundo fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}