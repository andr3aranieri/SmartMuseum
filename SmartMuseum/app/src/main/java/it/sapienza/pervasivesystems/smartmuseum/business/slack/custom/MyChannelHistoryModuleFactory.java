package it.sapienza.pervasivesystems.smartmuseum.business.slack.custom;

import com.ullink.slack.simpleslackapi.ChannelHistoryModule;
import com.ullink.slack.simpleslackapi.SlackSession;

/**
 * Created by andrearanieri on 19/06/16.
 */
public class MyChannelHistoryModuleFactory {

    public static ChannelHistoryModule createChannelHistoryModule(SlackSession session){
        return new MyChannelHistoryModuleImpl(session);
    };

}
