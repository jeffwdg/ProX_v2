package com.example.prox;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.example.prox.Grid.ImageAdapter;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
 
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StoreBookDetails extends Activity {
	 
	    Button btnShowProgress;
	    TextView booktitle, bookfilename;
	 
	    private ProgressDialog pDialog;
	    ImageView my_image;
	    String objectId , userId;

	    //file url to download
	    //private static String file_url = "http://api.androidhive.info/progressdialog/hive.jpg";
	    String file_url="";
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.bookdetails);
	        
	        Parse.initialize(this, "x9n6KdzqtROdKDXDYF1n5AEoZLZKOih8rIzcbPVP", "JkqOqaHmRCA35t9xTtyoiofgG3IO7E6b82QIIHbF");

        	ParseUser currentUser = ParseUser.getCurrentUser();
        	userId = currentUser.getObjectId();
        	
	        String title = "";
	        String author = "";
	        String filename = "";
	 
	        Intent i = getIntent();
	        
	        // Selected image id
	        int position = i.getExtras().getInt("id");
	        title = i.getExtras().getString("title");
	        objectId = i.getExtras().getString("ebookID");
	        //filename = i.getExtras().getString("filename");
 
	        booktitle = (TextView) findViewById(R.id.bookTitle);
	        booktitle.setText(title);
	        
	        
	        Log.d("Ebook Store", "Details"+objectId +userId);
	        
	        //bookfilename = (TextView) findViewById(R.id.book_filelocation);
	        //bookfilename.setText(myfilename);
	        
	        file_url = i.getExtras().getString("filename");
	        Toast.makeText(this,"The ebook " +  title + " selected", Toast.LENGTH_SHORT).show();
	 
	        btnShowProgress = (Button) findViewById(R.id.btnProgressBar);
	        
	        //Image to show after downloading
	        my_image = (ImageView) findViewById(R.id.my_image);
	   
	        btnShowProgress.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                // starting new Async Task
	               
	            	ParseObject userEbooks = new ParseObject("userEbooks");
	            	userEbooks.put("userID", userId);
	            	userEbooks.put("ebookID", objectId);
	    
	            	userEbooks.saveInBackground();
	            	
	                Log.d("Ebook Purchase", "Adding ebook to user account..");
	            }
	        });
	    }
	 
 
 
	}
