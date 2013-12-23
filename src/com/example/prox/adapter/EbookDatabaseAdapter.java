package com.example.prox.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.prox.DataBaseHelper;
import com.example.prox.Ebook;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class EbookDatabaseAdapter extends SQLiteOpenHelper{
	
		static final String DATABASE_NAME = "users.db";
		public static final int DATABASE_VERSION = 1;
		
	    static final String DATABASE_TABLE = "userebooks";
	    public static final String KEY_ROWID = "_id";
		public static final String KEY_OBJECTID = "objectId";
	    public static final String KEY_TITLE = "title";
	    public static final String KEY_AUTHOR ="author";
	    public static final String KEY_ISBN ="ISBN";
	    public static final String KEY_FILENAME = "filename";
	    public static final String KEY_COVER = "cover";
	    public static final String KEY_CATEGORY = "category";
	    public static final String KEY_STATUS = "status";
		
	    
		// TODO: Create public field for each column in your table.
		// SQL Statement to create a new database.
		static final String DATABASE_CREATE = "create table "+" userebooks"+ "( " +"_id"+" integer primary key autoincrement,"+ "objectId text, title text, filename text, author text, ISBN text, category text, cover text, status integer); ";
		// Variable to hold the database instance
		public  SQLiteDatabase db;
		// Context of the application using the database.
		public Context context;
		// Database open/upgrade helper
		private DataBaseHelper dbHelper;
		
		 private String[] allColumns = {KEY_OBJECTID, KEY_TITLE,KEY_AUTHOR,KEY_FILENAME,KEY_COVER,KEY_CATEGORY,KEY_STATUS};
		
		public  EbookDatabaseAdapter(Context context) 
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DATABASE_CREATE);
			
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newwVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE);
			onCreate(db);
		}	


		public  void  open() throws SQLException 
		{
			db = this.getWritableDatabase();
		 
		}
		
		
		public void close() 
		{
			db.close();
		}

		public  SQLiteDatabase getDatabaseInstance()
		{	
			db.execSQL(DATABASE_CREATE);
			return db;
		}

		public void insertEntry(String objectId, String title, String filename, String author, String ISBN, String cover, String status, String category)
		{
		   SQLiteDatabase db=this.getWritableDatabase();
	       ContentValues newValues = new ContentValues();
			// Assign values for each row.
	       newValues.put("objectId", objectId);
			newValues.put("filename", filename);
			newValues.put("title",title);
			newValues.put("author", author);
			newValues.put("ISBN",ISBN);
			newValues.put("cover", cover);
			newValues.put("status",status);
			newValues.put("category",category);
			
			// Insert the row into your table
			db.insert("userebooks", null, newValues);
			//db.close();
			//Toast.makeText(context, "Ebook added successfully", Toast.LENGTH_LONG).show();
		}
		
		
		public int deleteEntry(String objectId)
		{
			SQLiteDatabase db=this.getWritableDatabase();
		    String where="objectId=?";
		    int numberOFEntriesDeleted= db.delete("userebooks", where, new String[]{objectId});
		    db.close();
		    return numberOFEntriesDeleted;
		}	
		
 
		public Cursor getSingleEntry(String objectId)
		{
			SQLiteDatabase db=this.getReadableDatabase();
			String [] columns=new String[]{"id",KEY_OBJECTID,KEY_AUTHOR,KEY_CATEGORY,KEY_COVER,KEY_FILENAME,KEY_TITLE,KEY_STATUS};
			Cursor cur = db.query(DATABASE_TABLE, columns, objectId +"=?", new String[]{objectId}, null, null, null);
			
			return cur;		
		}
		
		@SuppressLint("NewApi")
		public ArrayList<Ebook> fetchAllUserEbooks() {
			ArrayList<Ebook> ebooks = new ArrayList<Ebook>();
			
			SQLiteDatabase db=this.getReadableDatabase();
			Cursor cur = db.rawQuery("select * from userebooks",  null);
			
			cur.moveToFirst();
			while(!cur.isAfterLast()){
				Ebook ebook = cursorToEbook(cur);
				ebooks.add(ebook);
				cur.moveToNext();
			}
			cur.close();
			return ebooks;
		}
	    public Cursor fetchAllEbooks() {
	    	 
	        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID,KEY_TITLE,KEY_FILENAME,KEY_COVER, KEY_AUTHOR,KEY_ISBN,KEY_OBJECTID,KEY_STATUS}, null, null, null, null, null);
	    }
	    
		private Ebook cursorToEbook(Cursor cursor){
			Ebook ebook = new Ebook();
			ebook.setID(cursor.getString(0));
			ebook.setTitle(cursor.getString(1));
			
			return ebook;
			
		}
		  
		public int updateEntry(String objectId, String title, String filename, String author, String ISBN, String cover, int status, String category)
		{
			// Define the updated row content.
			ContentValues updatedValues = new ContentValues();
			// Assign values for each row.
			updatedValues.put("filename", filename);
			updatedValues.put("title",title);
			updatedValues.put("author", author);
			updatedValues.put("ISBN",ISBN);
			updatedValues.put("cover", cover);
			updatedValues.put("status",status);
			updatedValues.put("category",category);
			
	        String where="objectId = ?";
		    return db.update("userebooks",updatedValues, where, new String[]{objectId});			   
		}



		

}



