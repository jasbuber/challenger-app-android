package com.cespenar.thechallenger.services;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.cespenar.thechallenger.models.CustomResponse;
import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by jasbuber on 2016-01-12.
 */
public class CustomRequestTest {

    @Test
    public void testParseNetworkResponseForString(){

        String stringResponse = "testResponse";
        byte[] byteResponse = stringResponse.getBytes();

        CustomRequest<String> request = new CustomRequest<>(Request.Method.POST, "testUrl", null, null, null, String.class);

        NetworkResponse nResponse = new NetworkResponse(byteResponse);
        Response response = request.parseNetworkResponse(nResponse);

        assertEquals(stringResponse, response.result);
    }

    @Test
    public void testParseNetworkResponseForCustomResponse(){

        CustomResponse customResponse = new CustomResponse(50, "testMessage");
        customResponse.setStatus(CustomResponse.ResponseStatus.success);

        byte[] byteResponse = new Gson().toJson(customResponse).getBytes();

        CustomRequest<CustomResponse> request = new CustomRequest<>(Request.Method.POST, "testUrl", null, null, null, CustomResponse.class);

        NetworkResponse nResponse = new NetworkResponse(byteResponse);
        Response response = request.parseNetworkResponse(nResponse);

        assertEquals("Points not parsed properly", customResponse.getRewardedPoints(), ((CustomResponse) response.result).getRewardedPoints());
    }

    @Test
    public void testParseNetworkResponseFailsForNull(){

        CustomRequest<CustomResponse> request = new CustomRequest<>(Request.Method.POST, "testUrl", null, null, null, CustomResponse.class);

        NetworkResponse nResponse = new NetworkResponse(null);
        Response response = request.parseNetworkResponse(nResponse);

        assertFalse("Parsing should have failed for null", response.isSuccess());
    }
}
