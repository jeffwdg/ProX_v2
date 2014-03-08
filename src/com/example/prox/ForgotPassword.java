package com.example.prox;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.radaee.reader.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Dialog;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class ForgotPassword extends Activity{
	
	TextView txtforgotPass;
	Button emailPassword;
	Utilities util= new Utilities();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgotpassword);
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

	    txtforgotPass = (TextView)findViewById(R.id.emailforgotPassword);
		emailPassword=(Button)findViewById(R.id.btnforgotPassword);
 
		// Set On ClickListener
		emailPassword.setOnClickListener(new View.OnClickListener() {
					
			public void onClick(View v) {
				// get The User name and Password
				final String email=txtforgotPass.getText().toString();
				View focusView = null;
				boolean cancel = false;
				String email_pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
				
				
				if(util.isOnline(getApplicationContext()) == true)
				{
					Toast.makeText(getApplicationContext(), "Resetting password...", Toast.LENGTH_SHORT).show();
					Log.d("ProX Forgot Password","" +email);
					
	 
					// check if any of the fields are vacant
					if(TextUtils.isEmpty(email))
					{
						txtforgotPass.setError(getString(R.string.required_field));
						focusView = txtforgotPass;
						cancel = true;
					}else if(!email.matches(email_pattern)){
						txtforgotPass.setError(getString(R.string.invalid_email));
						focusView =  txtforgotPass;
						cancel = true;
					}
					
					if(cancel == false){
						
						ParseUser.requestPasswordResetInBackground(email,new RequestPasswordResetCallback() {
						public void done(ParseException e) {
							if (e == null) {
							// An email was successfully sent with reset instructions.
								Log.d("ProX Forgot Password","An email notification was sent to " + email);
								util.showAlertDialog(ForgotPassword.this, "Forgot Password", "An email notification was sent to your email to continue reset your password.", false);
							} else {
								util.showAlertDialog(ForgotPassword.this, "Forgot Password", " An error occured. Please check your internet connection and try again.", false);
								Log.d("ProX Forgot Password","Unsuccessful");
							}
						}
						});
						
					}else{focusView.requestFocus();}
	 
				}else{
					util.showAlertDialog(ForgotPassword.this, "Network Error", "Please check your internet connection and try again.", false);
					
				}
				
				
				
				
			}
		});
				    
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
