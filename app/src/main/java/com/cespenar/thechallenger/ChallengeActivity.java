package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.ChallengeParticipation;
import com.cespenar.thechallenger.models.ChallengeResponse;
import com.cespenar.thechallenger.models.CustomResponse;
import com.cespenar.thechallenger.models.User;
import com.cespenar.thechallenger.services.ChallengeService;
import com.cespenar.thechallenger.services.FacebookService;
import com.cespenar.thechallenger.services.UserService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ChallengeActivity extends Activity {

    private static final int SELECT_VIDEO = 1;

    private List<ChallengeResponse> challengeResponses = new ArrayList<>();

    private static LinearLayout challengeResponsesList;

    private Challenge challenge;

    private static String responseVideo;

    private static VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        videoView = (VideoView) findViewById(R.id.challenge_details_video);

        challenge = (Challenge) getIntent().getSerializableExtra("challenge");

        if (challenge == null) {
            ChallengeService.getService().getChallenge(this, getIntent().getLongExtra("challengeId", -1), videoView);
        } else if (challenge.getCreator().equals(UserService.getCurrentUser())) {
            populateChallenge(challenge, ChallengeParticipation.CREATOR_STATE);
            FacebookService.getService().getVideo(this, challenge.getVideoPath(), videoView);
        } else {
            ChallengeService.getService().getChallengeParticipationState(this, challenge);
            FacebookService.getService().getVideo(this, challenge.getVideoPath(), videoView);
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
        ImageView creatorPicture = (ImageView) findViewById(R.id.challenge_details_picture);
        challengeName.setText(challenge.getName());
        challengeCategory.setText(challenge.getCategory().toString());
        challengeCreatedAt.setText(challenge.getCreationDate());
        challengeCreator.setText(challenge.getCreator().getFormattedName());
        challengeDifficulty.setText(challenge.getFormattedDifficulty());
        challengeRating.setRating(challenge.getRating());

        FacebookService.getService().loadProfilePicture(creatorPicture, challenge.getCreator().getProfilePictureUrl());
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
        challengeResponsesList = (LinearLayout) findViewById(R.id.challenge_details_responses);

        ChallengeResponsesListAdapter adapter = new ChallengeResponsesListAdapter(this, responses);

        for(int i = 0; i < adapter.getCount(); i++){
            challengeResponsesList.addView(adapter.getView(i, null, null));
        }

    }

    private void checkIfUserParticipatesInChallenge(int participationState) {
        if (participationState == ChallengeParticipation.NOT_PARTICIPATING_STATE) {
            findViewById(R.id.challenge_details_join).setVisibility(View.VISIBLE);
            findViewById(R.id.challenge_details_action_bar).setVisibility(View.VISIBLE);
        } else if (participationState == ChallengeParticipation.NOT_RESPONDED_STATE) {
            findViewById(R.id.challenge_details_action_bar).setVisibility(View.VISIBLE);
            findViewById(R.id.challenge_details_show_respond).setVisibility(View.VISIBLE);
        }
    }

    public void onClickJoinChallenge(View view) {
        ChallengeService.getService().joinChallenge(this, this.challenge);
    }

    public void finalizeJoinChallenge(CustomResponse response) {
        findViewById(R.id.challenge_details_join).setVisibility(View.GONE);
        findViewById(R.id.challenge_details_show_respond).setVisibility(View.VISIBLE);
        openVideoChooser();
    }

    public void onClickRespond(View view) {
        openVideoChooser();
    }

    private void openVideoChooser(){
        Intent videoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        videoIntent.setType("video/*");
        startActivityForResult(Intent.createChooser(videoIntent, "Select Video"), SELECT_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        FacebookService.callbackManager.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                responseVideo = CreateChallengeActivity.getPath(this, data.getData());
                TextView videoView = (TextView) findViewById(R.id.challenge_details_response_video);
                File videoName = new File(responseVideo);
                videoView.setText(videoName.getName());
                videoView.setVisibility(View.VISIBLE);
                findViewById(R.id.challenge_details_submit_response).setVisibility(View.VISIBLE);
                findViewById(R.id.challenge_details_show_respond).setVisibility(View.GONE);
            }
        }
    }

    public void onClickResponseVideo(View view){
        openVideoChooser();
    }

    public void onClickSubmit(View view){
        findViewById(R.id.challenge_details_submit_response).setEnabled(false);
        ProgressBar progress = (ProgressBar) findViewById(R.id.challenge_details_progress);
        progress.setVisibility(View.VISIBLE);
        FacebookService.getService().publishVideo(this, responseVideo,
                getString(R.string.response_for) + "" + challenge.getName(), progress, challenge.getId());
    }

    public void finalizeSubmitResponse(){
        findViewById(R.id.challenge_details_submit_response).setVisibility(View.GONE);
        findViewById(R.id.challenge_details_progress).setVisibility(View.GONE);
        findViewById(R.id.challenge_details_response_video).setVisibility(View.GONE);
    }

    public void finalizeRateResponse(){}
}
