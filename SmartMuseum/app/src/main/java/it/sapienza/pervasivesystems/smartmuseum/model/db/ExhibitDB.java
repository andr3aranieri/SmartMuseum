package it.sapienza.pervasivesystems.smartmuseum.model.db;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.business.aws.AWSConfiguration;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.CypherRow;
import it.sapienza.pervasivesystems.smartmuseum.neo4j.wsinterface.WSOperations;

/**
 * Created by andrearanieri on 01/05/16.
 */
public class ExhibitDB {

    private WSOperations wsOperations = new WSOperations();

    public List<ExhibitModel> getSortedExhibitList() {
        List<ExhibitModel> exhibits = new ArrayList<ExhibitModel>();

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
        exhibits.add(e1);

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
        exhibits.add(e2);

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
        exhibits.add(e3);

        return exhibits;
    }

    public ExhibitModel getExhibitDetail(int id) {
        ExhibitModel e = null;
        switch (id) {
            case 1:
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

            case 2:
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

            case 3:
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

    public HashMap<String, ExhibitModel> getExhibitsFromDB() {
        HashMap<String, ExhibitModel> hashMapExhibits = new HashMap<String, ExhibitModel>();
        List<CypherRow<List<Object>>> rows = null;
        try {
            String cypher = "MATCH (e:Exhibit) return e";
            rows = this.wsOperations.getCypherMultipleResults(cypher);
            ExhibitModel exhibitModel = null;
            ExhibitBusiness exhibitBusiness = new ExhibitBusiness();
            for (CypherRow<List<Object>> row: rows) {
                exhibitModel = this.readExhibit(row);
                hashMapExhibits.put(exhibitBusiness.getExhibitHashmapKey(exhibitModel), exhibitModel);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
            hashMapExhibits = null;
        }

        return hashMapExhibits;
    }

    //key used to retrieve exhibits from the application level hashmap that contains all of them;
    public String getExhibitHashmapKey(ExhibitModel em) {
        return em.getBeaconMajor().concat(":").concat(em.getBeaconMinor());
    }

    private ExhibitModel readExhibit(CypherRow row) {
        LinkedTreeMap<String, String> objectMap = ((ArrayList<LinkedTreeMap<String,String>>)row.getRow()).get(0);
        ExhibitModel exhibit = new ExhibitModel();
        exhibit.setTitle(objectMap.get("title"));
        exhibit.setShortDescription(objectMap.get("shortDescription"));
        exhibit.setLongDescriptionURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("longDescription"))));
        exhibit.setImage("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("image"))));
        exhibit.setLocation(objectMap.get("location"));
        exhibit.setOpeningHour(objectMap.get("openingHour"));
        exhibit.setPeriod(objectMap.get("period"));
        exhibit.setBeaconProximityUUID(objectMap.get("beaconProximityUUID"));
        exhibit.setBeaconMajor(objectMap.get("beaconMajor"));
        exhibit.setBeaconMinor(objectMap.get("beaconMinor"));
        exhibit.setBeacon(objectMap.get("beacon"));
        exhibit.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat(objectMap.get("audio"))));
        return exhibit;
    }
}
