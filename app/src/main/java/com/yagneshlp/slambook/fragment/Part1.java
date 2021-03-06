package com.yagneshlp.slambook.fragment;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yagneshlp.slambook.R;
import com.yagneshlp.slambook.activity.SlambookActivity;
import com.yagneshlp.slambook.app.AppConfig;
import com.yagneshlp.slambook.app.AppController;
import com.yagneshlp.slambook.helper.SQLiteHandler;
import com.yagneshlp.slambook.helper.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//Created by Yagnesh L P

public class Part1 extends Fragment {

    public Part1() {
        // Required empty public constructor
    }

    private static final String TAG = SlambookActivity.class.getSimpleName(); //for Logger Purposes

    String buff = "";    //contains the formated date for the MySQL table at server
    SwitchDateTimeDialogFragment dateTimeFragment;   //for Datepicker
    ActionProcessButton button;    //submit buttom
    EditText Et1, Et2;
    TextView tv;
    TextView tvWarn;
    private AdView mAdView;

    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";

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
        View view = inflater.inflate(R.layout.fragment_part1,
                container, false);

        Et1 = (EditText) view.findViewById(R.id.Et1);
        Et2 = (EditText) view.findViewById(R.id.Et2);
        tv=(TextView) view.findViewById(R.id.editText5);
        tvWarn=(TextView) view.findViewById(R.id.warning);
        button = (ActionProcessButton) view.findViewById(R.id.btn_signup);
        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("5AB42BEA113D6BA5C3DDC861AE5B9165")
                .build();
        mAdView.loadAd(adRequest);
        checker(1);
        button.setMode(ActionProcessButton.Mode.ENDLESS);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Et1.getText().toString().length()!=0 && Et2.getText().toString().length()!=0 )
                {
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
                }}
                else
                    Toast.makeText(getContext(),"Fill all the fields!",Toast.LENGTH_LONG);

            }
        });

        dateTimeFragment = (SwitchDateTimeDialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if(dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(R.string.positive_button_datetime_picker),
                    getString(R.string.negative_button_datetime_picker)
            );
        }
        final SimpleDateFormat myDateFormat = new SimpleDateFormat("dd MMM yyyy ", java.util.Locale.getDefault());
        dateTimeFragment.startAtYearView();
        dateTimeFragment.set24HoursMode(false);
        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(1997, Calendar.JANUARY, 1).getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(2001, Calendar.DECEMBER, 31).getTime());
        dateTimeFragment.setDefaultDateTime(new GregorianCalendar(2000, Calendar.MARCH, 26, 15, 20).getTime());
        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }
        // Set listener for date
       dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ", java.util.Locale.getDefault());
                tv.setText("Your Birthday? - " + myDateFormat.format(date));
               buff=df.format(date).toString();

            }
            @Override
            public void onNegativeButtonClick(Date date) {
                tv.setText("Your Birthday?");
            }
        });

        tv.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dateTimeFragment.show(getActivity().getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void checker(final int choice)
    {
        String tag_string_req = "req_page1_val";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Page 1 get Response: " + response.toString());

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
                            { new AlertDialog.Builder(getContext())
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
                                            insert_into(Et1.getText().toString(), Et2.getText().toString(),buff);
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();}
                        }
                        else
                        if(choice==2)
                            insert_into(Et1.getText().toString(), Et2.getText().toString(),buff);

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
                params.put("route", "1");               //   json POST paran add
                params.put("userid",uid);               //   "
                params.put("username", uname);          //    "
                params.put("need", "get");             //    "
                return params;  //returning ready json
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void insert_into(final String name, final String nickname, final String dob) {

        String tag_string_req = "req_page1_Sub";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Page 1 Submit Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response); //objectifying the json
                    boolean error = jObj.getBoolean("error");  //detecting if an error was sent in json
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
                params.put("route", "1");               //   json POST paran add
                params.put("userid",uid);               //   "
                params.put("username", uname);          //    "
                params.put("full_name", name);          //    "
                params.put("nickname", nickname);       //    "
                params.put("dob", dob);                 //     "
                return params;  //returning ready json
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}