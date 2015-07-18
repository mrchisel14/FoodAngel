package com.cop4656.teamdns.foodangel;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.result.ResultHandler;


public class ScannerActivity extends CaptureActivity {
    static FragmentManager fm;
    ImageButton pantryButton;
    public Util.ProductData data = new Util.ProductData();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Scanner", "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        pantryButton = (ImageButton)findViewById(R.id.Pantry_Button);
        fm = getFragmentManager();
        pantryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Scanner", "Going to Pantry Activity");
            }
        });
    }
    @Override
    public void handleDecodeInternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
        //This is what happens after an item is scanned successfully
        Log.d("Scanner", "Results: " + rawResult.toString());
        data = retrieveData(rawResult.toString());
        AddItemDialog d = new AddItemDialog(this, this);
        d.setTitle(getResources().getString(R.string.add_item_title));
        d.show();
    }
    private Util.ProductData retrieveData(String barcode){
        //search for barcode in database
        Util.ProductData data = new Util.ProductData();

        return data;
    }
}
