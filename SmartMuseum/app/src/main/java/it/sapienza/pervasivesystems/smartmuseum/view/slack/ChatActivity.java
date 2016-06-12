package it.sapienza.pervasivesystems.smartmuseum.view.slack;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import java.io.IOException;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.SlackBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;

public class ChatActivity extends AppCompatActivity implements ChatAsyncResponse {

    static public String channelToLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent mIntent = getIntent();
        channelToLoad = mIntent.getStringExtra("channelToLoad");

        new ChatAsync(this, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.OPEN_SESSION).execute();
    }

    @Override
    public void onStop(){
        super.onStop();

        new ChatAsync(this, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.CLOSE_SESSION).execute();
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//
//        new ChatAsync(this, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.OPEN_SESSION).execute();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        new ChatAsync(this, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.CLOSE_SESSION).execute();
//    }

    @Override
    public void sessionOpened(ILCMessage message) {
        Log.i("CHATACTIVITY", message.getMessageText());

        new ChatAsync(this, SmartMuseumApp.loggedUser, SlackBusiness.SlackCommand.DOWNLOAD_MESSAGES).execute();
    }

    @Override
    public void sessionClosed(ILCMessage message) {
        Log.i("CHATACTIVITY", message.getMessageText());
    }

    @Override
    public void messagesDownloaed(ILCMessage message) {
        Log.i("CHATACTIVITY", message.getMessageText());

        List<SlackMessagePosted> messages = (List<SlackMessagePosted>) message.getMessageObject();
        for(SlackMessagePosted m: messages) {
            Log.i("CHATACTIVITY", m.getMessageContent());
        }
    }
}

interface ChatAsyncResponse {
    void sessionOpened(ILCMessage message);
    void sessionClosed(ILCMessage message);
    void messagesDownloaed(ILCMessage message);
}

class ChatAsync extends AsyncTask<Void, Integer, String> {

    private SlackBusiness.SlackCommand command;
    private UserModel userModel;
    private ChatActivity delegate;
    private ILCMessage message = new ILCMessage();
    private SlackBusiness slackBusiness = new SlackBusiness();

    public ChatAsync(ChatActivity ca, UserModel um, SlackBusiness.SlackCommand c) {
        this.delegate = ca;
        this.userModel = um;
        this.command = c;
    }

    @Override
    protected String doInBackground(Void... voids) {
        switch(this.command) {
            case OPEN_SESSION:
                //slack session creation;
                try {
                    SmartMuseumApp.slackSession = new SlackBusiness().createSession(SlackBusiness.token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.message.setMessageType(ILCMessage.MessageType.SUCCESS);
                this.message.setMessageObject(null);
                this.message.setMessageText("Slack session opened");
                break;
            case CLOSE_SESSION:
                try {
                    SmartMuseumApp.slackSession.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.message.setMessageType(ILCMessage.MessageType.SUCCESS);
                this.message.setMessageObject(null);
                this.message.setMessageText("Slack session closed");
                break;
            case DOWNLOAD_MESSAGES:
                List<SlackMessagePosted> messages = this.slackBusiness.getMessagesInChannel(SmartMuseumApp.slackSession, ChatActivity.channelToLoad, 100);
                this.message.setMessageType(ILCMessage.MessageType.SUCCESS);
                this.message.setMessageObject(messages);
                this.message.setMessageText("Messages Downloaded");
                break;
        }

        return null;
    }

    protected void onPostExecute(String result) {
        switch(this.command) {
            case OPEN_SESSION:
                this.delegate.sessionOpened(this.message);
                break;
            case CLOSE_SESSION:
                this.delegate.sessionClosed(this.message);
                break;
            case DOWNLOAD_MESSAGES:
                this.delegate.messagesDownloaed(this.message);
                break;
        }
    }
}

