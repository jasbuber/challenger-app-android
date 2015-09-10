package com.cespenar.thechallenger.services;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Jasbuber on 22/08/2015.
 */
public class RouterTest extends TestCase {

    private static final Router.ROUTE_NAME ROUTE = Router.ROUTE_NAME.CREATE_CHALLENGE;
    private static final String ROUTE_NAME = "CREATE_CHALLENGE";
    private static Router router;

    protected void setUp() {
        router = Router.getRouter();
    }

    public void testGetRouterNotNull() {
        assertNotNull(router);
    }

    public void testGetRouterSingleton() {
        Router router2 = Router.getRouter();
        assertSame(router, router2);
    }

    public void testGetRoute() {
        Route route = router.getRoute(ROUTE);
        assertNotNull(route);
    }

    public void testCreatePostRequestWithParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("param1", "param1Value");
        params.put("param2", "param2Value");

        CustomRequest request = router.createPostRequest(ROUTE, params, null, null, Object.class);

        try {
            assertEquals(params, request.getParams());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
    }

    public void testCreatePostRequestWithoutParams() {
        CustomRequest request = router.createPostRequest(ROUTE, null, null, null, Object.class);

        try {
            assertEquals(null, request.getParams());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
    }

    public void testCreatePostRequestValidUrl() {
        CustomRequest request = router.createPostRequest(ROUTE, null, null, null, Object.class);

        assertTrue(request.getUrl().endsWith(request.getUrl()));
        assertTrue(request.getUrl().matches("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"));
    }

    public void testCreatePostRequestValidMethod() {
        CustomRequest request = router.createPostRequest(ROUTE, null, null, null, Object.class);
        assertEquals(request.getMethod(), Request.Method.POST);
    }

    public void testCreateGetRequestWithParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("param1", "param1Value");
        params.put("param2", "param2Value");

        CustomRequest request = router.createGetRequest(ROUTE, params, null, null, Object.class);

        String url = request.getUrl();

        assertTrue(url.contains("param1=param1Value"));
        assertTrue(url.contains("param2=param2Value"));
    }

    public void testCreateGetRequestWithoutParams() {
        CustomRequest request = router.createGetRequest(ROUTE, null, null, null, Object.class);

        assertFalse(request.getUrl().contains("?"));
        try {
            assertEquals(null, request.getParams());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
    }

    public void testCreateGetRequestValidUrl() {
        CustomRequest request = router.createPostRequest(ROUTE, null, null, null, Object.class);

        assertTrue(request.getUrl().endsWith(request.getUrl()));
        assertTrue(request.getUrl().matches("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"));
    }

    public void testCreateGetRequestValidMethod() {
        CustomRequest request = router.createGetRequest(ROUTE, null, null, null, Object.class);
        assertEquals(request.getMethod(), Request.Method.GET);
    }

    public void testROUTE_NAMEValue(){
        assertEquals(Router.ROUTE_NAME.valueOf(ROUTE_NAME), ROUTE);
    }

    public void testROUTE_NAMEValues(){
        Arrays.asList(Router.ROUTE_NAME.values()).contains(ROUTE);
    }

}
