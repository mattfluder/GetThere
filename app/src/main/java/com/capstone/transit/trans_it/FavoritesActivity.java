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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


        createGroupList();

        HashMap<String, List<String>> collections = new HashMap<String, List<String>>();
        stop_child_list = new ArrayList<String>();
        trip_child_list = new ArrayList<String>();

        FavoritesManager.LoadFavorites(this);

        if (FavoritesManager.stopsize() == 0) {
            stop_child_list.add(FavoritesManager.empty_stop_list);
        } else {
            stop_child_list.addAll(FavoritesManager.stop_set);
        }
        collections.put("Stops", stop_child_list);

        if (FavoritesManager.tripsize() == 0) {
            trip_child_list.add(FavoritesManager.empty_trip_list);
        }else {
            trip_child_list.addAll(FavoritesManager.trip_set);
        }
        collections.put("Trips", trip_child_list);

        expListView = (ExpandableListView) findViewById(R.id.FavList);
        final FavoritesListAdapter expListAdapter = new FavoritesListAdapter(
                this, groupList, collections);
        expListView.setAdapter(expListAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(groupPosition, childPosition);
                if (!selected.equals(FavoritesManager.empty_stop_list)
                        && !selected.equals(FavoritesManager.empty_trip_list)) {
                    Intent stopmonitorintent = new Intent(FavoritesActivity.this, StopListActivity.class);
                    stopmonitorintent.putExtra("STOP_CODE", selected);
                    startActivity(stopmonitorintent);
                }

                return true;
            }
        });

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


 */