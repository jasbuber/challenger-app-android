package com.cespenar.thechallenger;

import android.app.Activity;
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
        setContentView(R.layout.activity_user);

        String username = getIntent().getStringExtra("username");

        usernameView = (TextView) findViewById(R.id.user_profile_name);
        profilePictureView = (ImageView) findViewById(R.id.user_profile_picture);
        createdChallengesView = (TextView) findViewById(R.id.user_profile_created);
        completedChallengesView = (TextView) findViewById(R.id.user_profile_completed);
        joinedChallengesView = (TextView) findViewById(R.id.user_profile_joined);
        pointsView = (TextView) findViewById(R.id.user_profile_points);

        UserService.getService().getProfile(this, username);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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

        FacebookService.getService().loadProfilePicture(profilePictureView, user.getProfilePictureUrl());


    }
}
