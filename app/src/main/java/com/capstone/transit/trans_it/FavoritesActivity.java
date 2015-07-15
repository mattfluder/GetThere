/**
 *
 This file is part of the "Get There!" application for android developed
 for the SFWR ENG 4G06 Capstone course in the 2014/2015 Fall/Winter
 terms at McMaster University.


 Copyright (C) 2015 M. Fluder, T. Miele, N. Mio, M. Ngo, and J. Rabaya

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package com.capstone.transit.trans_it;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class FavoritesActivity extends ActionBarActivity {

    List<String> groupList;
    List<String> stop_child_list;
    List<String> trip_child_list;
    Map<String, List<String>> collections;
    ExpandableListView expListView;
    FavoritesListAdapter expListAdapter;
    Menu theMenu;
    TextView renamingText;

    boolean renaming = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        renamingText = (TextView) findViewById(R.id.renamingText);
        createGroupList();

        collections = new HashMap<String, List<String>>();
        stop_child_list = new ArrayList<String>();
        trip_child_list = new ArrayList<String>();

        FavoritesManager.LoadFavorites(this);

        if (FavoritesManager.stopSize() == 0) {
            stop_child_list.add(FavoritesManager.empty_stop_list);
        } else {
            stop_child_list.addAll(FavoritesManager.stop_set);
        }
        collections.put("Stops", stop_child_list);

        if (FavoritesManager.tripSize() == 0) {
            trip_child_list.add(FavoritesManager.empty_trip_list);
        }else {
            trip_child_list.addAll(FavoritesManager.trip_descriptions.keySet());
        }
        collections.put("Trips", trip_child_list);

        expListView = (ExpandableListView) findViewById(R.id.FavList);
        expListAdapter = new FavoritesListAdapter( this, groupList, collections);
        expListView.setAdapter(expListAdapter);

        expListView.setOnChildClickListener(startActivityListener);

        expListView.expandGroup( 0 );
        expListView.expandGroup( 1 );



    }//ONCREATE END===================================================================================


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        theMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rename) {
            if (renaming) {
                stopRenaming();
            } else {
                startRenaming();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        expListAdapter.notifyDataSetChanged();
    }

    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add("Stops");
        groupList.add("Trips");
    }

    ExpandableListView.OnChildClickListener startActivityListener = new ExpandableListView.OnChildClickListener() {

        public boolean onChildClick(ExpandableListView parent, View v,
        int groupPosition, int childPosition, long id) {
            final String selected = (String) expListAdapter.getChild(groupPosition, childPosition);
            Intent nextActivityIntent;
            if (groupPosition == 0) {
                if (!selected.equals(FavoritesManager.empty_stop_list)) {
                    nextActivityIntent = new Intent(FavoritesActivity.this, StopListActivity.class);
                    nextActivityIntent.putExtra("STOP_CODE", selected);
                    startActivity(nextActivityIntent);
                }

                return true;
            }
            else if (groupPosition == 1){
                if (!selected.equals(FavoritesManager.empty_trip_list)) {
                    nextActivityIntent = new Intent(FavoritesActivity.this, TripPlannerActivity.class);
                    nextActivityIntent.putExtra("EXTRA_TRIP_START", FavoritesManager.trip_descriptions.get(selected).start);
                    nextActivityIntent.putExtra("EXTRA_TRIP_END", FavoritesManager.trip_descriptions.get(selected).end);
                    startActivity(nextActivityIntent);
                }

                return true;
            }
            return false;
        }
    };

    ExpandableListView.OnChildClickListener startRenameListener = new ExpandableListView.OnChildClickListener() {

        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {
            final String selected = (String) expListAdapter.getChild(groupPosition, childPosition);

            if (groupPosition == 0) {
                if (!selected.equals(FavoritesManager.empty_stop_list)) {
                    //rename stop
                    AlertDialog.Builder alert = new AlertDialog.Builder(FavoritesActivity.this);

                    alert.setTitle("Rename Favorite Stop");
                    alert.setMessage("Enter a new description/name to identify stop " + selected + ":");

                    // Set an EditText view to get user input
                    final EditText input = new EditText(FavoritesActivity.this);
                    input.setMaxLines(1);
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String description = input.getText().toString();
                            // Do something with value!
                            FavoritesManager.deleteFavoriteStop(selected, getApplication());
                            FavoritesManager.addFavoriteStop(selected, description, getApplication());

                            Toast toast = Toast.makeText(FavoritesActivity.this, "Favorite Stop Renamed.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });

                    alert.show();

                    stopRenaming();
                }
                return true;
            }
            else if (groupPosition == 1){
                if (!selected.equals(FavoritesManager.empty_trip_list)) {
                    ///rename trip.
                    AlertDialog.Builder alert = new AlertDialog.Builder(FavoritesActivity.this);

                    alert.setTitle("Rename Favorite Trip");
                    alert.setMessage("Enter a new description/name to identify trip. \n(e.g. \"To Grandpas\"):");

                    // Set an EditText view to get user input
                    final EditText input = new EditText(FavoritesActivity.this);
                    input.setMaxLines(1);
                    alert.setView(input);

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //This is not used in the end. Just to display the button.
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
                                        Toast toast = Toast.makeText(FavoritesActivity.this, "Description cannot be empty.", Toast.LENGTH_SHORT);
                                        toast.show();
                                    } else if (FavoritesManager.isFavoriteTripName(description)) {
                                        Toast toast = Toast.makeText(FavoritesActivity.this, "Description has already been used.", Toast.LENGTH_SHORT);
                                        toast.show();
                                    } else {
                                        dialog.dismiss();
                                        Trip trip = FavoritesManager.getTrip(selected);
                                        trip.setDescription(description);
                                        FavoritesManager.deleteFavoriteTrip(selected, getApplication());
                                        FavoritesManager.addFavoriteTrip(trip, getApplication());

                                        Toast toast = Toast.makeText(FavoritesActivity.this, "Trip Renamed.", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }

                                }
                            });

                    stopRenaming();
                }
                return true;
            }
            return false;
        }
    };

    private void startRenaming() {
        expListView.setOnChildClickListener(startRenameListener);
        theMenu.getItem(0).setTitle("Stop Renaming");
        Toast toast = Toast.makeText(this, "Tap to rename a favorite.", Toast.LENGTH_SHORT);
        toast.show();
        renaming = !renaming;
        renamingText.setVisibility(View.VISIBLE);
    }

    private void stopRenaming() {
        expListView.setOnChildClickListener(startActivityListener);
        theMenu.getItem(0).setTitle("Rename Favorite");
        /*
        //Might use this later if this is implemented as a rename mode instead of a single rename then done.
        Toast toast = Toast.makeText(this, "Done renaming.", Toast.LENGTH_SHORT);
        toast.show();
        */
        renaming = !renaming;
        renamingText.setVisibility(View.GONE);
    }

}

/*
TODO
create shortcut when installing

Update list such that when all item are removed the default string is added.

 */