package com.example.prox;


import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.radaee.reader.R;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.prox.model.TypefaceSpan;

public class MainActivity extends Activity {
	
	Button btnSignIn,btnSignUp, btnStore;
	TextView linktosignup, linktoforgetPassword;
	Utilities util = new Utilities();
	EditText editTextEmail, editTextPassword;
    boolean isInternetPresent = false;
    // Connection detector class
    InternetDetector internetdetected;
    
 
    @Override
    public void onBackPressed() {
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		internetdetected = new InternetDetector(getApplicationContext());
		
		boolean isloggedin = isLoggedIn();
		
		if(isloggedin == true){
			Intent intentMenu = new Intent(getApplicationContext(),MenuActivity.class);
		    startActivity(intentMenu);
		}
		
		// initialize parse
		Parse.initialize(this, "x9n6KdzqtROdKDXDYF1n5AEoZLZKOih8rIzcbPVP", "JkqOqaHmRCA35t9xTtyoiofgG3IO7E6b82QIIHbF");
		//Calling ParseAnalytics to see Analytics of our app
        ParseAnalytics.trackAppOpened(getIntent());
        
		isInternetPresent = internetdetected.isNetworkAvailable();
		Log.d("ProX App", "Checking internet connection..."+ isInternetPresent);
		

		
	    // Get The Reference Of Buttons
	    btnSignIn=(Button)findViewById(R.id.buttonSignIn);
	    linktosignup = (TextView)findViewById(R.id.link_to_register);
	    linktoforgetPassword = (TextView)findViewById(R.id.link_to_forgotPassword);
	    editTextPassword = (EditText)findViewById(R.id.editTextPasswordToLogin);
	    editTextEmail = (EditText)findViewById(R.id.editTextEmailToLogin);
		
	     
        Typeface font = Typeface.createFromAsset(getAssets(), "daddysgirl.ttf");
        btnSignIn.setTypeface(font);
        linktosignup.setTypeface(font);
        linktoforgetPassword.setTypeface(font);
        editTextPassword.setTypeface(font);
        editTextEmail.setTypeface(font);
	    
        SpannableString s = new SpannableString("ProX");
        s.setSpan(new TypefaceSpan(this, "daddysgirl.otf"), 0, s.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(s);
        
	    btnSignIn.setOnClickListener(new OnClickListener() {
	    	
	    	
			public void onClick(View v) {
				//check if internet is present
					boolean isloggedin = isLoggedIn();
					Toast.makeText(getApplicationContext(), "Signing in...", Toast.LENGTH_LONG).show();

					if(isloggedin == true){
						Intent intentMenu = new Intent(getApplicationContext(),MenuActivity.class);
					    startActivity(intentMenu);
					}else if(isInternetPresent == true && util.isOnline(getApplicationContext()) == true){
							Log.d("ProX Sign In","Signing in...");
								signIn();				
					}else{
						Log.d("ProX App","No Internet connection detected.");
						util.showAlertDialog(MainActivity.this, "No Internet Connection", "You don't have internet connection.", false);
					}
					
					
				}
			});
	    
	    
	    // Set OnClick Listener on SignUp button 
	    linktosignup.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			signUp();
			}
		});
	    
	    // Set OnClick Listener on SignUp button 
	    linktoforgetPassword.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			 forgotPassword();
			}
		});
	 
	}	
 
	
		public void signUp(){
			/// Create Intent for SignUpActivity  and Start The Activity
			Intent intentSignUP=new Intent(getApplicationContext(),SignUpActivity.class);
			startActivity(intentSignUP);
		}
		
		
		public void forgotPassword(){
			/// Create Intent for SignUpActivity  and Start The Activity
			Intent intent=new Intent(this,ForgotPassword.class);
			startActivity(intent);
		}
		
		
		//Method to handleClick Event of Sign In Button
		public void signIn()
		{
			clearErrors();
			
			String email = editTextEmail.getText().toString();
			String password = editTextPassword.getText().toString();
			String pattern = "^[a-zA-Z0-9]*$";
			String email_pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			
 
			boolean cancel = false;
			View focusView = null;
			 
			//check if password is valid
			if(TextUtils.isEmpty(password)){
				editTextPassword.setError(getString(R.string.required_field));
				  focusView = editTextPassword;
                  cancel = true;
			}else if(password.length() > 6  && !password.matches(pattern)){
				editTextPassword.setError(getString(R.string.invalid_password));
				  focusView = editTextPassword;
                  cancel = true;
			}
			
			//check if email is valid
			if(TextUtils.isEmpty(email)){
				editTextEmail.setError(getString(R.string.required_field));
				focusView = editTextEmail;
                cancel = true;
			}else if(!email.matches(email_pattern)){
				editTextEmail.setError(getString(R.string.invalid_email));
				focusView = editTextEmail;
                cancel = true;
			}
			
			if(cancel == true){
				 focusView.requestFocus();
			}
			else{
				Toast.makeText(getApplicationContext(), "Signing in...", Toast.LENGTH_LONG).show();
				signInNow(email, password);
			}
 
		}
		
		public void signInNow(final String email, String password){
			
			
			
			Parse.initialize(this, "x9n6KdzqtROdKDXDYF1n5AEoZLZKOih8rIzcbPVP", "JkqOqaHmRCA35t9xTtyoiofgG3IO7E6b82QIIHbF");
			ParseUser.logInInBackground(email, password, new LogInCallback() {
				
				@Override
				public void done(ParseUser user, ParseException e) {
					// TODO Auto-generated method stub
					if(e == null){
						Log.d("ProX Sign In","Sign in successful.");
						SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
						Editor editor = pref.edit();
					
						
						File folder = new File("data/data/com.radaee.reader/proxbooks/");
			            boolean success = true;
			            if (!folder.exists()) {
			                success = folder.mkdir();
			            }
			            
						ParseUser currentUser = ParseUser.getCurrentUser();
						String fname = (String) currentUser.get("first_name");
						String lname = (String) currentUser.get("last_name");
						Log.d("ProX Sign In","User"+fname+lname);
						String objectId = currentUser.getObjectId();
						
						editor.putString("objectId",objectId); // Storing boolean - true/false
						editor.putString("email", email); // Storing email
						editor.putString("fname", fname); // Storing first name
						editor.putString("lname", lname); // Storing last name
						editor.putString("readingnow", null);
						editor.commit();
						
						Intent in =  new Intent(MainActivity.this, MenuActivity.class);
	                	startActivity(in);
					}
					else{
						Log.d("ProX Sign In","Error signing in.");
						util.showAlertDialog(MainActivity.this,"Login", "Email address or password is incorrect. Please try again.", false);
					}
				}

			});

	     }
		
		 private void clearErrors(){
             editTextEmail.setError(null);
             editTextPassword.setError(null);
		 }
		 
		public boolean isLoggedIn(){
			
		     SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
		     Editor editor = pref.edit();
		     
		     pref.getString("email", null); 
		     String email = pref.getString("email", null);
 
		     if(email != null){
		    	 Log.d("LoggedIn", "true");
		    	 return true;
		     }       
		    return false;
		}
		
		
	     
	    
		@Override
		protected void onDestroy() {
			super.onDestroy();
		    // Close The Database
			//loginDataBaseAdapter.close();
		}

}
