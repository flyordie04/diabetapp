package com.example.mirek.diabetapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String database_name="DiabetApp";
    private static final String table_settings  = "Settings";
    private static final String table_results  = "Results";

    private static final String CREATE_TABLE_SETTINGS = "CREATE TABLE " + table_settings + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, PHONE STRING)";
    private static final String CREATE_TABLE_RESULTS = "CREATE TABLE " + table_results + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, RESULT STRING, DATE STRING, TEXT_MESSAGE INT)";

    public DatabaseHelper(Context context) {
        super(context, database_name, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table " + table_results + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, RESULT STRING, DATE STRING, TEXT_MESSAGE INT)");
        //db.execSQL("create table Settings (ID INTEGER PRIMARY KEY AUTOINCREMENT, PHONE STRING)");
        Log.d("Database","2");
        db.execSQL(CREATE_TABLE_RESULTS);
        db.execSQL(CREATE_TABLE_SETTINGS);
        Log.d("Database","3");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.d("Database","4");
        db.execSQL("DROP TABLE IF EXISTS " + table_results);
        db.execSQL("DROP TABLE IF EXISTS " + table_settings);
        Log.d("Database","5");
        onCreate(db);
    }


    public boolean wstawdane(String result, String date, int text_message){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Date", date);
        cv.put("Text_message", text_message);
        if(db.insert(table_results, null, cv)==-1)return false;
        else return true;
    }
    public SQLiteCursor pokazdane(){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteCursor cursor = (SQLiteCursor) db.rawQuery("SELECT * FROM " + table_results, null);
        return cursor;
    }
    public SQLiteCursor pokazUstawienia(){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteCursor cursor = (SQLiteCursor) db.rawQuery("SELECT * FROM  " + table_settings, null);
        Log.d("Database","3");
        return cursor;
    }
    public boolean aktualizujUstawienia(String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        //SQLiteCursor cursor = (SQLiteCursor) db.rawQuery("SELECT * FROM Settings",null);
        ContentValues cv = new ContentValues();
        cv.put("Phone",phone);
        //if(cursor == null){
            db.insert(table_settings,null,cv);
        //} else {
           //db.update("Settings",cv,"ID = ?", new String[]{"1"});
        //}
        return true;
    }
}
