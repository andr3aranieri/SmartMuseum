package it.sapienza.pervasivesystems.smartmuseum.view;

import android.app.ProgressDialog;
import android.content.Intent;
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
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.WorkofartBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.model.adapter.VisitWorkofartModelArrayAdapter;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitWorkofartModel;
import it.sapienza.pervasivesystems.smartmuseum.view.slack.gui.MainChatActivity;

public class ListOfUHObjectsActivity extends AppCompatActivity implements ListOfUHWorksofartActivityLoadUserHistoryAsyncResponse {

    List<VisitWorkofartModel> dataItems = null;
    private ProgressDialog progressDialog;
    VisitWorkofartModelArrayAdapter visitWorkofartModelArrayAdapter;
    private ListView listView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_uhobjects);

        Intent mIntent = getIntent();
        int exhibitId = mIntent.getIntExtra("exhibitId", 0);

        this.progressDialog = new ProgressDialog(ListOfUHObjectsActivity.this, R.style.AppTheme_Dark_Dialog);
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setMessage("Loading User History (Objects)... Please wait.");
        this.progressDialog.show();

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        new ListOfUHWorksofartActivityLoadUserHistoryAsync(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        toolbar.getMenu().findItem(R.id.exhibition_list).setVisible(true);
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
            //TODO
            System.out.println("******ACTION logout*********");
            return true;
        }
        if(id == R.id.action_ask) {
            Intent intent = null;
            intent = new Intent(this, MainChatActivity.class);
            this.startActivity(intent);
        }

        if (id == R.id.exhibition_list) {
            Intent intent = null;
            intent = new Intent(this, ListOfUHExhibitsActivity.class);
            this.startActivity(intent);
        }

        if(id == R.id.action_back) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void loadUserHistoryFinish(ILCMessage message) {
        this.dataItems = (List< VisitWorkofartModel>) message.getMessageObject();
        visitWorkofartModelArrayAdapter = new VisitWorkofartModelArrayAdapter(ListOfUHObjectsActivity.this, R.layout.activity_workofart_visit_item, dataItems);

        // Getting a reference to listview of activity_item_of_exhibits layout file
        listView = (ListView) findViewById(R.id.listview);
        listView.setItemsCanFocus(false);
        listView.setAdapter(visitWorkofartModelArrayAdapter);

        this.progressDialog.dismiss();
    }
}

/* Load User Exhibit History: exhibits he already visited today */
interface ListOfUHWorksofartActivityLoadUserHistoryAsyncResponse {
    void loadUserHistoryFinish(ILCMessage message);
}

class ListOfUHWorksofartActivityLoadUserHistoryAsync extends AsyncTask<Void, Integer, String> {

    private ListOfUHWorksofartActivityLoadUserHistoryAsyncResponse delegate;
    private ILCMessage message = new ILCMessage();

    public ListOfUHWorksofartActivityLoadUserHistoryAsync(ListOfUHWorksofartActivityLoadUserHistoryAsyncResponse d) {
        this.delegate = d;
    }

    @Override
    protected String doInBackground(Void... voids) {
        this.message.setMessageType(ILCMessage.MessageType.INFO);
        this.message.setMessageText("List of total exhibit user history");
        WorkofartBusiness workofartBusiness = new WorkofartBusiness();
        List<VisitWorkofartModel> totalVisits = workofartBusiness.getUserWorkofartHistoryList(SmartMuseumApp.loggedUser);
        this.message.setMessageObject(totalVisits);
        return this.message.getMessageText();
    }

    @Override
    protected void onPostExecute(String result) {
        this.delegate.loadUserHistoryFinish(this.message);
    }
}

