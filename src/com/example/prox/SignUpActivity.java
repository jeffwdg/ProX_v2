package com.example.prox;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.radaee.reader.R;

public class SignUpActivity extends Activity implements OnClickListener
{
	EditText editTextEmail,editTextPassword,editTextConfirmPassword, editTextFirstName, editTextLastName;
	TextView editText1, editText2;
	Button btnCreateAccount;
	 // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    InternetDetector internetdetected;
	LoginDataBaseAdapter loginDataBaseAdapter;
	Utilities util;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		internetdetected = new InternetDetector(getApplicationContext());
		
		// get Instance  of Database Adapter
		//loginDataBaseAdapter=new LoginDataBaseAdapter(this);
		//loginDataBaseAdapter=loginDataBaseAdapter.open();
		util = new Utilities();
		
		// Get References of Views
		editText1 = (TextView)findViewById(R.id.textView1);
		editText2 = (TextView)findViewById(R.id.textView2);
		
		editTextEmail = (EditText)findViewById(R.id.editTextEmail);
		editTextPassword=(EditText)findViewById(R.id.editTextPassword);
		editTextConfirmPassword=(EditText)findViewById(R.id.editTextConfirmPassword);
		editTextFirstName = (EditText)findViewById(R.id.editTextFirstName);
		editTextLastName = (EditText)findViewById(R.id.editTextLastName);
		
		btnCreateAccount=(Button)findViewById(R.id.buttonCreateAccount);
		btnCreateAccount.setOnClickListener(this);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "daddysgirl.ttf");
		editTextEmail.setTypeface(font);
		editTextPassword.setTypeface(font);
		editTextConfirmPassword.setTypeface(font);
		editTextFirstName.setTypeface(font);
		editTextLastName.setTypeface(font);
		btnCreateAccount.setTypeface(font);
		
	}
	
	public void onClick(View v) {
		
			switch(v.getId()){
				case R.id.buttonCreateAccount:
						isInternetPresent = internetdetected.isNetworkAvailable();
						
						if(isInternetPresent == true){
							createAccount();
							Log.d("ProX User Signup", "Connected to internet.." + isInternetPresent);
						}else{
							showAlertDialog(getApplicationContext(), "No Internet Connection",
	                                "You don't have internet connection.", false);
						}
						break;

                default:
                        break;
                }
	}
			
			
	 
	@SuppressWarnings("deprecation")
	private void showAlertDialog(Context context, String title, String message, boolean status) {
			AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	
	        alertDialog.setTitle(title);
	        alertDialog.setMessage(message);
	        alertDialog.setIcon(R.drawable.ic_action_network_wifi);
	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                }
	        });
	
	        alertDialog.show();
        
	}

	private void createAccount(){
		clearErrors();
		boolean cancel = false;
        View focusView = null;
		 
		String email=editTextEmail.getText().toString();
		String password=editTextPassword.getText().toString();
		String confirmPassword=editTextConfirmPassword.getText().toString();
		String fname=editTextFirstName.getText().toString();
		String lname=editTextLastName.getText().toString();
		String pattern = "^[a-zA-Z0-9]*$";
		String email_pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
		//check for a valid password
		if(TextUtils.isEmpty(password)){
				editTextPassword.setError(getString(R.string.required_field));
				focusView = editTextPassword;
				cancel =true;
		}else if(password.length() < 7 || !password.matches(pattern)){
				editTextPassword.setError(getString(R.string.invalid_password));
				focusView = editTextPassword;
				cancel =true;
		}
				
		//Check if password & confirm password matches
		if(TextUtils.isEmpty(confirmPassword)){
			editTextConfirmPassword.setError(getString(R.string.required_field));
			focusView = editTextConfirmPassword;
			cancel =true;
			
		} else if(password != null && !confirmPassword.equals(password)){
			editTextConfirmPassword.setError(getString(R.string.invalid_confirm_password));
			focusView = editTextPassword;
			cancel =true;
			
		}
		
		
		
		//check for a valid email
		if(TextUtils.isEmpty(email)){
			editTextEmail.setError(getString(R.string.required_field));
			focusView = editTextEmail;
			cancel =true;
		}else if(!email.matches(email_pattern)){
			editTextEmail.setError(getString(R.string.invalid_email));
			focusView = editTextEmail;
			cancel =true;
		}
		
		// check if first & last name are valid
		if(TextUtils.isEmpty(fname)){
			editTextFirstName.setError(getString(R.string.required_field));
			focusView = editTextFirstName; 
			cancel =true;
		}
		if(TextUtils.isEmpty(lname)){
			editTextLastName.setError(getString(R.string.required_field));
			focusView = editTextLastName;
			cancel =true;
		}
		
		if(cancel){
			focusView.requestFocus();
		} else{
			signUp(email, password, fname, lname);
		}
		/*
		// check if any of the fields are vacant
		if(email.equals("") || password.equals("") || confirmPassword.equals("") || fname.equals("") || lname.equals(""))
		{
				Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_LONG).show();
				return;
		}
		
			//check if email already exist
			if(loginDataBaseAdapter.isExisting(email) == 1){
				
				Toast.makeText(getApplicationContext(), "Email address already taken. Try another.", Toast.LENGTH_LONG).show();
				
			}else{
				
				if(util.isValidEmail(email) == true){
					// check if both password matches
					if(!password.equals(confirmPassword)){
						Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
						return;
					}
					else
					{	
						 
						//Save in Shared Preferences
						SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
						Editor editor = pref.edit();
						editor.putString("email", email); // Storing email
						editor.putString("fname", fname); // Storing first name
						editor.putString("lname", lname); // Storing last name
						//editor.putBoolean("isLoggedIn", true); // Storing boolean - true/false
						editor.commit();
						
					    // Save the Data in Database
					    loginDataBaseAdapter.insertEntry(email, password, fname, lname);
					    Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "Email address is invalid. Try another.", Toast.LENGTH_LONG).show();
				}
				
			}
			*/
	}
	
	private void signUp(final String email, String password, final String fname, final String lname){
		Toast.makeText(getApplicationContext(), "Sign up.. Please wait for a moment.", Toast.LENGTH_LONG).show();
		 Parse.initialize(this, "x9n6KdzqtROdKDXDYF1n5AEoZLZKOih8rIzcbPVP", "JkqOqaHmRCA35t9xTtyoiofgG3IO7E6b82QIIHbF");
		
		 ParseUser user = new ParseUser();
         user.setUsername(email);
         user.setPassword(password);
         user.setEmail(email);
         user.put("first_name", fname);
         user.put("last_name", lname);
         
         
         user.signUpInBackground(new SignUpCallback () {
           public void done(ParseException e) {
             if (e == null) {
            	//Save in Shared Preferences
				SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
				Editor editor = pref.edit();
				editor.putString("email", email); // Storing email
				editor.putString("fname", fname); // Storing first name
				editor.putString("lname", lname); // Storing last name
				
				ParseUser currentUser = ParseUser.getCurrentUser();
				String objectId = currentUser.getObjectId();
				editor.putString("objectId",objectId); // Storing boolean - true/false
				Log.d("ProX User Signup", currentUser.getObjectId());
				editor.commit();
					
               signUpMsg("Account created successfully. Logging in...");
               Intent in = new Intent(getApplicationContext(), MenuActivity.class);
               startActivity(in);
             } else {
               // Sign up didn't succeed. Look at the ParseException
               // to figure out what went wrong
                     signUpMsg("This email address is already taken.");
             }
           }

         });
	}
	
	protected void signUpMsg(String msg) {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();        
}
	
	private void clearErrors(){ 
			editTextEmail.setError(null);
			editTextPassword.setError(null);
			editTextConfirmPassword.setError(null);
			editTextFirstName.setError(null);
			editTextLastName.setError(null);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
}

