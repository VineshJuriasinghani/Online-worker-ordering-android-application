package com.technical.myapplication.ClientsViewStuff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.location.LocationRequest;
import com.technical.myapplication.MainActivity;
import com.technical.myapplication.R;

public class Client_view extends AppCompatActivity {
    private LocationRequest locationRequest;
    int UID;
    static TextView message;
    MeowBottomNavigation bottomNavigation;
    Fragment fragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_view);

        bottomNavigation = findViewById(R.id.bottom);
        message= findViewById(R.id.message1);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.nearby));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_accepted));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.process));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.completedtask));
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 1:
                        Intent intent = getIntent();
                        UID = intent.getIntExtra("UID", 1);
                        fragment = new Nearby();
                        Bundle bundle = new Bundle();
                        bundle.putDouble("Lat2", intent.getDoubleExtra("Lat", 1));
                        bundle.putDouble("Lng2", intent.getDoubleExtra("Lng", 1));
                        bundle.putInt("UID", UID);
                        message.setVisibility(View.VISIBLE);
                        message.setText("select a category");
                        fragment.setArguments(bundle);
                        break;
                    case 2:
                        message.setVisibility(View.INVISIBLE);
                        fragment = new Aceepted_req();

                        break;
                    case 3:
                        message.setVisibility(View.INVISIBLE);
                        fragment = new process();

                        break;
                    case 4:

                            message.setVisibility(View.INVISIBLE);
                        fragment = new work_completed();
                        break;
                    default:
                        break;
                }
                loadFragment(fragment);
            }
        });
        bottomNavigation.show(1,true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });
    }

    private void loadFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame,fragment)
                .commit();
    }

}
