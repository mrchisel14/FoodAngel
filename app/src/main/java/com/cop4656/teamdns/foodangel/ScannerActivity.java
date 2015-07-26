package com.cop4656.teamdns.foodangel;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.result.ResultHandler;


public class ScannerActivity extends CaptureActivity {
    static FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Scanner", "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        fm = getFragmentManager();
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
