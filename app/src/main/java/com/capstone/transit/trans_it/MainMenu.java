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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Settings = (ImageButton) findViewById(R.id.SettingsA);
        Map = (ImageButton) findViewById(R.id.MapA);
        StopList = (ImageButton) findViewById(R.id.StopMonitorA);
        TripPlanner = (ImageButton) findViewById(R.id.TripPlannerA);
        Favorites = (ImageButton) findViewById(R.id.FavoritesA);

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
    }

    public void onResume()
    {
        super.onResume();
        Map.setImageResource(R.drawable.map);
        Settings.setImageResource(R.drawable.settings);
        StopList.setImageResource(R.drawable.stopmonitor);
        Favorites.setImageResource(R.drawable.favourites);
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
