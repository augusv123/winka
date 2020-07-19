package com.google.firebase.codelab.friendlychat.Calendario;

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

import com.google.firebase.codelab.friendlychat.Inicio.InicioViewModel;
import com.google.firebase.codelab.friendlychat.R;

public class CalendarioFragment extends Fragment {
    private CalendarioViewModel calendarioViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarioViewModel =
                ViewModelProviders.of(this).get(CalendarioViewModel.class);
        View root = inflater.inflate(R.layout.inicio, container, false);

        return root;
    }
}
