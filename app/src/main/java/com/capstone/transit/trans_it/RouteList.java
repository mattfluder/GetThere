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

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class RouteList extends ActionBarActivity {

    ListView routesLV;
    String routetxt;
    /*
    might be used later. not now.

    public static TreeSet<Integer> routeNumbers = new TreeSet<>();
    public static HashMap<Integer, String> routeNames = new HashMap<>();
    */
    ArrayList<String> routeNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);

        routesLV = (ListView) findViewById(R.id.routeListView);
        routetxt = "routes.txt";
        final HashMap<String, String> routeIDs = new HashMap<>();

        //LOAD LIST OF ROUTES
        try {
            AssetManager assetManager = getAssets();
            InputStream fis = assetManager.open(routetxt);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String splitLine[];
            int routeNumber = 0;
            reader.readLine();

            for (String line; (line = reader.readLine()) != null; ) {
                // process the line.
                /*
                This way seems better to me because it guarantees ordering.
                The text file is already sorted right now though, so that's not a concern,
                plus I can skip a LOT of code by not adding this in.
                 */
                splitLine = line.split(",");
                /*
                routeNumber = Integer.parseInt(splitLine[splitLine.length - 1]);
                routeNumbers.add(routeNumber);
                routeNames.put(routeNumber,splitLine[0]);
                */
                routeNames.add(splitLine[splitLine.length - 1] + " - " + splitLine[0]);
                routeIDs.put(splitLine[splitLine.length - 1] + " - " + splitLine[0], splitLine[5]);
                //might have to get a list of ints one way or another to send to the next activity.
            }

            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //SET ONCLICK LISTENER

        //POPULATE LISTVIEW
        ListAdapter routeListAdapter = new ArrayAdapter<String>(this, R.layout.list_group, R.id.lblListHeader, routeNames);
        routesLV.setAdapter(routeListAdapter);
        routesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nextActivity = new Intent("com.capstone.transit.trans_it.RouteMap");
                TextView temp = (TextView) view.findViewById(R.id.lblListHeader);
                nextActivity.putExtra("EXTRA_NAME", temp.getText().toString());
                nextActivity.putExtra("EXTRA_ROUTE_ID", routeIDs.get(temp.getText().toString()));
                startActivity(nextActivity);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_route_list, menu);
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
