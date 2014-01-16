package com.example.prox;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sf.andpdf.pdfviewer.PdfViewerActivity;

import com.example.prox.Download.DownloadFileFromURL;
import com.example.prox.adapter.EbookAdapter;
import com.example.prox.adapter.EbookDatabaseAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.radaee.reader.MyPDFOpen;
import com.radaee.reader.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

public class UserEbookList extends Activity {
	
  private EbookDatabaseAdapter datasource;
  ArrayList<Ebook> data = new ArrayList<Ebook>();
  TextView mTitle;
  private ProgressDialog pDialog;
  Boolean isInternetPresent = false;
  // Connection detector class
  InternetDetector internetdetected;
  Utilities util = new Utilities();
  String[] ebookViewCategory;
  Cursor  ebookCursor;
  // Progress dialog type (0 - for Horizontal progress bar)
  public final int progress_bar_type = 0; 
  
  @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ebook_list, menu);
		// Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
 
        return super.onCreateOptionsMenu(menu);

		//return true;
	}
  
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{    
	     switch (item.getItemId()) 
	     {        
	        case R.id.view_list: changeViewToList();     
	        	return true;        
	        default:            
	           return super.onOptionsItemSelected(item);    
	     }
	}
	
	public void changeViewToList(){
		Intent intent=new Intent(this, UserBookListView.class);
		startActivity(intent);
	}
	
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.userebooks_list);
    ActionBar actionBar = this.getActionBar();
	actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setTitle("My Books");
    actionBar.setIcon(R.drawable.books);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	
 
	// initialize parse
	Parse.initialize(this, "x9n6KdzqtROdKDXDYF1n5AEoZLZKOih8rIzcbPVP", "JkqOqaHmRCA35t9xTtyoiofgG3IO7E6b82QIIHbF");
	//Calling ParseAnalytics to see Analytics
	ParseAnalytics.trackAppOpened(getIntent());
	        
	Boolean isInternetPresent = false;
    // Connection detector class	
	
	InternetDetector internetdetected;
    setContentView(R.layout.grid);
    datasource = new EbookDatabaseAdapter(this);
    datasource.open();
      

    
    fillData();
    handleIntent(getIntent());
  }
  
  
  
  /*
   * Displays user ebook from local sqlite in a listview
   */
  
	private void fillData() {
        // Get all of the notes from the database and create the item list
       ebookCursor = datasource.fetchAllEbooks();

       
        String[] from = new String[] {EbookDatabaseAdapter.KEY_TITLE,EbookDatabaseAdapter.KEY_FILENAME,EbookDatabaseAdapter.KEY_COVER,EbookDatabaseAdapter.KEY_AUTHOR, EbookDatabaseAdapter.KEY_OBJECTID,EbookDatabaseAdapter.KEY_STATUS,EbookDatabaseAdapter.KEY_CATEGORY};
        int[] to = new int[] { R.id.mTitle ,R.id.mFilename, R.id.mCover,R.id.mAuthor, R.id.mObjectId};
        
        
        // Now create an array adapter and set it to display using our row
        @SuppressWarnings("deprecation")
		//SimpleCursorAdapter ebooks=new SimpleCursorAdapter(this, R.layout.ebooks_row, ebookCursor, from, to);
        	ArrayList<Ebook> gridArray = new ArrayList<Ebook>();
        	GridView grv = (GridView) findViewById(R.id.gridview);
        
        	if (ebookCursor != null) {
        	    startManagingCursor(ebookCursor);
        	    Ebook ebook = new Ebook();
        	    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.row_grid, ebookCursor, from, to);
        	    
        	    while (ebookCursor.moveToNext() ) {
 
        	        Ebook gebook = new Ebook();
	    	    	  gebook.setFilename(ebookCursor.getString(ebookCursor.getColumnIndex("filename")));
	    	    	  gebook.setAuthor(ebookCursor.getString(ebookCursor.getColumnIndex("author")));
	    	    	  gebook.setCover(ebookCursor.getString(ebookCursor.getColumnIndex("cover")));
	    	    	  gebook.setID(ebookCursor.getString(ebookCursor.getColumnIndex("objectId")));
	    	    	  gebook.setISBN(ebookCursor.getString(ebookCursor.getColumnIndex("ISBN")));
	    	    	  gebook.setTitle(ebookCursor.getString(ebookCursor.getColumnIndex("title")));
	    	    	  gebook.setStatus(ebookCursor.getString(ebookCursor.getColumnIndex("status")));
	    	    	  gebook.setCategory(ebookCursor.getString(ebookCursor.getColumnIndex("category")));
	    	    	  Log.d("Ebook Details", "Details " + ebookCursor.getString(ebookCursor.getColumnIndex("cover")));
	    	    	 
	    	    	  gridArray.add(gebook);
        	    }
        	    
 
        	      MyLocalGridViewAdapter customGridAdapter = new MyLocalGridViewAdapter(UserEbookList.this, R.layout.row_grid, gridArray);
	        	  grv.setAdapter(customGridAdapter);
        	    
        	     
        	}
        	
        	 stopManagingCursor(ebookCursor);
        	
        	 grv.setOnItemClickListener(new OnItemClickListener(){
        		 
		        	@SuppressLint("NewApi")
					public void onItemClick(AdapterView parent,View v, final int position, final long id){	
		        		ebookCursor.moveToPosition(position);
        	    		final String objectId = ebookCursor.getString(ebookCursor.getColumnIndex("objectId"));
        	    		final String title = ebookCursor.getString(ebookCursor.getColumnIndex("title"));
        	    		final String author = ebookCursor.getString(ebookCursor.getColumnIndex("author"));
        	    		final String ISBN = ebookCursor.getString(ebookCursor.getColumnIndex("ISBN"));
        	    		final String cover = ebookCursor.getString(ebookCursor.getColumnIndex("cover"));
        	    		final String filename = ebookCursor.getString(ebookCursor.getColumnIndex("filename"));
        	    		String status =  ebookCursor.getString(ebookCursor.getColumnIndex("status"));
        	    		final String category =  ebookCursor.getString(ebookCursor.getColumnIndex("category"));
        	    		
        	    		Log.d("Ebook Details", "Details " + status+filename+cover);
        	    		 
        	    		CharSequence[] items = {"View","Delete","Download"};
		        		
		        		if(status.equals("1")){
		        			 CharSequence[] newitems={"View","Delete"};
		        			 items = newitems;
		        			 Log.d("Ebook Details", "Not Downloaded" + status);
		        		}else{
		        			 Log.d("Ebook Details", "Downloaded" + status);
		        		} 
		        		
		        		
		        		
		        		AlertDialog.Builder builder3 =new AlertDialog.Builder(UserEbookList.this);
						builder3.setTitle("Select an action").setItems(items, new DialogInterface.OnClickListener() {

	        	    	@Override
	        	    	public void onClick(DialogInterface dialog, int which) {
	        	  
	        	    			switch(which){
	        	    			case 0: 
	        	    				openEbook(objectId, title);
	        	    				break;
	        	    			case 1:
	        	    				
	        	    				attemptDeleteMyEbook(objectId, title, filename);
	        	    				break;
	        	    			case 2:
	        	    				Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_LONG).show(); 
        	    					boolean downloadedFile = downloadEbook(objectId, filename);
        	    					String downloadedStatus = "1";
        	    					if(downloadedFile == true){ 
        	    						datasource.updateEntry(objectId, title,filename,author, ISBN, cover, downloadedStatus, category); 
        	    					}
        	    					break;
	        	    				
	        	    			}
	        	    	}
	        	    	});
	        	    	
	        	    	builder3.show();
		        	}
        	 });
			 
        	 
    
    	     
    }
 
	
	/*
	 *  This method opens the ebook
	 */
	
	public void openEbook(String objectId, String title){
		
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
	    Editor editor = pref.edit();
        String userFolderName = pref.getString("email", null);
        
		String ebookLocation = "data/data/com.radaee.reader/proxbooks/"+ userFolderName +"/";
		String ebookFile = objectId +".pdf";
		Toast.makeText(getApplicationContext(), "Opening.. " + title, Toast.LENGTH_LONG).show();
		
		File ebookLocal = new File(ebookLocation+ebookFile);
		
		if(ebookLocal.exists()){
			// Redirect screen to pdf viewer
			Intent intent = new Intent(UserEbookList.this, MyPDFOpen.class);
			intent.putExtra("ebookFile", ebookLocation+ebookFile);
			
			//Intent intent = new Intent(this, ProxReader.class);
			//intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME,ebookLocation+ebookFile);
			startActivity(intent);
		}else{
			Toast.makeText(getApplicationContext(), "File not found for " + title, Toast.LENGTH_LONG).show();
		}
		
	}
	
	
	
	/*
	 *  This method asks for confirmation from the user before deleting the book
	 */
	
	public void attemptDeleteMyEbook(final String objectId, final String title, final String filelocation){
		
	        AlertDialog.Builder builder = new AlertDialog.Builder(UserEbookList.this);
	        builder.setTitle("Delete book");
	        builder.setMessage("Are you sure you want to delete this book " + title +" in your account?");
	        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   if(true == deleteEbook(objectId,title,filelocation)){
	                		   Intent intent = getIntent();
	                		    finish();
	                		    startActivity(intent);
	                		   //Toast.makeText(getApplicationContext(), "Deleted " +title, Toast.LENGTH_LONG).show();
	                	   }else{
	                		   Toast.makeText(getApplicationContext(), "Failed to delete " +title, Toast.LENGTH_LONG).show();
	                	   }
	                   }
	               });
	        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                   }
	               });
	        
	        AlertDialog dialog = builder.create();
	        dialog.show();
		
	}
	
	/*
	 * This method deletes the book locally and online from the user account after confirmation
	 */
	public boolean deleteEbook(String objectId, final String title, String filelocation){
		boolean deleted= true;

 
		int d = datasource.deleteEntry(objectId);
		if(d > 0 ){
			
			// get user folder path
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
		    Editor editor = pref.edit();
            String userFolderName = pref.getString("email", null);
            // delete the ebook file and cover
			File file = new File("data/data/com.radaee.reader/proxbooks/"+userFolderName+"/"+objectId+".jpg");
			boolean filedeleted = file.delete();
			deleted = true;
			Toast.makeText(getApplicationContext(), "Deleted book " + title, Toast.LENGTH_LONG).show();
			
			if(deleted == true){
				
				ParseQuery<ParseObject> query = ParseQuery.getQuery("userEbooks");
					// Retrieve the object by id
					query.getInBackground(objectId, new GetCallback<ParseObject>() {
						  public void done(ParseObject userebooks, ParseException e) {
							   
							     // userebooks.deleteEventually();
							      Log.d("Ebook Deletion", "Ebook will be deleted when connection is detected.");
							   
						  }
					 });
				}
		}
	
		return deleted;
	}
	
	/*
	 * This method is called when the user click download ebook after purchasing it 
	 * 
	 */
	public boolean downloadEbook(String objectId, String filename){
		boolean downloaded =false;
		
		internetdetected = new InternetDetector(this.getApplicationContext());
		
		isInternetPresent = internetdetected.isNetworkAvailable();
		
		if(isInternetPresent == true){
			long freememory = util.getAvailableInternalMemorySize();
			// get filesize before download
			int file_size = 0;
			
			try{
				URL url = new URL(filename);
				URLConnection urlConnection = url.openConnection();
				urlConnection.connect();
				file_size = urlConnection.getContentLength();
				
				if (file_size >= 1024) {
					file_size /= 1024;  
		        }
		        
				
			}catch (Exception e) {
				Toast.makeText(getApplicationContext(), "An error occured.", Toast.LENGTH_LONG).show();
            }
			
			Toast.makeText(getApplicationContext(), "Ebook downloaded?" +file_size, Toast.LENGTH_LONG).show();
			Log.d("File ", "Size:" + file_size + " Free: "+ freememory);
			
			if(freememory > file_size){
				new DownloadFileFromURL().execute(filename,objectId);
				downloaded =true;
			}else{
				Toast.makeText(getApplicationContext(), "Memory not enough to store file.", Toast.LENGTH_LONG).show();
			}
			
		}else{
			util.showAlertDialog(this, "Network Error", "Please check your internet connection.", false);
		}
		
		
		
		return downloaded;
	}
	
	/**
     * Showing Dialog
     * */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case progress_bar_type: // we set this to 0
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.show();
            return pDialog;
        default:
            return null;
        }
    }
    
    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }
 
        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
		    Editor editor = pref.edit();
            String userFolderName = pref.getString("email", null);
            
            File folder = new File("data/data/com.radaee.reader/proxbooks/"+userFolderName);
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            
            Log.d("Ebook Details", "Location " + f_url[1]);
            
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();
 
                
                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                String root = "data/data/com.radaee.reader/proxbooks/"+userFolderName;
                
                
                String[] file1 = f_url[0].split("/");
                String[] file2 = file1[4].split("-");
                String filename = file2[5];
                
                Log.d("Ebook Details", "New filename " + f_url[1]);
              
                // Output stream
                OutputStream output = new FileOutputStream(root + "/" + f_url[1] + ".pdf");
 
                byte data[] = new byte[1024];
 
                long total = 0;
 
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
 
                    // writing data to file
                    output.write(data, 0, count);
                }
 
                // flushing output
                output.flush();
 
                // closing streams
                Log.d("Ebook Download", "Download completed");
                dismissDialog(progress_bar_type);
                output.close();
                input.close();
 
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
 
            return null;
        }
        
        
        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
       }
        
        
        
        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url, String objectId) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            Log.d("Ebook Download", "Completed" + objectId);
           /* 
            File file = new File("data/data/com.example.prox/proxbooks/book.pdf"); 

            if (file.exists()) { 
                Uri path = Uri.fromFile(file); 
                Intent intent = new Intent(Intent.ACTION_VIEW); 
                intent.setDataAndType(path, "application/pdf"); 
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 

                try { 
                    startActivity(intent); 
                }  
                catch (ActivityNotFoundException e) { 
                   // Toast.makeText(this,  "No Application Available to View PDF",  Toast.LENGTH_SHORT).show(); 
                }  
            }
            */
            // Displaying downloaded image into image view
            // Reading image path from sdcard
            //String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.pdf";
            // setting downloaded into image view
            //my_image.setImageDrawable(Drawable.createFromPath(imagePath));
        }
 
    }
    
    /**EBOOK Search**/
    
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
            
            Log.d("Searching",""+searchquery);
 
            ebookCursor = datasource.searchBook(searchquery);
            
            String[] from = new String[] {EbookDatabaseAdapter.KEY_TITLE,EbookDatabaseAdapter.KEY_FILENAME,EbookDatabaseAdapter.KEY_COVER,EbookDatabaseAdapter.KEY_AUTHOR, EbookDatabaseAdapter.KEY_OBJECTID,EbookDatabaseAdapter.KEY_STATUS,EbookDatabaseAdapter.KEY_CATEGORY};
            int[] to = new int[] { R.id.mTitle ,R.id.mFilename, R.id.mCover,R.id.mAuthor, R.id.mObjectId};
            
            
            // Now create an array adapter and set it to display using our row
            @SuppressWarnings("deprecation")
    		//SimpleCursorAdapter ebooks=new SimpleCursorAdapter(this, R.layout.ebooks_row, ebookCursor, from, to);
            	ArrayList<Ebook> gridArray = new ArrayList<Ebook>();
            	GridView grv = (GridView) findViewById(R.id.gridview);
            
            	if (ebookCursor != null) {
            	    startManagingCursor(ebookCursor);
            	    Ebook ebook = new Ebook();
            	    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.row_grid, ebookCursor, from, to);
            	    
            	    while (ebookCursor.moveToNext() ) {
     
            	        Ebook gebook = new Ebook();
    	    	    	  gebook.setFilename(ebookCursor.getString(ebookCursor.getColumnIndex("filename")));
    	    	    	  gebook.setAuthor(ebookCursor.getString(ebookCursor.getColumnIndex("author")));
    	    	    	  gebook.setCover(ebookCursor.getString(ebookCursor.getColumnIndex("cover")));
    	    	    	  gebook.setID(ebookCursor.getString(ebookCursor.getColumnIndex("objectId")));
    	    	    	  gebook.setISBN(ebookCursor.getString(ebookCursor.getColumnIndex("ISBN")));
    	    	    	  gebook.setTitle(ebookCursor.getString(ebookCursor.getColumnIndex("title")));
    	    	    	  gebook.setStatus(ebookCursor.getString(ebookCursor.getColumnIndex("status")));
    	    	    	  gebook.setCategory(ebookCursor.getString(ebookCursor.getColumnIndex("category")));
    	    	    	  Log.d("Ebook Details", "Details " + ebookCursor.getString(ebookCursor.getColumnIndex("cover")));
    	    	    	 
    	    	    	  gridArray.add(gebook);
            	    }
            	    
     
            	      MyLocalGridViewAdapter customGridAdapter = new MyLocalGridViewAdapter(UserEbookList.this, R.layout.row_grid, gridArray);
    	        	  grv.setAdapter(customGridAdapter);
            	    
            	     
            	}
            	
            	 stopManagingCursor(ebookCursor);
            	 grv.setOnItemClickListener(new OnItemClickListener(){
            		 
 		        	@SuppressLint("NewApi")
 					public void onItemClick(AdapterView parent,View v, final int position, final long id){	
 		        		ebookCursor.moveToPosition(position);
         	    		final String objectId = ebookCursor.getString(ebookCursor.getColumnIndex("objectId"));
         	    		final String title = ebookCursor.getString(ebookCursor.getColumnIndex("title"));
         	    		final String author = ebookCursor.getString(ebookCursor.getColumnIndex("author"));
         	    		final String ISBN = ebookCursor.getString(ebookCursor.getColumnIndex("ISBN"));
         	    		final String cover = ebookCursor.getString(ebookCursor.getColumnIndex("cover"));
         	    		final String filename = ebookCursor.getString(ebookCursor.getColumnIndex("filename"));
         	    		String status =  ebookCursor.getString(ebookCursor.getColumnIndex("status"));
         	    		final String category =  ebookCursor.getString(ebookCursor.getColumnIndex("category"));
         	    		
         	    		Log.d("Ebook Details", "Details " + status+filename+cover);
         	    		 
         	    		CharSequence[] items = {"View","Delete","Download"};
 		        		
 		        		if(status.equals("1")){
 		        			 CharSequence[] newitems={"View","Delete"};
 		        			 items = newitems;
 		        			 Log.d("Ebook Details", "Not Downloaded" + status);
 		        		}else{
 		        			 Log.d("Ebook Details", "Downloaded" + status);
 		        		} 
 		        		
 		        		
 		        		
 		        		AlertDialog.Builder builder3 =new AlertDialog.Builder(UserEbookList.this);
 						builder3.setTitle("Select an action").setItems(items, new DialogInterface.OnClickListener() {

 	        	    	@Override
 	        	    	public void onClick(DialogInterface dialog, int which) {
 	        	  
 	        	    			switch(which){
 	        	    			case 0: 
 	        	    				openEbook(objectId, title);
 	        	    				break;
 	        	    			case 1:
 	        	    				
 	        	    				attemptDeleteMyEbook(objectId, title, filename);
 	        	    				break;
 	        	    			case 2:
 	        	    				Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_LONG).show(); 
         	    					boolean downloadedFile = downloadEbook(objectId, filename);
         	    					String downloadedStatus = "1";
         	    					if(downloadedFile == true){ 
         	    						datasource.updateEntry(objectId, title,filename,author, ISBN, cover, downloadedStatus, category); 
         	    					}
         	    					break;
 	        	    				
 	        	    			}
 	        	    	}
 	        	    	});
 	        	    	
 	        	    	builder3.show();
 		        	}
         	 });
 			 
            
           
        }
 
    }
	
	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}
	
	@Override
	protected void onRestart() {
		//datasource.open();
		super.onRestart();
		ebookCursor.requery();
	}

  @Override
  protected void onPause() {
    datasource.close();
    super.onPause();
  }
 

} 
