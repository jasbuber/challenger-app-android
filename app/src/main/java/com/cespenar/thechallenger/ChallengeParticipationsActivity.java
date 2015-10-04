package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cespenar.thechallenger.models.ChallengeWithParticipantsNr;
import com.cespenar.thechallenger.services.ChallengeService;
import com.cespenar.thechallenger.services.FacebookService;

import java.util.List;


public class ChallengeParticipationsActivity extends Activity {

    private static ListView challengesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookService.getService().validateToken(this);

        setContentView(R.layout.activity_challenge_participations);
        challengesListView = (ListView) findViewById(R.id.challenge_participations_list);
        challengesListView.setOnItemClickListener(getOnItemClickListener(this));
        ChallengeService.getService().getMyParticipations(this, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_challenge_participations, menu);
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

    public void populateChallengesList(List<ChallengeWithParticipantsNr> challenges){
        ChallengesListAdapter adapter = new ChallengesListAdapter(this, challenges, ChallengesListAdapter.TYPE.CHALLENGES_LIST);
        challengesListView.setAdapter(adapter);
    }

    public AdapterView.OnItemClickListener getOnItemClickListener(final ChallengeParticipationsActivity activity){

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
}
