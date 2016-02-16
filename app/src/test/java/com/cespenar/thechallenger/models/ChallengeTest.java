package com.cespenar.thechallenger.models;

import com.google.gson.internal.LinkedTreeMap;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Jasbuber on 21/08/2015.
 */
public class ChallengeTest {

    private static final String TEST_NAME = "testName";
    private static final String TEST_CATEGORY = "AQUA_SPHERE";
    private static final Challenge.CHALLENGE_CATEGORY TEST_CATEGORY_EXPECTED = Challenge.CHALLENGE_CATEGORY.AQUA_SPHERE;
    private static final String TEST_VIDEO_PATH = "memory/falalala.mp4";
    private static final double TEST_ID = 999.00;
    private static final double TEST_RATING = 4.0;
    private static final String TEST_DATE = "2015-08-04 21:57:33.545";
    private static final int TEST_DIFFICULTY = 2;
    private static final User TEST_USER = new User("testUsername", "photoUrl", "firstName", "lastName", 0, 0, 0);
    private static final String TEST_USERNAME = "testUsername";


    @Test
    public void testCreateChallenge() {
        Challenge challenge = new Challenge();
    }

    @Test
    public void testSetDifficultyTooBig() {
        Challenge challenge = new Challenge(TEST_NAME, TEST_VIDEO_PATH, TEST_CATEGORY, false, 111);
        assertEquals(challenge.getDifficulty(), 1);
    }

    @Test
    public void testSetDifficultyTooSmall() {
        Challenge challenge = new Challenge(TEST_NAME, TEST_VIDEO_PATH, TEST_CATEGORY, false, -1);
        assertEquals(challenge.getDifficulty(), 1);
    }

    @Test
    public void testSetDifficulty() {
        Challenge challenge = new Challenge(TEST_NAME, TEST_VIDEO_PATH, TEST_CATEGORY, false, 3);
        assertEquals(challenge.getDifficulty(), 3);
    }

    @Test
    public void testSetCategory() {
        Challenge challenge = new Challenge(TEST_NAME, TEST_VIDEO_PATH, TEST_CATEGORY, false, 111);
        assertEquals(challenge.getCategory(), TEST_CATEGORY_EXPECTED);
    }

    @Test
    public void testSetWrongCategory() {
        Challenge challenge = new Challenge(TEST_NAME, TEST_VIDEO_PATH, "Wrong category", false, 111);
        assertEquals(challenge.getCategory(), Challenge.CHALLENGE_CATEGORY.ALL);
        assertEquals(Challenge.CHALLENGE_CATEGORY.valueOf("OTHER"), Challenge.CHALLENGE_CATEGORY.OTHER);
    }

    @Test
    public void testGetCategoryByString() {
        assertEquals(Challenge.CHALLENGE_CATEGORY.valueOf("OTHER"), Challenge.CHALLENGE_CATEGORY.OTHER);
    }

    @Test
    public void testGetters() {
        Challenge challenge = new Challenge(TEST_NAME, TEST_VIDEO_PATH, TEST_CATEGORY, false, 111);
        assertTrue(challenge.getName().equals(TEST_NAME));
        assertTrue(challenge.getVideoPath().equals(TEST_VIDEO_PATH));
        assertFalse(challenge.isVisibile());
    }

    @Test
    public void testGetPropertyHashMap() {

        Challenge challenge = new Challenge(TEST_NAME, TEST_VIDEO_PATH, TEST_CATEGORY, false, TEST_DIFFICULTY);

        HashMap<String, String> params = challenge.getPropertyHashmap();

        assertTrue(params.get("challengeName").equals(TEST_NAME));
        assertNull(params.get("videoPath"));
        assertTrue(params.get("category").equals(TEST_CATEGORY_EXPECTED.toString()));
        assertEquals(Integer.parseInt(params.get("difficulty")), TEST_DIFFICULTY);
        assertEquals(Boolean.parseBoolean(params.get("visibility")), false);
    }

    @Test
    public void testGetFormattedDifficulty(){
        Challenge challenge = new Challenge(TEST_NAME, TEST_VIDEO_PATH, TEST_CATEGORY, false, TEST_DIFFICULTY);
        assertEquals(challenge.getFormattedDifficulty(), Challenge.CHALLENGE_DIFFICULTY.HARD.toString());
    }

    @Test
    public void testCastingLinkedTreeMapToChallenge(){

        Challenge challenge = Challenge.castLinkedTreeMapToChallenge(getChallengeLinkedTreeMap());

        assertEquals(TEST_ID, challenge.getId(), 0.1);
    }

    private LinkedTreeMap<String, Object> getUserLinkedTreeMap(){
        LinkedTreeMap<String, Object> userMap = new LinkedTreeMap<>();
        userMap.put("username", TEST_USERNAME);
        userMap.put("profilePicUrl", "profilePicUrl");
        userMap.put("firstName", "firstName");
        userMap.put("lastName", "lastName");
        userMap.put("creationPoints", 0.0);
        userMap.put("participationPoints", 0.0);
        userMap.put("otherPoints", 0.0);

        return userMap;
    }

    private LinkedTreeMap<String, Object> getChallengeLinkedTreeMap(){
        LinkedTreeMap<String, Object> challengeMap = new LinkedTreeMap<>();
        challengeMap.put("id", TEST_ID);
        challengeMap.put("rating", TEST_RATING);
        challengeMap.put("creator", getUserLinkedTreeMap());
        challengeMap.put("name", TEST_NAME);
        challengeMap.put("difficulty", (double) TEST_DIFFICULTY);
        challengeMap.put("creationDate", TEST_DATE);
        challengeMap.put("category", Challenge.CHALLENGE_CATEGORY.AQUA_SPHERE.toString());
        challengeMap.put("videoId", TEST_VIDEO_PATH);

        return challengeMap;
    }

}
