package com.cespenar.thechallenger.models;

import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jasbuber on 14/08/2015.
 */
public class Challenge implements Serializable {

    public enum CHALLENGE_CATEGORY{ ALL, FOOD_COMA, EARGASMIC, DRINKING_ZONE, FREAK_MODE, GIVING_BACK, FITNESS_AVENUE, MIND_GAMES, AQUA_SPHERE, OTHER }

    public enum CHALLENGE_DIFFICULTY { EASY, MEDIUM, HARD, INSANE }

    public static final int POPULARITY_LEVEL_1 = 3;
    public static final int POPULARITY_LEVEL_2 = 8;
    public static final int POPULARITY_LEVEL_3 = 15;
    public static final int POPULARITY_LEVEL_4 = 25;

    private String challengeName;
    private String videoPath;
    private CHALLENGE_CATEGORY category;
    private boolean visibility;
    private int difficulty;
    private Long id;
    private User creator;
    private float rating;
    private Date creationDate;

    public Challenge(){}

    public Challenge(String name, String videoPath, String category, boolean visibility, int difficulty) {
        this.challengeName = name;
        this.videoPath = videoPath;
        this.category = validateCategory(category);
        this.visibility = visibility;
        this.difficulty = validateDifficulty(difficulty);
    }

    public Challenge(long id, User creator, String name, CHALLENGE_CATEGORY category, float rating, Date creationDate, int difficulty, String videoPath) {
        this.creator = creator;
        this.challengeName = name;
        this.difficulty = validateDifficulty(difficulty);
        this.rating = rating;
        this.id = id;
        this.creationDate = creationDate;
        this.category = category;
        this.videoPath = videoPath;
    }

    public String getName() {
        return challengeName;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public CHALLENGE_CATEGORY getCategory() {
        return category;
    }

    public boolean isVisibile() {
        return visibility;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getFormattedDifficulty(){
        return CHALLENGE_DIFFICULTY.values()[difficulty].toString();
    }

    public long getId() {
        return id;
    }

    public HashMap<String, String> getPropertyHashmap(){

        HashMap<String, String> properties = new HashMap<>();
        properties.put("challengeName", challengeName);
        properties.put("category", String.valueOf(category));
        properties.put("visibility", String.valueOf(visibility));
        properties.put("difficulty", String.valueOf(difficulty));

        return properties;
    }

    private int validateDifficulty(int difficulty){
        if(difficulty < 0 || difficulty > 3){
            return 1;
        }

        return difficulty;
    }

    private CHALLENGE_CATEGORY validateCategory(String category){

        String formattedCategory = category.replace(" ", "_").toUpperCase();
        for(CHALLENGE_CATEGORY cat : CHALLENGE_CATEGORY.values()){
            if(cat.toString().equals(formattedCategory)){
                return cat;
            }
        }

        return CHALLENGE_CATEGORY.ALL;

    }

    public User getCreator() {
        return creator;
    }

    public float getRating() {
        return rating;
    }

    public String getCreationDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(this.creationDate);
    }

    public boolean isVisibility() {
        return visibility;
    }

    /**
     * TO_DO Remove as soon as possible after Gson casting was figured out
     * @param map
     * @return
     */
    public static List<Challenge> castLinkedTreeMapToChallengeList(List<LinkedTreeMap<String, Object>> map){

        ArrayList<Challenge> challenges = new ArrayList<>();

        for(LinkedTreeMap<String, Object> challenge : map){

            challenges.add(castLinkedTreeMapToChallenge(challenge));
        }

        return challenges;
    }

    public static Challenge castLinkedTreeMapToChallenge(LinkedTreeMap<String, Object> challenge) {

        String name = String.valueOf(challenge.get("challengeName"));
        double rating = (double)challenge.get("rating");
        float flRating = Float.valueOf(Double.toString(rating) + "f");
        LinkedTreeMap creator = (LinkedTreeMap) challenge.get("creator");
        long id = (long)((double) challenge.get("id"));
        Date creationDate = new Date();
        int difficulty = ((Double) challenge.get("difficulty")).intValue();
        Challenge.CHALLENGE_CATEGORY category = CHALLENGE_CATEGORY.valueOf((String) challenge.get("category"));
        String videoId = String.valueOf(challenge.get("videoId"));
        try {
            creationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse((String) challenge.get("creationDate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Challenge(id, User.castLinkedTreeMapToUser(creator), name, category, flRating, creationDate, difficulty, videoId);
    }

    public static Integer getPopularityLevel(Long participantsNr){

        if(participantsNr < POPULARITY_LEVEL_1){
            return 1;
        }else if(participantsNr < POPULARITY_LEVEL_2){
            return 2;
        }else if(participantsNr < POPULARITY_LEVEL_3){
            return 3;
        }else if(participantsNr < POPULARITY_LEVEL_4){
            return 4;
        }else{
            return 5;
        }

    }


}
