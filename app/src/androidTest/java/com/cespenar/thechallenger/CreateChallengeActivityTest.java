package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.lang.reflect.Method;

import android.net.Uri;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Jasbuber on 25/08/2015.
 */
public class CreateChallengeActivityTest extends ActivityUnitTestCase<CreateChallengeActivity> {

    private static CreateChallengeActivity activity;
    private static Bundle testBundle;
    private static Method onCreate;

    private static final String TEST_NAME = "Test challenge name";
    private static final int TEST_CATEGORY_POSITION = 2;
    private static final int TEST_PROGRESS = 2;
    private static final int TEST_VISIBILITY_POSITION = 1;
    private static final String TEST_VIDEO_NAME = "test.mp4";
    private static final String TEST_VIDEO_PATH = "mem/videos/" + TEST_VIDEO_NAME;

    public CreateChallengeActivityTest() throws Exception{
        super(CreateChallengeActivity.class);
        onCreate = CreateChallengeActivity.class.getDeclaredMethod("onCreate", Bundle.class);
        onCreate.setAccessible(true);
    }

    @Override
    public void setUp() throws Exception{
        super.setUp();
        startActivity(new Intent(Intent.ACTION_MAIN), null, null);
        activity = getActivity();
        testBundle = new Bundle();
    }

    public void testIfStepOneVisible(){
       assertEquals(activity.findViewById(R.id.first_step).getVisibility(), View.VISIBLE);
    }

    public void testIfStepTwoVisible(){
        assertEquals(activity.findViewById(R.id.second_step).getVisibility(), View.INVISIBLE);
    }

    public void testIfStepThreeVisible(){
        assertEquals(activity.findViewById(R.id.third_step).getVisibility(), View.INVISIBLE);
    }

    public void testShowSecondStep(){
        activity.findViewById(R.id.show_second_step_action).performClick();
        assertEquals(activity.findViewById(R.id.second_step).getVisibility(), View.VISIBLE);
        assertEquals(activity.findViewById(R.id.first_step_arrow).getVisibility(), View.VISIBLE);
    }

    public void testShowThirdStep(){
        activity.findViewById(R.id.show_third_step_action).performClick();
        assertEquals(activity.findViewById(R.id.third_step).getVisibility(), View.VISIBLE);
        assertEquals(activity.findViewById(R.id.second_step_arrow).getVisibility(), View.VISIBLE);
    }

    public void testSavingStateName(){
        ((EditText)activity.findViewById(R.id.create_challenge_name)).setText(TEST_NAME);
        ((CreateChallengeActivity)activity).onSaveInstanceState(testBundle);
        assertEquals(testBundle.getString("challengeName"), TEST_NAME);
    }

    public void testSavingStateCategory(){
        ((Spinner)activity.findViewById(R.id.create_challenge_category)).setSelection(TEST_CATEGORY_POSITION);
        ((CreateChallengeActivity)activity).onSaveInstanceState(testBundle);
        assertEquals(testBundle.getInt("challengeCategory"), TEST_CATEGORY_POSITION);
    }

    public void testSavingStateDifficulty(){
        ((SeekBar)activity.findViewById(R.id.create_challenge_difficulty)).setProgress(TEST_PROGRESS);
        ((CreateChallengeActivity)activity).onSaveInstanceState(testBundle);
        assertEquals(testBundle.getInt("challengeDifficulty"), TEST_PROGRESS);
    }

    public void testSavingStateVisibility(){
        ((Spinner)activity.findViewById(R.id.create_challenge_visibility)).setSelection(TEST_VISIBILITY_POSITION);
        ((CreateChallengeActivity)activity).onSaveInstanceState(testBundle);
        assertEquals(testBundle.getInt("challengeVisibility"), TEST_VISIBILITY_POSITION);
    }

    public void testSavingStateStepTwo(){
        activity.findViewById(R.id.show_second_step_action).performClick();
        ((CreateChallengeActivity)activity).onSaveInstanceState(testBundle);
        assertEquals(testBundle.getBoolean("isStep2Visible"), true);
    }

    public void testSavingStateStepThree(){
        activity.findViewById(R.id.show_third_step_action).performClick();
        ((CreateChallengeActivity)activity).onSaveInstanceState(testBundle);
        assertEquals(testBundle.getBoolean("isStep3Visible"), true);
    }

    public void testRestoreStateName() throws Exception{
        testBundle.putString("challengeName", TEST_NAME);
        onCreate.invoke(activity, testBundle);
        assertEquals(((EditText)activity.findViewById(R.id.create_challenge_name)).getText().toString(), TEST_NAME);
    }

    public void testRestoreStateCategory() throws Exception{
        testBundle.putInt("challengeCategory", TEST_CATEGORY_POSITION);
        onCreate.invoke(activity, testBundle);
        assertEquals(((Spinner)activity.findViewById(R.id.create_challenge_category)).getSelectedItemPosition(), TEST_CATEGORY_POSITION);
    }

    public void testRestoreStateDifficulty() throws Exception{
        testBundle.putInt("challengeDifficulty", TEST_PROGRESS);
        onCreate.invoke(activity, testBundle);
        assertEquals(((SeekBar)activity.findViewById(R.id.create_challenge_difficulty)).getProgress(), TEST_PROGRESS);
    }

    public void testRestoreStateVisibility() throws Exception{
        testBundle.putInt("challengeVisibility", TEST_VISIBILITY_POSITION);
        onCreate.invoke(activity, testBundle);
        assertEquals(((Spinner)activity.findViewById(R.id.create_challenge_visibility)).getSelectedItemPosition(), TEST_VISIBILITY_POSITION);
    }

    public void testRestoreStateStepTwo() throws Exception{
        testBundle.putBoolean("isStep2Visible", true);
        onCreate.invoke(activity, testBundle);
        assertEquals(activity.findViewById(R.id.second_step).getVisibility(), View.VISIBLE);
        assertEquals(activity.findViewById(R.id.first_step_arrow).getVisibility(), View.VISIBLE);
    }

    public void testRestoreStateWithoutStepThree() throws Exception{
        testBundle.putBoolean("isStep2Visible", true);
        onCreate.invoke(activity, testBundle);
        assertEquals(activity.findViewById(R.id.third_step).getVisibility(), View.INVISIBLE);
        assertEquals(activity.findViewById(R.id.second_step_arrow).getVisibility(), View.INVISIBLE);
    }

    public void testRestoreStateStepThree() throws Exception{
        testBundle.putBoolean("isStep2Visible", true);
        testBundle.putBoolean("isStep3Visible", true);
        onCreate.invoke(activity, testBundle);
        assertEquals(activity.findViewById(R.id.third_step).getVisibility(), View.VISIBLE);
        assertEquals(activity.findViewById(R.id.second_step_arrow).getVisibility(), View.VISIBLE);
    }

    public void testOnUploadVideoStartsIntent(){
        Button upload = (Button) activity.findViewById(R.id.create_challenge_upload);
        upload.performClick();
        assertNotNull(getStartedActivityIntent());
    }

}
