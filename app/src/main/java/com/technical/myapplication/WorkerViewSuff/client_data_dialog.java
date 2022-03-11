package com.technical.myapplication.WorkerViewSuff;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technical.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class client_data_dialog extends DialogFragment {
private int W_ID;
private double Wlat,Wlng;
TextView FirstName,LastName,phno;
RatingBar ratingBar;
Button start,decline;
ImageView directions;
WorkerModelClass workerModelClass;
Workerview_adapter.myviewholder holder;
String wstatus;
String url;
Workerview_adapter workerview_adapter = new Workerview_adapter();
public client_data_dialog(int W_ID, WorkerModelClass workerModelClass, Workerview_adapter.myviewholder holder,
                          double w_Lat, double w_Lng)
        {
        this.W_ID = W_ID;
        this.workerModelClass = workerModelClass;
        this.holder = holder;
        Wlat=w_Lat;
        Wlng=w_Lng;
        }
  public client_data_dialog()
  {}


@Nullable
@Override
public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_clientdata,container,false);
        start = view.findViewById(R.id.start_working);
        decline = view.findViewById(R.id.Decline_req);
        FirstName =view.findViewById(R.id.cFirstName);
        LastName = view.findViewById(R.id.cLastName);
        ratingBar = view.findViewById(R.id.cshow_rating);
        phno = view.findViewById(R.id.cphno);
        directions = view.findViewById(R.id.directions);
        decline.setVisibility(View.VISIBLE);
        start.setText("start working");
        change_state("accepted");
        FirstName.setText(workerModelClass.getFName());
        LastName.setText(workerModelClass.getLName());
        ratingBar.setRating(workerModelClass.getRating());
        phno.setText(workerModelClass.getPhno());

        directions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        Intent intent = new Intent(getContext(),Client_position_map.class);
                        intent.putExtra("clat",workerModelClass.getLat());
                        intent.putExtra("clng",workerModelClass.getLng());
                        intent.putExtra("wlat",Wlat);
                        intent.putExtra("wlng",Wlng);
                        startActivity(intent);

                }
        });

        decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        url = "https://auxetic-personality.000webhostapp.com/delete_req.php";
                        remove_req_dialog(getContext(),workerModelClass.getId(),W_ID,workerModelClass.getLat(),workerModelClass.getLng());
                }
        });

        start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        if(!start.getText().equals("end work"))
                        {
                                decline.setVisibility(View.INVISIBLE);
                                start.setText("end work");
                                change_state("work started");
                        }
                        else {
                                change_state("workcompleted");
                                start.setText("work ended");
                                start.setClickable(false);
                                ratingBar.setIsIndicator(false);
                                ratingBar.setRating(0);
                                Toast.makeText(getContext(), "please give feedback", Toast.LENGTH_SHORT).show();

                                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                        @Override
                                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                                if(rating > 0)
                                                {
                                                        update_client_rating(getContext(),workerModelClass.getId(),rating);
                                                }
                                        }
                                });

                        }

                }
        });

        phno.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                        String ph = "tel:"+workerModelClass.getPhno();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(ph));
                        startActivity(intent);
                        return false;
                }
        });

        return view;
        }

        private void update_client_rating(Context context, int id, double rating) {

                 url = "https://auxetic-personality.000webhostapp.com/update_client_rating.php";

                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                        try{

                                                JSONObject jsonObject = new JSONObject(response);
                                                String sucess = jsonObject.getString("success");
                                                if(sucess.equals("1")){
                                                        Toast.makeText(context,"Thanks for your response", Toast.LENGTH_SHORT).show();
                                                        getDialog().dismiss();
                                                }


                                        }
                                        catch (JSONException e){
                                                e.printStackTrace();
                                        }
                                }
                        }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                })
                {
                        @Override
                        protected Map<String,String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<String,String>();

                                params.put("UID", String.valueOf(id));
                                params.put("rating",String.valueOf(rating));
                                return params;
                        }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(request);


        }




        public void change_state(String status)
        {
                String url = "https://auxetic-personality.000webhostapp.com/update_status.php";

                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                        try{
                                                JSONObject jsonObject = new JSONObject(response);
                                                String sucess = jsonObject.getString("success");
                                                if(sucess.equals("1")){
                                                }
                                                else
                                                {
                                                        Toast.makeText(getContext(),"Sorry client has declined the request", Toast.LENGTH_SHORT).show();
                                                        getDialog().dismiss();
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
                                Map<String,String> params = new HashMap<String,String>();

                                params.put("W_ID", String.valueOf(W_ID));
                                params.put("UID", String.valueOf(workerModelClass.getId()));
                                params.put("lat", String.valueOf(workerModelClass.getLat()));
                                params.put("lng", String.valueOf(workerModelClass.getLng()));
                                params.put("status",status);
                                if(status.equals("work started"))
                                {
                                        wstatus = "working";
                                }
                                else
                                {
                                        wstatus = "not working";
                                }
                                params.put("worker_working_status",wstatus);

                                return params;
                        }
                };


                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(request);

        }



        public void remove_req_dialog(Context context,int UID, int W_ID, double lat, double lng)
        {

                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                        try{

                                                JSONObject jsonObject = new JSONObject(response);
                                                String sucess = jsonObject.getString("success");
                                                if(sucess.equals("1")){
                                                        getDialog().dismiss();
                                                }


                                        }
                                        catch (JSONException e){
                                                e.printStackTrace();
                                        }
                                }
                        }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                })
                {
                        @Override
                        protected Map<String,String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<String,String>();

                                params.put("W_ID", String.valueOf(W_ID));
                                params.put("UID", String.valueOf(UID));
                                params.put("lat", String.valueOf(lat));
                                params.put("lng", String.valueOf(lng));
                                return params;
                        }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(request);


        }

}





