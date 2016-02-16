package com.cespenar.thechallenger.services;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Created by Jasbuber on 22/08/2015.
 */
public class RouterTest{

    private static final Router.ROUTE_NAME ROUTE = Router.ROUTE_NAME.CREATE_CHALLENGE;
    private static final String ROUTE_NAME = "CREATE_CHALLENGE";
    private static Router router;

    @Before
    public void setUp() {
        router = Router.getRouter();
    }

    @Test
    public void testGetRouterNotNull() {
        assertNotNull(router);
    }

    @Test
    public void testGetRouterSingleton() {
        Router router2 = Router.getRouter();
        assertSame(router, router2);
    }

    @Test
    public void testGetRoute() {
        Route route = router.getRoute(ROUTE);
        assertNotNull(route);
    }

    @Test
    public void testROUTE_NAMEValue(){
        assertEquals(Router.ROUTE_NAME.valueOf(ROUTE_NAME), ROUTE);
    }

    @Test
    public void testROUTE_NAMEValues(){
        Arrays.asList(Router.ROUTE_NAME.values()).contains(ROUTE);
    }

}
