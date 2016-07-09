package it.sapienza.pervasivesystems.smartmuseum.business.slack;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.actions.ChannelsInfo;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.actions.CreateChannel;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.actions.FetchingMessageHistory;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.actions.InviteToChannel;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.actions.SendingMessages;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.custom.MySlackMessagePostedImpl;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.listeners.MyMessagePostedListener;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.mymodel.MySlackChannel;
import it.sapienza.pervasivesystems.smartmuseum.model.db.QuestionDB;
import it.sapienza.pervasivesystems.smartmuseum.model.db.SlackChannelDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ChatModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.QuestionModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.SlackChannelModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.view.slack.gui.ChatMessage;

/**
 * Created by andrearanieri on 01/06/16.
 */
public class SlackBusiness {

    public enum SlackCommand {
        OPEN_SESSION,
        CLOSE_SESSION,
        DOWNLOAD_MESSAGES,
        SEND_MESSAGE,
        CREATE_CHANNEL,
        CHANNEL_LIST
    }

    static public String token = ""; //send messages;
    static public String slack_client_id = "";
    static public String slack_client_secret = "";
    static public String slack_token_scope = "channels:write channels:history channels:read chat:write:bot chat:write:user im:history im:read im:write";

    private FetchingMessageHistory fetchingMessageHistory = new FetchingMessageHistory();
    private SendingMessages sendingMessages = new SendingMessages();
    private CreateChannel createChannel = new CreateChannel();
    private QuestionDB questionDB = new QuestionDB();
    private SlackChannelDB slackChannelDB = new SlackChannelDB();

    public SlackSession createSession(String t) throws IOException {
        SlackSession session = SlackSessionFactory.createWebSocketSlackSession(t);
        session.connect();
        return session;
    }

    public void startListener(String channel, String botUserID) {
        SmartMuseumApp.slackSession.addMessagePostedListener(new MyMessagePostedListener());
        new InviteToChannel().inviteToChannel(SmartMuseumApp.slackSession, channel, botUserID);
    }

    public List<SlackMessagePosted> getMessagesInChannel(SlackSession session, String channelName, int numberOfMessages) {
        return this.fetchingMessageHistory.fetchSomeMessagesFromChannelHistory(session, this.getChannelByName(session, channelName), numberOfMessages);
    }

    public List<QuestionModel> getUserQuestions(UserModel userModel) {
        return this.questionDB.getQuestionsByUser(userModel);
    }

    public String createChannel(SlackSession session, String channelName) {
        return this.createChannel.createChannel(session, channelName);
    }

    public void sendMessageToChannel(SlackSession session, String channelName, String message, UserModel userModel) {
        this.sendingMessages.sendMessageToAChannel(session, this.getChannelByName(session, channelName), message, userModel);
    }

    public SlackChannelModel getFreeSlackChannel() {
        SlackChannelModel freeChannel = null;
        List<MySlackChannel> availableChannels = this.getChannelList(SmartMuseumApp.slackSession);
        HashMap<String, SlackChannelModel> takenChannels = this.slackChannelDB.getTakenChannels();
        for (MySlackChannel mySlackChannel: availableChannels) {
            if(!takenChannels.containsKey(mySlackChannel.getId())) {
                freeChannel = new SlackChannelModel();
                freeChannel.setChannelId(mySlackChannel.getId());
                freeChannel.setChannelName(mySlackChannel.getName());
                break;
            }
        }
        return freeChannel;
    }

    public void sendMessageToUser(SlackSession session) {
        this.sendingMessages.sendDirectMessageToAUserTheHardWay(session);
    }

    public boolean createQuestionOnDB(Date ts, QuestionModel qm, UserModel um){
        return this.questionDB.insertQuestion(ts, qm, um);
    }

    public ArrayList<ChatModel> convertSlackMessages(List<SlackMessagePosted> messagePosteds) {
        ArrayList<ChatModel> chatModels = new ArrayList<>();
        ChatModel chatModel = null;
        SlackMessagePosted slackMessagePosted = null;
        for (int i = messagePosteds.size() - 1; i >= 0; i--) {
            slackMessagePosted = messagePosteds.get(i);
            chatModel = new ChatModel();
            chatModel.setMessage(slackMessagePosted.getMessageContent());
            chatModel.setUserName(((MySlackMessagePostedImpl) slackMessagePosted).getUserName());
            chatModel.setsTimeStamp(slackMessagePosted.getTimeStamp());
            chatModel.setTimeStamp(new Date((long) Double.parseDouble(slackMessagePosted.getTimeStamp())));
            chatModels.add(chatModel);
        }

        return chatModels;
    }

    public ArrayList<ChatMessage> convertSlackMessages2(List<SlackMessagePosted> messagePosteds) {
        ArrayList<ChatMessage> chatMessages = new ArrayList<>();
        ChatMessage chatMessage = null;
        SlackMessagePosted slackMessagePosted = null;
        int index = 0;
        String userContent = "";
        for (int i = messagePosteds.size() - 1; i >= 0; i--) {
            slackMessagePosted = messagePosteds.get(i);

            index = slackMessagePosted.getMessageContent().indexOf("*\n");
            if(index == -1)
                index = 0;
            else
                index = index + 3;

            userContent = slackMessagePosted.getMessageContent().substring(index);

            if(((MySlackMessagePostedImpl) slackMessagePosted).getSubtype() != null) {
                //right
                chatMessage = new ChatMessage(false, userContent);
            }
            else {
                //left
                chatMessage = new ChatMessage(true, userContent);
            }

            chatMessages.add(chatMessage);
        }

        return chatMessages;
    }

    public String getToken() throws IOException {
        String ret = "";
        URL url = new URL("https://slack.com/oauth/authorize");
        URLConnection conn = (HttpURLConnection) url.openConnection();
        conn.addRequestProperty("client_id", slack_client_id);
        conn.addRequestProperty("client_secret", slack_client_secret);
        conn.addRequestProperty("scope", slack_token_scope);


        return ret;
    }

    public List<MySlackChannel> getChannelList(SlackSession session) {
        return new ChannelsInfo().channelList(session);
    }

    private SlackChannel getChannelByName(SlackSession session, String name) {
        SlackChannel channel = session.findChannelByName(name);
        return channel;
    }

}
