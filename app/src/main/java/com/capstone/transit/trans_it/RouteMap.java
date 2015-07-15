/**
 *
 This file is part of the "Get There!" application for android developed
 for the SFWR ENG 4G06 Capstone course in the 2014/2015 Fall/Winter
 terms at McMaster University.


 Copyright (C) 2015 M. Fluder, T. Miele, N. Mio, M. Ngo, and J. Rabaya

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package com.capstone.transit.trans_it;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class RouteMap extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Intent positionsServiceIntent;
    private String routeID, routeName;
    private ArrayList<Integer> latitudeList, longitudeList;
    private ArrayList<Marker> busMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);
        setUpMapIfNeeded();
        routeID = getIntent().getStringExtra("EXTRA_ROUTE_ID");
        routeName = getIntent().getStringExtra("EXTRA_NAME");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        positionsServiceIntent = new Intent(getApplicationContext(),RefreshPositionsService.class);
        positionsServiceIntent.putExtra("EXTRA_ROUTE_ID", routeID);
        positionsServiceIntent.putExtra("EXTRA_RECEIVER", new PositionsReceiver(new Handler()));
        final PendingIntent pendingIntent = PendingIntent.getService(this, 0, positionsServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long trigger = System.currentTimeMillis();
        int intervalMillis = 1000*60;
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC, trigger, intervalMillis,pendingIntent);
    }

    @Override
    protected void onPause(){
        super.onPause();
        stopService(positionsServiceIntent);
        positionsServiceIntent = new Intent(getApplicationContext(),RefreshPositionsService.class);
        final PendingIntent pendingIntent = PendingIntent.getService(this, 0, positionsServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
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
        mMap.setMyLocationEnabled(true);
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.2500, -79.919501), 13.0f));
    }

    private void CreateMarkers(){
        mMap.clear();
        float tempLat, tempLongitude;
        busMarkers = new ArrayList<>();
        busMarkers.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i =0; i < longitudeList.size(); i++){
            tempLat = (latitudeList.get(i)/(float)100000);
            tempLongitude = (longitudeList.get(i)/(float)100000);
            System.out.println("Lat:" + tempLat + "Long:" + tempLongitude);
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(tempLat,tempLongitude)).title(routeName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            builder.include(new LatLng(tempLat,tempLongitude));
            busMarkers.add(marker);
            marker.setVisible(true);
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,70);
        mMap.animateCamera(cu);
    }

    class PositionsReceiver extends ResultReceiver {
        public PositionsReceiver(Handler handler){
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            boolean errors;
            errors= resultData.getBoolean("Errors");
            if (errors){
                AlertDialog.Builder builder = new AlertDialog.Builder(RouteMap.this);
                builder.setTitle("Error getting position data.")
                        .setMessage("Check data connection?")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
            else {
                latitudeList = resultData.getIntegerArrayList("LatitudeList");
                longitudeList = resultData.getIntegerArrayList("LongitudeList");
                CreateMarkers();
                //System.out.println("Got Data");
            }
        }
    }
}
