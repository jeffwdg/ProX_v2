package com.example.prox;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.example.prox.Grid.ImageAdapter;
import com.example.prox.adapter.EbookDatabaseAdapter;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.radaee.reader.R;
 
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class StoreBookDetails extends Activity {
	 
	    Button btnShowProgress;
	    TextView booktitle, bookfilename, bookauthor;
	    private ProgressDialog pDialog;
	    ImageView my_image;
	    String objectId , userId;
	    
	    private ImageDownloader mDownloader;
	    private static Bitmap bmp;
	    private FileOutputStream fos;
	    
        String title = "";
        String author = "";
        String filename = "";
        String ebookID = "";
        String cover="";
        String ISBN="";
        String status="0";
        String category="";

	    //file url to download
	    //private static String file_url = "http://api.androidhive.info/progressdialog/hive.jpg";
	    String file_url="";
	    
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.actionbar, menu);
	 
	        // Associate searchable configuration with the SearchView
	        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
	                .getActionView();
	        searchView.setSearchableInfo(searchManager
	                .getSearchableInfo(getComponentName()));
	 
	        return super.onCreateOptionsMenu(menu);
	    }
	    
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.bookdetails);
	        
	        ActionBar ab = getActionBar();
		    ab.setDisplayHomeAsUpEnabled(true);
	
	        ab.setIcon(R.drawable.ebookstore);
	        
	        Parse.initialize(this, "x9n6KdzqtROdKDXDYF1n5AEoZLZKOih8rIzcbPVP", "JkqOqaHmRCA35t9xTtyoiofgG3IO7E6b82QIIHbF");

        	ParseUser currentUser = ParseUser.getCurrentUser();
        	userId = currentUser.getObjectId();
        	
	        Intent i = getIntent();
	        
	        // Get ebook details
	        int position = i.getExtras().getInt("id");
	        title = i.getExtras().getString("title");
	        author = i.getExtras().getString("author");
	        cover = i.getExtras().getString("cover");
	        status = i.getExtras().getString("status");
	        ISBN = i.getExtras().getString("ISBN");
	        objectId =i.getExtras().getString("ebookID");
	        filename =i.getExtras().getString("filename");
	        category =i.getExtras().getString("category");
	        
	        ab.setTitle("Book: "+title);
	        
	        Log.d("Ebook Store", "Details "+objectId + author +" " +cover);
	        
	        if(TextUtils.isEmpty(cover)){cover="https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcSMeKS7nHHfbiw08SQ4Z7jQh6Vzji36dOzWENTmXEn74Fp_tCM3";}
	        
	       
	       //Set View with ebook details 
	        booktitle = (TextView) findViewById(R.id.bookTitle);
	        booktitle.setText(title);
	        
	        bookauthor= (TextView) findViewById(R.id.bookAuthor);
	        bookauthor.setText("By: " +author);
	        
	        file_url = i.getExtras().getString("filename");
	        Toast.makeText(this,"Ebook " +  title + " selected", Toast.LENGTH_SHORT).show();
	 
	        btnShowProgress = (Button) findViewById(R.id.btnProgressBar);
	        
	        //Image to show  
	        my_image = (ImageView) findViewById(R.id.book_cover);
	        Drawable bookcover1 = LoadImageFromURL(cover);
	        my_image.setImageDrawable(bookcover1);
	   
	        
	        
	        btnShowProgress.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                // starting new Async Task
	               
	            	ParseObject userEbooks = new ParseObject("userEbooks");
	            	userEbooks.put("userID", userId);
	            	userEbooks.put("ebookID", objectId);
	    
	            	userEbooks.saveInBackground();
	            	Toast.makeText(getApplicationContext(), "Ebook purchased successfully.", Toast.LENGTH_LONG).show();
	                Log.d("Ebook Purchase", "Adding ebook to user account..");
	                
	                // Save to local DB
	                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
	   		     	Editor editor = pref.edit();
		
	                String userFolderName = pref.getString("email", null);
	                File folder = new File("data/data/com.radaee.reader/proxbooks");
	                boolean success = true;
	                
	                if(!folder.exists()) {
	                    success = folder.mkdir(); // create proxbooks folder
	                    Log.d("Ebook Folder Creation", "Created main folder.");
	                }else{
	                	Log.d("Ebook Folder Creation", "Failed to create main folder.");
	                }
	                
	                if(folder.exists()){
                    	boolean userfoldersuccess = true;
                    	File userFolder = new File("data/data/com.radaee.reader/proxbooks/"+userFolderName);
                    	userfoldersuccess= userFolder.mkdir(); // create user folder
                    	Log.d("Ebook Folder Creation", "Created user folder.");
                    	
                    	//download books & bookcover here & save to localdb
                    	if(userFolder.exists()){
                    		EbookDatabaseAdapter ebookDatabaseAdapter = new EbookDatabaseAdapter(StoreBookDetails.this);
                    		ebookDatabaseAdapter.open();
                    		String category = "3";
                    		
                    		 if (cover.toString().trim().length() > 0) {
                    	            bmp = ImageDownloader.getBitmapFromURL(cover.toString().trim());
                    	            //img.setImageBitmap(bmp);
                    	            //save.setEnabled(true);
                    	        }
                    		 
                    		 saveImageToSD(userFolderName,objectId);
                    		String ebookStatus = "0"; //not downloaded
                     		ebookDatabaseAdapter.insertEntry(objectId, title, filename, author, ISBN, cover, ebookStatus, category);
                    		
                    		Log.d("Ebook Local Storage", "Added userebook to local db.");
                    	}
	                }
	                
	                Intent i = new Intent(StoreBookDetails.this, UserEbookList.class);
		    	  	startActivity(i);
	                
	            }
	        });
	    }
		 public Drawable LoadImageFromURL(String url){
			 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			    StrictMode.setThreadPolicy(policy);
		    try
		    {
			    InputStream is = (InputStream) new URL(url).getContent();
			    Drawable d = Drawable.createFromStream(is, "src");
			    return d;	
		    }catch (Exception e) {
			    System.out.println(e);
			    return null;
		    }
		}
		 
		 public void downloadBookCover(String... f_url){
			 
			 try {
	                URL url = new URL(f_url[0]);
	                URLConnection conection = url.openConnection();
	                conection.connect();
	                // this will be useful so that you can show a tipical 0-100% progress bar
	                int lenghtOfFile = conection.getContentLength();
	 
	                
	                // download the file
	                InputStream input = new BufferedInputStream(url.openStream(), 8192);
	                String root = "data/data/com.radaee.reader/proxbooks";
	                
	 
	                String[] file1 = file_url.split("/");
	                String[] file2 = file1[4].split("-");
	                String filename = file2[5];
	                Log.d("Filename", filename);
	                
	                String bookfilename = file2[5]; //"ebook.pdf";
	                // Output stream
	                OutputStream output = new FileOutputStream(root + "/" + bookfilename);
	 
	                byte data[] = new byte[1024];
	 
	                long total = 0;
	 
	                int count;
					while ((count = input.read(data)) != -1) {
	                    total += count;
	                    // publishing the progress....
	                    // After this onProgressUpdate will be called
	                   // publishProgress(""+(int)((total*100)/lenghtOfFile));
	 
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
		 }
		 /*
		  * This methods saves the downloaded image to sdcard
		  */
		 private void saveImageToSD(String userFolderName, String objectId) {
			 	
			 Log.d("Ebook Save Cover", "Saving cover...");
			 
			    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			    //you can select your preferred CompressFormat and quality.
			    bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			    
			    // create a new file on SD card  
			    File file = new File("data/data/com.radaee.reader/proxbooks/"+userFolderName+"/"+objectId+".jpg");
			    
			    try {
			        file.createNewFile();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			    
			    // create a new FileOutputStream and write bytes to file
			    try {
			        fos = new FileOutputStream(file);
			    } catch (FileNotFoundException e) {
			        e.printStackTrace();
			    }
			    
			    try {
			        fos.write(bytes.toByteArray());
			        fos.close();
			        Log.d("Ebook Local Storage", "Cover saved..");
			        Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
			    } catch (IOException e) {
			    	Log.d("Ebook Local Storage", "Cover not saved.");
			        e.printStackTrace();
			    }

			}
	 
 
 
	}
