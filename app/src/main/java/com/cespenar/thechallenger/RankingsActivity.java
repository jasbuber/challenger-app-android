package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.ChallengeWithParticipantsNr;
import com.cespenar.thechallenger.models.User;
import com.cespenar.thechallenger.services.ChallengeService;
import com.cespenar.thechallenger.services.FacebookService;
import com.cespenar.thechallenger.services.UserService;

import java.util.ArrayList;
import java.util.List;


public class RankingsActivity extends Activity {

    private static ListView bestUsersListView;
    private static ListView bestChallengesListView;
    private static ListView trendingChallengesListView;
    private static ListView popularChallengesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookService.getService().validateToken(this);
        setContentView(R.layout.activity_rankings);

        bestUsersListView = (ListView) findViewById(R.id.rankings_best_users_list);
        bestChallengesListView = (ListView) findViewById(R.id.best_challenges_list);
        trendingChallengesListView = (ListView) findViewById(R.id.rankings_trending_challenges_list);
        popularChallengesListView = (ListView) findViewById(R.id.rankings_popular_challenges_list);

        if(savedInstanceState != null){
            ArrayList<User> bestUsers = (ArrayList) savedInstanceState.getSerializable("bestUsers");
            ArrayList<Challenge> bestChallenges = (ArrayList) savedInstanceState.getSerializable("bestChallenges");
            ArrayList<Challenge> trendingChallenges = (ArrayList) savedInstanceState.getSerializable("trendingChallenges");
            ArrayList<ChallengeWithParticipantsNr> popularChallenges = (ArrayList) savedInstanceState.getSerializable("popularChallenges");

            populateRankings(bestChallenges, bestUsers, trendingChallenges, popularChallenges);
        }else {
            ChallengeService.getService().getRankings(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        UserListAdapter usersAdapter = (UserListAdapter) bestUsersListView.getAdapter();
        RankingsChallengeListAdapter bestChallengesAdapter = (RankingsChallengeListAdapter) bestChallengesListView.getAdapter();
        RankingsChallengeListAdapter trendingAdapter = (RankingsChallengeListAdapter) trendingChallengesListView.getAdapter();
        ChallengesListAdapter popularAdapter = (ChallengesListAdapter) popularChallengesListView.getAdapter();
        ArrayList<User> bestUsers = (ArrayList) usersAdapter.getUsers();
        ArrayList<Challenge> bestChallenges = (ArrayList) bestChallengesAdapter.challenges;
        ArrayList<Challenge> trendingChallenges = (ArrayList) trendingAdapter.challenges;
        ArrayList<ChallengeWithParticipantsNr> popularChallenges = (ArrayList) popularAdapter.challenges;

        savedInstanceState.putSerializable("bestUsers", bestUsers);
        savedInstanceState.putSerializable("bestChallenges", bestChallenges);
        savedInstanceState.putSerializable("trendingChallenges", trendingChallenges);
        savedInstanceState.putSerializable("popularChallenges", popularChallenges);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rankings, menu);
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
            case R.id.action_search :
                intent = new Intent(this, BrowseChallengesActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateRankings(List<Challenge> topRatedChallenges, List<User> topRatedUsers, List<Challenge> trendingChallenges,
                                 List<ChallengeWithParticipantsNr> mostPopularChallenges){

        UserListAdapter userAdapter = new UserListAdapter(this, topRatedUsers);
        RankingsChallengeListAdapter bestChallengesAdapter = new RankingsChallengeListAdapter(this, topRatedChallenges);
        RankingsChallengeListAdapter trendingChallengesAdapter = new RankingsChallengeListAdapter(this, trendingChallenges);
        ChallengesListAdapter popularChallengesAdapter = new ChallengesListAdapter(this, mostPopularChallenges, ChallengesListAdapter.TYPE.MOST_POPULAR_LIST);

        bestUsersListView.setAdapter(userAdapter);
        bestUsersListView.setOnItemClickListener(getOnItemClickBestUsersListener(this));

        bestChallengesListView.setAdapter(bestChallengesAdapter);
        bestChallengesListView.setOnItemClickListener(getOnItemClickBestChallengesListener(this));

        trendingChallengesListView.setAdapter(trendingChallengesAdapter);
        trendingChallengesListView.setOnItemClickListener(getOnItemClickBestChallengesListener(this));
        popularChallengesListView.setAdapter(popularChallengesAdapter);
        popularChallengesListView.setOnItemClickListener(getOnItemClickPopularChallengesListener(this));

    }

    public AdapterView.OnItemClickListener getOnItemClickBestChallengesListener(final RankingsActivity activity){

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RankingsChallengeListAdapter.ViewHolder holder = (RankingsChallengeListAdapter.ViewHolder) view.getTag();

                Intent intent = new Intent(activity, ChallengeActivity.class);
                intent.putExtra("challenge", holder.challenge);

                startActivity(intent);
            }
        };
    }

    public AdapterView.OnItemClickListener getOnItemClickPopularChallengesListener(final RankingsActivity activity){

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

    public AdapterView.OnItemClickListener getOnItemClickBestUsersListener(final RankingsActivity activity){

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UserListAdapter.ViewHolder holder = (UserListAdapter.ViewHolder) view.getTag();

                Intent intent = new Intent(activity, UserActivity.class);
                intent.putExtra("username", holder.username);

                startActivity(intent);
            }
        };
    }
}
