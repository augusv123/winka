package com.google.firebase.codelab.friendlychat.AgregarTarjeta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.codelab.friendlychat.HomeActivity;
import com.google.firebase.codelab.friendlychat.Inicio.InicioViewModel;
import com.google.firebase.codelab.friendlychat.MainActivity;
import com.google.firebase.codelab.friendlychat.MiCuenta.MiCuentaFragment;
import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import java.io.IOException;

public class AgregarTarjetaFragment extends Fragment {
    private AgregarTarjetaViewModal calendarioViewModel;
    private  String[] tarjetas = { "BBVA", "Galicia", "ICBC", "Santander Rio"};
    private FirebaseAuth mFirebaseAuth  = FirebaseAuth.getInstance();
    private Button backButton;
    private FirebaseUser mFirebaseUser= mFirebaseAuth.getCurrentUser();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference getTarjetasCondition = mRootRef.child(mFirebaseAuth.getUid()).child("Tarjetas");
    private Button addCard;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarioViewModel =
                ViewModelProviders.of(this).get(AgregarTarjetaViewModal.class);
        View root = inflater.inflate(R.layout.agregar_tarjeta, container, false);

        backButton = (Button) root.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).changeFragment(new MiCuentaFragment());

            }
        });




        //spinner
        final Spinner spin = (Spinner) root.findViewById(R.id.input_card_bank);
        /*spin.setOnItemSelectedListener(this);*/
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, tarjetas);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          /*      String s  = parent.getItemAtPosition(position).toString();
                try {
                    addTarjeta(s);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                /*Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(adapter);


        //agregar tarjeta listner
        addCard = (Button) root.findViewById(R.id.accountAddCard);
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s  = spin.getSelectedItem().toString();
                try {
                    addTarjeta(s);
                    Toast.makeText(getContext(), "Se agrego con exito la tarjeta " + s, Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return root;
    }
    private void addTarjeta(  String nombreTarjeta) throws IOException, JSONException {
        DatabaseReference newRef = getTarjetasCondition.push();

        newRef.setValue(nombreTarjeta);
     /*   sendNewPurchaseNotification();*/

    }
}
