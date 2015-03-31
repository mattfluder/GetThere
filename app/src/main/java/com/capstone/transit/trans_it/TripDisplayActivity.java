package com.capstone.transit.trans_it;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TripDisplayActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_display);
        setUpMapIfNeeded();

    }

    public String retrieveStartAddress() {

        Intent intent = getIntent();
        return intent.getStringExtra("START_ADDRESS");

    }

    public String retrieveEndAddress() {

        Intent intent = getIntent();
        return intent.getStringExtra("END_ADDRESS");
    }

    public double[] getFromLocation(String address){

        double[] Coordinates = new double[2];

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try
        {
            Address addressConvert = geoCoder.getFromLocationName(address , 1).get(0);
            Coordinates[0] = addressConvert.getLatitude();
            Coordinates[1] = addressConvert.getLongitude();

        }
        catch(Exception ee)
        {

        }

        return Coordinates;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                mMap.setMyLocationEnabled(true);

                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {

                        double[] StartCoordinates,EndCoordinates;

                        StartCoordinates = getFromLocation(retrieveStartAddress());
                        EndCoordinates = getFromLocation(retrieveEndAddress());

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(new LatLng(StartCoordinates[0], StartCoordinates[1]));
                        builder.include(new LatLng(EndCoordinates[0], EndCoordinates[1]));
                        LatLngBounds bounds = builder.build();

                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,200));
                    }
                });

            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        double[] StartCoordinates,EndCoordinates;

        StartCoordinates = getFromLocation(retrieveStartAddress());
        EndCoordinates = getFromLocation(retrieveEndAddress());

        mMap.addMarker(new MarkerOptions().position(new LatLng(StartCoordinates[0], StartCoordinates[1])).title("Start Address"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(EndCoordinates[0], EndCoordinates[1])).title("End Address"));
    }
}
