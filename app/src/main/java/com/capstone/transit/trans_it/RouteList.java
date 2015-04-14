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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


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
                //TODO(NICK):
                // putExtra info.
                startActivity(new Intent("com.capstone.transit.trans_it.RouteDetails"));
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
