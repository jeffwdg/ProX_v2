package com.example.prox;

import java.util.ArrayList;
import java.util.List;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.radaee.reader.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BestBookView extends Activity {
		
		GridView gridView;
		ArrayList<Ebook> gridArray = new ArrayList<Ebook>();
		MyGridViewAdapter customGridAdapter;
		
		
		public boolean onCreateOptionsMenu(Menu menu) {
			 getMenuInflater().inflate(R.menu.ereader_actionbar, menu);
			return true;
		}
		
		public boolean onOptionsItemSelected(MenuItem item) {
		      //Handle item selection
		      switch (item.getItemId()){
		      case R.id.action_ebookstore:  
		    	  	Intent i = new Intent(this, SearchActivity.class);
		    	  	startActivity(i);
		      	break;
		      }
		      return false;
		}
		
		
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.storeview);
			Bitmap userIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.bookcover);

				//Get data from parse.com  
		    	ParseQuery<ParseObject> query = ParseQuery.getQuery("ebook");
		    	query.whereEqualTo("categoryID", "9");
		    	
		    	query.findInBackground(new FindCallback<ParseObject>() {
					@SuppressLint("NewApi")
					@Override
					public void done(List<ParseObject> ebookslist, ParseException e) {
						
						Toast.makeText(getApplicationContext(),"Loading...", Toast.LENGTH_LONG).show(); 
						
						if (e == null) {
			    	    	Log.d("ebooks", "Found " + ebookslist.size() + " ebooks");
			    	    	Toast.makeText(getApplicationContext(), "Displaying..", Toast.LENGTH_LONG).show();

				    	      String mybooktitle, myebookID = null, cover, filename, author,ISBN, bookstatus, ebookID;
				    	      
				    	      for(ParseObject userbooks : ebookslist) {
				    	    	  
				    	    	  mybooktitle=(String) userbooks.get("title");
				    	    	  filename = (String) userbooks.get("filename");
				    	    	  cover =  (String) userbooks.get("cover");
				    	    	  author = (String) userbooks.get("author");	
				    	    	  ISBN =  (String) userbooks.get("ISBN");
				    	    	  bookstatus =  (String) userbooks.get("status");
				    	    	  ebookID =  userbooks.getObjectId();
				    	    	  
				    	    	  Ebook ebook = new Ebook();
				    	    	  ebook.setFilename(filename);
				    	    	  ebook.setAuthor(author);
				    	    	  ebook.setCover(cover);
				    	    	  ebook.setID(ebookID);
				    	    	  ebook.setISBN(ISBN);
				    	    	  ebook.setTitle(mybooktitle);
				    	    	  
				    	    	  userbooks.put("ebookID", ebookID);
				    	    	  Log.d("Parse Data", "Retrieved ebook details " + cover + filename +author +mybooktitle+ ISBN +bookstatus);
				    	    	  gridArray.add(ebook);

				              }
				    	      
				    	    GridView gridView = (GridView) findViewById(R.id.newstoregridview);
				  	        customGridAdapter = new MyGridViewAdapter(BestBookView.this, R.layout.row_grid, gridArray);
				  	        gridView.setAdapter(customGridAdapter); 
				    	      
			    	    } else {
			    	    	Log.d("Ebooks Error", "Error");
			    	    }
					}
		    	});

		    	
		        GridView gridView = (GridView) findViewById(R.id.newstoregridview);
		        customGridAdapter = new MyGridViewAdapter(this, R.layout.row_grid, gridArray);
		        gridView.setAdapter(customGridAdapter);
		        
		        
		        gridView.setOnItemClickListener(new OnItemClickListener(){
		        	@SuppressLint("NewApi")
					public void onItemClick(AdapterView parent,View v, int position, long id){	
			
		        		Ebook ebook = new Ebook();
		        		ebook = gridArray.get(position);
 	
		        		Intent bookdetails = new Intent(getApplicationContext(), StoreBookDetails.class);

		        		bookdetails.putExtra("title", ebook.getTitle());
		        		bookdetails.putExtra("filename", ebook.getFilename());
		        		bookdetails.putExtra("author", ebook.getAuthor());
		        		bookdetails.putExtra("cover", ebook.getCover());
		    	    	bookdetails.putExtra("ebookID", ebook.getID());
		        		bookdetails.putExtra("id",position);
		        		bookdetails.putExtra("ISBN",ebook.getISBN());
		        		Log.d("ebooks", "Retrieved bookcover " +ebook.getCover());
		                startActivity(bookdetails);
				 	}
				 });
		        //return rootView;
		    }
}
