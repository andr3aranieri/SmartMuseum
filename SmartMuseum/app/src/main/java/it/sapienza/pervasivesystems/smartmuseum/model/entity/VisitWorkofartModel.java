package it.sapienza.pervasivesystems.smartmuseum.model.entity;

import java.util.Date;

/**
 * Created by andrearanieri on 07/05/16.
 */
public class VisitWorkofartModel {
    private WorkofartModel workofartModel;
    private Date timestamp;

    public WorkofartModel getWorkofartModel() {
        return workofartModel;
    }

    public void setWorkofartModel(WorkofartModel workofartModel) {
        this.workofartModel = workofartModel;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
