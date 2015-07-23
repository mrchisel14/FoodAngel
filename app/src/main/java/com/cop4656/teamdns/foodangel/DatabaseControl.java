package com.cop4656.teamdns.foodangel;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DatabaseControl extends SQLiteOpenHelper {

    private final SQLiteDatabase _writeDb = this.getWritableDatabase();
    private final SQLiteDatabase _readDb = this.getReadableDatabase();

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
                "Quantity INTEGER, " +
                "InsertDate INTEGER);");

        db.execSQL("CREATE TABLE GCM (" +
                "InstanceId TEXT);");
    }

    public void insertNewProduct(String barcode, String food, Date expire, int quantity) {
        _writeDb.execSQL("INSERT INTO FoodAngel(Barcode, FoodItem, ExpirationDate, Quantity, InsertDate)" +
                "VALUES('" + barcode + "', '" + food + "', " + expire.getTime() + ", " + quantity + ", " + (new Date()).getTime() + ");");
    }

    public Cursor selectProducts() {
        return _readDb.rawQuery("SELECT * FROM FoodAngel;", null);
    }
    public Util.ProductData retrieveProduct(String barcode){
        Util.ProductData data = new Util.ProductData();
        Cursor c = _readDb.rawQuery("SELECT * FROM FoodAngel WHERE Barcode == " + barcode + ";", null);
        if(c.moveToFirst()){
            data.barcode = c.getString(1);
            data.name = c.getString(2);
            data.expDate = new Date(c.getInt(3));
            data.quantity = c.getInt(4);
        }
        else{
            data = null;
        }
        return data;
    }

    public void insertInstanceId(String instanceId) {
        _writeDb.execSQL("INSERT INTO GCM(InstanceId)" +
                "VALUES('" + instanceId + "');");
    }

    public void deleteInstanceId(String instanceId) {
        _writeDb.execSQL("DELETE FROM GCM" +
                "WHERE InstanceId = '" + instanceId + "';");
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

        db.execSQL("DROP TABLE GCM;");

        db.execSQL("CREATE TABLE GCM (" +
                "InstanceId TEXT);");
    }
}
