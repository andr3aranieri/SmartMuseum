package it.sapienza.pervasivesystems.smartmuseum;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;

import it.sapienza.pervasivesystems.smartmuseum.business.beacons.Monitoring;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.model.db.ExhibitDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;

/**
 * Created by andrearanieri on 21/04/16.
 */
public class SmartMuseumApp extends Application implements LoadExhibitsAsyncResponse {

    private Monitoring beaconMonitoring = new Monitoring();
    static public boolean isUserInsideMuseum = false;
    static public HashMap<String, ExhibitModel> unsortedExhibits = null;

    private ExhibitDB exhibitDB = new ExhibitDB();

    @Override
    public void onCreate() {
        Log.i("SmartMuseumApp", "APPLICATION START**************************************");

        super.onCreate();

        //beacons monitoring to detect users entrance and exit;
        this.beaconMonitoring.app = this;
        this.beaconMonitoring.initMonitoring((ContextWrapper) getApplicationContext());

        //initial loading the unordered exhibits from DB (we'll order this list in the list of exhibits view during beacons ranging);
        new LoadExhibitsAsync(this).execute();
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
//                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    @Override
    public void processFinish(ILCMessage message) {
        Log.i("SmartMuseum", "*********************");
        Log.i("SmartMuseum", "Load of exhibits done");
        unsortedExhibits = (HashMap<String, ExhibitModel>) message.getMessageObject();
        if(unsortedExhibits != null) {
            Log.i("Exhibit", "SOME DATA...");
            for (ExhibitModel em : unsortedExhibits.values()) {
                Log.i("Exhibit", em.getTitle() + ", " + em.getImage() + ", " + em.getLongDescriptionURL());
            }
        }
        else {
            Log.i("Exhibit", "NO DATA");
        }
        Log.i("SmartMuseum", "*********************");
    }
}

/***********************************************************************/
/* Async Task to retrieve data from neo4j rest ws */

interface LoadExhibitsAsyncResponse {
    void processFinish(ILCMessage message);
}

class LoadExhibitsAsync extends AsyncTask<Void, Integer, String> {

    private SmartMuseumApp delegate;
    private ILCMessage message = new ILCMessage();

    public LoadExhibitsAsync(SmartMuseumApp d) {
        this.delegate = d;
    }

    @Override
    protected String doInBackground(Void... voids) {
        this.message.setMessageType(ILCMessage.MessageType.INFO);
        this.message.setMessageText("List of unordered exhibits from DB");
        this.message.setMessageObject(new ExhibitDB().getModelsFromDB());
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        this.delegate.processFinish(this.message);
    }

}

