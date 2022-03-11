package com.technical.myapplication.WorkerViewSuff;

import static com.technical.myapplication.MainActivity.reqholder;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.TokenWatcher;
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

public class clientsrequests extends Fragment {
    RecyclerView recyclerView;
    private  int W_ID;
    Bundle bundle;
    SwipeRefreshLayout swipeRefreshLayout;
    View view;
    public clientsrequests() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        bundle = getArguments();
        W_ID = bundle.getInt("W_ID");
        view = inflater.inflate(R.layout.fragment_clientsrequests, container, false);
        swipeRefreshLayout= view.findViewById(R.id.swipe_request);

            recyclerView  = view.findViewById(R.id.request_list);
            recyclerView.setAdapter(new Workerview_adapter(reqholder,W_ID,
                    bundle.getDouble("Lng2"),bundle.getDouble("Lat2"), getFragmentManager()));
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    see_requests();

                }
            });

        return view;
    }


    public void see_requests()
    {

        String url = "https://auxetic-personality.000webhostapp.com/get_requests.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            reqholder.clear();
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("workers");

                            if(sucess.equals("1")){

                                for(int i=0;i<jsonArray.length();i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    reqholder.clear();
                                    if(reqholder.isEmpty())
                                    {
                                        reqholder.add(new WorkerModelClass(object.getInt("UID"),
                                                object.getString("Fname"), object.getString("Lname"),object.getString("Phno"),
                                                object.getDouble("lat"), object.getDouble("lng"),
                                                object.getInt("rating")));
                                    }
                                    else {
                                        for(int j = 0; j < reqholder.size(); j++)
                                        {
                                            if (reqholder.get(j).getId() != object.getInt("UID")) {
                                                reqholder.add(new WorkerModelClass(object.getInt("UID"),
                                                        object.getString("Fname"), object.getString("Lname"),object.getString("Phno"),
                                                        object.getDouble("lat"), object.getDouble("lng"),
                                                        object.getInt("rating")));
                                            }
                                        }
                                    }
                                }

                            }
                            else
                            {
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
                params.put("W_ID", String.valueOf(W_ID));
                params.put("status","workcompleted");
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);


    }



    }
