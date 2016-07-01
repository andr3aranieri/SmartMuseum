package it.sapienza.pervasivesystems.smartmuseum.business.slack;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.actions.CreateChannel;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.actions.FetchingMessageHistory;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.actions.InviteToChannel;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.actions.SendingMessages;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.custom.MySlackMessagePostedImpl;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.listeners.MyMessagePostedListener;
import it.sapienza.pervasivesystems.smartmuseum.model.db.QuestionDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ChatModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.QuestionModel;
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
        CREATE_CHANNEL
    }

    static public String token = ""; //send messages;

    private FetchingMessageHistory fetchingMessageHistory = new FetchingMessageHistory();
    private SendingMessages sendingMessages = new SendingMessages();
    private CreateChannel createChannel = new CreateChannel();
    private QuestionDB questionDB = new QuestionDB();

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
        for (int i = messagePosteds.size() - 1; i >= 0; i--) {
            slackMessagePosted = messagePosteds.get(i);

            if(((MySlackMessagePostedImpl) slackMessagePosted).getSubtype() != null) {
                //right
                chatMessage = new ChatMessage(false, slackMessagePosted.getMessageContent());
            }
            else {
                //left
                chatMessage = new ChatMessage(true, slackMessagePosted.getMessageContent());
            }

            chatMessages.add(chatMessage);
        }

        return chatMessages;
    }

    private SlackChannel getChannelByName(SlackSession session, String name) {
        SlackChannel channel = session.findChannelByName(name);
        return channel;
    }

}
