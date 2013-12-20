package com.example.prox;

import java.util.ArrayList;
import java.util.List;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
	// View rootView;
	
	GridView gridView;
	ArrayList<Item> gridArray = new ArrayList<Item>();
	CustomGridViewAdapter customGridAdapter;
	Boolean isInternetPresent = false;
	    // Connection detector class
	InternetDetector internetdetected;
	    
	
	public StoreFragment(){}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getActivity().getMenuInflater().inflate(R.menu.actionbar, menu);
	 
		return true;
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
      
        /*
       //set grid view item
        Bitmap homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.userguide);
        Bitmap userIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.bookcover);

 
        
         //Get data from parse.com  

    	ParseQuery<ParseObject> query = ParseQuery.getQuery("ebook");
    	query.findInBackground(new FindCallback<ParseObject>() {
			@SuppressLint("NewApi")
			@Override
			public void done(List<ParseObject> ebookslist, ParseException e) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(),"Checking internet connection...", Toast.LENGTH_LONG).show(); 
				if (e == null) {
	    	    	Log.d("ebooks", "Found " + ebookslist.size() + " ebooks");
	    	    	//Toast.makeText(getActivity(), "Ret", Toast.LENGTH_LONG).show(); 

		    	      String mybooktitle, intent, uri;
		    	      for(ParseObject userbooks : ebookslist) {
		    	    	  mybooktitle=(String) userbooks.get("title");
		    	    	  //intent = (String) userbooks.get("filename");
		    	    	  uri =  (String) userbooks.get("filename");
		    	    	  Log.d("ebooks", "Retrieved " + mybooktitle);
		    	    	  gridArray.add(new Item(mybooktitle,uri));
		              }
		    	      
		    	      gridView = (GridView) rootView.findViewById(R.id.gridview);
		    	      customGridAdapter = new CustomGridViewAdapter(getActivity(), R.layout.row_grid, gridArray);
		    	      gridView.setAdapter(customGridAdapter);
	    	      
	    	    } else {
	    	      // something went wrong
	    	    	Toast.makeText(getActivity(),"Not connected to a newtwork. Please check your internet connection.", Toast.LENGTH_LONG).show(); 
	    	    	Log.d("ebooks error", "Error");
	    	    }
			}
    	});
     
    	//END get data 
    	
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        customGridAdapter = new CustomGridViewAdapter(getActivity(), R.layout.row_grid, gridArray);
        gridView.setAdapter(customGridAdapter);
        
        final String[] ebook = null;
        
        gridView.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick(AdapterView parent,View v, int position, long id){	
	
	 			//openBook(position);
        		Item ebook =gridArray.get(position);
        		Intent bookdetails = new Intent(getActivity(), Download.class);
        		bookdetails.putExtra("filename", "http://files.parse.com/afc311a3-01af-4e45-ad6a-4ea2f171e17a/86d3d88b-17f4-4eaa-aac0-5846a636c3a7-book.pdf");
        		bookdetails.putExtra("title", ebook.getText());
        		//bookdetails.putExtra("filename", ebook.getText());
        		bookdetails.putExtra("id",position);
        		
        		Toast.makeText(getActivity(),"Ebook " +  position + " selected", Toast.LENGTH_SHORT).show();
                startActivity(bookdetails);
                
	 			
		 	}
		 });
       */
        return rootView;
    }
	
 
}
