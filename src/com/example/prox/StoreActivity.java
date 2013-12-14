package com.example.prox;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class StoreActivity extends Activity{

	private WebView webView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.storeview);
		
		Parse.initialize(this, "x9n6KdzqtROdKDXDYF1n5AEoZLZKOih8rIzcbPVP", "JkqOqaHmRCA35t9xTtyoiofgG3IO7E6b82QIIHbF"); 
 
		
		//get objects
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ebook");
		query.getInBackground("BmNNTwwAJt", new GetCallback<ParseObject>() {
		@Override
		public void done(ParseObject ebook, ParseException e) {
			// TODO Auto-generated method stub
			   if (e == null && ebook != null) {
				      // object will be your game score
				    	String title = ebook.getString("title");
				    	String author = ebook.getString("author");
				    	String fileName = ebook.getString("filename");
				    	
				    	Toast.makeText(StoreActivity.this, fileName, Toast.LENGTH_LONG).show();
				    } else {
				      // something went wrong
				    	Toast.makeText(StoreActivity.this, "Something is wrong", Toast.LENGTH_LONG).show();
				    }
		}

		});
		
		 
		
		//webView = (WebView)findViewById(R.id.storeview);
		//webView.getSettings().setJavaScriptEnabled(true);
		//webView.loadUrl("http://prox.parseapp.com/");
 
	}

}
