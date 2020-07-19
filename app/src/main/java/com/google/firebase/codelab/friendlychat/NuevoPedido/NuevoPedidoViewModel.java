package com.google.firebase.codelab.friendlychat.NuevoPedido;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NuevoPedidoViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public NuevoPedidoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Nuevo Pedido fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
