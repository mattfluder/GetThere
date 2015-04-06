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
    public static TreeSet<String> trip_set = new TreeSet<>();
    public static HashMap<String, String> stop_descriptions = new HashMap<>();

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

    public static void deleteFavoriteTrip(String value, Context ctx) {

        trip_set.remove(value);
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

        /*
        tring s = "<b>Bolded text</b>, <i>italic text</i>, even <u>underlined</u>!"
        TextView tv = (TextView)findViewById(R.id.THE_TEXTVIEW_ID);
        tv.setText(Html.fromHtml(s));
        */
        stop_set.add(stop_code);
        stop_descriptions.put(stop_code, description);
        stop_list_updated = true;
        saveFavorites(ctx);

    }
    public static void addFavoriteTrip(String trip_info, Context ctx) {
        if (!loaded) {
            LoadFavorites(ctx);
        }

        trip_set.add(trip_info);
        trip_list_updated = true;
        saveFavorites(ctx);
    }

    public static void addFavoriteTrip(String trip_info, String name, Context ctx) {
        if (!loaded) {
            LoadFavorites(ctx);
        }
        //name this  trip! ex. To Grandma's

        trip_set.add(trip_info);
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

                for (String value : trip_set) {
                    fos.write((value + "\n").getBytes());
                }


                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static int stopsize() {
        return stop_set.size();
    }
    public static int tripsize() {
        return trip_set.size();
    }

    public static void LoadFavorites(Context ctx) {
        if (!loaded) {
            Log.d("ME TALKING: ", "LOADING THINGS NOW/AGAIN");
            String description;
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
                FileInputStream fis = ctx.openFileInput(TRIPFILENAME);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

                for (String line; (line = reader.readLine()) != null; ) {
                    // process the line.
                    trip_set.add(line);
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