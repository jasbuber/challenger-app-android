package com.cespenar.thechallenger.models;

import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jasbuber on 15/09/2015.
 */
public class ChallengeParticipation implements Serializable{

    public static int CREATOR_STATE = 3;
    public static int NOT_PARTICIPATING_STATE = 0;
    public static int NOT_RESPONDED_STATE = 1;
    public static int VIDEO_CHOSEN = 4;
    public static int RESPONDED = 2;
    public static int RATED = 5;

    private Long id;

    private Challenge challenge;

    private User participator;

    private Date joined;

    private Character isChallengeRated;

    private Character isResponseSubmitted;

    protected ChallengeParticipation() {
        //for jpa purposes...
    }

    public ChallengeParticipation(Challenge challenge, User participator) {
        this.challenge = challenge;
        this.participator = participator;
        this.joined = new Date();
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public ChallengeParticipation(Long id, Challenge challenge, User participator, Date joined, Character isChallengeRated, Character isResponseSubmitted) {
        this.id = id;
        this.challenge = challenge;
        this.participator = participator;
        this.joined = joined;
        this.isChallengeRated = isChallengeRated;
        this.isResponseSubmitted = isResponseSubmitted;
    }

    public User getParticipator() {
        return participator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChallengeParticipation that = (ChallengeParticipation) o;

        if (!challenge.equals(that.challenge)) return false;
        if (!participator.equals(that.participator)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = challenge.hashCode();
        result = 31 * result + participator.hashCode();
        return result;
    }

    public User getCreator() {
        return challenge.getCreator();
    }

    public Long getId() {
        return id;
    }

    public String getJoined() {
        return new SimpleDateFormat("dd-MM-yyyy").format(this.joined);
    }

    public void rateChallenge() {
        this.isChallengeRated = 'Y';
    }

    public boolean isChallengeRated() {
        return this.isChallengeRated != null;
    }

    public boolean isResponseSubmitted() {
        return this.isResponseSubmitted != null;
    }

    public void submit() {
        this.isResponseSubmitted = 'Y';
    }

    public static List<ChallengeParticipation> castLinkedTreeMapToChallengeParticipationsList(List<LinkedTreeMap<String, Object>> map){

        ArrayList<ChallengeParticipation> participations = new ArrayList<>();

        for(LinkedTreeMap<String, Object> participation : map){

            participations.add(castLinkedTreeMapToChallengeParticipation(participation));
        }

        return participations;
    }

    public static ChallengeParticipation castLinkedTreeMapToChallengeParticipation(LinkedTreeMap<String, Object> participation) {

        long id = (long)((double) participation.get("id"));
        Challenge challenge = Challenge.castLinkedTreeMapToChallenge((LinkedTreeMap) participation.get("challenge"));
        User participant = User.castLinkedTreeMapToUser((LinkedTreeMap) participation.get("participator"));

        String isChallengeRatedString = (String) participation.get("isChallengeRated");
        Character isChallengeRated = null;
        Character isResponseSubmitted = null;

        if(isChallengeRatedString != null){
            isChallengeRated = isChallengeRatedString.charAt(0);
        }

        String isResponseSubmittedString = (String) participation.get("isChallengeRated");
        if(isResponseSubmittedString != null) {
            isResponseSubmitted = ((String) participation.get("isResponseSubmitted")).charAt(0);
        }

        Date joined = new Date();

        try {
            joined = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse((String) participation.get("joined"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new ChallengeParticipation(id, challenge, participant, joined, isChallengeRated, isResponseSubmitted);
    }
}
