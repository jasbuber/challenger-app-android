package com.cespenar.thechallenger.services;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jasbuber on 14/08/2015.
 */
public class Router {

    private static Router router = null;

    Map<ROUTE_NAME, Route> routes = new HashMap<>();

    public enum ROUTE_NAME { CREATE_CHALLENGE, LATEST_CHALLENGES, FIND_CHALLENGES, CHALLENGE_RESPONSES, JOIN_CHALLENGE, MY_CHALLENGES,
        GET_CHALLENGE, GET_PARTICIPATION_STATE, MY_PARTICIPATIONS, RANKINGS, CREATE_USER, SUBMIT_RESPONSE,
        RATE_RESPONSE, GET_PROFILE, GET_VIDEO, RATE_CHALLENGE, CREATE_COMMENT, GET_COMMENTS, COMPLETE_TUTORIAL }

    private static final String server = "https://nameless-badlands-7043.herokuapp.com/";//"http://192.168.100.189:9000/";// "http://10.0.2.2:9000/";//

    private Router(){
        initializeRoutes();
    }

    public static Router getRouter(){
        if(router == null){
            router = new Router();
        }

        return router;
    }

    private void initializeRoutes(){
        routes.put(ROUTE_NAME.CREATE_CHALLENGE, new Route(Request.Method.POST, "services/challenge/create"));
        routes.put(ROUTE_NAME.LATEST_CHALLENGES, new Route(Request.Method.GET, "services/challenge/latest"));
        routes.put(ROUTE_NAME.FIND_CHALLENGES, new Route(Request.Method.GET, "services/challenge/search"));
        routes.put(ROUTE_NAME.CHALLENGE_RESPONSES, new Route(Request.Method.GET, "services/challenge/responses"));
        routes.put(ROUTE_NAME.JOIN_CHALLENGE, new Route(Request.Method.POST, "services/challenge/join"));
        routes.put(ROUTE_NAME.MY_CHALLENGES, new Route(Request.Method.GET, "services/user/mychallenges"));
        routes.put(ROUTE_NAME.GET_CHALLENGE, new Route(Request.Method.GET, "services/challenge"));
        routes.put(ROUTE_NAME.GET_PARTICIPATION_STATE, new Route(Request.Method.GET, "services/participation/state"));
        routes.put(ROUTE_NAME.MY_PARTICIPATIONS, new Route(Request.Method.GET, "services/user/participations"));
        routes.put(ROUTE_NAME.RANKINGS, new Route(Request.Method.GET, "services/rankings"));
        routes.put(ROUTE_NAME.CREATE_USER, new Route(Request.Method.POST, "services/user/create"));
        routes.put(ROUTE_NAME.SUBMIT_RESPONSE, new Route(Request.Method.POST, "services/challenge/respond"));
        routes.put(ROUTE_NAME.RATE_RESPONSE, new Route(Request.Method.POST, "services/response/decide"));
        routes.put(ROUTE_NAME.GET_PROFILE, new Route(Request.Method.GET, "services/user/profile"));
        routes.put(ROUTE_NAME.GET_VIDEO, new Route(Request.Method.GET, "services/challenge/video"));
        routes.put(ROUTE_NAME.RATE_CHALLENGE, new Route(Request.Method.POST, "services/challenge/rate"));
        routes.put(ROUTE_NAME.CREATE_COMMENT, new Route(Request.Method.POST, "services/comment/create"));
        routes.put(ROUTE_NAME.GET_COMMENTS, new Route(Request.Method.GET, "services/comments/get"));
        routes.put(ROUTE_NAME.COMPLETE_TUTORIAL, new Route(Request.Method.POST, "services/user/completetutorial"));
    }

    public Route getRoute(ROUTE_NAME name){
        return routes.get(name);
    }

    public CustomRequest createRequest(ROUTE_NAME name, final HashMap<String, String> params, Response.Listener listener,
                                           Response.ErrorListener errorListener, Class responseClass){

        Route route = routes.get(name);
        CustomRequest request;
        String url;

        if(route.getRequestType() == Request.Method.GET){
            url = generateUrl(routes.get(name).getUrl(), params);
            request = new CustomRequest( Request.Method.GET, url, null, listener, errorListener, responseClass);
        }else{
            url = generateUrl(routes.get(name).getUrl(), null);
            request = new CustomRequest( Request.Method.POST, url, params, listener, errorListener, responseClass);
        }

        return request;
    }

    private String generateUrl(String url, HashMap<String, String> params){

        Uri.Builder builder = Uri.parse(server + url).buildUpon();

        if(params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {

                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        return builder.build().toString();
    }
}
