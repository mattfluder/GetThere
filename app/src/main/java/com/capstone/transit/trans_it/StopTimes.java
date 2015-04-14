package com.capstone.transit.trans_it;

import android.graphics.Color;

/**
 * Created by matt on 18/03/15.
 */
public class StopTimes {
    private String time, route, trip, vehicleNumber, toDisplay, tripHeader;
    private int timeColor = Color.BLACK;
    private int delay = 0;
    private boolean isRealtime;

    //Constructor
    public StopTimes (String inTime, String inRoute, String inTrip, boolean realtime, int inDelay,String inVehicleNumber, String inTripHeader){
        time = inTime;
        route = inRoute;

        trip = inTrip;
        isRealtime = realtime;
        delay = inDelay;
        vehicleNumber = inVehicleNumber;
        tripHeader = inTripHeader;
        setDisplay();
    }

    public void setTime (String inTime){
        this.time = inTime;
    }

    public String getTime() {
        return time;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getToDisplay() {
        return toDisplay;
    }

    public void setToDisplay(String toDisplay) {
        this.toDisplay = toDisplay;
    }

    public int getTimeColor() {
        return timeColor;
    }

    public void setTimeColor(int timeColor) {
        this.timeColor = timeColor;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
        if (isRealtime){
            if (this.delay > 180) timeColor = Color.RED;
            else timeColor = Color.GREEN;
        }
    }

    public boolean isRealtime() {
        return isRealtime;
    }

    public void setRealtime(boolean isRealtime) {
        this.isRealtime = isRealtime;
    }

    private void setDisplay(){
        if (isRealtime){
            if (delay > 180) {
                timeColor = Color.RED;
            }
            else timeColor = Color.rgb(0,153,0);
        }
        else timeColor = Color.BLACK;
        toDisplay = time + " To: "+ tripHeader + "\nVehicle:" + vehicleNumber;
    }

}
