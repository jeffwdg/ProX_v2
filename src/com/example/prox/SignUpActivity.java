package com.example.prox;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity
{
	EditText editTextEmail,editTextPassword,editTextConfirmPassword, editTextFirstName, editTextLastName;
	Button btnCreateAccount;
	
	LoginDataBaseAdapter loginDataBaseAdapter;
	Utilities util;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		
		// get Instance  of Database Adapter
		loginDataBaseAdapter=new LoginDataBaseAdapter(this);
		loginDataBaseAdapter=loginDataBaseAdapter.open();
		util = new Utilities();
		
		// Get References of Views
		editTextEmail = (EditText)findViewById(R.id.editTextEmail);
		editTextPassword=(EditText)findViewById(R.id.editTextPassword);
		editTextConfirmPassword=(EditText)findViewById(R.id.editTextConfirmPassword);
		editTextFirstName = (EditText)findViewById(R.id.editTextFirstName);
		editTextLastName = (EditText)findViewById(R.id.editTextLastName);
		
		btnCreateAccount=(Button)findViewById(R.id.buttonCreateAccount);
		btnCreateAccount.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			String email=editTextEmail.getText().toString();
			String password=editTextPassword.getText().toString();
			String confirmPassword=editTextConfirmPassword.getText().toString();
			String fname=editTextFirstName.getText().toString();
			String lname=editTextLastName.getText().toString();
			
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
			}
		});
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		loginDataBaseAdapter.close();
	}
}

