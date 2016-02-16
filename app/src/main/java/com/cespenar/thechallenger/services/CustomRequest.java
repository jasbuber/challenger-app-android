package com.cespenar.thechallenger.services;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.cespenar.thechallenger.models.Challenge;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by Jasbuber on 15/08/2015.
 */
public class CustomRequest<T> extends Request<T> {

    private Response.Listener<T> listener;
    private Map<String, String> params;
    Class<T> responseClass;
    private static final int DEFAULT_MAX_RETRIES = 3;

    public CustomRequest(int method, String url, Map<String, String> params,
                         Response.Listener<T> responseListener, Response.ErrorListener errorListener, Class<T> responseClass) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.params = params;
        this.responseClass = responseClass;
        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            return Response.success(
                    new Gson().fromJson(jsonString, responseClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}
