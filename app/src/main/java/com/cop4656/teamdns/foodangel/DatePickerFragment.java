package com.cop4656.teamdns.foodangel;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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
        Log.d("Scanner", "onCreateDialog()");
        final Calendar c = Calendar.getInstance();
        int year, month, day;
        if(d.data.expDate != null){ // Use the current expDate as the default expDate in the picker
            c.setTime(d.data.expDate);
        }
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the expDate chosen by the user
        Log.d("Scanner", "onDateSet");
        Calendar c = Calendar.getInstance();
        c.set(year + 1900, month, day);
        if(c.compareTo(Calendar.getInstance()) == -1){//c is before today's date
            Toast.makeText(getActivity(), "Cannot set expiration date to the past", Toast.LENGTH_SHORT).show();
            c = Calendar.getInstance();
        }
        else{
            Log.d("Scanner", "Valid Date");
        }
        d.data.expDate = c.getTime();
        if(textId != 0){
            TextView tv = (TextView) d.findViewById(textId);
            tv.setText((c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + +c.get(Calendar.YEAR));
        }
        dismiss();
    }
    public void setTextId(int id){this.textId = id;}
    public void setDialog(AddItemDialog d){this.d = d;}
}
