package com.technical.myapplication.ClientsViewStuff;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class Aceepted_req extends Fragment {


private RecyclerView recyclerView ;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_aceepted_req, container, false);


        swipeRefreshLayout = view.findViewById(R.id.swipe_request);
            recyclerView = view.findViewById(R.id.acceptedlist);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new my_adapter(MainActivity.accepted, "accepted_request", getContext()));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieve_accepted_status();

            }
        });
        return view;

    }
    public void retrieve_accepted_status()
    {

        String url = "https://auxetic-personality.000webhostapp.com/get_status.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("requests");

                            if(sucess.equals("1")){
                                MainActivity.accepted.clear();
                                for(int i=0;i<jsonArray.length();i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    if(object.getString("status").equals("accepted"))
                                    {

                                        if(MainActivity.accepted.isEmpty())
                                        {
                                            MainActivity.accepted.add(new model_class(object.getString("Fname"),object.getString("Phno"),
                                                    object.getInt("rating"),object.getString("status"),object.getInt("W_ID"),MainActivity.UID,object.getDouble("lat"),
                                                    object.getDouble("lng")));
                                        }
                                        else {
                                            for(int j = 0; j < MainActivity.accepted.size(); j++)
                                            {
                                                if (MainActivity.accepted.get(j).getW_ID() != object.getInt("W_ID")) {
                                                    MainActivity.accepted.add(new model_class(object.getString("Fname"),object.getString("Phno"),
                                                            object.getInt("rating"),object.getString("status"),object.getInt("W_ID"),MainActivity.UID,object.getDouble("lat"),
                                                            object.getDouble("lng")));
                                                }
                                            }
                                        }
                                    }


                                }

                            }
                            else
                            {
                                MainActivity.accepted.clear();

                                Toast.makeText(getContext(),"No requests found", Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                        recyclerView.getAdapter().notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
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
                params.put("UID", String.valueOf(MainActivity.UID));
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);




    }

}


