package com.example.prox;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

public class SearchActivity extends Activity{

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
        
        Log.d("Internet detecting..", "Internet present =" + isInternetPresent);
        
        if(isInternetPresent == true){
        	setContentView(R.layout.bookstore_search_results);
        }else{
        	util.showAlertDialog(getApplicationContext(), "Network Error", "Please check your internet cconnection.", false);
        }
        
        
        
    }
	
	
}
