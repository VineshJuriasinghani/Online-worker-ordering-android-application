package com.technical.myapplication;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.technical.myapplication.ClientsViewStuff.Client_view;
import com.technical.myapplication.ClientsViewStuff.model_class;
import com.technical.myapplication.WorkerViewSuff.WorkerModelClass;
import com.technical.myapplication.WorkerViewSuff.WorkerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    private String url ,phone , pass , Fname , Lname , Cnic ,rating ;
    static public int UID,W_ID;
    private LocationRequest locationRequest;
    private Button button;
    private EditText PhoneNo, password;
    private TextView signup;
    private RadioGroup select;
     public double lat,lng;
    static public ArrayList<WorkerModelClass> reqholder = new ArrayList<WorkerModelClass>();
    static public ArrayList<model_class> process , accepted , work_completed;
    boolean  selected_client = false , selected_worker = false ;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        process = new ArrayList<model_class>();
        accepted = new ArrayList<model_class>();
        work_completed = new ArrayList<model_class>();

        button = findViewById(R.id.signin);
        PhoneNo = findViewById(R.id.pno);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        select = findViewById(R.id.category);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_empty() == 1) {
                    PhoneNo.setError("Phone No: required");
                    password.setError("password required");
                } else if (is_empty() == 2) {
                    PhoneNo.setError("Phone No: required");
                } else if (is_empty() == 3) {
                    password.setError("password required");
                } else {

                       phone = PhoneNo.getText().toString().trim();
                       pass = password.getText().toString().trim();
                    if(selected_worker)
                    {
                        retrieveWorkerData();

                    }
                    else if(selected_client)
                    {
                        retrieveUserData();
                    }

                }
            }

        });

        select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId ==  R.id.worker) {
                    selected_worker =true;
                    selected_client=false;
                }

                else if(checkedId==R.id.client) {
                    selected_client = true;
                    selected_worker = false;
                }
            }

        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });

    }


        public void retrieveWorkerData()
        {
            url = "https://auxetic-personality.000webhostapp.com/retrieve_worker_ID.php";

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                String sucess = jsonObject.getString("success");
                                JSONArray jsonArray = jsonObject.getJSONArray("workers");

                                if(sucess.equals("1")){

                                    Toast.makeText(MainActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();

                                    for(int i=0;i<jsonArray.length();i++){

                                        JSONObject object = jsonArray.getJSONObject(i);
                                        W_ID = object.getInt("W_ID");

                                    }
                                    see_requests();


                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this,"Phone or Password are incorrect", Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("Phno",phone);
                    params.put("password",pass);
                    return params;
                }
            };


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);

        }

        public void retrieve_status()
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

                                accepted.clear();
                                process.clear();
                                work_completed.clear();
                                if(sucess.equals("1")){


                                    for(int i=0;i<jsonArray.length();i++) {

                                        JSONObject object = jsonArray.getJSONObject(i);


                                        if(object.getString("status").equals("accepted"))
                                        {


                                            if(accepted.isEmpty())
                                            {
                                                accepted.add(new model_class(object.getString("Fname"),object.getString("Phno"),
                                                        object.getInt("rating"),object.getString("status"),object.getInt("W_ID"),UID,object.getDouble("lat"),
                                                        object.getDouble("lng")));
                                            }
                                            else
                                                {
                                                for(int j = 0; j < accepted.size(); j++)
                                                {
                                                    if (accepted.get(j).getW_ID() != object.getInt("W_ID")) {
                                                        accepted.add(new model_class(object.getString("Fname"),object.getString("Phno"),
                                                                object.getInt("rating"),object.getString("status"),object.getInt("W_ID"),UID,object.getDouble("lat"),
                                                                object.getDouble("lng")));
                                                    }
                                                }
                                            }

                                        }
                                         else if(object.getString("status").equals("work started"))
                                        {

                                            if(process.isEmpty())
                                            {
                                                process.add(new model_class(object.getString("Fname"),object.getString("Phno"),
                                                        object.getInt("rating"),object.getString("status"),object.getInt("W_ID"),UID,object.getDouble("lat"),
                                                        object.getDouble("lng")));
                                            }
                                            else {
                                                for(int j = 0; j < process.size(); j++)
                                                {
                                                    if (process.get(j).getW_ID() != object.getInt("W_ID")) {
                                                        process.add(new model_class(object.getString("Fname"),object.getString("Phno"),
                                                                object.getInt("rating"),object.getString("status"),object.getInt("W_ID"),UID,object.getDouble("lat"),
                                                                object.getDouble("lng")));
                                                    }
                                                }
                                            }
                                        }
                                         else if(object.getString("status").equals("workcompleted"))
                                        {

                                            if(work_completed.isEmpty())
                                            {
                                                work_completed.add(new model_class(object.getString("Fname"),object.getString("Phno"),
                                                        object.getInt("rating"),object.getString("status"),object.getInt("W_ID"),UID,object.getDouble("lat"),
                                                        object.getDouble("lng")));
                                            }
                                            else {
                                                for(int j = 0; j < work_completed.size(); j++)
                                                {
                                                    if (work_completed.get(j).getW_ID() != object.getInt("W_ID")) {
                                                        work_completed.add(new model_class(object.getString("Fname"),object.getString("Phno"),
                                                                object.getInt("rating"),object.getString("status"),object.getInt("W_ID"),UID,object.getDouble("lat"),
                                                                object.getDouble("lng")));
                                                    }
                                                }
                                            }

                                        }

                                    }

                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this,"No requests found", Toast.LENGTH_SHORT).show();
                                }
                                intent = new Intent(MainActivity.this,Client_view.class);
                                intent.putExtra("UID",UID);
                                intent.putExtra("Lat",lat);
                                intent.putExtra("Lng",lng);
                                startActivity(intent);


                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("UID", String.valueOf(UID));
                    return params;
                }
            };


            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(request);




        }

        public void retrieveUserData(){

        url = "https://auxetic-personality.000webhostapp.com/retrieve_user_data.php";

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                String sucess = jsonObject.getString("success");
                                JSONArray jsonArray = jsonObject.getJSONArray("users");

                                if(sucess.equals("1")){

                                    Toast.makeText(MainActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();

                                    for(int i=0;i<jsonArray.length();i++){

                                        JSONObject object = jsonArray.getJSONObject(i);
                                        UID = object.getInt("UID");
                                        Fname = object.getString("Fname");
                                        Lname = object.getString("Lname");
                                        Cnic = object.getString("Cnic");
                                        rating = object.getString("rating");

                                    }
                                    getCurrentLocation();


                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this,"Phone or Password are incorrect", Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("Phno",phone);
                    params.put("password",pass);
                    return params;
                }
            };


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);

        }


    int is_empty()
    {
        String Username, Password;
        Username = PhoneNo.getText().toString();
        Password = password.getText().toString();
        if(Username.isEmpty() && Password.isEmpty())
        {
            return  1;
        }
        else if(Username.isEmpty())
        {
            return 2;
        }
        else if(Password.isEmpty())
        {
            return 3;
        }
        return 0;
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
                                                    object.getString("Fname"), object.getString("Lname"), object.getString("Phno"),
                                                    object.getDouble("lat"), object.getDouble("lng"),
                                                    object.getInt("rating")));
                                            }
                                        }
                                    }
                                }

                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"No requests found", Toast.LENGTH_SHORT).show();
                            }

                            getCurrentLocation();

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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


        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0) {

                                        int index = locationResult.getLocations().size() - 1;

                                        lat = locationResult.getLocations().get(index).getLatitude();
                                        lng = locationResult.getLocations().get(index).getLongitude();
                                        if(selected_worker)
                                        {
                                            intent = new Intent(MainActivity.this, WorkerView.class);
                                            intent.putExtra("W_ID",W_ID);
                                            intent.putExtra("Lat",lat);
                                            intent.putExtra("Lng",lng);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            retrieve_status();
                                        }




                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(MainActivity.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled()
    {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }





}