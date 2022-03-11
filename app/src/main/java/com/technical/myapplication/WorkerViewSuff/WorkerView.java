package com.technical.myapplication.WorkerViewSuff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import com.google.android.gms.location.LocationRequest;

import com.technical.myapplication.MainActivity;
import com.technical.myapplication.R;

public class WorkerView extends AppCompatActivity {
    private LocationRequest locationRequest;
    MeowBottomNavigation bottomNavigation;
    Fragment fragment = null;
    int W_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_view);
        bottomNavigation = findViewById(R.id.bottom);
        bottomNavigation.startLayoutAnimation();
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.worker_req));
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                    switch (item.getId()) {
                        case 1:

                            Intent intent = getIntent();
                            W_ID = intent.getIntExtra("W_ID", 1);
                            fragment = new clientsrequests();
                            Bundle bundle = new Bundle();
                            bundle.putDouble("Lat2", intent.getDoubleExtra("Lat",1));
                            bundle.putDouble("Lng2", intent.getDoubleExtra("Lng",1));
                            bundle.putInt("W_ID", W_ID);
                            fragment.setArguments(bundle);
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

    }

    private void loadFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame2,fragment)
                .commit();
    }

}


