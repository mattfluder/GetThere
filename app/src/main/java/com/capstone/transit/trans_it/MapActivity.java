package com.capstone.transit.trans_it;

import android.content.Intent;
import android.content.res.AssetManager;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setUpMapIfNeeded();
        testCameraChange();
        infoWindowClick();

        //Map Activity Done

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
            }
        }
    }

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
            //System.out.print(LatV.get(1));
            System.out.print("Got here");
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
            System.out.print(LonV.get(1));

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

    public ArrayList<String> createStopIDArrayList(){
        AssetManager mngr;
        String line = null;
        ArrayList<String> StopID = new ArrayList<String>();
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
                    StopID.add(ArrayValues[4]);  // Access first index (latitude) and add to ArrayList
                }
                count++;
            }
            br.close();
        }
        catch (IOException e1){
        }
        return StopID;

    }

    public ArrayList<String> createStopCodeArrayList(){
        AssetManager mngr;
        String line = null;
        ArrayList<String> StopCode = new ArrayList<String>();
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
                    StopCode.add(ArrayValues[2]);  // Access first index (latitude) and add to ArrayList
                }
                count++;
            }
            br.close();
        }
        catch (IOException e1){
        }
        return StopCode;

    }

    public List<Marker> createMarkerList(){

        // Storing the latitude / longitude / names by calling their respective functions

        ArrayList<String> LatValues = createLatArrayList();
        ArrayList<String> LonValues = createLonArrayList();
        ArrayList<String> StopNames = createStopNameArrayList();
        ArrayList<String> StopIDs = createStopIDArrayList();
        ArrayList<String> StopCodes = createStopCodeArrayList();


        // Creating a marker list to store the markers

        List<Marker> markers = new ArrayList<Marker>();

        String StopName,StopID,StopCode;
        final int size = LatValues.size();
        double LatV,LonV;
        int i = 0;

        while(i<size){

            // Looping through both lists and setting values from strings to doubles

            LatV = Double.parseDouble(LatValues.get(i));
            LonV = Double.parseDouble(LonValues.get(i));
            StopName = StopNames.get(i);
            StopID = StopIDs.get(i);
            StopCode = StopCodes.get(i);

            // Set a marker for each iteration
            // Add each marker to the list "Markers"

            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(LatV, LonV)).title(StopName));
            marker.setSnippet(StopCode);
            marker.setVisible(false);
            markers.add(marker);
            i++;
        }

        return markers;
    }

    private void setUpMap() {

        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(43.2500,-79.919501), 13.0f) );

    }

    private void testCameraChange(){

        // Rather than completely deleting the markers depending on zoom level, we just change the visibility
        // property to true or false depending on the situation

        final List<Marker> markers = createMarkerList(); // Returns the markers list as well as sets the markers on the map
        final int size = markers.size();  // retrieves length to use for the for loop

        mMap.setOnCameraChangeListener( new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange( CameraPosition cameraPosition ) {
                if(mMap.getCameraPosition().zoom >= 15){   // If zoomed in sufficiently, sets the visibility to true
                    for(int a=0;a<size;a++){
                        markers.get(a).setVisible(true);
                    }
                }
                else{
                    for(int a=0;a<size;a++){
                        markers.get(a).setVisible(false);  // If not zoomed in sufficiently, sets the visibility to false
                    }
                }
            }
        });
    }

    private void infoWindowClick(){

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                Intent intent = new Intent(getBaseContext(), StopListActivity.class);
                intent.putExtra("STOP_CODE", marker.getSnippet()); // Passing stop ID to next activity
                startActivity(intent);
            }
        });

    }


}
