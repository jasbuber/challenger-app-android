package com.cespenar.thechallenger.models;

import java.util.HashMap;

/**
 * Created by Jasbuber on 14/08/2015.
 */
public class Challenge {

    public enum CHALLENGE_CATEGORY{ ALL, FOOD_COMA, EARGASMIC, DRINKING_ZONE, FREAK_MODE, GIVING_BACK, FITNESS_AVENUE, MIND_GAMES, AQUA_SPHERE, OTHER }

    private String name;
    private String videoPath;
    private CHALLENGE_CATEGORY category;
    private boolean visibility;
    private int difficulty;

    public Challenge(){}

    public Challenge(String name, String videoPath, String category, boolean visibility, int difficulty) {
        this.name = name;
        this.videoPath = videoPath;
        this.category = validateCategory(category);
        this.visibility = visibility;
        this.difficulty = validateDifficulty(difficulty);
    }

    public String getName() {
        return name;
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

    public HashMap<String, String> getPropertyHashmap(){

        HashMap<String, String> properties = new HashMap<>();
        properties.put("name", name);
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

}
