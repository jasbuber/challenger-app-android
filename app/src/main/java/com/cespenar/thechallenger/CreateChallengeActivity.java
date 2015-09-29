package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.CustomResponse;
import com.cespenar.thechallenger.services.ChallengeService;
import com.cespenar.thechallenger.services.FacebookService;

import java.io.File;

public class CreateChallengeActivity extends Activity {

    private static final int SELECT_VIDEO = 1;

    private static String challengeVideo;

    private boolean isStep2Visible = false;
    private boolean isStep3Visible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("isStep2Visible")) {

                onClickStepOne(findViewById(R.id.show_second_step_action));

                if (savedInstanceState.getBoolean("isStep3Visible")) {
                    onClickStepTwo(findViewById(R.id.show_third_step_action));
                }
            }

            getChallengeNameElement().setText(savedInstanceState.getString("challengeName"));
            challengeVideo = savedInstanceState.getString("challengeVideo");
            if (challengeVideo != null) {
                getChallengeVideoElement().setText(new File(challengeVideo).getName());
            }
            getChallengeCategoryElement().setSelection(savedInstanceState.getInt("challengeCategory"));
            getChallengeDifficultyElement().setProgress(savedInstanceState.getInt("challengeDifficulty"));
            getChallengeVisibilityElement().setSelection(savedInstanceState.getInt("challengeVisibility"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("challengeName", getChallengeNameElement().getText().toString());
        savedInstanceState.putInt("challengeCategory", getChallengeCategoryElement().getSelectedItemPosition());
        savedInstanceState.putString("challengeVideo", challengeVideo);
        savedInstanceState.putInt("challengeDifficulty", getChallengeDifficultyElement().getProgress());
        savedInstanceState.putInt("challengeVisibility", getChallengeVisibilityElement().getSelectedItemPosition());

        savedInstanceState.putBoolean("isStep2Visible", isStep2Visible);
        savedInstanceState.putBoolean("isStep3Visible", isStep3Visible);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_challenge, menu);
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

    public void onClickUpload(View view) {
        Intent videoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        videoIntent.setType("video/*");
        startActivityForResult(Intent.createChooser(videoIntent, "Select Video"), SELECT_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                challengeVideo = getPath(data.getData());
                File videoName = new File(challengeVideo);

                getChallengeVideoElement().setText(videoName.getName());
                getChallengeVideoElement().setError(null);
            }
        }
    }

    public String getPath(Uri uri) {

        String result = null;

        Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(column_index);
        }
        cursor.close();

        return result;
    }

    public void onClickStepOne(View view) {
        view.setVisibility(View.GONE);
        findViewById(R.id.second_step).setVisibility(View.VISIBLE);
        findViewById(R.id.first_step_arrow).setVisibility(View.VISIBLE);
        isStep2Visible = true;
    }

    public void onClickStepTwo(View view) {
        view.setVisibility(View.GONE);
        findViewById(R.id.third_step).setVisibility(View.VISIBLE);
        findViewById(R.id.second_step_arrow).setVisibility(View.VISIBLE);
        isStep3Visible = true;
    }

    public void onClickSubmit(View view) {

        EditText challengeNameElement = getChallengeNameElement();
        String challengeName = challengeNameElement.getText().toString();

        if (validateChallengeName(challengeNameElement, challengeName) & validateChallengeVideo()) {

            view.setEnabled(false);

            String challengeCategory = (String) getChallengeCategoryElement().getSelectedItem();
            boolean challengeVisibility = false;
            if (getChallengeVisibilityElement().getSelectedItemPosition() == 0) {
                challengeVisibility = true;
            }
            int challengeDifficulty = getChallengeDifficultyElement().getProgress();

            Challenge challenge = new Challenge(challengeName, challengeVideo, challengeCategory,
                    challengeVisibility, challengeDifficulty);

            ChallengeService.getService().createChallenge(this, challenge);
        }
    }

    private boolean validateChallengeName(EditText challengeNameElement, String challengeName) {
        if (challengeName.isEmpty()) {
            challengeNameElement.setError(getString(R.string.validation_challenge_name_empty));
            return false;
        } else if (challengeName.length() <= 5) {
            challengeNameElement.setError(getString(R.string.validation_challenge_name_too_short));
            return false;
        } else if (challengeName.length() > 255) {
            challengeNameElement.setError(getString(R.string.validation_challenge_name_too_long));
            return false;
        }
        challengeNameElement.setError(null);
        return true;
    }

    private boolean validateChallengeVideo() {
        if (challengeVideo == null) {
            getChallengeVideoElement().setError(getString(R.string.validation_challenge_video_empty));
            return false;
        }
        getChallengeVideoElement().setError(null);
        return true;
    }

    public void finalizeCreateChallenge(CustomResponse response){

        if(response.getStatus() == CustomResponse.ResponseStatus.failure){

            for(String message : response.getMessages()){
                switch(message){
                    case "no_participants" :
                        break;
                }

            }

            findViewById(R.id.create_challenge_submit).setEnabled(true);

            return;
        }

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.create_challenge_progress);
        String challengeName = getChallengeNameElement().getText().toString();

        FacebookService.getService().publishVideo(this, challengeVideo, challengeName, progressBar, response.getChallengeId());
    }

    private EditText getChallengeNameElement() {
        return (EditText) findViewById(R.id.create_challenge_name);
    }

    private Spinner getChallengeCategoryElement() {
        return (Spinner) findViewById(R.id.create_challenge_category);
    }

    private Spinner getChallengeVisibilityElement() {
        return (Spinner) findViewById(R.id.create_challenge_visibility);
    }

    private SeekBar getChallengeDifficultyElement() {
        return (SeekBar) findViewById(R.id.create_challenge_difficulty);
    }

    private Button getChallengeVideoElement() {
        return (Button) findViewById(R.id.create_challenge_upload);
    }

}
