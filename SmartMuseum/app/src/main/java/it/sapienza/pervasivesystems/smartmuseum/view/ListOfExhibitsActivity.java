package it.sapienza.pervasivesystems.smartmuseum.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.model.adapter.ExhibitModelArrayAdapter;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;


public class ListOfExhibitsActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private ListView listView;
    ExhibitModelArrayAdapter exhibitAdapter;
    ArrayList<ExhibitModel> dataItems = new ArrayList<ExhibitModel>();

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


    }

}

