package com.cespenar.thechallenger.services;

import com.cespenar.thechallenger.models.Challenge;

/**
 * Created by Jasbuber on 14/08/2015.
 */
public class ChallengeService {

    private static ChallengeService service;

    private static FacebookService fbService;

    private ChallengeService(){
        fbService = FacebookService.getService();
    }

    public static ChallengeService getService(){
        if(service == null){
            service = new ChallengeService();
        }

        return service;
    }

    public String createChallenge(Challenge challenge){
        return "not implemented method";
    }

    public String updateChallenge(Challenge challenge){
        return "not implemented method";
    }
}
