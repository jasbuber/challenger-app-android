package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
    private int PAGE = 0;
    private static String lastSearchedName = "";

    private static ListView challengesListView;
    private Button selectedToolbarButton;

    private Challenge.CHALLENGE_CATEGORY currentCategory = Challenge.CHALLENGE_CATEGORY.ALL;
    private ChallengeService.SORTING_ORDER currentOrder = ChallengeService.SORTING_ORDER.RECENT;

    private boolean categoriesListVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookService.getService().validateToken(this);

        setContentView(R.layout.activity_browse_challenges);

        showLoader();

        challengesListView = (ListView) findViewById(R.id.browse_challenges_list);
        challengesListView.setOnScrollListener(getOnScrollListener(this));
        challengesListView.setOnItemClickListener(getOnItemClickListener(this));
        addOnTouchListeners();
        addSubmitActionListener();

        ((ListView) findViewById(R.id.browse_challenges_categories_list)).setAdapter(new CategoriesListAdapter(this));

        ((ListView) findViewById(R.id.browse_challenges_categories_list)).setOnItemClickListener(getOnCategoryClickListener());

        selectedToolbarButton = (Button) findViewById(R.id.browse_challenges_toolbar_recent);

        if (savedInstanceState != null) {
            preLast = savedInstanceState.getInt("preLast");
            hasMore = savedInstanceState.getBoolean("hasMore");
            PAGE = savedInstanceState.getInt("page");
            lastSearchedName = savedInstanceState.getString("lastSearchedName");
            ((EditText)findViewById(R.id.browse_challenges_search_input)).setText(lastSearchedName);
            currentOrder = ChallengeService.SORTING_ORDER.valueOf(savedInstanceState.getString("currentOrder"));
            currentCategory = Challenge.CHALLENGE_CATEGORY.valueOf(savedInstanceState.getString("currentCategory"));
            Button currentToolbarButton = (Button) findViewById(savedInstanceState.getInt("selectedToolbarButtonId"));

            List<Challenge> challenges = (List<Challenge>) savedInstanceState.getSerializable("challenges");

            if(challenges != null) {
                populateChallengesList(challenges);
            }else{
                hideLoader();
                if(selectedToolbarButton.getId() != R.id.browse_challenges_toolbar_categories ) {
                    findViewById(R.id.browse_challenges_no_challenges).setVisibility(View.VISIBLE);
                }
            }

            switch (currentToolbarButton.getId()){
                case R.id.browse_challenges_toolbar_categories:
                    categoriesListVisible = savedInstanceState.getBoolean("categoriesListVisible");
                    if(categoriesListVisible) {
                        findViewById(R.id.browse_challenges_list).setVisibility(View.GONE);
                        findViewById(R.id.browse_challenges_categories_list).setVisibility(View.VISIBLE);
                        findViewById(R.id.browse_challenges_no_challenges).setVisibility(View.GONE);
                        switchToolbarButton(currentToolbarButton);
                        categoriesListVisible = true;
                    }else{
                        switchToolbarButton(currentToolbarButton);
                    }
                    break;
                case R.id.browse_challenges_toolbar_top:
                    switchToolbarButton(currentToolbarButton);
                    break;
                case R.id.browse_challenges_toolbar_recent:
                    switchToolbarButton(currentToolbarButton);
                    break;
                case R.id.browse_challenges_toolbar_popular:
                    switchToolbarButton(currentToolbarButton);
                    break;
            }
        } else {
            ChallengeService.getService().getLatestChallenges(this);
            selectedToolbarButton = (Button) findViewById(R.id.browse_challenges_toolbar_recent);
        }

        if(getActionBar() != null){
            getActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        if(challengesListView.getAdapter() != null) {
            BrowseChallengesListAdapter adapter = (BrowseChallengesListAdapter) challengesListView.getAdapter();
            ArrayList<Challenge> challenges = (ArrayList) adapter.getChallenges();
            savedInstanceState.putSerializable("challenges", challenges);
        }
        savedInstanceState.putInt("preLast", preLast);
        savedInstanceState.putBoolean("hasMore", hasMore);
        savedInstanceState.putInt("page", PAGE);
        savedInstanceState.putString("lastSearchedName", lastSearchedName);
        savedInstanceState.putString("currentOrder", currentOrder.toString());
        savedInstanceState.putString("currentCategory", currentCategory.toString());
        savedInstanceState.putInt("selectedToolbarButtonId", selectedToolbarButton.getId());
        savedInstanceState.putBoolean("categoriesListVisible", categoriesListVisible);
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
            case android.R.id.home:
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateChallengesList(List<Challenge> challenges) {
        hideLoader();

        BrowseChallengesListAdapter adapter = new BrowseChallengesListAdapter(this, challenges);
        challengesListView.setAdapter(adapter);

        if(challenges.isEmpty()){
            findViewById(R.id.browse_challenges_no_challenges).setVisibility(View.VISIBLE);
        }
    }

    public void appendChallengesList(List<Challenge> challenges) {
        hideLoader();
        BrowseChallengesListAdapter adapter = (BrowseChallengesListAdapter) challengesListView.getAdapter();
        adapter.challenges.addAll(challenges);
        adapter.notifyDataSetChanged();
    }

    public void getChallengesByPhrase(Challenge.CHALLENGE_CATEGORY category, ChallengeService.SORTING_ORDER order) {
        EditText nameInput = (EditText) findViewById(R.id.browse_challenges_search_input);
        String phrase = nameInput.getText().toString();
        resetPage();
        findViewById(R.id.browse_challenges_no_challenges).setVisibility(View.GONE);
        showLoader();
        findViewById(R.id.browse_challenges_list).setVisibility(View.VISIBLE);
        findViewById(R.id.browse_challenges_categories_list).setVisibility(View.GONE);
        ChallengeService.getService().getChallengesByCriteria(this, phrase, category, PAGE, order);
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
                        showLoader();
                        ChallengeService.getService().getChallengesByCriteria(activity,
                                lastSearchedName, Challenge.CHALLENGE_CATEGORY.ALL, PAGE, currentOrder);
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
                intent.putExtra("challengeId", holder.id);

                startActivity(intent);
            }
        };
    }

    public void showLoader(){
        findViewById(R.id.browse_challenges_loader).setVisibility(View.VISIBLE);
    }

    public void hideLoader(){
        findViewById(R.id.browse_challenges_loader).setVisibility(View.GONE);
    }

    public View.OnTouchListener getOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundResource(R.drawable.toolbar_clicked_background);
                        break;
                    case MotionEvent.ACTION_UP:
                        view.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        };
    }

    private void addOnTouchListeners(){
        findViewById(R.id.browse_challenges_toolbar_categories).setOnTouchListener(getOnTouchListener());
        findViewById(R.id.browse_challenges_toolbar_top).setOnTouchListener(getOnTouchListener());
        findViewById(R.id.browse_challenges_toolbar_popular).setOnTouchListener(getOnTouchListener());
    }

    private void addSubmitActionListener() {
        final EditText challengeNameInput = (EditText) findViewById(R.id.browse_challenges_search_input);

        challengeNameInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    categoriesListVisible = false;
                    getChallengesByPhrase(currentCategory, currentOrder);
                    return true;
                }
                return false;
            }
        });

        challengeNameInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                challengeNameInput.setCursorVisible(true);
                challengeNameInput.requestFocus();

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (challengeNameInput.getRight() -
                            challengeNameInput.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        categoriesListVisible = false;
                        getChallengesByPhrase(currentCategory, currentOrder);
                        challengeNameInput.clearFocus();
                        challengeNameInput.setCursorVisible(false);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void onClickToolbarButton(View view){

        switchToolbarButton(view);

        switch(view.getId()){
            case R.id.browse_challenges_toolbar_categories:
                currentOrder = ChallengeService.SORTING_ORDER.CATEGORIES;
                findViewById(R.id.browse_challenges_list).setVisibility(View.GONE);
                findViewById(R.id.browse_challenges_categories_list).setVisibility(View.VISIBLE);
                findViewById(R.id.browse_challenges_no_challenges).setVisibility(View.GONE);
                categoriesListVisible = true;
                break;
            case R.id.browse_challenges_toolbar_top:
                handleCommonToolbarClick(ChallengeService.SORTING_ORDER.TOP);
                break;
            case R.id.browse_challenges_toolbar_recent:
                handleCommonToolbarClick(ChallengeService.SORTING_ORDER.RECENT);
                break;
            case R.id.browse_challenges_toolbar_popular:
                handleCommonToolbarClick(ChallengeService.SORTING_ORDER.POPULAR);
                break;
        }
    }

    private void switchToolbarButton(View view){

        if(view.equals(selectedToolbarButton)){
            return;
        }

        view.setBackgroundResource(R.drawable.toolbar_selected_background);
        ((Button)view).setTextColor(getResources().getColor(R.color.white));
        view.setOnTouchListener(null);
        selectedToolbarButton.setBackgroundColor(Color.TRANSPARENT);
        selectedToolbarButton.setTextColor(getResources().getColor(R.color.font_dark));
        selectedToolbarButton.setOnTouchListener(getOnTouchListener());
        selectedToolbarButton = (Button) view;
        categoriesListVisible = false;
    }

    private void handleCommonToolbarClick(ChallengeService.SORTING_ORDER order){
        currentOrder = order;
        getChallengesByPhrase(Challenge.CHALLENGE_CATEGORY.ALL, currentOrder);
    }

    private AdapterView.OnItemClickListener getOnCategoryClickListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CategoriesListAdapter.ViewHolder holder = (CategoriesListAdapter.ViewHolder) view.getTag();
                currentCategory = holder.category;
                categoriesListVisible = false;
                getChallengesByPhrase(holder.category, currentOrder);
            }
        };
    }
}
