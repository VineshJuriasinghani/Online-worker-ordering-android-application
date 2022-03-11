package com.technical.myapplication.WorkerViewSuff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.technical.myapplication.ClientsViewStuff.model_class;
import com.technical.myapplication.R;

public class Client_position_map extends Fragment {

    private double Lat , Lng;
    private double distance;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {



        @Override
        public void onMapReady(GoogleMap googleMap) {



                Bundle bundle = getArguments();

                Lat = bundle.getDouble("WLat");
                Lng = bundle.getDouble("WLng");
            double lat = bundle.getDouble("CLat");
            double lng = bundle.getDouble("CLng");


            Toast.makeText(getContext(), Lat+"\n"+Lng, Toast.LENGTH_SHORT).show();

            float results[] = new float[1];
            Location.distanceBetween(Lat, Lng, lat, lng, results);
            distance = (results[0] / 1000);
            distance = Math.round((distance * 100.0) / 100.0);
            LatLng Loc = new LatLng(lat,lng);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(Loc).title(distance + " km").icon(BitmapDescriptorFactory.fromResource(R.drawable.client)));
            LatLng syd = new LatLng(Lat, Lng);

            googleMap.addMarker(new MarkerOptions().position(syd).icon(BitmapDescriptorFactory.fromResource(R.drawable.worker)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(syd));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
            googleMap.setMinZoomPreference(6);



            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {

                    if (marker.getPosition().latitude == Lat && marker.getPosition().longitude == Lng) {
                        googleMap.getUiSettings().setMapToolbarEnabled(false);

                    } else {
                        googleMap.getUiSettings().setMapToolbarEnabled(true);
                    }
                    return false;
                }
            });



        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_position_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.worker_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}