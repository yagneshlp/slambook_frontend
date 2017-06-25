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

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.master.permissionhelper.PermissionHelper;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;
import com.onurciner.toastox.ToastOXDialog;
import com.yagneshlp.slambook.R;
import com.yagneshlp.slambook.app.AnalyticsTrackers;
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
    private static SplashActivity mInstance;
    AlertDialog.Builder builder;
    AlertDialog dialog;

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
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);

        splashStat = (TextView) findViewById(R.id.splashStatus);
        circload = (CircularFillableLoaders)findViewById(R.id.circularFillableLoaders);
        pseudoAnim();

         builder = new AlertDialog.Builder(SplashActivity.this,R.style.MyAlertDialogStyle)
                .setTitle("No Internet!")
                .setMessage("No Internet Connection Detected!\nCannot Ping server")
                .setPositiveButton("Wi-Fi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.i("Click","Yes");
                        dialog.dismiss();
                        dialog.dismiss();
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));


                    }
                })
                .setNegativeButton("Mobile Data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.w("Click","No");
                        dialog.dismiss();
                        dialog.dismiss();
                        startActivity(new Intent(Settings.ACTION_SETTINGS));

                    }
                })
                .setCancelable(true);

         }

    public static synchronized SplashActivity getInstance() {
        return mInstance;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */

    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
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
                                    dialog=builder.create();
                                    dialog.show();




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