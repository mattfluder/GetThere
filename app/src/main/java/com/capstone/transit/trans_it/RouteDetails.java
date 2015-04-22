package com.capstone.transit.trans_it;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.protobuf.Internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;



public class RouteDetails extends ActionBarActivity {
    private String[] days;
    private String currentDayString;
    private int currentDayInt;
    private String routeName;
    private String routeID;

    private TextView dayTextView;
    private ImageView previousDay;
    private ImageView nextDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        dayTextView = (TextView)findViewById(R.id.dayoftheweek);
        previousDay = (ImageView) findViewById(R.id.previousDay);
        nextDay = (ImageView) findViewById(R.id.nextDay);

        routeName = getIntent().getStringExtra("EXTRA_NAME");
        routeID = getIntent().getStringExtra("EXTRA_ROUTE_ID");
        final String TAG = "ME TALKING";


        //DAYS OF THE WEEK ====================================================================================
        Calendar c = Calendar.getInstance();
        currentDayInt = c.get(Calendar.DAY_OF_WEEK);
        //Translate into the schedule that we use.
        switch(currentDayInt) {
            case Calendar.SUNDAY:
                currentDayInt = 0;
                break;
            case Calendar.SATURDAY:
                currentDayInt = 1;
                break;
            default:
                currentDayInt = 2;
                break;
        }




        /*
                3_merged_965559 SUNDAY
                2_merged_965560 SATURDAY
                1_merged_965558 WEEKDAY
        */

        HashMap<Integer, String> numToServiceID = new HashMap<>();
        numToServiceID.put(0, "3_merged_965559");
        numToServiceID.put(1, "2_merged_965560");
        numToServiceID.put(2, "1_merged_965558");

        final HashMap<Integer, String> numToDay = new HashMap<>();
        numToDay.put(0, "Sunday/Holiday");
        numToDay.put(1, "Saturday");
        numToDay.put(2, "Weekday");

        currentDayString = numToDay.get(currentDayInt);
        dayTextView.setText(currentDayString);

        previousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDayInt -= 1;
                currentDayInt = (currentDayInt + 3) % 3;
                currentDayString = numToDay.get(currentDayInt);
                dayTextView.setText(currentDayString);
            }
        });

        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDayInt += 1;
                currentDayInt = currentDayInt % 3;
                currentDayString = numToDay.get(currentDayInt);
                dayTextView.setText(currentDayString);
            }
        });

        //Make table that can scroll while keeping column headers visible.=======================
        TableLayout headerTable = (TableLayout) findViewById(R.id.header_table);
        headerTable.setColumnStretchable(0, true);
        headerTable.setColumnShrinkable(1, true);
        headerTable.setColumnShrinkable(2, true);

        addRowToTable(headerTable, "Stop", "Time", "Time", getApplicationContext());

        TableLayout contentTable = (TableLayout) findViewById(R.id.content_table);
        contentTable.setColumnStretchable(0, true);
        contentTable.setColumnShrinkable(1, true);
        contentTable.setColumnShrinkable(2, true);

        //LOAD ROUTE INFORMATION.
        String triptxt = "trips.txt";
        String stopstxt = "stop_times.txt";
        String stopnamestxt = "stops.txt";

        HashMap<String, List>   serviceIdToTripID     = new HashMap<>();
        HashMap<String, List>   serviceIdToBlockID    = new HashMap<>();
        HashSet<String>         blockIDs              = new HashSet<>();
        HashMap<String, List>   blockIDToTripID       = new HashMap<>();
        HashMap<String, List>   tripIDToStop          = new HashMap<>();
        HashMap<String, String> stopNames           = new HashMap();


        for (String i : numToServiceID.values() ) {
            serviceIdToTripID.put(i, new ArrayList<String>());
        }
        for (String i : blockIDs) {
            blockIDToTripID.put(i, new ArrayList<String>());
        }

        for (String i : numToServiceID.values() ) {
            serviceIdToBlockID.put(i, new ArrayList<String>());
        }


        //OH MY GOD THIS IS TERRIBLE.
        try {
            AssetManager assetManager = getAssets();
            InputStream fis = assetManager.open(triptxt);
            InputStream fis2 = assetManager.open(stopstxt);
            InputStream fis3 = assetManager.open(stopnamestxt);
            BufferedReader tripReader = new BufferedReader(new InputStreamReader(fis));
            BufferedReader stopReader = new BufferedReader(new InputStreamReader(fis2));
            BufferedReader stopNamesReader = new BufferedReader(new InputStreamReader(fis3));
            String splitLine[];
            tripReader.readLine();
            stopReader.readLine();
            stopNamesReader.readLine();


            //TRIPS.TXT
            //route_id,service_id,trip_id,trip_headsign,trip_short_name,direction_id,block_id,shape_id,wheelchair_boarding,bikes_allowed

            //STOPS_TIMES.TXT
            //trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type,shape_dist_traveled

            //STOPS.TXT
            //stop_lat,wheelchair_boarding,stop_code,stop_lon,stop_id,stop_url,parent_station,stop_desc,stop_name,location_type,zone_id


            //Get all trips associated with a route.
            //Fills:
            //blockIDs
            //serviceIDToTripID
            //blockIDToTripID
            for (String line; (line = tripReader.readLine()) != null; ) {
                // process the line in TRIPS.
                splitLine = line.split(",");
                if (routeID.equals(splitLine[0])) {
                    //This now contains a set with lists of all the trips associated with the picked route, grouped by day
                    serviceIdToTripID.get(splitLine[1]).add(splitLine[2]);
                    blockIDs.add(splitLine[6]);
                    try {
                        blockIDToTripID.get(splitLine[6]).add(splitLine[2]);
                    } catch (NullPointerException e) {
                        blockIDToTripID.put(splitLine[6], new ArrayList());
                        blockIDToTripID.get(splitLine[6]).add(splitLine[2]);
                    }
                }
            }

            //Get all stops associated with a trip.
            List<String> tripIDs = serviceIdToTripID.get(numToServiceID.get(currentDayInt));
            for (String line; (line = stopReader.readLine()) != null; ) {
                // process the line in STOPS.
                splitLine = line.split(",");

                if (tripIDs.contains(splitLine[0])) {

                    //TripID -> StopID,Time,sequenceNumber
                    try {
                        tripIDToStop.get(splitLine[0]).add(new Stop(splitLine[3], splitLine[1], splitLine[4]));
                    } catch (NullPointerException e) {
                        tripIDToStop.put(splitLine[0], new ArrayList<Stop>());
                        tripIDToStop.get(splitLine[0]).add(new Stop(splitLine[3], splitLine[1], splitLine[4]));
                    }

                }
            }

            for (String line; (line = stopNamesReader.readLine()) != null; ) {
                splitLine = line.split(",");
                stopNames.put(splitLine[4], splitLine[8]);
            }

            fis.close();
            fis2.close();
            fis3.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        //SHOULD HAVE ALL THE INFO IN THE WORLD NOW.
        /*
        WE HAVE:

        numToServiceID
        serviceIdToTripID
        serviceIdToBlockID
        blockIDs
        blockIDToTripID
        tripIDToStop
        stopNames

         */

        //Print it out to test:
        //FOR TESTING PURPOSES ONLY. DELETE LATER.
        //print Route ID
        Log.d(TAG, "Route ID: " + routeID);
        //Print all trips associated with that route?
        int count = 1;
        Iterator<String> iterator = blockIDs.iterator();
        while (iterator.hasNext()) {
            Log.d(Integer.toString(count), iterator.next());
            count++;
        }
        //populate table

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_route_details, menu);
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

    //TABLE METHODS========================================================
    private static TableLayout addRowToTable(TableLayout table, String contentCol1, String contentCol2, String contentCol3, Context context) {
        TableRow row = new TableRow(context);

        TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
        // Wrap-up the content of the row
        rowParams.height = TableLayout.LayoutParams.WRAP_CONTENT;
        rowParams.width = TableLayout.LayoutParams.WRAP_CONTENT;

        // The simplified version of the table of the picture above will have two columns
        // FIRST COLUMN
        TableRow.LayoutParams col1Params = new TableRow.LayoutParams();
        // Wrap-up the content of the row
        col1Params.height = TableRow.LayoutParams.WRAP_CONTENT;
        col1Params.width = TableRow.LayoutParams.WRAP_CONTENT;
        // Set the gravity to center the gravity of the column
        col1Params.gravity = Gravity.LEFT;
        TextView col1 = new TextView(context);
        col1.setText(contentCol1);
        col1.setTextSize(20);
        row.addView(col1, col1Params);

        // SECOND COLUMN
        TableRow.LayoutParams col2Params = new TableRow.LayoutParams();
        // Wrap-up the content of the row
        col2Params.height = TableRow.LayoutParams.WRAP_CONTENT;
        col2Params.width = TableRow.LayoutParams.WRAP_CONTENT;
        // Set the gravity to center the gravity of the column
        col2Params.gravity = Gravity.CENTER;
        TextView col2 = new TextView(context);
        col2.setText(contentCol2);
        col2.setPadding(0, 0, 10, 0);
        row.addView(col2, col2Params);

        // THIRD COLUMN
        TableRow.LayoutParams col3Params = new TableRow.LayoutParams();
        // Wrap-up the content of the row
        col3Params.height = TableRow.LayoutParams.WRAP_CONTENT;
        col3Params.width = TableRow.LayoutParams.WRAP_CONTENT;
        // Set the gravity to center the gravity of the column
        col3Params.gravity = Gravity.CENTER;
        TextView col3 = new TextView(context);
        col3.setText(contentCol3);
        col3.setPadding(0, 0, 10, 0);
        row.addView(col3, col3Params);

        table.addView(row, rowParams);

        return table;
    }

}


//STOPS_TIMES.TXT
//trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type,shape_dist_traveled
//3, 1, 4
class Stop implements Comparable<Stop> {
    private String stopID;
    private String arrivalTime;
    private String sequenceNumber;

    public Stop(String stopID, String arrivalTime, String sequenceNumber) {
        this.arrivalTime = arrivalTime;
        this.sequenceNumber = sequenceNumber;
        this.stopID = stopID;
    }

    public String getStopID() {
        return stopID;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public int compareTo(Stop stop2) {
        return stopID.compareTo(stop2.getStopID());
    }
}

/*
TODO(NICK):
move the loading business to a separate thread like matt did with stop list.
 */