package com.example.prox.reminder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.example.prox.MainActivity;
import com.radaee.reader.R;

 


import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.ResourceCursorAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class nextmonthcalendar extends ListActivity{

	private DataBaseHelper dbHelper;
	private SimpleCursorAdapter dataAdapter;
	private ReminderDatabaseAdapter reminderadapter;
	private SQLiteDatabase newDB;
	//	private MyListAdapter newcursor;

	private ListView listView;



	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todaycalendar);

		reminderadapter = new ReminderDatabaseAdapter(this);
		reminderadapter.open();
		
		listView = (ListView)findViewById(android.R.id.list);

		displaydata();

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {

		case R.id.action_new:
			// add action
			AddReminder();
		case R.id.action_cancel:  Intent i = new Intent(this, Tablayout.class);
			i.putExtra("activetab", "2");
	        startActivity(i);
	        break;
	
		default:
			}
		return super.onOptionsItemSelected(item);
		
	}
	private void AddReminder() {
		Intent i = new Intent(nextmonthcalendar.this, ReminderAdd.class);
		startActivity(i);
	}

	private void displaydata() 
	{	
		Calendar _calendar;
		int month,year, day;
		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		day = _calendar.get(Calendar.DATE);
		Log.d("Date",""+day+ "-"+month+"-"+year);
		if(month == 12){ month = 1;}
		month += 1;
		String tdate = "" + month;
		
		// Get all of the notes from the database and create the item list
		Cursor reminderCursor = reminderadapter.fetchAllReminderThisMonth(tdate);
		
		if(reminderCursor.getCount() <= 0){
	 
			LinearLayout linearLayout = (LinearLayout) findViewById(R.id.monthreminder);
			
	        TextView noreminder = new TextView(this);
			noreminder.setText("There are no reminders for next month.");
			noreminder.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			linearLayout.setGravity(Gravity.TOP);
			linearLayout.addView(noreminder);
		}
		
		
		String[] from = new String[] {ReminderDatabaseAdapter.KEY_TITLE,ReminderDatabaseAdapter.KEY_DATE,ReminderDatabaseAdapter.KEY_TIME};
		int[] to = new int[] { R.id.Title ,R.id.Date,R.id.Time};


		// Now create an array adapter and set it to display using our row
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter reminders=new SimpleCursorAdapter(this, R.layout.todaycalendar_row, reminderCursor, from, to);


		if (reminderCursor != null) {
			startManagingCursor(reminderCursor);
			Reminder ebook = new Reminder();
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.todaycalendar_row, reminderCursor, from, to);
		};


		setListAdapter(reminders);
	}
	
	public void attemptDelete(final int reminderId){
		
		//Toast.makeText(getBaseContext(), "ReminderID: "+ reminderId, Toast.LENGTH_LONG).show();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(nextmonthcalendar.this);
        builder.setTitle("Delete reminder");
        builder.setMessage("Are you sure you want to delete this reminder in your account?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   	   reminderadapter.deleteEntry(reminderId);
                		   Toast.makeText(getApplicationContext(), "Successfully deleted.", Toast.LENGTH_LONG).show();
                		   Intent intent = new Intent(getApplicationContext(),  com.example.prox.reminder.MainActivity.class);
                		   finish();
	                	startActivity(intent);
                   }
               });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                   }
               });
        
        AlertDialog dialog = builder.create();
        dialog.show();
        
	}	
	
	public void updateReminder(int reminderId, String title, String date, String time, String desc){
		
		//Toast.makeText(getApplicationContext(), ""+reminderId, Toast.LENGTH_SHORT).show();
		
		Intent i = new Intent(this, ReminderUpdate.class);
		i.putExtra("id", reminderId);
		i.putExtra("title", title);
		i.putExtra("date", date);
		i.putExtra("time", time);
		i.putExtra("desc", desc);
		
		startActivity(i);
	}
	
 
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		
		Calendar _calendar;
		int month,year, day;
		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		day = _calendar.get(Calendar.DATE);
		Log.d("Date",""+day+ "-"+month+"-"+year);
		if(month == 12){ month = 1;}
		month += 1;
		String tdate = "" + month;
		
		Cursor listCursor = reminderadapter.fetchAllReminderThisMonth(tdate);

		listCursor.moveToPosition(position);
		final int item_id = listCursor.getInt(listCursor.getColumnIndex(ReminderDatabaseAdapter.KEY_ID));
        final String title = listCursor.getString(listCursor.getColumnIndex(ReminderDatabaseAdapter.KEY_TITLE));
        final String date = listCursor.getString(listCursor.getColumnIndex(ReminderDatabaseAdapter.KEY_DATE));
        final String time = listCursor.getString(listCursor.getColumnIndex(ReminderDatabaseAdapter.KEY_TIME));
        final String desc = listCursor.getString(listCursor.getColumnIndex(ReminderDatabaseAdapter.KEY_DESCRIPTION));
        
        CharSequence[] items = {"View","Delete"};
        
        
        
		AlertDialog.Builder builder3 =new AlertDialog.Builder(nextmonthcalendar.this);
		builder3.setTitle("Select an action").setItems(items, new DialogInterface.OnClickListener() {

    	@Override
    	public void onClick(DialogInterface dialog, int which) {
    		
    		switch(which){
			case 0: 
				updateReminder(item_id, title, date, time, desc);
				break;
			case 1:
				attemptDelete(item_id);
				break;
			}
    	}
    	});
    	
    	builder3.show();
        
      
	}
	

};
        

 
