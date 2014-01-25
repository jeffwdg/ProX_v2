package com.example.prox;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.radaee.reader.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;

import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.app.LocalActivityManager;


public class StoreFragment extends Fragment{
	private WebView webView;
	private TabHost mTabHost;
	Utilities util = new Utilities();
	GridView gridView;
	ArrayList<Item> gridArray = new ArrayList<Item>();
	CustomGridViewAdapter customGridAdapter;
	Boolean isInternetPresent = false;
	InternetDetector internetdetected;
	    
	
	public StoreFragment(){}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getActivity().getMenuInflater().inflate(R.menu.ereader_actionbar, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	      // Handle item selection
	      switch (item.getItemId()) {
	      case R.id.action_ebookstore:  
	    	  	Intent i = new Intent(getActivity(), SearchActivity.class);
	    	  	startActivity(i);
	      	break;

	      }
	      return false;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
 
		
        final View rootView = inflater.inflate(R.layout.fragment_store, container, false);
        
        internetdetected = new InternetDetector(getActivity().getApplicationContext());
	
		isInternetPresent = internetdetected.isNetworkAvailable();
		
		if(isInternetPresent == true){
			
		}else{
			util.showAlertDialog(getActivity(), "Network Error", "Please check your internet connection.", false);
		}
      
        mTabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(getActivity(), false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        mTabHost.setup(mLocalActivityManager);//very important to call this
        
        TabHost.TabSpec free = mTabHost.newTabSpec("Free");
        free.setIndicator("Free", getResources().getDrawable(R.drawable.free_tab));
        Intent freeIntent = new Intent(getActivity(), FreeBookView.class);
        free.setContent(freeIntent);
        
        TabHost.TabSpec latest = mTabHost.newTabSpec("Latest");
        latest.setIndicator("Latest", getResources().getDrawable(R.drawable.new_tab));
        Intent latestIntent = new Intent(getActivity(), LatestBookView.class);
        latest.setContent(latestIntent);
        
        TabHost.TabSpec best = mTabHost.newTabSpec("Best Seller");
        best.setIndicator("Best Seller", getResources().getDrawable(R.drawable.best_tab));
        Intent bestIntent = new Intent(getActivity(), BestBookView.class);
        best.setContent(bestIntent);
        
        mTabHost.addTab(free);
        mTabHost.addTab(latest);
        mTabHost.addTab(best);
      
        return rootView;
    }
	
 
}
