package com.cespenar.thechallenger.services;

import com.cespenar.thechallenger.models.User;

/**
 * Created by Jasbuber on 17/08/2015.
 */
public class UserService {

    //Blank until facebook integration implemented
    private static String currentToken = "dummyToken";

    private static User currentUser = new User("1384398625202082", "stubphoto", "Janusz", "One");

    public static String getCurrentUsername(){
        return currentUser.getUsername();
    }

    public static String getCurrentToken(){
        return currentToken;
    }

    public static User getCurrentUser(){
        return currentUser;
    }
}
