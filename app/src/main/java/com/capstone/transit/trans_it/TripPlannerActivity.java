package com.capstone.transit.trans_it;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;


public class TripPlannerActivity extends ListActivity {

    public static final String TAG = TripPlannerActivity.class.getSimpleName();

    private Step[] mStep;

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
        final ImageView reverseButton = (ImageView) findViewById(R.id.reverseButton);
        final Button loadDirectionsButton = (Button) findViewById(R.id.loadDirectionsButton);

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

        loadDirectionsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Trying to hide the dial pad afterwards. It worked once. Never again.
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(loadDirectionsButton.getApplicationWindowToken(), 0);

                if(validAddressCheck(Start,End) == 1){

                    double[] StartCoordinates, EndCoordinates;

                    StartCoordinates = getFromLocation(Start.getText().toString());
                    EndCoordinates = getFromLocation(End.getText().toString());

                    LatLng origin = new LatLng(StartCoordinates[0], StartCoordinates[1]);
                    LatLng dest = new LatLng(EndCoordinates[0], EndCoordinates[1]);

                    getDirections(origin,dest);

                    StepAdapter adapter = new StepAdapter(getApplicationContext(),mStep);

                    setListAdapter(adapter);
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
        }
        catch(Exception ee)
        {

            Log.d("RAAAAAAGE", ee.toString());
        }


        try
        {
            Address endAddressConvert = geoCoder.getFromLocationName(EndAddress , 1).get(0);
            endCoordinates[0] = endAddressConvert.getLatitude();
            endCoordinates[1] = endAddressConvert.getLongitude();
        }
        catch(Exception ee)
        {

            Log.d("RAAAAAAGE", ee.toString());
        }

        if(startCoordinates[0] == 0.0 && startCoordinates[1] == 0.0){

            signal = 0;
            startWarn = "Start Address";
        }

        if(endCoordinates[0] == 0.0 && endCoordinates[1] == 0.0){

            signal = 0;
            endWarn = "End Address";

            if(startWarn.equals("Start Address")){

                endWarn = " and End Address";
            }
        }


        if(signal == 0){

            Toast toast = Toast.makeText(TripPlannerActivity.this, "Please enter a valid " + startWarn + endWarn, Toast.LENGTH_SHORT);
            toast.show();
            startWarn = "";
            endWarn = "";
        }

        return signal;
    }

    public double[] getFromLocation(String address){

        double[] Coordinates = new double[2];

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

        try{

            Address addressConvert = geoCoder.getFromLocationName(address , 1).get(0);
            Log.v("ADDRESS CHECK",addressConvert+"");
            Coordinates[0] = addressConvert.getLatitude();
            Log.v("ADDRESS CHECK", Coordinates[0]+"");
            Coordinates[1] = addressConvert.getLongitude();
            Log.v("ADDRESS CHECK", Coordinates[1]+"");

        }
        catch(Exception ee)
        {
            Log.d(TAG, "THERE IS AN EXCEPTION SHIz: " + ee.toString());
        }

        return Coordinates;
    }

    //JSON STUFF=========================================================================

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

    private boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;

        if(networkInfo != null && networkInfo.isConnected()){

            isAvailable = true;
        }

        return isAvailable;
    }

    private void alertUserAboutError() {

        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");

    }

    private Step[] getSteps(String jsonData) throws JSONException{

        JSONObject trip = new JSONObject(jsonData);

        JSONArray routes = trip.getJSONArray("routes");
        JSONObject route = routes.getJSONObject(0);

        JSONArray legs = route.getJSONArray("legs");
        JSONObject leg = legs.getJSONObject(0);

        JSONArray data = leg.getJSONArray("steps");

        Step[] steps = new Step[data.length()];

        for(int i = 0; i < data.length(); i++){

            JSONObject jsonStep = data.getJSONObject(i);
            Step step = new Step();

            JSONObject stepDistance = jsonStep.getJSONObject("distance");
            JSONObject stepDuration = jsonStep.getJSONObject("duration");

            step.setDistance(stepDistance.getString("text"));
            step.setDuration(stepDuration.getString("text"));
            step.setHtmlInstructions(jsonStep.getString("html_instructions"));

            steps[i] = step;
        }

        return steps;
    }

    private void getDirections(LatLng origin, LatLng destination) {

        final String directionsURL = getDirectionsUrl(origin,destination);
        Log.v(TAG, directionsURL);

        if(isNetworkAvailable()) {

           Thread T = new Thread() {
               public void run() {
                   OkHttpClient client = new OkHttpClient();
                   Request request = new Request.Builder()
                           .url(directionsURL)
                           .build();


                   Call call = client.newCall(request);
                   try {
                       Response response = call.execute();
                       String jsonData = response.body().string();

                       Log.v(TAG, jsonData);

                       if (response.isSuccessful()) {

                           mStep = getSteps(jsonData);

                       } else {
                           alertUserAboutError();
                       }
                   } catch (JSONException | IOException e) {
                       e.printStackTrace();
                   }

               }
           };
            T.start();

            try {
                T.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        else{
            Toast.makeText(this, "unavailable network", Toast.LENGTH_LONG).show();
        }
    }


    //FAVORITES METHODS=======================================
    final View.OnClickListener favButtonClickListener = new View.OnClickListener() {
        @Override

        public void onClick(final View v) {

            final AutoCompleteTextView Start = (AutoCompleteTextView) findViewById(R.id.Start);
            final AutoCompleteTextView End = (AutoCompleteTextView) findViewById(R.id.End);

            if(validAddressCheck(Start,End) == 1) {

                final Trip trip = new Trip(Start.getText().toString(), End.getText().toString());

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
}

/*
TODO(TOM):

Some valid trips aren't validated properly.
 GH Gunther Huettlin Manufacturing .....
 to
 DFFD, Ouagadougou, Centre, Burkina Faso

 Does not work.

 ACTUALLY it doesn't validate anything.



 Also in the same session but unrelated, It crashed with a null pointer exception.
 StackTrace:
 04-17 14:43:47.663    9092-9092/com.capstone.transit.trans_it E/AndroidRuntime﹕ FATAL EXCEPTION: main
    java.lang.NullPointerException
            at com.capstone.transit.trans_it.PlacesAutoCompleteAdapter.getCount(PlacesAutoCompleteAdapter.java:43)
            at android.widget.AdapterView.checkFocus(AdapterView.java:700)
            at android.widget.AdapterView$AdapterDataSetObserver.onInvalidated(AdapterView.java:823)
            at android.widget.AbsListView$AdapterDataSetObserver.onInvalidated(AbsListView.java:5619)
            at android.database.DataSetObservable.notifyInvalidated(DataSetObservable.java:47)
            at android.widget.BaseAdapter.notifyDataSetInvalidated(BaseAdapter.java:59)
            at com.capstone.transit.trans_it.PlacesAutoCompleteAdapter$1.publishResults(PlacesAutoCompleteAdapter.java:74)

The layout needs to be reorganized, the buttons aren't in great spots and don't work at all in landscape.

 */

/*
TODO:(NICK)

Check if renaming favorites works after tom fixes the validation issue.
*/
