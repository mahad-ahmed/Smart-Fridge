package com.bytasaur.smartfridge;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GeofenceTransitionService extends IntentService {
    public GeofenceTransitionService() {
        super("GeofenceTransitionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event=GeofencingEvent.fromIntent(intent);
        if(event.hasError()) {
            return;
        }
        if(event.getGeofenceTransition()==Geofence.GEOFENCE_TRANSITION_ENTER) {
//            stackBuilder.addNextIntent(new Intent(this, MainActivity.class).setAction(Intent.ACTION_MAIN)
//                    .addCategory(Intent.CATEGORY_LAUNCHER).addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP));
            if(!MainActivity.adapter2.isEmpty()) {  //Check if null??
                final Intent notificationIntent=new Intent(this, MainActivity.class);
                notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                notificationIntent.putExtra("nClicked", true);
                NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this, "").setSmallIcon(R.drawable.common_full_open_on_phone)
                        .setContentTitle(getString(R.string.app_name)).setContentText("You're running out of some items. Nearest store is: "+event.getTriggeringGeofences().get(0).getRequestId())
                        .setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                        .setAutoCancel(true).setVibrate(new long[]{400, 100, 30, 100});
                NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify(-1, notificationBuilder.build());
                }
            }
        }
    }
}
