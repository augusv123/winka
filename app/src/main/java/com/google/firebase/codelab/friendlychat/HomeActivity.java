package com.google.firebase.codelab.friendlychat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.codelab.friendlychat.Calendario.CalendarioFragment;
import com.google.firebase.codelab.friendlychat.Chats.ChatsFragment;
import com.google.firebase.codelab.friendlychat.Inicio.InicioFragment;
import com.google.firebase.codelab.friendlychat.Mas.MasFragment;
import com.google.firebase.codelab.friendlychat.MiCuenta.MiCuentaFragment;
import com.google.firebase.codelab.friendlychat.NuevoPedido.NuevoPedidoFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.google.firebase.codelab.friendlychat.MainActivity.ANONYMOUS;

public class HomeActivity extends AppCompatActivity {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    BottomNavigationView bottomNavigation;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private String mUsername;
    private GoogleSignInClient mSignInClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        InicioFragment inicioFragment = new InicioFragment();
        changeFragment(inicioFragment);
        //singout
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(this, gso);

        //me subscribo al tema que quiero
        FirebaseMessaging.getInstance()
                .subscribeToTopic("santander");


        bottomNavigation =  (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page_1:
                        InicioFragment inicioFragment = new InicioFragment();
                        item.setChecked(true);
                        changeFragment(inicioFragment);
                        break;
                    case R.id.page_2:
                        MiCuentaFragment fragment = new MiCuentaFragment();
                        item.setChecked(true);
                        changeFragment(fragment);
                        break;
                    case R.id.page_3:
                        ChatsFragment chatsFragment = new ChatsFragment();
                        item.setChecked(true);
                        changeFragment(chatsFragment);
                        break;
                    case R.id.page_4:
                        NuevoPedidoFragment nuevoPedidoFragment = new NuevoPedidoFragment();
                        item.setChecked(true);
                        changeFragment(nuevoPedidoFragment);
                        break;
                    case R.id.page_5:
                        MasFragment masFragment = new MasFragment();
                        item.setChecked(true);
                        changeFragment(masFragment);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

       /*     //mostrar la info del usuario loggeado con google
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

        }*/

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                mSignInClient.signOut();

                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void changeFragment(Fragment fr){
        FrameLayout fl = (FrameLayout) findViewById(R.id.mainFragment);
        fl.removeAllViews();
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.add(R.id.mainFragment, fr);
        transaction1.commit();
    }

}
