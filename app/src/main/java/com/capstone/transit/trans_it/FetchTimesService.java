package com.capstone.transit.trans_it;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.AssetManager;
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
 * An {@link android.app.IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
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
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            System.out.println("Opened Connection");
            errors = 1;
        }
        catch(Exception e) {
            /*toast = Toast.makeText(getApplicationContext(), "Failed to Open URL", Toast.LENGTH_SHORT);
            toast.show();*/
            e.printStackTrace();
            errors = 1;
        }
        try {
            is = new BufferedInputStream(urlConnection.getInputStream());
            System.out.println("Created input stream");
        }
        catch(Exception e) {
            /*toast = Toast.makeText(getApplicationContext(), "Failed to Create Input Stream", Toast.LENGTH_SHORT);
            toast.show();*/
            e.printStackTrace();
            errors = 1;
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
            errors = 1;
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
                errors = 1;
            }
        }
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle resultData = new Bundle();
        resultData.putBoolean("Errors", false);
        receiver.send(errors,resultData);
        System.out.println("Sent Results");
    }
}
