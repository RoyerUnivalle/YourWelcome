package com.example.usuario.yourwelcome;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class HomeApp extends AppCompatActivity implements DataBaseCrud.OnFragmentInteractionListener, Network.OnFragmentInteractionListener, EstudiantesView.OnFragmentInteractionListener {


    DataBaseCrud frag;
    Network networkFrag;
    HttpExample fragHttp;
    GoogleMapExample fr4;


    FragmentManager manager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_app);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Proyecto móviles");

        setSupportActionBar(myToolbar);
        //setActionBar(myToolbar);
        //myToolbar.hideOverflowMenu();

        //EditText fragment = (EditText) getFragmentManager().findFragmentById(R.id.edName);


    }

    public void mostrarDatabaseOption(){
        if(frag instanceof DataBaseCrud){

        }else{
            frag = new DataBaseCrud();
            android.support.v4.app.FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
            transition.add(R.id.fragmentA,frag);
            transition.commit();
        }
    }

    public void showNetworkOptions(){
        networkFrag = new Network();
        android.support.v4.app.FragmentTransaction transactionNet = getSupportFragmentManager().beginTransaction();
        transactionNet.add(R.id.fragmentA,networkFrag);
        transactionNet.commit();
    }

    public  void cerrarDatabaseOptions(View g){
        android.support.v4.app.FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        if(frag.isAdded()){
            transition.hide(frag);
            transition.commit();
            frag=null;
        }
    }

    public  void insertWorkers(View k){
        //fra.
    }

    public  void ejemplosHttp(){
        if(fragHttp instanceof HttpExample){
        }else{
            fragHttp = new HttpExample();
            android.support.v4.app.FragmentTransaction transtion=getSupportFragmentManager().beginTransaction();
            transtion.add(R.id.fragmentA,fragHttp);
            transtion.commit();
        }
    }
    public  void ejemplosMaps(){
        if(fr4 instanceof GoogleMapExample){

        }else{
            fr4 = new GoogleMapExample();
            android.support.v4.app.FragmentTransaction transtion=getSupportFragmentManager().beginTransaction();
            transtion.add(R.id.fragmentA,fr4);
            transtion.commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    ////////MÉTODOS NECESARIOS PARA INFLAR LA VISTA CON UN MENU//////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate activity menu items.
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back_gorund:
                // User chose the "Settings" item, show the app settings UI...
                //item.getTitle();
                //Toast.makeText(this,"hola "+item.getTitle(),Toast.LENGTH_LONG).show();
                ejemplosHttp();
                return true;

            case R.id.action_data_base:
                //Toast.makeText(this,"hola "+item.getTitle(),Toast.LENGTH_LONG).show();
                mostrarDatabaseOption();
                return true;

            case R.id.action_service:
                //Toast.makeText(this,"hola "+item.getTitle(),Toast.LENGTH_LONG).show();
                ejemplosMaps();
                return true;

            case R.id.action_network:
                //Toast.makeText(this,"hola "+item.getTitle(),Toast.LENGTH_LONG).show();
                showNetworkOptions();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
    }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
