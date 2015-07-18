package com.cop4656.teamdns.foodangel;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shawn on 7/16/2015.
 */
public class AddItemDialog extends Dialog {
    public int month = 0, day = 0, year = 0; //expiration date
    ScannerActivity activity;
    AddItemDialog(Context context){
        super(context);
        setContentView(R.layout.add_item);
        TextView tv = (TextView)findViewById(R.id.date);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
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
                quantity.getText();
                storeData(barcode.getText().toString(), name.getText().toString(), quantity.getText().toString());
            }
        });
    }
    AddItemDialog(Context context, ScannerActivity s){
        this(context);
        activity = s;
        if(activity.data != null){
            //Barcode was found in database populate appropriate fields
            EditText et = (EditText)findViewById(R.id.name);
            et.setText(activity.data.name);
            TextView tv2 = (TextView)findViewById(R.id.current_quantity);
            tv2.setText(String.format("%d", activity.data.quantity));
        }
    }
    private void showDateDialog(){
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setTextId(R.id.date);
        newFragment.setDialog(this);
        newFragment.show(ScannerActivity.fm, "datePicker");
    }
    private void storeData(String barcode, String name, String quantity){
        //store date to database
        if(activity == null) return;
        activity.data.date = new Date(year, month, day);
        Log.d("Scanner", "Storing data: " + activity.data.barcode + " " + activity.data.name + " "
            + activity.data.date + " " + activity.data.quantity);
    }

}
