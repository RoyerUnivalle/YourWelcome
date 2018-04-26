package com.example.usuario.yourwelcome;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity implements View.OnClickListener, Network.OnFragmentInteractionListener,EstudiantesView.OnFragmentInteractionListener{

    Button bnt1,btn2,btn3,btnVolver,btnPintar;
    int contador;

    Pintar obj;
    boolean flag=false;

    Network fragN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //enlace
        bnt1 = (Button) findViewById(R.id.bntInterface);
        btn2 = (Button) findViewById(R.id.btnListen);
        btn3 = (Button) findViewById(R.id.bntInterface2);

        btnVolver = (Button) findViewById(R.id.btnVolver);

        btnPintar = (Button) findViewById(R.id.btnPintar);

        //btnVolver.setBackgroundColor(Color.rgb(255,0,0));

        bnt1.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btnVolver.setOnClickListener(this);
        btnPintar.setOnClickListener(this);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplication(),"Hola Listen",Toast.LENGTH_LONG).show();
            }
        });

        Bundle recibo = getIntent().getExtras();

        if(recibo!=null){
            contador = recibo.getInt("contador");
        }
        if(savedInstanceState!=null){
            contador=savedInstanceState.getInt("contador");
        }
    }

    public void showFragment(View h){
        fragN = new Network();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentA,fragN);
        transaction.commit();
    }

    //VOLVER - INLINE
    public void irMainApp(){
        Intent ir = new Intent(Home.this,MainActivity.class);
        ir.addFlags(ir.FLAG_ACTIVITY_CLEAR_TOP | ir.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(ir);
    }
    public void irPulso(View g){
        Intent ir = new Intent(Home.this,Pulso.class);
        ir.addFlags(ir.FLAG_ACTIVITY_CLEAR_TOP | ir.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle entrega = new Bundle();
        entrega.putInt("contador",contador);
        ir.putExtras(entrega);
        startActivity(ir);
    }
            /*
            * param: none
            * propose: Call TestService Activity
             */
    public void irTestService(){
        Intent ir = new Intent(Home.this,TestService.class);
        ir.addFlags(ir.FLAG_ACTIVITY_CLEAR_TOP | ir.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(ir);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bntInterface:
                Toast.makeText(this,"Hola interface",Toast.LENGTH_LONG).show();
                break;
            case R.id.bntInterface2:
                irTestService();
                break;
            case R.id.btnVolver:
                irMainApp();
                break;
            case R.id.btnPintar:
                Toast.makeText(this, "-->>"+flag, Toast.LENGTH_LONG).show();
                if(!flag){
                    obj=new Pintar();//inicializamos
                    obj.execute();//ejecutamos
                }else{
                    obj.cancel(true);
                }
                break;

            default:
                Toast.makeText(this,"Hola",Toast.LENGTH_LONG).show();
        }
    }

    public int GenerarAleatorio(){
        return  (int) (Math.random()*255);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(flag){
            obj.cancel(true);
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class Pintar extends AsyncTask<Void,Void,Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            flag=true;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            while (flag){
                publishProgress();
                if (isCancelled()){
                    break;
                }
            }
            return null;
        }
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate();
            btnVolver.setBackgroundColor(Color.rgb(GenerarAleatorio(),GenerarAleatorio(),GenerarAleatorio()));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            flag=false;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            flag=false;

        }
    }
}
