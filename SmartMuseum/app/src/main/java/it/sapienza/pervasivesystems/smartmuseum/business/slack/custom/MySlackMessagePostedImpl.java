package it.sapienza.pervasivesystems.smartmuseum.business.slack.custom;

import com.ullink.slack.simpleslackapi.SlackBot;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackFile;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackEventType;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import org.json.simple.JSONObject;

import java.util.Map;

/**
 * Created by andrearanieri on 19/06/16.
 */

public class MySlackMessagePostedImpl implements SlackMessagePosted {

    private String messageContent;
    private SlackBot bot;
    private SlackChannel channel;
    private SlackFile slackFile;
    private JSONObject jsonSource;
    private Map<String, Integer> reactions;
    private String timeStamp;
    private SlackEventType eventType;
    private SlackUser sender;
    private String userName;
    private String subtype;

    public MySlackMessagePostedImpl(String text, Object o, SlackUser user, SlackChannel channel, String ts, Object o1, JSONObject obj, Object o2, String un, String st) {
        this.messageContent = text;
        this.timeStamp = ts;
        this.channel = channel;
        this.sender = user;
        this.userName = un;
        this.subtype = st;
    }

    @Override
    public int getTotalCountOfReactions() {
        return 0;
    }

    @Override
    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public SlackBot getBot() {
        return bot;
    }

    public void setBot(SlackBot bot) {
        this.bot = bot;
    }

    @Override
    public SlackChannel getChannel() {
        return channel;
    }

    public void setChannel(SlackChannel channel) {
        this.channel = channel;
    }

    @Override
    public SlackFile getSlackFile() {
        return slackFile;
    }

    public void setSlackFile(SlackFile slackFile) {
        this.slackFile = slackFile;
    }

    @Override
    public JSONObject getJsonSource() {
        return jsonSource;
    }

    @Override
    public String getTimestamp() {
        return timeStamp;
    }

    public void setJsonSource(JSONObject jsonSource) {
        this.jsonSource = jsonSource;
    }

    @Override
    public Map<String, Integer> getReactions() {
        return reactions;
    }

    public void setReactions(Map<String, Integer> reactions) {
        this.reactions = reactions;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public SlackEventType getEventType() {
        return eventType;
    }

    public void setEventType(SlackEventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public SlackUser getSender() {
        return sender;
    }

    public void setSender(SlackUser sender) {
        this.sender = sender;
    }

    @Override
    public String getTimeStamp() {
        return timeStamp;
    }

    public String getUserName() {
        if(userName == null)
            return sender.getUserName();
        else
            return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }
}
