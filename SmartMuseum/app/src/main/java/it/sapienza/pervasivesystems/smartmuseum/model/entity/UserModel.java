package it.sapienza.pervasivesystems.smartmuseum.model.entity;

/**
 * Created by andrearanieri on 25/04/16.
 */

import com.google.gson.annotations.Expose;

public class UserModel {

    @Expose
    private String name;
    @Expose
    private String email;
    @Expose
    private String password;
    @Expose
    private String profileImage;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
