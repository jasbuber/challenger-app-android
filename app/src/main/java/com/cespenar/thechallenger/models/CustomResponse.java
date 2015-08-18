package com.cespenar.thechallenger.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jasbuber on 2014-11-15.
 */
public class CustomResponse {

    public enum ResponseStatus {failure, success}

    private ResponseStatus status = ResponseStatus.success;

    private int rewardedPoints = 0;

    private List<String> messages = new ArrayList<String>();

    private List<Integer> points = new ArrayList<Integer>();

    private long challengeId;

    public CustomResponse(){}

    public CustomResponse(int points, String message){
        this.rewardedPoints = points;
        this.messages.add(message);
    }

    public CustomResponse(int points, List<String> messages){
        this.rewardedPoints = points;
        this.messages = messages;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public int getRewardedPoints() {
        return rewardedPoints;
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<String> addMessage(String message){
        this.messages.add(message);
        return this.messages;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<Integer> getPoints() {
        return points;
    }

    public void setPoints(List<Integer> points) {
        this.points = points;
    }

    public List<Integer> addPoints(Integer points){
        this.points.add(points);
        this.rewardedPoints += points;
        return this.points;
    }

    public long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(long challengeId) {
        this.challengeId = challengeId;
    }
}
