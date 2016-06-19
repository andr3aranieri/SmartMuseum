package it.sapienza.pervasivesystems.smartmuseum.business.slack.custom;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.events.SlackChannelDeleted;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

/**
 * Created by andrearanieri on 19/06/16.
 */
public class MySlackChannelDeletedImpl implements SlackChannelDeleted {
    private SlackChannel slackChannel;

    MySlackChannelDeletedImpl(SlackChannel slackChannel)
    {
        this.slackChannel = slackChannel;
    }

    @Override
    public SlackChannel getSlackChannel()
    {
        return slackChannel;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_CHANNEL_DELETED;
    }

}
