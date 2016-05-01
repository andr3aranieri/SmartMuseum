package it.sapienza.pervasivesystems.smartmuseum.model.entity;

/**
 * Created by andrearanieri on 01/05/16.
 */
public class EntranceModel {

    private int id;
    private String name;
    private String location;
    private String beaconProximityUUID;
    private String beaconMajor;
    private String beaconMinor;
    private int idMuseum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
