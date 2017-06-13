package com.yagneshlp.slambook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yagneshlp.slambook.R;

/**
 * Created by Yagnesh L P on 13-06-2017.
 */

public class ProfileActivity extends Activity {

    @Override
    public void onBackPressed()
    {
            finish();
           Intent i = new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.alerter_slide_in_from_top,R.anim.no_change);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void goup(View view)
    {
        finish();
        Intent i = new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.alerter_slide_in_from_top,R.anim.no_change);

    }
}
