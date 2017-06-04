package com.yagneshlp.slambook.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;

import com.dd.processbutton.iml.ActionProcessButton;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.yagneshlp.slambook.fragment.DatePickerFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.app.DialogFragment;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yagneshlp.slambook.R;
import com.yagneshlp.slambook.activity.RegisterActivity;
import com.yagneshlp.slambook.activity.SlambookActivity;
import com.yagneshlp.slambook.app.AppConfig;
import com.yagneshlp.slambook.app.AppController;
import com.yagneshlp.slambook.helper.SQLiteHandler;
import com.yagneshlp.slambook.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.grantland.widget.AutofitTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Part23 extends Fragment {


    public Part23() {
        // Required empty public constructor
    }

    private static final String TAG = SlambookActivity.class.getSimpleName();
    private String date;
    String ch1,ch2,ch3,datebuff="";

    ActionProcessButton button;
    SwitchDateTimeDialogFragment dateTimeFragment;

    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_part23,
                container, false);
        final CheckBox cb1,cb2,cb3;

        cb1=(CheckBox) view.findViewById(R.id.chkbox1);
        cb2=(CheckBox) view.findViewById(R.id.chkbox2);
        cb3=(CheckBox) view.findViewById(R.id.chkbox3);
        final AutofitTextView t1,t2;
        t1=(AutofitTextView) view.findViewById(R.id.datepik);
        t2=(AutofitTextView) view.findViewById(R.id.datedisp);

        DatePicker datepicker;


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
                        insert_into(ch1,ch2,ch3,datebuff.toString());

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
                        insert_into(ch1,ch2,ch3,datebuff);
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
        // Or assign each element, default element is the current moment
        // dateTimeFragment.setDefaultHourOfDay(15);
        // dateTimeFragment.setDefaultMinute(20);
        // dateTimeFragment.setDefaultDay(4);
        // dateTimeFragment.setDefaultMonth(Calendar.MARCH);
        // dateTimeFragment.setDefaultYear(2017);

        // Define new day and month format
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


    private void insert_into(final String bfa, final String reply, final String pdf, final String doc) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        //pDialog.setMessage("Submitting");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                //hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        //SlambookActivity.viewPager.setCurrentItem(SlambookActivity.viewPager.getCurrentItem()+1,true);
                        String errorMsg = jObj.getString("message");
                        button.setProgress(100);
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        button.setProgress(-1);
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
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