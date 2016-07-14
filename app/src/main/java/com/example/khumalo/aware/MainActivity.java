package com.example.khumalo.aware;

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
import android.location.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.DoubleSummaryStatistics;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap m_map;
    Button moveNextLocation;
    LatLng currentCameraPosition;
    CameraPosition current;
    MarkerOptions yourRide;
    Marker now;

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yourRide= new MarkerOptions()
                .position(new LatLng(-33.983964,18.4648159))
                .title("Your Ride")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ride));



        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buildGoodleApiClient();
    }

    private void buildGoodleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create()
                .setInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Log", "GoogleApiClient connection has been suspend");
    }



    private void updatePosition(LatLng currentCameraPosition) {
        current = new CameraPosition.Builder()
                .target(currentCameraPosition)
                .zoom(14)
                .build();
        m_map.clear();
        yourRide= new MarkerOptions()
                .position(currentCameraPosition)
                .title("Your Ride")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ride));
        m_map.addMarker(yourRide);
        m_map.animateCamera(CameraUpdateFactory.newCameraPosition(current),5000,null);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
       m_map = googleMap;
        m_map.addMarker(yourRide);
   }



    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        currentCameraPosition = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        updatePosition(currentCameraPosition);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("Log", "GoogleApiClient connection has failed");
    }
}
