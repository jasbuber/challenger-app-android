package com.cespenar.thechallenger.models;

import junit.framework.TestCase;

import java.util.HashMap;

/**
 * Created by Jasbuber on 21/08/2015.
 */
public class ChallengeTest extends TestCase {

    private static final String TEST_NAME = "testName";
    private static final String TEST_CATEGORY = "AQUA SPHERE";
    private static final Challenge.CHALLENGE_CATEGORY TEST_CATEGORY_EXPECTED = Challenge.CHALLENGE_CATEGORY.AQUA_SPHERE;
    private static final String TEST_VIDEO_PATH = "memory/falalala.mp4";


    public void testCreateChallenge() {
        Challenge challenge = new Challenge();
    }

    public void testSetDifficultyTooBig() {
        Challenge challenge = new Challenge(TEST_NAME, TEST_VIDEO_PATH, TEST_CATEGORY, false, 111);
        assertEquals(challenge.getDifficulty(), 1);
    }

    public void testSetDifficultyTooSmall() {
        Challenge challenge = new Challenge(TEST_NAME, TEST_VIDEO_PATH, TEST_CATEGORY, false, -1);
        assertEquals(challenge.getDifficulty(), 1);
    }

    public void testSetDifficulty() {
        Challenge challenge = new Challenge(TEST_NAME, TEST_VIDEO_PATH, TEST_CATEGORY, false, 3);
        assertEquals(challenge.getDifficulty(), 3);
    }

    public void testSetCategory() {
        Challenge challenge = new Challenge(TEST_NAME, TEST_VIDEO_PATH, TEST_CATEGORY, false, 111);
        assertEquals(challenge.getCategory(), TEST_CATEGORY_EXPECTED);
    }

    public void testSetWrongCategory() {
        Challenge challenge = new Challenge(TEST_NAME, TEST_VIDEO_PATH, "Wrong category", false, 111);
        assertEquals(challenge.getCategory(), Challenge.CHALLENGE_CATEGORY.ALL);
        assertEquals(Challenge.CHALLENGE_CATEGORY.valueOf("OTHER"), Challenge.CHALLENGE_CATEGORY.OTHER);
    }

    public void testGetCategoryByString() {
        assertEquals(Challenge.CHALLENGE_CATEGORY.valueOf("OTHER"), Challenge.CHALLENGE_CATEGORY.OTHER);
    }

    public void testGetters() {
        Challenge challenge = new Challenge(TEST_NAME, TEST_VIDEO_PATH, TEST_CATEGORY, false, 111);
        assertTrue(challenge.getName().equals(TEST_NAME));
        assertTrue(challenge.getVideoPath().equals(TEST_VIDEO_PATH));
        assertFalse(challenge.isVisibile());
    }

    public void testGetPropertyHashMap() {

        Challenge challenge = new Challenge(TEST_NAME, TEST_VIDEO_PATH, TEST_CATEGORY, false, 2);

        HashMap<String, String> params = challenge.getPropertyHashmap();

        assertTrue(params.get("name").equals(TEST_NAME));
        assertNull(params.get("videoPath"));
        assertTrue(params.get("category").equals(TEST_CATEGORY_EXPECTED.toString()));
        assertEquals(Integer.parseInt(params.get("difficulty")), 2);
        assertEquals(Boolean.parseBoolean(params.get("visibility")), false);
    }
}
