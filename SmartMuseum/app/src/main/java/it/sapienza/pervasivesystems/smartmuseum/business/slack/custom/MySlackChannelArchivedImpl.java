package it.sapienza.pervasivesystems.smartmuseum.business.slack.custom;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackChannelArchived;
import com.ullink.slack.simpleslackapi.events.SlackEventType;

/**
 * Created by andrearanieri on 19/06/16.
 */
public class MySlackChannelArchivedImpl implements SlackChannelArchived {

    private SlackChannel slackChannel;
    private SlackUser slackuser;

    MySlackChannelArchivedImpl(SlackChannel slackChannel, SlackUser slackuser)
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
    public SlackUser getUser()
    {
        return slackuser;
    }

    @Override
    public SlackEventType getEventType()
    {
        return SlackEventType.SLACK_CHANNEL_ARCHIVED;
    }

}
