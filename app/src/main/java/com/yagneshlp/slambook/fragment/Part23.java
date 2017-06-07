package com.yagneshlp.slambook.fragment;


import com.dd.processbutton.iml.ActionProcessButton;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
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
import me.grantland.widget.AutofitTextView;

//Created by Yagnesh L P

public class Part23 extends Fragment {


    public Part23() {
        // Required empty public constructor
    }

    private static final String TAG = SlambookActivity.class.getSimpleName();


    String ch1,ch2,ch3,datebuff="";
    ActionProcessButton button;
    SwitchDateTimeDialogFragment dateTimeFragment;
    CheckBox cb1,cb2,cb3;
    AutofitTextView t1,t2;

    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_part23,
                container, false);

        cb1=(CheckBox) view.findViewById(R.id.chkbox1);
        cb2=(CheckBox) view.findViewById(R.id.chkbox2);
        cb3=(CheckBox) view.findViewById(R.id.chkbox3);
        t1=(AutofitTextView) view.findViewById(R.id.datepik);
        t2=(AutofitTextView) view.findViewById(R.id.datedisp);
        button = (ActionProcessButton) view.findViewById(R.id.btn_signup);

        button.setMode(ActionProcessButton.Mode.ENDLESS);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        if(cb1.isChecked())
                          ch1="Yes";
                        else
                            ch1="No";
                        if(cb2.isChecked())
                            ch2="Yes";
                        else
                            ch2="No";
                        if(cb3.isChecked())
                            ch3="Yes";
                        else
                            ch3="No";
                        button.setProgress(1);
                        checker();

                    }
                    else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        if(cb1.isChecked())
                            ch1="Yes";
                        else
                            ch1="No";
                        if(cb2.isChecked())
                            ch2="Yes";
                        else
                            ch2="No";
                        if(cb3.isChecked())
                            ch3="Yes";
                        else
                            ch3="No";
                        button.setProgress(1);
                        checker();
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

        dateTimeFragment = (SwitchDateTimeDialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if(dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(R.string.positive_button_datetime_picker),
                    getString(R.string.negative_button_datetime_picker)
            );
        }
        final SimpleDateFormat myDateFormat = new SimpleDateFormat("dd MMM yyyy ", java.util.Locale.getDefault());
        dateTimeFragment.startAtCalendarView();
        dateTimeFragment.set24HoursMode(false);
        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(2017, Calendar.MAY, 1).getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(2017, Calendar.JULY, 31).getTime());
        dateTimeFragment.setDefaultDateTime(new GregorianCalendar(2017, Calendar.MAY, 25, 15, 20).getTime());
        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd ", java.util.Locale.getDefault());
                t2.setText(myDateFormat.format(date));
                datebuff=df.format(date).toString();
                t2.setVisibility(View.VISIBLE);

            }

            @Override
            public void onNegativeButtonClick(Date date) {
                t2.setVisibility(View.GONE);
            }
        });
        t1.setOnClickListener( new View.OnClickListener()
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

    private void checker()
    {
        String tag_string_req = "req_page23_val";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Page 23 get Response: " + response.toString());

                try {
                    final JSONObject jObj = new JSONObject(response); //objectifying the json
                    boolean error = jObj.getBoolean("error");  //detecting if an error was sent in json
                    // Check for error node in json
                    if (!error) {
                        //Got status Successfully
                        String status=jObj.getString("value");
                        if(status.equals("Yes"))
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
                                            insert_into(ch1,ch2,ch3,datebuff.toString());
                                        }
                                    })
                                    .show();
                        }
                        else
                            insert_into(ch1,ch2,ch3,datebuff.toString());

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
                params.put("route", "23");               //   json POST paran add
                params.put("userid",uid);               //   "
                params.put("username", uname);          //    "
                params.put("need", "get");             //    "
                return params;  //returning ready json
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void insert_into(final String bfa, final String reply, final String pdf, final String doc) {
        // Tag used to cancel the request
        String tag_string_req = "req_page23_sub";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Page 23 submit  Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        //Submitted Successfully
                        button.setProgress(100); //the button is set to green colour(subitted)
                        // TODO: On successfull completion of slambook a popup must apperand must take user to MainActivity.
                        // SlambookActivity.viewPager.setCurrentItem(SlambookActivity.viewPager.getCurrentItem()+1,true); //the veiwpager is changed to next page
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
                params.put("route", "23");
                params.put("userid", uid);
                params.put("username", uname);
                params.put("bfa", bfa);
                params.put("reply", reply);
                params.put("pdf", pdf);
                params.put("doc", doc);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}