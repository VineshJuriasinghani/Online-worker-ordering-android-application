package com.technical.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class select_skills extends AppCompatActivity {

    CheckBox plumber, mechanic, electrician,carpenter;
    Button next;
    int selected = 0;
    public static ArrayList<String> skills = new ArrayList<>();

    @Override
    public void onBackPressed() {
       skills.clear();
       super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_skills);

        plumber = findViewById(R.id.plumber);
        mechanic = findViewById(R.id.mechanic);
        electrician = findViewById(R.id.electrician);
        carpenter = findViewById(R.id.carpenter);
        next = findViewById(R.id.next);

        plumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(plumber.isChecked())
                {
                    selected += 1;
                    if(!check_if_exist("plumber"))
                    {
                        skills.add("plumber");
                    }
                }
                else
                {
                    selected -= 1;
                    if(check_if_exist("plumber"))
                        skills.remove(new String("plumber"));
                }
            }
        });
        mechanic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mechanic.isChecked())
                {
                    selected += 1;
                    if(!check_if_exist("mechanic"))
                        skills.add("mechanic");
                }
                else
                {
                    selected -= 1;
                    if(check_if_exist("mechanic"))
                        skills.remove(new String("mechanic"));
                }

            }
        });
        electrician.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(electrician.isChecked())
                {
                    selected += 1;
                    if(!check_if_exist("electrician"))
                        skills.add("electrician");
                }
                else
                {
                    selected -= 1;
                    if(check_if_exist("electrician"))
                        skills.remove(new String("electrician"));
                }
            }
        });
        carpenter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(carpenter.isChecked())
                {
                    selected += 1;
                    if(!check_if_exist("carpenter"))
                        skills.add("carpenter");
                }
                else
                {
                    selected -= 1;
                    if(check_if_exist("carpenter"))
                        skills.remove(new String("carpenter"));
                }

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(selected > 0) {
                                            Registration.Signup.setText("signup");
                                            finish();

                                        }
                                        else
                                            Toast.makeText(select_skills.this, "Please select one of the fields", Toast.LENGTH_SHORT).show();
                                    }
                                }
        );




    }
    boolean check_if_exist(String temp)
    {
      for(int i=0 ; i < skills.size() ; i++)
      {
          if(skills.get(i) == temp)
          {
              return  true;
          }
      }
      return false;
    }
}