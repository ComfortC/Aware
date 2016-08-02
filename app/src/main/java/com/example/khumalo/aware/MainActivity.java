package com.example.khumalo.aware;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khumalo.aware.Utils.PermissionUtils;
import com.google.android.gms.location.LocationResult;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    static GoogleMap m_map;
    Button moveNextLocation;
    LatLng currentCameraPosition;
    static CameraPosition current;
    Marker marker;
    private static Marker InitialLocation;
    private final Handler mHandler = new Handler();
    ArrayList<Marker> markers;

    private static final LatLng Century_City = new LatLng(-33.8931255,18.5092153);
    private static final LatLng Green_Point = new LatLng(-33.9047245,18.4076673);
    private static final LatLng Clare_Mont = new LatLng(-33.957652,18.4590104);
    public static TextView latitudeTextView;
    public static TextView longitudeTextView;
    public static Marker TrackingMarker;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
   BackgroundLocationService mService;
    boolean mBound = false;
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;
    public static boolean FirstResultFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirstResultFlag = true;


        Log.d("Tag", "In on Create Method");
        markers = new ArrayList<Marker>();

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    private static void updatePosition(LatLng currentCameraPosition) {


        current = new CameraPosition.Builder()
                .target(currentCameraPosition)
                .zoom(18)
                .tilt(45)
                .build();

         m_map.animateCamera(CameraUpdateFactory.newCameraPosition(current), 2000, null);
        TrackingMarker = m_map.addMarker(new MarkerOptions().position(currentCameraPosition));
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





        }


    private Location convertLatLngToLocation(LatLng latLng) {
        Location location = new Location("someLoc");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }

    private float bearingBetweenLatLngs(LatLng beginLatLng,LatLng endLatLng) {
        Location beginLocation = convertLatLngToLocation(beginLatLng);
        Location endLocation = convertLatLngToLocation(endLatLng);
        return beginLocation.bearingTo(endLocation);
    }







    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            Intent intent = new Intent(this, BackgroundLocationService.class);
            startService(intent);

            Toast.makeText(this, "The Permision has Been Granted",Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this,"The Permision has Been Dinied",Toast.LENGTH_LONG).show();
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }else{
            Intent intent = new Intent(this, BackgroundLocationService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }

    }



   public static class LocationReceiver extends BroadcastReceiver {




        @Override
        public void onReceive(Context context, Intent intent) {

            LocationResult result = LocationResult.extractResult(intent);
            if(result!=null&& m_map!=null){
                LatLng currentPosition = new LatLng(result.getLastLocation().getLatitude(),result.getLastLocation().getLongitude());
                if(FirstResultFlag){
                    updatePosition(currentPosition);
                    FirstResultFlag=false;
                }


                MarkerAnimation animator = new MarkerAnimation();
                animator.animateMarkerToGB(TrackingMarker, currentPosition, new LatLngInterpolator.Linear(), m_map);

            }
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BackgroundLocationService.LocalBinder binder = (BackgroundLocationService.LocalBinder) service;
            mService = binder.getServerInstance();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


}



