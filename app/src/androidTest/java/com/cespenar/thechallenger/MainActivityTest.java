package com.cespenar.thechallenger;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.test.MoreAsserts;
import android.view.View;
import android.widget.Button;

/**
 * Created by Jasbuber on 24/08/2015.
 */
public class MainActivityTest extends ActivityUnitTestCase<MainActivity>{

    private static Activity activity;

    public MainActivityTest(){
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception{
        super.setUp();
        startActivity(new Intent(Intent.ACTION_MAIN), null, null);
        activity = getActivity();
    }

    public void testIfCreateBlockClickable(){
        Button createBlock = (Button)getActivity().findViewById(R.id.create_challenge_block);
        assertTrue(createBlock.isClickable());
    }

    public void testIfBrowseBlockClickable(){
        Button browseBlock = (Button)getActivity().findViewById(R.id.browse_challenges_block);
        assertTrue(browseBlock.isClickable());
    }

    public void testIfRankingsBlockClickable(){
        Button rankingsBlock = (Button)getActivity().findViewById(R.id.rankings_block);
        assertTrue(rankingsBlock.isClickable());
    }

    public void testIfMyChallengesBlockClickable(){
        Button myChallengesBlock = (Button)getActivity().findViewById(R.id.my_challenges_block);
        assertTrue(myChallengesBlock.isClickable());
    }

    public void testIfCreateBlockStartsActivity(){
        final Button createBlock = (Button)getActivity().findViewById(R.id.create_challenge_block);
        createBlock.performClick();
        assertNotNull(getStartedActivityIntent());
    }

}
