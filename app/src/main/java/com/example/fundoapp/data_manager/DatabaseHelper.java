//package com.example.fundoapp.data_manager;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//public class DatabaseHelper extends SQLiteOpenHelper {
//
//    private static final String TAG = "DatabaseHelper";
//
//    private static final String TABLE_NAME1 = "Users";
//    private static final String TABLE_NAME2= "User Notes";
//    private static final String TABLE_NAME3= "User Notes ID";
//    private static final String COL1 = "UserID";
//    private static final String COL2 = "NoteID";
//    private static final String COL3 ="Title";
//    private static final String COL4 ="Content";
//
//
//
//
//    public DatabaseHelper(Context context) {
//        super(context, TAG, null, 1);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String createTable1 = "CREATE TABLE " + TABLE_NAME1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COL1 +" TEXT)";
//        String createTable2 = "CREATE TABLE " + TABLE_NAME2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COL2 +" TEXT)";
//        String createTable3 = "CREATE TABLE " + TABLE_NAME2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COL2 +" TEXT)";
//        db.execSQL(createTable1);
//        db.execSQL(createTable2);
//        db.execSQL(createTable3);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
//        onCreate(db);
//    }
//
//    public boolean addData(String user,long timeID,String title,String content) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL1,user);
//
//        Log.d(TAG, "addData: Adding " + user + " to " + TABLE_NAME1);
//
//        db.insert(TABLE_NAME1, null, contentValues);
//
//        ContentValues contentValues1 = new ContentValues();
//        contentValues1.put(COL1,user);
//
//        Log.d(TAG, "addData: Adding " + user + " to " + TABLE_NAME1);
//
//        db.insert(TABLE_NAME2, null, contentValues);
//
//        ContentValues contentValues2 = new ContentValues();
//        contentValues2.put(COL2,timeID);
//
//        Log.d(TAG, "addData: Adding " + user + " to " + TABLE_NAME2);
//
//        db.insert(TABLE_NAME3, null, contentValues);
//
//        ContentValues contentValues3 = new ContentValues();
//        contentValues3.put(COL3,title);
//        contentValues3.put(COL4,content);
//        //if date as inserted incorrectly it will return -1
//        if (result == -1) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//
//
//}