package com.example.khumalo.aware;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap m_map;
    Button moveNextLocation;
    LatLng currentCameraPosition;
    CameraPosition current;
    Marker marker;
    private Marker selectedMarker;
    private final Handler mHandler = new Handler();
    ArrayList<Marker> markers;

    private static final LatLng Century_City = new LatLng(-33.8931255,18.5092153);
    private static final LatLng Green_Point = new LatLng(-33.9047245,18.4076673);
    private static final LatLng Clare_Mont = new LatLng(-33.9815935,18.4648163);

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
              final Marker trackingMarker = m_map.addMarker(new MarkerOptions().position(Century_City));
              final MarkerAnimation animator = new MarkerAnimation();
              animator.animateMarkerToGB(trackingMarker, Green_Point, new LatLngInterpolator.Spherical());
              Handler handler = new Handler();
              handler.postDelayed(new Runnable() {
                  public void run() {
                      trackingMarker.setPosition(Green_Point);
                      animator.animateMarkerToGB(trackingMarker, Century_City, new LatLngInterpolator.Spherical());
                  }
              }, 10500);



            }
        });


        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void updatePosition(LatLng currentCameraPosition) {
        current = new CameraPosition.Builder()
                .target(currentCameraPosition)
                .zoom(12)
                .bearing(43)
                .tilt(45)
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

      /*  m_map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Tag", "Bearing is " + String.valueOf(cameraPosition.bearing));
                Log.d("Tag", "The Zoom is " + String.valueOf(cameraPosition.zoom));
                Log.d("Tad", "Latitude is " + String.valueOf(cameraPosition.target.latitude) + "Longitude is "
                        + String.valueOf(cameraPosition.target.longitude));

            }
        });*/

        Log.d("Tag", "Map is Ready");

        Marker Century = m_map.addMarker(new MarkerOptions().title("Century City").position(Century_City));
        markers.add(Century);
       /* Marker Har = m_map.addMarker(new MarkerOptions().title("Harfield").position(Harfield));
        markers.add(Har);*/
        Marker Stadium = m_map.addMarker(new MarkerOptions().title("Calaremont").position(Green_Point));
        markers.add(Stadium);

      /*  Marker Clare= m_map.addMarker(new MarkerOptions().title("Calaremont").position(Clare_Mont));
        markers.add(Clare);*/
        m_map.addPolyline(new PolylineOptions().add(Century_City).add(Green_Point).color(Color.LTGRAY));
        updatePosition(new LatLng(-33.90133237992072, 18.455616384744644));
   }










}



