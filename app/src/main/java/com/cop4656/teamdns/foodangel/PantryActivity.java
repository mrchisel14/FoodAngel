package com.cop4656.teamdns.foodangel;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class PantryActivity extends Activity {

    DatabaseControl dbControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        dbControl = new DatabaseControl(this, "FA", null, 7);

        dbControl.insertNewProduct("0000000", "Test Food", new Date(), 1);
        dbControl.insertNewProduct("0000000", "Test Food", new Date(), 1);
        dbControl.insertNewProduct("0000000", "Test Food", new Date(), 1);
        dbControl.insertNewProduct("0000000", "Test Food", new Date(), 1);
        dbControl.insertNewProduct("0000000", "Test Food", new Date(), 1);
        dbControl.insertNewProduct("0000000", "Test Food", new Date(), 1);
        dbControl.insertNewProduct("0000000", "Test Food", new Date(), 1);

        Cursor c = dbControl.selectProducts();

        if (c.moveToFirst()) {
            do {
                // Get FragmentManager to handle creation of a new pantry item
                FragmentManager manager = getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();

                // Add pantry item fragment to activity
                PantryItemFragment pantry = new PantryItemFragment();
                trans.add(R.id.linearLayout, pantry, "Pantry Item");
                trans.commit();

                // Grab the necessary fields from this row
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(c.getLong(3));
                pantry.setFields(c.getInt(0), c.getString(2), cal, c.getInt(4));
            } while (c.moveToNext());
        } else {
            findViewById(R.id.noItemsMessage).setVisibility(View.VISIBLE);
        }

        c.close();
        dbControl.close();

        this.findViewById(R.id.returnButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ScannerActivity.class);
                startActivity(i);
            }
        });
    }
}
