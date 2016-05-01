package it.sapienza.pervasivesystems.smartmuseum.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.estimote.sdk.Beacon;
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

        if(this.exhibitBusiness.hasOrderingChanged(this.dataItems, newSortedList)) {
            this.dataItems = newSortedList;

            Log.i("ListOfExhibitsActivity", "List Sorting...");
            //REFRESH THE EXHIBIT LIST HERE
            Log.i("ListOfExhibitsActivity", "List Sorted");
        }

    }
}

