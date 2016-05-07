package it.sapienza.pervasivesystems.smartmuseum.model.entity;

/**
 * Created by andrearanieri on 04/05/16.
 */
public class WorkofartModel {
    private int id;
    private String title;
    private String shortDescription;
    private String longDescription;
    private String longDescriptionURL;
    private String image;
    private String audioURL;

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

    public String getLongDescriptionURL() {
        return longDescriptionURL;
    }

    public void setLongDescriptionURL(String longDescriptionURL) {
        this.longDescriptionURL = longDescriptionURL;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }
}
