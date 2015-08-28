package com.cespenar.thechallenger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.User;

import java.util.ArrayList;
import java.util.List;


public class BrowseChallengesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_challenges);

        populateChallengesList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse_challenges, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateChallengesList(){

        ListView challengesListView = (ListView) findViewById(R.id.browse_challenges_list);

        List<Challenge> challenges = new ArrayList<>();

        challenges.add(new Challenge(new User("username1"), "Challenge1", 3));
        challenges.add(new Challenge(new User("username2"), "Challengefdfdfd fddffd", 4));

        BrowseChallengesListAdapter adapter = new BrowseChallengesListAdapter(this, challenges);
        challengesListView.setAdapter(adapter);
    }
}
