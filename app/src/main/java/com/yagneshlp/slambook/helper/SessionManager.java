package com.yagneshlp.slambook.helper;

/**
 * Created by yagne on 30-01-2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref1,pref2,pref3,pref4, pref;

    Editor editor1,editor2,editor3,editor4,editor ;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME_LOGIN = "YLP_LoginStatus";
    private static final String PREF_NAME_FIRST = "YLP_FIrstTImeLogin";
    private static final String PREF_NAME_UNAME = "YLP_UserName";
    private static final String PREF_NAME_PERCENT = "YLP_PerctageFilled";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_FIRSTTIME = "isFirstTime";
    private static final String KEY_USER_NAME = "UserName";
    private static final String KEY_PERCENT = "PercentageCompleted";

    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public SessionManager(Context context) {
        this._context = context;
        pref1 = _context.getSharedPreferences(PREF_NAME_LOGIN, PRIVATE_MODE);
        pref2 = _context.getSharedPreferences(PREF_NAME_FIRST,PRIVATE_MODE);
        pref3 =  _context.getSharedPreferences(PREF_NAME_UNAME,PRIVATE_MODE);
        pref4 = _context.getSharedPreferences(PREF_NAME_PERCENT,PRIVATE_MODE);
        editor1 = pref1.edit();
        editor2 = pref2.edit();
        editor3 = pref3.edit();
        editor4 = pref4.edit();

        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor1.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor1.commit();
        Log.d(TAG, "User login session modified!");
    }

    public void setFirstTime(boolean isFirstTime) {
        editor2.putBoolean(KEY_IS_FIRSTTIME, isFirstTime);
        // commit changes
        editor2.commit();
        Log.d(TAG, "First TIme set");
    }



    public void setUserName(String uname ) {
        editor3.putString(KEY_USER_NAME, uname);
        // commit changes
        editor3.commit();
        Log.d(TAG, "Username set!");
    }
    public void setPercentage(float percent) {
        editor4.putFloat(KEY_PERCENT,percent);
        // commit changes
        editor4.commit();
        Log.d(TAG, "Percentage completed Modified");
    }

    public boolean isLoggedIn(){

        return pref1.getBoolean(KEY_IS_LOGGEDIN, false);
    }
    public boolean isFirstTime(){

        return pref2.getBoolean(KEY_IS_FIRSTTIME, true);
    }
    public String getUsername(){

        return pref3.getString(KEY_USER_NAME,"");
    }
    public Float getPercentage(){

        return pref4.getFloat(KEY_PERCENT,0);    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
