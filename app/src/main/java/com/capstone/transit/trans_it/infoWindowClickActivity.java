package com.capstone.transit.trans_it;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Thomas on 2/8/2015.
 */
public class infoWindowClickActivity extends Activity{

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        this.setTitle(retrieveStopName());
        setContentView(R.layout.marker_info);

    }

    public String retrieveStopName(){

        Intent intent = getIntent();
        String stopName = intent.getStringExtra("STOP_NAME");

        return stopName;

    }

    public String retrieveStopID(){

        Intent intent = getIntent();
        String stopName = intent.getStringExtra("STOP_ID");

        return stopName;
    }

}
