package it.sapienza.pervasivesystems.smartmuseum.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.WorkofartBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.model.adapter.WorkOfArtModelAdapter;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;

public class ListOfObjectsActivity extends AppCompatActivity implements ListOfObjectsActivityAsyncResponse
{

    private WorkofartBusiness workofartBusiness = new WorkofartBusiness();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_objects);

        Intent mIntent = getIntent();
        String exhibitKey = mIntent.getStringExtra("exhibitId");
        new ListOfObjectsActivityAsync(this, new ExhibitBusiness().getExhibitDetail(exhibitKey)).execute();

//        ArrayList<WorkofartModel> workofartModels = new WorkofartBusiness().getWorkOfArtListFAKE();
//        GridView gridview = (GridView) findViewById(R.id.gridView);
//        gridview.setAdapter(new WorkOfArtModelAdapter(this, workofartModels));

    }

    @Override
    public void processFinish(ILCMessage message) {
        switch(message.getMessageType()) {
            case SUCCESS:
                Log.i("ListOfObjects", "*****************************");
                SmartMuseumApp.workofartModelHashMap = (HashMap<String, WorkofartModel>) message.getMessageObject();
                ArrayList<WorkofartModel> l = this.workofartBusiness.getWorksofartList(SmartMuseumApp.workofartModelHashMap);
                for(WorkofartModel w: l) {
                    Log.i("ListOfObjects", w.getIdWork() + ", " + w.getImage() + ", " + w.getShortDescription() + ", " + w.getAudioURL());
                }

                ArrayList<WorkofartModel> workofartModels = this.workofartBusiness.getWorksofartList(SmartMuseumApp.workofartModelHashMap);

                GridView gridview = (GridView) findViewById(R.id.gridView);
                gridview.setAdapter(new WorkOfArtModelAdapter(this, workofartModels));

                Log.i("ListOfObjects", "*****************************");
                break;
            case ERROR:
                break;
        }
    }
}

interface ListOfObjectsActivityAsyncResponse {
    void processFinish(ILCMessage message);
}

class ListOfObjectsActivityAsync extends AsyncTask<Void, Integer, String> {
    private ListOfObjectsActivityAsyncResponse delegate;
    private ILCMessage message = new ILCMessage();
    private WorkofartBusiness workofartBusiness = new WorkofartBusiness();
    private ExhibitModel exhibit;

    public ListOfObjectsActivityAsync(ListOfObjectsActivityAsyncResponse d, ExhibitModel key) {
        this.delegate = d;
        this.exhibit = key;
    }

    @Override
    protected String doInBackground(Void... voids) {
        HashMap<String, WorkofartModel> works = workofartBusiness.getWorkofarts(this.exhibit);
        if(works != null) {
            this.message.setMessageType(ILCMessage.MessageType.SUCCESS);
            this.message.setMessageText("Works of art successfully retrieved from DB");
            this.message.setMessageObject(works);
        } else {
            this.message.setMessageType(ILCMessage.MessageType.ERROR);
            this.message.setMessageText("Works of art not retrieved");
            this.message.setMessageObject(null);
        }

        return null;
    }

    protected void onPostExecute(String result) {
        this.delegate.processFinish(this.message);
    }
}
