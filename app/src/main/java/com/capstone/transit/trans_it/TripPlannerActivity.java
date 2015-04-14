package com.capstone.transit.trans_it;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class TripPlannerActivity extends ActionBarActivity {

    private static final String TAG_ROUTES = "routes";
    private static final String TAG_COPYRIGHTS = "copyrights";
    private static final String TAG_LEGS = "legs";
    private static final String TAG_STEPS = "steps";
    private static final String TAG_DISTANCE = "distance";
    private static final String TAG_DISTANCE_TEXT = "distance_text";
    private static final String TAG_DISTANCE_VALUE = "distance_value";
    private static final String TAG_DURATION = "duration";
    private static final String TAG_END_LOCATION = "end_location";
    private static final String TAG_HTML_INSTRUCTIONS = "html_instructions";
    private static final String TAG_START_LOCATION = "start_location";
    private static final String TAG_TRAVEL_MODE = "travel_mode";

    private static String url;// = "http://maps.googleapis.com/maps/api/directions/json?origin=redfern+ave,+dublin&destination=limetree+ave,+dublin&sensor=false/";

    // contacts JSONArray
    JSONArray jRoutes = null;
    JSONArray jLegs = null;
    JSONArray jSteps = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> directionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_planner);

        directionsList = new ArrayList<HashMap<String, String>>();

        //ListView list = (ListView)findViewById(android.R.id.list);

        final AutoCompleteTextView Start = (AutoCompleteTextView) findViewById(R.id.Start);
        final AutoCompleteTextView End = (AutoCompleteTextView) findViewById(R.id.End);

        Start.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.trip_list_item));
        End.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.trip_list_item));

        final Button ShowOnMap = (Button) findViewById(R.id.ShowMapB);
        final Button GetDirections = (Button) findViewById(R.id.getDirectionsB);
        final ImageView favButton = (ImageView) findViewById(R.id.favButton);
        final ImageView reverseButton = (ImageView) findViewById(R.id.reverseButton);

        FavoritesManager.LoadFavorites(this);

        Intent myIntent = getIntent();
        Start.setText(myIntent.getStringExtra("EXTRA_TRIP_START"));
        End.setText(myIntent.getStringExtra("EXTRA_TRIP_END"));

        ShowOnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(validAddressCheck(Start,End) == 1){

                    final String StartAddress,EndAddress;

                    StartAddress = Start.getText().toString();
                    EndAddress = End.getText().toString();

                    Intent intent = new Intent(getBaseContext(), TripDisplayActivity.class);

                    intent.putExtra("START_ADDRESS", StartAddress); // Passing StartAddress to next activity
                    intent.putExtra("END_ADDRESS", EndAddress); // Passing EndAddress to next activity

                    startActivity(intent);
                }
            }
        });

        GetDirections.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (validAddressCheck(Start, End) == 1) {

                    double[] StartCoordinates, EndCoordinates;

                    StartCoordinates = getFromLocation(Start.getText().toString());
                    EndCoordinates = getFromLocation(End.getText().toString());

                    LatLng origin = new LatLng(StartCoordinates[0], StartCoordinates[1]);
                    LatLng dest = new LatLng(EndCoordinates[0], EndCoordinates[1]);

                    url = getDirectionsUrl(origin, dest);

                    new GetDirections().execute();


                }
            }
        });

        favButton.setOnClickListener(favButtonClickListener);
        reverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable temp;

                temp = Start.getText();
                Start.setText(End.getText());
                End.setText(temp);

            }
        });
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetDirections extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    jRoutes = jsonObj.getJSONArray(TAG_ROUTES);

                    for(int i=0;i<jRoutes.length();i++){
                        jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");

                        /** Traversing all legs */
                        for(int j=0;j<jLegs.length();j++){
                            jSteps = ((JSONObject)jLegs.get(j)).getJSONArray("steps");

                            /** Traversing all steps */
                            for(int k=0;k<jSteps.length();k++){

                                // Getting Distance Object

                                JSONObject distance = ((JSONObject)jSteps.get(j)).getJSONObject(TAG_DISTANCE);
                                String distance_text = distance.getString(TAG_DISTANCE_TEXT);
                                String distance_value = distance.getString(TAG_DISTANCE_VALUE);

                                HashMap<String, String> singleDirection = new HashMap<String, String>();

                                singleDirection.put(TAG_DISTANCE_TEXT,distance_text);
                                singleDirection.put(TAG_DISTANCE_VALUE,distance_value);

                                directionsList.add(singleDirection);



                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            /**
             * Updating parsed JSON data into ListView
             * */

             ListAdapter adapter = new SimpleAdapter(
                    TripPlannerActivity.this, directionsList,
                    R.layout.direction_list_item, new String[] { TAG_DISTANCE_TEXT, TAG_DISTANCE_VALUE
                     }, new int[] { R.id.text,
                    R.id.value });

            //ListView list = (ListView)findViewById(android.R.id.list);

            //list.setAdapter(adapter);


        }

    }

    public int validAddressCheck(AutoCompleteTextView Start, AutoCompleteTextView End){

        final String StartAddress,EndAddress;
        String startWarn = "";
        String endWarn = "";
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        int signal=1;
        double[] startCoordinates = new double[2];
        double[] endCoordinates = new double[2];

        StartAddress = Start.getText().toString();
        EndAddress = End.getText().toString();

        try
        {
            Address startAddressConvert = geoCoder.getFromLocationName(StartAddress , 1).get(0);
            startCoordinates[0] = startAddressConvert.getLatitude();
            startCoordinates[1] = startAddressConvert.getLongitude();

            Address endAddressConvert = geoCoder.getFromLocationName(EndAddress , 1).get(0);
            endCoordinates[0] = endAddressConvert.getLatitude();
            endCoordinates[1] = endAddressConvert.getLongitude();

        }
        catch(Exception ee)
        {

        }

        if(startCoordinates[0] == 0.0 && startCoordinates[1] == 0.0){

            signal = 0;
            startWarn = "Start Address and ";
        }

        if(endCoordinates[0] == 0.0 && endCoordinates[1] == 0.0){

            signal = 0;
            endWarn = "End Address";
        }

        if(signal == 0){

            Toast toast = Toast.makeText(TripPlannerActivity.this, "Please enter a valid "+startWarn+endWarn, Toast.LENGTH_SHORT);
            toast.show();
        }

        return signal;
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Transit Mode

        String mode = "mode=transit";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+mode+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    public double[] getFromLocation(String address){

        double[] Coordinates = new double[2];

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try
        {
            Address addressConvert = geoCoder.getFromLocationName(address , 1).get(0);
            Coordinates[0] = addressConvert.getLatitude();
            Coordinates[1] = addressConvert.getLongitude();

        }
        catch(Exception ee)
        {

        }

        return Coordinates;
    }


    //FAVORITES METHODS=======================================
    final View.OnClickListener favButtonClickListener = new View.OnClickListener() {
        @Override

        public void onClick(final View v) {

            final AutoCompleteTextView Start = (AutoCompleteTextView) findViewById(R.id.Start);
            final AutoCompleteTextView End = (AutoCompleteTextView) findViewById(R.id.End);

            if(validAddressCheck(Start,End) == 1) {
                final TextView tripStart = (TextView) findViewById(R.id.Start);
                final TextView tripEnd = (TextView) findViewById(R.id.End);
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

                                if (description.equals("")) {
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
        }

    };

    // DEFAULT STUFF ============================================================

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

/*
TODO
The  Favorites button never glows and I'm not sure how to make it better. :|
Also can't delete favorites from this screen, that might be for the best though.

The layout needs to be reorganized, the buttons aren't in great spots and don't work at all in landscape.

 */



