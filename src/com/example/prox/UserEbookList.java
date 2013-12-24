package com.example.prox;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import com.example.prox.Download.DownloadFileFromURL;
import com.example.prox.adapter.EbookDatabaseAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

public class UserEbookList extends Activity {
	
  private EbookDatabaseAdapter datasource;
  ArrayList<Ebook> data = new ArrayList<Ebook>();
  TextView mTitle;
  private ProgressDialog pDialog;
  
  // Progress dialog type (0 - for Horizontal progress bar)
  public static final int progress_bar_type = 0; 
  
  @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ebook_list, menu);


		return true;
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
    setContentView(R.layout.grid);
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
	    	    	  //gebook.setAuthor(ebookCursor.getString(ebookCursor.getColumnIndex("author")));
	    	    	  //gebook.setCover(ebookCursor.getString(ebookCursor.getColumnIndex("cover")));
	    	    	  gebook.setID(ebookCursor.getString(ebookCursor.getColumnIndex("objectId")));
	    	    	  gebook.setISBN(ebookCursor.getString(ebookCursor.getColumnIndex("ISBN")));
	    	    	  gebook.setTitle(ebookCursor.getString(ebookCursor.getColumnIndex("title")));
	    	    	  gebook.setStatus(ebookCursor.getString(ebookCursor.getColumnIndex("status")));
	    	    	  //gebook.setCategory(ebookCursor.getString(ebookCursor.getColumnIndex("category")));
	    	    	  Log.d("Ebook Details", "Details " + ebookCursor.getString(ebookCursor.getColumnIndex("cover")));
	    	    	 
	    	    	  gridArray.add(gebook);
        	    }
        	    
 
        	      MyLocalGridViewAdapter customGridAdapter = new MyLocalGridViewAdapter(UserEbookList.this, R.layout.row_grid, gridArray);
	        	  grv.setAdapter(customGridAdapter);
        	    
        	     
        	}
        	
        	
        	
        	 grv.setOnItemClickListener(new OnItemClickListener(){
        		 
		        	@SuppressLint("NewApi")
					public void onItemClick(AdapterView parent,View v, final int position, final long id){	
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
		        		
		        		
		        		
		        		AlertDialog.Builder builder3 =new AlertDialog.Builder(UserEbookList.this);
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
	        	    			case 2: Toast.makeText(getApplicationContext(), "You clicked View" + objectId, Toast.LENGTH_LONG).show();
	        	    				break;
	        	    			}
	        	    	}
	        	    	});
	        	    	
	        	    	builder3.show();
		        	}
        	 });
			 
        	 
    
    	     
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
		
		new DownloadFileFromURL().execute(filename);
		
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
            pDialog.setCancelable(true);
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
            
            File folder = new File("data/data/com.example.prox/proxbooks/"+userFolderName);
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            
            Log.d("Ebook Details", "Location " + f_url[0]);
            
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();
 
                
                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                String root = "data/data/com.example.prox/proxbooks/"+userFolderName;
                
                String[] file1 = f_url[0].split("/");
                String[] file2 = file1[4].split("-");
                String filename = file2[5];
                
                Log.d("Ebook Details", "New filename" + filename);
              
                // Output stream
                OutputStream output = new FileOutputStream(root + "/" + filename);
 
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
