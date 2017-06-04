package com.yagneshlp.slambook.fragment;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.widget.TextView;
import android.widget.DatePicker;
import android.app.Dialog;
import java.util.Calendar;

import com.yagneshlp.slambook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current date as the default date in the date picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //Create a new DatePickerDialog instance and return it
        /*
            DatePickerDialog Public Constructors - Here we uses first one
            public DatePickerDialog (Context context, DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth)
            public DatePickerDialog (Context context, int theme, DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth)
         */
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //Do something with the date chosen by the user
        //TextView tv = (TextView) getActivity().findViewById(R.id.Et3);
       // TextView tv1 = (TextView) getActivity().findViewById(R.id.editText5);
        //tv.setText("Date changed...");
        //tv.setText(tv.getText() + "\nYear: " + year);
        //tv.setText(tv.getText() + "\nMonth: " + month);
        //tv.setText(tv.getText() + "\nDay of Month: " + day);
        String string = day + "/" + (month+1) + "/" + year;
       // tv.setText(string);
        String stringOfDate;
        if(month>9)
            stringOfDate=  String.valueOf(year)+String.valueOf(month+1)+String.valueOf(day);
        else
            stringOfDate =  String.valueOf(year)+ "0" + String.valueOf(month+1)+String.valueOf(day);

       // tv1.setText(stringOfDate);
    }
}