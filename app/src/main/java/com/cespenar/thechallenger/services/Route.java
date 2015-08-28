package com.cespenar.thechallenger.services;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jasbuber on 14/08/2015.
 */
public class Route {

    private int requestType;
    private String url;

    public Route(int type, String url){
        this.requestType = type;
        this.url = url;
    }

    public int getRequestType() {
        return requestType;
    }

    public String getUrl() {
        return url;
    }
}

