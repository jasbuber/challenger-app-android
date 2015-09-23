package com.cespenar.thechallenger;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.ChallengeParticipation;
import com.cespenar.thechallenger.models.ChallengeResponse;
import com.cespenar.thechallenger.models.CustomResponse;
import com.cespenar.thechallenger.models.User;
import com.cespenar.thechallenger.services.ChallengeService;
import com.cespenar.thechallenger.services.UserService;

import java.util.ArrayList;
import java.util.List;


public class ChallengeActivity extends Activity {

    private List<ChallengeResponse> challengeResponses = new ArrayList<>();

    private static ListView challengeResponsesListView;

    private Challenge challenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        challenge = (Challenge) getIntent().getSerializableExtra("challenge");

        if (challenge == null) {
            ChallengeService.getService().getChallenge(this, getIntent().getLongExtra("challengeId", -1));
        } else if (challenge.getCreator().equals(UserService.getCurrentUser())) {
            populateChallenge(challenge, ChallengeParticipation.CREATOR_STATE);
        } else {
            ChallengeService.getService().getChallengeParticipationState(this, challenge);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_challenge, menu);
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

    private void fillChallengeDetails(Challenge challenge) {
        TextView challengeName = (TextView) findViewById(R.id.challenge_details_name);
        TextView challengeCategory = (TextView) findViewById(R.id.challenge_details_category);
        TextView challengeCreatedAt = (TextView) findViewById(R.id.challenge_details_created_at);
        TextView challengeDifficulty = (TextView) findViewById(R.id.challenge_details_difficulty);
        TextView challengeCreator = (TextView) findViewById(R.id.challenge_details_creator);
        RatingBar challengeRating = (RatingBar) findViewById(R.id.challenge_details_rating);
        challengeName.setText(challenge.getName());
        challengeCategory.setText(challenge.getCategory().toString());
        challengeCreatedAt.setText(challenge.getCreationDate());
        challengeCreator.setText(challenge.getCreator().getFormattedName());
        challengeDifficulty.setText(challenge.getFormattedDifficulty());
        challengeRating.setRating(challenge.getRating());
    }

    public void setChallengeResponses(List<ChallengeResponse> responses) {
        this.challengeResponses = responses;
    }

    public void populateChallenge(Challenge challenge, int participationState) {
        this.challenge = challenge;

        fillChallengeDetails(challenge);

        checkIfUserParticipatesInChallenge(participationState);

        ChallengeService.getService().getChallengeResponses(this, challenge);
    }

    public void populateChallengeResponses(List<ChallengeResponse> responses){
        challengeResponsesListView = (ListView) findViewById(R.id.challenge_details_responses);

        /*
        ChallengeResponse response = new ChallengeResponse(new ChallengeParticipation(new Challenge("ffddfdfdffd", "ddfsdsdsdsdds", "ALL", true, 3), new User("1384032931905091", "ble", "Janusz", "One")));
        ChallengeResponse response2 = new ChallengeResponse(new ChallengeParticipation(new Challenge("ffddfdfdffd", "ddfsdsdsdsdds", "ALL", true, 3), new User("1384032931905091", "ble", "Janusz", "Two")));
        ChallengeResponse response3 = new ChallengeResponse(new ChallengeParticipation(new Challenge("ffddfdfdffd", "ddfsdsdsdsdds", "ALL", true, 3), new User("1384032931905091", "ble", "Janusz", "Three")));
        responses.add(response);
        responses.add(response2);
        responses.add(response3);*/
        ChallengeResponsesListAdapter adapter = new ChallengeResponsesListAdapter(this, responses);
        challengeResponsesListView.setAdapter(adapter);

    }

    private void checkIfUserParticipatesInChallenge(int participationState) {
        if (participationState == ChallengeParticipation.CREATOR_STATE ||
                participationState == ChallengeParticipation.RESPONDED) {
            findViewById(R.id.challenge_details_join).setVisibility(View.INVISIBLE);
        } else if (participationState == ChallengeParticipation.NOT_RESPONDED_STATE) {
            findViewById(R.id.challenge_details_join).setVisibility(View.INVISIBLE);
            findViewById(R.id.challenge_details_show_respond).setVisibility(View.VISIBLE);
        }
    }

    public void onClickJoinChallenge(View view) {
        ChallengeService.getService().joinChallenge(this, this.challenge);
    }

    public void finalizeJoinChallenge(CustomResponse response) {
        findViewById(R.id.challenge_details_join).setVisibility(View.INVISIBLE);
        findViewById(R.id.challenge_details_show_respond).setVisibility(View.VISIBLE);
    }
}
