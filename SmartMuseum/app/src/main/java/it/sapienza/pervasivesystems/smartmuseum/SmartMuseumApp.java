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

import com.estimote.sdk.Beacon;
import com.ullink.slack.simpleslackapi.SlackSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import it.sapienza.pervasivesystems.smartmuseum.business.InternalStorage.FileSystemBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.beacons.BeaconBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.beacons.Monitoring;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.SlackBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.db.ExhibitDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitWorkofartModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;
import it.sapienza.pervasivesystems.smartmuseum.view.slack.gui.ChatMessage;

/**
 * Created by andrearanieri on 21/04/16.
 */
public class SmartMuseumApp extends Application implements LoadExhibitsAsyncResponse {

    private Monitoring beaconMonitoring = new Monitoring();

    static public boolean isUserInsideMuseum = true;
    static public HashMap<String, ExhibitModel> unsortedExhibits = null;
    static public HashMap<String, WorkofartModel> workofartModelHashMap = null;
    static public Collection<ExhibitModel> detectedSortedExhibits = null;

    //hashmap used to store only once each exhibit visit for a user;
    static public HashMap<String, VisitExhibitModel> todayVisitedExhibits = null;
    static public HashMap<String, VisitExhibitModel> totalVisitedExhibits = null;
    static public HashMap<String, VisitWorkofartModel> todayVisitedWorksofart = null;
    static public HashMap<String, VisitWorkofartModel> totalVisitedWorksofart = null;

    static public int visitDistanceTreshold = 1;
    static public UserModel loggedUser = null;
    static public Date lastSeenBeaconTimeStamp;
    static public Date lastOrderingTimeStamp;
    static public int exhibitsReorderingPeriod = 5;
    static public boolean noBeacons = false;
    static public String newQuestionText;
    static public Beacon nearestExhibitBeacon;
    static public String localLoginFile = "LOCAL_LOGIN";
    static public boolean saveVisit = true;
    static public boolean newMessage = false;
    static public boolean newMessageRead = true;

    static public SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");

    //Slack integration;
    static public SlackSession slackSession = null;

    private ExhibitDB exhibitDB = new ExhibitDB();

    private ArrayList<ChatMessage> oldMessages;
    private SlackBusiness slackBusiness = new SlackBusiness();

    @Override
    public void onCreate() {
        Log.i("SmartMuseumApp", "APPLICATION START**************************************");

        super.onCreate();

        //beacons monitoring to detect users entrance and exit;
        this.beaconMonitoring.app = this;
        this.beaconMonitoring.initMonitoring((ContextWrapper) getApplicationContext());

        //try to load user from local file system;
        SmartMuseumApp.loggedUser = new FileSystemBusiness(this).readUserFromFile(SmartMuseumApp.localLoginFile);

        //initial loading the unordered exhibits from DB (we'll order this list in the list of exhibits view during beacons ranging);
        new LoadExhibitsAsync(this).execute();
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_smartmuseumnotifica6)
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

    static public String getNearestExhibitDescription() {
        if (nearestExhibitBeacon != null) {
            String key = new BeaconBusiness().getBeaconHashmapKey(nearestExhibitBeacon);

            ExhibitModel em = SmartMuseumApp.unsortedExhibits.get(key);
            return em != null ? "_EXHIBIT_: *" + em.getTitle() + "* - _LOCATION_: *" + em.getLocation() + "*\n": "";
        } else
            return "";
    }

    @Override
    public void processFinish(ILCMessage message) {
        Log.i("SmartMuseum", "*********************");
        Log.i("SmartMuseum", "Load of exhibits done");
        unsortedExhibits = (HashMap<String, ExhibitModel>) message.getMessageObject();
        if (unsortedExhibits != null) {
            Log.i("Exhibit", "SOME DATA...");
            for (ExhibitModel em : unsortedExhibits.values()) {
                Log.i("Exhibit", em.getTitle() + ", " + em.getImage() + ", " + em.getLongDescriptionURL() + ", " + em.getAudioURL() + ", " + em.getTimestamp() + ", " + em.getColor());
            }
        } else {
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
        this.message.setMessageObject(new ExhibitBusiness().getUnorderedExhibits());
        return this.message.getMessageText();
    }

    @Override
    protected void onPostExecute(String result) {
        this.delegate.processFinish(this.message);
    }

}
