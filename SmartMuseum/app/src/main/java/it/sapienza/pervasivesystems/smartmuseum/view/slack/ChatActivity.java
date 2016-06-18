package it.sapienza.pervasivesystems.smartmuseum.view.slack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.SlackBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.adapter.ChatModelArrayAdapter;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ChatModel;

public class ChatActivity extends AppCompatActivity implements ChatAsyncResponse {

    static public String channelToLoad;
    private ProgressDialog progressDialog;
    private ChatModelArrayAdapter chatModelArrayAdapter = null;
    private SlackBusiness slackBusiness = new SlackBusiness();
    private ListView listView;

    @Bind(R.id.btn_sendMessage)
    Button sendButton;
    @Bind(R.id.input_message)
    EditText inputMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Intent mIntent = getIntent();
        channelToLoad = mIntent.getStringExtra("channelToLoad");

        this.showProgressPopup("Loading messages. Please wait...");

        //Loading messages;
        new ChatAsync(this, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.DOWNLOAD_MESSAGES, "channelslack1", "").execute();

        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void showProgressPopup(String message) {
        //show progress popup
        this.progressDialog = new ProgressDialog(ChatActivity.this,
                R.style.AppTheme_Dark_Dialog);
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setMessage(message);
        this.progressDialog.show();
    }

    public void sendMessage() {

        this.showProgressPopup("Sending. Please wait...");

        //send message;
        new ChatAsync(this, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.SEND_MESSAGE, "channelslack1", inputMessage.getText().toString()).execute();
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
        ArrayList<ChatModel> chatModels = this.slackBusiness.convertSlackMessages(messages);

        if(messages != null) {
            // Getting a reference to listview of activity_item_of_exhibits layout file
            chatModelArrayAdapter = new ChatModelArrayAdapter(this, R.layout.activity_item_of_chat, chatModels);

            listView = (ListView) findViewById(R.id.listview);
            listView.setItemsCanFocus(false);
            listView.setAdapter(chatModelArrayAdapter);
        }

        //hide progress popup;
        progressDialog.dismiss();
    }
}


