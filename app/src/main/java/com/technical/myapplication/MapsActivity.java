package com.technical.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.technical.myapplication.databinding.ActivityMapsBinding;


import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final String color = "#E8F6EF";
    private double distance;
    boolean color_changed = false;

    ArrayList<LatLng> list = new ArrayList<LatLng>();
    double lat1[] = {24.921521310504158,24.894318990126624 ,24.85103843429758};
    double lng1[] = { 67.12995756571152, 67.21682087143444,67.26410032122322};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        for(int i=0;i<3;i++) {
          LatLng countries = new LatLng(lat1[i],lng1[i]);
            list.add(countries);
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        double lat = b.getDouble("lat");
        double lng = b.getDouble("long");

        Toast.makeText(this, "Lat:"+lat, Toast.LENGTH_SHORT).show();
        LatLng syd = new LatLng(lat,lng);

        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(lat,lng))
                .radius(15000)
                .fillColor(Color.parseColor(color))
                .strokeWidth(1)

        );

        mMap.addMarker(new MarkerOptions().position(syd).icon(BitmapDescriptorFactory.fromResource(R.drawable.client)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(syd));
        for(int i=0 ; i<list.size();i++) {

            float results[] = new float[1];
            Location.distanceBetween(lat, lng, lat1[i], lng1[i], results);
             distance  =  (results[0] / 1000);
             distance = Math.round(distance * 100.0) / 100.0;
            mMap.addMarker(new MarkerOptions().position(list.get(i)).title(distance + " km").icon(BitmapDescriptorFactory.fromResource(R.drawable.worker)));
        }
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
        mMap.setMinZoomPreference(6);

        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(@NonNull Marker marker) {

            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                if(marker.getPosition().latitude == lat && marker.getPosition().longitude == lng)
                {
                    mMap.getUiSettings().setMapToolbarEnabled(false);
                    if(color_changed == false) {
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.client_yellow));
                        color_changed = true;
                    }
                    else{

                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.client));
                        color_changed = false;

                    }

                }
                else
                {
                    mMap.getUiSettings().setMapToolbarEnabled(true);
                }
                return false;
            }
        });

        //mMap.addMarker(new MarkerOptions().position(syd).title("Name: vinesh  Address: sidnisenceddnid"));

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(syd));
    }
}