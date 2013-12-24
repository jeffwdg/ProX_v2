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
import android.net.Uri;
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

	public class LatestBookView extends Activity {
		
		GridView gridView;
		ArrayList<Ebook> gridArray = new ArrayList<Ebook>();
		MyGridViewAdapter customGridAdapter;
		
		
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
		    	query.whereEqualTo("categoryID", "8");
		    	query.findInBackground(new FindCallback<ParseObject>() {
					@SuppressLint("NewApi")
					@Override
					public void done(List<ParseObject> ebookslist, ParseException e) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(),"Loading...", Toast.LENGTH_LONG).show(); 
						if (e == null) {
			    	    	Log.d("ebooks", "Found " + ebookslist.size() + " ebooks");
			    	    	Toast.makeText(getApplicationContext(), "Displaying..", Toast.LENGTH_LONG).show();

				    	      String mybooktitle, myebookID = null, cover, filename, author,ISBN, category,bookstatus, ebookID;
				    	      for(ParseObject userbooks : ebookslist) {
				    	    	  
				    	    	  
				    	    	  mybooktitle=(String) userbooks.get("title");
				    	    	  filename = (String) userbooks.get("filename");
				    	    	  cover =  (String) userbooks.get("title");
				    	    	  author = (String) userbooks.get("author");	
				    	    	  ISBN =  (String) userbooks.get("ISBN");
				    	    	  bookstatus =  (String) userbooks.get("status");
				    	    	  ebookID =  userbooks.getObjectId();
				    	    	  category = (String) userbooks.get("category");
				    	    	  
				    	    	  Ebook ebook = new Ebook();
				    	    	  ebook.setFilename(filename);
				    	    	  ebook.setAuthor(author);
				    	    	  ebook.setCover(cover);
				    	    	  ebook.setID(ebookID);
				    	    	  ebook.setISBN(ISBN);
				    	    	  ebook.setTitle(mybooktitle);
				    	    	  ebook.setStatus(bookstatus);
				    	    	  ebook.setCategory(category);
				    	    	  
				    	    	  userbooks.put("ebookID", ebookID);
				    	    	  Log.d("Parse Data", "Retrieved ebook details " + cover + filename +author +mybooktitle+ ISBN +bookstatus);
				    	    	  gridArray.add(ebook);

				              }
				    	      
				    	     GridView gridView = (GridView) findViewById(R.id.newstoregridview);
				  	        customGridAdapter = new MyGridViewAdapter(LatestBookView.this, R.layout.row_grid, gridArray);
				  	        gridView.setAdapter(customGridAdapter); 
				    	      
			    	    } else {
			    	      // something went wrong
			    	    	//Toast.makeText(this,"Not connected to a newtwork. Please check your internet connection.", Toast.LENGTH_LONG).show(); 
			    	    	Log.d("ebooks error", "Error");
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
		    	    	bookdetails.putExtra("status", ebook.getStatus());
		    	    	bookdetails.putExtra("ISBN", ebook.getISBN());
		    	    	bookdetails.putExtra("category", ebook.getCategory());
		    	    	
		        		bookdetails.putExtra("id",position);
		        		Log.d("ebooks", "Retrieved bookcover " +ebook.getFilename());
		        		//Toast.makeText(this,"Ebook " +  position + " selected", Toast.LENGTH_SHORT).show();
		                startActivity(bookdetails);
		                
			 			
				 	}
				 });
		       
	 
		        //return rootView;
		        
		    }
	    
	    
	}
