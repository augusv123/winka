package com.google.firebase.codelab.friendlychat.NuevoPedido;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.codelab.friendlychat.HomeActivity;
import com.google.firebase.codelab.friendlychat.MiCuenta.MiCuentaFragment;
import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class NuevoPedidoFragment extends Fragment {
    private static final String TAG = "";
    private NuevoPedidoViewModel nuevoPedidoViewModel;
    private Button crearPedidoButton;
    private EditText inputTarjeta;
    private TextView inputUrl;
    private String valorTarjeta;
    private  String[] tarjetas = { "BBVA", "Galicia", "ICBC", "Santander Rio"};
    private  String[] categorias = { "Seleccione una categoria","ROPA Y ACCESORIOS", "Electronica"};
    private  String[] ropa = { "Calzado", "Saquitos,sweaters y chalecos"};
    private  String[] genero = { "Hombre", "Mujer","Niños","Niñas","Bebe"};
    private Button backButton ;
    private EditText inputURL ;
    private EditText inputMonto ;
    private EditText inputDescuento ;
    private EditText inputNombre ;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    DatabaseReference mConditionBallance ;
    DatabaseReference mRootRef ;
    DatabaseReference mconditionPedidos ;
    private int balance;
    private Spinner spinGenero;
    private  Spinner spintipoderopa;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        nuevoPedidoViewModel =
                ViewModelProviders.of(this).get(NuevoPedidoViewModel.class);
        final View root = inflater.inflate(R.layout.nuevo_pedido, container, false);
        inputUrl = (TextView) root.findViewById(R.id.inputURL);
        valorTarjeta="";

        inputURL = (EditText) root.findViewById(R.id.inputURL);
        inputMonto = (EditText) root.findViewById(R.id.inputMonto);
        inputDescuento = (EditText) root.findViewById(R.id.inputDiscout);
        inputNombre = (EditText) root.findViewById(R.id.inputName);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mconditionPedidos = mRootRef.child("pedidos");
        //boton back
        backButton = (Button) root.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                FragmentTransaction fragmentTransaction2 = getParentFragmentManager().beginTransaction();
                MiCuentaFragment fragment2 = new MiCuentaFragment();
               *//* fragmentTransaction2.addToBackStack("xyz");*//*
                fragmentTransaction2.hide(NuevoPedidoFragment.this);
                fragmentTransaction2.add(android.R.id.content, fragment2);
                fragmentTransaction2.commit();*/
                ((HomeActivity)getActivity()).changeFragment(new MiCuentaFragment());

            }
        });
        mConditionBallance = mRootRef.child(mFirebaseUser.getUid()).child("Ballance");

        //el try actualiza  el dinero en cuenta
        try {
            getAccountBalance();
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }



        //spinner
        final Spinner spin = (Spinner) root.findViewById(R.id.input_card_bank2);
        final Spinner spinCategoria = (Spinner) root.findViewById(R.id.inputCategory);
        /*spin.setOnItemSelectedListener(this);*/
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, tarjetas);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s  = parent.getItemAtPosition(position).toString();
                valorTarjeta = s;
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(adapter);
        spinGenero = (Spinner) root.findViewById(R.id.inputGenero);
        ArrayAdapter<String> adapterGenero = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, genero);
        spinGenero.setAdapter(adapterGenero);

        spintipoderopa = (Spinner) root.findViewById(R.id.inputtipoderopa);
        ArrayAdapter<String> adapterTipoderopa = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, ropa);
        spintipoderopa.setAdapter(adapterTipoderopa);


        final ArrayAdapter<String> adapterCategoria = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, categorias);
        spinCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s  = parent.getItemAtPosition(position).toString();
                if(s == "ROPA Y ACCESORIOS"){

                    spinGenero.setVisibility(View.VISIBLE);
                    spintipoderopa.setVisibility(View.VISIBLE);


                }
                else{
                    spinGenero.setVisibility(View.GONE);
                    spintipoderopa.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        spinCategoria.setAdapter(adapterCategoria);


        crearPedidoButton = (Button) root.findViewById(R.id.crearPedidoButton);

        crearPedidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = inputURL.getText().toString();
                    String monto = inputMonto.getText().toString();
                    if(monto == "") monto ="0";
                    int amount = Integer.parseInt(inputMonto.getText().toString());
                    String valorTarjeta  = spin.getSelectedItem().toString();
                    String descuento = inputDescuento.getText().toString();
                    String nombre = inputNombre.getText().toString();
                    if(retenerDinero(amount)){

                        newOrder(valorTarjeta,url,monto,descuento,nombre,amount);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return root;

    }

    private void sendMessage(){


    }
    private boolean retenerDinero(final int amount){
        float result = balance - amount;
        if(result < 0){
            Toast.makeText(getContext(),"Saldo insuficiente",Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            mConditionBallance.setValue(balance - amount);
            return true;

        }
    }
    private void getAccountBalance(){

        mConditionBallance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    int value = dataSnapshot.getValue(Integer.class);
                    balance = value;
                }
                else
                    mConditionBallance.setValue(0);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    private void newOrder(String valorTarjeta, String url, String monto, String descuento, String nombre,int amount) throws IOException, JSONException {
        //get the push key value
        String key = mconditionPedidos.push().getKey();
           mconditionPedidos.child(key).setValue(new Pedido(amount,false,key));
           sendNewPurchaseNotification( valorTarjeta, url,monto,descuento,nombre,key);

    }
    private void sendNewPurchaseNotification(String tarjeta, String url, String monto, String descuento, String producto,String key) throws IOException, JSONException {
        String authKey = "AAAA4tXDhsU:APA91bG-tO7aQl4xaoDTF88cz9d-TiQ_26Q9i7OI9pUbn2k5mrKwiTe_BfOkrknMWYwnk2yxDYMEryIaRUdUfuIt1zf5eIZOglXLTBOlquIDX3SRg63jljV34ivG7vL0e7wQxN5HRgjC";   // You FCM AUTH key
        String FMCurl = "https://fcm.googleapis.com/fcm/send";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL urlP = new URL(FMCurl);
        HttpURLConnection conn = (HttpURLConnection) urlP.openConnection();
        System.out.println(tarjeta);
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization","key="+authKey);
        conn.setRequestProperty("Content-Type","application/json");


        JSONObject json = new JSONObject();
        json.put("to","/topics/"+tarjeta);

    /*    String condition = tarjeta+ "in topics || 'industry-tech' in topics";
        json.put("to",condition);*/

        JSONObject data = new JSONObject();
        data.put("message","Un usuario esta solicitando una compra con su tarjeta"+tarjeta+ ", por el monto de : "+ monto +"En el producto: " + producto);
        data.put("title","Nuevo Pedido de compra!!");
        data.put("url",url);
        data.put("monto",monto);
        data.put("descuento",descuento);
        data.put("producto",producto);
        data.put("key",key);
        data.put("uid",mFirebaseUser.getUid());


        json.put("data", data);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(json.toString());
        wr.flush();
        conn.getInputStream();

    }



}
