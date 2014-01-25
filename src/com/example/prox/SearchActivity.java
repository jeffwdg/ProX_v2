package com.example.prox;

import java.util.ArrayList;

import com.radaee.reader.R;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnNavigationListener{

	  Boolean isInternetPresent = false;
	    // Connection detector class
	    InternetDetector internetdetected;
	    Utilities util = new Utilities(); 
	    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
 
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
 
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        internetdetected = new InternetDetector(getApplicationContext());
        isInternetPresent = internetdetected.isNetworkAvailable();
        ActionBar ab = getActionBar();
	    ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Store Search");
        ab.setIcon(R.drawable.ebookstore);
        
        ab.setDisplayShowTitleEnabled(false);
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        ArrayList<String> categoryList = new ArrayList<String>();
        categoryList.add("By Title");
        categoryList.add("By Author");
        categoryList.add("By ISBN");
 
        ArrayAdapter<String> aAdpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, categoryList);
        ab.setListNavigationCallbacks(aAdpt,(OnNavigationListener) this);

        
        Log.d("Internet detecting..", "Internet present =" + isInternetPresent);
        
        if(isInternetPresent == true){
        	setContentView(R.layout.bookstore_search_results);
        }else{
        	util.showAlertDialog(getApplicationContext(), "Network Error", "Please check your internet connection.", false);
        }
        
        
        
    }
 
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// TODO Auto-generated method stub
		return false;
	}

 
	
}
