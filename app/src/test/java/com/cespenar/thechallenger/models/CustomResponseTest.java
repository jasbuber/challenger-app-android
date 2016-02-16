package com.cespenar.thechallenger.models;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Jasbuber on 22/08/2015.
 */
public class CustomResponseTest{

    private static final String TEST_MESSAGE = "Test message";
    private static final String TEST_MESSAGE_2 = "Test message_2";
    private static final String TEST_MESSAGE_3 = "Test message_3";
    private static final int TEST_POINTS = 15;
    private static final int TEST_POINTS_2 = 5;
    private static final int TEST_POINTS_3 = 10;

    @Test
    public void testCustomResponseConstructor_1() {
        CustomResponse response = new CustomResponse();
        assertEquals(response.getRewardedPoints(), 0);
        assertEquals(response.getStatus(), CustomResponse.ResponseStatus.success);
        assertTrue(response.getPoints().size() == 0);
    }

    @Test
    public void testCustomResponseConstructor_2() {
        CustomResponse response = new CustomResponse(TEST_POINTS, TEST_MESSAGE);
        assertEquals(response.getRewardedPoints(), TEST_POINTS);
        assertTrue(response.getMessages().get(0).equals(TEST_MESSAGE));
        assertEquals(response.getStatus(), CustomResponse.ResponseStatus.success);
        assertTrue(response.getPoints().size() == 0);
    }

    @Test
    public void testCustomResponseConstructor_3() {
        List<String> messages = new ArrayList<>();
        messages.add(TEST_MESSAGE);

        CustomResponse response = new CustomResponse(TEST_POINTS, messages);
        assertEquals(response.getRewardedPoints(), TEST_POINTS);
        assertTrue(response.getMessages().get(0).equals(TEST_MESSAGE));
        assertEquals(response.getStatus(), CustomResponse.ResponseStatus.success);
        assertTrue(response.getPoints().size() == 0);
    }

    @Test
    public void testPoints() {
        CustomResponse response = new CustomResponse();

        List<Integer> points = new ArrayList<>();
        points.add(TEST_POINTS);
        points.add(TEST_POINTS_2);
        response.setPoints(points);
        response.addPoints(TEST_POINTS_3);

        assertEquals((int) response.getPoints().get(0), TEST_POINTS);
        assertEquals((int) response.getPoints().get(1), TEST_POINTS_2);
        assertEquals((int) response.getPoints().get(2), TEST_POINTS_3);
        assertEquals(response.getRewardedPoints(), TEST_POINTS_3);
    }

    @Test
    public void testChallengeId() {
        int challengeId = 555;
        CustomResponse response = new CustomResponse();
        response.setChallengeId(challengeId);
        assertEquals(response.getChallengeId(), challengeId);
    }

    @Test
    public void testMessages() {
        CustomResponse response = new CustomResponse();

        List<String> messages = new ArrayList<>();
        messages.add(TEST_MESSAGE);
        messages.add(TEST_MESSAGE_2);
        response.setMessages(messages);
        response.addMessage(TEST_MESSAGE_3);

        assertTrue(TEST_MESSAGE.equals(response.getMessages().get(0)));
        assertTrue(TEST_MESSAGE_2.equals(response.getMessages().get(1)));
        assertTrue(TEST_MESSAGE_3.equals(response.getMessages().get(2)));
    }

    @Test
    public void testRESPONSE_STATUSValues() {
        CustomResponse.ResponseStatus[] statuses = CustomResponse.ResponseStatus.values();
        assertEquals(statuses[0], CustomResponse.ResponseStatus.failure);
        assertEquals(statuses[1], CustomResponse.ResponseStatus.success);
    }

    @Test
    public void testRESPONSE_STATUSValue() {
        assertEquals(CustomResponse.ResponseStatus.valueOf("success")
                , CustomResponse.ResponseStatus.success);
    }

    @Test
    public void testRESPONSE_STATUS() {
        CustomResponse response = new CustomResponse();
        response.setStatus(CustomResponse.ResponseStatus.failure);
        assertEquals(CustomResponse.ResponseStatus.failure, response.getStatus());
    }

}
