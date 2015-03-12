package com.capstone.transit.trans_it;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.google.protobuf.Internal;
import com.google.transit.realtime.GtfsRealtime.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class StopListActivity extends ActionBarActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> routesServicing;
    List<String> listRouteTimes;
    List<Integer> routesPerDataHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_list);

        Button GoButton = (Button) findViewById(R.id.goButton);
        final EditText stopCodeEdit = (EditText)findViewById(R.id.editText);

        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        listAdapter = new ExpandableListAdapter(this,listDataHeader,listDataChild);
        expListView.setAdapter(listAdapter);

        String StopCode = retrieveStopCode();

        GoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int count =  listAdapter.getGroupCount();
                for (int i = 0; i <count ; i++)
                    expListView.collapseGroup(i);
                String stopID; //not what is posted on bus stop sign
                String routeID;
                stopID = translateStopId(stopCodeEdit.getText().toString()); //stop code is what is posted on bus stop sign
                FeedMessage realData = null;
                FileInputStream fileIn = null;
                routesServicing = new ArrayList<String>();
                routesPerDataHeader = new ArrayList<Integer>();
                listDataHeader.clear();
                listDataChild.clear();
                boolean stopFound = false;

                int indexOfRouteId;
                int currentRouteCount;

                AssetManager mngr;
                String line, line2;

                try {
                    mngr = getAssets();
                    InputStream is = mngr.open("out.txt");
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    int routes;

                    try {
                        fileIn = openFileInput("GTFS_TripUpdates.pb");
                        realData = FeedMessage.parseFrom(fileIn); //get data from pb file
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    while ((line = br.readLine()) != null) {  // Read until last line in .txt file
                            String[] ArrayValues = line.split(","); // Seperate line by commas into a list

                            if (stopID.equals(ArrayValues[0])) { //if the stop id inputted equals the stop id of the current line being read
                                for (int i=1; i<ArrayValues.length;i++) {
                                    routes = 0;
                                    System.out.println(ArrayValues[i]);
                                    listDataHeader.add(ArrayValues[i]); //add the route at the current position to the list of headers
                                    InputStream is2 = mngr.open("routes.txt");
                                    InputStreamReader isr2 = new InputStreamReader(is2);
                                    BufferedReader br2 = new BufferedReader(isr2);
                                    while ((line2 = br2.readLine()) != null) {  // Read until last line in routes.txt file
                                        String [] ArrayValues2 = line2.split(","); // Seperate line by commas into a list
                                        if (ArrayValues2[8].equals(ArrayValues[i].substring(0,2))){ //if the
                                            routesServicing.add(ArrayValues2[5]);//
                                            routes ++;
                                        }
                                    }
                                    routesPerDataHeader.add(routes);
                                    stopFound = true;
                                }
                            }
                    }
                    br.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                if (!stopFound) realData = null;

                if (realData != null) {
                    currentRouteCount = 0;
                    for (int i = 0; i < listDataHeader.size(); i++) {
                        listRouteTimes = new ArrayList<String>();
                        currentRouteCount +=2;
                        for (FeedEntity entity : realData.getEntityList()) {
                            if (!entity.hasTripUpdate()) continue;
                            TripUpdate trip = entity.getTripUpdate();
                            if (!trip.hasTrip()) continue;
                            routeID = trip.getTrip().getRouteId();
                            indexOfRouteId = routesServicing.indexOf(routeID);
                            if ((indexOfRouteId > ((currentRouteCount+2*i)-1) || (indexOfRouteId < currentRouteCount-2))) continue;
                            System.out.println(routeID);
                            for (TripUpdate.StopTimeUpdate stopTime : trip.getStopTimeUpdateList()) {
                                if (stopTime.getStopId().equals(stopID)) {
                                    TripUpdate.StopTimeEvent stopEventArrival = stopTime.getArrival();
                                    long unixSeconds = stopEventArrival.getTime();
                                    Date date = new Date(unixSeconds * 1000);//the time the pb files give us needs to be scaled up buy 1000
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                                    String formattedDate = sdf.format(date);
                                    listRouteTimes.add(formattedDate);
                                    System.out.println(String.valueOf(stopEventArrival.getTime()));
                                }
                            }
                        }
                        System.out.println("Adding to List");
                        if (listRouteTimes.isEmpty())listRouteTimes.add("");
                        listDataChild.put(listDataHeader.get(i),listRouteTimes);
                        System.out.println("Next Loop");
                    }
                }
                listAdapter.notifyDataSetChanged();
            }
        });

        if (StopCode != null){

            stopCodeEdit.setText(StopCode);
            GoButton.callOnClick();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stop_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String translateStopId(String stopCode){
        String stopId = "";

        try {
            InputStreamReader is = new InputStreamReader(getAssets().open("stops.txt"));
            BufferedReader reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] RowData = line.split(",");
                if (stopCode.equals(RowData[2]))stopId = RowData[4];
            }
            is.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return stopId;
    }

    public String retrieveStopCode() {

        Intent intent = getIntent();

        return intent.getStringExtra("STOP_CODE");
    }

}
