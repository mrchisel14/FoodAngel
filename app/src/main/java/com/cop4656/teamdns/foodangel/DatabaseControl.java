package com.cop4656.teamdns.foodangel;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class DatabaseControl extends SQLiteOpenHelper {

    private final SQLiteDatabase _writeDb = this.getWritableDatabase();
    private final SQLiteDatabase _readDb = this.getReadableDatabase();
    private Context context;

    public DatabaseControl(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE FoodAngel (" +
                "Id INTEGER PRIMARY KEY, " +
                "Barcode TEXT, " +
                "FoodItem TEXT, " +
                "ExpirationDate INTEGER, " +
                "Quantity INTEGER, " +
                "InsertDate INTEGER);");
    }

    public void insertNewProduct(String barcode, String food, Date expire, int quantity) {
        if (food.contains("'")) {
            food = food.replace("'", "\\'");
        }

        _writeDb.execSQL("INSERT INTO FoodAngel(Barcode, FoodItem, ExpirationDate, Quantity, InsertDate)" +
                "VALUES('" + barcode + "', '" + food + "', " + expire.getDate() + ", " + quantity + ", " + (new Date()).getDate() + ");");
    }

    public Cursor selectProducts() {
        return _readDb.rawQuery("SELECT * FROM FoodAngel" +
                " WHERE ExpirationDate > " + (new Date()).getDate() +
                " ORDER BY ExpirationDate DESC;", null);
    }

    public void deleteProduct(int i) {
        _writeDb.execSQL("DELETE FROM FoodAngel " +
                "WHERE Id = " + i + ";");
    }

    public Util.ProductData retrieveProduct(String barcode){
        Util.ProductData data = null;
        if(barcode == null)return null;
        try {
            Cursor c = _readDb.rawQuery("SELECT * FROM FoodAngel WHERE Barcode=\'" + barcode + "\';", null);
            if(c.moveToFirst()){
                data = new Util.ProductData();
                data.barcode = c.getString(1);
                data.name = c.getString(2);
                data.expDate = new Date(c.getLong(3));
                data.quantity = c.getInt(4);
                data.entryDate = new Date(c.getLong(5));
            }
            else{
                data = null;
            }
            c.close();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Unable to search barcode", Toast.LENGTH_SHORT);
        }
        return data;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE FoodAngel;");

        db.execSQL("CREATE TABLE FoodAngel (" +
                "Id INTEGER PRIMARY KEY, " +
                "Barcode TEXT, " +
                "FoodItem TEXT, " +
                "ExpirationDate INTEGER, " +
                "Quantity INTEGER, " +
                "InsertDate INTEGER);");
    }
}
