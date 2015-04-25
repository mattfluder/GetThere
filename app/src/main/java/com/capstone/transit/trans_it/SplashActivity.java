package com.capstone.transit.trans_it;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.MenuItem;

/**
 * Created by Thomas on 2/6/2015.
 */

public class SplashActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        //Intent fetchTimesIntent = new Intent(getApplicationContext(), FetchTimesService.class);
        //fetchTimesIntent.putExtra("receiver",new timesReceiver(new Handler()));
        //startService(fetchTimesIntent);

        Thread logoTimer = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                    startActivity(new Intent("com.capstone.transit.trans_it.MAINMENU"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally{
                    finish();
                }
            }
        };
        logoTimer.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
