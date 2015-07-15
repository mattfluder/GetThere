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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
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

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logocapstone)
                        .setContentTitle("Notification Test")
                        .setContentText("Open app by tapping notification");

        Intent resultIntent = new Intent(this,MainMenu.class);
// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

// Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

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
                    Toast.makeText(Settings.this, "Notifications ON", Toast.LENGTH_SHORT).show();


                }
                else
                {
                   // notifications.setChecked(true);
                    Toast.makeText(Settings.this, "Notifications OFF", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Settings.this, "Realtime ON", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //realtime.setChecked(true);
                    Toast.makeText(Settings.this, "Realtime OFF", Toast.LENGTH_SHORT).show();
                }

            }
        });

       refreshrate.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            int progress = 0;

            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
               progress = progressValue;
              Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
                }

           public void onStartTrackingTouch(SeekBar seekBar) {
                 Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
                }

          public void onStopTrackingTouch(SeekBar seekBar) {
                progressView.setText("Refreshrate " + progress + " /minute");
                Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
                  }

        });



    }
}
