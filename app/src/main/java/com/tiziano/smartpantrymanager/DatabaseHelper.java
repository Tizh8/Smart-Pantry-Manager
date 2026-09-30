package com.tiziano.smartpantrymanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE pantry_items(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "expiryDate TEXT)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS pantry_items");
        onCreate(db);

    }

    public boolean addPantryItem(PantryItem item) {
        android.database.sqlite.SQLiteDatabase db = super.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("quantity", item.getQuantity());
        values.put("unit", item.getUnit());
        values.put("expiryDate", item.getExpiryDate());

        long result = db.insert("pantry_items", null, values);
        db.close();

        // -1 indicates that the insert did not work
        return result != -1;
    }

    public ArrayList<PantryItem> getAllPantryItems() {
        ArrayList<PantryItem> pantryItems = new ArrayList<>();

        SQLiteDatabase db = super.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM pantry_items", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow("quantity"));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow("unit"));
                String expiryDate = cursor.getString(cursor.getColumnIndexOrThrow("expiryDate"));

                PantryItem item = new PantryItem(id, name, quantity, unit, expiryDate);
                pantryItems.add(item);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return pantryItems;
    }

    public boolean updatePantryItem(PantryItem item) {

        SQLiteDatabase db = super.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("quantity", item.getQuantity());
        values.put("unit", item.getUnit());
        values.put("expiryDate", item.getExpiryDate());

        int result = db.update(
                "pantry_items",
                values,
                "id = ? ",
                new String[]{String.valueOf(item.getId())}
        );

        db.close();

        return result > 0;
    }

    public boolean deletePantryItem(int id) {

        SQLiteDatabase db = super.getWritableDatabase();

        db.beginTransaction();

        try {
            int result = db.delete(
                    "pantry_items",
                    "id = ?",
                    new String[]{String.valueOf(id)}
            );

            if (result > 0) {
                db.setTransactionSuccessful();
                return true;
            }

            return false;

        } finally {
            db.endTransaction();
            db.close();
        }
        }
}




