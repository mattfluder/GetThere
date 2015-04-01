package com.capstone.transit.trans_it;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.transit.realtime.GtfsRealtime.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class StopListActivity extends ActionBarActivity {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<StopTimes>> listDataChild;
    private List<String> routesServicing;
    private List<StopTimes> listRouteTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_list);

        final EditText stopCodeEdit = (EditText)findViewById(R.id.editText);
        final Button GoButton = (Button) findViewById(R.id.goButton);
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);

        final Intent fetchTimesIntent = new Intent(getApplicationContext(), FetchTimesService.class);
        fetchTimesIntent.putExtra("receiver",new times2Receiver(new Handler()));

        String intentStopCode = retrieveStopCode();

        stopCodeEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    GoButton.performClick();
                    return true;
                }
                return false;
            }
        });

        GoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(stopCodeEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                startService(fetchTimesIntent);
            }
        });

        if (intentStopCode != null){
            stopCodeEdit.setText(intentStopCode);
            GoButton.performClick();
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

    private String retrieveStopCode() {

        Intent intent = getIntent();

        return intent.getStringExtra("STOP_CODE");
    }

    private void updateTimesList(){

        final EditText stopCodeEdit = (EditText)findViewById(R.id.editText);
        StopTimes currentStopTime;

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<StopTimes>>();

        listAdapter = new ExpandableListAdapter(this,listDataHeader,listDataChild);
        expListView.setAdapter(listAdapter);

        int count =  listAdapter.getGroupCount();
        for (int i = 0; i <count ; i++)
            expListView.collapseGroup(i);
        String stopID; //not what is posted on bus stop sign
        String routeID;
        stopID = translateStopId(stopCodeEdit.getText().toString()); //stop code is what is posted on bus stop sign
        FeedMessage realData = null;
        FileInputStream fileIn = null;
        routesServicing = new ArrayList<String>();
        listDataHeader.clear();
        listDataChild.clear();
        boolean stopFound = false;

        int indexOfRouteId;

        AssetManager mngr;
        String line, longAndShortName;

        try {
            mngr = getAssets();
            InputStream is = mngr.open("routesAtStop.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

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
                    stopFound = true;
                    longAndShortName = ArrayValues[2] + " " + ArrayValues [3];
                    listDataHeader.add(longAndShortName);
                    routesServicing.add(ArrayValues[1]);
                }
            }
            br.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if (!stopFound) realData = null;

        if (realData != null) {
            for (int i = 0; i < listDataHeader.size(); i++) {
                listRouteTimes = new ArrayList<>();
                for (FeedEntity entity : realData.getEntityList()) {
                    if (!entity.hasTripUpdate()) continue;
                    TripUpdate trip = entity.getTripUpdate();
                    if (!trip.hasTrip()) continue;
                    routeID = trip.getTrip().getRouteId();
                    indexOfRouteId = routesServicing.indexOf(routeID);
                    if (indexOfRouteId != i) continue;
                    System.out.println(routeID);
                    for (TripUpdate.StopTimeUpdate stopTime : trip.getStopTimeUpdateList()) {
                        if (stopTime.getStopId().equals(stopID)) {
                            TripUpdate.StopTimeEvent stopEventArrival = stopTime.getArrival();
                            long unixSeconds = stopEventArrival.getTime();
                            Date date = new Date(unixSeconds * 1000);//the time the pb files give us needs to be scaled up buy 1000
                            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                            String formattedDate = sdf.format(date);
                            currentStopTime = new StopTimes(formattedDate, routeID, trip.getTrip().getTripId(),true, stopTime.getArrival().getDelay(), trip.getVehicle().getLabel());
                            listRouteTimes.add(currentStopTime);
                            System.out.println(String.valueOf(stopEventArrival.getTime()));
                        }
                    }
                }
                System.out.println("Adding to List");
                if (listRouteTimes.isEmpty())listRouteTimes.add(null);
                listDataChild.put(listDataHeader.get(i),listRouteTimes);
                System.out.println("Next Loop");
            }
        }
        listAdapter.notifyDataSetChanged();
    }
    class times2Receiver extends ResultReceiver {
        public times2Receiver(Handler handler){
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            System.out.println("Result Received");
            updateTimesList();
        }
    }
}
