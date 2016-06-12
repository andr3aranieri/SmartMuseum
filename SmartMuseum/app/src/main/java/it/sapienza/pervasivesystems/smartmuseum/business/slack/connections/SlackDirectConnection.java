package it.sapienza.pervasivesystems.smartmuseum.business.slack.connections;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

import java.io.IOException;

/**
 * This sample code is creating a Slack session and is connecting to slack. To get some more details on
 * how to get a token, please have a look here : https://api.slack.com/bot-users
 */
public class SlackDirectConnection
{
    public static void main(String[] args) throws IOException
    {
        SlackSession session = SlackSessionFactory.createWebSocketSlackSession("xoxp-43100032400-43201859862-47155173957-987ed62aa5");
        session.connect();
    }
}