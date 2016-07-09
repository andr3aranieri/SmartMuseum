package it.sapienza.pervasivesystems.smartmuseum.business.slack.mymodel;

/**
 * Created by andrearanieri on 05/07/16.
 */
public class MySlackChannel {
    private String id;
    private String name;
    private String members;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }
}
