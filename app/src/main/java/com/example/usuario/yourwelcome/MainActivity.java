package com.example.usuario.yourwelcome;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this,"Hola oncreae",Toast.LENGTH_LONG).show();
    }
    public void irHome(View g){
        Intent ir = new Intent(MainActivity.this,Home.class);
        ir.addFlags(ir.FLAG_ACTIVITY_CLEAR_TOP | ir.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(ir);
    }
    public void irHomeApp(View g){
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
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this,"Hola pause",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Hola destroy",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"Hola resume",Toast.LENGTH_LONG).show();
    }
}
