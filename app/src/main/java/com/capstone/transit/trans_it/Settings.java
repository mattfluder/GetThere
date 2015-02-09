package com.capstone.transit.trans_it;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;


public class Settings extends ActionBarActivity {


    Switch notifications;
    Switch realtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

    public void setUpButton()
    {
        notifications = (Switch) findViewById(R.id.notifications);
        realtime  = (Switch) findViewById(R.id.realtime);


        notifications.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                if(notifications.isChecked())
                {
                    notifications.setChecked(false);
                    Toast.makeText(Settings.this, "Notifications Off", Toast.LENGTH_LONG).show();
                }
                else
                {
                    notifications.setChecked(true);
                    Toast.makeText(Settings.this, "Notifications On", Toast.LENGTH_LONG).show();
                }
                //Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
              //  startActivity(intent);
            }
        });

        realtime.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                if(realtime.isChecked())
                {
                    realtime.setChecked(false);
                    Toast.makeText(Settings.this, "Realtime Off", Toast.LENGTH_LONG).show();
                }
                else
                {
                    realtime.setChecked(true);
                    Toast.makeText(Settings.this, "Realtime On", Toast.LENGTH_LONG).show();
                }

            }
        });



    }
}
