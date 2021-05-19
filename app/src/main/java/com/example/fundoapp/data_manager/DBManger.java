package com.example.fundoapp.data_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBManger extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="fundoNotes.db";

    public DBManger(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String qry1="create table tbl_users (id integer primary key autoincrement, users text)";
        String qry2="create table tbl_notes (id integer primary key autoincrement,noteID text, title text, content text)";
        db.execSQL(qry1);
        db.execSQL(qry2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS tbl_users");
        db.execSQL("DROP TABLE IF EXISTS tbl_notes");
        onCreate(db);
    }

    public  boolean addNotes(String user,String docID,String title,String content)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues cv1=new ContentValues();
        cv1.put("userID",user);
        ContentValues cv2=new ContentValues();
        cv2.put("docID",docID);
        cv2.put("title",title);
        cv2.put("content",content);

        db.insert("tbl_users",null,cv1);
        db.insert("tbl_users_note_id",null,cv2);
        return true;
    }



}