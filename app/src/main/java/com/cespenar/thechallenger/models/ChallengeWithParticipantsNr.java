package com.cespenar.thechallenger.models;

import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jasbuber on 19/09/2015.
 */
public class ChallengeWithParticipantsNr {

    private final String challengeName;
    private final Date challengeCreationDate;
    private final Long challengeId;
    private final Long participantsNr;


    public ChallengeWithParticipantsNr(String challengeName, Long participantsNr, Long challengeId) {
        this(challengeName, null, participantsNr, challengeId);
    }

    public ChallengeWithParticipantsNr(String challengeName, Date challengeCreationDate, Long participantsNr, Long challengeId) {
        this.challengeName = challengeName;
        this.challengeCreationDate = challengeCreationDate;
        this.challengeId = challengeId;
        this.participantsNr = participantsNr;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public Date getChallengeCreationDate() {
        return challengeCreationDate;
    }

    public Long getParticipantsNr() {
        return participantsNr;
    }

    public Long getChallengeId() {
        return challengeId;
    }

    public static List<ChallengeWithParticipantsNr> castLinkedTreeMapToChallengeList(List<LinkedTreeMap<String, Object>> map){

        ArrayList<ChallengeWithParticipantsNr> challenges = new ArrayList<>();

        for(LinkedTreeMap<String, Object> challenge : map){

            challenges.add(castLinkedTreeMapToChallenge(challenge));
        }

        return challenges;
    }

    public static ChallengeWithParticipantsNr castLinkedTreeMapToChallenge(LinkedTreeMap<String, Object> challenge) {

        String challengeName = String.valueOf(challenge.get("challengeName"));
        long id = (long)((double) challenge.get("challengeId"));
        long participantsNr = (long)((double) challenge.get("participantsNr"));
        Date creationDate = new Date();

        try {
            creationDate = new SimpleDateFormat("MMM dd, yyyy h:mm:ss a").parse((String) challenge.get("challengeCreationDate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new ChallengeWithParticipantsNr(challengeName, creationDate, participantsNr, id);
    }

    public static List<ChallengeWithParticipantsNr> castToChallengeList(List<ArrayList> map){

        ArrayList<ChallengeWithParticipantsNr> challenges = new ArrayList<>();

        for(ArrayList challenge : map){

            Log.e("challenge", challenge.toString());
            challenges.add(castToChallenge(challenge));
        }

        return challenges;
    }

    public static ChallengeWithParticipantsNr castToChallenge(ArrayList challenge) {

        String challengeName = String.valueOf(challenge.get(0));
        long id = (long)((double) challenge.get(3));
        long participantsNr = (long)((double) challenge.get(2));
        Date creationDate = new Date();

        try {
            creationDate = new SimpleDateFormat("MMM dd, yyyy h:mm:ss a").parse((String) challenge.get(1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new ChallengeWithParticipantsNr(challengeName, creationDate, participantsNr, id);
    }
}
