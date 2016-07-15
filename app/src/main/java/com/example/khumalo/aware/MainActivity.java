package com.example.khumalo.aware;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap m_map;
    Button moveNextLocation;
    LatLng currentCameraPosition;
    CameraPosition current;
    Marker marker;

    ArrayList<Marker> markers;

    private static final LatLng Kenilworth = new LatLng(-34.0047145,18.4689943);
    private static final LatLng Harfield = new LatLng(-33.9861115,18.4690193);
    private static final LatLng Claremont= new LatLng(-33.9814405,18.4648243);
    private static final LatLng Newlands = new LatLng(-33.9728675,18.4651473);
    private static final LatLng Rosebank = new LatLng(-33.9546765,18.4709463);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moveNextLocation = (Button)findViewById(R.id.testing_move_next_location);

        Log.d("Tag", "In on Create Method");
        markers = new ArrayList<Marker>();
        moveNextLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCameraPosition = new LatLng(-33.96912114785187,18.46262365579605);
                updatePosition(currentCameraPosition);
            }
        });


        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void updatePosition(LatLng currentCameraPosition) {
        current = new CameraPosition.Builder()
                .target(currentCameraPosition)
                .zoom(13)
                .bearing(355)
                .build();
        /* marker = m_map.addMarker(new MarkerOptions()
                 .position(currentCameraPosition)
                 .title("Some Place"));*/
         m_map.animateCamera(CameraUpdateFactory.newCameraPosition(current),5000,null);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
       m_map = googleMap;
        m_map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                return false;
            }
        });

        m_map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Tag", "Bearing is " + String.valueOf(cameraPosition.bearing));
                Log.d("Tag", "The Zoom is " + String.valueOf(cameraPosition.zoom));
                Log.d("Tad", "Latitude is " + String.valueOf(cameraPosition.target.latitude) + "Longitude is "
                        + String.valueOf(cameraPosition.target.longitude));

            }
        });

        Log.d("Tag", "Map is Ready");

        Marker kenil = m_map.addMarker(new MarkerOptions().title("Kenilworth").position(Kenilworth));
        markers.add(kenil);
        Marker Har = m_map.addMarker(new MarkerOptions().title("Harfield").position(Harfield));
        markers.add(Har);
        Marker Clar = m_map.addMarker(new MarkerOptions().title("Calaremont").position(Claremont));
        markers.add(Clar);
        Marker New = m_map.addMarker(new MarkerOptions().title("NewLands").position(Newlands));
        markers.add(New);
        Marker Rose = m_map.addMarker(new MarkerOptions().title("RoseBank").position(Rosebank));
        markers.add(Rose);
   }
}
