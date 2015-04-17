package com.capstone.transit.trans_it;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
                currentDayInt = currentDayInt % 8;
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

        //Make table that can scroll while keeping column headers visible.
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
}
