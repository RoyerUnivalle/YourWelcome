package com.example.usuario.yourwelcome;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    boolean isMobile,IsWifi=false;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient googleApiClient;

    AccountManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(this,"Hola oncreae",Toast.LENGTH_LONG).show();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        manager = AccountManager.get(this);

        findViewById(R.id.sign_in).setOnClickListener(this);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener(){

        });
    }
    public void irHome(View g){
        Intent ir = new Intent(MainActivity.this,Home.class);
        ir.addFlags(ir.FLAG_ACTIVITY_CLEAR_TOP | ir.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(ir);
    }
    /*
    llama al metodo que crea la intención para ir a la activity que tiene los
    fragments
     */
    public void irHomeAppView(View g){
        irHomeApp();
    }
    public void irHomeApp(){
        String login="cvb";
        String passwd="cvcb";
        if(login.length()==0|| passwd.length()==0){
            final AlertDialog dialogo = new AlertDialog.Builder(this).create();
            dialogo.setTitle("Acceso no permitido");
            dialogo.setMessage("Por favor registrese");
            dialogo.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialogo.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogo.dismiss();
                }
            });
            dialogo.show();
        }else{
            Intent ir = new Intent(MainActivity.this,HomeApp.class);
            ir.addFlags(ir.FLAG_ACTIVITY_CLEAR_TOP | ir.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(ir);
        }
    }
    public  boolean checkConnection(){
        Toast.makeText(this,"hola1",Toast.LENGTH_SHORT).show();
        boolean isConnet=false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork!=null){
            isConnet = activeNetwork.isConnectedOrConnecting();
        }
        if(isConnet && activeNetwork!=null){
            isMobile= activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
            IsWifi = activeNetwork.getType()== ConnectivityManager.TYPE_WIFI;
            return  true;
        }else{
            return  false;
        }
    }
    private  void signIn(){
        if(checkConnection()){
            Toast.makeText(this,"hola2",Toast.LENGTH_SHORT).show();
            if(isMobile){
                Toast.makeText(this,"hola3",Toast.LENGTH_SHORT).show();
                AlertDialog dialogo = new AlertDialog.Builder(this).create();
                dialogo.setTitle("Validar red");
                dialogo.setMessage("Desea consumir datos");
                dialogo.setCancelable(false);
                dialogo.setButton(DialogInterface.BUTTON_POSITIVE,"Aceptar",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                        startActivityForResult(signIntent,RC_SIGN_IN);
                    }
                });
                dialogo.setButton(DialogInterface.BUTTON_NEGATIVE, "La proxima", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogo.show();
            }else{
                Toast.makeText(this,"hola4",Toast.LENGTH_SHORT).show();
                Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signIntent,RC_SIGN_IN);
            }
        }else{
            Toast.makeText(this,"55",Toast.LENGTH_SHORT).show();
            AlertDialog dialogo = new AlertDialog.Builder(this).create();
            dialogo.setTitle("Sin conexion");
            dialogo.setMessage("No puede ingresar sin estar conectado");
            dialogo.setButton(DialogInterface.BUTTON_NEUTRAL, "Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogo.show();
        }
    }
    private void handleSignInResult(GoogleSignInResult result ) {
        if(result.isSuccess()){
            irHomeApp();
        }else{
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (RC_SIGN_IN==requestCode){
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignInResult resul = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(resul);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in:
                signIn();
                break;
        }
    }
    /*
    Metodo que se encarga de recibir un String y cifrarlo en el
    formato MD5
     */
    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(this,"Hola pause",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this,"Hola destroy",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this,"Hola resume",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
