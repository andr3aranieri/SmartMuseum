package it.sapienza.pervasivesystems.smartmuseum.view;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ListView;

import java.util.ArrayList;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.model.adapter.ExhibitModelArrayAdapter;
import it.sapienza.pervasivesystems.smartmuseum.model.db.ExhibitDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;

public class ListOfUHExhibitsActivity extends AppCompatActivity {

    private ListView listView;
    ExhibitModelArrayAdapter exhibitAdapter;
    ArrayList<ExhibitModel> dataItems = null;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_uh_exhibits);

        //TODO getUserExhibitHistory iig duudna
        this.dataItems = (ArrayList<ExhibitModel>) new ExhibitDB().getSortedExhibitList();
        exhibitAdapter = new ExhibitModelArrayAdapter(ListOfUHExhibitsActivity.this, R.layout.activity_item_of_exhibits, dataItems);

        // Getting a reference to listview of activity_item_of_exhibits layout file
        listView = (ListView) findViewById(R.id.listview);
        listView.setItemsCanFocus(false);
        listView.setAdapter(exhibitAdapter);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
