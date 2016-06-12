package it.sapienza.pervasivesystems.smartmuseum.model.entity;

import java.util.Date;

/**
 * Created by andrearanieri on 12/06/16.
 */
public class ChatModel {

    private String userName;
    private Date timeStamp;
    private String message;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
