package it.sapienza.pervasivesystems.smartmuseum.business.visits;

import it.sapienza.pervasivesystems.smartmuseum.model.db.VisitDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;

/**
 * Created by andrearanieri on 05/05/16.
 */
public class VisitBusiness {
    private VisitDB visitDB = new VisitDB();

    public boolean insertExhibitVisit(ExhibitModel em, UserModel um) {
        return this.visitDB.insertExhibitVisit(em, um);
    }
}
