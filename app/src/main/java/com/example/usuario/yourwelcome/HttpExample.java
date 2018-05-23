package com.example.usuario.yourwelcome;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class HttpExample extends Fragment implements View.OnClickListener{

    View rootView;
    Button btnTestFeed,btnInsertar,btnVolver,btnConsultarPorId;
    ProbarFeed test;
    RegistrarEstudiante insert;
    EditText resultadoFeed,Ednombre,Eddireccion,edEscuchador;
    String URLConnect="http://ep00.epimg.net/rss/elpais/portada_america.xml";

    String URL_BASE="http://www.programa2.net/univalle/";
    String insertarAlumno=URL_BASE+"insertar_alumno.php";
    String consultarAlumnoPorId=URL_BASE+"obtener_alumno_por_id.php";

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("real_time_database");

    String fecha;
    SimpleDateFormat dateFormat;

    public HttpExample() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_http_example, container, false);

        btnTestFeed = (Button) rootView.findViewById(R.id.btnTestFeed);
        btnInsertar = (Button) rootView.findViewById(R.id.btnRegistrarEstudiante);
        btnVolver = (Button) rootView.findViewById(R.id.bntCerrarHttp);
        btnConsultarPorId = (Button) rootView.findViewById(R.id.btnConsultarEstudiantePorId);

        resultadoFeed = (EditText) rootView.findViewById(R.id.edResultadoFeed);
        Ednombre = (EditText) rootView.findViewById(R.id.edName);
        Eddireccion = (EditText) rootView.findViewById(R.id.edAdress);
        edEscuchador =(EditText) rootView.findViewById(R.id.edEscuchador);

        btnTestFeed.setOnClickListener(this);
        btnVolver.setOnClickListener(this);
        btnInsertar.setOnClickListener(this);
        btnConsultarPorId.setOnClickListener(this);
        return  rootView;
    }

    public void ejecutarPeticionFeed(){
        test = new ProbarFeed();
        test.execute();
    }
    /*
    integer opcion
    1:insertar
    2:consultar por id
    3:eliminar estudiante
    4:consultar todos
     */
    public void ejecutarPeticionEstudiante(Integer opcion){
        //Toast.makeText(getActivity(),"test",Toast.LENGTH_SHORT).show();
        insert = new RegistrarEstudiante();
        insert.execute(opcion);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTestFeed:
                ejecutarPeticionFeed();
                break;
            case R.id.btnRegistrarEstudiante:
                ejecutarPeticionEstudiante(1);
                break;
            case R.id.bntCerrarHttp:
                break;
            case R.id.btnConsultarEstudiantePorId:
                ejecutarPeticionEstudiante(2);
                break;
            default: break;
        }
    }

    public void EscribirFirebaseFeed(String value){
        fecha = dateFormat.format(new Date());
        myRef.child("feeds").child(fecha).setValue(value);
    }
    public void EscribirFirebaseEstudiante(String value){
        fecha = dateFormat.format(new Date());
        myRef.child("estudiante").child(fecha).setValue(value);
        myRef.child("estudiante").child(fecha).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edEscuchador.setText(""+dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class ProbarFeed extends AsyncTask<Void,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            resultadoFeed.setText("");
        }

        @Override
        protected String doInBackground(Void... voids) {
            int i=0,j=0;
            String salida ="";
            String link ="";
            try{
                URL urlConexion = new URL(URLConnect);
                //primer paso segun https://developer.android.com/reference/java/net/HttpURLConnection.html
                HttpURLConnection conexion = (HttpURLConnection) urlConexion.openConnection();
                //segundo paso -headers
                conexion.setRequestProperty("User-Agent","Mozilla/5.0" +
                        "(Linux; Android 1.5; es-Es) Ejemplo Uceva Http");
                //segundo paso -headers
                int conectado= conexion.getResponseCode();
                if(conectado==HttpURLConnection.HTTP_OK){//NOS CONECTAMOS
                    BufferedReader xml = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                    String linea = xml.readLine(); // leemos la primera linea del xml que contiene datos de la fuente
                    int k=0;
                    while (linea!=null || linea!=""){
                        if(!isCancelled()){ //SI NO HA SIDO DETENIDA LA TASK pues entonces siga con la iteración
                            //procesar el contenido de xml
                            if(linea.indexOf("<title><![CDATA[")>=0){// si es >= 0 es porque en la posiciçon 0 o superior inicia la cadena comparada
                                i = linea.indexOf("<title><![CDATA[")+16;// inicio de cadena a capturar
                                j = linea.indexOf("</title>")-3;// fin  de cadena a capturar

                                salida  = linea.substring(i,j);
                                salida += "\n";
                                EscribirFirebaseFeed(salida);
                                publishProgress(salida);
                                Thread.sleep(800);//2 segundo entre cada iteración
                            }
                            linea = xml.readLine(); //la siguiente liena
                            //procesar el contenido de xml
                        }else{ //close -> else isCancelled
                            break;
                        }
                        k++;
                    } //close while
                    xml.close();
                }else{
                    salida ="401 Unauthorized";
                }
                conexion.disconnect();
                return  salida;
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            return "";
        }
        @Override
        protected void onProgressUpdate(String... s) {
            super.onProgressUpdate(s);
            resultadoFeed.append("\n"+s[0].toString());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            resultadoFeed.append("\nterminó");
        }
    }

    //http://www.evanjbrunner.info/posts/json-requests-with-httpurlconnection-in-android/
    //https://stackoverflow.com/questions/13911993/sending-a-json-http-post-request-from-android
    public class RegistrarEstudiante extends AsyncTask<Integer,String,Void>{

        String nombre="";
        String direccion="";
        Integer opcion=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nombre = Ednombre.getText().toString().trim();
            direccion = Eddireccion.getText().toString().trim();
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            try{
                StringBuilder sb = new StringBuilder();
                opcion=integers[0];
                URL urlConexion;;

                if(opcion==1){
                    urlConexion= new URL(insertarAlumno);
                    //primer paso segun https://developer.android.com/reference/java/net/HttpURLConnection.html
                    HttpURLConnection urlConnection = (HttpURLConnection) urlConexion.openConnection();
                    //segundo paso -headers
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setUseCaches(false);
                    urlConnection.setConnectTimeout(10000);
                    urlConnection.setReadTimeout(10000);

                    urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                    //open
                    urlConnection.connect();

                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream ());
                    //wr.writeBytes(otherParametersUrServiceNeed);

                    //segundo paso -headers
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("nombre", nombre);
                    jsonParam.put("direccion", direccion);

                    wr.writeBytes(jsonParam.toString());
                    wr.flush();
                    wr.close();

                    int conectado= urlConnection.getResponseCode();
                    if(conectado==HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream(),"utf-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            //resultado = resultado +  resultadoFeed.toString();
                            sb.append(line + "\n");
                        }
                        br.close();
                        publishProgress(sb.toString());
                        System.out.println(""+sb.toString());
                        EscribirFirebaseEstudiante(sb.toString());
                    }else{
                        System.out.println(urlConnection.getResponseMessage());
                    }
                    urlConnection.disconnect();
                }else if(opcion==2){
                    urlConexion= new URL(consultarAlumnoPorId+"?idalumno=42");
                    HttpURLConnection urlConnection = (HttpURLConnection) urlConexion.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setUseCaches(false);
                    urlConnection.setConnectTimeout(10000);
                    urlConnection.setReadTimeout(10000);
                    //open
                    urlConnection.connect();
                    int conectado= urlConnection.getResponseCode();
                    if(conectado==HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream(),"utf-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            //resultado = resultado +  resultadoFeed.toString();
                            sb.append(line + "\n");
                        }
                        br.close();
                        publishProgress(sb.toString());
                        System.out.println("opcion2:_"+sb.toString()+" "+consultarAlumnoPorId+"?idalumno=42");
                    }else{
                        System.out.println(urlConnection.getResponseMessage());
                    }
                    urlConnection.disconnect();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.fillInStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            resultadoFeed.setText(values[0]);
        }
    }

}
