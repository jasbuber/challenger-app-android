package com.cespenar.thechallenger;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.Comment;
import com.cespenar.thechallenger.models.User;
import com.cespenar.thechallenger.services.ChallengeService;
import com.cespenar.thechallenger.services.FacebookService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by jasbuber on 2016-02-01.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ChallengeActivityTest {

    public FacebookService fbService;

    public ChallengeService chService;

    private ChallengeActivity spyActivity;

    public Challenge challenge;

    @Before
    public void setUp() {

        fbService = mock(FacebookService.class);
        chService = mock(ChallengeService.class);

        ChallengeActivity activity =
                Robolectric.buildActivity(ChallengeActivity.class).attach().get();
        spyActivity = spy(activity);

        doReturn(true).when(fbService).isAccessTokenValid();
        doReturn(fbService).when(spyActivity).getFacebookService();
        doReturn(chService).when(spyActivity).getChallengeService();

        challenge = new Challenge(1L, new User("testUser1", "firstName1", "lastname1", "profilePic1"),
                "testChallenge1", Challenge.CHALLENGE_CATEGORY.AQUA_SPHERE, 3.5f, new Date(), 3, "");

        spyActivity.onCreate(null);
    }

    @Test
    public void testPopulateChallengeServices(){

        spyActivity.populateChallenge(challenge, 1, new ArrayList<Comment>());

        ImageView creatorPicture = (ImageView) spyActivity.findViewById(R.id.challenge_details_picture);

        verify(spyActivity.getChallengeService(), times(1)).getChallengeResponses(spyActivity, challenge);
        verify(spyActivity.getFacebookService(), times(1))
                .loadProfilePicture(spyActivity, creatorPicture, challenge.getCreator().getUsername());
    }

    @Test
    public void testPopulateChallengeData(){
        spyActivity.populateChallenge(challenge, 1, new ArrayList<Comment>());

        TextView challengeName = (TextView) spyActivity.findViewById(R.id.challenge_details_name);
        TextView challengeCreator = (TextView) spyActivity.findViewById(R.id.challenge_details_creator);

        assertEquals("Challenge name not filled", challenge.getName(), challengeName.getText());
        assertEquals("Challenge creator not filled",
                challenge.getCreator().getFormattedName(), challengeCreator.getText());
    }

    @Test
    public void testUserNotParticipatingState(){
        spyActivity.populateChallenge(challenge, 0, new ArrayList<Comment>());

        assertThat("Join button must be visible",
                spyActivity.findViewById(R.id.challenge_details_join).getVisibility(), equalTo(View.VISIBLE));
        assertThat("Respond button must be invisible",
                spyActivity.findViewById(R.id.challenge_details_show_respond).getVisibility(), not(equalTo(View.VISIBLE)));
        assertThat("Submit button must be invisible",
                spyActivity.findViewById(R.id.challenge_details_submit_response).getVisibility(), not(equalTo(View.VISIBLE)));
    }

    @Test
    public void testUserParticipatingState(){
        spyActivity.populateChallenge(challenge, 1, new ArrayList<Comment>());

        assertThat("Join button must be invisible",
                spyActivity.findViewById(R.id.challenge_details_join).getVisibility(), not(equalTo(View.VISIBLE)));
        assertThat("Respond button must be visible",
                spyActivity.findViewById(R.id.challenge_details_show_respond).getVisibility(), equalTo(View.VISIBLE));
        assertThat("Submit button must be invisible",
                spyActivity.findViewById(R.id.challenge_details_submit_response).getVisibility(), not(equalTo(View.VISIBLE)));
    }

}
