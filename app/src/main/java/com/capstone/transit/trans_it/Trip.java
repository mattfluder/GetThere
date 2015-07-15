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
