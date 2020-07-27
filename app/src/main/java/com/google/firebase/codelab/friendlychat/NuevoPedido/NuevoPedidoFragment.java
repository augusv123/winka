package com.google.firebase.codelab.friendlychat.NuevoPedido;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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

import com.google.firebase.codelab.friendlychat.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        nuevoPedidoViewModel =
                ViewModelProviders.of(this).get(NuevoPedidoViewModel.class);
        View root = inflater.inflate(R.layout.nuevo_pedido, container, false);
        inputUrl = (TextView) root.findViewById(R.id.inputURL);
        valorTarjeta="";
        crearPedidoButton = (Button) root.findViewById(R.id.crearPedidoButton);

        crearPedidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    newOrder(valorTarjeta);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        //spinner
        Spinner spin = (Spinner) root.findViewById(R.id.input_card_bank2);
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
        return root;

    }

    private void sendMessage(){


    }
    private void newOrder(  String nombreTarjeta) throws IOException, JSONException {

           sendNewPurchaseNotification( nombreTarjeta);

    }
    private void sendNewPurchaseNotification(String nombreTarjeta ) throws IOException, JSONException {
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
        json.put("to","/topics/"+nombreTarjeta);
        JSONObject data = new JSONObject();
        data.put("message","Un usuario esta solicitando una compra con su tarjeta");
        data.put("title","Nuevo Pedido de compra!!");

        json.put("data", data);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(json.toString());
        wr.flush();
        conn.getInputStream();

    }



}
