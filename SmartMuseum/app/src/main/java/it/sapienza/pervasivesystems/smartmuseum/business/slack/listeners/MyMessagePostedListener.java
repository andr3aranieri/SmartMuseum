package it.sapienza.pervasivesystems.smartmuseum.business.slack.listeners;

import android.util.Log;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

/**
 * Created by andrearanieri on 27/06/16.
 */

public class MyMessagePostedListener implements SlackMessagePostedListener {
    @Override
    public void onEvent(SlackMessagePosted event, SlackSession session) {
        Log.i("SLACKMESSAGE", "**************");
        Log.i("SLACKMESSAGE", event.getMessageContent());
        Log.i("SLACKMESSAGE", "**************");
    }
}
