package com.technical.myapplication.ClientsViewStuff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.technical.myapplication.R;
import com.technical.myapplication.Worker_category_stuff.Categories_data;
import com.technical.myapplication.Worker_category_stuff.Category_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Nearby extends Fragment {

    private double Lat , Lng;
    private static final String color = "#E8F6EF";
    private double distance;
    boolean color_changed = false;

    private Category_adapter category_adapter;
    String url = "https://auxetic-personality.000webhostapp.com/retrieve_worker_data.php";

        private Spinner spinner;
        private View Gmap;
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        private int W_ID;
        private int UID;
        private double Wlat;
        private double Wlng;

        @Override
        public void onMapReady(GoogleMap googleMap) {
            Bundle bundle = getArguments();
            Lat = bundle.getDouble("Lat2");
            Lng = bundle.getDouble("Lng2");
            UID = bundle.getInt("UID");


            category_adapter = new Category_adapter(getContext(), Categories_data.getCategories());
            spinner.setAdapter(category_adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (!category_adapter.getItemName(position).equals("Select Category")) {

                        Gmap.setVisibility(View.VISIBLE);
                        googleMap.clear();
                        url = "https://auxetic-personality.000webhostapp.com/retrieve_worker_data.php";

                        StringRequest request = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String sucess = jsonObject.getString("success");
                                            JSONArray jsonArray = jsonObject.getJSONArray("workers");

                                            if (sucess.equals("1")) {
                                                for (int i = 0; i < jsonArray.length(); i++) {

                                                    JSONObject object = jsonArray.getJSONObject(i);
                                                    W_ID = object.getInt("W_ID");
                                                    Wlat = Double.parseDouble(object.getString("lat"));
                                                    Wlng = Double.parseDouble(object.getString("lng"));

                                                    float[] results = new float[1];
                                                    Location.distanceBetween(Lat, Lng, Wlat, Wlng, results);
                                                    distance = (results[0] / 1000);
                                                    distance = Math.round((distance * 100.0) / 100.0);
                                                    LatLng countries = new LatLng(Wlat, Wlng);
                                                    Marker marker = googleMap.addMarker(new MarkerOptions().position(countries).title(distance + " km").icon(BitmapDescriptorFactory.fromResource(R.drawable.worker)));
                                                    marker.setZIndex(W_ID);

                                                }

                                            } else {
                                                Toast.makeText(getContext(), "No workers available yet", Toast.LENGTH_SHORT).show();
                                            }


                                            LatLng syd = new LatLng(Lat, Lng);
                                            Circle circle = googleMap.addCircle(new CircleOptions()
                                                    .center(new LatLng(Lat, Lng))
                                                    .radius(15000)
                                                    .fillColor(Color.parseColor(color))
                                                    .strokeWidth(1)
                                            );

                                            googleMap.addMarker(new MarkerOptions().position(syd).icon(BitmapDescriptorFactory.fromResource(R.drawable.client)));
                                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(syd));
                                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null);
                                            googleMap.setMinZoomPreference(6);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("category", category_adapter.getItemName(position));
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(request);



                    } else {

                        Gmap.setVisibility(View.INVISIBLE);

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            googleMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
                @Override
                public void onInfoWindowLongClick(@NonNull Marker marker) {

                    worker_data_dialog dialog = new worker_data_dialog(UID,(int) marker.getZIndex(),Lat,Lng);
                    dialog.setCancelable(false);

                    dialog.show(getFragmentManager(), "dialog");
                }
            });


            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {

                    if (marker.getPosition().latitude == Lat && marker.getPosition().longitude == Lng) {
                        googleMap.getUiSettings().setMapToolbarEnabled(false);
                        if (!color_changed) {
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.client_yellow));
                            color_changed = true;
                        } else {

                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.client));
                            color_changed = false;

                        }

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


        View view= inflater.inflate(R.layout.fragment_nearby, container, false);
        spinner = view.findViewById(R.id.spinner);
        Gmap = view.findViewById(R.id.map);

        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }


    }


}