package com.yagneshlp.slambook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


import com.yagneshlp.slambook.R;
import com.yagneshlp.slambook.other.StereoView;
import com.yagneshlp.slambook.other.LogUtil;


public class newLoginActivity extends AppCompatActivity  {

    private EditText etUsername;
    private EditText etPassword;
    private StereoView stereoView;
    private int translateY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        startEnterAnim();
        etUsername.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    stereoView.toNext();
                    etPassword.requestFocus();
                    return true;
                }
                return false;
            }
        });

        etPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    startExitAnim();
                    return true;
                }
                return false;
            }
        });
       // stereoView.setStartScreen(0);
        stereoView.setResistance(1f);
        stereoView.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                stereoView.getLocationOnScreen(location);
                translateY = location[1];
            }
        });
        stereoView.setiStereoListener(new StereoView.IStereoListener() {
            @Override
            public void toPre(int curScreen) {
                LogUtil.m("跳转到前一页 " + curScreen);
            }

            @Override
            public void toNext(int curScreen) {
                LogUtil.m("跳转到下一页 " + curScreen);
            }
        });
    }

    private void initView() {
        stereoView = (StereoView) findViewById(R.id.stereoView);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
            }




    private void startExitAnim() {
        stereoView.animate()
                .alpha(0f)
                .translationY(-100f)
                .start();

    }
    private void startEnterAnim() {
        stereoView.setAlpha(0f);
        stereoView.animate()
                .alpha(1f)
                .setDuration(800L)
                .translationY(-150f)
                .start();

    }
    public void goNXT(View view)
    {
        stereoView.toNext();
    }
}
