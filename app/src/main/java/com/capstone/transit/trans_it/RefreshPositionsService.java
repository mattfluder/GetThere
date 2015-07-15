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

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.google.transit.realtime.GtfsRealtime.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class RefreshPositionsService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public RefreshPositionsService() {
        super("RefreshPositionsService");
    }
    private String routeID;
    private ArrayList<Integer> latitudeList;
    private ArrayList<Integer> longitudeList;
    @Override
    protected void onHandleIntent(Intent intent) {
        int error = FetchPositionsFile();
        ResultReceiver receiver = intent.getParcelableExtra("EXTRA_RECEIVER");
        routeID = intent.getStringExtra("EXTRA_ROUTE_ID");
        Bundle resultData = new Bundle();
        if (error == 0){
            resultData.putBoolean("Errors", false);
            ParseLocations();
        }
        else {
            resultData.putBoolean("Errors", true);
        }
        resultData.putIntegerArrayList("LatitudeList", latitudeList);
        resultData.putIntegerArrayList("LongitudeList", longitudeList);
        receiver.send(error,resultData);
        System.out.println("Sent Results");
    }

    /**
     Gets the real time GTFS_VehiclePositions file from the City of Hamilton OpenData server
     returns:
     0 if successful
     1 if URL error
     2 if urlConnection Error
     3 if BufferedInput Stream Error
     4 if Writing to File error
     5 if Close Stream Error
     */
    private int FetchPositionsFile(){
        InputStream is = null;
        HttpURLConnection urlConnection = null;
        FileOutputStream fileStream = null;
        URL url = null;
        try {
            url = new URL("http://opendata.hamilton.ca/GTFS-RT/GTFS_VehiclePositions.pb");
        }
        catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            //System.out.println("Opened Connection");
        }
        catch(Exception e) {
            /*toast = Toast.makeText(getApplicationContext(), "Failed to Open URL", Toast.LENGTH_SHORT);
            toast.show();*/
            e.printStackTrace();
            return 2;
        }
        try {
            is = new BufferedInputStream(urlConnection.getInputStream());
            //System.out.println("Created input stream");
        }
        catch(Exception e) {
            /*toast = Toast.makeText(getApplicationContext(), "Failed to Create Input Stream", Toast.LENGTH_SHORT);
            toast.show();*/
            e.printStackTrace();
            return 3;
        }
        try {
            File GTFR_TripUpdates = new File (getFilesDir(),"GTFS_VehiclePositions.pb");
            fileStream = new FileOutputStream(GTFR_TripUpdates);
            int read;
            byte[] data = new byte[1024];
            while ((read = is.read(data)) != -1)
                fileStream.write(data, 0, read);
            //System.out.println("parsed data");
        }
        catch (Exception e) {
            /*toast = Toast.makeText(getApplicationContext(), "Failed to Write File", Toast.LENGTH_SHORT);
            toast.show();*/
            e.printStackTrace();
            return 4;
        }
        try {
            if (fileStream != null)
                fileStream.close();
            if (is != null)
                is.close();
        }
        catch (Exception e) {
            //System.out.print("Failed to Close Streams");
            e.printStackTrace();
            return 5;
        }
        return 0;
    }

    private void ParseLocations(){
        FileInputStream fileIn;
        FeedMessage realData = null;
        float latitude, longitude;
        String vehicleLabel;
        try {
            fileIn = openFileInput("GTFS_VehiclePositions.pb");
            realData = FeedMessage.parseFrom(fileIn); //get data from pb file
        }
        catch (Exception e){
            e.printStackTrace();
        }
        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();
        int tempLat, tempLong;
        if (realData != null){
            //System.out.println("Data not null");
            for (FeedEntity entity : realData.getEntityList()){
                if (!entity.hasVehicle()) continue;
                //System.out.println("Has vehicle");
                VehiclePosition vehiclePosition = entity.getVehicle();
                if (!vehiclePosition.getTrip().getRouteId().equals(routeID)) continue;
                //System.out.println("Found a trip");
                latitude = vehiclePosition.getPosition().getLatitude();
                longitude = vehiclePosition.getPosition().getLongitude();
                vehicleLabel = vehiclePosition.getVehicle().getLabel();
                tempLat = (int) (latitude*10000);
                tempLong = (int) (longitude *100000);
                //System.out.println(tempLat+ ", " + tempLong+ ", " + vehicleLabel);
                latitudeList.add((int)(latitude*100000));
                longitudeList.add((int) (longitude*100000));
            }
        }
    }

}
