package com.example.usuario.yourwelcome;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usuario.yourwelcome.Connection.Connection;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Network.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Network#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Network extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Connection conexion;
    SQLiteDatabase db;

    Button btnInsertar,btnConsultar;
    EditText edtName;
    View rootView;
    EstudiantesView estudiantesObj;

    private OnFragmentInteractionListener mListener;

    public Network() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Network.
     */
    // TODO: Rename and change types and number of parameters
    public static Network newInstance(String param1, String param2) {
        Network fragment = new Network();
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
        //hasOptionsMenu();
        setHasOptionsMenu(true);
        conexion = new Connection(getContext(),"UnivalleDb",null,1);
        db = conexion.getWritableDatabase();
        if(conexion!=null){
            Toast.makeText(getContext(),"Bd Creada",Toast.LENGTH_SHORT).show();
            //String query="insert into estudiante (name) values ('royer');";
            //db.execSQL(query);
        }
    }

    public void insertDatabase(String name){
        String query="insert into estudiante (name) values ('"+name+"');";
        db.execSQL(query);
    }

    public void consultarEstudiante(){
        Cursor c = db.rawQuery("SELECT * FROM estudiante", null);
        ArrayList<String> estudiantes= new ArrayList<String>();
        if(c.moveToFirst()){
            do {
                estudiantes.add(c.getString(1));
            }while (c.moveToNext());
        }
        c.close();

        estudiantesObj = new EstudiantesView().newInstance(estudiantes);
        android.support.v4.app.FragmentTransaction transaccion = getActivity().getSupportFragmentManager().beginTransaction();
        transaccion.replace(R.id.fragmentA,estudiantesObj);
        transaccion.addToBackStack(null);
        transaccion.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_network, container, false);

        btnInsertar = (Button) rootView.findViewById(R.id.btnRegistrar);
        btnConsultar = (Button) rootView.findViewById(R.id.btnConsultar);
        edtName = (EditText) rootView.findViewById(R.id.edName);
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=edtName.getText().toString().trim();
                insertDatabase(name);
            }
        });
        btnConsultar.setOnClickListener(this);
        return rootView;
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
            case R.id.btnConsultar:
                consultarEstudiante();
                break;
            default:
                break;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_network,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        boolean showMessage = false;
        String message="";
        // handle item selection
        switch (item.getItemId()) {
            case R.id.networkF_option:
                message="Hola fragment network";
                showMessage=true;
                //return true;
        }
        if(showMessage) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setMessage(message);
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);

    }
}
