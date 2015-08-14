package com.cespenar.thechallenger.models;

/**
 * Created by Jasbuber on 14/08/2015.
 */
public class Challenge {

    private String name;
    private String videoPath;
    private int category;
    private boolean visibility;
    private int difficulty;

    public Challenge(){}

    public Challenge(String name, String videoPath, int category, boolean visibility, int difficulty) {
        this.name = name;
        this.videoPath = videoPath;
        this.category = category;
        this.visibility = visibility;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public int getCategory() {
        return category;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
