package com.example.prox;


import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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

public class MainActivity extends Activity {
	
	Button btnSignIn,btnSignUp, btnStore;
	TextView linktosignup, linktoforgetPassword;
	Utilities util = new Utilities();
	EditText editTextEmail, editTextPassword;
    Boolean isInternetPresent = false;
    // Connection detector class
    InternetDetector internetdetected;
    
 
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		internetdetected = new InternetDetector(getApplicationContext());
		
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
		
	    btnSignIn.setOnClickListener(new OnClickListener() {
	    	
			public void onClick(View v) {
				//check if internet is present
				boolean isloggedin = isLoggedIn();
				
					if(isInternetPresent == true){
						Log.d("ProX App Initialization","Internet is detected.");
						// checks if already logged in - redirect to menu app
						if(isloggedin == true){
							Intent intentMenu = new Intent(getApplicationContext(),MenuActivity.class);
						    startActivity(intentMenu);
						}else{
							Log.d("ProX Sign In","Signing in...");
								signIn();				
						}
						
					}else{
						Log.d("ProX App","No Internet connection detected.");
						showAlertDialog(MainActivity.this, "No Internet Connection", "You don't have internet connection.", false);
						
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
				signInNow(email, password);
			}
			
			    /*
				Button btnSignIn=(Button)findViewById(R.id.buttonSignIn);
					
				// Set On ClickListener
				btnSignIn.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						// get The User name and Password

						
						// fetch the Password form database for respective user name
						String storedPassword=loginDataBaseAdapter.getSingleEntry(email);
						
						if(util.isValidEmail(email)){
							// check if the Stored password matches with  Password entered by user
							if(password.equals(storedPassword))
							{
								Toast.makeText(MainActivity.this, "Congrats. You are logged in.", Toast.LENGTH_LONG).show();
								//dialog.dismiss();
								
								/// Create Intent for MenuActivity  and Start The Activity
								Intent menuActivity=new Intent(getApplicationContext(),MenuActivity.class);
								startActivity(menuActivity);
							}
							else
							{
								Toast.makeText(MainActivity.this, "Email address or Password does not match.", Toast.LENGTH_LONG).show();
							}
						}else{
							Toast.makeText(MainActivity.this, "Email address is invalid.", Toast.LENGTH_LONG).show();
						}
						
					}
				});
				*/
 
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
					
						
						ParseUser currentUser = ParseUser.getCurrentUser();
						String fname = (String) currentUser.get("first_name");
						String lname = (String) currentUser.get("last_name");
						Log.d("ProX Sign In","User"+fname+lname);
						String objectId = currentUser.getObjectId();
						
						editor.putString("objectId",objectId); // Storing boolean - true/false
						editor.putString("email", email); // Storing email
						editor.putString("fname", fname); // Storing first name
						editor.putString("lname", lname); // Storing last name
						editor.commit();
						
						Intent in =  new Intent(MainActivity.this,MenuActivity.class);
	                	startActivity(in);
					}
					else{
						Log.d("ProX Sign In","Error signing in.");
						 showAlertDialog(MainActivity.this,"Login", "Username or Password is invalid.", false);
					}
				}

			});

	     }
		
		
		 private void clearErrors(){
             editTextEmail.setError(null);
             editTextPassword.setError(null);
		 }
		 
		@SuppressWarnings("deprecation")
        public void showAlertDialog(Context context, String title, String message, Boolean status) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                // Setting Dialog Title
                alertDialog.setTitle(title);

                // Setting Dialog Message
                alertDialog.setMessage(message);

                // Setting alert dialog icon
                alertDialog.setIcon(R.drawable.ic_action_network_wifi);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                });

                // Showing Alert Message
                alertDialog.show();
        }

		
		public boolean isLoggedIn(){
			
		     SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
		     Editor editor = pref.edit();
		     
		     pref.getString("email", null); // getting email
		     //pref.getBoolean("isLoggedIn", null); // getting Boolean
		     
		     String email = pref.getString("email", null);
		     //Toast.makeText(MainActivity.this, email, Toast.LENGTH_LONG).show();  
		     
		     if(email != null){
		           //Intent intentMenu = new Intent(getApplicationContext(),MenuActivity.class);
		           //startActivity(intentMenu);
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
