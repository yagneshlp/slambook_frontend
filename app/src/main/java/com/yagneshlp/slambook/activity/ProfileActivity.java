package com.yagneshlp.slambook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yagneshlp.slambook.R;
import com.yagneshlp.slambook.helper.SQLiteHandler;
import com.yagneshlp.slambook.helper.SessionManager;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

/**
 * Created by Yagnesh L P on 13-06-2017.
 */

public class ProfileActivity extends Activity {
    TextView name,email;
    AvatarView avatarView;
    IImageLoader imageLoader;

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
        name=(TextView) findViewById(R.id.textViewName);
        email=(TextView) findViewById(R.id.textViewEmail);
        SQLiteHandler db= new SQLiteHandler(this);  //object of the sqlLite helper
        SessionManager cur = new SessionManager(this);  //object of the session manager
        String emailid=db.getEmail();  //getting the current userid from local db
        String uname=cur.getUsername(); //getting current user name from sessionmnager
        if(emailid != "null")
            email.setText(emailid);
        else
            email.setText("Email id not available");
        name.setText(uname);
        avatarView = (AvatarView) findViewById(R.id.avatarPic);

        imageLoader = new PicassoLoader();
        imageLoader.loadImage(avatarView, "http:/example.com/user/someUserAvatar.png", uname);

    }

    public void goup(View view)
    {
        finish();
        Intent i = new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.alerter_slide_in_from_top,R.anim.no_change);

    }
}
