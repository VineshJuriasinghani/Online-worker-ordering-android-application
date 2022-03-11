package com.technical.myapplication.ClientsViewStuff;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technical.myapplication.R;
import com.technical.myapplication.WorkerViewSuff.client_data_dialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class my_adapter extends RecyclerView.Adapter<my_adapter.myviewholder> {

    ArrayList<model_class> dataholder;
    String cardview_id;
    Context context;


    public my_adapter(ArrayList<model_class> dataholder, String cardview_id , Context context) {
        this.dataholder = dataholder;
        this.cardview_id=cardview_id;
        this.context = context;
    }

    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if(cardview_id.equals("work_in_process")) {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_in_process, parent, false);
        }
        if(cardview_id.equals("work_completed")) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_completed, parent, false);
        }
        if(cardview_id.equals("accepted_request")) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accepted_req, parent, false);
        }
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {

        final model_class data = dataholder.get(position);
        holder.Fname.setText(data.getFName());
        holder.Phno.setText(data.getPhno());

        if(cardview_id .equals( "work_completed")) {

            holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    update_client_rating(ratingBar.getContext(),holder.getBindingAdapterPosition(),rating,holder);
                }
            });
        }
        if(cardview_id .equals("accepted_request"))
        {
            holder.cencel_req.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(v.getContext(),data.getUID(),data.getW_ID(), data.getLat(),data.getLng(),holder);

                }
            });
        }

        holder.Phno.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String ph = "tel:"+data.getPhno();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(ph));
                context.startActivity(intent);
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView Fname,Phno;
        RatingBar ratingBar;
        Button cencel_req;
        public myviewholder(@NonNull View itemView) {
            super(itemView);

            cencel_req = itemView.findViewById(R.id.cq);
            ratingBar = itemView.findViewById(R.id.rating);
            Fname = itemView.findViewById(R.id.FName);
            Phno = itemView.findViewById(R.id.phone);

        }
    }
    public void delete(Context context,int UID, int W_ID, double lat, double lng , myviewholder holder)
    {

        String url = "https://auxetic-personality.000webhostapp.com/delete_req.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            if(sucess.equals("1")){
                                dataholder.remove(holder.getBindingAdapterPosition());
                                notifyItemRemoved(holder.getBindingAdapterPosition());
                                notifyItemRangeChanged(holder.getBindingAdapterPosition(),dataholder.size());

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

    private void update_client_rating(Context context, int position,  double rating, myviewholder holder) {

        model_class object = dataholder.get(position);
      String  url = "https://auxetic-personality.000webhostapp.com/update_worker_rating.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            if(sucess.equals("1")){
                                Toast.makeText(context,"Thanks for your response", Toast.LENGTH_SHORT).show();
                                delete(context,object.getUID(), object.getW_ID(), object.getLat(), object.getLng()
                                        ,holder);

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


                params.put("W_ID", String.valueOf(dataholder.get(holder.getBindingAdapterPosition()).getW_ID()));
                params.put("rating",String.valueOf(rating));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);


    }



}
