package com.cespenar.thechallenger.services;

import com.android.volley.Request;

import junit.framework.TestCase;

/**
 * Created by Jasbuber on 22/08/2015.
 */
public class RouteTest extends TestCase {

    private static final String TEST_URL = "http://www.test.com";

    public void testCreateRoute(){
        Route route = new Route(Request.Method.POST, TEST_URL);
        assertEquals(route.getRequestType(), Request.Method.POST);
        assertTrue(route.getUrl().equals(TEST_URL));
    }
}
