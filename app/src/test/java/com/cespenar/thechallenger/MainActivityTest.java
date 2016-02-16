package com.cespenar.thechallenger;

import android.content.Intent;
import android.view.MenuItem;

import com.cespenar.thechallenger.services.FacebookService;
import com.cespenar.thechallenger.services.TutorialService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by jasbuber on 2016-01-18.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest{

    public FacebookService fbService;

    public TutorialService tService;

    private MainActivity spyActivity;

    @Before
    public void setUp(){

        fbService = mock(FacebookService.class);
        tService = mock(TutorialService.class);

        MainActivity activity = Robolectric.buildActivity(MainActivity.class).attach().get();
        spyActivity = spy(activity);

        doReturn(true).when(fbService).isAccessTokenValid();
        doReturn(fbService).when(spyActivity).getFacebookService();
        doReturn(tService).when(spyActivity).getTutorialService();
        doReturn("testUsername").when(spyActivity).getCurrentUsername();

        spyActivity.onCreate(null);

    }

    @Test
    public void testClickCreateBlock(){

        spyActivity.findViewById(R.id.create_challenge_block).performClick();

        Intent expected = new Intent(spyActivity, CreateChallengeActivity.class);

        assertEquals(shadowOf(spyActivity).getNextStartedActivity(), expected);
    }

    @Test
    public void testClickBrowseBlock(){

        spyActivity.findViewById(R.id.browse_challenges_block).performClick();

        Intent expected = new Intent(spyActivity, BrowseChallengesActivity.class);

        assertEquals(shadowOf(spyActivity).getNextStartedActivity(), expected);
    }

    @Test
    public void testClickRankingsBlock(){

        spyActivity.findViewById(R.id.rankings_block).performClick();

        Intent expected = new Intent(spyActivity, RankingsActivity.class);

        assertEquals(shadowOf(spyActivity).getNextStartedActivity(), expected);
    }

    @Test
    public void testClickMyChallengesBlock(){

        spyActivity.findViewById(R.id.my_challenges_block).performClick();

        Intent expected = new Intent(spyActivity, MyChallengesActivity.class);

        assertEquals(shadowOf(spyActivity).getNextStartedActivity(), expected);
    }

    @Test
    public void testClickProfileMenuItem(){

        RoboMenuItem item = new RoboMenuItem(R.id.action_profile);

        spyActivity.onOptionsItemSelected(item);
        Intent expected = new Intent(spyActivity, UserActivity.class);
        Intent started = shadowOf(spyActivity).getNextStartedActivity();

        assertThat("Intent error", expected.getComponent().getClassName(),
                equalTo(started.getComponent().getClassName()));

        assertEquals("Intent username error", started.getStringExtra("username"), "testUsername");
    }

    @Test
    public void testClickMyChallengesMenuItem(){

        RoboMenuItem item = new RoboMenuItem(R.id.action_my_challenges);

        spyActivity.onOptionsItemSelected(item);
        Intent expected = new Intent(spyActivity, CreatedChallengesActivity.class);
        Intent started = shadowOf(spyActivity).getNextStartedActivity();

        assertThat("Intent error", expected.getComponent().getClassName(),
                equalTo(started.getComponent().getClassName()));
    }

    @Test
    public void testClickMyParticipationsMenuItem(){

        RoboMenuItem item = new RoboMenuItem(R.id.action_my_participations);

        spyActivity.onOptionsItemSelected(item);
        Intent expected = new Intent(spyActivity, ChallengeParticipationsActivity.class);
        Intent started = shadowOf(spyActivity).getNextStartedActivity();

        assertThat("Intent error", expected.getComponent().getClassName(),
                equalTo(started.getComponent().getClassName()));
    }

    @Test
    public void testClickCreateChallengeMenuItem(){

        RoboMenuItem item = new RoboMenuItem(R.id.action_create_challenge);

        spyActivity.onOptionsItemSelected(item);
        Intent expected = new Intent(spyActivity, CreateChallengeActivity.class);
        Intent started = shadowOf(spyActivity).getNextStartedActivity();

        assertThat("Intent error", expected.getComponent().getClassName(),
                equalTo(started.getComponent().getClassName()));
    }

    @Test
    public void testClickBrowseChallengesMenuItem(){

        RoboMenuItem item = new RoboMenuItem(R.id.action_search);

        spyActivity.onOptionsItemSelected(item);
        Intent expected = new Intent(spyActivity, BrowseChallengesActivity.class);
        Intent started = shadowOf(spyActivity).getNextStartedActivity();

        assertThat("Intent error", expected.getComponent().getClassName(),
                equalTo(started.getComponent().getClassName()));
    }
}
