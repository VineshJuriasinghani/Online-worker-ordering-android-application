package com.technical.myapplication.ClientsViewStuff;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technical.myapplication.MainActivity;
import com.technical.myapplication.MapsActivity2;
import com.technical.myapplication.R;
import com.technical.myapplication.Registration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class worker_data_dialog extends DialogFragment {

    private ImageView exit;
    private double lat=0,lng=0;
    private int UID,W_phno,W_ID;
    private Button request_button ;
    private String color = "#FF03DAC5" , color1 = "#31afb4";
    private String url;
    private String Fname,Lname,Phno,rating,status,profession;
    TextView FirstName,LastName,pro,stats;
    RatingBar ratingBar;
    public worker_data_dialog(int UID, int W_ID, double lat, double lng)
    {

        this.UID = UID;
        this.W_ID = W_ID;
        this.lat = lat;
        this.lng = lng;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_workerdata,container,false);
        exit = view.findViewById(R.id.exit);
        request_button = view.findViewById(R.id.request_button);
        FirstName =view.findViewById(R.id.FirstName);
        LastName = view.findViewById(R.id.LastName);
        ratingBar = view.findViewById(R.id.show_rating);
        pro = view.findViewById(R.id.profession);
        stats = view.findViewById(R.id.status);


         retrieveWorkerData();

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insert_in_request_table();

            }
        });



        return view;
    }


    private void insert_in_request_table()
    {
        url ="https://auxetic-personality.000webhostapp.com/send_req.php";

        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                request_button.setText("Request sent");
                Drawable buttonDrawable = request_button.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, Color.parseColor(color));
                request_button.setBackground(buttonDrawable);
                request_button.setBackgroundColor(Color.parseColor(color));

            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("UID", String.valueOf(UID));
                params.put("W_ID", String.valueOf(W_ID));
                params.put("lat",String.valueOf(lat));
                params.put("lng", String.valueOf(lng));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }




    private void retrieveWorkerData(){

        url ="https://auxetic-personality.000webhostapp.com/retrieve_worker_data_by_ID.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("workers");
                            if(sucess.equals("1"))
                            {

                                for(int i=0;i<jsonArray.length();i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    Fname = object.getString("Fname");
                                    Lname = object.getString("Lname");
                                    rating = object.getString("rating");
                                    status = object.getString("status");
                                    profession = object.getString("CName");

                                    check_if_request_sent();
                                    FirstName.setText(Fname);
                                    LastName.setText(Lname);
                                    ratingBar.setRating(Float.parseFloat(rating));
                                    pro.setText(profession);
                                    stats.setText(status);

                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(),"no record available", Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                params.put("W_ID", String.valueOf(W_ID));
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }
    void check_if_request_sent()
    {
        url ="https://auxetic-personality.000webhostapp.com/check_if_req_sent.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            if(sucess.equals("1"))
                            {
                                request_button.setText("Request sent");
                                Drawable buttonDrawable = request_button.getBackground();
                                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                                DrawableCompat.setTint(buttonDrawable, Color.parseColor(color));
                                request_button.setBackground(buttonDrawable);
                                request_button.setBackgroundColor(Color.parseColor(color));
                                request_button.setClickable(false);
                            }
                            else
                            {
                                request_button.setClickable(true);
                                request_button.setText("send request");
                                Drawable buttonDrawable = request_button.getBackground();
                                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                                DrawableCompat.setTint(buttonDrawable, Color.parseColor(color1));
                                request_button.setBackground(buttonDrawable);
                                request_button.setBackgroundColor(Color.parseColor(color1));
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                params.put("W_ID", String.valueOf(W_ID));
                params.put("UID",String.valueOf(UID));
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }



}
