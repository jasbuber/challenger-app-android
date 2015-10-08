package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.ChallengeWithParticipantsNr;
import com.cespenar.thechallenger.services.ChallengeService;
import com.cespenar.thechallenger.services.FacebookService;
import com.cespenar.thechallenger.services.UserService;

import java.util.ArrayList;
import java.util.List;


public class CreatedChallengesActivity extends Activity {

    private static ListView challengesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookService.getService().validateToken(this);

        setContentView(R.layout.activity_created_challenges);
        challengesListView = (ListView) findViewById(R.id.created_challenges_list);
        challengesListView.setOnItemClickListener(getOnItemClickListener(this));

        if (savedInstanceState != null) {
            populateChallengesList((List) savedInstanceState.getSerializable("challenges"));
            return;
        }

        ChallengeService.getService().getMyChallenges(this, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        ChallengesListAdapter adapter = (ChallengesListAdapter) challengesListView.getAdapter();
        ArrayList<ChallengeWithParticipantsNr> challenges = (ArrayList) adapter.challenges;

        savedInstanceState.putSerializable("challenges", challenges);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_created_challenges, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent;

        switch (id) {
            case R.id.action_profile:
                intent = new Intent(this, UserActivity.class);
                intent.putExtra("username", UserService.getCurrentUsername());
                startActivity(intent);
                break;
            case R.id.action_my_participations:
                intent = new Intent(this, ChallengeParticipationsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_create_challenge:
                intent = new Intent(this, CreateChallengeActivity.class);
                startActivity(intent);
                break;
            case R.id.action_search:
                intent = new Intent(this, BrowseChallengesActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateChallengesList(List<ChallengeWithParticipantsNr> challenges) {
        ChallengesListAdapter adapter = new ChallengesListAdapter(this, challenges, ChallengesListAdapter.TYPE.CHALLENGES_LIST);
        challengesListView.setAdapter(adapter);

        if (challenges.isEmpty()) {
            findViewById(R.id.create_first_challenge_action).setVisibility(View.VISIBLE);
        }
    }

    public AdapterView.OnItemClickListener getOnItemClickListener(final CreatedChallengesActivity activity) {

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ChallengesListAdapter.ViewHolder holder = (ChallengesListAdapter.ViewHolder) view.getTag();

                Intent intent = new Intent(activity, ChallengeActivity.class);
                intent.putExtra("challengeId", holder.id);

                startActivity(intent);
            }
        };
    }

    public void onClickCreateFirstChallenge(View view) {
        Intent intent = new Intent(this, CreateChallengeActivity.class);
        startActivity(intent);
    }
}
