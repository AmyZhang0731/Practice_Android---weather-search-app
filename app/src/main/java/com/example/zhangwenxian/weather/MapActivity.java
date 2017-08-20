package com.example.zhangwenxian.weather;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hamweather.aeris.communication.AerisCallback;
import com.hamweather.aeris.communication.AerisEngine;
import com.hamweather.aeris.communication.parameter.PlaceParameter;
import com.hamweather.aeris.maps.AerisGoogleInfoAdapter;
import com.hamweather.aeris.maps.AerisMapView;
import com.hamweather.aeris.maps.MapViewFragment;
import com.hamweather.aeris.maps.interfaces.OnAerisMapLongClickListener;
import com.hamweather.aeris.model.Place;
import com.hamweather.aeris.tiles.AerisTile;

public class MapActivity extends AppCompatActivity { //} implements OnMapReadyCallback {

    String lat="", lng="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        lat = extras.getString("lat");
        lng = extras.getString("lng");

        Bundle bundle = new Bundle();
        bundle.putString("lat", lat);
        bundle.putString("lng", lng);
        setContentView(R.layout.map_frame);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MapFragment map = new MapFragment();
        map.setArguments(bundle);
        fragmentTransaction.add(R.id.map_frame, map);
        fragmentTransaction.commit();


    }

    public static class MapFragment extends MapViewFragment{

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            Bundle bundle = getArguments();
            String lat = bundle.getString("lat");
            String lng = bundle.getString("lng");
            double lat_d = Double.parseDouble(lat);
            double lng_d = Double.parseDouble(lng);

            View view = inflater.inflate(R.layout.activity_maps, container, false);
            // setting up secret key and client id for oauth to aeris
            AerisEngine.initWithKeys(this.getString(R.string.aeris_client_id), this.getString(R.string.aeris_client_secret), "com.example.zhangwenxian.weather");

            mapView = (AerisMapView) view.findViewById(R.id.aerisfragment_map);

            mapView.init(savedInstanceState, AerisMapView.AerisMapType.GOOGLE);


            //mapView.setOnAerisMapLongClickListener(this);
            mapView.addLayer(AerisTile.RADSAT);

            LatLng location = new LatLng(lat_d, lng_d);
            mapView.moveToLocation(location,9);
            mapView.setZoomControlsEnabled(true);

            //mapView.onCameraChange(CameraUpdateFactory.newLatLng(sydney));
            return view;
        }


        /*@Override
        public void onMapLongClick(double lat, double longitude) {
            // code to handle map long press. i.e. Fetch current conditions?
            // see demo app MapActivity.java
        }*/
    }


}


/* private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // setting up secret key and client id for oauth to aeris
        AerisEngine.initWithKeys(this.getString(R.string.aeris_client_id), this.getString(R.string.aeris_client_secret), this);
    }


    *//*
*/
/**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *//*
*/
/*
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }*//*

}
*/
