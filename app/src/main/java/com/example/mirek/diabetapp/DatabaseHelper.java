package com.example.mirek.diabetapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;


public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String database_name="DiabetApp";
    //private static final String table_name="Oceny";

    public DatabaseHelper(Context context) {
        super(context, database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Wyniki (ID INTEGER PRIMARY KEY AUTOINCREMENT, RESULT STRING, DATE STRING, TEXT_MESSAGE INT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Wyniki");
        onCreate(db);
    }

    public boolean wstawdane(String result, String date, int text_message){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Result", result);
        cv.put("Date", date);
        cv.put("Text_message", text_message);
        if(db.insert("Wyniki", null, cv)==-1)return false;
        else return true;
    }
    public SQLiteCursor pokazdane(){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteCursor cursor = (SQLiteCursor) db.rawQuery("SELECT * FROM Wyniki",null);
        return cursor;
    }
}
