package com.cop4656.teamdns.foodangel;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

public class PantryItemFragment extends Fragment {

    private View currentItem;
    private TextView ItemName;
    private TextView ExpireDate;
    private TextView Quantity;
    private String itemNameText;
    private String expireDateText;
    private String quantityText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentItem = inflater.inflate(R.layout.fragment_pantryitem, container, false);

        ItemName = (TextView)currentItem.findViewById(R.id.item_name);
        ExpireDate = (TextView)currentItem.findViewById(R.id.expire_date);
        Quantity = (TextView)currentItem.findViewById(R.id.quantity);

        final PantryItemFragment tmp = this;

        currentItem.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().beginTransaction().remove(tmp).commit();
            }
        });

        ItemName.setText(itemNameText);
        ExpireDate.setText(expireDateText);
        Quantity.setText(quantityText);

        return currentItem;
    }

    public void setFields(String name, Calendar expire, int quantity) {
        if (name != null)
            itemNameText = "Name: " + name.trim();

        if (expire != null)//Calendar.Month is + 1 because Months are zero based.
            expireDateText = "Expiration Date: " + (expire.get(Calendar.MONTH)+1) + "/" + expire.get(Calendar.DAY_OF_MONTH) + "/" + expire.get(Calendar.YEAR);
        quantityText = "Quantity: " + quantity;
    }
}
