package com.example.prox;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ClipData.Item;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FreeBookView  extends Activity {
	
	GridView gridView;
	ArrayList<Item> gridArray = new ArrayList<Item>();
	CustomGridViewAdapter customGridAdapter;
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		 getMenuInflater().inflate(R.menu.actionbar, menu);
	 
		return true;
	}
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//sLayoutInflater inflater = (LayoutInflater)FreeBookView.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//View rootView = inflater.inflate(R.layout.storeview, null);
 
		setContentView(R.layout.storeview);
		Bitmap userIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.bookcover);

	         //Get data from parse.com  
		 
	    	ParseQuery<ParseObject> query = ParseQuery.getQuery("ebook");
	    	query.whereEqualTo("categoryID", "0");
	    	query.findInBackground(new FindCallback<ParseObject>() {
				@SuppressLint("NewApi")
				@Override
				public void done(List<ParseObject> ebookslist, ParseException e) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(),"Loading...", Toast.LENGTH_LONG).show(); 
					if (e == null) {
		    	    	Log.d("ebooks", "Found " + ebookslist.size() + " ebooks");
		    	    	Toast.makeText(getApplicationContext(), "Displaying..", Toast.LENGTH_LONG).show();

			    	      String mybooktitle, intent, uri;
			    	      for(ParseObject userbooks : ebookslist) {
			    	    	  mybooktitle=(String) userbooks.get("title");
			    	    	  //intent = (String) userbooks.get("filename");
			    	    	  uri =  (String) userbooks.get("filename");
			    	    	  Log.d("ebooks", "Retrieved " + mybooktitle);
			    	    	  gridArray.add(new Item(mybooktitle,uri));
			              }
			    	      
			    	     GridView gridView = (GridView) findViewById(R.id.newstoregridview);
			  	        customGridAdapter = new CustomGridViewAdapter(FreeBookView.this, R.layout.row_grid, gridArray);
			  	        gridView.setAdapter(customGridAdapter); 
			    	      
		    	    } else {
		    	      // something went wrong
		    	    	//Toast.makeText(this,"Not connected to a newtwork. Please check your internet connection.", Toast.LENGTH_LONG).show(); 
		    	    	Log.d("ebooks error", "Error");
		    	    }
				}
	    	});

	    	
	        GridView gridView = (GridView) findViewById(R.id.newstoregridview);
	        customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, gridArray);
	        gridView.setAdapter(customGridAdapter);
	        
	        final String[] ebook = null;
	        
	        gridView.setOnItemClickListener(new OnItemClickListener(){
	        	public void onItemClick(AdapterView parent,View v, int position, long id){	
		
		 			//openBook(position);
	        		Item ebook =gridArray.get(position);
	        		Intent bookdetails = new Intent(getApplicationContext(), StoreBookDetails.class);
	        		bookdetails.putExtra("filename", "http://files.parse.com/afc311a3-01af-4e45-ad6a-4ea2f171e17a/86d3d88b-17f4-4eaa-aac0-5846a636c3a7-book.pdf");
	        		bookdetails.putExtra("title", ebook.getText());
	        		//bookdetails.putExtra("filename", ebook.getText());
	        		bookdetails.putExtra("id",position);
	        		
	        		//Toast.makeText(this,"Ebook " +  position + " selected", Toast.LENGTH_SHORT).show();
	                startActivity(bookdetails);
	                
		 			
			 	}
			 });
	       
 
	        //return rootView;
	        
	    }
    
    
}
