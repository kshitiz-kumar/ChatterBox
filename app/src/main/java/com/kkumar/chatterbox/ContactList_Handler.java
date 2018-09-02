package com.kkumar.chatterbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ContactList_Handler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static  final String DATABASE_NAME="Contacts";
    private static  String TABLE_PERSON="Person";
    private static final String COLUMN_ID="Id";
    private static final String COLUMN_NAME="Name";
    private static final String COLUMN_PHONE="Phone";

    public ContactList_Handler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_PERSON + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +", "+
                COLUMN_NAME + " TEXT" + ", "+COLUMN_PHONE + " TEXT"+ ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        onCreate(db);
    }

    public void addcontact(String name,String phone)
    {
        ContentValues Values=new ContentValues();
        Values.put(COLUMN_NAME,name);
        Values.put(COLUMN_PHONE,phone);
        SQLiteDatabase db=getWritableDatabase();
        db.insert(TABLE_PERSON,null,Values);
    }

    public void deletemessage(int position)
    {
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERSON +" WHERE " + COLUMN_ID+ "=\"" + position + "\";" );
    }

    public Cursor databaseToString(){
        SQLiteDatabase db=getReadableDatabase();
        String query="SELECT * FROM " + TABLE_PERSON;
        Cursor c = db.rawQuery(query,null);
        return c;
    }

    public Cursor databaseToString(String name){
        SQLiteDatabase db=getReadableDatabase();
        String query="SELECT * FROM " + TABLE_PERSON+ " WHERE "+COLUMN_NAME+" = "+name;
        Cursor c = db.rawQuery(query,null);
        return c;
    }

    public int getMessageCount()
    {
        SQLiteDatabase db=getReadableDatabase();
        String query="SELECT * FROM " + TABLE_PERSON ;
        Cursor c = db.rawQuery(query,null);
        return c.getCount();
    }
}

