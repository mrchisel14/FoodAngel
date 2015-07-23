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

import io.github.johncipponeri.outpanapi.OutpanAPI;
import io.github.johncipponeri.outpanapi.OutpanObject;

/**
 * Created by Shawn on 7/16/2015.
 */
public class AddItemDialog extends Dialog {

    DateFormat df;
    public Util.ProductData data;
    ScannerActivity activity;
    private final String outpan_api_key = "f70d071b91ee72dab3c524dc8abe5517";
    OutpanObject outpanData;
    boolean found = false;

    AddItemDialog(Context context, ScannerActivity s, String bcode){
        super(context);
        setContentView(R.layout.add_item);
        activity = s;
        data = retrieveData(bcode);//searches for barcode in databases
        TextView tv;
        if(data == null){
            data = new Util.ProductData();
            data.barcode = bcode;
        }
        if(data.name != null){
            //Barcode was found in database populate appropriate fields
            EditText et = (EditText)findViewById(R.id.name);
            et.setText(data.name);
            tv = (TextView)findViewById(R.id.current_quantity);
            tv.setText(String.format("%d", data.quantity));
        }
        tv = (TextView)findViewById(R.id.date);
        df = new SimpleDateFormat("MM/dd/yyyy");
        if(data.expDate == null) data.expDate = new Date();
        tv.setText(df.format(data.expDate));
        tv = (TextView)findViewById(R.id.barcode);
        tv.setText(data.barcode);
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
                TextView barcodeView = (TextView) findViewById(R.id.barcode);
                EditText name = (EditText) findViewById(R.id.name);
                EditText quantity = (EditText) findViewById(R.id.quantity);
                storeData(barcodeView.getText().toString(), name.getText().toString(), quantity.getText().toString());
                dismiss();
            }
        });
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
        Date selectedDate;
        if(data.expDate != null)//get expDate if was found in database
            selectedDate = data.expDate;
        else selectedDate = new Date();

        //store form data in data
        data.barcode = barcode;
        data.name = name;
        if(data.expDate == null) data.expDate = selectedDate;
        data.quantity = Integer.parseInt(quantity);
        //data object now contains all data needed to store in database.
        Log.d("Scanner", "Storing data: " + data.barcode + " " + data.name + " "
                + data.expDate + " " + data.quantity);
    }
    //method below copied from http://stackoverflow.com/questions/6185966/converting-a-date-object-to-a-calendar-object
    public static int daysBetween(Date startDate, Date endDate) {
        long end = endDate.getTime();
        long start = startDate.getTime();
        return (int) TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }
    private Util.ProductData retrieveData(final String bcode){
        //search for barcode in database if not found return object only containing barcode
        Util.ProductData rdata;
        DatabaseControl dbControl = new DatabaseControl(activity, "FA", null, 7);
        //Search embedded database and fill rdata object if found
        rdata = dbControl.retrieveProduct(bcode);
        if(rdata != null ) found = true;
        else{
            rdata = new Util.ProductData();
            rdata.barcode = bcode;
        }
        if(found){
            Log.d("Scanner", "Found in Embedded Database");
            //int daysTillExpiration = AddItemDialog.daysBetween(rdata.entryDate, rdata.expDate);
            Calendar c = Calendar.getInstance();//used to calculate today's expDate + last expiration time
            //c.add(Calendar.DATE, daysTillExpiration);
            rdata.expDate = c.getTime();
        }
        //Search online database
        if(!found){
            Log.d("Scanner", "Not found in embedded database trying online");
            final OutpanAPI api = new OutpanAPI(outpan_api_key);
            Thread searchThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("Scanner", "Searching online database");
                    try{
                        outpanData = api.getProductName(bcode);
                        if(outpanData.name == null){
                            found = false;
                        } else found = true;
                    }catch(Exception e){
                        e.printStackTrace();
                        found = false;
                    }
                }
            });
            searchThread.start();
            try{
                searchThread.join();
            }catch (InterruptedException e){
                Log.e("Scanner", e.getMessage());
                e.printStackTrace();
            }
            if(found){
                rdata.name = outpanData.name;
                rdata.quantity = 0;
                rdata.expDate = new Date(); // sets the expDate to the current time
                found = true;
                Log.d("Scanner", "Found in Online Database");
            }
        }
        found = false;
        Log.d("Scanner", "Leaving Retrieve Data");
        return rdata;
    }
}
