package it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication;

/**
 * Created by andrearanieri on 28/04/16.
 */

public class ILCMessage {
    public enum MessageType {
        SUCCESS,
        ERROR,
        INFO,
        WARNING,
        DEBUG
    }

    private MessageType messageType;
    private String messageText;
    private Object messageObject;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Object getMessageObject() {
        return messageObject;
    }

    public void setMessageObject(Object messageObject) {
        this.messageObject = messageObject;
    }
}
