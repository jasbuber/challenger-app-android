package com.cespenar.thechallenger.models;

import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasbuber on 28/08/2015.
 */
public class User implements Serializable{

    private String username;

    private String profilePictureUrl;

    private String firstName;

    private String lastName;

    public User(String username) {
        this.username = username;
    }

    public User(String username, String profilePictureUrl, String firstName, String lastName) {
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getFormattedName(){
        return firstName + " " + lastName.substring(0,3);
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public static User castLinkedTreeMapToUser(LinkedTreeMap<String, Object> map) {

        String username = (String) map.get("username");
        String profilePictureUrl = (String) map.get("profilePictureUrl");
        String firstName = (String) map.get("firstName");
        String lastName = (String) map.get("lastName");

        return new User(username, profilePictureUrl, firstName, lastName);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + getFormattedName() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!username.equalsIgnoreCase(user.username)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
