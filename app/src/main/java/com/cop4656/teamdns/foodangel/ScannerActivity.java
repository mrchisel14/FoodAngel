package com.cop4656.teamdns.foodangel;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        //must be called after retrieveData
        AddItemDialog d = new AddItemDialog(this, this, rawResult.toString());
        d.setTitle(getResources().getString(R.string.add_item_title));
        d.show();
    }
    public void SwitchActivity(View view) {
        Intent i = new Intent(this, PantryActivity.class);
        startActivity(i);
    }
}
