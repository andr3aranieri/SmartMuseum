package it.sapienza.pervasivesystems.smartmuseum.business.slack.custom;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackEvent;

import org.json.simple.JSONObject;

import java.util.Map;

/**
 * Created by andrearanieri on 19/06/16.
 */
public class MySlackJSONMessageParser {

    static SlackEvent decode(SlackSession slackSession, JSONObject obj) {
        return extractMessageEvent(slackSession, obj);
    }

    private static SlackEvent extractMessageEvent(SlackSession slackSession, JSONObject obj)
    {
        String channelId = (String) obj.get("channel");
        SlackChannel channel = getChannel(slackSession, channelId);

        String ts = (String) obj.get("ts");
        return parseMessagePublished(obj, channel, ts, slackSession);
    }

    private static MySlackMessagePostedImpl parseMessagePublished(JSONObject obj, SlackChannel channel, String ts, SlackSession slackSession) {
        String text = (String) obj.get("text");
        String userId = (String) obj.get("user");
        String userName = null;
        if (userId == null) {
            userId = (String) obj.get("bot_id");
            userName = (String) obj.get("username");
        }

        String subtype = (String) obj.get("subtype");
        SlackUser user = slackSession.findUserById(userId);
        Map<String, Integer> reacs = null;
        MySlackMessagePostedImpl message = new MySlackMessagePostedImpl(text, null, user, channel, ts, null, obj, null, userName);
        message.setReactions(reacs);
        return message;
    }

    private static SlackChannel getChannel(SlackSession slackSession, String channelId)
    {
        if (channelId != null)
        {
            return slackSession.findChannelById(channelId);
        }
        return null;
    }

}
