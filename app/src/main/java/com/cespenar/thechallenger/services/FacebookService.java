package com.cespenar.thechallenger.services;

/**
 * Created by Jasbuber on 14/08/2015.
 */
public class FacebookService {

    private static FacebookService service;

    private FacebookService(){}

    public static FacebookService getService(){
        if(service == null){
            service = new FacebookService();
        }

        return service;
    }
}
