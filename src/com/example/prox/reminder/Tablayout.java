package com.example.prox.reminder;

import com.radaee.reader.R;

import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Tablayout extends TabActivity{

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder_editview);

		//ActionBar actionBar = getActionBar();

		// Enabling Up / Back navigation
		//actionBar.setDisplayHomeAsUpEnabled(true);

		//declaring for TabHost
		TabHost tabHost = getTabHost();
		
		Intent i = getIntent();
		String curtab = i.getExtras().getString("activetab");
		if(TextUtils.isEmpty(curtab) || TextUtils.equals(curtab, null)){
			curtab = "1";
		}
		
		
		
		// Tab for this last reminder
		TabSpec lastspec = tabHost.newTabSpec("Last Month");        
		lastspec.setIndicator("Last Month", getResources().getDrawable(R.drawable.lastmonth));
		Intent lastmonth = new Intent(this, lastmonthcalendar.class);
		lastspec.setContent(lastmonth);

		// Tab for this month reminder
		TabSpec todayspec = tabHost.newTabSpec("This Month");
		todayspec.setIndicator("This Month", getResources().getDrawable(R.drawable.thismonth));
		Intent today = new Intent(this, ReminderThisMonth.class);
		todayspec.setContent(today);

		// Tab for this next reminder
		TabSpec nextspec = tabHost.newTabSpec("Next Month");
		nextspec.setIndicator("Next Month", getResources().getDrawable(R.drawable.nextmonth));
		Intent nextmonth = new Intent(this, nextmonthcalendar.class);
		nextspec.setContent(nextmonth);

		// Adding all TabSpec to TabHost
		tabHost.addTab(lastspec); // Adding songs tab
		tabHost.addTab(todayspec); // Adding photos tab
		tabHost.addTab(nextspec); // Adding videos tab
		
		tabHost.setCurrentTab(Integer.parseInt(curtab));

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

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	private void AddReminder() {
		Intent i = new Intent(Tablayout.this, ReminderAdd.class);
		startActivity(i);
	}

}

