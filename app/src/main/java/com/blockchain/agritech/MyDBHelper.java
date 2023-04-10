package com.blockchain.agritech;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BidsOnCrops.db";
    private static final String TABLE_NAME = "BidsOnCrops";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_UPLOADER = "uploader";
    private static final String COLUMN_BIDDER = "bidder";
    private static final String COLUMN_VALUE_BIDDED = "valueBidded";
    private static final String COLUMN_QUALITY = "quality";
    private static final String COLUMN_DATE = "date";

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_UPLOADER + " TEXT, " +
                COLUMN_BIDDER + " TEXT, " +
                COLUMN_VALUE_BIDDED + " TEXT, " +
                COLUMN_QUALITY + " TEXT, " +
                COLUMN_DATE + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addBid(String uploader, String bidder, String valueBidded, String quality, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_UPLOADER, uploader);
        values.put(COLUMN_BIDDER, bidder);
        values.put(COLUMN_VALUE_BIDDED, valueBidded);
        values.put(COLUMN_QUALITY, quality);
        values.put(COLUMN_DATE, date);
        db.insert(TABLE_NAME, null, values);
    }

    public void deleteBid(String uploader, String bidder) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = COLUMN_UPLOADER + "=? AND " + COLUMN_BIDDER + "=?";
        String[] whereArgs = { uploader, bidder };
        db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    public void deleteAllBidsByUploader(String uploader) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = COLUMN_UPLOADER + "=?";
        String[] whereArgs = { uploader };
        db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    public List<String[]> getBidsByBidderAndUploader(String uploaderName, String quality, String date) {
        List<String[]> bidsList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_UPLOADER + "=? AND " + COLUMN_QUALITY + "=? AND " + COLUMN_DATE + "=?";
        String[] selectionArgs = { uploaderName, quality, date };
        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                String bidderName = cursor.getString(cursor.getColumnIndex(COLUMN_BIDDER));
                String valueBidded = cursor.getString(cursor.getColumnIndex(COLUMN_VALUE_BIDDED));
                String[] bid = { bidderName, valueBidded };
                bidsList.add(bid);
            } while (cursor.moveToNext());
        }

        return bidsList;
    }

    public void deleteBidByUploaderAndQualityAndDate(String bidder, String quality, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereArgs = { bidder, quality, date };
        db.delete(TABLE_NAME, COLUMN_UPLOADER + " = ? AND " + COLUMN_QUALITY + " = ? AND " + COLUMN_DATE + " = ?", whereArgs);
    }



}