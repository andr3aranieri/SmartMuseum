package it.sapienza.pervasivesystems.smartmuseum.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.WorkofartBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.model.adapter.VisitWorkofartModelArrayAdapter;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitWorkofartModel;

public class ListOfUHObjectsActivity extends AppCompatActivity implements ListOfUHWorksofartActivityLoadUserHistoryAsyncResponse {

    List<VisitWorkofartModel> dataItems = null;
    private ProgressDialog progressDialog;
    VisitWorkofartModelArrayAdapter visitWorkofartModelArrayAdapter;
    private ListView listView;

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

        new ListOfUHWorksofartActivityLoadUserHistoryAsync(this).execute();
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

