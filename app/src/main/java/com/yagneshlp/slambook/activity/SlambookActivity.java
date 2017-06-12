package com.yagneshlp.slambook.activity;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yagneshlp.slambook.R;
import com.yagneshlp.slambook.fragment.Part1;
import com.yagneshlp.slambook.fragment.Part10;
import com.yagneshlp.slambook.fragment.Part11;
import com.yagneshlp.slambook.fragment.Part12;
import com.yagneshlp.slambook.fragment.Part13;
import com.yagneshlp.slambook.fragment.Part14;
import com.yagneshlp.slambook.fragment.Part15;
import com.yagneshlp.slambook.fragment.Part16;
import com.yagneshlp.slambook.fragment.Part17;
import com.yagneshlp.slambook.fragment.Part18;
import com.yagneshlp.slambook.fragment.Part19;
import com.yagneshlp.slambook.fragment.Part2;
import com.yagneshlp.slambook.fragment.Part20;
import com.yagneshlp.slambook.fragment.Part21;
import com.yagneshlp.slambook.fragment.Part22;
import com.yagneshlp.slambook.fragment.Part23;
import com.yagneshlp.slambook.fragment.Part3;
import com.yagneshlp.slambook.fragment.Part4;
import com.yagneshlp.slambook.fragment.Part5;
import com.yagneshlp.slambook.fragment.Part6;
import com.yagneshlp.slambook.fragment.Part7;
import com.yagneshlp.slambook.fragment.Part8;
import com.yagneshlp.slambook.fragment.Part9;

import java.util.ArrayList;
import java.util.List;

public class SlambookActivity extends AppCompatActivity {

    public static ViewPager viewPager;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slambook);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);



    }

    private void setupViewPager(ViewPager viewPager) {
        SlambookActivity.ViewPagerAdapter adapter = new SlambookActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Part1(), "ONE");
        adapter.addFragment(new Part2(), "TWO");
        adapter.addFragment(new Part3(), "THREE");
        adapter.addFragment(new Part4(), "FOUR");
        adapter.addFragment(new Part5(), "FIVE");
        adapter.addFragment(new Part6(), "SIX");
        adapter.addFragment(new Part7(), "SEVEN");
        adapter.addFragment(new Part8(), "EIGHT");
        adapter.addFragment(new Part9(), "NINE");
        adapter.addFragment(new Part10(), "TEN");
        adapter.addFragment(new Part11(), "TEN");
        adapter.addFragment(new Part12(), "TEN");
        adapter.addFragment(new Part13(), "TEN");
        adapter.addFragment(new Part14(), "TEN");
        adapter.addFragment(new Part15(), "TEN");
        adapter.addFragment(new Part16(), "TEN");
        adapter.addFragment(new Part17(), "TEN");
        adapter.addFragment(new Part18(), "TEN");
        adapter.addFragment(new Part19(), "TEN");
        adapter.addFragment(new Part20(), "TEN");
        adapter.addFragment(new Part21(), "TEN");
        adapter.addFragment(new Part22(), "TEN");
        adapter.addFragment(new Part23(), "TEN");


        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }




    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
