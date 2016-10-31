package it.sapienza.pervasivesystems.smartmuseum.view.slack.gui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.InternalStorage.FileSystemBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.SlackBusiness;
import it.sapienza.pervasivesystems.smartmuseum.view.ListOfUHExhibitsActivity;
import it.sapienza.pervasivesystems.smartmuseum.view.ListOfUHObjectsActivity;
import it.sapienza.pervasivesystems.smartmuseum.view.LoginActivity;
import it.sapienza.pervasivesystems.smartmuseum.view.slack.ChatAsync;
import it.sapienza.pervasivesystems.smartmuseum.view.slack.ChatAsyncResponse;

public class MainChatActivity extends AppCompatActivity implements ChatAsyncResponse {

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    static public String channelToLoad;
    private ProgressDialog progressDialog;
    private SlackBusiness slackBusiness = new SlackBusiness();
    private ArrayList<ChatMessage> oldMessages;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_chat);

        /****** LOADING OLD MESSAGES**********/
        ButterKnife.bind(this);
        channelToLoad = SmartMuseumApp.loggedUser.getSlackChannel().getChannelName();

        this.showProgressPopup("Loading messages. Please wait...");

        //Loading messages;
        new ChatAsync(this, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.DOWNLOAD_MESSAGES, channelToLoad, "").execute();

        /*****SLACK CHANNEL MESSAGES PUSH********/
        final MainChatActivity parentActivity = this;
        final Handler handler=new Handler();
        handler.post(new Runnable(){
            @Override
            public void run() {
                // upadte textView here
                handler.postDelayed(this,5000); // set time here to refresh textView
                new ChatAsync(parentActivity, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.DOWNLOAD_MESSAGES, MainChatActivity.channelToLoad, "").execute();
            }
        });
        /*************************************/

        buttonSend = (Button) findViewById(R.id.send);

        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        toolbar.getMenu().findItem(R.id.action_ask).setVisible(false);
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
            try {
                new FileSystemBusiness(this).deleteFile(SmartMuseumApp.localLoginFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
        }
        if(id == R.id.action_back) {
            super.onBackPressed();
            SmartMuseumApp.newMessageRead = true;
        }

        if (id == R.id.exhibition_list) {
            Intent intent = new Intent(this, ListOfUHExhibitsActivity.class);
            this.startActivity(intent);
        }

        if (id == R.id.work_of_art_list) {
            Intent intent = new Intent(this, ListOfUHObjectsActivity.class);
            this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean sendChatMessage() {
        String messageShowToUser = chatText.getText().toString();
        String messageToShowToExpert = SmartMuseumApp.getNearestExhibitDescription() + "\n" + chatText.getText().toString();
        chatArrayAdapter.add(new ChatMessage(false, messageShowToUser));
        //send message to slack;
        new ChatAsync(this, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.SEND_MESSAGE, channelToLoad, messageToShowToExpert).execute();
        chatText.setText("");
        this.showProgressPopup("Sending. Please wait...");
        return true;
    }

    private void showProgressPopup(String message) {
        //show progress popup
        this.progressDialog = new ProgressDialog(MainChatActivity.this,
                R.style.AppTheme_Dark_Dialog);
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setMessage(message);
        this.progressDialog.show();
    }

    @Override
    public void onBackPressed() {
        //stop the async channel messages downloading;
        SmartMuseumApp.newMessageRead = true;
        return;
    }

    @Override
    public void sessionOpened(ILCMessage message) {
        Log.i("CHATACTIVITY2", message.getMessageText());
    }

    @Override
    public void sessionClosed(ILCMessage message) {
        Log.i("CHATACTIVITY2", message.getMessageText());
    }

    @Override
    public void messagesDownloaed(ILCMessage message) {
        Log.i("CHATACTIVITY2", message.getMessageText());

        ArrayList<SlackMessagePosted> messages = (ArrayList<SlackMessagePosted>) message.getMessageObject();
        ArrayList<ChatMessage> chatMessages = this.slackBusiness.convertSlackMessages2(messages);

        boolean reloadMessages = false;

        if(oldMessages == null || (oldMessages.size() == 0 && chatMessages.size() > 0)) {
            reloadMessages = true;
        }
        else if(chatMessages != null && chatMessages.size() > 0 && !chatMessages.get(chatMessages.size()-1).message.equals(oldMessages.get(oldMessages.size()-1).message)) {
            reloadMessages = true;
        }
        else {
            reloadMessages = false;
        }

        if (reloadMessages) {
            oldMessages = chatMessages;

            Log.i("CHATACTIVITY2", "LOAD MESSAGES");
            // Getting a reference to listview of activity_item_of_exhibits layout file
            listView = (ListView) findViewById(R.id.msgview);
            chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_right_chat, chatMessages);
            listView.setAdapter(chatArrayAdapter);
            listView.setItemsCanFocus(false);
            listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

            //to scroll the list view to bottom on data change
            chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    listView.setSelection(chatArrayAdapter.getCount() - 1);
                }
            });
        }
        else {
            Log.i("CHATACTIVITY2", "Dont LOAD MESSAGES");
        }

        SmartMuseumApp.newMessage = reloadMessages;

        //hide progress popup;
        this.progressDialog.dismiss();
    }

    @Override
    public void messageSent(ILCMessage message) {
        //hide progress popup;
        this.progressDialog.dismiss();
    }

    @Override
    public void channelCreated(ILCMessage message) {

    }

    @Override
    public void channelListFetched(ILCMessage message) {

    }
}

