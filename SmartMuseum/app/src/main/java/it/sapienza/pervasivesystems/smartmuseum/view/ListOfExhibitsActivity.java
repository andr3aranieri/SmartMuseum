package it.sapienza.pervasivesystems.smartmuseum.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.beacons.BeaconBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.beacons.Ranging;
import it.sapienza.pervasivesystems.smartmuseum.business.beacons.RangingDetection;
import it.sapienza.pervasivesystems.smartmuseum.business.datetime.DateTimeBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.WorkofartBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.business.visits.VisitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.adapter.ExhibitModelArrayAdapter;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitWorkofartModel;
import it.sapienza.pervasivesystems.smartmuseum.view.slack.gui.MainChatActivity;

public class ListOfExhibitsActivity extends AppCompatActivity implements RangingDetection, ListOfExhibitsAsyncResponse, LoadUserHistoryAsyncResponse {

    private ListView listView;
    ExhibitModelArrayAdapter exhibitAdapter;
    ArrayList<ExhibitModel> dataItems = new ArrayList<ExhibitModel>();
    private Ranging beaconsRanging;
    private ExhibitBusiness exhibitBusiness = new ExhibitBusiness();
    private BeaconBusiness beaconBusiness = new BeaconBusiness();
    private VisitBusiness visitBusiness = new VisitBusiness();
    private boolean iAmWriting = false;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_exhibits);

//        //reading data from sorted exhibitList and set it to the adapter class
//        this.dataItems = (ArrayList<ExhibitModel>) new ExhibitDB().getSortedExhibitList();
//        exhibitAdapter = new ExhibitModelArrayAdapter(ListOfExhibitsActivity.this, R.layout.activity_item_of_exhibits, dataItems);
//
//        // Getting a reference to listview of activity_item_of_exhibits layout file
//        listView = (ListView) findViewById(R.id.listview);
//        listView.setItemsCanFocus(false);
//        listView.setAdapter(exhibitAdapter);

        //initial loading of the today user exhibit history not to store a visit more than once;
        new LoadUserHistoryAsync(this).execute();

        this.progressDialog = new ProgressDialog(ListOfExhibitsActivity.this, R.style.AppTheme_Dark_Dialog);
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setMessage("Loading User History... Please wait.");
        this.progressDialog.show();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(SmartMuseumApp.noBeacons) {
            this.loadExhibitsNoBeacons();
        }
        else {
            //start ranging;
            this.beaconsRanging = new Ranging(this);
            Ranging.rangingDetection = this;
            this.beaconsRanging.initRanging();
        }

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            System.out.println("******ACTION logout*********");
            return true;
        }
        if(id == R.id.action_ask) {
            System.out.println("******ACTION ask*********");
            Intent intent = null;
            intent = new Intent(this, MainChatActivity.class);
            this.startActivity(intent);
        }

        if(id == R.id.action_back) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        if(!SmartMuseumApp.noBeacons) {
            Log.i("MainActivity", "Start Ranging");
            this.beaconsRanging.startRanging();
        }
    }

    @Override
    protected void onPause() {

        if(!SmartMuseumApp.noBeacons) {
            Log.i("MainActivity", "Stop Ranging");
            this.beaconsRanging.stopRanging();
        }

        super.onPause();
    }

    @Override
    public void beaconsDetected(ILCMessage message) {
        List<Beacon> listOfBeaconsDetected = (List<Beacon>) message.getMessageObject();

        boolean firstTime = false;
        if (SmartMuseumApp.lastOrderingTimeStamp == null) {
            SmartMuseumApp.lastOrderingTimeStamp = new Date();
            firstTime = true;
        }
        else {
            firstTime = false;
        }

        int howManySecondsFromLastOrdering = DateTimeBusiness.howManySecondsFromDate(SmartMuseumApp.lastOrderingTimeStamp);
        Log.i("ListOfExhibitsActivity", howManySecondsFromLastOrdering + " seconds since last ordering> firstTIme: " + firstTime);

        //we reorder list only after a period of time from the last ordering;
        if (firstTime || (howManySecondsFromLastOrdering > SmartMuseumApp.exhibitsReorderingPeriod)) {
            Log.i("ListOfExhibitsActivity", howManySecondsFromLastOrdering + " seconds since last ordering> new ordering");

            SmartMuseumApp.lastOrderingTimeStamp = new Date();

            ArrayList<ExhibitModel> newSortedList = this.exhibitBusiness.getSortedExhibits(listOfBeaconsDetected);

            if (newSortedList != null && (this.dataItems == null || this.dataItems.size() == 0 || this.exhibitBusiness.hasOrderingChanged(this.dataItems, newSortedList))) {
                Log.i("ListOfExhibitsActivity", howManySecondsFromLastOrdering + " seconds since last ordering> order changed... reorder list");
                //reading data from sorted exhibitList and set it to the adapter class
                this.dataItems = newSortedList;
                //static variable to understand if the user is detecting some beacon or not;
                SmartMuseumApp.detectedSortedExhibits = this.dataItems;
                exhibitAdapter = new ExhibitModelArrayAdapter(ListOfExhibitsActivity.this, R.layout.activity_item_of_exhibits, dataItems);

                // Getting a reference to listview of activity_item_of_exhibits layout file
                listView = (ListView) findViewById(R.id.listview);
                listView.setItemsCanFocus(false);
                listView.setAdapter(exhibitAdapter);
            } else {
                Log.i("ListOfExhibitsActivity", howManySecondsFromLastOrdering + " seconds since last ordering> order did not change. Doing nothing.");
            }
        } else {
            Log.i("ListOfExhibitsActivity", howManySecondsFromLastOrdering + " seconds since last ordering> not ordering... maybe inserting a visit");
        }

        //if the user is nearer than a treshold to the nearest beacon, we store only once the visit on the DB;
        Beacon beacon = listOfBeaconsDetected.get(0); //nearest beacon;
        //we store the nearest beacon to include its information in chat messages;
        SmartMuseumApp.nearestExhibitBeacon = beacon;
        double estimatedDistance = Utils.computeAccuracy(beacon);
        Log.i("ListOfExhibitsActivity", "The nearest beacon is at " + estimatedDistance + " meters!");
        if (estimatedDistance > -1 && estimatedDistance < SmartMuseumApp.visitDistanceTreshold) {
            String key = this.beaconBusiness.getBeaconHashmapKey(beacon);
            ExhibitModel em = SmartMuseumApp.unsortedExhibits.get(key);
            if (!this.iAmWriting && em != null && SmartMuseumApp.unsortedExhibits != null && !SmartMuseumApp.todayVisitedExhibits.containsKey(key)) {
                Log.i("ListOfExhibitsActivity", "Call visit registration async");
                new ListOfExhibitsAsync(this, em, SmartMuseumApp.loggedUser).execute();
                Log.i("ListOfExhibitsActivity", "Done calling visit registration async");
                this.iAmWriting = true;
            } else {
                Log.i("ListOfExhibitsActivity", "DIDN'T WRITE: " + this.iAmWriting + ", " + SmartMuseumApp.unsortedExhibits);
            }
        }
    }

    @Override
    public void processFinish(ILCMessage message) {
        Log.i("ListOfExhibitsActivity", message.getMessageText());
        if (message.getMessageObject() != null) {
            ExhibitModel em = (ExhibitModel) message.getMessageObject();
            VisitExhibitModel visitExhibitModel = new VisitExhibitModel();
            visitExhibitModel.setExhibitModel(em);
            String key = this.exhibitBusiness.getExhibitHashmapKey(em);
            SmartMuseumApp.todayVisitedExhibits.put(key, visitExhibitModel);

            VisitExhibitModel visitExhibitModel1 = SmartMuseumApp.totalVisitedExhibits.get(key);
            ExhibitModel exhibitModel = visitExhibitModel1 != null ? visitExhibitModel1.getExhibitModel() : null;
            if(exhibitModel != null) {
                SmartMuseumApp.totalVisitedExhibits.remove(key);
                SmartMuseumApp.totalVisitedExhibits.put(key, visitExhibitModel);
            }
        }

        this.iAmWriting = false;
    }

    @Override
    public void loadUserHistoryFinish(ILCMessage message) {
        Object[] objects = (Object[]) message.getMessageObject();
        SmartMuseumApp.todayVisitedExhibits = (HashMap<String, VisitExhibitModel>) objects[0];
        SmartMuseumApp.totalVisitedExhibits = (HashMap<String, VisitExhibitModel>) objects[1];
        SmartMuseumApp.todayVisitedWorksofart = (HashMap<String, VisitWorkofartModel>) objects[2];
        SmartMuseumApp.totalVisitedWorksofart = (HashMap<String, VisitWorkofartModel>) objects[3];

        this.progressDialog.dismiss();
    }

    private void loadExhibitsNoBeacons() {
        this.dataItems = new ExhibitBusiness().getUnorderedExhibitList(SmartMuseumApp.unsortedExhibits);
        exhibitAdapter = new ExhibitModelArrayAdapter(ListOfExhibitsActivity.this, R.layout.activity_item_of_exhibits, dataItems);

        // Getting a reference to listview of activity_item_of_exhibits layout file
        listView = (ListView) findViewById(R.id.listview);
        listView.setItemsCanFocus(false);
        listView.setAdapter(exhibitAdapter);
    }
}

//class RangingTest2 {
//    static int i = 1;
//
//    private ListOfExhibitsActivity act;
//
//    public RangingTest2(ListOfExhibitsActivity a) {
//        this.act = a;
//    }
//
//    public void getSortedBeacons() {
//        ILCMessage message = new ILCMessage();
//        List<Beacon> beacons = new ArrayList<Beacon>();
//        switch (i)
//
//        {
//            case 1:
//                i = 2;
//                Beacon b1 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString("58:b0:35:f0:95:a1"), 33510, 55725, 0, 0);
//                Beacon b2 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString("AB-46-C5-4A-F8-B4"), 20512, 25367, 0, 0);
//                Beacon b3 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString("C5-62-DE-4B-39-85"), 2048, 8066, 0, 0);
//                beacons.add(b1);
//                beacons.add(b2);
//                beacons.add(b3);
//                break;
//            case 2:
//                i = 3;
//                Beacon b4 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString(""), 20512, 25367, 0, 0);
//                Beacon b5 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString(""), 33510, 55725, 0, 0);
//                Beacon b6 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString(""), 2048, 8066, 0, 0);
//                beacons.add(b4);
//                beacons.add(b5);
//                beacons.add(b6);
//                break;
//            case 3:
//                i = 1;
//                Beacon b8 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString(""), 20512, 25367, 0, 0);
//                Beacon b9 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString(""), 33510, 55725, 0, 0);
//                Beacon b7 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString(""), 2048, 8066, 0, 0);
//                beacons.add(b7);
//                beacons.add(b8);
//                beacons.add(b9);
//                break;
//        }
//
//        message.setMessageType(ILCMessage.MessageType.DEBUG);
//        message.setMessageText("TEST");
//        message.setMessageObject(beacons);
//
//        this.act.beaconsDetected(message);
//    }
//}

interface ListOfExhibitsAsyncResponse {
    void processFinish(ILCMessage message);
}

class ListOfExhibitsAsync extends AsyncTask<Void, Integer, String> {
    private UserModel userModel;
    private ExhibitModel exhibitModel;
    private VisitBusiness visitBusiness = new VisitBusiness();
    private ILCMessage message = new ILCMessage();
    private ListOfExhibitsActivity delegate;

    public ListOfExhibitsAsync(ListOfExhibitsActivity d, ExhibitModel em, UserModel um) {
        this.delegate = d;
        this.exhibitModel = em;
        this.userModel = um;
    }

    protected String doInBackground(Void... arg0) {
        VisitExhibitModel vme = new VisitExhibitModel();
        Date timestamp = new Date();
        vme.setTimeStamp(timestamp);
        vme.setExhibitModel(this.exhibitModel);
        if (this.visitBusiness.insertExhibitVisit(vme.getTimeStamp(), vme.getExhibitModel(), this.userModel)) {
            this.message.setMessageType(ILCMessage.MessageType.SUCCESS);
            this.message.setMessageText("Visit registered!");
            this.message.setMessageObject(this.exhibitModel);
            this.exhibitModel.setTimestamp(timestamp);
            this.exhibitModel.setColor("#00ffbf");
        } else {
            this.message.setMessageType(ILCMessage.MessageType.ERROR);
            this.message.setMessageText("Error registering visit!");
            this.message.setMessageObject(null);
            this.exhibitModel.setColor("#ffe6e6");
        }
        return this.message.getMessageText();
    }

    protected void onPostExecute(String result) {
        this.delegate.processFinish(this.message);
    }
}

/* Load User Exhibit History: exhibits he already visited today */
interface LoadUserHistoryAsyncResponse {
    void loadUserHistoryFinish(ILCMessage message);
}

class LoadUserHistoryAsync extends AsyncTask<Void, Integer, String> {

    private ListOfExhibitsActivity delegate;
    private ILCMessage message = new ILCMessage();

    public LoadUserHistoryAsync(ListOfExhibitsActivity d) {
        this.delegate = d;
    }

    @Override
    protected String doInBackground(Void... voids) {
        this.message.setMessageType(ILCMessage.MessageType.INFO);
        this.message.setMessageText("List of today exhibit user history");
        ExhibitBusiness exhibitBusiness = new ExhibitBusiness();
        HashMap<String, VisitExhibitModel> todayVisits = exhibitBusiness.getTodayUserExhibitVisitsHistoryMap(SmartMuseumApp.loggedUser);
        HashMap<String, VisitExhibitModel> totalVisits = exhibitBusiness.getUserExhibitVisitsHistoryMap(SmartMuseumApp.loggedUser);

        WorkofartBusiness workofartBusiness = new WorkofartBusiness();
        HashMap<String, VisitWorkofartModel> todayWorkofartsVisits = workofartBusiness.getTodayUserWorksofartHistoryHashMap(SmartMuseumApp.loggedUser);
        HashMap<String, VisitWorkofartModel> totalWorkofartsVisits = workofartBusiness.getTotalUserWorksofartHistoryHashMap(SmartMuseumApp.loggedUser);

        Object[] objects = new Object[] {todayVisits, totalVisits, todayWorkofartsVisits, totalWorkofartsVisits};
        this.message.setMessageObject(objects);
        return this.message.getMessageText();
    }

    @Override
    protected void onPostExecute(String result) {
        this.delegate.loadUserHistoryFinish(this.message);
    }
}

