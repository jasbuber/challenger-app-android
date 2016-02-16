package com.cespenar;

import android.app.Activity;
import android.content.Context;
import android.support.test.espresso.IdlingResource;

import com.cespenar.thechallenger.R;

/**
 * Created by jasbuber on 2016-01-16.
 */
public class FacebookLoginIdlingResource implements IdlingResource {

    Activity context;

    ResourceCallback callback;

    public FacebookLoginIdlingResource(Activity context){
        this.context = context;
    }

    @Override
    public String getName() {
        return "FacebookLogin";
    }

    @Override
    public boolean isIdleNow() {
        if(context.findViewById(R.id.create_challenge_block) != null){
            callback.onTransitionToIdle();
            return true;
        }
        return false;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }
}
