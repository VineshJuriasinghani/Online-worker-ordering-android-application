package com.technical.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.technical.myapplication.databinding.ActivityMaps2Binding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    SearchView searchview;
    static double cl_lat=0, cl_lng=0;
    Button confirm_add ;
    public static final int DEFAULT_ZOOM = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        confirm_add = findViewById(R.id.confirm_add);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.address_pickup);
        mapFragment.getMapAsync(this);
        searchview = findViewById(R.id.search);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


       searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               geoLocate();
               return false;
           }
           @Override
           public boolean onQueryTextChange(String newText) {
               return false;
           }
       });
       mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
           @Override
           public boolean onMarkerClick(@NonNull Marker marker) {
               confirm_add.setVisibility(View.VISIBLE);
         confirm_add.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 cl_lat=marker.getPosition().latitude;
                 cl_lng=marker.getPosition().longitude;
                 confirm_add.setVisibility(View.INVISIBLE);
                 finish();
             }
         });
                       return false;
           }
       });

        LatLng latLng=new LatLng(33.70381517738392, 73.06959169121485);
        mMap.setMinZoomPreference(5);
        mMap.addMarker(new MarkerOptions().position(new LatLng(33.70381517738392, 73.06959169121485)).icon(BitmapDescriptorFactory.fromResource(R.drawable.address)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 3000, null);

    }


    private void geoLocate() {
        //hideSoftKeyboard(view);
        String content = searchview.getQuery().toString();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(content,10);
            if(addressList.size()>0)
            {

                for ( Address a : addressList ){
                    gotoLocation(a.getLatitude(),a.getLongitude());
                    Toast.makeText(MapsActivity2.this, "address: "+a.getMaxAddressLineIndex(), Toast.LENGTH_SHORT).show();
            }
                mMap.getUiSettings().setMapToolbarEnabled(false);

                Toast.makeText(MapsActivity2.this, "Long press the title bar of marker which is nearest to your address!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(MapsActivity2.this, "Address not found!", Toast.LENGTH_SHORT).show();
            }
        }catch(IOException e) {

        }


    }
    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void gotoLocation(double lat,double lng){
        mMap.clear();
        LatLng latLng=new LatLng(lat,lng);
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.address)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(19), 2000, null);
    }
}