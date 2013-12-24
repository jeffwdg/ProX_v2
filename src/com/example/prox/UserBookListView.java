package com.example.prox;
 
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import com.example.prox.adapter.EbookDatabaseAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
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
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

public class UserBookListView extends ListActivity {
	
  private EbookDatabaseAdapter datasource;
  ArrayList<Ebook> data = new ArrayList<Ebook>();
  TextView mTitle; 
  Boolean isInternetPresent = false;
  InternetDetector internetdetected;
  Utilities util = new Utilities();
  
  @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ebook_grid, menu);
		return true;
	}
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) 
  {    
     switch (item.getItemId()) 
     {        
        case R.id.view_grid: changeViewToGrid();     
        	return true;        
        default:            
           return super.onOptionsItemSelected(item);    
     }
  }
	public void changeViewToGrid(){
		
		Intent intent=new Intent(this, UserEbookList.class);
		startActivity(intent);
	}
	
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.userebooks_list);

	
    datasource = new EbookDatabaseAdapter(this);
    datasource.open();
    
    fillData();
    
  }
  
  /*
   * Displays user ebook from local sqlite in a listview
   */
  
	private void fillData() {
        // Get all of the notes from the database and create the item list
        final Cursor ebookCursor = datasource.fetchAllEbooks();
        
        
        

        String[] from = new String[] {EbookDatabaseAdapter.KEY_TITLE,EbookDatabaseAdapter.KEY_FILENAME,EbookDatabaseAdapter.KEY_COVER,EbookDatabaseAdapter.KEY_AUTHOR, EbookDatabaseAdapter.KEY_OBJECTID,EbookDatabaseAdapter.KEY_STATUS};
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
             	  		  	Editor editor = pref.edit();

             	            String userFolderName = pref.getString("email", null);
             	            
             	  		   
             	  		  	String bitmapPath = "data/data/com.example.prox/proxbooks/" + userFolderName +"/" + coverString + ".jpg";
             	            Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath);
             	            bookcover = new BitmapDrawable(bitmap);
             	            bookCover.setImageDrawable(bookcover);
             	            
             	            if(bookcover == null){
             	            	bookCover.setImageResource(R.drawable.nocover);
             	            }
             	            
             	            
             	            
             	            //Toast.makeText(getApplicationContext(), "Local Cover " + categoryIndex+columnIndex, Toast.LENGTH_LONG).show();
             	            return true;
        	        	}
        	       
        	            return false;
        	        }
        	    };
        	    
        	    adapter.setViewBinder(binder);
        	    setListAdapter(adapter);
        	    
        	}
        	
        
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
	
	
	@SuppressLint("NewApi")
	public void onListItemClick(ListView parent, View v, final int position, final long id){	
		final Cursor ebookCursor = datasource.fetchAllEbooks();
		ebookCursor.moveToPosition(position);
		final String objectId = ebookCursor.getString(ebookCursor.getColumnIndex("objectId"));
		final String title = ebookCursor.getString(ebookCursor.getColumnIndex("title"));
		String cover = ebookCursor.getString(ebookCursor.getColumnIndex("cover"));
		final String filename = ebookCursor.getString(ebookCursor.getColumnIndex("filename"));
		String status =  ebookCursor.getString(ebookCursor.getColumnIndex("status"));
		
		Log.d("Ebook Details", "Details " + status+filename+cover);
		
		CharSequence[] items = {"Download","Delete","View",};
		
		if(status.equals("0")){
			 CharSequence[] newitems={"Download","Delete"};
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
    			case 0: Toast.makeText(getApplicationContext(), "You clicked Download", Toast.LENGTH_LONG).show(); 
    				downloadEbook(objectId, filename);
    				break;
    			case 1:
    				attemptDeleteMyEbook(objectId, title, filename);
    				break;
    			case 2: 
    				Toast.makeText(getApplicationContext(), "You clicked View" + objectId, Toast.LENGTH_LONG).show();
    				break;
    			}
    	}
    	});
    	
    	builder3.show();
	}
	
	
	/*
	 * This method deletes the book locally and online from the user account after confirmation
	 */
	public boolean deleteEbook(String objectId, final String title, String filelocation){
		boolean deleted= false;

 
		int d = datasource.deleteEntry(objectId);
		if(d > 0 ){
			// get user folder path
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
		    Editor editor = pref.edit();
            String userFolderName = pref.getString("email", null);
            // delete the ebook file and cover
			File file = new File("data/data/com.example.prox/proxbooks/"+userFolderName+"/"+objectId+".jpg");
			boolean filedeleted = file.delete();
			
			Toast.makeText(getApplicationContext(), "File deleted?" +filedeleted, Toast.LENGTH_LONG).show();
			deleted = true;
			Toast.makeText(getApplicationContext(), "Deleted book " + title, Toast.LENGTH_LONG).show();
		}
		
		return deleted;
	}
	
	/*
	 * This method is called when the user click download ebook after purchasing it 
	 * 
	 */
	public boolean downloadEbook(String objectId, String filename){
		boolean downloaded =false;
		Toast.makeText(getApplicationContext(), "Ebook downloaded?" +filename, Toast.LENGTH_LONG).show();
		
	    internetdetected = new InternetDetector(getApplicationContext());
	    isInternetPresent = internetdetected.isNetworkAvailable();
		Log.d("ProX App", "Checking internet connection..."+ isInternetPresent);
		
		if(isInternetPresent == true){
			Log.d("Ebook Download","Internet is detected.");
			 
			
		}else{
			Log.d("Ebook Download","No Internet connection detected.");
			util.showAlertDialog(UserBookListView.this, "Network Error", "Please check your internet connection.", false);
			
		}
		
		return downloaded;
	}
	
 

	
	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

  @Override
  protected void onPause() {
    datasource.close();
    super.onPause();
  }
 

} 
