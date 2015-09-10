package com.cespenar.thechallenger.models;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasbuber on 28/08/2015.
 */
public class User {

    private String username;

    private String profilePictureUrl;

    public User(String username) {
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

    public static User castLinkedTreeMapToUser(LinkedTreeMap<String, Object> map) {

        String username = (String) map.get("username");
        String profilePictureUrl = (String) map.get("profilePictureUrl");

        return new User(username, profilePictureUrl);
    }
}
