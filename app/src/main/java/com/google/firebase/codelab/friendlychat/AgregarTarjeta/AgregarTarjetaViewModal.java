package com.google.firebase.codelab.friendlychat.AgregarTarjeta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class AgregarTarjetaViewModal extends ViewModel {

    private MutableLiveData<String> mText;

    public AgregarTarjetaViewModal() {
        mText = new MutableLiveData<>();
        mText.setValue("segundo fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}