package com.google.firebase.codelab.friendlychat.NuevoPedido;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.iid.FirebaseInstanceId;

public class NuevoPedidoFragment extends Fragment {
    private static final String TAG = "";
    private NuevoPedidoViewModel nuevoPedidoViewModel;
    private Button crearPedidoButton;
    private EditText inputTarjeta;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        nuevoPedidoViewModel =
                ViewModelProviders.of(this).get(NuevoPedidoViewModel.class);
        View root = inflater.inflate(R.layout.nuevo_pedido, container, false);

        crearPedidoButton = (Button) root.findViewById(R.id.crearPedidoButton);
        inputTarjeta = (EditText) root.findViewById(R.id.inputTarjeta);

        crearPedidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return root;
    }
    private void sendMessage(){


    }


}
