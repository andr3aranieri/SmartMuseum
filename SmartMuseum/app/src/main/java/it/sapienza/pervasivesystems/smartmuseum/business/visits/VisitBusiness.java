package it.sapienza.pervasivesystems.smartmuseum.business.visits;

import java.util.Date;

import it.sapienza.pervasivesystems.smartmuseum.model.db.VisitDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;

/**
 * Created by andrearanieri on 05/05/16.
 */
public class VisitBusiness {
    private VisitDB visitDB = new VisitDB();

    public boolean insertExhibitVisit(Date timeStamp, ExhibitModel em, UserModel um) {
        return this.visitDB.insertExhibitVisit(timeStamp, em, um);
    }

    public boolean insertWorkofartVisit(Date timeStamp, WorkofartModel wom, UserModel um) {
        return this.visitDB.insertWorkofartVisit(timeStamp, wom, um);
    }
}
