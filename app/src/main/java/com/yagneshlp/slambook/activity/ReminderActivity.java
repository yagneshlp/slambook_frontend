package com.yagneshlp.slambook.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.nightonke.boommenu.Util;
import com.onurciner.toastox.ToastOXDialog;
import com.yagneshlp.slambook.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import me.grantland.widget.AutofitTextView;

import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.LONG;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;


/**
 * Created by Yagnesh L P on 11-06-2017.
 */

public class ReminderActivity extends FragmentActivity {



    ActionProcessButton buttonstartSetDialog;
    AutofitTextView tv;
    Calendar cal;
    boolean isSet = false;

    SwitchDateTimeDialogFragment dateTimeFragment;   //for Datepicker

    String tom="";

    final static int RQS_1 = 1;
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    private static final String TAG = ReminderActivity.class.getSimpleName(); //for Logger Purposes

    private BoomMenuButton bmb;
    private static int[] imageResources = new int[]{
            R.drawable.ic_30,
            R.drawable.ic_1,
            R.drawable.ic_2,
            R.drawable.ic_6,
            R.drawable.ic_tom,


    };
    private static int[] subStringResources = new int[] {

            R.string.fab1,
            R.string.fab2,
            R.string.fab3,
            R.string.fab4,
            R.string.fab5,



    };

    private static int[] stringResources = new int[] {

            R.string.ham1,
            R.string.ham2,
            R.string.ham3,
            R.string.ham4,
            R.string.ham5,


    };

    private static int[] colorResources = new int[] {

            R.color.fb_blue,
            R.color.tw_blu,
            R.color.ig_yell,
            R.color.gp_red,
            R.color.yt_red,


    };

    private static int imageResourceIndex = 0;
    private static int subStringResourceIndex = 0;
    private static int stringResourceIndex = 0;
    private static int colorResourceIndex = 0;

    static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }

    static int getSubStringResource() {
        if (subStringResourceIndex >= stringResources.length) subStringResourceIndex = 0;
        return subStringResources[subStringResourceIndex++];
    }

    static int getStringResource() {
        if (stringResourceIndex >= stringResources.length) stringResourceIndex = 0;
        return stringResources[stringResourceIndex++];
    }

    static int getColorResource() {
        if (colorResourceIndex >= colorResources.length) colorResourceIndex = 0;
        return colorResources[colorResourceIndex++];
    }


    HamButton.Builder getHamButtonBuilder() {
        return new HamButton.Builder()
                .normalImageRes(getImageResource())
                .normalTextRes(getStringResource())
                .subNormalTextRes(getSubStringResource())
                .normalColorRes(getColorResource())
                .buttonWidth(Util.dp2px(250))
                .imagePadding(new Rect(40, 40, 40, 40))
                .rippleEffect(true)
                .subTextGravity(Gravity.CENTER)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        switch (index) {
                            case 0:
                                cal = Calendar.getInstance();
                                Log.d(TAG,"30 mins set");
                                cal.add(MINUTE,30 );
                                tom="";
                                isSet=true;
                                break;
                            case 1:
                                cal = Calendar.getInstance();
                                cal.add(HOUR, 1);
                                tom="";
                                isSet=true;
                                break;
                            case 2:
                                cal = Calendar.getInstance();
                                cal.add(HOUR, 2);
                                tom="";
                                isSet=true;
                                break;
                            case 3:
                                cal = Calendar.getInstance();
                                cal.add(HOUR, 6);
                                tom="";
                                isSet=true;
                                break;
                            case 4:
                                cal = Calendar.getInstance();
                                cal.add(DATE, 1);
                                tom=" Tomorrow";
                                isSet=true;
                                break;



                        }
                    }
                });
    }

    @Override
    public void onBackPressed()
    {
        if(!bmb.isBoomed())
        {
            finish();
            startActivity(new Intent(ReminderActivity.this,MainActivity.class));
            overridePendingTransition(R.anim.slide_right,R.anim.no_change);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        tv = (AutofitTextView) findViewById(R.id.hint);

        buttonstartSetDialog = (ActionProcessButton) findViewById(R.id.btnset);
        buttonstartSetDialog.setEnabled(false);

        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_5);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_5);
        bmb.clearBuilders();
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++)
            bmb.addBuilder(getHamButtonBuilder());
        bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                // If you have implement listeners for boom-buttons in builders,
                // then you shouldn't add any listener here for duplicate callbacks.
            }

            @Override
            public void onBackgroundClick() {

            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {
                if (isSet) {
                    String amPM = "AM";
                    if ((cal.get(Calendar.AM_PM)) == 1) {
                        amPM = "PM";
                    }
                    String hour;
                    if ((cal.get(Calendar.HOUR_OF_DAY) > 12))
                        hour = "" + ((cal.get(Calendar.HOUR_OF_DAY)) - 12);
                    else
                        hour = "" + (cal.get(Calendar.HOUR_OF_DAY));


                    tv.setText("Reminder has been Scheduled at: "+ hour + ":" + cal.get(MINUTE) + amPM + tom  );
                    tv.setTextSize(30);
                    tv.setVisibility(View.VISIBLE);
                    buttonstartSetDialog.setEnabled(true);

                }
            }

            @Override
            public void onBoomWillShow() {
                tv.setVisibility(View.INVISIBLE);
                buttonstartSetDialog.setEnabled(false);

            }

            @Override
            public void onBoomDidShow() {

            }
        });


        buttonstartSetDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
             setAlarm(cal);

            }
        });


    }
    private void setAlarm(Calendar targetCal) {

        //textAlarmPrompt.setText("\n\n***\n" + "Alarm is set "
             //   + targetCal.getTime() + "\n" + "***\n");
        Toast.makeText(this,targetCal.toString(),Toast.LENGTH_LONG);

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);
        Toast.makeText(ReminderActivity.this,"Reminder Set!",Toast.LENGTH_LONG);
        finish();
        startActivity(new Intent(ReminderActivity.this,MainActivity.class));

    }
}