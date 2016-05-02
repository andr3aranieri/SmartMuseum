package it.sapienza.pervasivesystems.smartmuseum.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.MacAddress;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.ArrayList;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.business.beacons.Ranging;
import it.sapienza.pervasivesystems.smartmuseum.business.beacons.RangingDetection;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.model.adapter.ExhibitModelArrayAdapter;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;


public class ListOfExhibitsActivity extends AppCompatActivity implements RangingDetection {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private ListView listView;
    ExhibitModelArrayAdapter exhibitAdapter;
    ArrayList<ExhibitModel> dataItems = new ArrayList<ExhibitModel>();
    private Ranging beaconsRanging;
    private ExhibitBusiness exhibitBusiness = new ExhibitBusiness();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_exhibits);

        //TODO
        //Reading sorted exhibits from db and assign it to the dataItems instance variable

        exhibitAdapter = new ExhibitModelArrayAdapter(ListOfExhibitsActivity.this, R.layout.activity_item_of_exhibits, dataItems);

        // Getting a reference to listview of main.xml layout file
        listView = (ListView) findViewById(R.id.listview);
        listView.setItemsCanFocus(false);
        listView.setAdapter(exhibitAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {

                System.out.println("***pos : " + position);
            }

        });

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
            this.dataItems = newSortedList;

            Log.i("ListOfExhibitsActivity", "List Sorting...");
            //REFRESH THE EXHIBIT LIST HERE
            Log.i("ListOfExhibitsActivity", "List Sorted");
        }
    }

    private void buttonToTestBeaconsSorting() {
        new RangingTest2(this).getSortedBeacons();
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
        switch(i)

        {
            case 1:
                i = 2;
                Beacon b1 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString(""), 33510, 55725, 0, 0);
                Beacon b2 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString(""), 20512, 25367, 0, 0);
                Beacon b3 = new Beacon(new java.util.UUID(new Long(1), new Long(1)), MacAddress.fromString(""), 2048, 8066, 0, 0);
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

