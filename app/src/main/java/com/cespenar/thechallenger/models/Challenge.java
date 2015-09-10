package com.cespenar.thechallenger.models;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jasbuber on 14/08/2015.
 */
public class Challenge {

    public enum CHALLENGE_CATEGORY{ ALL, FOOD_COMA, EARGASMIC, DRINKING_ZONE, FREAK_MODE, GIVING_BACK, FITNESS_AVENUE, MIND_GAMES, AQUA_SPHERE, OTHER }

    private String challengeName;
    private String videoPath;
    private CHALLENGE_CATEGORY category;
    private boolean visibility;
    private int difficulty;
    private Long id;
    private User creator;
    private float rating;

    public Challenge(){}

    public Challenge(String name, String videoPath, String category, boolean visibility, int difficulty) {
        this.challengeName = name;
        this.videoPath = videoPath;
        this.category = validateCategory(category);
        this.visibility = visibility;
        this.difficulty = validateDifficulty(difficulty);
    }

    public Challenge(long id, User creator, String name, float rating) {
        this.creator = creator;
        this.challengeName = name;
        this.difficulty = validateDifficulty(difficulty);
        this.rating = rating;
        this.id = id;
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

    /**
     * TO_DO Remove as soon as possible after Gson casting was figured out
     * @param map
     * @return
     */
    public static List<Challenge> castLinkedTreeMapToChallengeList(List<LinkedTreeMap<String, Object>> map){

        ArrayList<Challenge> challenges = new ArrayList<>();

        for(LinkedTreeMap<String, Object> challenge : map){
            String name = String.valueOf(challenge.get("challengeName"));
            double rating = (double)challenge.get("rating");
            float flRating = Float.valueOf(Double.toString(rating) + "f");
            LinkedTreeMap creator = (LinkedTreeMap) challenge.get("creator");
            long id = (long)((double) challenge.get("id"));

            challenges.add(new Challenge(id, User.castLinkedTreeMapToUser(creator), name, flRating));
        }

        return challenges;
    }
}
