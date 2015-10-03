package com.cespenar.thechallenger.services;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.cespenar.thechallenger.ChallengeActivity;
import com.cespenar.thechallenger.CreateChallengeActivity;
import com.cespenar.thechallenger.CreateChallengeFinalizeActivity;
import com.cespenar.thechallenger.R;
import com.cespenar.thechallenger.models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;

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
                        if (exception instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                                logIn(activity);
                            }
                        }
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

        if (!isAccessTokenValidForPublishing()) {
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

                if (activity instanceof CreateChallengeActivity) {
                    ChallengeService.getService().updateChallengeVideo(activity, challengeId, videoId);
                } else {
                    ChallengeService.getService().submitChallengeResponse(activity, challengeId, videoId);
                }
            }
        });
    }

    public void getVideo(final Activity activity, String videoId, final VideoView videoView) {

        if (videoId != null) {
            Bundle bundle = new Bundle();
            bundle.putString("fields", "source, picture");
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/" + videoId,
                    bundle,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {

                            try {

                                String videoSource = response.getJSONObject().getString("source");

                                if (videoSource == null) {
                                    return;
                                }

                                MediaController controller = new MediaController(activity);
                                videoView.setVideoPath(videoSource);
                                videoView.setMediaController(controller);
                                videoView.start();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).executeAsync();
        } else {
            activity.findViewById(R.id.challenge_details_video).setVisibility(View.GONE);
        }
    }

    public void loadVideoThumbnail(ImageView view, String url) {
        Ion.with(view)
                .placeholder(R.drawable.player)
                .error(R.drawable.player)
                .load(url);
    }

    public void loadProfilePicture(ImageView view, String url) {
        Ion.with(view)
                .placeholder(R.drawable.avatar_small)
                .error(R.drawable.avatar_small)
                .load(url);
    }

}
