package com.bytasaur.smartaffix;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import java.util.List;

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
            //Toast.makeText(getApplicationContext(), "Error: "+event.getErrorCode(), Toast.LENGTH_SHORT).show();
            return;
        }
        if(event.getGeofenceTransition()==Geofence.GEOFENCE_TRANSITION_ENTER) {
            TaskStackBuilder stackBuilder=TaskStackBuilder.create(this);
            stackBuilder.addParentStack(SigninActivity.class);
            stackBuilder.addNextIntent(new Intent(this, SigninActivity.class));
            NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this).setSmallIcon(R.drawable.common_full_open_on_phone)
                    .setContentTitle(getString(R.string.app_name)).setContentText(event.getTriggeringGeofences().get(0).getRequestId())
                    .setContentIntent(stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)).setAutoCancel(true).setVibrate(new long[]{400, 100, 30, 100});

            NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
            // TBD: remove notification with id on click

            //System.out.println("You've entered: "+lst);
            //Toast.makeText(getApplicationContext(), "You've entered: "+lst.get(0)+"\nSize: "+lst.size(), Toast.LENGTH_LONG).show();
        }
    }
}
