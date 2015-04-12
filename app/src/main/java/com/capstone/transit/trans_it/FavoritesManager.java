package com.capstone.transit.trans_it;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by Nicholas on 2015-04-04.
 */

public final class FavoritesManager {

    final static String empty_stop_list = "You don't have any favorite stops!";
    final static String empty_trip_list = "You don't have any favorite trips!";
    final private static String STOPFILENAME = "favorite_stops";
    final private static String TRIPFILENAME = "favorite_trips";
    public static TreeSet<String> stop_set = new TreeSet<>();
    public static TreeSet<Trip> trip_set = new TreeSet<>();
    public static HashMap<String, String> stop_descriptions = new HashMap<>();
    public static HashMap<String, Trip> trip_descriptions = new HashMap<>();

    private static boolean loaded = false;
    private static boolean stop_list_updated = false;
    private static boolean trip_list_updated = false;

    public static void deleteFavoriteStop(String value, Context ctx) {

        stop_set.remove(value);
        stop_descriptions.remove(value);
        stop_list_updated = true;
        saveFavorites(ctx);

    }

    public static boolean isFavoriteStop(String value) {
        return stop_set.contains(value);
    }

    public static boolean isFavoriteTrip(Trip trip) {
        return trip_set.contains(trip);
        //Right now comparing on description. Probably change that for adding/removing trip.
    }

    public static boolean isFavoriteTripName(String name) {
        return trip_descriptions.containsKey(name);
    }

    public static void deleteFavoriteTrip(Trip trip, Context ctx) {
        //CANNOT REALLY DELETE RIGHT NOW.
        trip_set.remove(trip);
        trip_descriptions.remove(trip.description);
        trip_list_updated = true;
        saveFavorites(ctx);

    }

    public static void addFavoriteStop(String stop_code, Context ctx) {
        if (!loaded) {
            LoadFavorites(ctx);
        }

        stop_set.add(stop_code);
        stop_descriptions.put(stop_code, "");
        stop_list_updated = true;
        saveFavorites(ctx);

    }

    public static void addFavoriteStop(String stop_code, String description, Context ctx) {
        if (!loaded) {
            LoadFavorites(ctx);
        }

        stop_set.add(stop_code);
        stop_descriptions.put(stop_code, description);
        stop_list_updated = true;
        saveFavorites(ctx);

    }
    public static void addFavoriteTrip(Trip new_trip, Context ctx) {
        if (!loaded) {
            LoadFavorites(ctx);
        }

        trip_set.add(new_trip);
        trip_descriptions.put(new_trip.description, new_trip);
        trip_list_updated = true;
        saveFavorites(ctx);
    }


    public static void saveFavorites(Context ctx) {

        //File handling stuff. LATER
        FileOutputStream fos;

        if (stop_list_updated) {
            try {
                fos = ctx.openFileOutput(STOPFILENAME, Context.MODE_PRIVATE);

                for (String value : stop_set) {
                    fos.write((value + "\n").getBytes());
                    fos.write((stop_descriptions.get(value) + "\n").getBytes());
                }


                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (trip_list_updated) {
            try {
                fos = ctx.openFileOutput(TRIPFILENAME, Context.MODE_PRIVATE);

                for (Trip trip : trip_set) {
                    fos.write((trip.start + "\n").getBytes());
                    fos.write((trip.end + "\n").getBytes());
                    fos.write((trip.description + "\n").getBytes());
                }


                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static int stopSize() {
        return stop_set.size();
    }
    public static int tripSize() {
        return trip_set.size();
    }

    public static void LoadFavorites(Context ctx) {
        if (!loaded) {
            Log.d("ME TALKING: ", "LOADING THINGS NOW/AGAIN");
            String description;
            Trip trip;
            try {
                FileInputStream fis = ctx.openFileInput(STOPFILENAME);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

                for (String stop_code; (stop_code = reader.readLine()) != null; ) {
                    // process the line.
                    //always an even number of lines.
                    stop_set.add(stop_code);
                    description = reader.readLine();
                    stop_descriptions.put(stop_code, description);
                }

                fis.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                /*
                //TEMP TO DESTROY FILE.
                FileOutputStream fos = ctx.openFileOutput(TRIPFILENAME, Context.MODE_PRIVATE);
                fos.close();
                */
                FileInputStream fis = ctx.openFileInput(TRIPFILENAME);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

                for (String line; (line = reader.readLine()) != null; ) {
                    // process the line.
                    trip = new Trip(line, reader.readLine(), reader.readLine());
                    trip_set.add(trip);
                    trip_descriptions.put(trip.description, trip);
                }

                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            loaded = true;
        } else {
            Log.d("ME TALKING: ", "LOADED ALREADY");
        }
    }
}