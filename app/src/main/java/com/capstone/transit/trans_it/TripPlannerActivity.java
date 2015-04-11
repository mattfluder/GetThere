package com.capstone.transit.trans_it;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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
        final ImageView favButton = (ImageView) findViewById(R.id.favButton);

        Intent myIntent = getIntent();
        Start.setText(myIntent.getStringExtra("EXTRA_TRIP_START"));
        End.setText(myIntent.getStringExtra("EXTRA_TRIP_END"));

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

        favButton.setOnClickListener(favButtonClickListener);

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


    //FAVORITES METHODS=======================================
    final View.OnClickListener favButtonClickListener = new View.OnClickListener() {
        @Override

        public void onClick(final View v) {
            final TextView tripStart = (TextView)findViewById(R.id.Start);
            final TextView tripEnd = (TextView)findViewById(R.id.End);
            final Trip trip = new Trip(tripStart.getText().toString(), tripEnd.getText().toString());

            //ADD THE FAVORITE
            AlertDialog.Builder alert = new AlertDialog.Builder(TripPlannerActivity.this);

            alert.setTitle("Add To Favorites");
            alert.setMessage("Enter a small description/name to identify trip. \n(e.g. \"ToGrandmas\"):");

            // Set an EditText view to get user input
            final EditText input = new EditText(TripPlannerActivity.this);
            input.setMaxLines(1);
            alert.setView(input);

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //This is not used in the end. Just to diplay the button.
                }
            });

            final AlertDialog dialog = alert.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String description = input.getText().toString();

                            if (description.equals("")){
                                Toast toast = Toast.makeText(TripPlannerActivity.this, "Description cannot be empty.", Toast.LENGTH_SHORT);
                                toast.show();
                            } else if (FavoritesManager.isFavoriteTripName(description)) {
                                Toast toast = Toast.makeText(TripPlannerActivity.this, "Description has already been used.", Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                dialog.dismiss();
                                trip.setDescription(description);
                                FavoritesManager.addFavoriteTrip(trip, getApplication());

                                Toast toast = Toast.makeText(TripPlannerActivity.this, "Trip Added to Favorites", Toast.LENGTH_SHORT);
                                toast.show();
                            }






                        }
                    });


        }

    };

}

/*
TODO
Add a switch button to switch destination and start addresses
The  Favorites button never glows and I'm not sure how to make it better. :|
 */



