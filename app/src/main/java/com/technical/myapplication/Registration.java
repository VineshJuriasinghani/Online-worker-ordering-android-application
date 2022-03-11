package com.technical.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

           static Button Signup;
            boolean selected_client = false , selected_worker = false , Empty = false ;
            private EditText firstname, password , lastname , phone , cnic , repass;
            String Firstname, Password , Lastname ,Phone , Cnic ;
            RadioGroup radioGroup;
            LinearLayout pick_add;
            ImageView map_icon;
            String url;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_registration);

                firstname = findViewById(R.id.firstname);
                password = findViewById(R.id.pass);
                repass = findViewById(R.id.repass);
                lastname = findViewById(R.id.lastname);
                Signup = findViewById(R.id.sup);
                phone = findViewById(R.id.phone);
                cnic = findViewById(R.id.cnic);
                radioGroup = findViewById(R.id.category);
                pick_add = findViewById(R.id.enter_add);
                map_icon = findViewById(R.id.map_icon);



                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if(checkedId ==  R.id.worker) {
                                selected_worker =true;
                                selected_client=false;
                                pick_add.setVisibility(View.VISIBLE);
                                Signup.setText("next");

                            }

                            else if(checkedId==R.id.client) {
                                selected_client = true;
                                Signup.setText("signup");
                                MapsActivity2.cl_lat=0;
                                MapsActivity2.cl_lng=0;
                                selected_worker = false;
                                pick_add.setVisibility(View.INVISIBLE);
                            }
                        }

                });
                Signup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        is_empty();
                        if(Signup.getText() == "next" && !is_empty() && MapsActivity2.cl_lat!=0 && MapsActivity2.cl_lng !=0) {
                            Intent intent = new Intent(Registration.this, select_skills.class);
                            startActivity(intent);
                        }

                    }
                });
                map_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Registration.this, MapsActivity2.class);
                        startActivity(intent);
                    }
                });
            }
            boolean is_empty() {
                Empty = false;

                if ((firstname.getText().toString()).isEmpty()) {
                    Empty =true;
                    firstname.setError("First Name required");
                } if ((lastname.getText().toString()).isEmpty()) {
                    lastname.setError("last Name required");
                    Empty =true;
                } if ((password.getText().toString()).isEmpty()) {
                    password.setError("Password required");
                    Empty =true;
                }  if ((repass.getText().toString()).isEmpty()) {
                    repass.setError("retype password");
                    Empty =true;
                }  if ((cnic.getText().toString()).isEmpty()) {
                    cnic.setError("cnic required");
                    Empty =true;
                }  if ((phone.getText().toString()).isEmpty()) {
                    phone.setError("Phone no required");
                    Empty =true;
                }
                if(!selected_worker && !selected_client)
                {
                    Empty =true;
                    Toast.makeText(Registration.this, "Please select one of the category", Toast.LENGTH_SHORT).show();
                }

                if(selected_worker && (MapsActivity2.cl_lat==0 ||
                        MapsActivity2.cl_lng ==0))
                {
                    Toast.makeText(Registration.this, "Please select your address from Map", Toast.LENGTH_SHORT).show();
                }
                if(!password.getText().toString().trim().equals(repass.getText().toString().trim()))
                {
                    Empty = true;
                    Toast.makeText(Registration.this, ""+password.getText().toString().trim()+"\n"+repass.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                    repass.setError("password does not match");
                    password.setError("Password does not match");
                }

                else if(!Empty && (selected_client  || (selected_worker && !select_skills.skills.isEmpty() && (MapsActivity2.cl_lat!=0 && MapsActivity2.cl_lng!=0)))){
                    if(password.getText().toString().equals(repass.getText().toString()))
                    {
                        if(selected_client)
                        {
                             url = "https://auxetic-personality.000webhostapp.com/registration_client.php";
                        }
                        else if(selected_worker)
                        {
                            url = "https://auxetic-personality.000webhostapp.com/registration_worker.php";

                        }
                        Firstname = firstname.getText().toString().trim();
                        Lastname = lastname.getText().toString().trim();
                        Password = password.getText().toString().trim();
                        Phone  = phone.getText().toString().trim();
                        Cnic = cnic.getText().toString().trim();
                        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(response.equals("registered successfully"))
                                {

                                    if(selected_worker)
                                    {

                                        store_worker_categories();
                                    }
                                    else
                                    {
                                        Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                }
                                else
                                Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();

                            }
                        }, new Response.ErrorListener(){

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Registration.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        ){
                            @Override
                            protected Map<String,String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String,String>();
                           params.put("Fname",Firstname);
                           params.put("Lname",Lastname);
                           params.put("Cnic",Cnic);
                           params.put("Phno",Phone);
                           params.put("password",Password);
                           if(selected_worker){
                               params.put("lat",String.valueOf(MapsActivity2.cl_lat));
                           params.put("lng",String.valueOf(MapsActivity2.cl_lng));
                           }
                            return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
                        requestQueue.add(request);

                    } //if


                }

                return Empty;
            }

boolean yes = false;
            void store_worker_categories() {

                url = "https://auxetic-personality.000webhostapp.com/store_worker_categories.php";
                Phone = phone.getText().toString().trim();
                for (int i = 0; i < select_skills.skills.size(); i++) {
                    int index = i;
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.equals("registered successfully")) {

                                yes = true;
                            } 

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Registration.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Phno", Phone);
                            params.put("skill",select_skills.skills.get(index));
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
                    requestQueue.add(request);
                }
                if(yes)
                {
                    yes = false;
                    finish();
                }
                else
                {
                    Toast.makeText(Registration.this, "CNIC or Phone No: already exist", Toast.LENGTH_SHORT).show();
                }


            }

}

