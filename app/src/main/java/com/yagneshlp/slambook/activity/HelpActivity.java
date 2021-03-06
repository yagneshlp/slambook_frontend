package com.yagneshlp.slambook.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.yagneshlp.slambook.R;

/**
 * Created by Yagnesh L P on 13-06-2017.
 */

public class HelpActivity extends Activity {

    @Override
    public void onBackPressed()    {

            finish();
            startActivity(new Intent(HelpActivity.this,MainActivity.class));
            overridePendingTransition(R.anim.slide_left,R.anim.no_change);

    }

    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }
    public void credits(View view)
    {
        startActivity(new Intent(HelpActivity.this,CreditsActivity.class));
        overridePendingTransition(R.anim.slide_left,R.anim.no_change);
    }
    public void ylp(View view)
    {
        String url = "http://yagneshlp.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void help(View view)
    {

    }
    public void license(View view)
    {

    }
    public void goBack(View view)
    {
        finish();
        startActivity(new Intent(HelpActivity.this,MainActivity.class));
        overridePendingTransition(R.anim.slide_left,R.anim.no_change);
    }


}
