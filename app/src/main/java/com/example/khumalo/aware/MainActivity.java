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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap m_map;
    Button moveNextLocation;

    LatLng currentCameraPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moveNextLocation = (Button)findViewById(R.id.testing_move_next_location);
        currentCameraPosition = new LatLng(-33.9891809,18.4832172);


        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
       m_map = googleMap;
        CameraPosition current = new CameraPosition.Builder()
                .target(currentCameraPosition)
                .zoom(12)
                .build();

        m_map.animateCamera(CameraUpdateFactory.newCameraPosition(current),5000,null);

    }
}
