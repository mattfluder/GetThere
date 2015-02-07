package com.capstone.transit.trans_it;

import android.content.res.AssetManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.ArrayList;

public class MapActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

    public ArrayList<String> createLatArrayList(){
        // LatV = lat values
        // Used array lists due to dyanmic size setting

        AssetManager mngr;
        String line = null;
        ArrayList<String> LatV = new ArrayList<String>();
        int count=0;
        boolean skillcheck = false;

        try{
            mngr = getAssets();
            InputStream is = mngr.open("stops.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while((line=br.readLine()) != null){ // Read until last line in .txt file
                if(count>0){
                    line = line.toUpperCase();
                    String[] ArrayValues = line.split(","); // Seperate line by commas into a list
                    LatV.add(ArrayValues[0]); // Access first index (latitude) and add to ArrayList
                }
                count++;
            }

            br.close();
        }
        catch (IOException e1){

        }

        return LatV;
    }

    public ArrayList<String> createLonArrayList(){
        // LonV = lon values
        // Used array lists due to dyanmic size setting

        AssetManager mngr;
        String line = null;
        ArrayList<String> LonV = new ArrayList<String>();
        int count=0;
        boolean skillcheck = false;

        try{
            mngr = getAssets();
            InputStream is = mngr.open("stops.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while((line=br.readLine()) != null){  // Read until last line in .txt file
                if(count>0){
                    line = line.toUpperCase();
                    String[] ArrayValues = line.split(","); // Seperate line by commas into a list
                    LonV.add(ArrayValues[3]);  // Access first index (latitude) and add to ArrayList
                }
                count++;
            }

            br.close();
        }
        catch (IOException e1){

        }

        return LonV;

    }

    public ArrayList<String> createStopNameArrayList(){
        // LonV = lon values
        // Used array lists due to dyanmic size setting

        AssetManager mngr;
        String line = null;
        ArrayList<String> NameV = new ArrayList<String>();
        int count=0;
        boolean skillcheck = false;

        try{
            mngr = getAssets();
            InputStream is = mngr.open("stops.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while((line=br.readLine()) != null){  // Read until last line in .txt file
                if(count>0){
                    line = line.toUpperCase();
                    String[] ArrayValues = line.split(","); // Seperate line by commas into a list
                    NameV.add(ArrayValues[8]);  // Access first index (latitude) and add to ArrayList
                }
                count++;
            }

            br.close();
        }
        catch (IOException e1){

        }

        return NameV;

    }

    private void setUpMap() {

        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(43.2500,-79.919501), 13.0f) );

        // Creating ArrayLists of Strings

        ArrayList<String> LatValues = createLatArrayList();
        ArrayList<String> LonValues = createLonArrayList();
        ArrayList<String> StopNames = createStopNameArrayList();
        String StopName;
        int size = LatValues.size();
        double LatV,LonV;
        int i = 0;

        while(i<size){

            // Looping through both lists and setting values from strings to doubles

            LatV = Double.parseDouble(LatValues.get(i));
            LonV = Double.parseDouble(LonValues.get(i));
            StopName = StopNames.get(i);

            // Set a marker for each iteration

            mMap.addMarker(new MarkerOptions().position(new LatLng(LatV, LonV)).title(StopName));
            i++;
        }
    }
}
