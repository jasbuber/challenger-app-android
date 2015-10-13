package com.cespenar.thechallenger.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cespenar.thechallenger.BrowseChallengesActivity;
import com.cespenar.thechallenger.ChallengeActivity;
import com.cespenar.thechallenger.ChallengeParticipationsActivity;
import com.cespenar.thechallenger.CreateChallengeActivity;
import com.cespenar.thechallenger.CreateChallengeFinalizeActivity;
import com.cespenar.thechallenger.CreatedChallengesActivity;
import com.cespenar.thechallenger.RankingsActivity;
import com.cespenar.thechallenger.UserActivity;
import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.ChallengeResponse;
import com.cespenar.thechallenger.models.ChallengeWithParticipantsNr;
import com.cespenar.thechallenger.models.CustomResponse;
import com.cespenar.thechallenger.models.User;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
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
        params.put("token", FacebookService.getService().getAccessToken().getToken());

        CustomRequest request = Router.getRouter()
                .createRequest(Router.ROUTE_NAME.CREATE_CHALLENGE, params, listener, errorListener, CustomResponse.class);

        queue.add(request);
    }

    public void updateChallengeVideo(final Context context, final long challengeId, String videoId) {

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {

                    Intent intent = new Intent(context, CreateChallengeFinalizeActivity.class);
                    intent.putExtra("challengeId", challengeId);

                    context.startActivity(intent);
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
        params.put("username", UserService.getCurrentUser().getUsername());
        params.put("videoId", videoId);
        params.put("challengeId", String.valueOf(challengeId));


        CustomRequest request = Router.getRouter()
                .createRequest(Router.ROUTE_NAME.UPDATE_CHALLENGE_VIDEO, params, listener, errorListener, String.class);

        queue.add(request);
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

    private void prepareChallengesByCriteria(final BrowseChallengesActivity context, HashMap<String, String> params, Router.ROUTE_NAME route_name, final boolean isNextPage) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<List<LinkedTreeMap<String, Object>>> listener = new Response.Listener<List<LinkedTreeMap<String, Object>>>() {
            @Override
            public void onResponse(List<LinkedTreeMap<String, Object>> response) {

                List<Challenge> challenges = Challenge.castLinkedTreeMapToChallengeList(response);

                if (challenges.size() < PAGING_ROW_NUMBER) {
                    context.setHasMore(false);
                } else {
                    context.setHasMore(true);
                    challenges.remove(challenges.size() - 1);
                }

                if (isNextPage) {
                    context.appendChallengesList(challenges);
                } else {
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

        if (params == null) {
            params = new HashMap<>();
        }
        params.put("username", UserService.getCurrentUsername());

        CustomRequest request = Router.getRouter().createRequest(route_name, params, listener, errorListener, List.class);

        // Add the request to the RequestQueue.
        queue.add(request);
    }

    public void getChallengeResponses(final ChallengeActivity context, Challenge challenge) {

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<List<LinkedTreeMap<String, Object>>> listener = new Response.Listener<List<LinkedTreeMap<String, Object>>>() {
            @Override
            public void onResponse(List<LinkedTreeMap<String, Object>> response) {
                List<ChallengeResponse> responses = ChallengeResponse.castLinkedTreeMapToChallengeResponseList(response);
                context.populateChallengeResponses(responses);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        };

        User currentUser = UserService.getCurrentUser();

        HashMap<String, String> params = challenge.getPropertyHashmap();
        params.put("challengeId", String.valueOf(challenge.getId()));
        params.put("username", currentUser.getUsername());
        params.put("token", FacebookService.getService().getAccessToken().getToken());

        CustomRequest request = Router.getRouter()
                .createRequest(Router.ROUTE_NAME.CHALLENGE_RESPONSES, params, listener, errorListener, List.class);

        queue.add(request);
    }

    public void joinChallenge(final ChallengeActivity context, Challenge challenge) {

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<CustomResponse> listener = new Response.Listener<CustomResponse>() {
            @Override
            public void onResponse(CustomResponse response) {
                context.finalizeJoinChallenge(response);

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        };

        User currentUser = UserService.getCurrentUser();

        HashMap<String, String> params = challenge.getPropertyHashmap();
        params.put("challengeId", String.valueOf(challenge.getId()));
        params.put("username", currentUser.getUsername());
        params.put("fullName", currentUser.getFormattedName());
        params.put("token", FacebookService.getService().getAccessToken().getToken());

        CustomRequest request = Router.getRouter()
                .createRequest(Router.ROUTE_NAME.JOIN_CHALLENGE, params, listener, errorListener, CustomResponse.class);

        queue.add(request);
    }

    public void getMyChallenges(final CreatedChallengesActivity context, int page) {

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<List<LinkedTreeMap<String, Object>>> listener = new Response.Listener<List<LinkedTreeMap<String, Object>>>() {
            @Override
            public void onResponse(List<LinkedTreeMap<String, Object>> response) {
                context.populateChallengesList(ChallengeWithParticipantsNr.castLinkedTreeMapToChallengeList(response));
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        };

        User currentUser = UserService.getCurrentUser();

        HashMap<String, String> params = new HashMap<>();
        params.put("username", currentUser.getUsername());
        params.put("page", String.valueOf(page));

        //params.put("token", UserService.getCurrentToken());

        CustomRequest request = Router.getRouter()
                .createRequest(Router.ROUTE_NAME.MY_CHALLENGES, params, listener, errorListener, List.class);

        queue.add(request);
    }

    public void getChallenge(final ChallengeActivity context, final long challengeId, final VideoView videoView) {

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<HashMap> listener = new Response.Listener<HashMap>() {
            @Override
            public void onResponse(HashMap response) {
                LinkedTreeMap<String, Object> challengeMap = (LinkedTreeMap<String, Object>) response.get("challenge");
                int participationState = (int) ((double) response.get("participationState"));

                Challenge challenge = Challenge.castLinkedTreeMapToChallenge(challengeMap);
                context.populateChallenge(challenge, participationState);
                getVideo(context, challenge.getVideoPath(), videoView);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        };

        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(challengeId));
        params.put("username", UserService.getCurrentUser().getUsername());

        CustomRequest request = Router.getRouter()
                .createRequest(Router.ROUTE_NAME.GET_CHALLENGE, params, listener, errorListener, HashMap.class);

        queue.add(request);
    }

    public void getChallengeParticipationState(final ChallengeActivity context, final Challenge challenge) {

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<Integer> listener = new Response.Listener<Integer>() {
            @Override
            public void onResponse(Integer participationState) {
                context.populateChallenge(challenge, participationState);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        };

        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(challenge.getId()));
        params.put("username", UserService.getCurrentUser().getUsername());

        CustomRequest request = Router.getRouter()
                .createRequest(Router.ROUTE_NAME.GET_PARTICIPATION_STATE, params, listener, errorListener, Integer.class);

        queue.add(request);
    }

    public void getMyParticipations(final ChallengeParticipationsActivity context, int page) {

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<List<ArrayList>> listener = new Response.Listener<List<ArrayList>>() {
            @Override
            public void onResponse(List<ArrayList> response) {
                context.populateChallengesList(ChallengeWithParticipantsNr.castToChallengeList(response));
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        };

        User currentUser = UserService.getCurrentUser();

        HashMap<String, String> params = new HashMap<>();
        params.put("username", currentUser.getUsername());
        params.put("page", String.valueOf(page));

        //params.put("token", UserService.getCurrentToken());

        CustomRequest request = Router.getRouter()
                .createRequest(Router.ROUTE_NAME.MY_PARTICIPATIONS, params, listener, errorListener, List.class);

        queue.add(request);
    }

    public void getRankings(final RankingsActivity context) {

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<HashMap> listener = new Response.Listener<HashMap>() {
            @Override
            public void onResponse(HashMap response) {

                List<Challenge> topRatedChallenges = Challenge.castLinkedTreeMapToChallengeList((List<LinkedTreeMap<String, Object>>) response.get("topRatedChallenges"));
                List<Challenge> trendingChallenges = Challenge.castLinkedTreeMapToChallengeList((List<LinkedTreeMap<String, Object>>) response.get("trendingChallenges"));
                List<ChallengeWithParticipantsNr> mostPopularChallenges =
                        ChallengeWithParticipantsNr.castLinkedTreeMapToChallengeList((List<LinkedTreeMap<String, Object>>) response.get("mostPopularChallenges"));
                List<User> topRatedUsers = User.castLinkedTreeMapToUserList((List<LinkedTreeMap<String, Object>>) response.get("topRatedUsers"));


                context.populateRankings(topRatedChallenges, topRatedUsers, trendingChallenges, mostPopularChallenges);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        };

        HashMap<String, String> params = new HashMap<>();
        //params.put("username", UserService.getCurrentUser().getUsername());

        CustomRequest request = Router.getRouter()
                .createRequest(Router.ROUTE_NAME.RANKINGS, params, listener, errorListener, HashMap.class);

        queue.add(request);
    }

    public void submitChallengeResponse(final Context context, final long challengeId, String videoId) {

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<CustomResponse> listener = new Response.Listener<CustomResponse>() {
            @Override
            public void onResponse(CustomResponse response) {
                if (response.getStatus() == CustomResponse.ResponseStatus.success) {
                    ((ChallengeActivity) context).finalizeSubmitResponse();
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
        params.put("username", UserService.getCurrentUser().getUsername());
        params.put("videoId", videoId);
        params.put("challengeId", String.valueOf(challengeId));
        params.put("token", FacebookService.getService().getAccessToken().getToken());

        CustomRequest request = Router.getRouter()
                .createRequest(Router.ROUTE_NAME.SUBMIT_RESPONSE, params, listener, errorListener, CustomResponse.class);

        queue.add(request);
    }

    public void rateChallengeResponse(final Context context, final long responseId, Character isAccepted) {

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<CustomResponse> listener = new Response.Listener<CustomResponse>() {
            @Override
            public void onResponse(CustomResponse response) {
                if (response.getStatus() == CustomResponse.ResponseStatus.success) {
                    ((ChallengeActivity) context).finalizeRateResponse();
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
        params.put("username", UserService.getCurrentUser().getUsername());
        params.put("responseId", String.valueOf(responseId));
        params.put("isAccepted", String.valueOf(isAccepted));
        params.put("token", FacebookService.getService().getAccessToken().getToken());

        CustomRequest request = Router.getRouter()
                .createRequest(Router.ROUTE_NAME.RATE_RESPONSE, params, listener, errorListener, CustomResponse.class);

        queue.add(request);
    }

    public void getVideo(final Activity activity, String videoId, final VideoView videoView) {

        RequestQueue queue = Volley.newRequestQueue(activity);

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response == null || response.isEmpty()) {
                    videoView.setVisibility(View.GONE);
                    return;
                }

                MediaController controller = new MediaController(activity);
                videoView.setVideoPath(response);
                videoView.setMediaController(controller);
                videoView.setVisibility(View.VISIBLE);
                videoView.start();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        };

        User currentUser = UserService.getCurrentUser();

        HashMap<String, String> params = new HashMap<>();
        params.put("token", FacebookService.getService().getAccessToken().getToken());
        params.put("videoId", videoId);

        CustomRequest request = Router.getRouter()
                .createRequest(Router.ROUTE_NAME.GET_VIDEO, params, listener, errorListener, String.class);

        queue.add(request);
    }

    public void rateChallenge(final Context context, long challengeId, final int rating) {

        RequestQueue queue = Volley.newRequestQueue(context);

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {}
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        };

        HashMap<String, String> params = new HashMap<>();
        params.put("username", UserService.getCurrentUser().getUsername());
        params.put("challengeId", String.valueOf(challengeId));
        params.put("rating", String.valueOf(rating));
        params.put("token", FacebookService.getService().getAccessToken().getToken());

        CustomRequest request = Router.getRouter()
                .createRequest(Router.ROUTE_NAME.RATE_CHALLENGE, params, listener, errorListener, String.class);

        queue.add(request);
    }

}
