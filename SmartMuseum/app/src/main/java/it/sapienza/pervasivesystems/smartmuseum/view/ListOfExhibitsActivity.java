package it.sapienza.pervasivesystems.smartmuseum.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.MacAddress;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.Utils;

import java.util.ArrayList;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.beacons.BeaconBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.beacons.Ranging;
import it.sapienza.pervasivesystems.smartmuseum.business.beacons.RangingDetection;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.business.visits.VisitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.adapter.ExhibitModelArrayAdapter;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;


public class ListOfExhibitsActivity extends AppCompatActivity implements RangingDetection, ListOfExhibitsAsyncResponse {

    private ListView listView;
    ExhibitModelArrayAdapter exhibitAdapter;
    ArrayList<ExhibitModel> dataItems = new ArrayList<ExhibitModel>();
    private Ranging beaconsRanging;
    private ExhibitBusiness exhibitBusiness = new ExhibitBusiness();
    private BeaconBusiness beaconBusiness = new BeaconBusiness();
    private VisitBusiness visitBusiness = new VisitBusiness();
    private boolean iAmWriting = false;

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


        //start ranging;
        this.beaconsRanging = new Ranging(this);
        Ranging.rangingDetection = this;
        this.beaconsRanging.initRanging();

    }


    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        Log.i("MainActivity", "Start Ranging");
        this.beaconsRanging.startRanging();
    }

    @Override
    protected void onPause() {
        Log.i("MainActivity", "Stop Ranging");
        this.beaconsRanging.stopRanging();

        super.onPause();
    }

    @Override
    public void beaconsDetected(ILCMessage message) {
        Log.i("ListOfExhibitsActivity", message.getMessageText());
        List<Beacon> listOfBeaconsDetected = (List<Beacon>) message.getMessageObject();

        ArrayList<ExhibitModel> newSortedList = this.exhibitBusiness.getSortedExhibits(listOfBeaconsDetected);

        if (this.exhibitBusiness.hasOrderingChanged(this.dataItems, newSortedList)) {

            //reading data from sorted exhibitList and set it to the adapter class
            this.dataItems = newSortedList;
            exhibitAdapter = new ExhibitModelArrayAdapter(ListOfExhibitsActivity.this, R.layout.activity_item_of_exhibits, dataItems);

            // Getting a reference to listview of activity_item_of_exhibits layout file
            listView = (ListView) findViewById(R.id.listview);
            listView.setItemsCanFocus(false);
            listView.setAdapter(exhibitAdapter);

//            Log.i("ListOfExhibitsActivity", "List Sorting...");
            //REFRESH THE EXHIBIT LIST HERE
//            Log.i("ListOfExhibitsActivity", "List Sorted");
        }

        //if the user is nearer than a treshold to the nearest beacon, we store only once the visit on the DB;
        Beacon beacon = listOfBeaconsDetected.get(0); //nearest beacon;
        double estimatedDistance = Utils.computeAccuracy(beacon);
//        Log.i("ListOfExhibitsActivity", "The nearest beacon is at " + estimatedDistance + " meters!");
        if (estimatedDistance > -1 && estimatedDistance < SmartMuseumApp.visitDistanceTreshold) {
            String key = this.beaconBusiness.getBeaconHashmapKey(beacon);
            ExhibitModel em = SmartMuseumApp.unsortedExhibits.get(key);
            if (!this.iAmWriting && em != null && SmartMuseumApp.unsortedExhibits != null && !SmartMuseumApp.visitedExhibits.containsKey(key)) {
                Log.i("ListOfExhibitsActivity", "Call visit registration async");
                new ListOfExhibitsAsync(this, em, SmartMuseumApp.loggedUser).execute();
                Log.i("ListOfExhibitsActivity", "Done calling visit registration async");
                this.iAmWriting = true;
            }
        }
    }


    @Override
    public void processFinish(ILCMessage message) {
        Log.i("ListOfExhibitsActivity", message.getMessageText());
        if (message.getMessageObject() != null) {
            ExhibitModel em = (ExhibitModel) message.getMessageObject();
            SmartMuseumApp.visitedExhibits.put(this.exhibitBusiness.getExhibitHashmapKey(em), em);
        }

        this.iAmWriting = false;
    }
}

class RangingTest2 {
    static int i = 1;

    private ListOfExhibitsActivity act;

    public RangingTest2(ListOfExhibitsActivity a) {
        this.act = a;
    }

    public void getSortedBeacons() {
        ILCMessage message = new ILCMessage();
        List<Beacon> beacons = new ArrayList<Beacon>();
        switch (i)

        {
            case 1:
                i = 2;
                Beacon b1 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString("58:b0:35:f0:95:a1"), 33510, 55725, 0, 0);
                Beacon b2 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString("AB-46-C5-4A-F8-B4"), 20512, 25367, 0, 0);
                Beacon b3 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString("C5-62-DE-4B-39-85"), 2048, 8066, 0, 0);
                beacons.add(b1);
                beacons.add(b2);
                beacons.add(b3);
                break;
            case 2:
                i = 3;
                Beacon b4 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString(""), 20512, 25367, 0, 0);
                Beacon b5 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString(""), 33510, 55725, 0, 0);
                Beacon b6 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString(""), 2048, 8066, 0, 0);
                beacons.add(b4);
                beacons.add(b5);
                beacons.add(b6);
                break;
            case 3:
                i = 1;
                Beacon b8 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString(""), 20512, 25367, 0, 0);
                Beacon b9 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString(""), 33510, 55725, 0, 0);
                Beacon b7 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString(""), 2048, 8066, 0, 0);
                beacons.add(b7);
                beacons.add(b8);
                beacons.add(b9);
                break;
        }

        message.setMessageType(ILCMessage.MessageType.DEBUG);
        message.setMessageText("TEST");
        message.setMessageObject(beacons);

        this.act.beaconsDetected(message);
    }
}

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
        if (this.visitBusiness.insertExhibitVisit(this.exhibitModel, this.userModel)) {
            this.message.setMessageType(ILCMessage.MessageType.SUCCESS);
            this.message.setMessageText("Visit registered!");
            this.message.setMessageObject(this.exhibitModel);
        } else {
            this.message.setMessageType(ILCMessage.MessageType.ERROR);
            this.message.setMessageText("Error registering visit!");
            this.message.setMessageObject(null);
        }
        return this.message.getMessageText();
    }

    protected void onPostExecute(String result) {
        this.delegate.processFinish(this.message);
    }
}

