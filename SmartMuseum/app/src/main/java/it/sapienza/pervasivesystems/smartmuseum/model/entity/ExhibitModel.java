package it.sapienza.pervasivesystems.smartmuseum.model.entity;

/**
 * Created by andrearanieri on 01/05/16.
 */
public class ExhibitModel {

    private int id;
    private String title;
    private String shortDescription;
    private String longDescription;
    private String longDescriptionURL;
    private String image;
    private String location;
    private String openingHour;
    private String period;
    private String beaconProximityUUID;
    private String beaconMajor;
    private String beaconMinor;
    private String beacon;
    private String audioURL;
    private int idMuseum;
    private String color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getLongDescriptionURL() {
        return longDescriptionURL;
    }

    public void setLongDescriptionURL(String longDescriptionURL) {
        this.longDescriptionURL = longDescriptionURL;
    }

    public int getIdMuseum() {
        return idMuseum;
    }

    public void setIdMuseum(int idMuseum) {
        this.idMuseum = idMuseum;
    }

    public String getBeaconProximityUUID() {
        return beaconProximityUUID;
    }

    public void setBeaconProximityUUID(String beaconProximityUUID) {
        this.beaconProximityUUID = beaconProximityUUID;
    }

    public String getBeaconMajor() {
        return beaconMajor;
    }

    public void setBeaconMajor(String beaconMajor) {
        this.beaconMajor = beaconMajor;
    }

    public String getBeaconMinor() {
        return beaconMinor;
    }

    public void setBeaconMinor(String beaconMinor) {
        this.beaconMinor = beaconMinor;
    }

    public String getBeacon() {
        return beacon;
    }

    public void setBeacon(String beacon) {
        this.beacon = beacon;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
