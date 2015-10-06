package com.cespenar.thechallenger.models;

import com.cespenar.thechallenger.services.UserService;
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
public class ChallengeResponse implements Serializable{

    private Long id;

    private ChallengeParticipation challengeParticipation;

    private Character isAccepted;

    private String videoResponseUrl;

    private String message;

    private Date submitted = new Date();

    private String thumbnailUrl;

    protected ChallengeResponse() {
        //for jpa purposes...
    }

    public ChallengeResponse(ChallengeParticipation challengeParticipation) {
        this.challengeParticipation = challengeParticipation;
    }

    public ChallengeResponse(ChallengeParticipation challengeParticipation, String videoResponseUrl) {
        this.challengeParticipation = challengeParticipation;
        this.videoResponseUrl = videoResponseUrl;
    }

    public ChallengeResponse(ChallengeParticipation challengeParticipation, String videoResponseUrl, String message) {
        this.challengeParticipation = challengeParticipation;
        this.videoResponseUrl = videoResponseUrl;
        this.message = message;
    }

    public ChallengeParticipation getChallengeParticipation() {
        return challengeParticipation;
    }

    public ChallengeResponse(Long id, ChallengeParticipation challengeParticipation, Character isAccepted, String videoResponseUrl, String message, Date submitted, String thumbnailUrl) {
        this(challengeParticipation, videoResponseUrl, message);
        this.id = id;
        this.isAccepted = isAccepted;
        this.submitted = submitted;
        this.thumbnailUrl = thumbnailUrl;
    }

    public boolean isAccepted() {
        return isAccepted != null && isAccepted == 'Y';
    }

    public void accept() {
        this.isAccepted = 'Y';
    }

    public void refuse() {
        this.isAccepted = 'N';
    }

    public boolean isDecided() {
        return this.isAccepted != null;
    }

    public Long getId() {
        return id;
    }

    public Character getIsAccepted() {
        return isAccepted;
    }

    public String getVideoResponseUrl() {
        return videoResponseUrl;
    }

    public void setVideoResponseUrl(String videoResponseUrl) {
        this.videoResponseUrl = videoResponseUrl;
    }

    public String getMessage() {
        return message;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getSubmitted() {
        return new SimpleDateFormat("H:mm dd-MM-yyyy").format(this.submitted);
    }

    public static List<ChallengeResponse> castLinkedTreeMapToChallengeResponseList(List<LinkedTreeMap<String, Object>> map){

        ArrayList<ChallengeResponse> responses = new ArrayList<>();

        for(LinkedTreeMap<String, Object> response : map){

            responses.add(castLinkedTreeMapToChallengeResponse(response));
        }

        return responses;
    }

    public static ChallengeResponse castLinkedTreeMapToChallengeResponse(LinkedTreeMap<String, Object> response) {

        long id = (long)((double) response.get("id"));
        ChallengeParticipation participation = ChallengeParticipation.castLinkedTreeMapToChallengeParticipation((LinkedTreeMap) response.get("challengeParticipation"));

        String isAcceptedString = (String) response.get("isAccepted");
        Character isAccepted = null;
        if(isAcceptedString != null){
            isAccepted = isAcceptedString.charAt(0);
        }
        String videoResponseUrl = String.valueOf(response.get("videoResponseUrl"));
        String message = String.valueOf(response.get("message"));
        String thumbnailUrl = String.valueOf(response.get("thumbnailUrl"));

        Date submitted = new Date();

        try {
            submitted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse((String) response.get("submitted"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new ChallengeResponse(id, participation, isAccepted, videoResponseUrl, message, submitted, thumbnailUrl);
    }

    public boolean isCurrentUserCanRate(){
        String creatorUsername = this.getChallengeParticipation().getChallenge().getCreator().getUsername();
        return !isDecided() &&
                UserService.getCurrentUsername().equals(creatorUsername);
    }

}
