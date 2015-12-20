package com.cespenar.thechallenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NoConnectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);
    }

    public void onClickConnectAgain(View view){
        startActivity(new Intent(this, MainActivity.class));
    }
}
