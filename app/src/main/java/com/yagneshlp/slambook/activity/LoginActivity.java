package com.yagneshlp.slambook.activity;

/**
 * Created by yagne on 30-01-2017.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.yagneshlp.slambook.R;
import com.yagneshlp.slambook.app.AnalyticsTrackers;
import com.yagneshlp.slambook.app.AppConfig;
import com.yagneshlp.slambook.app.AppController;
import com.yagneshlp.slambook.helper.SQLiteHandler;
import com.yagneshlp.slambook.helper.SessionManager;
import com.yagneshlp.slambook.other.LogUtil;
import com.yagneshlp.slambook.other.StereoView;

public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private EditText etUsername;
    private EditText etPassword;
    private StereoView stereoView;
    private int translateY;
    Intent intentproceed;


    @Override
    public void onBackPressed()
    {
        finishAffinity();
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        stereoView = (StereoView) findViewById(R.id.stereoView);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);

        startEnterAnim();



        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }



        // Login button Click Event

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

                    String email = etUsername.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    // Check for empty data in the form
                    if (!email.isEmpty() && !password.isEmpty()) {
                        // login user
                        checkLogin(email, password);
                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                "Please enter the credentials!", Toast.LENGTH_LONG)
                                .show();
                        startEnterAnim();
                        stereoView.setItem(1);
                        etUsername.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });
        //stereoView.setStartScreen(1);
        stereoView.setResistance(2f);
        stereoView.setCan3D(true);
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
                LogUtil.m("Previous screen moved " + curScreen);
            }

            @Override
            public void toNext(int curScreen) {
                LogUtil.m("next screen moved" + curScreen);
            }
        });
        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }



    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");
                        String id = jObj.getString("id");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(id, name, email, uid, created_at);
                        session.setUserName(name);
                        // Launch main activity
                        intentproceed = new Intent(LoginActivity.this,
                                MainActivity.class);
                        intentproceed.putExtra("displayAlerter", true);
                        startExitAnim();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void startExitAnim() {
        stereoView.animate()
                .alpha(0f)
                .translationY(-100f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        startActivity(intentproceed);
                        finish();
                    }
                })
                .start();

    }

    private void startEnterAnim() {
        stereoView.setAlpha(0f);
        stereoView.animate()
                .alpha(1f)
               // .setDuration(800L)
                .translationY(-150f)
                .start();

        etUsername.requestFocus();

    }

    public void goNXT(View view) {
        stereoView.toNext();
        etPassword.requestFocus();
    }

    public void login(View view) {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Check for empty data in the form
        if (!email.isEmpty() && !password.isEmpty()) {
            // login user
            checkLogin(email, password);
        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(),
                    "Please enter the credentials!", Toast.LENGTH_LONG)
                    .show();
            startEnterAnim();
            stereoView.setItem(1);
            etUsername.requestFocus();
        }
    }
}