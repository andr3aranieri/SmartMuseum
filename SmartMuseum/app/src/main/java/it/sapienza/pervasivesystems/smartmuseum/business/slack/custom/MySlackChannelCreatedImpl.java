package it.sapienza.pervasivesystems.smartmuseum.business.slack.custom;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackChannelCreated;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

/**
 * Created by andrearanieri on 19/06/16.
 */
public class MySlackChannelCreatedImpl implements SlackChannelCreated {
    private SlackChannel slackChannel;
    private SlackUser slackuser;

    MySlackChannelCreatedImpl(SlackChannel slackChannel, SlackUser slackuser)
    {
        this.slackChannel = slackChannel;
        this.slackuser = slackuser;
    }

    @Override
    public SlackChannel getSlackChannel()
    {
        return slackChannel;
    }

    @Override
    public SlackUser getCreator()
    {
        return slackuser;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_CHANNEL_CREATED;
    }
}
