package com.example.prox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.prox.adapter.EbookAdapter;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
 
public class BookSearch extends Activity {
 
    private TextView txtQuery;
    int res=0;
    Ebook results;
    Ebook ebooks;
    ArrayList<Ebook> resBook = new ArrayList<Ebook>();
    ArrayList<Ebook> bookresults = new ArrayList<Ebook>();
    final ArrayList<Ebook> ebook = new ArrayList<Ebook>();
    private EbookAdapter e_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookstore_search_results);
 
        // get the action bar
        ActionBar actionBar = getActionBar();

        // Enabling Back navigation on Action Bar icon
        actionBar.setDisplayHomeAsUpEnabled(true);
 
        txtQuery = (TextView) findViewById(R.id.txtQuery);
 
          handleIntent(getIntent());
        
        	
	
	        
    }
 
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
 
    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {
    	 
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String searchquery = intent.getStringExtra(SearchManager.QUERY);
          
            
            
 
            // Create an ArrayAdapter, that will actually make the Strings above
            // appear in the ListView
            //this.setListAdapter(new ArrayAdapter<String>(this,
             //       android.R.layout.simple_list_item_checked, names));
            
            /**
             * Use this query to display search results like 
             * 1. Getting the data from SQLite and showing in listview 
             * 2. Making webrequest and displaying the data 
             * For now we just display the query only
             */
            
            txtQuery.setText("Searching: " + searchquery);
            
            Parse.initialize(this, "x9n6KdzqtROdKDXDYF1n5AEoZLZKOih8rIzcbPVP", "JkqOqaHmRCA35t9xTtyoiofgG3IO7E6b82QIIHbF");
            
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ebook");
	    	query.whereStartsWith("title", searchquery);
	    	query.findInBackground(new FindCallback<ParseObject>() {
				@SuppressLint("NewApi")
				@Override
				public void done(List<ParseObject> ebookslist, ParseException e) {
					// TODO Auto-generated method stub
					res =  ebookslist.size();
					Toast.makeText(getApplicationContext(),"Searching...", Toast.LENGTH_LONG).show(); 
					Log.d("ebooks", "Found " + res + " ebooks");
					txtQuery.setText( res +" result(s) found for " + searchquery);
					
					
					if (e == null) {
		    	    	
		    	    	if(res > 0)
		    	    	{
		    	    		//results = new 
		    	    		Toast.makeText(getApplicationContext(), "Displaying..", Toast.LENGTH_LONG).show();
		    	    		int i=0;
			    	       String mybooktitle, myebookID = null, cover, filename, author,ISBN, category,bookstatus, ebookID;
			    	       for(ParseObject userbooks : ebookslist) {
			    	    	  
			    	    	  
			    	    	  mybooktitle=(String) userbooks.get("title");
			    	    	  filename = (String) userbooks.get("filename");
			    	    	  cover =  (String) userbooks.get("cover");
			    	    	  author = (String) userbooks.get("author");	
			    	    	  ISBN =  (String) userbooks.get("ISBN");
			    	    	  bookstatus =  (String) userbooks.get("status");
			    	    	  ebookID =  userbooks.getObjectId();
			    	    	  category = (String) userbooks.get("category");
			    	    	 
			    	    	  ebooks = new Ebook();
			    	    	  ebooks.setFilename(filename);
			    	    	  ebooks.setAuthor(author);
			    	    	  ebooks.setCover(cover);
			    	    	  ebooks.setID(ebookID);
			    	    	  ebooks.setISBN(ISBN);
			    	    	  ebooks.setTitle(mybooktitle);
			    	    	  ebooks.setStatus(bookstatus);
			    	    	  ebooks.setCategory(category);
			    	    	   
			    	    	  //resBook.add(ebooks);
			    	    	  resBook.add(i, ebooks);
			    	    	  TextView mTitle = (TextView) findViewById(R.id.mTitle);
			    	    	  TextView mAuthor = (TextView) findViewById(R.id.mAuthor);
			    	    	  //mTitle.setText(mybooktitle);
			    	    	  //mAuthor.setText(author);
			    	    	  //results[i] =  new Ebook(object);
			    	    	  i++;
			    	    	  userbooks.put("ebookID", ebookID);
			    	    	  Log.d("Parse Data", "Retrieved ebook details " + cover + filename +author +mybooktitle+ ISBN +bookstatus);
			              }
			    	      
			    	      ListView listview = (ListView) findViewById(R.id.listresults);
			    	      //e_adapter = new EbookAdapter(BookSearch.this, R.layout.ebooks_row, ebook);
			    	      
			    	      
			    	      final EbookAdapter  adapter = new EbookAdapter(BookSearch.this, R.layout.ebooks_row, R.id.mTitle, resBook);

			              listview.setAdapter(adapter); 
			              
			              // And if you want selection feedback:
			              listview.setOnItemClickListener(new OnItemClickListener() {
			                  @Override
			                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			                      //Do whatever you want with the selected item
			                	  
			                	  Ebook storeebook = new Ebook();
			                	  	storeebook = resBook.get(position);
					        		Intent bookdetails = new Intent(getApplicationContext(), StoreBookDetails.class);

					        		bookdetails.putExtra("title", storeebook.getTitle());
					        		bookdetails.putExtra("filename", storeebook.getFilename());
					        		bookdetails.putExtra("author", storeebook.getAuthor());
					        		bookdetails.putExtra("cover", storeebook.getCover());
					    	    	bookdetails.putExtra("ebookID", storeebook.getID());
					    	    	bookdetails.putExtra("status", storeebook.getStatus());
					    	    	bookdetails.putExtra("ISBN", storeebook.getISBN());
					    	    	bookdetails.putExtra("category", storeebook.getCategory());
	
					        		bookdetails.putExtra("id",position);
					                startActivity(bookdetails);
					                
			                      Log.d("Test", storeebook.getTitle() + " has been selected!");
			                  }
			              });
			              
			              
		    	    	}else{
		    	    		txtQuery.setText("No results found for " + searchquery);
		    	    		Toast.makeText(getApplicationContext(),"No results found... try another", Toast.LENGTH_LONG).show(); 
		    	    	}
		    	    	
		    	    	
		    	    } else {
		    	  
		    	    	 Log.d("ebooks error", "Error");
		    	    }
					
					 
				}
				
	    	});
	    	       	
	    	 
            Log.d("Store Search","Searching..."+ searchquery);
            
             
        }
 
  
    }
    
    
 
    
  
 
    
}
