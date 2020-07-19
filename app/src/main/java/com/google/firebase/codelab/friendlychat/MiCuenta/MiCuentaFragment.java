package com.google.firebase.codelab.friendlychat.MiCuenta;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.codelab.friendlychat.R;

public class MiCuentaFragment extends Fragment {

    private MiCuentaViewModal miCuentaViewModal;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        miCuentaViewModal =
                ViewModelProviders.of(this).get(MiCuentaViewModal.class);
        View root = inflater.inflate(R.layout.micuenta, container, false);
        final TextView textView = root.findViewById(R.id.textView2);
        miCuentaViewModal.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}