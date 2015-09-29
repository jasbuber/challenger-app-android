package com.cespenar.thechallenger.services;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.cespenar.thechallenger.CreateChallengeFinalizeActivity;
import com.cespenar.thechallenger.R;
import com.cespenar.thechallenger.models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by Jasbuber on 14/08/2015.
 */
public class FacebookService {

    private static FacebookService service;

    public static CallbackManager callbackManager;

    private FacebookService() {
    }

    public static FacebookService getService() {
        if (service == null) {
            service = new FacebookService();
        }

        return service;
    }

    public void logIn(final Activity activity) {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        setCurrentUser(Profile.getCurrentProfile());
                        UserService.getService().createUser(activity, UserService.getCurrentUser());
                    }

                    @Override
                    public void onCancel() {
                        Log.e("cancel", "cancel");
                        activity.setContentView(R.layout.activity_main);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("error", exception.toString());
                    }
                });

        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("user_videos"));
    }

    public static boolean isAccessTokenValid() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        return (token != null && !token.isExpired() && token.getPermissions().contains("user_videos"));
    }

    public static boolean isAccessTokenValidForPublishing() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        return (token != null && !token.isExpired() && token.getPermissions().contains("publish_actions"));
    }

    public AccessToken getAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }

    private void setCurrentUser(Profile profile) {

        String username = profile.getId();
        String firstName = profile.getFirstName();
        String lastName = profile.getLastName();
        String profilePicUrl = profile.getProfilePictureUri(32, 32).toString();

        User user = new User(username, profilePicUrl, firstName, lastName);
        UserService.setCurrentUser(user);
    }

    public void acquirePublishPermission(final Activity activity, final String path, final String description, final ProgressBar progressBar, final long challengeId) {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        setCurrentUser(Profile.getCurrentProfile());
                        publishVideo(activity, path, description, progressBar, challengeId);
                    }

                    @Override
                    public void onCancel() {
                        Log.e("cancel", "cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("error", exception.toString());
                    }
                });

        LoginManager.getInstance().logInWithPublishPermissions(activity, Arrays.asList("publish_actions"));
    }

    public void publishVideo(final Activity activity, String path, String description, ProgressBar progressBar, final long challengeId) {

        if(!isAccessTokenValidForPublishing()) {
            acquirePublishPermission(activity, path, description, progressBar, challengeId);
            return;
        }

        Ion.with(activity)
                .load("https://graph-video.facebook.com/" + UserService.getCurrentUser().getUsername() + "/videos?access_token=" + getAccessToken().getToken())
                .uploadProgressBar(progressBar)
                .setMultipartParameter("description", description)
                .setMultipartFile("file", "video", new File(path))
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                String videoId = result.get("id").getAsString();

                ChallengeService.getService().updateChallengeVideo(activity, challengeId, videoId);
            }
        });
    }

}
