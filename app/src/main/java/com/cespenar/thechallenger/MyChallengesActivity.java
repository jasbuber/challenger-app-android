package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cespenar.thechallenger.services.FacebookService;


public class MyChallengesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookService.getService().validateToken(this);
        setContentView(R.layout.activity_my_challenges);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_challenges, menu);
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

    public void onClickCreatedChallenges(View view){
        Intent intent = new Intent(this, CreatedChallengesActivity.class);
        startActivity(intent);
    }

    public void onClickChallengeParticipations(View view){
        Intent intent = new Intent(this, ChallengeParticipationsActivity.class);
        startActivity(intent);
    }
}
