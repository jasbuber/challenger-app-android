package com.cespenar.thechallenger.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cespenar.thechallenger.BrowseChallengesActivity;
import com.cespenar.thechallenger.CreateChallengeActivity;
import com.cespenar.thechallenger.CreateChallengeFinalizeActivity;
import com.cespenar.thechallenger.R;
import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.CustomResponse;
import com.cespenar.thechallenger.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jasbuber on 14/08/2015.
 */
public class ChallengeService {

    private static final int PAGING_ROW_NUMBER = 11;

    private static ChallengeService service;

    private static FacebookService fbService;

    private ChallengeService() {
        fbService = FacebookService.getService();
    }

    public static ChallengeService getService() {
        if (service == null) {
            service = new ChallengeService();
        }

        return service;
    }

    public void createChallenge(final Context context, Challenge challenge) {

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<CustomResponse> listener = new Response.Listener<CustomResponse>() {
            @Override
            public void onResponse(CustomResponse response) {
                ((CreateChallengeActivity) context).finalizeCreateChallenge(response);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        };

        HashMap<String, String> params = challenge.getPropertyHashmap();
        params.put("username", UserService.getCurrentUsername());
        params.put("token", UserService.getCurrentToken());

        CustomRequest request = Router.getRouter()
                .createPostRequest(Router.ROUTE_NAME.CREATE_CHALLENGE, params, listener, errorListener, CustomResponse.class);

        queue.add(request);
    }

    public String updateChallenge(Challenge challenge) {
        return "not implemented method";
    }

    public void getLatestChallenges(final BrowseChallengesActivity context) {
        prepareChallengesByCriteria(context, null, Router.ROUTE_NAME.LATEST_CHALLENGES, false);
    }

    public void getChallengesByCriteria(final BrowseChallengesActivity context, String phrase, int page) {
        HashMap<String, String> params = new HashMap<>();
        params.put("phrase", phrase);
        params.put("category", Challenge.CHALLENGE_CATEGORY.ALL.toString());
        params.put("page", String.valueOf(page));
        params.put("scope", "1");

        //params.put("token", UserService.getCurrentToken());
        prepareChallengesByCriteria(context, params, Router.ROUTE_NAME.FIND_CHALLENGES, page > 0);
    }

    private void prepareChallengesByCriteria(final BrowseChallengesActivity context, HashMap<String, String> params, Router.ROUTE_NAME route_name, final boolean isNextPage){
        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<List<LinkedTreeMap<String, Object>>> listener = new Response.Listener<List<LinkedTreeMap<String, Object>>>() {
            @Override
            public void onResponse(List<LinkedTreeMap<String, Object>> response) {

                List<Challenge> challenges = Challenge.castLinkedTreeMapToChallengeList(response);

                if(challenges.size() < PAGING_ROW_NUMBER){
                    context.setHasMore(false);
                }else{
                    context.setHasMore(true);
                    challenges.remove(challenges.size()-1);
                }

                if(isNextPage){
                    context.appendChallengesList(challenges);
                }else {
                    context.populateChallengesList(challenges);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        };

        if(params == null){
            params = new HashMap<>();
        }
        params.put("username", UserService.getCurrentUsername());

        CustomRequest request = Router.getRouter().createGetRequest(route_name, params, listener, errorListener, List.class);

        // Add the request to the RequestQueue.
        queue.add(request);
    }
}
