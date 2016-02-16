package com.cespenar.thechallenger;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.User;
import com.cespenar.thechallenger.services.ChallengeService;
import com.cespenar.thechallenger.services.FacebookService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowListView;
import org.robolectric.shadows.ShadowTextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by jasbuber on 2016-01-22.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class BrowseChallengesActivityTest {

    public FacebookService fbService;

    public ChallengeService chService;

    private BrowseChallengesActivity spyActivity;

    List<Challenge> challenges;

    ArgumentCaptor<BrowseChallengesActivity> activity;
    ArgumentCaptor<String> phrase;
    ArgumentCaptor<Challenge.CHALLENGE_CATEGORY> category;
    ArgumentCaptor<Integer> page;
    ArgumentCaptor<ChallengeService.SORTING_ORDER> order;

    @Before
    public void setUp() {

        activity = ArgumentCaptor.forClass(BrowseChallengesActivity.class);
        phrase = ArgumentCaptor.forClass(String.class);
        category = ArgumentCaptor.forClass(Challenge.CHALLENGE_CATEGORY.class);
        page = ArgumentCaptor.forClass(Integer.class);
        order = ArgumentCaptor.forClass(ChallengeService.SORTING_ORDER.class);

        populateChallengeListWithData();
        fbService = mock(FacebookService.class);
        chService = mock(ChallengeService.class);

        BrowseChallengesActivity activity =
                Robolectric.buildActivity(BrowseChallengesActivity.class).attach().get();
        spyActivity = spy(activity);

        doReturn(true).when(fbService).isAccessTokenValid();
        doReturn(fbService).when(spyActivity).getFacebookService();
        doReturn(chService).when(spyActivity).getChallengeService();

        spyActivity.onCreate(null);
    }

    public void populateChallengeListWithData() {

        challenges = new ArrayList<>();

        challenges.add(new Challenge(1L, new User("testUser1", "firstName1", "lastname1", "profilePic1"),
                "testChallenge1", Challenge.CHALLENGE_CATEGORY.AQUA_SPHERE, 3.5f, new Date(), 3, ""));
        challenges.add(new Challenge(143L, new User("testUser2", "firstName2", "lastname2", "profilePic2"),
                "testChallenge2", Challenge.CHALLENGE_CATEGORY.EARGASMIC, 1.5f, new Date(), 3, ""));
        challenges.add(new Challenge(21L, new User("testUser3", "firstName3", "lastname3", "profilePic3"),
                "testChallenge3", Challenge.CHALLENGE_CATEGORY.ALL, 4.5f, new Date(), 3, ""));
        challenges.add(new Challenge(431L, new User("testUser4", "firstName4", "lastname4", "profilePic4"),
                "testChallenge1", Challenge.CHALLENGE_CATEGORY.AQUA_SPHERE, 3.5f, new Date(), 1, ""));
        challenges.add(new Challenge(14300L, new User("testUser5", "firstName5", "lastname5", "profilePic5"),
                "testChallenge2", Challenge.CHALLENGE_CATEGORY.OTHER, 2.5f, new Date(), 2, ""));
        challenges.add(new Challenge(123L, new User("testUser6", "firstName6", "lastname6", "profilePic6"),
                "testChallenge3", Challenge.CHALLENGE_CATEGORY.ALL, 5.0f, new Date(), 2, ""));
    }

    @Test
    public void testPopulateChallenges() {

        spyActivity.populateChallengesList(challenges);

        ListView challengeList = (ListView) spyActivity.findViewById(R.id.browse_challenges_list);

        assertEquals("Adapter error", challengeList.getAdapter().getCount(), challenges.size());
    }

    @Test
    public void testPopulateEmptyChallengesList() {

        spyActivity.populateChallengesList(new ArrayList<Challenge>());

        View noChallengesView = spyActivity.findViewById(R.id.browse_challenges_no_challenges);
        ListView challengeList = (ListView) spyActivity.findViewById(R.id.browse_challenges_list);

        assertTrue("No challenges view error", noChallengesView.getVisibility() == View.VISIBLE);
        assertEquals("Adapter error", challengeList.getAdapter().getCount(), 0);
    }

    @Test
    public void testPopulateChallengesTwice() {

        spyActivity.populateChallengesList(challenges.subList(0, 1));

        spyActivity.populateChallengesList(challenges.subList(1, 4));

        ListView challengeList = (ListView) spyActivity.findViewById(R.id.browse_challenges_list);
        BrowseChallengesListAdapter adapter = (BrowseChallengesListAdapter) challengeList.getAdapter();

        assertEquals("Adapter error", adapter.getCount(), 3);
        assertThat(adapter.getChallenges(), not(hasItem(challenges.get(0))));
    }

    @Test
    public void testAppendChallenges() {

        spyActivity.populateChallengesList(challenges.subList(0, 2));

        spyActivity.appendChallengesList(challenges.subList(3, 5));

        ListView challengeList = (ListView) spyActivity.findViewById(R.id.browse_challenges_list);
        BrowseChallengesListAdapter adapter = (BrowseChallengesListAdapter) challengeList.getAdapter();

        assertEquals("Adapter error", 4, adapter.getCount());
        assertThat(adapter.getChallenges(), hasItem(challenges.get(0)));
    }

    @Test
    public void testAppendChallengesTwice() {

        spyActivity.populateChallengesList(challenges.subList(0, 1));

        spyActivity.appendChallengesList(challenges.subList(1, 3));

        spyActivity.appendChallengesList(challenges.subList(5, 6));

        ListView challengeList = (ListView) spyActivity.findViewById(R.id.browse_challenges_list);
        BrowseChallengesListAdapter adapter = (BrowseChallengesListAdapter) challengeList.getAdapter();

        assertEquals("Adapter error", 4, adapter.getCount());
        assertThat(adapter.getChallenges(), hasItem(challenges.get(0)));
    }

    @Test
    public void testGetChallengesByPhrase() {

        ((EditText) spyActivity.findViewById(R.id.browse_challenges_search_input)).setText("testPhrase");

        spyActivity.getChallengesByPhrase(Challenge.CHALLENGE_CATEGORY.DRINKING_ZONE, ChallengeService.SORTING_ORDER.POPULAR);

        verify(spyActivity.getChallengeService()).getChallengesByCriteria(
                activity.capture(), phrase.capture(), category.capture(), page.capture(), order.capture());

        assertThat("Incorrect phrase", phrase.getValue(), equalTo("testPhrase"));
        assertThat("Incorrect category", category.getValue(), equalTo(Challenge.CHALLENGE_CATEGORY.DRINKING_ZONE));
        assertEquals("Incorrect page", 0, page.getValue().intValue());
        assertThat("Incorrect order", order.getValue(), equalTo(ChallengeService.SORTING_ORDER.POPULAR));
    }

    @Test
    public void testClickChallenge() {

        spyActivity.populateChallengesList(challenges);

        ListView listView = (ListView) spyActivity.findViewById(R.id.browse_challenges_list);
        ShadowListView shadowListView = shadowOf(listView);
        shadowListView.populateItems();

        shadowListView.performItemClick(4);

        Intent started = shadowOf(spyActivity).getNextStartedActivity();
        Intent expected = new Intent(spyActivity, ChallengeActivity.class);
        expected.putExtra("challengeId", challenges.get(4).getId());

        assertThat(started, equalTo(expected));
    }

    @Test
    public void testLoadChallengesOnScroll() {

        ListView listView = (ListView) spyActivity.findViewById(R.id.browse_challenges_list);
        ShadowListView shadowListView = shadowOf(listView);

        shadowListView.getOnScrollListener().onScroll(listView, 2, 2, 4);

        verify(spyActivity.getChallengeService()).getChallengesByCriteria(
                activity.capture(), phrase.capture(), category.capture(), page.capture(), order.capture());

        assertThat("Incorrect category", category.getValue(), equalTo(Challenge.CHALLENGE_CATEGORY.ALL));
        assertEquals("Incorrect page", 1, page.getValue().intValue());
        assertThat("Incorrect order", order.getValue(), equalTo(ChallengeService.SORTING_ORDER.RECENT));
    }

    @Test
    public void testSubmitWithKeyboard() {
        EditText phraseView = (EditText) spyActivity.findViewById(R.id.browse_challenges_search_input);

        phraseView.setText("testPhrase");

        phraseView.onEditorAction(EditorInfo.IME_ACTION_DONE);

        verify(spyActivity.getChallengeService()).getChallengesByCriteria(
                activity.capture(), phrase.capture(), category.capture(), page.capture(), order.capture());

        assertThat("Incorrect phrase", phrase.getValue(), equalTo("testPhrase"));
    }

    @Test
    public void testNoSubmitWithRegularClickKeyboard() {
        EditText phraseView = (EditText) spyActivity.findViewById(R.id.browse_challenges_search_input);

        phraseView.setText("testPhrase");

        phraseView.onEditorAction(EditorInfo.IME_ACTION_UNSPECIFIED);

        verify(spyActivity.getChallengeService(), never()).getChallengesByCriteria(
                activity.capture(), phrase.capture(), category.capture(), page.capture(), order.capture());
    }

    @Test
    public void testSubmitWithImage() {
        EditText phraseView = (EditText) spyActivity.findViewById(R.id.browse_challenges_search_input);

        phraseView.setText("testPhrase");
        phraseView.dispatchTouchEvent(MotionEvent.obtain(999, 999, MotionEvent.ACTION_UP, 100, 100, 1));

        verify(spyActivity.getChallengeService()).getChallengesByCriteria(
                activity.capture(), phrase.capture(), category.capture(), page.capture(), order.capture());

        assertThat("Incorrect phrase", phrase.getValue(), equalTo("testPhrase"));
    }

    @Test
    public void testNoSubmitAfterRegularClick() {
        EditText phraseView = (EditText) spyActivity.findViewById(R.id.browse_challenges_search_input);

        phraseView.setText("testPhrase");
        phraseView.dispatchTouchEvent(MotionEvent.obtain(1, 1, MotionEvent.ACTION_DOWN, 100, 100, 1));

        verify(spyActivity.getChallengeService(), never()).getChallengesByCriteria(
                activity.capture(), phrase.capture(), category.capture(), page.capture(), order.capture());
    }

    @Test
    public void testClickTopToolbarButton() {

        EditText phraseView = (EditText) spyActivity.findViewById(R.id.browse_challenges_search_input);
        phraseView.setText("testPhrase");

        View topButton = spyActivity.findViewById(R.id.browse_challenges_toolbar_top);

        spyActivity.onClickToolbarButton(topButton);

        verify(spyActivity.getChallengeService()).getChallengesByCriteria(
                activity.capture(), phrase.capture(), category.capture(), page.capture(), order.capture());

        assertThat("Incorrect phrase attribute", phrase.getValue(), equalTo("testPhrase"));
        assertThat("Incorrect order attribute", order.getValue(), equalTo(ChallengeService.SORTING_ORDER.TOP));
        assertThat("Incorrect category attribute", category.getValue(), equalTo(Challenge.CHALLENGE_CATEGORY.ALL));
    }

    @Test
    public void testClickPopularToolbarButton() {

        EditText phraseView = (EditText) spyActivity.findViewById(R.id.browse_challenges_search_input);
        phraseView.setText("testPhrase");

        View popularButton = spyActivity.findViewById(R.id.browse_challenges_toolbar_popular);

        spyActivity.onClickToolbarButton(popularButton);

        verify(spyActivity.getChallengeService()).getChallengesByCriteria(
                activity.capture(), phrase.capture(), category.capture(), page.capture(), order.capture());

        assertThat("Incorrect phrase attribute", phrase.getValue(), equalTo("testPhrase"));
        assertThat("Incorrect order attribute", order.getValue(), equalTo(ChallengeService.SORTING_ORDER.POPULAR));
        assertThat("Incorrect category attribute", category.getValue(), equalTo(Challenge.CHALLENGE_CATEGORY.ALL));
    }

    @Test
    public void testClickCategoriesToolbarButton() {

        EditText phraseView = (EditText) spyActivity.findViewById(R.id.browse_challenges_search_input);
        phraseView.setText("testPhrase");

        View categoriesButton = spyActivity.findViewById(R.id.browse_challenges_toolbar_categories);

        spyActivity.onClickToolbarButton(categoriesButton);

        ListView categoriesList = (ListView) spyActivity.findViewById(R.id.browse_challenges_categories_list);
        assertEquals("Categories list not visible", View.VISIBLE, categoriesList.getVisibility());
        assertEquals("Challenge list visible", View.GONE,
                spyActivity.findViewById(R.id.browse_challenges_list).getVisibility());

        shadowOf(categoriesList).populateItems();
        shadowOf(categoriesList).performItemClick(4);

        verify(spyActivity.getChallengeService()).getChallengesByCriteria(
                activity.capture(), phrase.capture(), category.capture(), page.capture(), order.capture());

        assertThat("Incorrect phrase attribute", phrase.getValue(), equalTo("testPhrase"));
        assertThat("Incorrect order attribute", order.getValue(), equalTo(ChallengeService.SORTING_ORDER.CATEGORIES));
        assertThat("Incorrect category attribute", category.getValue(),
                equalTo(Challenge.CHALLENGE_CATEGORY.FITNESS_AVENUE));
    }

    @Test
    public void testClickRecentToolbarButton() {

        EditText phraseView = (EditText) spyActivity.findViewById(R.id.browse_challenges_search_input);
        phraseView.setText("testPhrase");

        View recentButton = spyActivity.findViewById(R.id.browse_challenges_toolbar_recent);

        spyActivity.onClickToolbarButton(recentButton);

        verify(spyActivity.getChallengeService(), times(1)).getChallengesByCriteria(
                activity.capture(), phrase.capture(), category.capture(), page.capture(), order.capture());

        assertThat("Incorrect phrase attribute", phrase.getValue(), equalTo("testPhrase"));
        assertThat("Incorrect order attribute", order.getValue(), equalTo(ChallengeService.SORTING_ORDER.RECENT));
        assertThat("Incorrect category attribute", category.getValue(), equalTo(Challenge.CHALLENGE_CATEGORY.ALL));
    }

}
