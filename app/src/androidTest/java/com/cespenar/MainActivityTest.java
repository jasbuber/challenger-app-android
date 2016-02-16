package com.cespenar.thechallenger;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.test.MoreAsserts;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.cespenar.FacebookLoginIdlingResource;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Jasbuber on 24/08/2015.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest{

    FacebookLoginIdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void before() {
        idlingResource = new FacebookLoginIdlingResource(mActivityRule.getActivity());
        Espresso.registerIdlingResources(idlingResource);
    }
    @After
    public void after() {
        Espresso.unregisterIdlingResources(idlingResource);

    }
/*
    @Test
    public void testMainScreen(){
       onView(withId(R.id.create_challenge_block)).check(matches(isDisplayed()));

    }*/

    @Test
    public void testCreateChallengeFlow(){
        onView(withId(R.id.create_challenge_block)).perform(click());
        onView(withId(R.id.create_challenge_name)).perform(typeText("testChallenge"));
        closeSoftKeyboard();
        onView(withId(R.id.create_challenge_category)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("AQUA SPHERE"))).perform(click());
        onView(withId(R.id.show_second_step_action)).perform(click());
        onView(withId(R.id.create_challenge_difficulty)).perform(setProgress(2));
        onView(withId(R.id.show_third_step_action)).perform(click());

        Intent resultData = new Intent();
        resultData.setData(Uri.parse("testLocation"));
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        Matcher<Intent> expectedIntent = hasAction(Intent.ACTION_PICK);
        Intents.init();
        intending(expectedIntent).respondWith(result);

        onView(withId(R.id.create_challenge_upload)).perform(click());

        intended(expectedIntent);
        Intents.release();
        onView(withId(R.id.create_challenge_category)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("AQUA SPHERE"))).perform(click());


    }

    public static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }
            @Override
            public String getDescription() {
                return "Set a progress on a SeekBar";
            }
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }

}
