package com.yagneshlp.slambook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.yagneshlp.slambook.R;
import com.yagneshlp.slambook.fragment.DeterminateViewFragment;

public class SplashActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.content_frame)
    FrameLayout content;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
       // initNavigationView();
        //configureToolbar();
        //showHomeView();
        determinateSampleMenuTouch();
    }






    private void determinateSampleMenuTouch() {
        showDeterminateView();
       // closeNav();
    }







    private void showDeterminateView() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, DeterminateViewFragment.newInstance())
                .commit();
    }




}