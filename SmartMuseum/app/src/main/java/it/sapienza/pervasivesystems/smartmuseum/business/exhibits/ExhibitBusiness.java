package it.sapienza.pervasivesystems.smartmuseum.business.exhibits;

import android.util.Log;

import com.estimote.sdk.Beacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.beacons.BeaconBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.db.ExhibitDB;
import it.sapienza.pervasivesystems.smartmuseum.model.db.VisitDB;
import it.sapienza.pervasivesystems.smartmuseum.model.db.WorkofartDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitExhibitModel;

/**
 * Created by andrearanieri on 01/05/16.
 */
public class ExhibitBusiness {

    private ExhibitDB exhibitDB = new ExhibitDB();
    private WorkofartDB workofartDB = new WorkofartDB();
    private BeaconBusiness beaconBusiness = new BeaconBusiness();
    private VisitDB visitDB = new VisitDB();

    public HashMap<String, ExhibitModel> getUnorderedExhibits() {
        return this.exhibitDB.getExhibitsFromDB2();
    }

    public ArrayList<ExhibitModel> getSortedExhibits(List<Beacon> sortedBeacons) {
        ArrayList<ExhibitModel> sortedExhibits = null;
        if(SmartMuseumApp.unsortedExhibits != null) {
            sortedExhibits = new ArrayList<ExhibitModel>();
            ExhibitModel em = null;
            VisitExhibitModel visitExhibitModel = null;
            String bKey;
            Log.i("BeaconsDetected", "*******************************");
            for(Beacon b: sortedBeacons) {
                bKey = this.beaconBusiness.getBeaconHashmapKey(b);
                if(SmartMuseumApp.unsortedExhibits.containsKey(bKey)) {
                    Log.i("BeaconsDetected", "key: " + bKey + " YES");
                    em = SmartMuseumApp.unsortedExhibits.get(bKey);

                    //timestamp and color;
                    if(SmartMuseumApp.todayVisitedExhibits != null && SmartMuseumApp.todayVisitedExhibits.containsKey(bKey)) {
                        visitExhibitModel = SmartMuseumApp.todayVisitedExhibits.get(bKey);
                        em.setTimestamp(visitExhibitModel.getTimeStamp());
                        em.setColor("#00ffbf");
                    }
                    else {
                        em.setColor("#ffe6e6");
                    }

                    sortedExhibits.add(em);
                }
                else {
                    Log.i("BeaconsDetected", "key: " + bKey + " NO");
                }
            }
            Log.i("BeaconsDetected", "*******************************");
        }
        else {
            sortedExhibits = null;
        }
        return sortedExhibits;
    }

    public ExhibitModel getExhibitDetail(String key) {
        return SmartMuseumApp.unsortedExhibits.get(key);
    }

    public ArrayList<ExhibitModel> getUnorderedExhibitList(HashMap<String, ExhibitModel> map) {
        return new ArrayList<ExhibitModel>(map.values());
    }

    public ExhibitModel getExhibitDetailFAKE(String key) {
        ExhibitModel e = null;
        switch (key) {
            case "2048:8066":
                ExhibitModel e1 = new ExhibitModel();
                e1.setId(1);
                e1.setTitle("Munch");
                e1.setShortDescription("The Scream (Norwegian: Skrik) is the popular name given to each of four versions of a composition, created as both paintings and pastels, by the Expressionist artist Edvard Munch between 1893 and 1910");
                e1.setLongDescription("The Scream (Norwegian: Skrik) is the popular name given to each of four versions of a composition, created as both paintings and pastels, by the Expressionist artist Edvard Munch between 1893 and 1910. The German title Munch gave these works is Der Schrei der Natur (The Scream of Nature). The works show a figure with an agonized expression against a landscape with a tumultuous orange sky. Arthur Lubow has described The Scream as an icon of modern art, a Mona Lisa for our time. Edvard Munch created the four versions in various media. The National Gallery, Oslo, holds one of two painted versions (1893, shown here). The Munch Museum holds the other painted version (1910, see gallery, below) and a pastel version from 1893. These three versions have not traveled for years.");
                e1.setLongDescriptionURL("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/2048_8066/longdescription/description.txt");
                e1.setImage("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/2048_8066/images/image0.jpg");
                e1.setOpeningHour("10:00-18:00");
                e1.setPeriod("01/05/2016 - 24/05/2016");
                e1.setLocation("Floor 1, Room 3");
                e = e1;
                break;

            case "20512:25367":
                ExhibitModel e2 = new ExhibitModel();
                e2.setId(2);
                e2.setTitle("Buddhist art");
                e2.setShortDescription("Buddhist art is the artistic practices that are influenced by Buddhism. It includes art media which depict Buddhas, bodhisattvas, and other entities.");
                e2.setLongDescription("Buddhist art is the artistic practices that are influenced by Buddhism. It includes art media which depict Buddhas, bodhisattvas, and other entities; notable Buddhist figures, both historical and mythical; narrative scenes from the lives of all of these; mandalas and other graphic aids to practice; as well as physical objects associated with Buddhist practice, such as vajras, bells, stupas and Buddhist temple architecture.[1] Buddhist art originated on the Indian subcontinent following the historical life of Siddhartha Gautama, 6th to 5th century BC, and thereafter evolved by contact with other cultures as it spread throughout Asia and the world.");
                e2.setLongDescriptionURL("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/20512_25367/longdescription/description.txt");
                e2.setImage("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/20512_25367/images/image0.jpg");
                e2.setOpeningHour("11:00-19:00");
                e2.setPeriod("01/05/2016 - 02/06/2016");
                e2.setLocation("Floor 2, Room 1");
                e = e2;
                break;

            case "33510:55725":
                ExhibitModel e3 = new ExhibitModel();
                e3.setId(3);
                e3.setTitle("Natural Arts");
                e3.setShortDescription("A star studded private gala night on 18th April will kick off this week long exhibition in the heart of London, just up the road from Buckingham Palace.");
                e3.setLongDescription("A star studded private gala night on 18th April will kick off this week long exhibition in the heart of London, just up the road from Buckingham Palace. Gary Hodges AFC has deliberately kept back a few of his rare sold-out editions for this show. Over 160 of his amazing drawings, including originals, limited edition prints and embellished prints will be on sale and for auction. Gary has just launched a special auction page on his website: garyhodges-wildlife-art.com/auction so that anyone throughout the world can place bids and help elephants and other wildlife.");
                e3.setLongDescriptionURL("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/33510_55725/longdescription/description.txt");
                e3.setImage("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/33510_55725/images/image0.jpg");
                e3.setOpeningHour("10:00-18:00");
                e3.setPeriod("01/05/2016 - 24/05/2016");
                e3.setLocation("Floor 1, Room 5");
                e = e3;
                break;

            default:
                e = null;
        }
        return e;
    }

//    public ArrayList<VisitExhibitModel> getUserExhibitsHistory(UserModel userModel) {
//        return this.visitDB.getUserExhibitHistory(userModel);
//    }

    public ArrayList<ExhibitModel> getUserExhibitHistory(UserModel um) {
        return this.exhibitDB.getUserExhibitHistory(um);
    }

//    public HashMap<String, ExhibitModel> getTodayUserExhibitsHistoryMap(UserModel userModel) {
//        return this.exhibitDB.getTodayUserExhibitHistory(userModel);
//    }
//
    public HashMap<String, VisitExhibitModel> getTodayUserExhibitVisitsHistoryMap(UserModel userModel) {
        return this.visitDB.getTodayUserExhibitHistoryHashMap(userModel);
    }

    public HashMap<String, VisitExhibitModel> getUserExhibitVisitsHistoryMap(UserModel userModel) {
        return this.visitDB.getUserExhibitHistoryHashMap(userModel);
    }

    public ArrayList<VisitExhibitModel> getUserExhibitHistoryList(HashMap<String, VisitExhibitModel> userHistoryMap) {
        return new ArrayList<VisitExhibitModel>(userHistoryMap.values());
    }

    public boolean hasAlreadyVisited(String key, HashMap<String, VisitExhibitModel> exhibitsHistoryMap) {
        boolean ret = false;
        VisitExhibitModel vem = null;
        if(exhibitsHistoryMap.containsKey(key)) {
            ret = true;
        }
        else {
            ret = false;
        }
        return ret;
    }

    public VisitExhibitModel getVisitExhibitDetail(HashMap<String, VisitExhibitModel> userHistoryMap, String key) {
        VisitExhibitModel visitExhibitModel;
        if (userHistoryMap.containsKey(key)) {
            visitExhibitModel = userHistoryMap.get(key);
        }
        else {
            visitExhibitModel = null;
        }

        return visitExhibitModel;
    }

    public boolean hasOrderingChanged(ArrayList<ExhibitModel> oldList, ArrayList<ExhibitModel> newList) {
        int index = 0;

        //if the number of detected beacons changes, newList will be surely different from oldList;
        if(oldList == null || oldList.size() == 0 || newList == null || newList.size() == 0 || (oldList.size() != newList.size()))
            return true;

        //if the number of elements in the 2 lists is the same, we check their ordering;
        for(ExhibitModel em: oldList) {
            index = oldList.indexOf(em);
            if(this.getExhibitHashmapKey(newList.get(index)).compareTo(this.getExhibitHashmapKey(em)) != 0) {
                return true;
            }
        }
        return false;
    }

    //key used to retrieve exhibits from the application level hashmap that contains all of them;
    public String getExhibitHashmapKey(ExhibitModel em) {
        return this.exhibitDB.getExhibitHashmapKey(em);
    }
}
