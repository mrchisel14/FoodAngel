package com.cop4656.teamdns.foodangel;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DatabaseControl extends SQLiteOpenHelper {

    public DatabaseControl(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE FoodAngel (" +
                "Id INTEGER PRIMARY KEY, " +
                "Barcode TEXT, " +
                "FoodItem TEXT, " +
                "ExpirationDate INTEGER, " +
                "Quantity INTEGER);");

        db.execSQL("CREATE TABLE GCM (" +
                "InstanceId TEXT);");

        db.close();
    }

    public void insertNewProduct(String barcode, String food, Date expire, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO FoodAngel(Barcode, FoodItem, ExpirationDate, Quantity)" +
                "VALUES('" + barcode + "', '" + food + "', " + expire.getTime() + ", " + quantity + ");");
        db.close();
    }

    public Cursor selectProducts() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM FoodAngel;", null);
    }

    public void insertInstanceId(String instanceId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO GCM(InstanceId)" +
                "VALUES('" + instanceId + "');");
        db.close();
    }

    public void deleteInstanceId(String instanceId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM GCM" +
                "WHERE InstanceId = '" + instanceId + "';");
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
