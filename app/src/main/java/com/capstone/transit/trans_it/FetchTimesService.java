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

import android.app.IntentService;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.ResultReceiver;


import com.google.transit.realtime.GtfsRealtime.*;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
    Gets the real time GTFS_TripUpdates file from the City of Hamilton OpenData server
    returns:
    0 if successful
    1 if URL error
    2 if urlConnection Error
    3 if BufferedInput Stream Error
    4 if Writing to File error
    5 if Close Stream Error
 */
public class FetchTimesService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public FetchTimesService() {
        super("FetchTimesService");
    }
    int errors = 0;
    @Override
    protected void onHandleIntent(Intent intent) {
        InputStream is = null;
        HttpURLConnection urlConnection = null;
        FileOutputStream fileStream = null;
        URL url = null;
        try {
            url = new URL("http://opendata.hamilton.ca/GTFS-RT/GTFS_TripUpdates.pb");
        }
        catch (Exception e) {
            e.printStackTrace();
            errors = 1;
            exitWithError(intent);
            return;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            System.out.println("Opened Connection");
        }
        catch(Exception e) {
            /*toast = Toast.makeText(getApplicationContext(), "Failed to Open URL", Toast.LENGTH_SHORT);
            toast.show();*/
            e.printStackTrace();
            errors = 2;
            exitWithError(intent);
            return;
        }
        try {
            is = new BufferedInputStream(urlConnection.getInputStream());
            System.out.println("Created input stream");
        }
        catch(Exception e) {
            /*toast = Toast.makeText(getApplicationContext(), "Failed to Create Input Stream", Toast.LENGTH_SHORT);
            toast.show();*/
            e.printStackTrace();
            errors = 3;
            exitWithError(intent);
            return;
        }
        try {
            File GTFR_TripUpdates = new File (getFilesDir(),"GTFS_TripUpdates.pb");
            fileStream = new FileOutputStream(GTFR_TripUpdates);
            int read;
            byte[] data = new byte[1024];
            while ((read = is.read(data)) != -1)
                fileStream.write(data, 0, read);
            System.out.println("parsed data");
        }
        catch (Exception e) {
            /*toast = Toast.makeText(getApplicationContext(), "Failed to Write File", Toast.LENGTH_SHORT);
            toast.show();*/
            e.printStackTrace();
            errors = 4;
            exitWithError(intent);
            return;
        }
        finally{
            try {
                if (fileStream != null)
                    fileStream.close();
                if (is != null)
                    is.close();
            }
            catch (Exception e) {
                System.out.print("Failed to Close Streams");
                e.printStackTrace();
                errors = 5;
                exitWithError(intent);
                return;
            }
        }
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle resultData = new Bundle();
        resultData.putBoolean("Errors", false);
        receiver.send(errors,resultData);
        System.out.println("Sent Results");
    }

    private void exitWithError(Intent intent){
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle resultData = new Bundle();
        resultData.putBoolean("Errors", true);
        receiver.send(errors,resultData);
        System.out.println("Sent Results");
    }

}
