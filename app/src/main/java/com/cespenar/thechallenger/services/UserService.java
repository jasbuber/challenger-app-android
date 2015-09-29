package com.cespenar.thechallenger.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cespenar.thechallenger.CreateChallengeActivity;
import com.cespenar.thechallenger.MainActivity;
import com.cespenar.thechallenger.R;
import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.CustomResponse;
import com.cespenar.thechallenger.models.User;

import java.util.HashMap;

/**
 * Created by Jasbuber on 17/08/2015.
 */
public class UserService {

    private static User currentUser;

    private static UserService service;

    public static String getCurrentUsername(){
        return currentUser.getUsername();
    }

    public static User getCurrentUser(){
        return currentUser;
    }

    private static FacebookService fbService;

    private UserService() {
        fbService = FacebookService.getService();
    }

    public static UserService getService() {
        if (service == null) {
            service = new UserService();
        }

        return service;
    }

    public static void setCurrentUser(User user){
        currentUser = user;
    }

    public void createUser(final Context context, User user) {

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
                if(response.equals("success")){
                    ((MainActivity) context).setContentView(R.layout.activity_main);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        };

        HashMap<String, String> params = new HashMap<>();

        params.put("username", user.getUsername());
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("profilePictureUrl", user.getProfilePictureUrl());

        CustomRequest request = Router.getRouter()
                .createRequest(Router.ROUTE_NAME.CREATE_USER, params, listener, errorListener, String.class);

        queue.add(request);
    }


}
