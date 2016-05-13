package it.sapienza.pervasivesystems.smartmuseum.business.exhibits;

import java.util.ArrayList;
import java.util.HashMap;

import it.sapienza.pervasivesystems.smartmuseum.business.aws.AWSConfiguration;
import it.sapienza.pervasivesystems.smartmuseum.model.db.WorkofartDB;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.UserModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;

/**
 * Created by andrearanieri on 07/05/16.
 */
public class WorkofartBusiness {

    private WorkofartDB workofartDB = new WorkofartDB();

    public HashMap<String, WorkofartModel> getTodayUserWorksofartHistoryMap(UserModel userModel) {
        return this.workofartDB.getTodayUserWorkofartHistory(userModel);
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
        w1.setImage("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/2048_8066/images/image0.jpg");
        w1.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/1/audio/audio0.mp3")));
        list.add(w1);
        WorkofartModel w2 = new WorkofartModel();
        w2.setIdWork(2);
        w2.setTitle("Washerwomen in the Ruins of the Colosseum");
        w2.setShortDescription("In 1754 Hubert Robert traveled to Italy to further his artistic training and soon found himself captivated by the crumbling ruins of ancient monuments.");
        w2.setLongDescription("In 1754 Hubert Robert traveled to Italy to further his artistic training and soon found himself captivated by the crumbling ruins of ancient monuments. During his eleven-year stay in Italy, Robert assembled a vast “catalogue” of drawings of such sites, which he would later include in the historically evocative fantasy scenes that earned him both patronage and acclaim. In this painting’s combination of real and imagined structures, the recognizable pierced walls of Rome’s Colosseum loom over the coffered arches of another decaying structure, while washerwomen gather around a fire.");
        w2.setImage("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/20512_25367/images/image0.jpg");
        w2.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/2/audio/audio0.mp3")));
        list.add(w2);

        WorkofartModel w3 = new WorkofartModel();
        w3.setIdWork(3);
        w3.setTitle("The Fountains, 1787/88");
        w3.setShortDescription("The Fountains exploits Robert’s stock-in-trade: fictive niches, arches, coffered vaults, majestic stairwells..");
        w3.setLongDescription("Neoclassical ruin painting had reached its zenith of popularity when Hubert Robert, its foremost French practitioner, was commissioned in 1787 to paint a suite of four canvases for a wealthy financier’s château at Méréville, France. Robert had gleaned his artistic vocabulary from more than a decade’s study in Rome (1754–65). Like the other three large paintings from the group, all in the Art Institute, The Fountains exploits Robert’s stock-in-trade: fictive niches, arches, coffered vaults, majestic stairwells, decaying colonnades, and Roman statuary that create a fantasy of expansive space. The four paintings are inhabited by tiny figures in the foreground; they serve only to set the scale and animate the scene, for the ruins themselves are the true subject of the pictures. The Fountains and its companion pieces were set into the paneled walls of one of the château’s salons, creating an alternate space that played off of the elegant, Neoclassical decor of the room. In his use of ruins, Robert embodied the Romantic concept of the relationship of man and his works to nature, as expressed by the French philosopher and encyclopedist Denis Diderot: \"Everything vanishes, everything dies, only time endures.\"");
        w3.setImage("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/2048_8066/images/image0.jpg");
        w3.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/1/audio/audio0.mp3")));
        list.add(w3);
        WorkofartModel w4 = new WorkofartModel();
        w4.setIdWork(4);
        w4.setTitle("Washerwomen in the Ruins of the Colosseum");
        w4.setShortDescription("In 1754 Hubert Robert traveled to Italy to further his artistic training and soon found himself captivated by the crumbling ruins of ancient monuments.");
        w4.setLongDescription("In 1754 Hubert Robert traveled to Italy to further his artistic training and soon found himself captivated by the crumbling ruins of ancient monuments. During his eleven-year stay in Italy, Robert assembled a vast “catalogue” of drawings of such sites, which he would later include in the historically evocative fantasy scenes that earned him both patronage and acclaim. In this painting’s combination of real and imagined structures, the recognizable pierced walls of Rome’s Colosseum loom over the coffered arches of another decaying structure, while washerwomen gather around a fire.");
        w4.setImage("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/20512_25367/images/image0.jpg");
        w4.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/2/audio/audio0.mp3")));
        list.add(w4);

        WorkofartModel w5 = new WorkofartModel();
        w5.setIdWork(5);
        w5.setTitle("The Fountains, 1787/88");
        w5.setShortDescription("The Fountains exploits Robert’s stock-in-trade: fictive niches, arches, coffered vaults, majestic stairwells..");
        w5.setLongDescription("Neoclassical ruin painting had reached its zenith of popularity when Hubert Robert, its foremost French practitioner, was commissioned in 1787 to paint a suite of four canvases for a wealthy financier’s château at Méréville, France. Robert had gleaned his artistic vocabulary from more than a decade’s study in Rome (1754–65). Like the other three large paintings from the group, all in the Art Institute, The Fountains exploits Robert’s stock-in-trade: fictive niches, arches, coffered vaults, majestic stairwells, decaying colonnades, and Roman statuary that create a fantasy of expansive space. The four paintings are inhabited by tiny figures in the foreground; they serve only to set the scale and animate the scene, for the ruins themselves are the true subject of the pictures. The Fountains and its companion pieces were set into the paneled walls of one of the château’s salons, creating an alternate space that played off of the elegant, Neoclassical decor of the room. In his use of ruins, Robert embodied the Romantic concept of the relationship of man and his works to nature, as expressed by the French philosopher and encyclopedist Denis Diderot: \"Everything vanishes, everything dies, only time endures.\"");
        w5.setImage("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/2048_8066/images/image0.jpg");
        w5.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/1/audio/audio0.mp3")));
        list.add(w5);
        WorkofartModel w6 = new WorkofartModel();
        w6.setIdWork(6);
        w6.setTitle("Washerwomen in the Ruins of the Colosseum");
        w6.setShortDescription("In 1754 Hubert Robert traveled to Italy to further his artistic training and soon found himself captivated by the crumbling ruins of ancient monuments.");
        w6.setLongDescription("In 1754 Hubert Robert traveled to Italy to further his artistic training and soon found himself captivated by the crumbling ruins of ancient monuments. During his eleven-year stay in Italy, Robert assembled a vast “catalogue” of drawings of such sites, which he would later include in the historically evocative fantasy scenes that earned him both patronage and acclaim. In this painting’s combination of real and imagined structures, the recognizable pierced walls of Rome’s Colosseum loom over the coffered arches of another decaying structure, while washerwomen gather around a fire.");
        w6.setImage("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/20512_25367/images/image0.jpg");
        w6.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/2/audio/audio0.mp3")));
        list.add(w6);

        WorkofartModel w7 = new WorkofartModel();
        w7.setIdWork(7);
        w7.setTitle("The Fountains, 1787/88");
        w7.setShortDescription("The Fountains exploits Robert’s stock-in-trade: fictive niches, arches, coffered vaults, majestic stairwells..");
        w7.setLongDescription("Neoclassical ruin painting had reached its zenith of popularity when Hubert Robert, its foremost French practitioner, was commissioned in 1787 to paint a suite of four canvases for a wealthy financier’s château at Méréville, France. Robert had gleaned his artistic vocabulary from more than a decade’s study in Rome (1754–65). Like the other three large paintings from the group, all in the Art Institute, The Fountains exploits Robert’s stock-in-trade: fictive niches, arches, coffered vaults, majestic stairwells, decaying colonnades, and Roman statuary that create a fantasy of expansive space. The four paintings are inhabited by tiny figures in the foreground; they serve only to set the scale and animate the scene, for the ruins themselves are the true subject of the pictures. The Fountains and its companion pieces were set into the paneled walls of one of the château’s salons, creating an alternate space that played off of the elegant, Neoclassical decor of the room. In his use of ruins, Robert embodied the Romantic concept of the relationship of man and his works to nature, as expressed by the French philosopher and encyclopedist Denis Diderot: \"Everything vanishes, everything dies, only time endures.\"");
        w7.setImage("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/2048_8066/images/image0.jpg");
        w7.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/1/audio/audio0.mp3")));
        list.add(w7);
        WorkofartModel w8 = new WorkofartModel();
        w8.setIdWork(8);
        w8.setTitle("Washerwomen in the Ruins of the Colosseum");
        w8.setShortDescription("In 1754 Hubert Robert traveled to Italy to further his artistic training and soon found himself captivated by the crumbling ruins of ancient monuments.");
        w8.setLongDescription("In 1754 Hubert Robert traveled to Italy to further his artistic training and soon found himself captivated by the crumbling ruins of ancient monuments. During his eleven-year stay in Italy, Robert assembled a vast “catalogue” of drawings of such sites, which he would later include in the historically evocative fantasy scenes that earned him both patronage and acclaim. In this painting’s combination of real and imagined structures, the recognizable pierced walls of Rome’s Colosseum loom over the coffered arches of another decaying structure, while washerwomen gather around a fire.");
        w8.setImage("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/20512_25367/images/image0.jpg");
        w8.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/2/audio/audio0.mp3")));
        list.add(w8);

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
                w1.setImage("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/20512_25367/images/image0.jpg");
                w1.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/1/audio/audio0.mp3")));
                ret = w1;
                break;
            case "2048:8066:2":
                WorkofartModel w2 = new WorkofartModel();
                w2.setIdWork(2);
                w2.setTitle("Washerwomen in the Ruins of the Colosseum");
                w2.setShortDescription("In 1754 Hubert Robert traveled to Italy to further his artistic training and soon found himself captivated by the crumbling ruins of ancient monuments.");
                w2.setLongDescription("In 1754 Hubert Robert traveled to Italy to further his artistic training and soon found himself captivated by the crumbling ruins of ancient monuments. During his eleven-year stay in Italy, Robert assembled a vast “catalogue” of drawings of such sites, which he would later include in the historically evocative fantasy scenes that earned him both patronage and acclaim. In this painting’s combination of real and imagined structures, the recognizable pierced walls of Rome’s Colosseum loom over the coffered arches of another decaying structure, while washerwomen gather around a fire.");
                w2.setImage("https://s3-eu-west-1.amazonaws.com/smartmuseum/exhibits/20512_25367/images/image0.jpg");
                w2.setAudioURL("https://".concat(AWSConfiguration.awsHostname.concat("/smartmuseum/exhibits/2048_8066/worksofart/2/audio/audio0.mp3")));
                ret = w2;
                break;
        }
        return ret;
    }

    public String getWorkofartHashmapKey(String exhibitKey, WorkofartModel wam) {
        return this.workofartDB.getWorkofartHashmapKey(exhibitKey, wam);
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