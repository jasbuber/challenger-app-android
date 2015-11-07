package com.cespenar.thechallenger;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.cespenar.thechallenger.models.Comment;
import com.cespenar.thechallenger.models.CustomResponse;
import com.cespenar.thechallenger.models.User;
import com.cespenar.thechallenger.services.ChallengeService;
import com.cespenar.thechallenger.services.FacebookService;
import com.cespenar.thechallenger.services.UserService;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChallengeActivity extends Activity {

    private static final int SELECT_VIDEO = 1;

    private static LinearLayout challengeResponsesList;

    private static LinearLayout challengeCommentsList;

    private Challenge challenge;

    private static String responseVideo;

    private static VideoView videoView;

    private int participationState;

    private List<ChallengeResponse> responses;

    private List<Comment> comments;

    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookService.getService().validateToken(this);
        setContentView(R.layout.activity_challenge);
        videoView = (VideoView) findViewById(R.id.challenge_details_video);
        findViewById(R.id.challenge_details_comment_message)
                .setOnFocusChangeListener(getOnFocusChangeListener(this));

        if(savedInstanceState != null){
            challenge = (Challenge) savedInstanceState.getSerializable("challenge");
            participationState = savedInstanceState.getInt("participationState");
            responseVideo = savedInstanceState.getString("responseVideo");
            responses = (List<ChallengeResponse>) savedInstanceState.getSerializable("responses");
            page = savedInstanceState.getInt("page");

            fillChallengeDetails(challenge);
            checkIfUserParticipatesInChallenge(participationState);
            populateChallengeResponses(responses);
            populateChallengeComments((List<Comment>) savedInstanceState.getSerializable("comments"));
            ChallengeService.getService().getVideo(this, challenge.getVideoPath(), videoView);

            return;
        }

        ChallengeService.getService().getChallenge(this, getIntent().getLongExtra("challengeId", -1), videoView);

        if(getActionBar() != null){
            getActionBar().setHomeButtonEnabled(true);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("challenge", challenge);
        savedInstanceState.putString("responseVideo", responseVideo);
        savedInstanceState.putInt("participationState", participationState);
        savedInstanceState.putSerializable("responses", (ArrayList) responses);
        savedInstanceState.putSerializable("comments", (ArrayList) comments);
        savedInstanceState.putInt("page", page);
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
            case android.R.id.home:
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
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

        FacebookService.getService().loadProfilePicture(this, creatorPicture, challenge.getCreator().getUsername());
    }

    public void populateChallenge(Challenge challenge, int participationState, List<Comment> comments) {
        this.challenge = challenge;
        this.participationState = participationState;

        fillChallengeDetails(challenge);

        checkIfUserParticipatesInChallenge(participationState);

        ChallengeService.getService().getChallengeResponses(this, challenge);

        populateChallengeComments(comments);
    }

    public void populateChallengeResponses(List<ChallengeResponse> responses){

        this.responses = responses;
        challengeResponsesList = (LinearLayout) findViewById(R.id.challenge_details_responses);

        ChallengeResponsesListAdapter adapter = new ChallengeResponsesListAdapter(this, responses);

        for(int i = 0; i < adapter.getCount(); i++){
            challengeResponsesList.addView(adapter.getView(i, null, null));
        }
    }

    public void populateChallengeComments(List<Comment> comments){

        if(this.comments == null){
            this.comments = new ArrayList<>();
        }

        this.comments.addAll(comments);
        challengeCommentsList = (LinearLayout) findViewById(R.id.challenge_details_comments);

        for(Comment comment : comments){
            View view = getLayoutInflater().inflate(R.layout.list_item_comments, null);

            TextView author = (TextView) view.findViewById(R.id.comment_author_name);
            author.setText(comment.getAuthor().getFormattedName());
            String date = String.valueOf(DateUtils.getRelativeTimeSpanString(
                    comment.getCreationTimestamp().getTime(), new Date().getTime(), DateUtils.SECOND_IN_MILLIS));
            ((TextView) view.findViewById(R.id.comment_creation_date)).setText(date);
            ((TextView) view.findViewById(R.id.comment_message)).setText(comment.getMessage());

            author.setOnClickListener(getOnClickAuthorListener(this, comment.getAuthor().getUsername()));
            challengeCommentsList.addView(view);
        }

        if (comments.size() % 10 == 0 && !comments.isEmpty()) {
            findViewById(R.id.show_more_comments).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.show_more_comments).setVisibility(View.GONE);
        }
    }

    private View.OnClickListener getOnClickAuthorListener(final Activity activity, final String username){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UserActivity.class);
                intent.putExtra("username", username);

                startActivity(intent);
            }
        };
    }

    private void checkIfUserParticipatesInChallenge(int participationState) {

        if (participationState == ChallengeParticipation.NOT_PARTICIPATING_STATE) {
            findViewById(R.id.challenge_details_join).setVisibility(View.VISIBLE);
            findViewById(R.id.challenge_details_action_bar).setVisibility(View.VISIBLE);
        } else if (participationState == ChallengeParticipation.NOT_RESPONDED_STATE) {
            findViewById(R.id.challenge_details_action_bar).setVisibility(View.VISIBLE);
            findViewById(R.id.challenge_details_show_respond).setVisibility(View.VISIBLE);
        }else if (participationState == ChallengeParticipation.VIDEO_CHOSEN) {
            findViewById(R.id.challenge_details_action_bar).setVisibility(View.VISIBLE);
            TextView videoView = (TextView) findViewById(R.id.challenge_details_response_video);
            File videoName = new File(responseVideo);
            videoView.setText(videoName.getName());
            videoView.setVisibility(View.VISIBLE);
            findViewById(R.id.challenge_details_submit_response).setVisibility(View.VISIBLE);
            findViewById(R.id.challenge_details_show_respond).setVisibility(View.GONE);
        }else if(participationState == ChallengeParticipation.RESPONDED){

            final Activity activity = this;
            RatingBar rating = (RatingBar) findViewById(R.id.challenge_details_rating);

            rating.setIsIndicator(false);
            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    openRateChallengeDialog(activity);
                }
            });
        }
    }

    public void onClickJoinChallenge(View view) {
        ChallengeService.getService().joinChallenge(this, this.challenge);
    }

    public void finalizeJoinChallenge(CustomResponse response) {
        findViewById(R.id.challenge_details_join).setVisibility(View.GONE);
        findViewById(R.id.challenge_details_show_respond).setVisibility(View.VISIBLE);
        this.participationState = ChallengeParticipation.NOT_RESPONDED_STATE;
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
                this.participationState = ChallengeParticipation.VIDEO_CHOSEN;
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
                getString(R.string.response_for) + " " + challenge.getName(), progress, challenge.getId());
    }

    public void finalizeSubmitResponse(){
        findViewById(R.id.challenge_details_submit_response).setVisibility(View.GONE);
        findViewById(R.id.challenge_details_progress).setVisibility(View.GONE);
        findViewById(R.id.challenge_details_response_video).setVisibility(View.GONE);
        participationState = ChallengeParticipation.RESPONDED;

        openRateChallengeDialog(this);
    }

    public void finalizeRateResponse(){}

    public void onClickShowProfile(View view){
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("username", challenge.getCreator().getUsername());

        startActivity(intent);
    }

    public void openRateChallengeDialog(final Activity activity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_rate_challenge);
        dialog.show();

        TextView cancelAction = (TextView) dialog.findViewById(R.id.rate_challenge_dialog_cancel_action);
        Button rateAction = (Button) dialog.findViewById(R.id.rate_challenge_dialog_rate_action);
        final RatingBar rating = (RatingBar) dialog.findViewById(R.id.rate_challenge_dialog_rating);

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if (rating < 1.0f) {
                    ratingBar.setRating(1.0f);
                }
            }
        });

        cancelAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                RatingBar rating = (RatingBar) findViewById(R.id.challenge_details_rating);

                rating.setIsIndicator(false);
                rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        openRateChallengeDialog(activity);
                    }
                });
            }
        });

        rateAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChallengeService.getService().rateChallenge(activity, challenge.getId(), (int) rating.getRating());
                dialog.dismiss();
                RatingBar rating = (RatingBar) activity.findViewById(R.id.challenge_details_rating);
                rating.setIsIndicator(true);
                rating.setRating(challenge.getRating());
            }
        });
    }

    private boolean validateCommentMessage(EditText commentMessageElement) {

        String commentMessage = commentMessageElement.getText().toString();

        if (commentMessage.trim().isEmpty()) {
            commentMessageElement.setError(getString(R.string.validation_comment_empty));
            return false;
        } else if (commentMessage.trim().length() > 250) {
            commentMessageElement.setError(getString(R.string.validation_comment_too_long));
            return false;
        }

        commentMessageElement.setError(null);
        return true;
    }

    public void openCommentDialog(final Activity activity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_comment_challenge);
        dialog.show();

        TextView cancelAction = (TextView) dialog.findViewById(R.id.comment_dialog_cancel_action);
        Button commentAction = (Button) dialog.findViewById(R.id.comment_dialog_submit_action);

        cancelAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        commentAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText messageElement = (EditText) dialog.findViewById(R.id.comment_dialog_message);

                if (!validateCommentMessage(messageElement)) {
                    return;
                }

                String message = messageElement.getText().toString();

                ChallengeService.getService().createComment(activity, challenge.getId(), message);

                View view = getLayoutInflater().inflate(R.layout.list_item_comments, null);

                ((TextView) view.findViewById(R.id.comment_author_name)).setText(UserService.getCurrentUser().getFormattedName());
                ((TextView) view.findViewById(R.id.comment_message)).setText(message);

                comments.add(0, new Comment(UserService.getCurrentUser(), message, challenge.getId(),
                        new Date()));
                challengeCommentsList.addView(view, 0);

                messageElement.setText("");
                dialog.dismiss();
            }
        });

    }

    private View.OnFocusChangeListener getOnFocusChangeListener(final Activity activity){
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    openCommentDialog(activity);
                    v.clearFocus();
                }
            }
        };
    }

    public void onClickShowMoreComments(View v){
        page++;

        ChallengeService.getService().getComments(this, challenge.getId(), page);
    }

}
