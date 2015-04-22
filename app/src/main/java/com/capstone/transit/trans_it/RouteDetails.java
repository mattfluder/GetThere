package com.capstone.transit.trans_it;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.protobuf.Internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


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
        Button showOnMap = (Button) findViewById(R.id.showOnMap);

        routeName = getIntent().getStringExtra("EXTRA_NAME");
        routeID = getIntent().getStringExtra("EXTRA_ROUTE_ID");

        Calendar c = Calendar.getInstance();
        currentDayString = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.CANADA);

        Map<String, Integer> dayToNum = c.getDisplayNames(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.CANADA);
        dayToNum.put("Holiday", 0);
        final Map<Integer, String> numToDay = new HashMap<>();
        for(Map.Entry<String, Integer> entry : dayToNum.entrySet()){
            numToDay.put(entry.getValue(), entry.getKey());
        }
        dayTextView.setText(currentDayString);

        currentDayInt = dayToNum.get(currentDayString);

        previousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDayInt -= 1;
                currentDayInt = (currentDayInt + 8) % 8;
                Log.d("ME TALKING: ", Integer.toString(currentDayInt));
                currentDayString = numToDay.get(currentDayInt);
                dayTextView.setText(currentDayString);
            }
        });

        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDayInt += 1;
                currentDayInt = currentDayInt % 8;
                currentDayString = numToDay.get(currentDayInt);
                dayTextView.setText(currentDayString);
            }
        });

        showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent("com.capstone.transit.trans_it.RouteMap");
                nextActivity.putExtra("EXTRA_ROUTE_ID", routeID);
                startActivity(nextActivity);
            }
        });

        //Make table that can scroll while keeping column headers visible.
        TableLayout headerTable = (TableLayout) findViewById(R.id.header_table);
        headerTable.setColumnStretchable(0, true);
        headerTable.setColumnShrinkable(1, true);
        headerTable.setColumnShrinkable(2, true);

        addRowToTable(headerTable, "Stop", "Time", "Time", getApplicationContext());

        TableLayout contentTable = (TableLayout) findViewById(R.id.content_table);
        contentTable.setColumnStretchable(0, true);
        contentTable.setColumnShrinkable(1, true);
        contentTable.setColumnShrinkable(2, true);

        //populate table
        //LOAD LIST OF ROUTES
        String triptxt = "trips.txt";
        String stopstxt = "stops.txt";

        try {
            AssetManager assetManager = getAssets();
            InputStream fis = assetManager.open(triptxt);
            InputStream fis2 = assetManager.open(stopstxt);
            BufferedReader tripReader = new BufferedReader(new InputStreamReader(fis));
            BufferedReader stopReader = new BufferedReader(new InputStreamReader(fis2));
            String splitLine[];
            String innerSplitLine[];
            tripReader.readLine();
            stopReader.readLine();

            for (String line; (line = tripReader.readLine()) != null; ) {
                // process the line.
                splitLine = line.split(",");
                if (routeID.equals(splitLine[0])) {
                    
                }
            }

            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


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
