package com.example.prox.reminder;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ReminderDatabaseAdapter extends SQLiteOpenHelper {
	
	static final String DATABASE_NAME = "users.db";
	public static final int DATABASE_VERSION = 1;
	
	static final String DATABASE_TABLE = "userreminders";
	public static final String KEY_ID = "_id";
	public static final String KEY_TITLE ="title";
	public static final String KEY_DATE ="date";
	public static final String KEY_TIME ="time";
	public static final String KEY_DESCRIPTION ="description";
	
	
	static final String DATABASE_CREATE = "create table "+"userreminders"+"("+ KEY_ID +" integer primary key autoincrement,"+"title,date,time,description);";
	public SQLiteDatabase db;
	public Context context;
 
	public ReminderDatabaseAdapter (Context context)
	{
		super (context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newwVersion){
		db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE);
		onCreate(db);
	}
	
	
	public void open() throws SQLException
	{
		db = this.getWritableDatabase();
	}
	

	public void close()
	{
		db.close();
	}
	
	public SQLiteDatabase gerDatabaseInstance()
	{
		db.execSQL(DATABASE_CREATE);
		return db;
	}
	

	public void insertEntry(String title, String date, String time, String description)
	{
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues newValues = new ContentValues();
		
		newValues.put("title", title);
		newValues.put("date", date);
		newValues.put("time", time);
		newValues.put("description", description);
		
		db.insert("userreminders", null, newValues);
		
		Log.d("Reminder Insert", "Add reminder successful.");
	}
	
	public int deleteEntry(int id)
	{
		int numberOFEntriesDeleted;
		String [] reminderid = new String[] {String.valueOf(id)};
		SQLiteDatabase db = this.getWritableDatabase();
		numberOFEntriesDeleted = db.delete("userreminders", "_id = ?", reminderid);
		db.close();
		
		return numberOFEntriesDeleted;
		
	}
    public int countReminders(){
    	Cursor cur = db.rawQuery("select * from userreminders",  null);
    	return cur.getCount();
    }

	
	public Cursor getSingleEntry(int id)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		String [] columns = new String[]{"_id",KEY_TITLE, KEY_DATE, KEY_TIME, KEY_DESCRIPTION};
		Cursor cur = db.query(DATABASE_TABLE, columns, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
		
		return cur;
		
	}
	public int checkReminderToDate(String tdate){
		
		//Log.d("Query", ""+ tdate); 
		SQLiteDatabase db=this.getReadableDatabase();
		String [] columns=new String[]{"_id",KEY_DATE,KEY_TITLE,KEY_TIME};
		String [] ttdate=new String[]{tdate};
		//Cursor cur = db.query(DATABASE_TABLE, columns, "date=?", tdate, null, null, null);
		Cursor cur = db.rawQuery("select * from userreminders where date=?",  ttdate); //db.query(DATABASE_TABLE, columns,"date=?", new String[]{tdate.toString()}, null, null, null);
		
	    int cnt = cur.getCount();
	    
	    //Log.d("Checking", "Check"+ cnt+tdate.toString());
	    //cur.close();
		return cnt; 
	}
//	public ArrayList<Reminder> fetchAllUserReminder(){
//		
//		ArrayList<Reminder> reminderlist = new ArrayList<Reminder>();
//		
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cur = db.rawQuery("select * from userreminders", null);
//		
//		if (cur.moveToFirst()) {
//	        do {
//	            Reminder reminder = new Reminder();
//	            reminder.setid(Integer.parseInt(cur.getString(0)));
//	            reminder.setTitle(cur.getString(1));
//	            reminder.setDate(cur.getString(2));
//	            reminder.setTime(cur.getString(3));
//	            reminder.setDescription(cur.getString(4));
//	            // Adding contact to list
//	            reminderlist.add(reminder);
//	        } while (cur.moveToNext());
//	    }
////		cur.moveToFirst();
////		while(!cur.isAfterLast()){
////			Reminder reminder = cursorToReminders(cur);
////			reminderlist.add(reminder);
////			cur.moveToNext();
////		}
////		cur.close();
//		return reminderlist;
//	}
	 public ArrayList<Reminder> getAllReminders() {
	        ArrayList<Reminder> reminders = new ArrayList<Reminder>();
	 
	        // 1. build the query
	        String query = "SELECT  * FROM " + DATABASE_TABLE;
	 
	        // 2. get reference to writable DB
	        SQLiteDatabase db = this.getWritableDatabase();
	        Cursor cursor = db.rawQuery("select * from userreminders", null);
	        
	        // 3. go over each row, build book and add it to list
	        Reminder reminder = null;
	        if (cursor.moveToFirst()) {
	            do {
	                reminder = new Reminder();
	                reminder.setid(Integer.parseInt(cursor.getString(0)));
	                reminder.setTitle(cursor.getString(1));
	                reminder.setDate(cursor.getString(2));
	 
	                // Add book to books
	                reminders.add(reminder);
	            } while (cursor.moveToNext());
	        }
	 
	        Log.d("getAllReminders()", reminders.toString());
	 
	        // return books
	        return reminders;
	    }
	
	public Cursor fetchAllReminder(){
 
		return db.query(DATABASE_TABLE, new String[]{KEY_ID,KEY_TITLE,KEY_DATE,KEY_TIME,KEY_DESCRIPTION},null , null, null, null,null);
 
	
	}
	public Cursor fetchAllReminderThisMonth(String month){
		 
		Cursor cursor = db.rawQuery("select * from userreminders where  date LIKE '" + month +"%' ", null);
		Log.d("This Month", ""+cursor.getCount());
		return cursor; //db.query(DATABASE_TABLE, new String[]{KEY_ID,KEY_TITLE,KEY_DATE,KEY_TIME,KEY_DESCRIPTION},"date" + "LIKE '" + month+ "%'" ,null, null, null,null);
 
	}
	public Cursor fetchAllReminderByDate(String date){
		 
		Cursor cursor = db.rawQuery("select * from userreminders where  date LIKE '" + date +"%' ", null);
		Log.d("This Month", ""+cursor.getCount());
		return cursor; //db.query(DATABASE_TABLE, new String[]{KEY_ID,KEY_TITLE,KEY_DATE,KEY_TIME,KEY_DESCRIPTION},"date" + "LIKE '" + month+ "%'" ,null, null, null,null);
 
	}
	
	
	private Reminder cursorToReminders(Cursor cursor){
		Reminder reminder = new Reminder();
		reminder.setid(cursor.getInt(0));
		reminder.setTitle(cursor.getString(1));
		
		return reminder;
	}
	
	public int updateEntry(int id, String title, String date, String time, String description)
	{
		ContentValues updatedValues = new ContentValues();
		
		updatedValues.put("title", title);
		updatedValues.put("date", date);
		updatedValues.put("time", time);
		updatedValues.put("description", description);
		
		String [] reminderid = new String[] {String.valueOf(id)};
		String where = "_id = ?";
		return db.update("userreminders", updatedValues, where,  reminderid);
		
	}
	
	
}
