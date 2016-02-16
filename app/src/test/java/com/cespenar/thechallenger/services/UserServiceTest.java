package com.cespenar.thechallenger.services;

import com.android.volley.RequestQueue;
import com.cespenar.thechallenger.models.User;
import com.facebook.AccessToken;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by jasbuber on 2016-01-12.
 */
public class UserServiceTest {

    UserService spyService;
    RequestQueue queue;
    ArgumentCaptor<CustomRequest> requestCaptor;
    FacebookService fbService;
    AccessToken accessToken;

    @Before
    public void setUp(){
        fbService = mock(FacebookService.class);
        UserService service = UserService.getService(fbService);
        spyService = spy(service);
        UserService.setCurrentUser(new User("testUser", "testPictureUrl", "Test", "User"));
        accessToken = new AccessToken("testToken", "11", "fdf", null, null, null, null, null);

        queue = mock(RequestQueue.class);
        doReturn(queue).when(spyService).getRequestQueue(null);
        doReturn(accessToken).when(fbService).getAccessToken();

        requestCaptor = ArgumentCaptor.forClass(CustomRequest.class);
    }

    @Test
    public void testGetCurrentUser(){

        User currentUser =  new User("currentUser", "testPictureUrl", "Test", "User");
        UserService.setCurrentUser(currentUser);

        assertSame(currentUser, UserService.getCurrentUser());
    }

    @Test
    public void testGetCurrentUsername(){

        User currentUser =  new User("currentUser", "testPictureUrl", "Test", "User");
        UserService.setCurrentUser(currentUser);

        assertEquals(currentUser.getUsername(), UserService.getCurrentUsername());
    }

    @Test
    public void testCreateUserCorrectUrl(){

        spyService.createUser(null, new User("currentUser", "testPictureUrl", "Test", "User"));

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                endsWith(Router.getRouter().getRoute(Router.ROUTE_NAME.CREATE_USER).getUrl()));
    }

    @Test
    public void testCreateChallengeDataIncluded() throws Exception{

        spyService.createUser(null, new User("currentUser", "testPictureUrl", "Test", "User"));

        verify(queue).add(requestCaptor.capture());

        assertEquals("Username parameter error",
                "currentUser", requestCaptor.getValue().getParams().get("username"));
        assertEquals("Token parameter error",
                "testToken", requestCaptor.getValue().getParams().get("token"));
        assertEquals("Firstname parameter error",
                "Test", requestCaptor.getValue().getParams().get("firstName"));
        assertEquals("Lastname parameter error",
                "User", requestCaptor.getValue().getParams().get("lastName"));
    }

    @Test
    public void testGetProfileCorrectUrl(){

        spyService.getProfile(null, "currentUser");

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                containsString(Router.getRouter().getRoute(Router.ROUTE_NAME.GET_PROFILE).getUrl()));
    }

    @Test
    public void testGetProfileDataIncluded() throws Exception{

        spyService.getProfile(null, "currentUser");

        verify(queue).add(requestCaptor.capture());

        assertThat("Username in url error", requestCaptor.getValue().getUrl(), containsString("username=currentUser"));
    }

    @Test
    public void testCompleteTutorialCorrectUrl(){

        spyService.completeTutorial(null);

        verify(queue).add(requestCaptor.capture());

        assertThat(requestCaptor.getValue().getUrl(),
                endsWith(Router.getRouter().getRoute(Router.ROUTE_NAME.COMPLETE_TUTORIAL).getUrl()));
    }

    @Test
    public void testCompleteTutorialDataIncluded() throws Exception{

        spyService.completeTutorial(null);

        verify(queue).add(requestCaptor.capture());

        assertEquals("Username parameter error",
                "testUser", requestCaptor.getValue().getParams().get("username"));
        assertEquals("Token parameter error",
                "testToken", requestCaptor.getValue().getParams().get("token"));
    }


}
