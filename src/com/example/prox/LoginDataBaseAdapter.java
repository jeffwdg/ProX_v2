package com.example.prox;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LoginDataBaseAdapter 
{
		static final String DATABASE_NAME = "userlogin.db";
		static final int DATABASE_VERSION = 1;
		public static final int NAME_COLUMN = 1;
		
		// TODO: Create public field for each column in your table.
		// SQL Statement to create a new database.
		static final String DATABASE_CREATE = "create table "+"LOGIN"+
		                             "( " +"ID"+" integer primary key autoincrement,"+ "EMAIL text, PASSWORD text, FIRSTNAME text, LASTNAME text); ";
		// Variable to hold the database instance
		public  SQLiteDatabase db;
		// Context of the application using the database.
		private final Context context;
		// Database open/upgrade helper
		private DataBaseHelper dbHelper;
		public  LoginDataBaseAdapter(Context _context) 
		{
			context = _context;
			dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		public  LoginDataBaseAdapter open() throws SQLException 
		{
			db = dbHelper.getWritableDatabase();
			return this;
		}
		public void close() 
		{
			db.close();
		}

		public  SQLiteDatabase getDatabaseInstance()
		{
			return db;
		}

		public void insertEntry(String email, String password, String fname, String lname)
		{
	       ContentValues newValues = new ContentValues();
			// Assign values for each row.
			newValues.put("EMAIL", email);
			newValues.put("PASSWORD",password);
			newValues.put("FIRSTNAME", fname);
			newValues.put("LASTNAME",lname);
			
			// Insert the row into your table
			db.insert("LOGIN", null, newValues);
			//Toast.makeText(context, "User added successfully", Toast.LENGTH_LONG).show();
		}
		
		
		public int deleteEntry(String email)
		{
			//String id=String.valueOf(ID);
		    String where="EMAIL=?";
		    int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{email}) ;
	       // Toast.makeText(context, "Number for Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
	        return numberOFEntriesDeleted;
		}	
		
		public int isExisting(String email){
			String columns ="EMAIL=?";
			String table = "LOGIN";
			String limit = "1";
			int exist = 0;
			
			Cursor existing =  db.query(table, null, columns, new String[]{email},null,null,limit);
			if(existing.getCount() > 0)
				exist = 1;
			return exist;
		}
		
		public String getSingleEntry(String email)
		{
			Cursor cursor=db.query("LOGIN", null, " EMAIL=?", new String[]{email}, null, null, null);
	        if(cursor.getCount()<1) // Email Not Exist
	        {
	        	cursor.close();
	        	return "NOT EXIST";
	        }
		    cursor.moveToFirst();
			String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
			cursor.close();
			return password;				
		}
		
		public void  updateEntry(String email,String password)
		{
			// Define the updated row content.
			ContentValues updatedValues = new ContentValues();
			// Assign values for each row.
			updatedValues.put("EMAIL", email);
			updatedValues.put("PASSWORD",password);
			
	        String where="EMAIL = ?";
		    db.update("LOGIN",updatedValues, where, new String[]{email});			   
		}	
		

}

