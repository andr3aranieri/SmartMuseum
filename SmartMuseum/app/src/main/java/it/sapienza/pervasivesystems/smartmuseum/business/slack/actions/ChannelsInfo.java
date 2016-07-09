package it.sapienza.pervasivesystems.smartmuseum.business.slack.actions;

import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.replies.GenericSlackReply;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.sapienza.pervasivesystems.smartmuseum.business.slack.mymodel.MySlackChannel;

/**
 * Created by andrearanieri on 05/07/16.
 */

public class ChannelsInfo {
    public List<MySlackChannel> channelList(SlackSession session) {
        List<MySlackChannel> channels = new ArrayList<MySlackChannel>();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("exclude_archived", "1");
        String command = "channels.list";

        SlackMessageHandle<GenericSlackReply> replySlackMessageHandle = session.postGenericSlackCommand(parameters, command);
        MySlackChannel channel = null;
        for(JSONObject jsonObject: (Collection<JSONObject>) replySlackMessageHandle.getReply().getPlainAnswer().get("channels")) {
            channel = this.readMySlackChannel(jsonObject);
            if(channel.getName().contains("channel"))
                channels.add(channel);
        }
        return channels;
    }

    private MySlackChannel readMySlackChannel(JSONObject obj) {
        MySlackChannel mySlackChannel = new MySlackChannel();
        mySlackChannel.setId(obj.get("id").toString());
        mySlackChannel.setName(obj.get("name").toString());
        mySlackChannel.setMembers(obj.get("members").toString());
        return mySlackChannel;
    }
}
