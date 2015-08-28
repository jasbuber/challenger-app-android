package com.cespenar.thechallenger.models;

/**
 * Created by Jasbuber on 28/08/2015.
 */
public class User {

    private String username;

    private String profilePictureUrl;

    public User(String username){
        this.username = username;
    }

    public User(String username, String profilePictureUrl) {
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
}
