package com.yagneshlp.slambook.activity;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dd.processbutton.iml.ActionProcessButton;
import com.github.yongjhih.mismeter.MisMeter;
import com.michaldrabik.tapbarmenulib.TapBarMenu;

import com.onurciner.toastox.ToastOXDialog;
import com.yagneshlp.slambook.R;

import com.yagneshlp.slambook.app.AppConfig;
import com.yagneshlp.slambook.app.AppController;
import com.yagneshlp.slambook.helper.SQLiteHandler;
import com.yagneshlp.slambook.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static android.view.View.GONE;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.tapBarMenu) TapBarMenu tapBarMenu;
    TextView tv,tvPerc;
     MisMeter meter;
    ValueAnimator anim;
    ActionProcessButton button;
    CardView cv;
    TapBarMenu t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t=(TapBarMenu) findViewById(R.id.tapBarMenu);
        t.setVisibility(View.INVISIBLE);
        meter = (MisMeter) findViewById(R.id.meter);
        tv=(TextView) findViewById(R.id.tview);
        tvPerc = (TextView) findViewById(R.id.percComp);
        tvPerc.setVisibility(GONE);
        cv= (CardView) findViewById(R.id.cardView);
        button = (ActionProcessButton) findViewById(R.id.btn_fill);
        button.setMode(ActionProcessButton.Mode.ENDLESS);
        tv.setVisibility(View.INVISIBLE);
        cv.setVisibility(GONE);
        insert_into();
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.toggle();
            }
        });
        ButterKnife.bind(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setProgress(1);
                ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        startActivity(new Intent(MainActivity.this,SlambookActivity.class));
                        button.setProgress(100);
                    }
                    else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        startActivity(new Intent(MainActivity.this,SlambookActivity.class));
                        button.setProgress(100);
                    }
                } else {
                    //Alerter.create(MainActivity.this)
                      //      .setTitle("Alert Title")
                        //    .setText("Alert text...")
                          //  .show();

                    new ToastOXDialog.Build(MainActivity.this)
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
                                }
                            }).show();
                }
            }
        }
        );

    }

    @OnClick({ R.id.item1, R.id.item2, R.id.item3, R.id.item4 }) public void onMenuItemClick(View view) {
        tapBarMenu.close();
        switch (view.getId()) {
            case R.id.item1:
            {
                Log.i(TAG, "Reminder option");
                startActivity(new Intent(MainActivity.this,ReminderActivity.class));
                break;
            }

            case R.id.item2:
                Log.i(TAG, "ISetttings Option");
                break;
            case R.id.item3:
                Log.i(TAG, "Help option");
                break;
            case R.id.item4:
            {
                Log.i(TAG, "Log out option");
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Log out?")
                        .setMessage("Are you sure you want to Log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteHandler db;
                                SessionManager session;
                                // SqLite database handler
                                db = new SQLiteHandler(getApplicationContext());
                                // session manager
                                session = new SessionManager(getApplicationContext());
                                session.setLogin(false);
                                db.deleteUsers();
                                // Launching the login activity
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //
                            }
                        })
                        .setCancelable(false)
                        .show();



                break;
            }
        }
    }
    private void insert_into() {


        String tag_string_req = "req_Homefragment_Sub";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Homefragemnt request Response: " + response.toString());
                t.setVisibility(View.VISIBLE);

                try {
                    JSONObject jObj = new JSONObject(response); //objectifying the json
                    boolean error = jObj.getBoolean("error");  //detecting if an error was sent in json

                    // Check for error node in json
                    if (!error) {
                        //Submitted Successfully

                        String errorMsg = jObj.getString("message"); //extracting the message
                        Log.d(TAG,"Message returned from server: " + errorMsg ); //logging the error message
                        SessionManager cur = new SessionManager(getApplication()); //setting the percentage in local preferences
                        final int progress = jObj.getInt("value");                //     "
                        cur.setPercentage(progress);                           //      "
                        String name=cur.getUsername();
                        if(progress==0) //
                        {
                            meter.setVisibility(GONE);
                            tv.setText("Hey " + name + " ! \nWhat are you waiting for?\nStart Filling the Slam book!");
                            tv.setVisibility(View.VISIBLE);
                            cv.setVisibility(View.VISIBLE);
                            tv.setTextSize(30);
                            tv.setPadding(0,20,0,0);
                        }
                        else
                        {
                         if(progress != 100)
                            {
                             tv.setText("\nNot yet Completed :( \nContinue Filling the Slambook");
                                Log.d(TAG,"Progresss was found to be non 100 or zero");
                            }
                            anim = ValueAnimator.ofFloat(meter.progress, progress/100f );
                            anim.setInterpolator(new AccelerateDecelerateInterpolator());
                            anim.setDuration(1000);
                            anim.setStartDelay(1000);
                            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                meter.setProgress((float) valueAnimator.getAnimatedValue());
                               if((float) valueAnimator.getAnimatedValue()== 1f)
                                    {
                                        meter.setVisibility(GONE);
                                        tv.setText("Congrats! You have finished the Slambook");
                                        tv.setPadding(0,40,0,0);
                                        tv.setVisibility(View.VISIBLE);
                                     }

                                if((float) valueAnimator.getAnimatedValue()== progress/100f && (float) valueAnimator.getAnimatedValue() != 1f)
                                    {
                                        tv.setVisibility(View.VISIBLE);
                                        cv.setVisibility(View.VISIBLE);
                                        tvPerc.setVisibility(View.VISIBLE);
                                    }
                            }

                        }); Log.d(TAG,"animator properties defined");
                            anim.start();
                            Log.d(TAG,"Animation started");

                        }

                    } else {
                        // Error in Submission

                        String errorMsg = jObj.getString("message"); //extracting the error
                        Log.d(TAG,"Message returned from server: " + errorMsg ); //logging the error message
                        Toast.makeText(getApplication(),"An Error occured and logged, try Again", Toast.LENGTH_LONG).show(); //displaying an error to the user
                    }
                } catch (JSONException e) {
                    // JSON data was not returned, because an error at php script/mysql

                    e.printStackTrace(); //logging error
                    Toast.makeText(getApplication(), "Internal error occured, try again later", Toast.LENGTH_LONG).show(); //displaying an error to the user
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "Volley Error: " + error.getMessage()); //error in android part logged
                new ToastOXDialog.Build(MainActivity.this)
                        .setTitle("Something's Wrong :(")
                        .setContent("Cannot Ping server. Might be a poor or unstable connection")
                        .setPositiveText("Wi-Fi")
                        //.setPositiveBackgroundColorResource(R.color.orange)
                        //.setPositiveTextColorResource(R.color.black)
                        .onPositive(new ToastOXDialog.ButtonCallback() {
                            @Override
                            public void onClick(@NonNull ToastOXDialog toastOXDialog) {
                                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                Log.i("Click","Yes");
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
                            }
                        }).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                SQLiteHandler db= new SQLiteHandler(getApplication());  //object of the sqlLite helper
                SessionManager cur = new SessionManager(getApplication());  //object of the session manager
                String uid=db.getUserID();  //getting the current userid from local db
                String uname=cur.getUsername(); //getting current user name from sessionmnager
                Map<String, String> params = new HashMap<String, String>();
                params.put("route", "30");               //   json POST paran add
                params.put("userid",uid);               //   "
                params.put("username", uname);          //    "
                params.put("need", "get");          //    "
                params.put("requirement", "progress");       //    "
                params.put("value", "null");
                return params;  //returning ready json
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}