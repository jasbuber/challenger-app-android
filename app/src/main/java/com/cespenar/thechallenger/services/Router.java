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

    public static enum ROUTE_NAME { CREATE_CHALLENGE, LATEST_CHALLENGES, FIND_CHALLENGES }

    private static final String server = "http://10.0.2.2:9000/";//"https://nameless-badlands-7043.herokuapp.com/";

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
    }

    public Route getRoute(ROUTE_NAME name){
        return routes.get(name);
    }

    public <T>CustomRequest createPostRequest(ROUTE_NAME name, final HashMap<String, String> params, Response.Listener listener,
                                     Response.ErrorListener errorListener, Class responseClass){

        String url = generateUrl(routes.get(name).getUrl(), null);

        CustomRequest request = new CustomRequest<T>( Request.Method.POST, url, params, listener, errorListener, responseClass);

        return request;
    }

    public CustomRequest createGetRequest(ROUTE_NAME name, final HashMap<String, String> params, Response.Listener listener,
                                           Response.ErrorListener errorListener, Class responseClass){

        String url = generateUrl(routes.get(name).getUrl(), params);

        CustomRequest request = new CustomRequest( Request.Method.GET, url, null, listener, errorListener, responseClass);

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
