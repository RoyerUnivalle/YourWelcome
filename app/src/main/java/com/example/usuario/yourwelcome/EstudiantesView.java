package com.example.usuario.yourwelcome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EstudiantesView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EstudiantesView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstudiantesView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<String> mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    View viewRoot;
    ListView listaEstudiantes;
    ArrayAdapter<String> estudiantesAdapter;

    public EstudiantesView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param estudiantes Parameter 1.
     * @return A new instance of fragment EstudiantesView.
     */
    // TODO: Rename and change types and number of parameters
    public static EstudiantesView newInstance(ArrayList<String> estudiantes ) {
        EstudiantesView fragment = new EstudiantesView();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, estudiantes);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArrayList(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot =inflater.inflate(R.layout.fragment_estudiantes_view, container, false);
        listaEstudiantes = (ListView)  viewRoot.findViewById(R.id.estudiantesList);
        Toast.makeText(getActivity(),"hola "+mParam1.size(),Toast.LENGTH_SHORT).show();
        estudiantesAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mParam1);
        listaEstudiantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        listaEstudiantes.setAdapter(estudiantesAdapter);
        return  viewRoot;
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
}
