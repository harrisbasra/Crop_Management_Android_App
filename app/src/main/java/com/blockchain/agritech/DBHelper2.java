package com.blockchain.agritech;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBHelper2 extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Transport.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "TransportTable";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "ContractorName";
    private static final String COL_3 = "TransporterName";
    private static final String COL_4 = "ValueBidded";
    private static final String COL_5 = "Quantity";
    private static final String COL_6 = "Date";

    public DBHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ContractorName TEXT, TransporterName TEXT, ValueBidded TEXT, Quantity TEXT, Date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String contractorName, String transporterName, String valueBidded,
                              String quantity, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, contractorName);
        contentValues.put(COL_3, transporterName);
        contentValues.put(COL_4, valueBidded);
        contentValues.put(COL_5, quantity);
        contentValues.put(COL_6, date);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean deleteDataByQuantity(String quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COL_5 + "=?", new String[]{quantity});
        return result > 0;
    }

    public boolean deleteDataByDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COL_6 + "=?", new String[]{date});
        return result > 0;
    }

    public boolean deleteDataByContractorName(String contractorName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COL_2 + "=?", new String[]{contractorName});
        return result > 0;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME, null);
    }

    public List<String[]> getDataByContractorName(String contractorName) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String[]> dataList = new ArrayList<>();
        String[] columns = {"ContractorName", "TransporterName", "ValueBidded", "Quantity", "Date"};
        String selection = "ContractorName = ?";
        String[] selectionArgs = { contractorName };
        Cursor cursor = db.query("Transport", columns, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            String[] data = new String[5];
            data[0] = cursor.getString(cursor.getColumnIndex("ContractorName"));
            data[1] = cursor.getString(cursor.getColumnIndex("TransporterName"));
            data[2] = cursor.getString(cursor.getColumnIndex("ValueBidded"));
            data[3] = cursor.getString(cursor.getColumnIndex("Quantity"));
            data[4] = cursor.getString(cursor.getColumnIndex("Date"));
            dataList.add(data);
        }
        return dataList;
    }

    public List<String[]> getDataByContractorNameQuantityAndDate(String contractorName, String quantity, String date) {

        SQLiteDatabase db = this.getReadableDatabase();
        List<String[]> dataList = new ArrayList<>();
        String[] columns = {"ContractorName", "TransporterName", "ValueBidded", "Quantity", "Date"};
        String selection = "ContractorName = ? AND Quantity = ? AND Date = ?";
        String[] selectionArgs = { contractorName, quantity, date };
        Cursor cursor = db.query("TransportTable", columns, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            String[] data = new String[5];
            data[0] = cursor.getString(cursor.getColumnIndex("ContractorName"));
            data[1] = cursor.getString(cursor.getColumnIndex("TransporterName"));
            data[2] = cursor.getString(cursor.getColumnIndex("ValueBidded"));
            data[3] = cursor.getString(cursor.getColumnIndex("Quantity"));
            data[4] = cursor.getString(cursor.getColumnIndex("Date"));
            dataList.add(data);
        }
        return dataList;
    }


}
