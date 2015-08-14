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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class CreateChallengeActivity extends Activity {

    private static final int SELECT_VIDEO = 1;

    private String challengeVideo;
    private String challengeName;
    private int challengeCategory;
    private int challengeDifficulty;
    private int challengeVisibility;
    private String challengeParticipants;

    private boolean isStep2Visible = false;
    private boolean isStep3Visible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);

        if(savedInstanceState != null){
            if(savedInstanceState.getBoolean("isStep2Visible")) {

                findViewById(R.id.show_second_step_action).setVisibility(View.GONE);
                findViewById(R.id.second_step).setVisibility(View.VISIBLE);
                findViewById(R.id.first_step_arrow).setVisibility(View.VISIBLE);
                isStep2Visible = true;

                if (savedInstanceState.getBoolean("isStep3Visible")) {
                    findViewById(R.id.show_third_step_action).setVisibility(View.GONE);
                    findViewById(R.id.third_step).setVisibility(View.VISIBLE);
                    findViewById(R.id.second_step_arrow).setVisibility(View.VISIBLE);
                    isStep3Visible = true;
                }
            }

            ((EditText) findViewById(R.id.create_challenge_name)).setText(savedInstanceState.getString("challengeName"));
            challengeVideo = savedInstanceState.getString("challengeVideo");
            if(challengeVideo != null) {
                ((Button) findViewById(R.id.create_challenge_upload)).setText(new File(challengeVideo).getName());
            }
            ((Spinner) findViewById(R.id.create_challenge_category)).setSelection(savedInstanceState.getInt("challengeCategory"));
            ((SeekBar) findViewById(R.id.create_challenge_difficulty)).setProgress(savedInstanceState.getInt("challengeDifficulty"));
            ((Spinner) findViewById(R.id.create_challenge_visibility)).setSelection(savedInstanceState.getInt("challengeVisibility"));


        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

        EditText challengeName = (EditText) findViewById(R.id.create_challenge_name);
        Spinner challengeCategory = (Spinner) findViewById(R.id.create_challenge_category);
        SeekBar challengeDifficulty = (SeekBar) findViewById(R.id.create_challenge_difficulty);
        Spinner challengeVisibility = (Spinner) findViewById(R.id.create_challenge_visibility);

        savedInstanceState.putString("challengeName", challengeName.getText().toString());
        savedInstanceState.putInt("challengeCategory", challengeCategory.getSelectedItemPosition());
        savedInstanceState.putString("challengeVideo", challengeVideo);
        savedInstanceState.putInt("challengeDifficulty", challengeDifficulty.getProgress());
        savedInstanceState.putInt("challengeVisibility", challengeVisibility.getSelectedItemPosition());


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

    public void onClickUpload(View view){
        Intent videoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        videoIntent.setType("video/*");
        startActivityForResult(Intent.createChooser(videoIntent, "Select Video"), SELECT_VIDEO);
    }

    @ Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                challengeVideo = getPath(data.getData());
                File videoName = new File(challengeVideo);

                ((Button)findViewById(R.id.create_challenge_upload)).setText(videoName.getName());
            }
        }
    }

    public String getPath(Uri uri) {

        String result = null;

        Cursor cursor = getContentResolver().query(uri, new String[]{ MediaStore.Images.Media.DATA }, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(column_index);
        }
        cursor.close();

        return result;
    }

    public void onClickStepOne(View view){
        view.setVisibility(View.GONE);
        findViewById(R.id.second_step).setVisibility(View.VISIBLE);
        findViewById(R.id.first_step_arrow).setVisibility(View.VISIBLE);
        isStep2Visible = true;
    }

    public void onClickStepTwo(View view){
        view.setVisibility(View.GONE);
        findViewById(R.id.third_step).setVisibility(View.VISIBLE);
        findViewById(R.id.second_step_arrow).setVisibility(View.VISIBLE);
        isStep3Visible = true;
    }

    public void onClickSubmit(View view){
        view.setEnabled(false);

    }
}
