package com.yagneshlp.slambook.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.yagneshlp.slambook.R;

import java.io.File;

/**
 * Created by Yagnesh L P on 20-06-2017.
 */

public class CreditsActivity extends Activity {

    @Override
    public void onBackPressed()    {

        finish();
        startActivity(new Intent(CreditsActivity.this,HelpActivity.class));
        overridePendingTransition(R.anim.slide_right,R.anim.no_change);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
    }

    public void eula(View view)
    {
        String url = "http://slambook.yagneshlp.com/eula/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void policy(View view)
    {
        String url = "http://slambook.yagneshlp.com/privacy/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void thirdparty(View view)
    {
        String url = "http://slambook.yagneshlp.com/thirdparty/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}
