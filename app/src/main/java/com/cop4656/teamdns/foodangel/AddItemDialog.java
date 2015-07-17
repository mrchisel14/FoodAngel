package com.cop4656.teamdns.foodangel;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

/**
 * Created by Shawn on 7/16/2015.
 */
public class AddItemDialog extends Dialog {
    AddItemDialog(Context context){
        super(context);
        setContentView(R.layout.add_item);
        //getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

}
