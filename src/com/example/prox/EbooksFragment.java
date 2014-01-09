package com.example.prox;

import java.util.ArrayList;
import java.util.List;
 
import android.content.Context;
import android.content.SharedPreferences;
import android.content.ClipData.Item;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prox.adapter.EbookDatabaseAdapter;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.radaee.reader.R;

public class EbooksFragment extends Fragment {
	
	GridView gridView;
	ArrayList<Item> gridArray = new ArrayList<Item>();
	CustomGridViewAdapter customGridAdapter;
	
	 
	private EbookDatabaseAdapter mDbHelper;
	
	public EbooksFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
 
        final View rootView = inflater.inflate(R.layout.fragment_ebooks, container, false);
        mDbHelper = new EbookDatabaseAdapter(getActivity());
		mDbHelper.open();
		
		ActionBar actionBar = getActivity().getActionBar();
        
        // Enabling Up / Back navigation
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
		
		Intent intent = new Intent(getActivity(), UserEbookList.class);
		startActivity(intent);
		
		
		/*
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(actionBar.getThemedContext(),android.R.layout.simple_list_item_1,ebookViewCategory);
		
		
        //Parse.initialize(getActivity(), "x9n6KdzqtROdKDXDYF1n5AEoZLZKOih8rIzcbPVP", "JkqOqaHmRCA35t9xTtyoiofgG3IO7E6b82QIIHbF");
        
        Bitmap homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.userguide);
        Bitmap userIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.bookcover);
        
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 1); // 0 - for private mode
	    Editor editor = pref.edit();
	   
	    String objectId = pref.getString("objectId", null);
	    Log.d("ProX Ebooks", "Fetching User Ebooks of " +objectId);
        //Get user ebooks from parse.com  
	    Toast.makeText(getActivity(),"Loading...", 6000).show();
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("userEbooks");
    	query.whereEqualTo("userID", objectId);
    	query.findInBackground(new FindCallback<ParseObject>() {
			@SuppressLint("NewApi")
			@Override
			public void done(List<ParseObject> userebookslist, ParseException e) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(),"Checking internet connection...", Toast.LENGTH_LONG).show();
				Log.d("ProX Ebooks", "Fetching User Ebooks");
				
				if (e == null) {
					Log.d("ProX Ebooks", "Found " + userebookslist.size() + " ebooks");
					String objectId, ebookID, uri;
		    	      
		    	      for(ParseObject userbooks : userebookslist) {
		    	    	  objectId = userbooks.getObjectId();
		    	    	  ebookID=(String) userbooks.get("ebookID");
		    	    	  //objectId=(String) userbooks.get("objectId");
		    	    	  
		    	    	  Log.d("ProX Ebooks", "Retrieved " + objectId);
		    	    	  gridArray.add(new Item(ebookID, objectId, null, null));
		              }
		    	      
		    	      GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
		    	      customGridAdapter = new CustomGridViewAdapter(getActivity(), R.layout.row_grid, gridArray);
		    	      gridView.setAdapter(customGridAdapter);
 
	    	    } else {
	    	      // something went wrong
	    	    	Toast.makeText(getActivity(),"Not connected to a newtwork. Please check your internet connection.", Toast.LENGTH_LONG).show(); 
	    	    	Log.d("ProX Ebooks", "Error");
	    	    }
			}
    	});
    	//END get user ebooks
    	
    	
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        customGridAdapter = new CustomGridViewAdapter(getActivity(), R.layout.row_grid, gridArray);
        gridView.setAdapter(customGridAdapter);
        
        final String[] ebook = null;
        
        gridView.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick(AdapterView parent,View v, int position, long id){	

        		Item ebook =gridArray.get(position);
        		Intent bookdetails = new Intent(getActivity(), StoreBookDetails.class);
        		//bookdetails.putExtra("objectId", "http://files.parse.com/afc311a3-01af-4e45-ad6a-4ea2f171e17a/86d3d88b-17f4-4eaa-aac0-5846a636c3a7-book.pdf");
        		bookdetails.putExtra("ebookID", ebook.getText());
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
