package com.cop4656.teamdns.foodangel;

import java.util.Date;

/**
 * Created by Shawn on 7/18/2015.
 */
public class Util {
    public static class ProductData{
        String barcode;
        String name;
        Date date;
        int quantity;
        ProductData(){
            barcode = "";
            name = "";
            date = null;
            quantity = 0;
        }
    }
}
