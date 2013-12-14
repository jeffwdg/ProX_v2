package com.example.prox;


import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Button btnSignIn,btnSignUp;
	TextView linktosignup;
 
	 
	LoginDataBaseAdapter loginDataBaseAdapter;
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.actionbar, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// checks if already logged in - redirect to menu app
		boolean isloggedin = isLoggedIn();
		
		if(isloggedin == true){
			Intent intentMenu = new Intent(getApplicationContext(),MenuActivity.class);
		    startActivity(intentMenu);
		}else{
		
				setContentView(R.layout.main);
				
				// create a instance of SQLite Database
			    loginDataBaseAdapter=new LoginDataBaseAdapter(this);
			    loginDataBaseAdapter=loginDataBaseAdapter.open();
			     
			         
			    // Get The Reference Of Buttons
			    btnSignIn=(Button)findViewById(R.id.buttonSignIn);
			    btnSignUp=(Button)findViewById(R.id.buttonSignUP);
			    linktosignup = (TextView)findViewById(R.id.link_to_register);
					
			    // Set OnClick Listener on SignUp button 
			    btnSignUp.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 signUp();
					
					}
				});
  
		}
	 
	}
		public void signUp(){
			/// Create Intent for SignUpActivity  and Start The Activity
			Intent intentSignUP=new Intent(getApplicationContext(),SignUpActivity.class);
			startActivity(intentSignUP);
		}
		
		//Method to handleClick Event of Sign In Button
		public void signIn(View V)
		   {
				//final Dialog dialog = new Dialog(MainActivity.this);
				//dialog.setContentView(R.layout.signin);
			    //dialog.setTitle("Sign In ");
		
			    // get the References of views
			    final  EditText editTextEmail=(EditText)findViewById(R.id.editTextEmailToLogin);
			    final  EditText editTextPassword=(EditText)findViewById(R.id.editTextPasswordToLogin);
			    
				Button btnSignIn=(Button)findViewById(R.id.buttonSignIn);
					
				// Set On ClickListener
				btnSignIn.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						// get The User name and Password
						String email=editTextEmail.getText().toString();
						String password=editTextPassword.getText().toString();
						
						// fetch the Password form database for respective user name
						String storedPassword=loginDataBaseAdapter.getSingleEntry(email);
						
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
							Toast.makeText(MainActivity.this, "Email Address or Password does not match", Toast.LENGTH_LONG).show();
						}
					}
				});
				
				//dialog.show();
				
				/*
				Button forgotPass=(Button)dialog.findViewById(R.id.forgotPassword);
				
				// Set OnClick Listener on SignUp button 
				forgotPass.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					/// Create Intent for SignUpActivity  and Start The Activity
					Intent forgotPass=new Intent(getApplicationContext(),ForgotPassword.class);
					startActivity(forgotPass);
					
					}
				});
				*/
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
			loginDataBaseAdapter.close();
		}

}
