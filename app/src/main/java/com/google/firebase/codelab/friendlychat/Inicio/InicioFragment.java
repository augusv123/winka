package com.google.firebase.codelab.friendlychat.Inicio;


import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment {

    private InicioViewModel inicioViewModel;
    private TextView name;
    private TextView balanceText;
    private TextView profit;
    private TextView mail;
    private ImageButton buttonL;
    private ImageButton buttonRetirarDinero;
    private int balance;
    private FirebaseAuth mFirebaseAuth  = FirebaseAuth.getInstance();
    private FirebaseUser mFirebaseUser= mFirebaseAuth.getCurrentUser();
    DatabaseReference mRootRef ;
    DatabaseReference mConditionBallance ;
    DatabaseReference getmConditionMovements;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mlayaoutManager;
    private List<Movement> movements;
    private EditText amountInput;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mRootRef = FirebaseDatabase.getInstance().getReference();
        try{
            mConditionBallance = mRootRef.child(mFirebaseAuth.getUid()).child("Ballance");

        }
        catch (NullPointerException e){
            System.out.println("excepcion");
        }
        try{
            getmConditionMovements = mRootRef.child(mFirebaseAuth.getUid()).child("movements");

        }
        catch (NullPointerException e){
            System.out.println("excepcion 2");
        }
        inicioViewModel =
                ViewModelProviders.of(this).get(InicioViewModel.class);
        View root = inflater.inflate(R.layout.inicio, container, false);

        name = (TextView) root.findViewById(R.id.userName);
        balanceText = (TextView) root.findViewById(R.id.accountBalance);
        profit = (TextView) root.findViewById(R.id.accountProfits);
        mail = (TextView) root.findViewById(R.id.mail);
        amountInput = (EditText) root.findViewById(R.id.amountInput2);

        //boton de ingresar dinero
        buttonL = (ImageButton) root.findViewById(R.id.floating_action_button1);
        buttonL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ingresarDinero();
            }
        });

        //boton de retirar dinero

        buttonRetirarDinero = (ImageButton) root.findViewById(R.id.floating_action_button2);
        buttonRetirarDinero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                retirarDinero();
            }
        });

        //mostrar la info del usuario loggeado con google
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            name.setText(personName);
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            mail.setText(personEmail);
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

        }

        //el try actualiza  el dinero en cuenta
        try {
            getAccountBalance();
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        balanceText.setText("Dinero en cuenta: $" + balance);

        //logica para mostrar la lista de movimientos
        try {

            movements = getMovements();
            mRecyclerView = (RecyclerView) root.findViewById(R.id.movementsRecyclerView);
            mlayaoutManager = new LinearLayoutManager(getContext());

            mAdapter = new MovimientosAdapter(movements,R.layout.recycler_view_item);
            mRecyclerView.setLayoutManager(mlayaoutManager);
            mRecyclerView.setAdapter(mAdapter);
            /*subscribeMethod();*/
        }
        catch (NullPointerException e){
            System.out.println(e);
        }





        return root;
    }
    private List<String> getAllMovements(){
        return new ArrayList<String>(){{
            add("ingreso de dinero");
            add("Retiro de dinero");
            add("cambio de dinero");
            add("ingreso de dinero2");
        }};
    }
    private void getAccountBalance(){

        mConditionBallance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    int value = dataSnapshot.getValue(Integer.class);
                    balance = value;
                    balanceText.setText("Ballance: " + balance);
                }
                else
                    mConditionBallance.setValue(0);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        }
     private void addAccountBalance(final int amount) throws IOException, JSONException {
         mConditionBallance.setValue(balance + amount);
         addMovementIngreso(amount);
     }
    private void withrawMoney(final int amount){
        float result = balance - amount;
        if(result < 0){
            Toast.makeText(getContext(),"Saldo insuficiente",Toast.LENGTH_LONG).show();
        }
        else{
            mConditionBallance.setValue(balance - amount);
            addMovementRetiro(amount);
            Toast.makeText(getContext(), "Dinero Retirado!", Toast.LENGTH_SHORT).show();

        }
    }

    private void addMovementIngreso(  int amount) throws IOException, JSONException {

        DatabaseReference newRef = getmConditionMovements.child("ingresos").push();
        newRef.setValue(amount);
        sendNewPurchaseNotification();

    }
    private void addMovementRetiro(  int amount){

        DatabaseReference newRef = getmConditionMovements.child("extracciones").push();
        newRef.setValue(amount);

    }

    private void ingresarDinero(){

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.ingresar_dinero_dialog, null);
        amountInput = (EditText) dialogView.findViewById(R.id.amountInput2);

        /* inflater.inflate(R.layout.ingresar_dinero_dialog, null)*/
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Ingresar dinero")
                .setMessage("seguro que quiere ingresar dinero?")
                .setView(dialogView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "Dinero Ingresado!", Toast.LENGTH_SHORT).show();
                        int amount = Integer.parseInt(amountInput.getText().toString());
                        /*          String amount = amountInput.getText().toString();*/

                        try {
                            addAccountBalance(amount);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*InicioFragment.this.getDialog().cancel();*/
                    }
                })

                .show();
    }
    private void retirarDinero(){

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.ingresar_dinero_dialog, null);
        amountInput = (EditText) dialogView.findViewById(R.id.amountInput2);

        /* inflater.inflate(R.layout.ingresar_dinero_dialog, null)*/
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Retirar dinero")
                .setMessage("Ingrese el dinero que desea retirar")
                .setView(dialogView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int amount = Integer.parseInt(amountInput.getText().toString());
                        /*          String amount = amountInput.getText().toString();*/

                        withrawMoney(amount);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*InicioFragment.this.getDialog().cancel();*/
                    }
                })

                .show();
    }
    private void sendNewPurchaseNotification() throws IOException, JSONException {
        String authKey = "AAAA4tXDhsU:APA91bG-tO7aQl4xaoDTF88cz9d-TiQ_26Q9i7OI9pUbn2k5mrKwiTe_BfOkrknMWYwnk2yxDYMEryIaRUdUfuIt1zf5eIZOglXLTBOlquIDX3SRg63jljV34ivG7vL0e7wQxN5HRgjC";   // You FCM AUTH key
        String FMCurl = "https://fcm.googleapis.com/fcm/send";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL url = new URL(FMCurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization","key="+authKey);
        conn.setRequestProperty("Content-Type","application/json");


        JSONObject json = new JSONObject();
        json.put("to","/topics/ICBC");
        JSONObject data = new JSONObject();
        data.put("message","Ahora todo cambio le toca a ella");
        data.put("title","titulo");

        json.put("data", data);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(json.toString());
        wr.flush();
        conn.getInputStream();

    }



    private List<Movement> getMovements(){
        final List<Movement> movementsList = new ArrayList<>();
         getmConditionMovements.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot movements : dataSnapshot.child("extracciones").getChildren()) {
                        String key =   "Extraccion de dinero:  ";
                        String move = movements.getValue().toString();
                        Movement m = new Movement(key,move);
                        movementsList.add(m);

                 }
                 for (DataSnapshot movements : dataSnapshot.child("ingresos").getChildren()) {
                     String key = "Ingreso de Dinero:  ";
                     String move = movements.getValue().toString();
                     Movement m = new Movement(key,move);
                     movementsList.add(m);
                 }
                /* MovimientosAdapter adapter = new MovimientosAdapter(movementsList,R.layout.recycler_view_item);
                 mRecyclerView.setAdapter(adapter);*/
                mAdapter.notifyDataSetChanged();

             }
             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
             }
         });
         return movementsList;

     }
     private void subscribeMethod(){
         FirebaseMessaging.getInstance().subscribeToTopic("American")
                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                     private static final String TAG = "";

                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         String msg = "suscripto";
                         if (!task.isSuccessful()) {
                             msg = "suscripto";
                         }
                         Log.d(TAG, msg);
                         Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                     }
                 });
     }
    private void enviarNotificacion(){
        // The topic name can be optionally prefixed with "/topics/".
        String topic = "highScores";

// See documentation on defining a message payload.
      /*   RemoteMessage message = Message.builder(getContext(),"asd")
                 .putData("score", "850")
                 .putData("time", "2:45")
                 .setTopic(topic)
                 .build();*/
// Send a message to the devices subscribed to the provided topic.
       /* String response = FirebaseMessaging.getInstance().send(message);*/
// Response is a message ID string.
     /*   System.out.println("Successfully sent message: " + response);*/
    }
    private void aceptarCompra(String message,String title){

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.aceptar_compra_dialog, null);
        TextView purchaseTitle = (TextView) dialogView.findViewById(R.id.purchaseTitle);
        purchaseTitle.setText(title);
        TextView purchaseMessage = (TextView) dialogView.findViewById(R.id.purchaseMessage);
        purchaseMessage.setText(message);
        /* inflater.inflate(R.layout.ingresar_dinero_dialog, null)*/
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Ingresar dinero")
                .setMessage("seguro que quiere ingresar dinero?")
                .setView(dialogView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "Compra Aceptada!", Toast.LENGTH_SHORT).show();
/*                        int amount = Integer.parseInt(amountInput.getText().toString());*/
                        /*          String amount = amountInput.getText().toString();*/

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*InicioFragment.this.getDialog().cancel();*/
                    }
                })

                .show();
    }
    }