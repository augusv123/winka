package com.google.firebase.codelab.friendlychat.Chats;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.codelab.friendlychat.Chats.UsersChatsViewModel;
import com.google.firebase.codelab.friendlychat.FriendlyMessage;
import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersChatsFragment extends Fragment {
    private UsersChatsViewModel usersChatsViewModel;

    //recycler view
    private RecyclerView usersRecyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private FirebaseAuth mFirebaseAuth  = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser= mFirebaseAuth.getCurrentUser();
    DatabaseReference mRootRef ;
    DatabaseReference mconditionChats ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        usersChatsViewModel =
                ViewModelProviders.of(this).get(UsersChatsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_users, container, false);

        usersRecyclerView = root.findViewById(R.id.users_recycler_view);
        usersRecyclerView.setHasFixedSize(true);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
     /*   userAdapter = new UserAdapter(getContext(),mUsers);
        usersRecyclerView.setAdapter(userAdapter);*/
        readUsers();

        //agarrar extra si es que apreto la notificacion de compra

        String title;
        String message;
        Bundle s = this.getArguments();
        if(s != null){
            Toast.makeText(getContext(), "entro", Toast.LENGTH_SHORT).show();
            title = s.getString("title");
            message = s.getString("message");
            String url = s.getString("url");
            String monto = s.getString("monto");
            String producto = s.getString("producto");
            String descuento = s.getString("descuento");
            aceptarCompra(message,title,url,monto,producto,descuento);
        }


        return root;
    }
    private void aceptarCompra(final String message, final String title, final String url, final String monto, final String producto, final String descuento){

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.aceptar_compra_dialog, null);
        TextView purchaseTitle = (TextView) dialogView.findViewById(R.id.purchaseTitle);
        purchaseTitle.setText(title);
        TextView purchaseMessage = (TextView) dialogView.findViewById(R.id.purchaseMessage);
        purchaseMessage.setText(message);
        /* inflater.inflate(R.layout.ingresar_dinero_dialog, null)*/
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Nueva pedido de compra")
                .setMessage("Quiere aceptar la compra?")
                .setView(dialogView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "Compra Aceptada!", Toast.LENGTH_SHORT).show();
                        /*                        int amount = Integer.parseInt(amountInput.getText().toString());*/
                        /*          String amount = amountInput.getText().toString();*/
                        FriendlyMessage u = new FriendlyMessage(title,message);
                        u.setPhotoUrl("default");
                        mconditionChats.child(mFirebaseUser.getUid()).setValue(u);

                        String message = "Pedido de compra de " + producto + "por el monto de:" + monto + ". Puede visualizar y comprar el producto en al URL: " + url;
                        FriendlyMessage fm = new FriendlyMessage();
                        fm.setText(message);
                        fm.setImageUrl("default");

                        mconditionChats.child(mFirebaseUser.getUid()).child("messages").push().setValue(fm);


                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*InicioFragment.this.getDialog().cancel();*/
                    }
                })

                .show();
    }

    private void readUsers() {
/*        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();*/
        mRootRef = FirebaseDatabase.getInstance().getReference();
         mconditionChats = mRootRef.child("chats");
        mconditionChats.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    mUsers.clear();
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                        String[] split = snapshot.getKey().split("-");
                        for (String s:split
                        ) {
                            if(s.equals(mFirebaseUser.getUid())){
                                FriendlyMessage fm = snapshot.getValue(FriendlyMessage.class);

                                User user = new User(fm.getName(),fm.getPhotoUrl());
                                user.setId(snapshot.getKey());
                                mUsers.add(user);
                                /*mUsers.add(user);*/
                     /*               assert user != null;
                    assert mFirebaseAuth != null;*/
             /*       if(!user.getId().equals(mFirebaseAuth.getUid())){
                        mUsers.add(user);
                    }*/
                            }
                        }
                        User user = snapshot.getValue(User.class);



                    }

                    userAdapter = new UserAdapter(getContext(),mUsers);
                    usersRecyclerView.setAdapter(userAdapter);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
