package com.example.prox;


import com.example.prox.adapter.EbookDatabaseAdapter;
import com.example.prox.adapter.NavDrawerListAdapter;
import com.example.prox.model.NavDrawerItem;
import com.example.prox.note.NotesDbAdapter;
import com.example.prox.reminder.ReminderDatabaseAdapter;
import com.parse.Parse;
import com.parse.ParseUser;
import com.radaee.reader.R;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
 
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;


public class MenuActivity extends Activity{

	Button btnLogOut,btnStore,btnNote;
	MainActivity mainact;
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		
		EbookDatabaseAdapter ebookDatabaseAdapter = new EbookDatabaseAdapter(MenuActivity.this);
		ebookDatabaseAdapter.open();
		
		NotesDbAdapter noteDatabaseAdapter = new NotesDbAdapter(MenuActivity.this);
		noteDatabaseAdapter.open();
		
		ReminderDatabaseAdapter reminderDatabaseAdapter = new ReminderDatabaseAdapter(MenuActivity.this);
		reminderDatabaseAdapter.open();
        
		Parse.initialize(this, "x9n6KdzqtROdKDXDYF1n5AEoZLZKOih8rIzcbPVP", "JkqOqaHmRCA35t9xTtyoiofgG3IO7E6b82QIIHbF"); 
		
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		Log.d("ProX User", currentUser.getObjectId());
		
		if (currentUser != null) {
		  // do stuff with the user
			
			mTitle = mDrawerTitle = getTitle();

			// load slide menu items
			navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

			// nav drawer icons from resources
			navMenuIcons = getResources()
					.obtainTypedArray(R.array.nav_drawer_icons);

			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

			navDrawerItems = new ArrayList<NavDrawerItem>();

			// adding nav drawer items to array
			// Home
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
			// Reminder
			String totalReminders = Integer.toString(reminderDatabaseAdapter.countReminders());
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1), true, totalReminders));
			// Ereader
			String totalBooks = Integer.toString(ebookDatabaseAdapter.countEbooks());
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1), true, totalBooks));
			// NOtes
			String totalNotes = Integer.toString(noteDatabaseAdapter.countNotes());
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, totalNotes));
			// Store
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
			// Logout
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
			

			// Recycle the typed array
			navMenuIcons.recycle();

			mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

			// setting the nav drawer list adapter
			adapter = new NavDrawerListAdapter(getApplicationContext(),
					navDrawerItems);
			mDrawerList.setAdapter(adapter);

			// enabling action bar app icon and behaving it as toggle button
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);

			mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
					R.drawable.ic_drawer, //nav menu toggle icon
					R.string.app_name, // nav drawer open - description for accessibility
					R.string.app_name // nav drawer close - description for accessibility
			) {
				public void onDrawerClosed(View view) {
					getActionBar().setTitle(mTitle);
					// calling onPrepareOptionsMenu() to show action bar icons
					invalidateOptionsMenu();
				}

				public void onDrawerOpened(View drawerView) {
					getActionBar().setTitle(mDrawerTitle);
					// calling onPrepareOptionsMenu() to hide action bar icons
					invalidateOptionsMenu();
				}
			};
			mDrawerLayout.setDrawerListener(mDrawerToggle);

			if (savedInstanceState == null) {
				// on first time display view for first nav item
				displayView(0);
			}
			
		} else {
			Intent mainactIntent=new Intent(getApplicationContext(),MainActivity.class);
			startActivity(mainactIntent);
		}
		
		

		
		
		
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ereader_actionbar, menu);

  
		return true;
	}
 
 


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
 
		 case R.id.action_ebookstore:  
	    	  	Intent i = new Intent(this, SearchActivity.class);
	    	  	startActivity(i);
	    	  	break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		//menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		android.app.Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new ReminderFragment();
			break;
		case 2:
			fragment = new EbooksFragment();
			break;
		case 3:
			fragment = new NotesFragment();
			break;
		case 4:
			fragment = new StoreFragment();
			break;
		case 5:
			fragment = new LogoutFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			 
			android.app.FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
 
			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	public void logOut(){
		//SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
		
		Toast.makeText(MenuActivity.this, "Logging out..", Toast.LENGTH_LONG).show(); 
		
		
		//pref.edit().remove("email").commit();
		
		/// Create Intent for SignUpActivity  and Start The Activity
		Intent mainactIntent=new Intent(getApplicationContext(),MainActivity.class);
		startActivity(mainactIntent);
	}
	
	    
	/*
    public void onClick(View v){
        // Take appropriate action for each action item click
        switch (v.getId()) {
        case R.id.btnNote:
            // search action
        	Download();
        	break;
        case R.id.buttonStore:
            // store
        	//goToBookShelf();
        	//goToStore();
        	goToGrid();
        	//forgotPassword();
        	break;
        case R.id.buttonLogOut:
            // location found
        	logOut();
        	break;
        }
    }
	
	public void goToStore(){
		/// Create Intent for StoreActivity  
		Intent gotostoreIntent=new Intent(getApplicationContext(),StoreActivity.class);
		startActivity(gotostoreIntent);
	}
	
	public void goToBookShelf(){
		/// Create Intent for StoreActivity  
		Intent bookviewIntent=new Intent(getApplicationContext(),BookView.class);
		startActivity(bookviewIntent);
	}
	
	public void forgotPassword(){
		/// Create Intent for StoreActivity  
		Intent forgotPasswordIntent=new Intent(getApplicationContext(),ForgotPassword.class);
		startActivity(forgotPasswordIntent);
	}
	
	public void goToGrid(){
		/// Create Intent for Grid Activity  
		Intent gridIntent=new Intent(getApplicationContext(),Grid.class);
		startActivity(gridIntent);
	}
	
	public void Download(){
		/// Create Intent for StoreActivity  
		Intent dIntent=new Intent(getApplicationContext(),Download.class);
		startActivity(dIntent);
	}
	
	
	public void logOut(){
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
		
		Toast.makeText(MenuActivity.this, "Logging out..", Toast.LENGTH_LONG).show(); 
		
		
		pref.edit().remove("email").commit();
		
		/// Create Intent for SignUpActivity  and Start The Activity
		Intent mainactIntent=new Intent(getApplicationContext(),MainActivity.class);
		startActivity(mainactIntent);
	}
	

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	*/
}
