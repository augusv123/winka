package com.google.firebase.codelab.friendlychat.MiCuenta;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.codelab.friendlychat.Inicio.MovimientosAdapter;
import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;


public class MiCuentaFragment extends Fragment {

    private MiCuentaViewModal miCuentaViewModal;
    private FirebaseAuth mFirebaseAuth  = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser= mFirebaseAuth.getCurrentUser();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionTarjetas = mRootRef.child(mFirebaseAuth.getUid()).child("Tarjetas");

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mlayaoutManager;
    private List<Card> cardList;
 


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        miCuentaViewModal =
                ViewModelProviders.of(this).get(MiCuentaViewModal.class);
        View root = inflater.inflate(R.layout.micuenta, container, false);



        //logica para mostrar la lista de movimientos
        cardList = getCards();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.creditCardsRecyclerView);
        mlayaoutManager = new LinearLayoutManager(getContext());

        mAdapter = new CardsAdapter(cardList, R.layout.credit_recycler_view_item, new CardsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Card card, int position, boolean checked) {

                Toast.makeText(getContext(),"esta checkeado:" + checked, Toast.LENGTH_LONG).show();
            }
        }
        );
        mRecyclerView.setLayoutManager(mlayaoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return root;
    }
    private void subscribeMethod(final String s){
        FirebaseMessaging.getInstance().subscribeToTopic(s)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    private static final String TAG = "";

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "suscripto";
                        if (!task.isSuccessful()) {
                            msg = "Ahora le llegaran notificaciones de la tarjeta: "+ s ;
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private List<Card> getCards(){
        final List<Card> cardlist = new ArrayList<>();
        mConditionTarjetas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot card : dataSnapshot.getChildren()) {
                    String key =   "Extraccion de dinero:  ";
                    String move = card.getValue().toString();
                    Card c = new Card(key,move);
                    cardlist.add(c);

                }
                mAdapter.notifyDataSetChanged();
             /*   CardsAdapter adapter = new CardsAdapter(cardlist, R.layout.credit_recycler_view_item, new CardsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Card card, int position) {
                        Toast.makeText(getContext(),"prueba boton" + card.getValue(), Toast.LENGTH_LONG).show();
                    }
                });
                mRecyclerView.setAdapter(adapter);*/

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return cardlist;

    }
}