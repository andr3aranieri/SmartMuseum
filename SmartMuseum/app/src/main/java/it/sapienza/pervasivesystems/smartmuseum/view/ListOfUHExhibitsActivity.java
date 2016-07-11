package it.sapienza.pervasivesystems.smartmuseum.view;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.model.adapter.VisitExhibitModelArrayAdapter;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitExhibitModel;

public class ListOfUHExhibitsActivity extends AppCompatActivity implements ListOfUHExhibitsActivityLoadUserHistoryAsyncResponse {

    private ListView listView;
    VisitExhibitModelArrayAdapter exhibitVisitsAdapter;
    List<VisitExhibitModel> dataItems = null;
    private ProgressDialog progressDialog;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_uh_exhibits);

        this.progressDialog = new ProgressDialog(ListOfUHExhibitsActivity.this, R.style.AppTheme_Dark_Dialog);
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setMessage("Loading User History... Please wait.");
        this.progressDialog.show();

        new ListOfUHExhibitsActivityLoadUserHistoryAsync(this).execute();

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

    @Override
    public void loadUserHistoryFinish(ILCMessage message) {
        //TODO getUserExhibitHistory iig duudna
        this.dataItems = (List<VisitExhibitModel>) message.getMessageObject();
        exhibitVisitsAdapter = new VisitExhibitModelArrayAdapter(ListOfUHExhibitsActivity.this, R.layout.activity_item_of_exhibits, dataItems);

        // Getting a reference to listview of activity_item_of_exhibits layout file
        listView = (ListView) findViewById(R.id.listview);
        listView.setItemsCanFocus(false);
        listView.setAdapter(exhibitVisitsAdapter);

        this.progressDialog.dismiss();
    }
}

/* Load User Exhibit History: exhibits he already visited today */
interface ListOfUHExhibitsActivityLoadUserHistoryAsyncResponse {
    void loadUserHistoryFinish(ILCMessage message);
}

class ListOfUHExhibitsActivityLoadUserHistoryAsync extends AsyncTask<Void, Integer, String> {

    private ListOfUHExhibitsActivityLoadUserHistoryAsyncResponse delegate;
    private ILCMessage message = new ILCMessage();

    public ListOfUHExhibitsActivityLoadUserHistoryAsync(ListOfUHExhibitsActivityLoadUserHistoryAsyncResponse d) {
        this.delegate = d;
    }

    @Override
    protected String doInBackground(Void... voids) {
        this.message.setMessageType(ILCMessage.MessageType.INFO);
        this.message.setMessageText("List of total exhibit user history");
        ExhibitBusiness exhibitBusiness = new ExhibitBusiness();
        List<VisitExhibitModel> totalVisits = exhibitBusiness.getUserExhibitHistoryList(SmartMuseumApp.loggedUser);
        this.message.setMessageObject(totalVisits);
        return this.message.getMessageText();
    }

    @Override
    protected void onPostExecute(String result) {
        this.delegate.loadUserHistoryFinish(this.message);
    }
}


