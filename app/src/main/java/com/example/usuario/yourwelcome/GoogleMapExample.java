package com.example.usuario.yourwelcome;


import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;

import android.Manifest;

import java.sql.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleMapExample extends Fragment implements OnMapReadyCallback {


    private static final int LOCATION_REQUEST_CODE = 1;
    private static final int ACCESS_FINE_LOCATION = 1;
    View rootView;
    SupportMapFragment mapFragment;
    MapFragment mapFragment2;

    GoogleMap mapaTotal;

    //https://www.quora.com/How-can-I-open-map-in-map-fragment-inside-another-fragment-in-Android
    public GoogleMapExample() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(this.getActivity());
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE);
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment==null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map,mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }
    protected void requestPermission(String permissionType, int
            requestCode) {
        int permission = ContextCompat.checkSelfPermission(getContext(), permissionType);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{permissionType}, requestCode
            );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getContext(),"no   tiene el permiso",Toast.LENGTH_SHORT).show();
                } return;
            }
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_google_map_example, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng tuluaUceva = new LatLng(4.062975, -76.198423);

        this.mapaTotal=map;

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mapaTotal.setMyLocationEnabled(true);
        mapaTotal.moveCamera(CameraUpdateFactory.newLatLngZoom(tuluaUceva, 10));
        mapaTotal.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                Toast.makeText(getContext(),
                        point.latitude + ", " + point.longitude,
                        Toast.LENGTH_SHORT).show();
                anadirMarca(point.latitude,point.longitude);
            }
        });
        mapaTotal.addMarker(new MarkerOptions()
                .title("Sydney")
                //.icon(R.drawable.agenda)
                .snippet("The most populous city in Australia.")
                .position(tuluaUceva));
    }

    public void anadirMarca(double lat, double lon){
        LatLng punto = new LatLng(lat, lon);
        Toast.makeText(getContext(),
                lat + ", " + lon,
                Toast.LENGTH_SHORT).show();
        mapaTotal.addMarker(new MarkerOptions()
                .title("pendiente")
                .snippet("hola.")
                .position(punto));
        /*PolygonOptions options = new PolygonOptions();
        LatLng punto1 = new LatLng(lat, lon);
        LatLng punto2 = new LatLng(lat, lon);
        ArrayList<LatLng> puntos = new ArrayList<LatLng>();
        puntos.add(punto1);
        puntos.add(punto2);
        options.add(punto1);
        mapaTotal.addPolyline(options);*/

    }
}
