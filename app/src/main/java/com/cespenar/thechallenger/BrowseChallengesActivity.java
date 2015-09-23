package com.cespenar.thechallenger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.User;
import com.cespenar.thechallenger.services.ChallengeService;

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
        setContentView(R.layout.activity_browse_challenges);

        challengesListView = (ListView) findViewById(R.id.browse_challenges_list);

        ChallengeService.getService().getLatestChallenges(this);
        challengesListView.setOnScrollListener(getOnScrollListener(this));

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

    public void populateChallengesList(List<Challenge> challenges){
        BrowseChallengesListAdapter adapter = new BrowseChallengesListAdapter(this, challenges);
        challengesListView.setAdapter(adapter);
    }

    public void appendChallengesList(List<Challenge> challenges){
        BrowseChallengesListAdapter adapter = (BrowseChallengesListAdapter) challengesListView.getAdapter();
        adapter.challenges.addAll(challenges);
        adapter.notifyDataSetChanged();
    }

    public void onClickGetChallengesByPhrase(View view){
        EditText nameInput = (EditText) findViewById(R.id.browse_challenges_search_input);
        String phrase = nameInput.getText().toString();
        resetPage();
        ChallengeService.getService().getChallengesByCriteria(this, phrase, PAGE);
        lastSearchedName = phrase;
    }

    private void resetPage(){
        PAGE = 0;
        preLast = 0;
    }

    private AbsListView.OnScrollListener getOnScrollListener(final BrowseChallengesActivity activity){
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount && hasMore) {
                    if(preLast!=lastItem){
                        preLast = lastItem;
                        PAGE++;
                        ChallengeService.getService().getChallengesByCriteria(activity, lastSearchedName, PAGE);
                    }
                }
            }
        };
    }

    public void setHasMore(boolean hasMore){
        this.hasMore = hasMore;
    }
}