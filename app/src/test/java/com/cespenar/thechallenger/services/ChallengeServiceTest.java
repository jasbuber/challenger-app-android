package com.cespenar.thechallenger.services;

import android.app.Activity;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.cespenar.thechallenger.BuildConfig;
import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.User;
import com.facebook.AccessToken;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by jasbuber on 2016-01-07.
 */
public class ChallengeServiceTest {

    FacebookService fbService;
    ChallengeService spyService;
    AccessToken accessToken;
    RequestQueue queue;
    ArgumentCaptor<CustomRequest> requestCaptor;

    @Before
    public void setUp(){
        fbService = mock(FacebookService.class);
        ChallengeService service = ChallengeService.getService(fbService);
        spyService = spy(service);
        accessToken = new AccessToken("testToken", "11", "fdf", null, null, null, null, null);
        UserService.setCurrentUser(new User("testUser", "testPictureUrl", "Test", "User"));

        queue = mock(RequestQueue.class);
        doReturn(accessToken).when(fbService).getAccessToken();
        doReturn(queue).when(spyService).getRequestQueue(null);

        requestCaptor = ArgumentCaptor.forClass(CustomRequest.class);
    }

    @Test
    public void testCreateChallengeCorrectUrl(){

        spyService.createChallenge(null, new Challenge("testChallenge", "testVideoPath", "AQUA_SPHERE", true, 3));

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                endsWith(Router.getRouter().getRoute(Router.ROUTE_NAME.CREATE_CHALLENGE).getUrl()));
    }

    @Test
    public void testCreateChallengeDataIncluded() throws Exception{

        spyService.createChallenge(null, new Challenge("testChallenge", "testVideoPath", "AQUA_SPHERE", true, 3));

        verify(queue).add(requestCaptor.capture());

        assertEquals("ChallengeName parameter error",
                "testChallenge", requestCaptor.getValue().getParams().get("challengeName"));
        assertEquals("Username parameter error",
                "testUser", requestCaptor.getValue().getParams().get("username"));
        assertEquals("Token parameter error",
                "testToken", requestCaptor.getValue().getParams().get("token"));
    }

    @Test
    public void testGetLatestChallengesCorrectUrl(){

        spyService.getLatestChallenges(null);

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                containsString(Router.getRouter().getRoute(Router.ROUTE_NAME.LATEST_CHALLENGES).getUrl()));
    }

    @Test
    public void testGetLatestChallengesDataIncluded() throws Exception{

        spyService.getLatestChallenges(null);

        verify(queue).add(requestCaptor.capture());

        assertThat("Username in url error", requestCaptor.getValue().getUrl(), containsString("username=testUser"));
    }

    @Test
    public void testGetChallengesByCriteriaCorrectUrl(){

        spyService.getChallengesByCriteria(null, "testPhrase", Challenge.CHALLENGE_CATEGORY.ALL, 3,
                ChallengeService.SORTING_ORDER.RECENT);

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                containsString(Router.getRouter().getRoute(Router.ROUTE_NAME.FIND_CHALLENGES).getUrl()));
    }

    @Test
    public void testGetChallengesByCriteriaDataIncluded() throws Exception{

        spyService.getChallengesByCriteria(null, "testPhrase", Challenge.CHALLENGE_CATEGORY.ALL, 3,
                ChallengeService.SORTING_ORDER.RECENT);

        verify(queue).add(requestCaptor.capture());

        assertThat("Username in url error", requestCaptor.getValue().getUrl(), containsString("username=testUser"));
        assertThat("Category in url error", requestCaptor.getValue().getUrl(), containsString("category=ALL"));
        assertThat("Phrase in url error", requestCaptor.getValue().getUrl(), containsString("phrase=testPhrase"));
        assertThat("Page in url error", requestCaptor.getValue().getUrl(), containsString("page=3"));
        assertThat("Order in url error", requestCaptor.getValue().getUrl(), containsString("order=RECENT"));
    }

    @Test
    public void testGetChallengesResponsesCorrectUrl(){

        spyService.getChallengeResponses(null, new Challenge(22L, UserService.getCurrentUser(),
                "testChallenge", Challenge.CHALLENGE_CATEGORY.ALL, 4.0f, new Date(), 3, "testVideoPath"));

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                containsString(Router.getRouter().getRoute(Router.ROUTE_NAME.CHALLENGE_RESPONSES).getUrl()));
    }

    @Test
    public void testGetChallengesResponsesDataIncluded(){

        spyService.getChallengeResponses(null, new Challenge(22L, UserService.getCurrentUser(),
                "testChallenge", Challenge.CHALLENGE_CATEGORY.ALL, 4.0f, new Date(), 3, "testVideoPath"));

        verify(queue).add(requestCaptor.capture());

        assertThat("Username in url error", requestCaptor.getValue().getUrl(), containsString("username=testUser"));
        assertThat("ChallengeId in url error", requestCaptor.getValue().getUrl(), containsString("challengeId=22"));
        assertThat("Token in url error", requestCaptor.getValue().getUrl(), containsString("token=testToken"));
    }

    @Test
    public void testJoinChallengeCorrectUrl(){

        spyService.joinChallenge(null, new Challenge(22L, UserService.getCurrentUser(),
                "testChallenge", Challenge.CHALLENGE_CATEGORY.ALL, 4.0f, new Date(), 3, "testVideoPath"));

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                containsString(Router.getRouter().getRoute(Router.ROUTE_NAME.JOIN_CHALLENGE).getUrl()));
    }

    @Test
    public void testJoinChallengeDataIncluded() throws Exception{

        spyService.joinChallenge(null, new Challenge(22L, UserService.getCurrentUser(),
                "testChallenge", Challenge.CHALLENGE_CATEGORY.ALL, 4.0f, new Date(), 3, "testVideoPath"));

        verify(queue).add(requestCaptor.capture());

        assertEquals("ChallengeId parameter error",
                "22", requestCaptor.getValue().getParams().get("challengeId"));
        assertEquals("Username parameter error",
                "testUser", requestCaptor.getValue().getParams().get("username"));
        assertEquals("Token parameter error",
                "testToken", requestCaptor.getValue().getParams().get("token"));
        assertEquals("Token parameter error",
                "Test Use", requestCaptor.getValue().getParams().get("fullName"));
    }

    @Test
    public void testGetMyChallengesCorrectUrl(){

        spyService.getMyChallenges(null, 1);

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                containsString(Router.getRouter().getRoute(Router.ROUTE_NAME.MY_CHALLENGES).getUrl()));
    }

    @Test
    public void testGetMyChallengesDataIncluded() throws Exception{

        spyService.getMyChallenges(null, 3);

        verify(queue).add(requestCaptor.capture());

        assertThat("Username in url error", requestCaptor.getValue().getUrl(), containsString("username=testUser"));
        assertThat("Page in url error", requestCaptor.getValue().getUrl(), containsString("page=3"));
    }

    @Test
    public void testGetMyParticipationsCorrectUrl(){

        spyService.getMyParticipations(null, 1);

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                containsString(Router.getRouter().getRoute(Router.ROUTE_NAME.MY_PARTICIPATIONS).getUrl()));
    }

    @Test
    public void testGetMyParticipationsDataIncluded() throws Exception{

        spyService.getMyParticipations(null, 3);

        verify(queue).add(requestCaptor.capture());

        assertThat("Username in url error", requestCaptor.getValue().getUrl(), containsString("username=testUser"));
        assertThat("Page in url error", requestCaptor.getValue().getUrl(), containsString("page=3"));
    }

    @Test
    public void testGetChallengeCorrectUrl(){

        spyService.getChallenge(null, 5L, null);

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                containsString(Router.getRouter().getRoute(Router.ROUTE_NAME.GET_CHALLENGE).getUrl()));
    }

    @Test
    public void testGetChallengeDataIncluded() throws Exception{

        spyService.getChallenge(null, 5L, null);

        verify(queue).add(requestCaptor.capture());

        assertThat("Username in url error", requestCaptor.getValue().getUrl(), containsString("username=testUser"));
        assertThat("ChallengeId in url error", requestCaptor.getValue().getUrl(), containsString("id=5"));
    }

    @Test
    public void testGetRankingsCorrectUrl(){

        spyService.getRankings(null);

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                containsString(Router.getRouter().getRoute(Router.ROUTE_NAME.RANKINGS).getUrl()));
    }

    @Test
    public void testSubmitResponseCorrectUrl(){

        spyService.submitChallengeResponse(null, 5L, "videoTestId");

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                containsString(Router.getRouter().getRoute(Router.ROUTE_NAME.SUBMIT_RESPONSE).getUrl()));
    }

    @Test
    public void testSubmitResponseDataIncluded() throws Exception{

        spyService.submitChallengeResponse(null, 5L, "videoTestId");

        verify(queue).add(requestCaptor.capture());

        assertEquals("VideoId parameter error",
                "videoTestId", requestCaptor.getValue().getParams().get("videoId"));
        assertEquals("Username parameter error",
                "testUser", requestCaptor.getValue().getParams().get("username"));
        assertEquals("Token parameter error",
                "testToken", requestCaptor.getValue().getParams().get("token"));
        assertEquals("ChallengeId parameter error",
                "5", requestCaptor.getValue().getParams().get("challengeId"));
    }

    @Test
    public void testRateChallengeResponseCorrectUrl(){

        spyService.rateChallengeResponse(null, 55L, 'Y');

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                containsString(Router.getRouter().getRoute(Router.ROUTE_NAME.RATE_RESPONSE).getUrl()));
    }

    @Test
    public void testRateChallengeResponseDataIncluded() throws Exception{

        spyService.rateChallengeResponse(null, 55L, 'Y');

        verify(queue).add(requestCaptor.capture());

        assertEquals("ResponseId parameter error",
                "55", requestCaptor.getValue().getParams().get("responseId"));
        assertEquals("Username parameter error",
                "testUser", requestCaptor.getValue().getParams().get("username"));
        assertEquals("Token parameter error",
                "testToken", requestCaptor.getValue().getParams().get("token"));
        assertEquals("isAccepted parameter error",
                "Y", requestCaptor.getValue().getParams().get("isAccepted"));
    }

    @Test
    public void testGetVideoCorrectUrl(){

        spyService.getVideo(null, "testVideoId", null);

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                containsString(Router.getRouter().getRoute(Router.ROUTE_NAME.GET_VIDEO).getUrl()));
    }

    @Test
    public void testGetVideoDataIncluded() throws Exception{

        spyService.getVideo(null, "testVideoId", null);

        verify(queue).add(requestCaptor.capture());

        assertThat("Token in url error", requestCaptor.getValue().getUrl(), containsString("token=testToken"));
        assertThat("VideoId in url error", requestCaptor.getValue().getUrl(), containsString("videoId=testVideoId"));
    }

    @Test
    public void testGetVideoNotProcessedWithEmptyVideoId() throws Exception{

        spyService.getVideo(null, "", mock(VideoView.class));

        verify(queue, never()).add(requestCaptor.capture());
    }

    @Test
    public void testGetVideoNotProcessedWithNullVideoId() throws Exception{

        spyService.getVideo(null, null, mock(VideoView.class));

        verify(queue, never()).add(requestCaptor.capture());
    }

    @Test
    public void testGetVideoNotProcessedWithNullStringVideoId() throws Exception{

        spyService.getVideo(null, "null", mock(VideoView.class));

        verify(queue, never()).add(requestCaptor.capture());
    }

    @Test
    public void testRareChallengeCorrectUrl(){

        spyService.rateChallenge(null, 4L, 4);

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                containsString(Router.getRouter().getRoute(Router.ROUTE_NAME.RATE_CHALLENGE).getUrl()));
    }

    @Test
    public void testRateChallengeDataIncluded() throws Exception{

        spyService.rateChallenge(null, 44L, 4);

        verify(queue).add(requestCaptor.capture());

        assertEquals("ChallengeId parameter error",
                "44", requestCaptor.getValue().getParams().get("challengeId"));
        assertEquals("Username parameter error",
                "testUser", requestCaptor.getValue().getParams().get("username"));
        assertEquals("Token parameter error",
                "testToken", requestCaptor.getValue().getParams().get("token"));
        assertEquals("Rating parameter error",
                "4", requestCaptor.getValue().getParams().get("rating"));
    }

    @Test
    public void testCreateCommentCorrectUrl(){

        spyService.createComment(null, 42L, "testCommentMessage");

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                containsString(Router.getRouter().getRoute(Router.ROUTE_NAME.CREATE_COMMENT).getUrl()));
    }

    @Test
    public void testCreateCommentDataIncluded() throws Exception{

        spyService.createComment(null, 42L, "testCommentMessage");

        verify(queue).add(requestCaptor.capture());

        assertEquals("ChallengeId parameter error",
                "42", requestCaptor.getValue().getParams().get("challengeId"));
        assertEquals("Username parameter error",
                "testUser", requestCaptor.getValue().getParams().get("username"));
        assertEquals("Token parameter error",
                "testToken", requestCaptor.getValue().getParams().get("token"));
        assertEquals("Message parameter error",
                "testCommentMessage", requestCaptor.getValue().getParams().get("message"));
    }

    @Test
    public void testGetCommentsCorrectUrl(){

        spyService.getComments(null, 30L, 5);

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                containsString(Router.getRouter().getRoute(Router.ROUTE_NAME.GET_COMMENTS).getUrl()));
    }

    @Test
    public void testGetCommentsDataIncluded() throws Exception{

        spyService.getComments(null, 30L, 5);

        verify(queue).add(requestCaptor.capture());

        assertThat("ChallengeId in url error", requestCaptor.getValue().getUrl(), containsString("challengeId=30"));
        assertThat("Offset in url error", requestCaptor.getValue().getUrl(), containsString("offset=5"));
    }


}
