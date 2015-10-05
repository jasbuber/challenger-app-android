package com.cespenar.thechallenger;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.VideoView;

import com.cespenar.thechallenger.models.Challenge;
import com.cespenar.thechallenger.models.ChallengeResponse;
import com.cespenar.thechallenger.services.FacebookService;


public class ChallengeResponseActivity extends Activity {

    private static ChallengeResponse response;

    private static VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookService.getService().validateToken(this);

        setContentView(R.layout.activity_challenge_response);
        videoView = (VideoView) findViewById(R.id.challenge_response_video);

        if(savedInstanceState != null){
             response = (ChallengeResponse) savedInstanceState.getSerializable("response");
        }else{
            response = (ChallengeResponse) getIntent().getSerializableExtra("response");
        }

        FacebookService.getService().getVideo(this, response.getVideoResponseUrl(), videoView);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("response", response);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_challenge_response, menu);
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
}
