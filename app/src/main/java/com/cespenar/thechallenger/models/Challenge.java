package com.cespenar.thechallenger.models;

import java.util.HashMap;

/**
 * Created by Jasbuber on 14/08/2015.
 */
public class Challenge {

    private String name;
    private String videoPath;
    private String category;
    private boolean visibility;
    private int difficulty;

    public Challenge(){}

    public Challenge(String name, String videoPath, String category, boolean visibility, int difficulty) {
        this.name = name;
        this.videoPath = videoPath;
        this.category = category.replace(" ", "_").toUpperCase();
        this.visibility = visibility;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getCategory() {
        return category;
    }

    public boolean isVisibility() {
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

}
