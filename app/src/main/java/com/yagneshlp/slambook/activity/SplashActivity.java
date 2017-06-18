package com.yagneshlp.slambook.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.master.permissionhelper.PermissionHelper;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;
import com.onurciner.toastox.ToastOXDialog;
import com.yagneshlp.slambook.R;
import com.yagneshlp.slambook.helper.SessionManager;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;



public class SplashActivity extends Activity {

    private static final String TAG = "MainActivity";



    TextView splashStat;
    ValueAnimator anim;
    CircularFillableLoaders circload;
    int i;
    PermissionHelper permissionHelper;

    @Override
    protected void onResume()
    {
        super.onResume();
        pseudoAnim();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        splashStat = (TextView) findViewById(R.id.splashStatus);
        circload = (CircularFillableLoaders)findViewById(R.id.circularFillableLoaders);
        pseudoAnim();

         }



    private void pseudoAnim()
    {
       circload.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashStat.setText("Setting up the Environment");
                circload.setProgress(10);
                permissionHelper = new PermissionHelper(SplashActivity.this, new String[]{Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                permissionHelper.request(new PermissionHelper.PermissionCallback() {
                    @Override
                    public void onPermissionGranted() {
                        Log.d(TAG, "onPermissionGranted() called");
                    }

                    @Override
                    public void onPermissionDenied() {
                        Log.d(TAG, "onPermissionDenied() called");
                    }

                    @Override
                    public void onPermissionDeniedBySystem() {
                        Log.d(TAG, "onPermissionDeniedBySystem() called");
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        splashStat.setText("Reading Local Settings");
                        circload.setProgress(40);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                splashStat.setText("Checking internet Connection");
                                ConnectivityManager cm = (ConnectivityManager) SplashActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                                if (activeNetwork != null) { // connected to the internet
                                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                                        circload.setProgress(60);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                splashStat.setText("Communicating with server");
                                                circload.setProgress(80);
                                                exit_anim();
                                            }
                                        }, 1000);

                                    }
                                    else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                                        circload.setProgress(60);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                splashStat.setText("Communicating with server");
                                                circload.setProgress(80);
                                                exit_anim();
                                            }
                                        }, 900);

                                    }
                                } else {
                                    circload.setVisibility(View.INVISIBLE);
                                    splashStat.setText("No Internet !!!");
                                    new ToastOXDialog.Build(SplashActivity.this)
                                            .setTitle("No Internet!")
                                            .setContent("No Internet Connection Detected!\nCannot Ping server")
                                            .setPositiveText("Wi-Fi")
                                            //.setPositiveBackgroundColorResource(R.color.orange)
                                            //.setPositiveTextColorResource(R.color.black)
                                            .onPositive(new ToastOXDialog.ButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull ToastOXDialog toastOXDialog) {
                                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                                    Log.i("Click","Yes");
                                                    closeContextMenu();

                                                }
                                            })
                                            .setNegativeText("Mobile Data")
                                            // .setNegativeBackgroundColorResource(R.color.black)
                                            //.setNegativeTextColorResource(R.color.orange)
                                            .onNegative(new ToastOXDialog.ButtonCallback(){
                                                @Override
                                                public void onClick(@NonNull ToastOXDialog toastOXDialog) {
                                                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                                                    Log.w("Click","No");
                                                    closeContextMenu();
                                                }
                                            }).show();


                                }
                            }
                        }, 1500);


                    }
                }, 1200);

            }
        }, 1000);
    }

    protected void exit_anim()
    {
       for(i=80;i<=100; i++)
       {
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   circload.setProgress(i);
               }
           }, 100);
       }
       clearRoute();

    }

    protected void clearRoute()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                circload.animate().alpha(0f).setDuration(1000).start();
                splashStat.animate().alpha(0f).setDuration(1000).start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.fade_out,R.anim.no_change);
                            finishAffinity();
                            finish();

                    }
                }, 1100);

            }
        }, 1000);

    }











}