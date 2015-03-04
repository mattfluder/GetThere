package com.capstone.transit.trans_it;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class Settings extends ActionBarActivity {


    private Switch notifications;
    private Switch realtime;
    private SeekBar refreshrate;
    private TextView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setUpButton();

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
        refreshrate = (SeekBar) findViewById(R.id.refreshRate);
        progressView=(TextView) findViewById(R.id.progressView);
        notifications.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(notifications.isChecked())
                {
                    //notifications.setChecked(false);
                    Toast.makeText(Settings.this, "Notifications ON", Toast.LENGTH_LONG).show();
                }
                else
                {
                   // notifications.setChecked(true);
                    Toast.makeText(Settings.this, "Notifications OFF", Toast.LENGTH_LONG).show();
                }
                //Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
              //  startActivity(intent);
            }
        });
        //thing
        realtime.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(realtime.isChecked())
                {
                    //realtime.setChecked(false);
                    Toast.makeText(Settings.this, "Realtime ON", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //realtime.setChecked(true);
                    Toast.makeText(Settings.this, "Realtime OFF", Toast.LENGTH_LONG).show();
                }

            }
        });

       refreshrate.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            int progress = 0;

            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
               progress = progresValue;
              Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
                }

           public void onStartTrackingTouch(SeekBar seekBar) {
                 Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
                }

          public void onStopTrackingTouch(SeekBar seekBar) {
                progressView.setText("Covered: " + progress + "/" + seekBar.getMax());
                Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
                  }

        });



    }
}
