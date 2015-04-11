package com.capstone.transit.trans_it;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


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

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

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
        });

        expListView.expandGroup( 0 );
        expListView.expandGroup( 1 );

    }//ONCREATE END===================================================================================


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
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



}

/*
TODO
change trip planner to start with the info from intent
add second piece of info, user_identifier

Hide dialpad when coming from another activity.

create shortcut when installing

Need to guarantee that decription is unique for trips.

Buttons to ask jim for:
    go
    refresh
    switch (could be the same as refresh if done right)
    delete

Update list such that when all item are removed the default string is added.

 */