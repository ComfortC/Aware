package com.example.khumalo.aware;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.DoubleSummaryStatistics;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap m_map;
    Button moveNextLocation;
    LatLng currentCameraPosition;
    CameraPosition current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moveNextLocation = (Button)findViewById(R.id.testing_move_next_location);

        moveNextLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCameraPosition = new LatLng(-33.9659602,18.4688974);
                updatePosition(currentCameraPosition);
            }
        });


        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void updatePosition(LatLng currentCameraPosition) {
        current = new CameraPosition.Builder()
                .target(currentCameraPosition)
                .zoom(14)
                .build();

        m_map.animateCamera(CameraUpdateFactory.newCameraPosition(current),5000,null);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
       m_map = googleMap;

   }
}
