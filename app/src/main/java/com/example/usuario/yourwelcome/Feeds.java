package com.example.usuario.yourwelcome;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Feeds.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Feeds#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Feeds extends Fragment implements  View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    String URLConnect="http://ep00.epimg.net/rss/elpais/portada.xml";
    View rootView;
    Button btnFeed;
    LectorFeeds lector;
    TextView resultado;

    public Feeds() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Feeds.
     */
    // TODO: Rename and change types and number of parameters
    public static Feeds newInstance(String param1, String param2) {
        Feeds fragment = new Feeds();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_feeds, container, false);
        btnFeed = (Button) rootView.findViewById(R.id.btnFeed);
        resultado = (TextView) rootView.findViewById(R.id.contenedorFeed);
        btnFeed.setOnClickListener(this);
        return rootView;
    }

    public void iniciarLectura(){
        lector = new LectorFeeds();
        lector.execute();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnFeed:
                iniciarLectura();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class LectorFeeds extends AsyncTask<Void,String,String>{

        @Override
        protected String doInBackground(Void... voids) {
            int i=0,j=0;
            int k=0,l=0;
            String salida ="";
            String salidaBd="";
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
                    while (linea!=null || linea!=""){
                        if(!isCancelled()){ //SI NO HA SIDO DETENIDA LA TASK pues entonces siga con la iteración
                            //procesar el contenido de xml
                            if(linea.indexOf("<title><![CDATA[")>=0){// si es >= 0 es porque en la posiciçon 0 o superior inicia la cadena comparada
                                i = linea.indexOf("<title><![CDATA[")+16;// inicio de cadena a capturar
                                j = linea.indexOf("</title>")-3;// fin  de cadena a capturar
                                /*if(linea.indexOf("<link><![CDATA[")>=0){
                                    k = linea.indexOf("<link><![CDATA[")+15;// inicio de cadena a capturar
                                    l = linea.indexOf("</link>")-3;// fin  de cadena a capturar
                                    link = linea.substring(k,l);
                                }else{
                                    link="#";
                                }*/
                                //salida  = "<a href=\""+link+"\">"+linea.substring(i,j)+"</a>";
                                salida  = linea.substring(i,j);
                                salida += "<br>-----------------<br>";

                                k++;
                                publishProgress(salida);
                                Thread.sleep(2000);//2 segundo entre cada iteración
                            }
                            linea = xml.readLine(); //la siguiente liena
                            //procesar el contenido de xml
                        }else{ //close -> else isCancelled
                            break;
                        }
                    } //close while
                    xml.close();
                }else{
                    salida ="Fuente no encontrada";
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
            resultado.append(s[0].toString());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            resultado.append("termino");
        }
    }
}
