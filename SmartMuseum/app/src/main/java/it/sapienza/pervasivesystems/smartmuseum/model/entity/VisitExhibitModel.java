package it.sapienza.pervasivesystems.smartmuseum.model.entity;

import java.util.Date;

/**
 * Created by andrearanieri on 07/05/16.
 */
public class VisitExhibitModel {
    private ExhibitModel exhibitModel;
    private Date timeStamp;

    public ExhibitModel getExhibitModel() {
        return exhibitModel;
    }

    public void setExhibitModel(ExhibitModel exhibitModel) {
        this.exhibitModel = exhibitModel;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
