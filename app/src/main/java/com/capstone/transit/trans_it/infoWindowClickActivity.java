package com.capstone.transit.trans_it;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;


public class infoWindowClickActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setTitle(retrieveStopName());
        setContentView(R.layout.marker_info);
        setRouteNames();
    }

    public String retrieveStopName() {

        Intent intent = getIntent();

        return intent.getStringExtra("STOP_NAME");

    }

    public String retrieveStopID() {

        Intent intent = getIntent();

        return intent.getStringExtra("STOP_ID");
    }

    protected void setRouteNames() {

        TextView t = (TextView) findViewById(R.id.textView1); // Linking variable t to the textView in the layout

        String StopID = retrieveStopID();

        AssetManager mngr;
        String line = null;
        int count = 0;

        try {
            mngr = getAssets();
            InputStream is = mngr.open("out.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while ((line = br.readLine()) != null) {  // Read until last line in .txt file
                if (count > 0) {
                    line = line.toUpperCase();
                    String[] ArrayValues = line.split(","); // Seperate line by commas into a list

                    if (Arrays.asList(ArrayValues).contains(StopID)) {
                        for (int i = 1; i < ArrayValues.length; i++) {
                            t.append(ArrayValues[i] + "\n");
                        }
                    }
                }
                count++;
            }

            br.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
