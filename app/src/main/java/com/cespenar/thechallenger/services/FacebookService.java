package com.cespenar.thechallenger.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.cespenar.thechallenger.MainActivity;
import com.cespenar.thechallenger.NoConnectionActivity;
import com.cespenar.thechallenger.R;
import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

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

        if(!isNetworkAvailable(activity)) {
            Log.e("no_connection", "no_connection");
            activity.startActivity(new Intent(activity, NoConnectionActivity.class));
            return;
        }
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    ProfileTracker mProfileTracker;

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        if(Profile.getCurrentProfile() == null) {
                            mProfileTracker = new ProfileTracker() {
                                @Override
                                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                                    setCurrentUser(profile2);
                                    UserService.getService().createUser(activity, UserService.getCurrentUser());
                                    mProfileTracker.stopTracking();
                                }
                            };
                            mProfileTracker.startTracking();
                        }else{
                            setCurrentUser(Profile.getCurrentProfile());
                            UserService.getService().createUser(activity, UserService.getCurrentUser());
                        }
                    }

                    @Override
                    public void onCancel() {
                        Log.e("cancel", "cancel");
                        logIn(activity);
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

    public boolean isAccessTokenValid() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        return (token != null && !token.isExpired() && token.getPermissions().contains("user_videos"));
    }

    public boolean isAccessTokenValidForPublishing() {
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
        String profilePicUrl = profile.getProfilePictureUri(150, 150).toString();

        User user = new User(username, profilePicUrl, firstName, lastName);
        UserService.setCurrentUser(user);
    }

    public void acquirePublishPermission(final Activity activity, final String path, final String description, final ProgressBar progressBar, final Challenge challenge) {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        setCurrentUser(Profile.getCurrentProfile());
                        publishVideo(activity, path, description, progressBar, challenge);
                    }

                    @Override
                    public void onCancel() {
                        if (activity instanceof CreateChallengeActivity) {
                            activity.findViewById(R.id.create_challenge_submit).setEnabled(true);
                        }else{
                            activity.findViewById(R.id.challenge_details_submit_response).setEnabled(true);
                        }
                        Log.e("cancel", "cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("error", exception.toString());
                    }
                });

        LoginManager.getInstance().setDefaultAudience(DefaultAudience.EVERYONE);
        LoginManager.getInstance().logInWithPublishPermissions(activity, Arrays.asList("publish_actions"));
    }

    public void publishVideo(final Activity activity, String path, String description, ProgressBar progressBar, final Challenge challenge) {

        if (!isAccessTokenValidForPublishing()) {
            acquirePublishPermission(activity, path, description, progressBar, challenge);
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
                challenge.setVideoPath(videoId);

                if (activity instanceof CreateChallengeActivity) {
                    ChallengeService.getService().createChallenge(activity, challenge);
                } else {
                    ChallengeService.getService().submitChallengeResponse(activity, challenge.getId(), videoId);
                }
            }
        });
    }

    public void loadVideoThumbnail(Activity context, ImageView view, String url) {
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.player)
                .error(R.drawable.player)
                .into(view);
    }

    public void loadProfilePicture(Activity context, ImageView view, String username) {
        Picasso.with(context)
                .load(prepareProfilePictureUrl(username))
                .placeholder(R.drawable.avatar_small)
                .error(R.drawable.avatar_small)
                .into(view);

    }

    public void validateToken(Activity activity) {

        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(activity.getApplicationContext());
        }
        if (!isAccessTokenValid()) {
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
        }
    }

    private String prepareProfilePictureUrl(String username){
        return "https://graph.facebook.com/" + username + "/picture?width=150&height=150";
    }

    private boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
