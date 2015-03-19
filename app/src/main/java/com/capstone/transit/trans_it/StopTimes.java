package com.capstone.transit.trans_it;

import android.graphics.Color;

/**
 * Created by matt on 18/03/15.
 */
public class StopTimes {
    private String time, route, trip;
    private int timeColor = Color.BLACK;
    private int delay = 0;
    private boolean isRealtime;

    //Constructor
    public StopTimes (String inTime, String inRoute, String inTrip, boolean realtime, int inDelay){
        time = inTime;
        route = inRoute;
        trip = inTrip;
        isRealtime = realtime;
        delay = inDelay;
        if (realtime == true){
            if (delay > 180){
                timeColor = Color.RED;
            }
            else timeColor = Color.GREEN;
        }
        else timeColor = Color.BLACK;
    }

    public void setTime (String inTime){
        time = inTime;
    }
    public void setRouteName (String name){
        route = name;
    }
    public int getColor (){
        return timeColor;
    }
}
