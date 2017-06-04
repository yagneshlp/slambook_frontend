package com.yagneshlp.slambook.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;

import com.dd.processbutton.iml.ActionProcessButton;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Part20 extends Fragment {


    public Part20() {
        // Required empty public constructor
    }

    private static final String TAG = SlambookActivity.class.getSimpleName();
    ActionProcessButton button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_part20,
                container, false);
        final EditText Et1;

        Et1 = (EditText) view.findViewById(R.id.buck);

        button = (ActionProcessButton) view.findViewById(R.id.btn_signup);

        button.setMode(ActionProcessButton.Mode.ENDLESS);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        button.setProgress(1);
                        insert_into(Et1.getText().toString() );

                    }
                    else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        button.setProgress(1);
                        insert_into(Et1.getText().toString() );
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


    private void insert_into(final String buck) {
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
                        SlambookActivity.viewPager.setCurrentItem(SlambookActivity.viewPager.getCurrentItem()+1,true);
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
                params.put("route", "20");
                params.put("userid", uid);
                params.put("username", uname);
                params.put("bucket", buck);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}