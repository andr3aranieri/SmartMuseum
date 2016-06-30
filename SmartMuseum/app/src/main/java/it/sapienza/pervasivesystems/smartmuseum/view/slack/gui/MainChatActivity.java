package it.sapienza.pervasivesystems.smartmuseum.view.slack.gui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import java.util.ArrayList;

import butterknife.ButterKnife;
import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.SlackBusiness;
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
    private ChatAsync chatAsyncPushMessages;
    private ArrayList<SlackMessagePosted> oldMessages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_chat);

        /****** LOADING OLD MESSAGES**********/
        ButterKnife.bind(this);
        Intent mIntent = getIntent();
        channelToLoad = mIntent.getStringExtra("channelToLoad");

        this.showProgressPopup("Loading messages. Please wait...");

        //Loading messages;
        new ChatAsync(this, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.DOWNLOAD_MESSAGES, channelToLoad, "").execute();

        /*****SLACK CHANNEL MESSAGES PUSH********/
//        this.chatAsyncPushMessages = new ChatAsync(this, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.PUSH_MESSAGES, channelToLoad, "");
//        this.chatAsyncPushMessages.setPushMessages(true);
//        this.chatAsyncPushMessages.execute();

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

        //TODO ANDREA: slack message posted listener not working
//        this.slackBusiness.startListener(channelToLoad, "andrea_visitor");
    }

    private boolean sendChatMessage() {
        chatArrayAdapter.add(new ChatMessage(false, chatText.getText().toString()));
        //send message to slack;
        new ChatAsync(this, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.SEND_MESSAGE, channelToLoad, chatText.getText().toString()).execute();
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
        this.chatAsyncPushMessages.setPushMessages(false);
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

        if(oldMessages == null) {
            reloadMessages = true;
        }
        else if(messages != null && !messages.get(messages.size()-1).getMessageContent().equals(oldMessages.get(oldMessages.size()-1).getMessageContent())) {
            reloadMessages = true;
        }
        else {
            reloadMessages = false;
        }

        if (reloadMessages) {
            oldMessages = messages;

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

        //hide progress popup;
        this.progressDialog.dismiss();
    }

    @Override
    public void messageSent(ILCMessage message) {
        //hide progress popup;
        this.progressDialog.dismiss();
    }

    @Override
    public void messagesPushed(final ILCMessage message) {
        Log.i("CHATACTIVITY2", message.getMessageText());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<SlackMessagePosted> messages = (ArrayList<SlackMessagePosted>) message.getMessageObject();
                ArrayList<ChatMessage> chatMessages = new SlackBusiness().convertSlackMessages2(messages);
                if (messages != null) {

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
            }
        });
    }
}

