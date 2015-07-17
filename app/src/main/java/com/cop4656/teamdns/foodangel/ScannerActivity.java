package com.cop4656.teamdns.foodangel;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.result.ResultHandler;


public class ScannerActivity extends CaptureActivity {
    FragmentManager fm;
    RetainedFragment fragment;
    ImageButton pantryButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Scanner", "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        pantryButton = (ImageButton)findViewById(R.id.Pantry_Button);
        fm = getFragmentManager();
        fragment = (RetainedFragment)fm.findFragmentByTag("retained");
        if(fragment == null){
            fragment = new RetainedFragment();
            fm.beginTransaction().add(fragment, "retained").commit();
        }
        pantryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Scanner", "Going to Pantry Activity");
            }
        });
    }
    public static class RetainedFragment extends Fragment {
        private boolean justOpened = true;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }
        @Override
        public void onDestroy(){
            super.onDestroy();
        }
        public void setJustOpened(boolean value){
            this.justOpened = value;
        }
        public boolean getJustOpened(){
            return this.justOpened;
        }
    }
    @Override
    public void handleDecodeInternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
        //This is what happens after an item is scanned successfully
        Log.d("Scanner", "Results: " + rawResult.toString());

        AddItemDialog d = new AddItemDialog(this);
        d.setTitle(getResources().getString(R.string.add_item_title));
        d.show();
    }

}
