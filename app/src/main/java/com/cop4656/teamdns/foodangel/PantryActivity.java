package com.cop4656.teamdns.foodangel;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Date;

public class PantryActivity extends Activity {

    DatabaseControl dbControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        dbControl = new DatabaseControl(this, "FA", null, 3);

        // dbControl.insertNewProduct("0000000", "Test Food", new Date(2015, 5, 17), 1);
        // dbControl.insertNewProduct("0000000", "Test Food", new Date(2015, 5, 17), 1);
        // dbControl.insertNewProduct("0000000", "Test Food", new Date(2015, 5, 17), 1);
        // dbControl.insertNewProduct("0000000", "Test Food", new Date(2015, 5, 17), 1);
        // dbControl.insertNewProduct("0000000", "Test Food", new Date(2015, 5, 17), 1);
        // dbControl.insertNewProduct("0000000", "Test Food", new Date(2015, 5, 17), 1);
        // dbControl.insertNewProduct("0000000", "Test Food", new Date(2015, 5, 17), 1);

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
                Date date = new Date(c.getInt(3));
                pantry.setFields(c.getString(2), date, c.getInt(4));
            } while (c.moveToNext());
        }

        c.close();
    }
}