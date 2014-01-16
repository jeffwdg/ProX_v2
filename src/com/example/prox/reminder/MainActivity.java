package com.example.prox.reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.radaee.reader.R;

 

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity implements OnClickListener 
{
	private static final String tag = "MyCalendarActivity";
	private ReminderDatabaseAdapter reminderadapter;

	private TextView currentMonth;
	private Button selectedDayMonthYearButton;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	public SQLiteDatabase db;
	private Calendar _calendar;
	String[] datebyView;
	@SuppressLint("NewApi")
	private int month, year;
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";
	ReminderDatabaseAdapter dataBase;
	
	/** Called when the activity is first created. */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionBar = getActionBar();

		// Enabling Up / Back navigation
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		
		dataBase = new ReminderDatabaseAdapter(this);
	    dataBase.open();

		//spinner navigation ////////////////////////////////////////////////////////////////////////////

		datebyView =getResources().getStringArray(R.array.labelMenu);

		ArrayAdapter<String> spinnerMenu = new ArrayAdapter<String>(actionBar.getThemedContext(),android.R.layout.simple_list_item_1,datebyView);


		actionBar.setListNavigationCallbacks(spinnerMenu, new ActionBar.OnNavigationListener() {

			public boolean onNavigationItemSelected(int position, long id) {
				// Take action here, e.g. switching to the
				// corresponding fragment.
				//android.app.FragmentTransaction tx = getFragmentManager().beginTransaction();

				switch (position) {
				case 0:
					Log.d("Row 1", "My Row");
					break;
				case 1:
					Intent i = new Intent(MainActivity.this, Tablayout.class);
					startActivity(i);

					break;
				case 2:
					Intent ia = new Intent(MainActivity.this, Tablayout.class);
					startActivity(ia);
					break;
				default:
					break;
				}
				/**/
				//     tx.commit();
				return true;
			}
		});
		//end of spinner navigation/////////////////////////////////////////////////////////////////////////////////////	




		//calendar  part 1///////////////////////////////////////////////////////////////////////////////////////// 			

		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
				+ year);

		selectedDayMonthYearButton = (Button) this.findViewById(R.id.selectedDayMonthYear);
		selectedDayMonthYearButton.setText("Selected: ");

		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate,_calendar.getTime()));

		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) this.findViewById(R.id.calendar);

		// Initialised
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);


		//  displayThisDayReminder();


	}

	//end of calendar part 1////////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		//  case R.id.action_search:
		// search action
		//     return true;

		case R.id.action_new:
			// add action
			AddReminder();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	private void AddReminder() {
		Intent i = new Intent(MainActivity.this, ReminderAdd.class);
		startActivity(i);
	}
	public boolean checkReminder(String thisdate){
		boolean ret = false;
		
		
		int f = dataBase.checkReminderToDate(thisdate);
		if(f > 0){
			ret = true;
		}
		//Log.d("Test", "Date"+ thisdate + f );
		return ret;
	}
	


	private void goToTab() {
		Intent i = new Intent(MainActivity.this, Tablayout.class);
		startActivity(i);
	}



	/**
	 * 
	 * @param month
	 * @param year
	 */

	//calendar gridcell adapter///////////////////////////////////////////////////////////////////////

	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(getApplicationContext(),R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(DateFormat.format(dateTemplate,_calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	//calendar again///////////
	@Override
	public void onClick(View v) {
		if (v == prevMonth) {
			if (month <= 1) 
			{
				month = 12;
				year--;
			} else 
			{
				month--;
			}
			Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}
		if (v == nextMonth) {
			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}
			Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}

	}

	@Override
	public void onDestroy() {
		Log.d(tag, "Destroying View ...");
		super.onDestroy();
	}

	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener, android.content.DialogInterface.OnClickListener {
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
				"Wed", "Thu", "Fri", "Sat" };
		private final String[] months = { "January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
				31, 30, 31 };
		private int daysInMonth;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		private TextView num_events_per_day;
		private final HashMap<String, Integer> eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"dd-MMM-yyyy");

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId,
				int month, int year) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			Log.d(tag, "==> Passed in Date FOR Month: " + month + " "+ "Year: " + year);
			Calendar calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
			Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

			// Print Month
			printMonth(month+1, year);

			// Find Number of Events
			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
		}
		
		public int getMonthAsInt(String month){
			int imonth = 0;
			int i=0;
 
			if(TextUtils.equals(month, "January"))
			{imonth = 1; }
			if(TextUtils.equals(month, "February"))
			{imonth = 2; }
			if(TextUtils.equals(month, "March"))
			{imonth = 3; }
			if(TextUtils.equals(month, "April"))
			{imonth = 4; }
			if(TextUtils.equals(month, "May"))
			{imonth = 5; }
			if(TextUtils.equals(month, "June"))
			{imonth = 6; }
			if(TextUtils.equals(month, "July"))
			{imonth = 7; }
			if(TextUtils.equals(month, "August"))
			{imonth = 8; }
			if(TextUtils.equals(month, "September"))
			{imonth = 9; }
			if(TextUtils.equals(month, "October"))
			{imonth = 10; }
			if(TextUtils.equals(month, "November"))
			{imonth = 11; }
			if(TextUtils.equals(month, "December"))
			{imonth = 12; }
		
			return imonth;
		}
		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * Prints Month
		 * 
		 * @param mm
		 * @param yy
		 */
		 private void printMonth(int mm, int yy) {
			Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm-2;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
					+ daysInMonth + " days.");

			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
			Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"+ prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"+ prevMonth + " NextMonth: " + nextMonth+ " NextYear: " + nextYear);
			} 
			else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"+ prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			//Log.d(tag, "Week Day:" + currentWeekDay + " is "+ getWeekDayAsString(currentWeekDay));
			//Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
			//Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

			if (cal.isLeapYear(cal.get(Calendar.YEAR)))
				if (mm == 2)
					++daysInMonth;
				else if (mm == 3)
					++daysInPrevMonth;

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				Log.d(tag,
						"PREV MONTH:= "
								+ prevMonth
								+ " => "
								+ getMonthAsString(prevMonth)
								+ " "
								+ String.valueOf((daysInPrevMonth
										- trailingSpaces + DAY_OFFSET)
										+ i));
				list.add(String
						.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
								+ i)
								+ "-GREY"
								+ "-"
								+ getMonthAsString(prevMonth)
								+ "-"
								+ prevYear);
			}

			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
				Log.d(currentMonthName, String.valueOf(i) + " "
						+ getMonthAsString(currentMonth) + " " + yy);
				if (i == getCurrentDayOfMonth()) {
					list.add(String.valueOf(i) + "-BLUE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else {
					list.add(String.valueOf(i) + "-WHITE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}
			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-GREY" + "-"
						+ getMonthAsString(nextMonth) + "-" + nextYear);
			}
		 }

		 /**
		  * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		  * ALL entries from a SQLite database for that month. Iterate over the
		  * List of All entries, and get the dateCreated, which is converted into
		  * day.
		  * 
		  * @param year
		  * @param month
		  * @return
		  */
		 private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
				 int month) {
			 HashMap<String, Integer> map = new HashMap<String, Integer>();

			 return map;
		 }

		 @Override
		 public long getItemId(int position) {
			 return position;
		 }

		 @Override
		 public View getView(int position, View convertView, ViewGroup parent) {
			 View row = convertView;
			 
			 
			 if (row == null) {
				 LayoutInflater inflater = (LayoutInflater) _context
						 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				 row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			 }

			 // Get a reference to the Day gridcell
			 gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
			 gridcell.setOnClickListener(this);

			 // ACCOUNT FOR SPACING

			 Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
			 String[] day_color = list.get(position).split("-");
			 String theday = day_color[0];
			 String themonth = day_color[2];
			 String theyear = day_color[3];
			 if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				 if (eventsPerMonthMap.containsKey(theday)) {
					 num_events_per_day = (TextView) row
							 .findViewById(R.id.num_events_per_day);
					 Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					 num_events_per_day.setText(numEvents.toString());
				 }
			 }
			 
			 
			 int cmonth = getMonthAsInt(themonth);
			 String todate = cmonth+ "-" + theday + "-" + theyear;
			 // Set the Day GridCell
			 gridcell.setText(theday);
			 gridcell.setTag(cmonth + "-" + theday + "-" + theyear);
			 //int cmonth = getMonthAsInt(themonth);
			 //String todate = cmonth+ "-" + theday + "-" + theyear;
			 Log.d("Date", ""+ todate);
			 
			 ImageView rbell = (ImageView) row.findViewById(R.id.reminderbell);
			 if(checkReminder(todate) == true){
				 
				 rbell.setBackgroundResource(R.drawable.reminderbell);
				 Log.d("Date", "Bell");
			 }else{
				 //rbell.setBackgroundResource(R.drawable.bg_green);
				 //Log.d("Date", "Green");
			 }

			 
			   
			 Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"+ theyear);

			 if (day_color[1].equals("GREY")) {
				 gridcell.setTextColor(getResources()
						 .getColor(R.color.lightgray));
			 }
			 if (day_color[1].equals("WHITE")) {
				 gridcell.setTextColor(getResources().getColor(
						 R.color.lightgray02));
			 }
			 if (day_color[1].equals("BLUE")) {
				 gridcell.setTextColor(getResources().getColor(R.color.green));
			 }
			 return row;
		 }

		 @Override
		 public void onClick(View view) {
			 String date_month_year = (String) view.getTag();
			 Toast.makeText(getApplicationContext(), "" + date_month_year, Toast.LENGTH_LONG).show();
			 
			 this.displayThisDayReminder(date_month_year);
			 selectedDayMonthYearButton.setText("Selected: " + date_month_year);
			 Log.e("Selected date", date_month_year);
			 try {
				 Date parsedDate = dateFormatter.parse(date_month_year);
				 Log.d(tag, "Parsed Date: " + parsedDate.toString());

			 } catch (ParseException e) {
				 e.printStackTrace();
			 }
		 }

		 public int getCurrentDayOfMonth() {
			 return currentDayOfMonth;
		 }

		 private void setCurrentDayOfMonth(int currentDayOfMonth) {
			 this.currentDayOfMonth = currentDayOfMonth;
		 }

		 public void setCurrentWeekDay(int currentWeekDay) {
			 this.currentWeekDay = currentWeekDay;
		 }

		 public int getCurrentWeekDay() {
			 return currentWeekDay;
		 }
			public void displayThisDayReminder(String date){
				AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
				//dialog.setContentView(R.layout.todaycalendar);
				dialog.setCancelable(true);
		    	dialog.setTitle("Today's Reminder");
		    	
		    	    
		    	//Toast.makeText(getApplicationContext(), "Displaying reminders", Toast.LENGTH_LONG).show();
		    	Cursor cursor = dataBase.fetchAllReminderByDate(date);
		    	String[] columns = new String[] {"title", "date", "time", "description"};
		    	
		        int[] to = new int[] { R.id.Date, R.id.editText1, R.id.editText2, R.id.editDesc };
 
		    	ListView thisDayReminderList = (ListView) findViewById(R.id.thisDayReminder);
		    	
		    	startManagingCursor(cursor);
		        
		    		if (cursor!=null){
		    			startManagingCursor(cursor);
		    			SimpleCursorAdapter newAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.thisdayreminder_row, cursor, columns, to);
		    			//thisDayReminderList.setAdapter(newAdapter);
		    			dialog.setAdapter(newAdapter, this);
		    		}
		    		
		    		if(cursor.getCount() > 0){
		    			AlertDialog datePicker = dialog.create();
		    			WindowManager.LayoutParams wmlp = datePicker.getWindow().getAttributes();

				    	wmlp.gravity = Gravity.TOP;
		    			datePicker.show();
		    		}else{
		    			Toast.makeText(getApplicationContext(), "No reminders for this day.", Toast.LENGTH_SHORT).show();
		    		}	
		   }

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), ""+arg1, Toast.LENGTH_SHORT).show();
			}
	}

	        


}