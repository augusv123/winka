package com.google.firebase.codelab.friendlychat.MiCuenta;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.codelab.friendlychat.AgregarTarjeta.AgregarTarjetaFragment;
import com.google.firebase.codelab.friendlychat.Inicio.InicioFragment;
import com.google.firebase.codelab.friendlychat.Inicio.MovimientosAdapter;
import com.google.firebase.codelab.friendlychat.NuevoPedido.NuevoPedidoFragment;
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
    private Button accountAddCard2;
    private Button newOrder;
    private FrameLayout fl ;
 


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        miCuentaViewModal =
                ViewModelProviders.of(this).get(MiCuentaViewModal.class);
        View root = inflater.inflate(R.layout.micuenta, container, false);


        fl = (FrameLayout) root.findViewById(R.id.mainFragment);
        //instancio el boton para agregar tarjetas y le agrego su listener
        accountAddCard2 = (Button) root.findViewById(R.id.accountAddCard2);
        accountAddCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                AgregarTarjetaFragment fragment2 = new AgregarTarjetaFragment();
                fragmentTransaction2.addToBackStack("xyz");
                fragmentTransaction2.hide(MiCuentaFragment.this);
                fragmentTransaction2.add(android.R.id.content, fragment2);
                fragmentTransaction2.commit();
                //

            }
        });
        newOrder = (Button) root.findViewById(R.id.newOrder);
        newOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                NuevoPedidoFragment fragment2 = new NuevoPedidoFragment();
                fragmentTransaction2.addToBackStack("xyz");
                fragmentTransaction2.hide(MiCuentaFragment.this);
                fragmentTransaction2.add(android.R.id.content, fragment2);
                fragmentTransaction2.commit();
                //

            }
        });



        //logica para mostrar la lista de movimientos
        cardList = getCards();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.creditCardsRecyclerView);
        mlayaoutManager = new LinearLayoutManager(getContext());

        mAdapter = new CardsAdapter(cardList, R.layout.credit_recycler_view_item, new CardsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Card card, int position, boolean checked) {
                String s =card.getValue();

               if(checked){
                   subscribeMethod(s);
               }
               else{
                   unSubscribeMethod(s);
               }
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
                        String msg = "Ahora le llegaran notificaciones de la tarjeta: "+ s;
                        if (!task.isSuccessful()) {
                            msg = "Oops! Hubo un problema" ;
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void unSubscribeMethod(final String s){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(s)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    private static final String TAG = "";

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "No le llegaran mas notificaciones de la tarjeta" + s;
                        if (!task.isSuccessful()) {
                            msg = "Oops! Hubo un problema" ;
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
/*    private void changeFragment(Fragment fr){

        fl.removeAllViews();
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.add(R.id.mainFragment, fr);
        transaction1.commit();
    }*/
}