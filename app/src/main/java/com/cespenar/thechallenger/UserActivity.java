package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.cespenar.thechallenger.models.User;
import com.cespenar.thechallenger.services.FacebookService;
import com.cespenar.thechallenger.services.UserService;


public class UserActivity extends Activity {

    private static User user;

    private static TextView usernameView;

    private static ImageView profilePictureView;

    private static TextView completedChallengesView;

    private static TextView joinedChallengesView;

    private static TextView createdChallengesView;

    private static TextView pointsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookService.getService().validateToken(this);
        setContentView(R.layout.activity_user);

        usernameView = (TextView) findViewById(R.id.user_profile_name);
        profilePictureView = (ImageView) findViewById(R.id.user_profile_picture);
        createdChallengesView = (TextView) findViewById(R.id.user_profile_created);
        completedChallengesView = (TextView) findViewById(R.id.user_profile_completed);
        joinedChallengesView = (TextView) findViewById(R.id.user_profile_joined);
        pointsView = (TextView) findViewById(R.id.user_profile_points);

        if (savedInstanceState != null) {

            User user = (User) savedInstanceState.getSerializable("user");
            long completedNr = savedInstanceState.getLong("completedNr");
            long joinedNr = savedInstanceState.getLong("joinedNr");
            long createdNr = savedInstanceState.getLong("createdNr");

            populateUserData(user, completedNr, joinedNr, createdNr);

        }else{
            String username = getIntent().getStringExtra("username");

            UserService.getService().getProfile(this, username);
        }

        if(getActionBar() != null){
            getActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("user", user);
        savedInstanceState.putLong("completedNr", Long.parseLong(completedChallengesView.getText().toString()));
        savedInstanceState.putLong("createdNr", Long.parseLong(createdChallengesView.getText().toString()));
        savedInstanceState.putLong("joinedNr", Long.parseLong(joinedChallengesView.getText().toString()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
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

    public void populateUserData(User viewedUser, long completedChallengesNr, long joinedChallengesNr,
                                 long createdChallengesNr){

        user = viewedUser;
        createdChallengesView.setText(String.valueOf(createdChallengesNr));
        joinedChallengesView.setText(String.valueOf(joinedChallengesNr));
        completedChallengesView.setText(String.valueOf(completedChallengesNr));
        usernameView.setText(user.getFormattedName());
        pointsView.setText(String.valueOf(user.getAllPoints()));

        FacebookService.getService().loadProfilePicture(this, profilePictureView, user.getUsername());


    }
}
