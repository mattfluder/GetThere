package com.capstone.transit.trans_it;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;


public class TripPlannerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_planner);

        final AutoCompleteTextView Start = (AutoCompleteTextView) findViewById(R.id.Start);
        final AutoCompleteTextView End = (AutoCompleteTextView) findViewById(R.id.End);

        Start.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.trip_list_item));
        End.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.trip_list_item));

        final Button ShowOnMap = (Button) findViewById(R.id.ShowMapB);

        ShowOnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String StartAddress,EndAddress;

                StartAddress = Start.getText().toString();
                EndAddress = End.getText().toString();

                Intent intent = new Intent(getBaseContext(), TripDisplayActivity.class);

                intent.putExtra("START_ADDRESS", StartAddress); // Passing StartAddress to next activity
                intent.putExtra("END_ADDRESS", EndAddress); // Passing EndAddress to next activity

                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trip_planner, menu);
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
