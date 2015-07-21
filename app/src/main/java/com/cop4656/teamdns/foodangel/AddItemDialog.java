package com.cop4656.teamdns.foodangel;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Shawn on 7/16/2015.
 */
public class AddItemDialog extends Dialog {
    public int month = 0, day = 0, year = 0; //expiration date
    DateFormat df;
    Date date;
    ScannerActivity activity;

    AddItemDialog(Context context){
        super(context);
        setContentView(R.layout.add_item);
        TextView tv = (TextView)findViewById(R.id.date);
        df = new SimpleDateFormat("MM/dd/yyyy");
        if(date == null) date = new Date();
        tv.setText(df.format(date));

        tv = (TextView)findViewById(R.id.date_change);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {showDateDialog();}
        });
        tv = (TextView)findViewById(R.id.add_item_button_cancel);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv = (TextView)findViewById(R.id.add_item_button);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView barcode = (TextView) findViewById(R.id.barcode);
                EditText name = (EditText) findViewById(R.id.name);
                EditText quantity = (EditText) findViewById(R.id.quantity);
                storeData(barcode.getText().toString(), name.getText().toString(), quantity.getText().toString());
                dismiss();
            }
        });
    }
    AddItemDialog(Context context, ScannerActivity s){
        this(context);
        activity = s;
        TextView tv = (TextView)findViewById(R.id.barcode);
        tv.setText(activity.data.barcode);
        if(activity.data.name != null){
            //Barcode was found in database populate appropriate fields
            EditText et = (EditText)findViewById(R.id.name);
            et.setText(activity.data.name);
            tv = (TextView)findViewById(R.id.current_quantity);
            tv.setText(String.format("%d", activity.data.quantity));
            date = activity.data.date;//added to the UI in default constructor
        }
    }
    @Override
    protected void onStop(){
        activity.restartPreviewAfterDelay(0L);
        super.onStop();
    }
    private void showDateDialog(){
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setTextId(R.id.date);
        newFragment.setDialog(this);
        newFragment.show(ScannerActivity.fm, "datePicker");
    }
    private void storeData(String barcode, String name, String quantity){
        //store data to database
        Calendar selectedDate = Calendar.getInstance();
        if(activity == null) return;
        if(year != 0 && day != 0 && month != 0) //get date from date picker selection
            selectedDate.setTime(new Date(year, month, day));
        else if(activity.data.date != null)//get date from activity object if was found in database
            selectedDate.setTime(activity.data.date);

        //store form data in activity.data
        activity.data.barcode = barcode;
        activity.data.name = name;
        activity.data.date = selectedDate.getTime();
        activity.data.quantity = Integer.parseInt(quantity);
        if(activity.data.lastDaysTillExpiration == 0){
            //calculate days until food expires
            activity.data.lastDaysTillExpiration = daysBetween(Calendar.getInstance(), selectedDate);
        }
        //activity.data object now contains all data needed to store in database.
        Log.d("Scanner", "Storing data: " + activity.data.barcode + " " + activity.data.name + " "
                + activity.data.date + " " + activity.data.quantity);
    }
    //method below copied from http://stackoverflow.com/questions/6185966/converting-a-date-object-to-a-calendar-object
    public static int daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return (int) TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }
}
