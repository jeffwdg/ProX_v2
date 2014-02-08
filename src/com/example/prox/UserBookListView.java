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
import java.util.concurrent.Delayed;

import net.sf.andpdf.pdfviewer.PdfViewerActivity;

import com.example.prox.UserEbookList.DownloadFileFromURL;
import com.example.prox.adapter.EbookDatabaseAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
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
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

public class UserBookListView extends ListActivity {
	
  private EbookDatabaseAdapter datasource;
  ArrayList<Ebook> data = new ArrayList<Ebook>();
  TextView mTitle; 
  private ProgressDialog pDialog;
  ProgressBar progressBar;
  boolean isInternetPresent = false;
  InternetDetector internetdetected;
  Utilities util = new Utilities();
  String[] ebookViewCategory;
  Cursor ebookCursor;
  SharedPreferences pref;
  Editor editor;
  Bitmap bmp;
  
  // Progress dialog type (0 - for Horizontal progress bar)
  public static final int progress_bar_type = 0; 
  
  @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ebook_grid, menu);
		
		// Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
 
        return super.onCreateOptionsMenu(menu);
	}
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) 
  {    
     switch (item.getItemId()) 
     {        
        case R.id.view_grid: changeViewToGrid();  break;   
        case R.id.prox_sync: syncLibrary();	break;
        case R.id.reading_now: readingNow(); break;
        default:        
     } 
     return super.onOptionsItemSelected(item);    
  }
	public void changeViewToGrid(){
		//Save view preference
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 1); // 0 - for private mode
		Editor editor = pref.edit();
		
		editor.putString("view", "grid"); 
		editor.commit();
		Log.d("SetView","Grid");
		 
		Intent intent=new Intent(this, UserEbookList.class);
		startActivity(intent);
	}
	
	

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.userebooks_list);
    ActionBar actionBar = this.getActionBar();
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setTitle("My Books");
	actionBar.setIcon(R.drawable.books);
	//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	
	internetdetected = new InternetDetector(this.getApplicationContext());
	isInternetPresent = internetdetected.isNetworkAvailable();
	
	pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
    editor = pref.edit(); 
	    
    datasource = new EbookDatabaseAdapter(this);
    datasource.open();
    
    fillData();
    handleIntent(getIntent());
  }
  
  
  /*
	 * This function synchronizes the local library with the user online library
	 */
	public void syncLibrary( ){

		String userobjectId = pref.getString("objectId", null);
		final String userFolderName = pref.getString("email", null);
		//util.showAlertDialog(this, "Library Sync", "Synchronizing library. Please wait...", false);
		final ProgressDialog pd = new ProgressDialog(UserBookListView.this);
		pd.setMessage("Library Synchronizing...");
		pd.setIndeterminate(true);
		pd.show();
		final ArrayList<String> bookstobedownload = new ArrayList<String>(); 
		
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("userEbooks");
		query.whereEqualTo("userID", userobjectId);
		query.findInBackground(new FindCallback<ParseObject>() {
			
			public void done(List<ParseObject> userebooks, ParseException e) {  
				Log.d("Sync", "Retrieved " + userebooks.size() + " ebooks");
				String[] onlinebooks = new String[userebooks.size()];
		        int i=0;
		         
				  if (e == null) {
				            // get the list of online user books and store the objectId in a string array, SET A
				            for(ParseObject userbook : userebooks) {
				    	    	  onlinebooks[i] = userbook.getString("ebookID");
				    	    	  //Log.d("Online", "" + userbook.getString("ebookID"));
				    	    i++;
				            }
				            
				            //get the list of local user books and store the object ID in a string array, SET B
				            Cursor eebookCursor = datasource.fetchAllEbooks();
				            String[] eebookCursorlist = new String[eebookCursor.getCount()];
				            
				            if (eebookCursor != null) {
				        	    startManagingCursor(eebookCursor);
				        	    int b = 0;
				        	    while (eebookCursor.moveToNext()) {
				        	        eebookCursorlist[b]  = eebookCursor.getString(eebookCursor.getColumnIndex("objectId"));
					    	    	//Log.d("Local", "" +eebookCursorlist[b]);
					    	    	b++;
				        	    }
				            }
				        	    
				        	//select a book from online that is not on local and add to bookstobedownload 
				            //String[] bookstobedownload=new String[onlinebooks.length1];
				           
				            
				            
				            if(onlinebooks.length > 0){
				            	for(int j = 0; j<onlinebooks.length; j++ ){
				            		//Log.d("SetA", "" + onlinebooks[j]);
				            		int isfound = 0;
				            		for(int k=0; k<eebookCursor.getCount(); k++){
				            			//Log.d("SetB", "" + eebookCursorlist[k]);
				            			if(TextUtils.equals(onlinebooks[j], eebookCursorlist[k]))
				            			{	
				            				isfound += 1;
				            		 
					            		}
				            		}
				            		
				            		if(isfound == 0){
				            			bookstobedownload.add(onlinebooks[j]);  
				            			Log.d("Add to Queue",""+onlinebooks[j]+bookstobedownload.size());
				            		}
				            		
				            	}
				            	
				            	
				            	if(bookstobedownload.size() > 0){
				        			String nebookId;
				        			
				        			for(int y=0; y<bookstobedownload.size(); y++){
				        			nebookId = bookstobedownload.get(y);
				        			
				        				//Download covers and save to local db
				        				ParseQuery<ParseObject> nquery = ParseQuery.getQuery("ebook");
				        		    	nquery.whereEqualTo("objectId", nebookId);
				        		    	nquery.findInBackground(new FindCallback<ParseObject>() {
				        					@SuppressLint("NewApi")
				        					@Override
				        					public void done(List<ParseObject> lebookslist, ParseException e){
				        					 
				        						if (e == null) {
				        			    	    	Log.d("Added", "Found " + lebookslist.size() + " ebooks");
				        			    	    	 
				        				    	      String lmybooktitle, lmyebookID = null, lcover, lfilename, lauthor,lISBN, lcategory,lbookstatus, lebookID;
				        				    	      for(ParseObject luserbooks : lebookslist) {
				        				    	    	  
				        				    	    	  lmybooktitle=(String) luserbooks.get("title");
				        				    	    	  lfilename = (String) luserbooks.get("filename");
				        				    	    	  lcover =  (String) luserbooks.get("cover");
				        				    	    	  lauthor = (String) luserbooks.get("author");	
				        				    	    	  lISBN =  (String) luserbooks.get("ISBN");
				        				    	    	  lbookstatus =  (String) luserbooks.get("status");
				        				    	    	  lebookID =  luserbooks.getObjectId();
				        				    	    	  lcategory = (String) luserbooks.get("category");
				        		 
				        				    	    	  Log.d("Parse Data", "Retrieved ebook details " + lcover + lfilename +lauthor +lmybooktitle+ lISBN + lbookstatus);
				        				    	    	  //Save bookcover to SD
				        				    	    	  
				        				    	    	  if(lcover.toString().trim().length() > 0) {
				      	                    	            bmp = ImageDownloader.getBitmapFromURL(lcover.toString().trim());
				        				    	    	  }
				        				    	    	  
				        				    	    	  util.saveImageToSD2(userFolderName,lebookID,bmp);
				        				    	    	  String ebookStatus = "0"; //not downloaded
				        				    	     	  datasource.insertEntry(lebookID, lmybooktitle, lfilename, lauthor, lISBN, lcover, ebookStatus, lcategory);
				        				    	     	  
				        				    	     	  pd.dismiss(); 
				        				    	     	 
				        				    	     	  Intent intent = getIntent();
				        		                		  finish();
				        		                		  startActivity(intent);
				        				    	      }
				        				    	      
				        			    	    } else {Log.d("Sync", "Error");}
				        					}
				        		    	});
				        		    	
				        		    	
				        			}
				        			
				        		}else{
				        			Log.d("Sync", "Library is currently updated.");
				        			pd.dismiss(); 
				        		}
				            	
				            	
				            	
				            }
				            
			        } else {
			            Log.d("Sync", "Error: " + e.getMessage());
			        }
			  }

		 });

	}
	
	

	public void readingNow(){
		
		String userFolderName = pref.getString("email", null);
		String readingnow = pref.getString("readingnow", null);
		String curpage = pref.getString(readingnow, null);
		String title="";
		Log.d("Reading now book",""+readingnow);
		
		if(!TextUtils.isEmpty(readingnow) || !TextUtils.equals(readingnow, null))
		{
			openEbook(readingnow,title);
		}else{
			Toast.makeText(getApplicationContext(), "Sorry, the book you last read does not exist. Please redownload it.", Toast.LENGTH_LONG).show();
		}

	}
	
	
  /*
   * Displays user ebook from local sqlite in a listview
   */
  
	private void fillData() {
        // Get all of the notes from the database and create the item list
         ebookCursor = datasource.fetchAllEbooks();


        String[] from = new String[] {EbookDatabaseAdapter.KEY_TITLE,EbookDatabaseAdapter.KEY_FILENAME,EbookDatabaseAdapter.KEY_COVER,EbookDatabaseAdapter.KEY_AUTHOR, EbookDatabaseAdapter.KEY_OBJECTID,EbookDatabaseAdapter.KEY_STATUS,EbookDatabaseAdapter.KEY_CATEGORY};
        int[] to = new int[] { R.id.mTitle ,R.id.mFilename, R.id.mCover,R.id.mAuthor, R.id.mObjectId, R.id.mObjectId};
        
        
        // Now create an array adapter and set it to display using our row
        @SuppressWarnings("deprecation")
		SimpleCursorAdapter ebooks=new SimpleCursorAdapter(this, R.layout.ebooks_row, ebookCursor, from, to);
      
        
        	if (ebookCursor != null) {
        	    startManagingCursor(ebookCursor);
        	    Ebook ebook = new Ebook();
        	    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.ebooks_row, ebookCursor, from, to);
        	    
        	 
        	    SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {
        	        @Override
        	        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        	        	int viewId = view.getId();
        	        	int categoryIndex = cursor.getColumnIndex("cover");
        	        	if(categoryIndex == columnIndex){
        	        	     String coverString = cursor.getString(ebookCursor.getColumnIndex("objectId"));
             	            ImageView bookCover = (ImageView) view;
             	            
             	            Drawable bookcover;
             	  		  	SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 1); // 0 - for private mode
             	  		  	//Editor editor = pref.edit();

             	            String userFolderName = pref.getString("email", null);
             	            
             	  		   
             	  		  	String bitmapPath = "data/data/com.radaee.reader/proxbooks/" + userFolderName +"/" + coverString + ".jpg";
             	            Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath);
             	            bookcover = new BitmapDrawable(bitmap);
             	            bookCover.setImageDrawable(bookcover);
             	            
             	            if(bookcover == null){
             	            	bookCover.setImageResource(R.drawable.nocover);
             	            }
             	            return true;
        	        	}
        	       
        	            return false;
        	        }
        	    };
        	    
        	    adapter.setViewBinder(binder);
        	    setListAdapter(adapter);
        	    
        	}
        	
        	 stopManagingCursor(ebookCursor);
    }
	
 
	
	@SuppressLint("NewApi")
	public void onListItemClick(ListView parent, final View v, final int position, final long id){	
		final Cursor ebookCursor = datasource.fetchAllEbooks();
		ebookCursor.moveToPosition(position);
		final String objectId = ebookCursor.getString(ebookCursor.getColumnIndex("objectId"));
		final String title = ebookCursor.getString(ebookCursor.getColumnIndex("title"));
		final String author = ebookCursor.getString(ebookCursor.getColumnIndex("author"));
		final String cover = ebookCursor.getString(ebookCursor.getColumnIndex("cover"));
		final String ISBN = ebookCursor.getString(ebookCursor.getColumnIndex("ISBN"));
		final String category = ebookCursor.getString(ebookCursor.getColumnIndex("category"));
		final String filename = ebookCursor.getString(ebookCursor.getColumnIndex("filename"));
		String status =  ebookCursor.getString(ebookCursor.getColumnIndex("status"));
		
		Log.d("Ebook Details", "Details " + status+filename+cover);
		
		CharSequence[] items = {"View","Delete","Download"};
		
		if(status.equals("1")){
			 CharSequence[] newitems={"View","Delete"};
			 items = newitems;
			 Log.d("Ebook Details", "Not Downloaded" + status);
		}else{
			 Log.d("Ebook Details", "Downloaded" + status);
		} 
		

		AlertDialog.Builder builder3 =new AlertDialog.Builder(UserBookListView.this);
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
				Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show(); 
				boolean downloadedFile = downloadEbook(objectId, filename, v);
				String downloadedStatus = "1";
				if(downloadedFile == true){ 
					datasource.updateEntry(objectId, title,filename,author, ISBN, cover, downloadedStatus, category); 
				}
				//Intent intent = getIntent();
    		    //finish();
    		    //startActivity(intent);
				break;
			}
    	}
    	});
    	
    	builder3.show();
	}
	
	
	/*
	 *  This method asks for confirmation from the user before deleting the book
	 */
	
	public void attemptDeleteMyEbook(final String objectId, final String title, final String filelocation){
		
	        AlertDialog.Builder builder = new AlertDialog.Builder(UserBookListView.this);
	        builder.setTitle("Delete book");
	        builder.setMessage("Are you sure you want to delete this book " + title +" in your account?");
	        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   if(deleteEbook(objectId,title,filelocation) == true){
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
		boolean deleted= false;

 
		int d = datasource.deleteEntry(objectId);
		if(d > 0 ){
			
			// get user folder path
			pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
		    editor = pref.edit();
            String userFolderName = pref.getString("email", null);
            String userObjectID = pref.getString("objectId", null);
            // delete the ebook file and cover
			File file = new File("data/data/com.radaee.reader/proxbooks/"+userFolderName+"/"+objectId+".jpg");
			File efile = new File("data/data/com.radaee.reader/proxbooks/"+userFolderName+"/"+objectId+".pdf");
			
			boolean filedeleted = file.delete();
			boolean efiledeleted = efile.delete();
			 
			Toast.makeText(getApplicationContext(), "Deleting book " + title, Toast.LENGTH_LONG).show();
			Log.d("Ebook Deletion", "Ebook internet."+isInternetPresent+filedeleted+efiledeleted);  
			if(filedeleted == true || efiledeleted == true){
				
				if(isInternetPresent == true){
					ParseQuery<ParseObject> query = ParseQuery.getQuery("userEbooks");
					// Retrieve the object by id
					query.whereStartsWith("ebookID",  objectId);
					query.whereEqualTo("userID", userObjectID);
					query.getFirstInBackground( new GetCallback<ParseObject>() {
						  public void done(ParseObject userebooks, ParseException e) {  
							  if(e == null){
							  userebooks.deleteInBackground();
							  Log.d("Ebook Deletion", "Ebook deleted.");  
							  }
							  else{Log.d("Ebook Deletion", "An error occured."+ e.getCode() );  }
						  }
					 });
					  
					
				}else{
					ParseQuery<ParseObject> query = ParseQuery.getQuery("userEbooks");
					// Retrieve the object by id
					query.getInBackground(objectId, new GetCallback<ParseObject>() {
						  public void done(ParseObject userebooks, ParseException e) {  
							  userebooks.deleteEventually();
						  }
					 });
					Log.d("Ebook Deletion", "Ebook will be deleted when connection is detected."); 
				}
				deleted = true;
				Toast.makeText(getApplicationContext(), "Deleted book " + title, Toast.LENGTH_LONG).show();
			}
		}
		return deleted;
	}
	
	
	/*
	 *  This method opens the ebook
	 */
	
	public void openEbook(String objectId, String title){
		
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
	    //Editor editor = pref.edit();
        String userFolderName = pref.getString("email", null);
        
		String ebookLocation = "data/data/com.radaee.reader/proxbooks/"+ userFolderName +"/";
		String ebookFile = objectId +".pdf";
		Toast.makeText(getApplicationContext(), "Opening.. " + title, Toast.LENGTH_LONG).show();
		
		File ebookLocal = new File(ebookLocation+ebookFile);
		
		if(ebookLocal.exists()){
			// Redirect screen to pdf viewer
			Intent intent = new Intent(UserBookListView.this, MyPDFOpen.class);
			intent.putExtra("ebookFile", ebookLocation+ebookFile);
			intent.putExtra("objectId", objectId);
			//intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME,ebookLocation+ebookFile);
			startActivity(intent);
		}else{
			Toast.makeText(getApplicationContext(), "File not found for " + title, Toast.LENGTH_LONG).show();
		}
		
	}
	
	/*
	 * This method is called when the user click download ebook after purchasing it 
	 * 
	 */
	public boolean downloadEbook(String objectId, String filename, View v){
		boolean downloaded =false;
		
		internetdetected = new InternetDetector(this.getApplicationContext());
		
		isInternetPresent = internetdetected.isNetworkAvailable();
		
		if(isInternetPresent == true){
			//Toast.makeText(getApplicationContext(), "Ebook downloaded?" +filename, Toast.LENGTH_LONG).show();
			downloaded = true;
			new DownloadFileFromURL().execute(filename,objectId);
			
			progressBar = (ProgressBar) v.findViewById(R.id.dlprogressbar);
			progressBar.setVisibility(View.VISIBLE);
			progressBar.setProgress(0);
			
			
		}else{
			util.showAlertDialog(this, "Network Error", "Please check your internet connection.", false);
		}
		
		return downloaded;
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
            //showDialog(progress_bar_type);
        }
 
        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
		    //Editor editor = pref.edit();
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
            //pDialog.setProgress(Integer.parseInt(progress[0]));
        	progressBar.setProgress(Integer.parseInt(progress[0]));
 
            if(Integer.parseInt(progress[0]) == 100){
            	progressBar.getProgressDrawable().setColorFilter(Color.argb(1, 0, 233, 0), Mode.SRC_IN);
            	progressBar.setVisibility(View.INVISIBLE);
            	Toast.makeText(getApplicationContext(), "Download completed.", Toast.LENGTH_SHORT).show();
            }
       }
       
        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url, String objectId) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            //Log.d("Ebook Download", "Completed" + objectId);
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
            
            if(ebookCursor.getCount() <=0 )
            {
            	Toast.makeText(getApplicationContext(), "Sorry. No results found for query "+ searchquery +"." , Toast.LENGTH_LONG).show();
            }

            String[] from = new String[] {EbookDatabaseAdapter.KEY_TITLE,EbookDatabaseAdapter.KEY_FILENAME,EbookDatabaseAdapter.KEY_COVER,EbookDatabaseAdapter.KEY_AUTHOR, EbookDatabaseAdapter.KEY_OBJECTID,EbookDatabaseAdapter.KEY_STATUS,EbookDatabaseAdapter.KEY_CATEGORY};
            int[] to = new int[] { R.id.mTitle ,R.id.mFilename, R.id.mCover,R.id.mAuthor, R.id.mObjectId, R.id.mObjectId};
            
            
            // Now create an array adapter and set it to display using our row
            @SuppressWarnings("deprecation")
    		SimpleCursorAdapter ebooks=new SimpleCursorAdapter(this, R.layout.ebooks_row, ebookCursor, from, to);
          
            
            	if (ebookCursor != null) {
            	    startManagingCursor(ebookCursor);
            	    Ebook ebook = new Ebook();
            	    SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.ebooks_row, ebookCursor, from, to);
            	    
            	 
            	    SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {
            	        @Override
            	        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            	        	int viewId = view.getId();
            	        	int categoryIndex = cursor.getColumnIndex("cover");
            	        	if(categoryIndex == columnIndex){
            	        	     String coverString = cursor.getString(ebookCursor.getColumnIndex("objectId"));
                 	            ImageView bookCover = (ImageView) view;
                 	            
                 	            Drawable bookcover;
                 	  		  	SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 1); // 0 - for private mode
                 	  		  	//Editor editor = pref.edit();

                 	            String userFolderName = pref.getString("email", null);
                 	            
                 	  		   
                 	  		  	String bitmapPath = "data/data/com.radaee.reader/proxbooks/" + userFolderName +"/" + coverString + ".jpg";
                 	            Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath);
                 	            bookcover = new BitmapDrawable(bitmap);
                 	            bookCover.setImageDrawable(bookcover);
                 	            
                 	            if(bookcover == null){
                 	            	bookCover.setImageResource(R.drawable.nocover);
                 	            }
                 	            return true;
            	        	}
            	       
            	            return false;
            	        }
            	    };
            	    
            	    adapter.setViewBinder(binder);
            	    setListAdapter(adapter);
            	    
            	}
            	
            	 stopManagingCursor(ebookCursor);
            	    
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
