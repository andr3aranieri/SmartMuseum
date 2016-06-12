package it.sapienza.pervasivesystems.smartmuseum.business.slack;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.business.slack.actions.CreateChannel;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.actions.FetchingMessageHistory;
import it.sapienza.pervasivesystems.smartmuseum.business.slack.actions.SendingMessages;
import it.sapienza.pervasivesystems.smartmuseum.model.db.QuestionDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.QuestionModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;

/**
 * Created by andrearanieri on 01/06/16.
 */
public class SlackBusiness {

    public enum SlackCommand {
        OPEN_SESSION,
        CLOSE_SESSION,
        DOWNLOAD_MESSAGES
    }

    static public String token = "xoxp-43100032400-43201859862-47155173957-987ed62aa5";

    private FetchingMessageHistory fetchingMessageHistory = new FetchingMessageHistory();
    private SendingMessages sendingMessages = new SendingMessages();
    private CreateChannel createChannel = new CreateChannel();
    private QuestionDB questionDB = new QuestionDB();

    public SlackSession createSession(String t) throws IOException {
        SlackSession session = SlackSessionFactory.createWebSocketSlackSession(t);
        session.connect();
        return session;
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

    public boolean createQuestionOnDB(Date ts, QuestionModel qm, UserModel um){
        return this.questionDB.insertQuestion(ts, qm, um);
    }

    private SlackChannel getChannelByName(SlackSession session, String name) {
        SlackChannel channel = session.findChannelByName(name);
        return channel;
    }

}
