package com.cespenar.thechallenger.services;

/**
 * Created by Jasbuber on 17/08/2015.
 */
public class UserService {

    //Hardcoded until facebook integration implemented
    private static String currentUsername = "1384398625202082";

    //Blank until facebook integration implemented
    private static String currentToken = "dummyToken";

    public static String getCurrentUsername(){
        return currentUsername;
    }

    public static String getCurrentToken(){
        return currentToken;
    }
}
