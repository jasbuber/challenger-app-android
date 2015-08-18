package com.cespenar.thechallenger.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.CustomResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Jasbuber on 14/08/2015.
 */
public class ChallengeService {

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

    public String createChallenge(Context context, Challenge challenge) {

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                CustomResponse customResponse = new Gson().fromJson(response.toString(), CustomResponse.class);
                Log.e("success", String.valueOf(customResponse.getChallengeId()));
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            };
        };

        HashMap<String, String> params = challenge.getPropertyHashmap();
        params.put("username", UserService.getCurrentUsername());
        params.put("token", UserService.getCurrentToken());

        CustomRequest request = Router.getRouter()
                .createPostRequest(Router.ROUTE_NAME.CREATE_CHALLENGE, params, listener, errorListener);

        // Add the request to the RequestQueue.
        queue.add(request);

        return "success";
    }

    public String updateChallenge(Challenge challenge) {
        return "not implemented method";
    }
}
