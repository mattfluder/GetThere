package com.capstone.transit.trans_it;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Thomas on 2/8/2015.
 */
public class infoWindowClickActivity extends Activity{

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        this.setTitle(retrieveStopName());
        setContentView(R.layout.marker_info);
        setRouteNames();
    }

    public String retrieveStopName(){

        Intent intent = getIntent();
        String stopName = intent.getStringExtra("STOP_NAME");

        return stopName;

    }

    public String retrieveStopID(){

        Intent intent = getIntent();
        String stopID = intent.getStringExtra("STOP_ID");

        return stopID;
    }

    public ArrayList<String> retrieveTripID(){

        String StopID = retrieveStopID();

        AssetManager mngr;
        String line = null;
        ArrayList<String> TripIDs = new ArrayList<String>(); //Create arrayList to store the TripIDs
        int count=0;
        boolean skillcheck = false;

        try{
            mngr = getAssets();
            InputStream is = mngr.open("stop_times.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while((line=br.readLine()) != null){  // Read until last line in .txt file
                if(count>0){
                    line = line.toUpperCase();
                    String[] ArrayValues = line.split(","); // Seperate line by commas into a list

                    if(Arrays.asList(ArrayValues).contains(StopID)){
                    //Check the current line from stop_times.txt to see if it contains the correct StopID
                    //If the stopID is present, the first field in the array (tripID) is added to the ArrayList
                    // Note that there are many many trips of the same route which explain the high number of entries
                        TripIDs.add(ArrayValues[0]);
                    }
                }

                count++;
            }

            br.close();
        }
        catch (IOException e1){

        }

        return TripIDs;
    }

    public ArrayList<String> retrieveRouteID(){

        ArrayList<String> TripIDs = retrieveTripID(); //Populating an ArrayList with the TripID's

        AssetManager mngr;
        String line = null;
        ArrayList<String> RouteIDs = new ArrayList<String>(); //Creating an ArrayList t store the RouteIDs
        int count=0;
        boolean skillcheck = false;

        try{
            mngr = getAssets();
            InputStream is = mngr.open("trips.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while((line=br.readLine()) != null){  // Read until last line in .txt file
                if(count>0){
                    line = line.toUpperCase();
                    String[] ArrayValues = line.split(","); // Seperate line by commas into a list

                    for(int i=0; i< TripIDs.size(); i++){
                        if(Arrays.asList(ArrayValues).contains(TripIDs.get(i))){
                            // Check the line being examined for the TripID
                            // If the TripID is present, the value at index 0 (RouteID) is added to the RouteIDs ArrayList
                            if(!RouteIDs.contains(ArrayValues[0])){
                                //Do a check to see if the route id is already present, this shall take
                                //care of the multiple trips per route
                                RouteIDs.add(ArrayValues[0]);
                            }
                        }
                    }
                }

                count++;
            }

            br.close();
        }
        catch (IOException e1){

        }

        return RouteIDs;
    }

    protected void setRouteNames(){

        TextView t = (TextView)findViewById(R.id.textView1); // Linking variable t to the textView in the layout

        ArrayList<String> RouteIDs = retrieveRouteID();

        AssetManager mngr;
        ArrayList<String> RouteNames = new ArrayList<String>();
        String line = null;
        int count=0;
        boolean skillcheck = false;

        try{
            mngr = getAssets();
            InputStream is = mngr.open("routes.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while((line=br.readLine()) != null){  // Read until last line in .txt file
                if(count>0){
                    line = line.toUpperCase();
                    String[] ArrayValues = line.split(","); // Seperate line by commas into a list

                    for(int i=0; i< RouteIDs.size(); i++){ // Looping through all RouteIDs
                        if(Arrays.asList(ArrayValues).contains(RouteIDs.get(i))){ // Check through routes.txt for the RouteID
                            if(!(RouteNames.contains(ArrayValues[8]))) {
                            // Checks to see if the RouteName as been added already to the
                            // ArrayList, not sure why double entries can appear without this check
                            // Perhaps due to there being two directions in most cases?
                                RouteNames.add(ArrayValues[8]);
                                t.append(ArrayValues[8] + " " + ArrayValues[0] + "\n");
                                // Append the route number and name to the textView
                            }
                        }
                    }
                }
                count++;
            }

            br.close();
        }
        catch (IOException e1){

        }

    }

}
