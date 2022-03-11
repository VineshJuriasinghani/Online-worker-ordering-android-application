package com.technical.myapplication.WorkerViewSuff;

import static com.technical.myapplication.MainActivity.reqholder;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TintableCheckedTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technical.myapplication.ClientsViewStuff.Client_view;
import com.technical.myapplication.ClientsViewStuff.model_class;
import com.technical.myapplication.ClientsViewStuff.my_adapter;
import com.technical.myapplication.MainActivity;
import com.technical.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Workerview_adapter extends RecyclerView.Adapter<Workerview_adapter.myviewholder> {

    ArrayList<WorkerModelClass> dataholder = new ArrayList<WorkerModelClass>();
private  int W_ID;
private double W_Lng;
private double W_Lat;
private FragmentManager fragmentManager;
client_data_dialog client_data_dialog;

    public Workerview_adapter(ArrayList<WorkerModelClass> dataholder, int w_ID, double c_Lng, double c_Lat,FragmentManager context) {
        this.dataholder = dataholder;
        W_ID = w_ID;
        W_Lng = c_Lng;
        W_Lat = c_Lat;
        this.fragmentManager = context;
    }
    Workerview_adapter()
    {}


    @Override
    public Workerview_adapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;


        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clients_requestes, parent, false);


        return new Workerview_adapter.myviewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Workerview_adapter.myviewholder holder, int position) {

        final WorkerModelClass data = dataholder.get(position);
        holder.Firstname.setText(data.getFName());
        holder.ratingBar.setRating(data.getRating());


        holder.Accept.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                client_data_dialog = new client_data_dialog(W_ID,dataholder.get(holder.getBindingAdapterPosition())
                        ,holder,W_Lat,W_Lng);
                client_data_dialog.setCancelable(false);
                client_data_dialog.show(fragmentManager, "dia");
                dataholder.remove(holder.getBindingAdapterPosition());
                notifyItemRemoved(holder.getBindingAdapterPosition());
                notifyItemRangeChanged(holder.getBindingAdapterPosition(),dataholder.size());
                return false;
            }



        });


        holder.Decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove_req(v.getContext(),data.getId(),
                W_ID,
                data.getLat(),
                data.getLng());
                holder.Decline.setText("declined");
                dataholder.remove(holder.getBindingAdapterPosition());
                notifyItemRemoved(holder.getBindingAdapterPosition());
                notifyItemRangeChanged(holder.getBindingAdapterPosition(),dataholder.size());
            }
        });

        holder.view_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new Client_position_map();
                Bundle bundle = new Bundle();
                bundle.putDouble("WLat",W_Lat);
                bundle.putDouble("WLng",W_Lng);
                bundle.putDouble("CLat",data.getLat());
                bundle.putDouble("CLng",data.getLng());
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame2, myFragment).addToBackStack(null).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView Firstname;
        RatingBar ratingBar;
        Button Decline , Accept;
        ImageView view_point;
        SwipeRefreshLayout swipeRefreshLayout;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            Decline = itemView.findViewById(R.id.rdecline);
            Accept = itemView.findViewById(R.id.raccept);
            view_point = itemView.findViewById(R.id.cposition);
            ratingBar = itemView.findViewById(R.id.rrating);
            Firstname = itemView.findViewById(R.id.rFName);



        }
    }




    public void remove_req(Context context,int UID, int W_ID, double lat, double lng)
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
                                Toast.makeText(context,"request declined", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(context,"request cannot be declined", Toast.LENGTH_SHORT).show();
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

