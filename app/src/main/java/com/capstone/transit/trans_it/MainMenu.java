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
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class MainMenu extends ActionBarActivity {

    // Setting up button references
    private ImageButton Settings;
    private ImageButton Map;
    private ImageButton StopList;
    private ImageButton TripPlanner;
    private ImageButton Favorites;
    private ImageButton RouteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Settings = (ImageButton) findViewById(R.id.SettingsA);
        Map = (ImageButton) findViewById(R.id.MapA);
        StopList = (ImageButton) findViewById(R.id.StopMonitorA);
        TripPlanner = (ImageButton) findViewById(R.id.TripPlannerA);
        Favorites = (ImageButton) findViewById(R.id.FavoritesA);
        RouteList = (ImageButton) findViewById(R.id.RouteListA);
/*
        Settings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.capstone.transit.trans_it.Settings"));

               if(Settings.isPressed())
                  Settings.setImageResource(R.drawable.settingspressed);
              //  else
                //    settings.setImageResource(R.drawable.settings);
            }
        });
        */

        Map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.capstone.transit.trans_it.MAPACTIVITY"));
              if(Map.isPressed())
                   Map.setImageResource(R.drawable.mappressed);
               // else
                //    Map.setImageResource(R.drawable.map);
            }
        });

        TripPlanner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //The caps lock activity names are kind of hideous TOM PLEASE
                startActivity(new Intent("com.capstone.transit.trans_it.TRIPPLANNERACTIVITY"));
            }
        });

        StopList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.capstone.transit.trans_it.StopListActivity"));
               if(StopList.isPressed())
                    StopList.setImageResource(R.drawable.stopmonitorpressed);
                //else
                //    StopList.setImageResource(R.drawable.stopmonitor);
            }
        });

        Favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.capstone.transit.trans_it.FavoritesActivity"));
                if(Favorites.isPressed())
                    Favorites.setImageResource(R.drawable.favouritespressed);
            }
        });

        RouteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.capstone.transit.trans_it.RouteList"));
                if(RouteList.isPressed())
                    RouteList.setImageResource(R.drawable.routelistpressed);
            }
        });
    }

    public void onResume()
    {
        super.onResume();
        Map.setImageResource(R.drawable.map);
        Settings.setImageResource(R.drawable.settings);
        StopList.setImageResource(R.drawable.stopmonitor);
        Favorites.setImageResource(R.drawable.favourites);
        RouteList.setImageResource(R.drawable.routelist);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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
