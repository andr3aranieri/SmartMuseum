package it.sapienza.pervasivesystems.smartmuseum.business.slack.actions;

import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.replies.GenericSlackReply;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrearanieri on 02/06/16.
 */
public class CreateChannel {

    public String createChannel(SlackSession session, String channelName) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("name", channelName);
        String command = "channels.create";

        SlackMessageHandle<GenericSlackReply> replySlackMessageHandle = session.postGenericSlackCommand(parameters, command);
        return "CREATED";
    }
}
