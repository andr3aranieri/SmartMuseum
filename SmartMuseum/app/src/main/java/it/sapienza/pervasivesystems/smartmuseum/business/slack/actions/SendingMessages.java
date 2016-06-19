package it.sapienza.pervasivesystems.smartmuseum.business.slack.actions;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.impl.SlackChatConfiguration;
import com.ullink.slack.simpleslackapi.replies.SlackChannelReply;

import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;

/**
 * This sample code is showing how to send some messages assuming you already have a SlackSession
 */
public class SendingMessages
{

    /**
     * This method shows how to send a message to a given channel (public channel, private group or direct message channel)
     */
    public void sendMessageToAChannel(SlackSession session, SlackChannel channel, String message, UserModel userModel)
    {
        SlackChatConfiguration slackChatConfiguration = SlackChatConfiguration.getConfiguration();
        slackChatConfiguration.asUser();
        slackChatConfiguration.withName("VISITOR [" + userModel.getEmail() + "]");
        session.sendMessage(channel, message, null, slackChatConfiguration);
    }

    /**
     * This method shows how to send a direct message to a user
     */
    public void sendDirectMessageToAUser(SlackSession session)
    {

        //get a user
        SlackUser user = session.findUserByUserName("adelie");

        session.sendMessageToUser(user, "Hi, how are you", null);

    }

    /**
     * This method shows how to send a direct message to a user, but this time it shows how it can be done using the
     * direct message channels
     */
    public void sendDirectMessageToAUserTheHardWay(SlackSession session)
    {

        //get a user
        SlackUser user = session.findUserByUserName("adelie");

        //get its direct message channel
        SlackMessageHandle<SlackChannelReply> reply = session.openDirectMessageChannel(user);

        //get the channel
        SlackChannel channel = reply.getReply().getSlackChannel();

        //send the message to this channel
        session.sendMessage(channel, "Hi, how are you, Adelie!", null);
    }

    /**
     * This method shows how to send a direct message to multiple users.
     */
    public void sendDirectMessageToMultipleUsers(SlackSession session)
    {

        //get some users
        SlackUser killroy = session.findUserByUserName("killroy");
        SlackUser janedoe = session.findUserByUserName("janedoe");
        SlackUser agentsmith = session.findUserByUserName("agentsmith");

        //open a multiparty direct message channel between the bot and these users
        SlackMessageHandle<SlackChannelReply> reply = session.openMultipartyDirectMessageChannel(killroy, janedoe, agentsmith);

        //get the channel
        SlackChannel channel = reply.getReply().getSlackChannel();

        //send the message to this channel
        session.sendMessage(channel, "Hi, how are you guys", null);
    }

    /**
     * This method shows how to send a message using the PreparedMessage builder (allows for multiple attachments)
     */
//    public void sendUsingPreparedMessage(SlackSession session)
//    {
//        //get a channel
//        SlackChannel channel = session.findChannelByName("achannel");
//
//        //build a message object
//        SlackPreparedMessage preparedMessage = new SlackPreparedMessage.Builder()
//                .withMessage("Hey, this is a message")
//                .withUnfurl(false)
//                .addAttachment(new SlackAttachment())
//                .addAttachment(new SlackAttachment())
//                .build();
//
//        session.sendMessage(channel, preparedMessage);
//    }
}