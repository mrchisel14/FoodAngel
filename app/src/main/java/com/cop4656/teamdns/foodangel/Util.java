package com.cop4656.teamdns.foodangel;

import java.util.Date;

/**
 * Created by Shawn on 7/18/2015.
 */
public class Util {
    public static class ProductData{
        String barcode;
        String name;
        Date entryDate, expDate;
        int quantity;
        ProductData(){
            barcode = "";
            name = "";
            expDate = entryDate = null;
            quantity = 0;
        }
    }
}
