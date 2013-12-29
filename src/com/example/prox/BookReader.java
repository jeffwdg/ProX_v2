package com.example.prox;

import java.io.File;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BookReader extends Activity {
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.bookreader);
	        
	        ActionBar actionBar = this.getActionBar();
	    	actionBar.setDisplayHomeAsUpEnabled(true);
	    	actionBar.setTitle("ProX Ereader");
	    	actionBar.setIcon(R.drawable.books);

	    	
	        Button button = (Button) findViewById(R.id.OpenPdf);
	        
	        button.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                File file = new File("data/data/com.example.prox/proxbooks/book.pdf");
	                Toast.makeText(BookReader.this, "Reading.....", Toast.LENGTH_SHORT).show();
	                if (file.exists()) {
	                	Toast.makeText(BookReader.this, "Opening..", Toast.LENGTH_SHORT).show();
	                	
	                    Uri path = Uri.fromFile(file);
	                    Intent intent = new Intent(Intent.ACTION_VIEW);
	                    intent.setDataAndType(path, "application/pdf");
	                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

	                    try {
	                        startActivity(intent);
	                    } 
	                    catch (ActivityNotFoundException e) {
	                        Toast.makeText(BookReader.this, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();
	                    }
	                }
	            }
	        });
	    }
	    
	    @Override
	    protected void onResume() {
	        super.onResume();
	         
	    }
	    
}
