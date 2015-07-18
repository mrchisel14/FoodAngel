package com.cop4656.teamdns.foodangel;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Copied from http://developer.android.com/guide/topics/ui/controls/pickers.html
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    int textId = 0;
    AddItemDialog d;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        month += 1;
        d.month = month;
        d.year = year;
        d.day = day;
        if(textId != 0){
            TextView tv = (TextView) d.findViewById(textId);
            tv.setText(month + "/" + day + "/" + year );
        }
        dismiss();
    }
    public void setTextId(int id){this.textId = id;}
    public void setDialog(AddItemDialog d){this.d = d;}
}
