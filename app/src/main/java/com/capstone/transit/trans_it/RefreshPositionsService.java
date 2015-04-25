package com.capstone.transit.trans_it;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class RefreshPositionsService extends Service {
    private String routeID;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        routeID = intent.getStringExtra("EXTRA_ROUTE_ID");
        Toast.makeText(RefreshPositionsService.this,
                "Route: "+ routeID,
                Toast.LENGTH_LONG).show();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy(){
        Toast.makeText(RefreshPositionsService.this,
                "Service Stopped",
                Toast.LENGTH_LONG).show();
    }
}
