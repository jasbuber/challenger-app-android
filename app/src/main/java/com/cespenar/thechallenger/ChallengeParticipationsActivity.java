package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.ChallengeWithParticipantsNr;
import com.cespenar.thechallenger.services.ChallengeService;
import com.cespenar.thechallenger.services.FacebookService;
import com.cespenar.thechallenger.services.UserService;

import java.util.ArrayList;
import java.util.List;


public class ChallengeParticipationsActivity extends Activity {

    private static ListView challengesListView;

    private int page = 0;
    private int preLast = 0;
    private boolean hasMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookService.getService().validateToken(this);

        setContentView(R.layout.activity_challenge_participations);
        challengesListView = (ListView) findViewById(R.id.challenge_participations_list);
        challengesListView.setOnItemClickListener(getOnItemClickListener(this));
        challengesListView.setOnScrollListener(getOnScrollListener(this));

        if(savedInstanceState != null){
            preLast = savedInstanceState.getInt("preLast");
            hasMore = savedInstanceState.getBoolean("hasMore");
            page = savedInstanceState.getInt("page");
            populateChallengesList((List) savedInstanceState.getSerializable("challenges"));
            return;
        }

        ChallengeService.getService().getMyParticipations(this, 0);

        if(getActionBar() != null){
            getActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        ChallengesListAdapter adapter = (ChallengesListAdapter) challengesListView.getAdapter();
        ArrayList<ChallengeWithParticipantsNr> challenges = (ArrayList) adapter.challenges;

        savedInstanceState.putInt("preLast", preLast);
        savedInstanceState.putBoolean("hasMore", hasMore);
        savedInstanceState.putInt("page", page);
        savedInstanceState.putSerializable("challenges", challenges);
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

        Intent intent;

        switch(id){
            case R.id.action_profile :
                intent = new Intent(this, UserActivity.class);
                intent.putExtra("username", UserService.getCurrentUsername());
                startActivity(intent);
                break;
            case R.id.action_my_challenges :
                intent = new Intent(this, CreatedChallengesActivity.class);
                startActivity(intent);
                break;
            case R.id.action_create_challenge:
                intent = new Intent(this, CreateChallengeActivity.class);
                startActivity(intent);
                break;
            case R.id.action_search :
                intent = new Intent(this, BrowseChallengesActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateChallengesList(List<ChallengeWithParticipantsNr> challenges){
            ChallengesListAdapter adapter = new ChallengesListAdapter(this, challenges, ChallengesListAdapter.TYPE.CHALLENGES_LIST);
            challengesListView.setAdapter(adapter);

        if(challenges.isEmpty()) {
            findViewById(R.id.join_first_challenge_action).setVisibility(View.VISIBLE);
        }

    }

    public void appendChallengesList(List<ChallengeWithParticipantsNr> challenges) {
        hideLoader();
        ChallengesListAdapter adapter = (ChallengesListAdapter) challengesListView.getAdapter();
        adapter.challenges.addAll(challenges);
        adapter.notifyDataSetChanged();
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

    public void onClickJoinFirstChallenge(View view){
        Intent intent = new Intent(this, BrowseChallengesActivity.class);
        startActivity(intent);
    }

    private AbsListView.OnScrollListener getOnScrollListener(final ChallengeParticipationsActivity activity) {
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount && hasMore) {
                    if (preLast != lastItem) {
                        preLast = lastItem;
                        page++;
                        showLoader();
                        ChallengeService.getService().getMyParticipations(activity, page);
                    }
                }
            }
        };
    }

    public void showLoader(){
        findViewById(R.id.my_participations_loader).setVisibility(View.VISIBLE);
    }

    public void hideLoader(){
        findViewById(R.id.my_participations_loader).setVisibility(View.GONE);
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
