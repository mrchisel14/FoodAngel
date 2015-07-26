package com.cop4656.teamdns.foodangel;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class PantryItemFragment extends Fragment {

    private View currentItem;
    private TextView ItemName;
    private TextView ExpireDate;
    private TextView Quantity;
    private TextView Id;
    private String itemNameText;
    private String expireDateText;
    private String quantityText;
    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentItem = inflater.inflate(R.layout.fragment_pantryitem, container, false);

        ItemName = (TextView)currentItem.findViewById(R.id.item_name);
        ExpireDate = (TextView)currentItem.findViewById(R.id.expire_date);
        Quantity = (TextView)currentItem.findViewById(R.id.quantity);
        Id = (TextView)currentItem.findViewById(R.id.id);

        final PantryItemFragment tmp = this;

        currentItem.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Animation
                Animation animation = AnimationUtils.loadAnimation(tmp.getActivity(), R.anim.abc_fade_out);
                animation.setDuration(getResources().getInteger(R.integer.abc_config_activityDefaultDur));

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        DatabaseControl db = new DatabaseControl(getActivity(), "FA", null, 7);
                        db.deleteProduct(Integer.parseInt(Id.getText().toString()));
                        getActivity().getFragmentManager().beginTransaction().remove(tmp).commit();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

                getView().startAnimation(animation);
            }
        });

        ItemName.setText(itemNameText);
        ExpireDate.setText(expireDateText);
        Quantity.setText(quantityText);
        Id.setText(id);

        ItemName.setShadowLayer(15, 0, 0, Color.BLACK);
        ExpireDate.setShadowLayer(15, 0, 0, Color.BLACK);
        Quantity.setShadowLayer(15, 0, 0, Color.BLACK);
        ((Button)currentItem.findViewById(R.id.button)).setShadowLayer(15, 0, 0, Color.BLACK);

        return currentItem;
    }

    public void setFields(int intid, String name, Calendar expire, int quantity) {
        if (name != null)
            itemNameText = "Name: " + name.trim();

        if (expire != null)//Calendar.Month is + 1 because Months are zero based.
            expireDateText = "Expiration Date: " + (expire.get(Calendar.MONTH)+1) + "/" + expire.get(Calendar.DAY_OF_MONTH) + "/" + expire.get(Calendar.YEAR);

        quantityText = "Quantity: " + quantity;

        id = String.valueOf(intid);
    }
}
