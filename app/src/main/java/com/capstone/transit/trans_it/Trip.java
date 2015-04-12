package com.capstone.transit.trans_it;

import android.util.Log;

/**
 * Created by Nicholas on 2015-04-10.
 */
public class Trip implements Comparable<Trip>{
    String start;
    String end;
    String description;

    public Trip(String start, String end) {
        this.start = start;
        this.end = end;
        this.description = "";
    }

    public Trip(String start, String end, String description) {
        this.start = start;
        this.end = end;
        this.description = description;
    }

    public boolean equals(Trip trip) {
        return this.start.equals(trip.start) && this.end.equals(trip.end);
    }

    @Override
    public int compareTo(Trip trip) {
        if (trip == null) {
            return -1;
        } else if (this.start.equals(trip.start) && this.end.equals(trip.end)) {
            return 0;
        } else {
                Log.d("METALKING: ", "THEY HAVE THE SAME END? " + this.end + "-" + trip.end);
                return this.description.compareTo(trip.description);
            }
        }


    public void setDescription(String newDesc ) {
        this.description = newDesc;
    }
}
