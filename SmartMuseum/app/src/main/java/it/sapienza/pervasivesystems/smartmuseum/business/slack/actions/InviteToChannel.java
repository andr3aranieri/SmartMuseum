package it.sapienza.pervasivesystems.smartmuseum.business.slack.actions;

import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.replies.GenericSlackReply;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrearanieri on 29/06/16.
 */
public class InviteToChannel {

    public boolean inviteToChannel(SlackSession session, String channelName, String botUserID) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("channel", channelName);
        parameters.put("user", botUserID);
        String command = "channels.invite";

        SlackMessageHandle<GenericSlackReply> replySlackMessageHandle = session.postGenericSlackCommand(parameters, command);
        return true;
    }
}
