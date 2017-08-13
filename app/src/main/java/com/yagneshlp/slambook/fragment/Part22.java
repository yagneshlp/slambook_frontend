package com.yagneshlp.slambook.fragment;


import com.dd.processbutton.iml.ActionProcessButton;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.yagneshlp.slambook.R;
import com.yagneshlp.slambook.activity.SlambookActivity;
import com.yagneshlp.slambook.app.AppConfig;
import com.yagneshlp.slambook.app.AppController;
import com.yagneshlp.slambook.helper.SQLiteHandler;
import com.yagneshlp.slambook.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

import static com.yagneshlp.slambook.src.Config.auth;

/**
 * A simple {@link Fragment} subclass.
 */
public class Part22 extends Fragment {


    public Part22() {
        // Required empty public constructor
    }

    private static final String TAG = SlambookActivity.class.getSimpleName();
    ActionProcessButton button;
    String ans1,ans2,ans3,ans4,ans5,ans6,ans7,ans8,ans9,ans10,ans11,ans12,ans13;
    ToggleSwitch t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13;
    private AdView mAdView;
    TextView tvWarn;

    @Override
    public void onPause() {
        if (mAdView != null) {

            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_part22,
                container, false);


        button = (ActionProcessButton) view.findViewById(R.id.btn_signup);
        button.setMode(ActionProcessButton.Mode.ENDLESS);

        ans1=ans2=ans3=ans4=ans5=ans6=ans7=ans8=ans9=ans10=ans11=ans12=ans13="Yes";
        t1 = (ToggleSwitch) view.findViewById(R.id.q1);
        t2= (ToggleSwitch) view.findViewById(R.id.q2);
        t3 = (ToggleSwitch) view.findViewById(R.id.q3);
        t4 = (ToggleSwitch) view.findViewById(R.id.q4);
        t5 = (ToggleSwitch) view.findViewById(R.id.q5);
        t6 = (ToggleSwitch) view.findViewById(R.id.q6);
        t7 = (ToggleSwitch) view.findViewById(R.id.q7);
        t8 = (ToggleSwitch) view.findViewById(R.id.q8);
        t9 = (ToggleSwitch) view.findViewById(R.id.q9);
        t10 = (ToggleSwitch) view.findViewById(R.id.q10);
        t11 = (ToggleSwitch) view.findViewById(R.id.q11);
        t12 = (ToggleSwitch) view.findViewById(R.id.q12);
        t13 = (ToggleSwitch) view.findViewById(R.id.q13);
        tvWarn=(TextView) view.findViewById(R.id.warning);

        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("5AB42BEA113D6BA5C3DDC861AE5B9165")
                .build();
        mAdView.loadAd(adRequest);


        t1.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position==1)
                {
                   ans1="No";
                }
                else if (position==0)
                {
                    ans1="Yes";
                }
            }
        });
        t2.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position==1)
                {
                    ans2="No";
                }
                else if (position==0)
                {
                    ans2="Yes";
                }
            }
        });
        t3.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position==1)
                {
                    ans3="No";
                }
                else if (position==0)
                {
                    ans3="Yes";
                }
            }
        });
        t4.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position==1)
                {
                    ans4="No";
                }
                else if (position==0)
                {
                    ans4="Yes";
                }
            }
        });
        t5.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position==1)
                {
                    ans5="No";
                }
                else if (position==0)
                {
                    ans5="Yes";
                }
            }
        });
        t6.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position==1)
                {
                    ans6="No";
                }
                else if (position==0)
                {
                    ans6="Yes";
                }
            }
        });

        t7.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position==1)
                {
                    ans7="No";
                }
                else if (position==0)
                {
                    ans1="Yes";
                }
            }
        });
        t8.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position==1)
                {
                    ans8="No";
                }
                else if (position==0)
                {
                    ans8="Yes";
                }
            }
        });
        t9.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position==1)
                {
                    ans9="No";
                }
                else if (position==0)
                {
                    ans9="Yes";
                }
            }
        });
        t10.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position==1)
                {
                    ans10="No";
                }
                else if (position==0)
                {
                    ans10="Yes";
                }
            }
        });
        t11.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position==1)
                {
                    ans11="No";
                }
                else if (position==0)
                {
                    ans11="Yes";
                }
            }
        });
        t12.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position==1)
                {
                    ans12="No";
                }
                else if (position==0)
                {
                    ans12="Yes";
                }
            }
        });
        t13.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                if(position==1)
                {
                    ans13="No";
                }
                else if (position==0)
                {
                    ans13="Yes";
                }
            }
        });

        checker(1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        button.setProgress(1);
                        checker(2);

                    }
                    else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        button.setProgress(1);
                        checker(2);
                    }
                } else {
                    Snackbar.make(view, "Check Your Internet Connection ", Snackbar.LENGTH_LONG)
                            .setAction("WIfi", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                        }
                                    }
                            ).show();
                }



            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void checker(final int choice)
    {
        String tag_string_req = "req_page22_val";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Page 22 get Response: " + response.toString());

                try {
                    final JSONObject jObj = new JSONObject(response); //objectifying the json
                    boolean error = jObj.getBoolean("error");  //detecting if an error was sent in json
                    // Check for error node in json
                    if (!error) {
                        //Got status Successfully
                        String status=jObj.getString("value");
                        if(status.equals("Yes"))
                        {
                            if(choice==1)
                            {
                                tvWarn.setVisibility(View.VISIBLE);
                            }
                            if(choice == 2)
                            {

                            new AlertDialog.Builder(getContext())
                                    .setTitle("Update the Data?")
                                    .setMessage("This page has already been filled.\nDo you want to update it with current data or retain previous data?")
                                    .setPositiveButton("Retain old info", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            button.setProgress(100); //the button is set to green colour(subitted)
                                            SlambookActivity.viewPager.setCurrentItem(SlambookActivity.viewPager.getCurrentItem()+1,true); //the veiwpager is changed to next page
                                            Log.d(TAG,"User decided to retain old value" ); //logging the error message
                                        }
                                    })
                                    .setNegativeButton("Update with new data", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            insert_into(ans1,ans2,ans3,ans4,ans5,ans6,ans7,ans8,ans9,ans10,ans11,ans12,ans13);
                                        }
                                    })
                                    .show();}
                        }
                        else
                        if(choice==2)
                            insert_into(ans1,ans2,ans3,ans4,ans5,ans6,ans7,ans8,ans9,ans10,ans11,ans12,ans13);

                    } else {
                        // Error in Submission
                        button.setProgress(-1); //button is set at error colour - red
                        String errorMsg = jObj.getString("message"); //extracting the error
                        Log.d(TAG,"Message returned from server: " + errorMsg ); //logging the error message
                        Toast.makeText(getContext(),"An Error occured and logged, try Again", Toast.LENGTH_LONG).show(); //displaying an error to the user
                    }
                } catch (JSONException e) {
                    // JSON data was not returned, because an error at php script/mysql
                    button.setProgress(-1);
                    e.printStackTrace(); //logging error
                    Toast.makeText(getContext(), "Internal error occured, try again later", Toast.LENGTH_LONG).show(); //displaying an error to the user
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                button.setProgress(-1);
                Log.e(TAG, "Volley Error: " + error.getMessage()); //error in android part logged
                Toast.makeText(getContext(),"Local error, try again", Toast.LENGTH_LONG).show(); //displaying the error to user
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                SQLiteHandler db= new SQLiteHandler(getContext());  //object of the sqlLite helper
                SessionManager cur = new SessionManager(getContext());  //object of the session manager
                String uid=db.getUserID();  //getting the current userid from local db
                String uname=cur.getUsername(); //getting current user name from sessionmnager
                Map<String, String> params = new HashMap<String, String>();
                params.put("route", "22");               //   json POST paran add
                params.put("userid",uid);               //   "
                params.put("username", uname);          //    "
                params.put("need", "get");             //    "
                return params;  //returning ready json
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void insert_into(final String ans1,final String ans2,final String ans3,final String ans4,final String ans5,final String ans6,final String ans7,final String ans8,final String ans9,final String ans10,final String ans11,final String ans12,final String ans13) {
        // Tag used to cancel the request
        String tag_string_req = "req_Page22_sub";

        //pDialog.setMessage("Submitting");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Page 22 Submit Response: " + response.toString());
                //hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        //Submitted Successfully
                        button.setProgress(100); //the button is set to green colour(subitted)
                        SlambookActivity.viewPager.setCurrentItem(SlambookActivity.viewPager.getCurrentItem()+1,true); //the veiwpager is changed to next page
                        String errorMsg = jObj.getString("message"); //extracting the message
                        Log.d(TAG,"Message returned from server: " + errorMsg ); //logging the error message
                        SessionManager cur = new SessionManager(getContext()); //setting the percentage in local preferences
                        int progress = jObj.getInt("progress");                //     "
                        cur.setPercentage(progress);                           //      "

                    } else {
                        // Error in Submission
                        button.setProgress(-1); //button is set at error colour - red
                        String errorMsg = jObj.getString("message"); //extracting the error
                        Log.d(TAG,"Message returned from server: " + errorMsg ); //logging the error message
                        Toast.makeText(getContext(),"An Error occured and logged, try Again", Toast.LENGTH_LONG).show(); //displaying an error to the user
                    }
                } catch (JSONException e) {
                    // JSON data was not returned, because an error at php script/mysql
                    button.setProgress(-1);
                    e.printStackTrace(); //logging error
                    Toast.makeText(getContext(), "Internal error occured, try again later", Toast.LENGTH_LONG).show(); //displaying an error to the user
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                button.setProgress(-1);
                Log.e(TAG, "Volley Error: " + error.getMessage()); //error in android part logged
                Toast.makeText(getContext(),"Local error, try again", Toast.LENGTH_LONG).show(); //displaying the error to user
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                SQLiteHandler db= new SQLiteHandler(getContext());
                SessionManager cur = new SessionManager(getContext());
                String uid =db.getUserID();
                String uname=cur.getUsername();
                Map<String, String> params = new HashMap<String, String>();
                params.put("route", "22");
                params.put("userid", uid);
                params.put("username", uname);
                params.put("q1", ans1);
                params.put("q2", ans2);
                params.put("q3", ans3);
                params.put("q4", ans4);
                params.put("q5", ans5);
                params.put("q6", ans6);
                params.put("q7", ans7);
                params.put("q8", ans8);
                params.put("q9", ans9);
                params.put("q10", ans10);
                params.put("q11", ans11);
                params.put("q12", ans12);
                params.put("q13", ans13);
                return params;
            }


        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}