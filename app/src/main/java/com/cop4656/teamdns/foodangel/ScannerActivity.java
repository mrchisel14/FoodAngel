package com.cop4656.teamdns.foodangel;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.result.ResultHandler;

import java.util.Calendar;
import java.util.Date;

import io.github.johncipponeri.outpanapi.OutpanAPI;
import io.github.johncipponeri.outpanapi.OutpanObject;


public class ScannerActivity extends CaptureActivity {
    static FragmentManager fm;
    public Util.ProductData data = new Util.ProductData();
    private final String outpan_api_key = "f70d071b91ee72dab3c524dc8abe5517";
    OutpanObject outpanData;
    boolean found = false;
    final int RQS_GooglePlayServices = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Scanner", "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        fm = getFragmentManager();
        /* Checks if device has Google Play Services activated */
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (resultCode == ConnectionResult.SUCCESS) {
            Toast.makeText(getApplicationContext(),
                    "isGooglePlayServicesAvailable SUCCESS",
                    Toast.LENGTH_LONG).show();
        } else
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices).show();
        /* --- */
    }
    @Override
    public void handleDecodeInternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
        //This is what happens after an item is scanned successfully
        Log.d("Scanner", "Results: " + rawResult.toString());
        data = retrieveData(rawResult.toString());
        //must be called after retrieveData
        AddItemDialog d = new AddItemDialog(this, this);
        d.setTitle(getResources().getString(R.string.add_item_title));
        d.show();
    }
    private Util.ProductData retrieveData(final String barcode){
        //search for barcode in database if not found return object only containing barcode
        Util.ProductData rdata = new Util.ProductData();
        rdata.barcode = barcode;
        //Search embedded database and fill rdata object if found
        if(found){
            //rdata.lastDaysTillExpiration = ?
            Calendar c = Calendar.getInstance();//used to calculate today's date + last expiration time
            c.add(Calendar.DATE, rdata.lastDaysTillExpiration);
            rdata.date = c.getTime();
            //rdata.name = ?
            //rdata.quantity = ?
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
                        outpanData = api.getProductName(barcode);
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
                rdata.date = new Date(); // sets the date to the current time
                found = true;
                Log.d("Scanner", "Found in Online Database");
            }
        }
        found = false;
        Log.d("Scanner", "Leaving Retrieve Data");
        return rdata;
    }
    public void SwitchActivity(View view) {
        Intent i = new Intent(this, PantryActivity.class);
        startActivity(i);
    }
}
