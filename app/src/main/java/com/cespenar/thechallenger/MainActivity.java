package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cespenar.thechallenger.services.FacebookService;
import com.cespenar.thechallenger.services.TutorialService;
import com.cespenar.thechallenger.services.UserService;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        if(!getFacebookService().isAccessTokenValid()) {
            FacebookService.getService().logIn(this);
        }else{
            setContentView(R.layout.activity_main);
        }

        getTutorialService().handleTutorial(this);
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                intent.putExtra("username", getCurrentUsername());
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

    public void onClickCreateChallenge(View view) {

        Intent intent = new Intent(this, CreateChallengeActivity.class);
        startActivity(intent);

    }

    public void onClickBrowseChallenges(View view) {
        Intent intent = new Intent(this, BrowseChallengesActivity.class);
        startActivity(intent);
    }

    public void onClickMyChallenges(View view) {
        Intent intent = new Intent(this, MyChallengesActivity.class);
        startActivity(intent);
    }

    public void onClickRankings(View view) {
        Intent intent = new Intent(this, RankingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FacebookService.callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected FacebookService getFacebookService(){
        return FacebookService.getService();
    }

    protected TutorialService getTutorialService(){
        return TutorialService.getService();
    }

    protected String getCurrentUsername(){
        return UserService.getCurrentUsername();
    }

}
