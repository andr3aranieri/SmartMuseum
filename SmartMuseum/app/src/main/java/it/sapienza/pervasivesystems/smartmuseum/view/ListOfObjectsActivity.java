package it.sapienza.pervasivesystems.smartmuseum.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.InternalStorage.FileSystemBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.WorkofartBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.model.adapter.WorkOfArtModelAdapter;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;
import it.sapienza.pervasivesystems.smartmuseum.view.slack.gui.MainChatActivity;

public class ListOfObjectsActivity extends AppCompatActivity implements ListOfObjectsActivityAsyncResponse
{

    private WorkofartBusiness workofartBusiness = new WorkofartBusiness();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_objects);

        Intent mIntent = getIntent();
        String exhibitKey = mIntent.getStringExtra("exhibitId");
        new ListOfObjectsActivityAsync(this, new ExhibitBusiness().getExhibitDetail(exhibitKey)).execute();

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.createThreadChatNotification();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            try {
                new FileSystemBusiness(this).deleteFile(SmartMuseumApp.localLoginFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
        }
        if(id == R.id.action_ask) {
            SmartMuseumApp.newMessageRead = true;
            Intent intent = new Intent(this, MainChatActivity.class);
            this.startActivity(intent);
        }

        if(id == R.id.action_back) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
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

    private void createThreadChatNotification() {
        final ListOfObjectsActivity parentActivity = this;
        final Handler handler=new Handler();
        handler.post(new Runnable(){
            @Override
            public void run() {
                // upadte textView here
                handler.postDelayed(this,5000); // set time here to refresh textView

                MenuItem item = toolbar.getMenu().findItem(R.id.action_ask);

                if(SmartMuseumApp.newMessage) {
                    SmartMuseumApp.newMessageRead = false;
                    item.setIcon(R.drawable.ic_chat_notification2);
                }
                else {
                    if(!SmartMuseumApp.newMessageRead)
                        item.setIcon(R.drawable.ic_chat_notification2);
                    else
                        item.setIcon(R.drawable.chat_icon);
                }
            }
        });
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
