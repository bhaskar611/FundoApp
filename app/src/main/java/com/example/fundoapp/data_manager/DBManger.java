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
    private static final String NOTES = "notes";
    private static final String KEY_ID = "docID";
    private static final String KEY_NOTE ="note";
    private static final String KEY_TITLE ="title";
    private static final String USERS = "users";
    private static final String KEY_UID = "userID";
    private static final String FOREIGN_KEY ="foriegnKey";
    private static final String TAG = "DBManager";

    public DBManger(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String qry1=("CREATE TABLE " + USERS + "("
                + KEY_UID + " INTEGER PRIMARY KEY,"  + "INTEGER REFERENCES " + NOTES + ")");
        String qry2=("CREATE TABLE " + NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_NOTE + " TEXT" + ")");
        db.execSQL(qry1);
        db.execSQL(qry2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + USERS);
            db.execSQL("DROP TABLE IF EXISTS " + NOTES);
            onCreate(db);
        }
    }

    public  void addNotes(String user,String docID,String title,String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv1 = new ContentValues();
            cv1.put(KEY_UID, user);
            ContentValues cv2 = new ContentValues();
            cv2.put(KEY_ID, docID);
            cv2.put(KEY_TITLE, title);
            cv2.put(KEY_NOTE, content);

            db.insert(USERS, null, cv1);
            db.insert(NOTES, null, cv2);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }


    }

}