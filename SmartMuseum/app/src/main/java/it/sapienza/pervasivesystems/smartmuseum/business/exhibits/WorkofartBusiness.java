package it.sapienza.pervasivesystems.smartmuseum.business.exhibits;

import java.util.ArrayList;
import java.util.HashMap;

import it.sapienza.pervasivesystems.smartmuseum.business.aws.AWSConfiguration;
import it.sapienza.pervasivesystems.smartmuseum.model.db.WorkofartDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;

/**
 * Created by andrearanieri on 07/05/16.
 */
public class WorkofartBusiness {

    private WorkofartDB workofartDB = new WorkofartDB();

    public String getWorkofartHashmapKey(String exhibitKey, WorkofartModel wam) {
        return this.workofartDB.getWorkofartHashmapKey(exhibitKey, wam);
    }

    public HashMap<String, WorkofartModel> getWorkofarts(ExhibitModel exhibitModel) {
        return this.workofartDB.getWorkofarts(exhibitModel);
    }

    public ArrayList<WorkofartModel> getWorksofartList(HashMap<String, WorkofartModel> map) {
        return new ArrayList<WorkofartModel>(map.values());
    }

    public ArrayList<WorkofartModel> getWorkOfArtListFAKE() {
        ArrayList<WorkofartModel> list = new ArrayList<WorkofartModel>();
        WorkofartModel w1 = new WorkofartModel();
        w1.setIdWork(1);
        w1.setTitle("The Fountains, 1787/88");
        w1.setShortDescription("The Fountains exploits Robert’s stock-in-trade: fictive niches, arches, coffered vaults, majestic stairwells..");
        w1.setLongDescription("Neoclassical ruin painting had reached its zenith of popularity when Hubert Robert, its foremost French practitioner, was commissioned in 1787 to paint a suite of four canvases for a wealthy financier’s château at Méréville, France. Robert had gleaned his artistic vocabulary from more than a decade’s study in Rome (1754–65). Like the other three large paintings from the group, all in the Art Institute, The Fountains exploits Robert’s stock-in-trade: fictive niches, arches, coffered vaults, majestic stairwells, decaying colonnades, and Roman statuary that create a fantasy of expansive space. The four paintings are inhabited by tiny figures in the foreground; they serve only to set the scale and animate the scene, for the ruins themselves are the true subject of the pictures. The Fountains and its companion pieces were set into the paneled walls of one of the château’s salons, creating an alternate space that played off of the elegant, Neoclassical decor of the room. In his use of ruins, Robert embodied the Romantic concept of the relationship of man and his works to nature, as expressed by the French philosopher and encyclopedist Denis Diderot: \"Everything vanishes, everything dies, only time endures.\"");
        w1.setImage("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/1/images/image0.jpg")));
        w1.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/1/audio/audio0.mp3")));
        list.add(w1);
        WorkofartModel w2 = new WorkofartModel();
        w2.setIdWork(2);
        w2.setTitle("Washerwomen in the Ruins of the Colosseum");
        w2.setShortDescription("In 1754 Hubert Robert traveled to Italy to further his artistic training and soon found himself captivated by the crumbling ruins of ancient monuments.");
        w2.setLongDescription("In 1754 Hubert Robert traveled to Italy to further his artistic training and soon found himself captivated by the crumbling ruins of ancient monuments. During his eleven-year stay in Italy, Robert assembled a vast “catalogue” of drawings of such sites, which he would later include in the historically evocative fantasy scenes that earned him both patronage and acclaim. In this painting’s combination of real and imagined structures, the recognizable pierced walls of Rome’s Colosseum loom over the coffered arches of another decaying structure, while washerwomen gather around a fire.");
        w2.setImage("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/2/images/image0.jpg")));
        w2.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/2/audio/audio0.mp3")));
        list.add(w2);
        return list;
    }

    public WorkofartModel getWorkOfArtDetailFAKE(String key) {
        WorkofartModel ret = null;
        switch (key) {
            case "2048:8066:1":
                WorkofartModel w1 = new WorkofartModel();
                w1.setIdWork(1);
                w1.setTitle("The Fountains, 1787/88");
                w1.setShortDescription("The Fountains exploits Robert’s stock-in-trade: fictive niches, arches, coffered vaults, majestic stairwells..");
                w1.setLongDescription("Neoclassical ruin painting had reached its zenith of popularity when Hubert Robert, its foremost French practitioner, was commissioned in 1787 to paint a suite of four canvases for a wealthy financier’s château at Méréville, France. Robert had gleaned his artistic vocabulary from more than a decade’s study in Rome (1754–65). Like the other three large paintings from the group, all in the Art Institute, The Fountains exploits Robert’s stock-in-trade: fictive niches, arches, coffered vaults, majestic stairwells, decaying colonnades, and Roman statuary that create a fantasy of expansive space. The four paintings are inhabited by tiny figures in the foreground; they serve only to set the scale and animate the scene, for the ruins themselves are the true subject of the pictures. The Fountains and its companion pieces were set into the paneled walls of one of the château’s salons, creating an alternate space that played off of the elegant, Neoclassical decor of the room. In his use of ruins, Robert embodied the Romantic concept of the relationship of man and his works to nature, as expressed by the French philosopher and encyclopedist Denis Diderot: \"Everything vanishes, everything dies, only time endures.\"");
                w1.setImage("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/1/images/image0.jpg")));
                w1.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/1/audio/audio0.mp3")));
                ret = w1;
                break;
            case "2048:8066:2":
                WorkofartModel w2 = new WorkofartModel();
                w2.setIdWork(2);
                w2.setTitle("Washerwomen in the Ruins of the Colosseum");
                w2.setShortDescription("In 1754 Hubert Robert traveled to Italy to further his artistic training and soon found himself captivated by the crumbling ruins of ancient monuments.");
                w2.setLongDescription("In 1754 Hubert Robert traveled to Italy to further his artistic training and soon found himself captivated by the crumbling ruins of ancient monuments. During his eleven-year stay in Italy, Robert assembled a vast “catalogue” of drawings of such sites, which he would later include in the historically evocative fantasy scenes that earned him both patronage and acclaim. In this painting’s combination of real and imagined structures, the recognizable pierced walls of Rome’s Colosseum loom over the coffered arches of another decaying structure, while washerwomen gather around a fire.");
                w2.setImage("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/2/images/image0.jpg")));
                w2.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/2/audio/audio0.mp3")));
                ret = w2;
                break;
        }
        return ret;
    }

    public WorkofartModel getWorkofartDetail(HashMap<String, WorkofartModel> map, String key) {
        WorkofartModel workofartModel;
        if (map.containsKey(key)) {
            workofartModel = map.get(key);
        }
        else {
            workofartModel = null;
        }

        return workofartModel;
    }
}