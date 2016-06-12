package it.sapienza.pervasivesystems.smartmuseum.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by andrearanieri on 03/06/16.
 */
public class QuestionModel implements Serializable {
    private String title;
    private Date timeStamp;
    private String channelName;
    private boolean open;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
