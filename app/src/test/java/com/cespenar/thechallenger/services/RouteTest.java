package com.cespenar.thechallenger.services;

import com.android.volley.Request;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Jasbuber on 22/08/2015.
 */
public class RouteTest{

    private static final String TEST_URL = "http://www.test.com";

    @Test
    public void testCreateRoute(){
        Route route = new Route(Request.Method.POST, TEST_URL);
        assertEquals(route.getRequestType(), Request.Method.POST);
        assertTrue(route.getUrl().equals(TEST_URL));
    }
}
