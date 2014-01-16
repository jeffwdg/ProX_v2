package com.example.prox.reminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{
	
//	public DataBaseHelper(Context context, String name, CursorFactory factory,
//			int version) {
//		super(context, name, factory, version);
//		// TODO Auto-generated constructor stub
//	}

	private static final int DATABASE_VERSION = 1;
	 
    // Database Name
    static final String DATABASE_NAME = "users.db";
 
    // Contacts table name
    static final String DATABASE_TABLE = "userreminders";
 
    // Contacts Table Columns names
    public static final String KEY_ID = "id";
   public static final String KEY_TITLE = "title";
   public static final String KEY_DATE = "date";
   public static final String KEY_TIME = "time";
   public static final String KEY_DESCRIPTION = "description";
 
   public DataBaseHelper(Context context) {
       super(context, DATABASE_NAME, null, DATABASE_VERSION);
   
   }
    // Creating Tables
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//       
//        db.execSQL(LoginDataBaseAdapter.DATABASE_CREATE);
//    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        String REMINDERS_TABLE = "CREATE TABLE " + DATABASE_TABLE + "(_id INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_TIME + " TEXT,"+ KEY_DESCRIPTION + " TEXT" +")";
        db.execSQL(REMINDERS_TABLE);
        
        }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
 
        // Create tables again
        onCreate(db);
    }
}
