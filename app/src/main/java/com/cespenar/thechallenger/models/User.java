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

    private Integer creationPoints = 0;

    private Integer participationPoints = 0;

    private Integer otherPoints = 0;

    public User(String username) {
        this.username = username;
    }

    public User(String username, String profilePictureUrl, String firstName, String lastName) {
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String username, String profilePictureUrl, String firstName, String lastName,
                int creationPoints, int participationPoints, int otherPoints) {
        this(username, profilePictureUrl, firstName, lastName);
        this.creationPoints = creationPoints;
        this.participationPoints = participationPoints;
        this.otherPoints = otherPoints;
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
        int creationPoints = (int)(double) map.get("creationPoints");
        int participationPoints = (int)(double) map.get("participationPoints");
        int otherPoints = (int)(double) map.get("otherPoints");

        return new User(username, profilePictureUrl, firstName, lastName, creationPoints,
                participationPoints, otherPoints);
    }

    public static List<User> castLinkedTreeMapToUserList(List<LinkedTreeMap<String, Object>> map){

        ArrayList<User> users = new ArrayList<>();

        for(LinkedTreeMap<String, Object> user : map){

            users.add(castLinkedTreeMapToUser(user));
        }

        return users;
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

    public Integer getAllPoints(){
        return this.creationPoints + this.participationPoints + this.otherPoints;
    }
}
