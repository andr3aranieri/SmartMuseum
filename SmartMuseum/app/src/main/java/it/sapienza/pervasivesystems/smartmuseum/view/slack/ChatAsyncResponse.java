package it.sapienza.pervasivesystems.smartmuseum.view.slack;

import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;

/**
 * Created by andrearanieri on 14/06/16.
 */
public interface ChatAsyncResponse {
    void sessionOpened(ILCMessage message);

    void sessionClosed(ILCMessage message);

    void messagesDownloaed(ILCMessage message);

    void messageSent(ILCMessage message);

    void channelCreated(ILCMessage message);
}
