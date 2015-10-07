package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.User;
import com.cespenar.thechallenger.services.ChallengeService;
import com.cespenar.thechallenger.services.FacebookService;
import com.cespenar.thechallenger.services.UserService;

import java.util.ArrayList;
import java.util.List;


public class BrowseChallengesActivity extends Activity {

    private int preLast = 0;
    private boolean hasMore = true;
    private static int PAGE = 0;
    private static String lastSearchedName = "";

    private static ListView challengesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookService.getService().validateToken(this);

        setContentView(R.layout.activity_browse_challenges);

        challengesListView = (ListView) findViewById(R.id.browse_challenges_list);
        challengesListView.setOnScrollListener(getOnScrollListener(this));
        challengesListView.setOnItemClickListener(getOnItemClickListener(this));

        if (savedInstanceState != null) {
            List<Challenge> challenges = (List<Challenge>) savedInstanceState.getSerializable("challenges");
            populateChallengesList(challenges);

            preLast = savedInstanceState.getInt("preLast");
            hasMore = savedInstanceState.getBoolean("hasMore");
            PAGE = savedInstanceState.getInt("page");
            lastSearchedName = savedInstanceState.getString("lastSearchedName");
        } else {
            ChallengeService.getService().getLatestChallenges(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        BrowseChallengesListAdapter adapter = (BrowseChallengesListAdapter) challengesListView.getAdapter();
        ArrayList<Challenge> challenges = (ArrayList) adapter.getChallenges();

        savedInstanceState.putSerializable("challenges", challenges);
        savedInstanceState.putInt("preLast", preLast);
        savedInstanceState.putBoolean("hasMore", hasMore);
        savedInstanceState.putInt("page", PAGE);
        savedInstanceState.putString("lastSearchedName", lastSearchedName);
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
            case R.id.action_my_participations :
                intent = new Intent(this, ChallengeParticipationsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_create_challenge:
                intent = new Intent(this, CreateChallengeActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateChallengesList(List<Challenge> challenges) {
        BrowseChallengesListAdapter adapter = new BrowseChallengesListAdapter(this, challenges);
        challengesListView.setAdapter(adapter);
    }

    public void appendChallengesList(List<Challenge> challenges) {
        BrowseChallengesListAdapter adapter = (BrowseChallengesListAdapter) challengesListView.getAdapter();
        adapter.challenges.addAll(challenges);
        adapter.notifyDataSetChanged();
    }

    public void onClickGetChallengesByPhrase(View view) {
        EditText nameInput = (EditText) findViewById(R.id.browse_challenges_search_input);
        String phrase = nameInput.getText().toString();
        resetPage();
        ChallengeService.getService().getChallengesByCriteria(this, phrase, PAGE);
        lastSearchedName = phrase;
    }

    private void resetPage() {
        PAGE = 0;
        preLast = 0;
    }

    private AbsListView.OnScrollListener getOnScrollListener(final BrowseChallengesActivity activity) {
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
                        PAGE++;
                        ChallengeService.getService().getChallengesByCriteria(activity, lastSearchedName, PAGE);
                    }
                }
            }
        };
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public AdapterView.OnItemClickListener getOnItemClickListener(final BrowseChallengesActivity activity) {

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BrowseChallengesListAdapter.ViewHolder holder = (BrowseChallengesListAdapter.ViewHolder) view.getTag();

                Intent intent = new Intent(activity, ChallengeActivity.class);
                intent.putExtra("challenge", holder.challenge);

                startActivity(intent);
            }
        };
    }
}
