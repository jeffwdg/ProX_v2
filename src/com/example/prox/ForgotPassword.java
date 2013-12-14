package com.example.prox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Dialog;
import android.telephony.SmsManager;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgotpassword);

	    txtforgotPass = (TextView)findViewById(R.id.btnforgotPassword);
		emailPassword=(Button)findViewById(R.id.btnforgotPassword);
 
		// Set On ClickListener
		emailPassword.setOnClickListener(new View.OnClickListener() {
					
			public void onClick(View v) {
				// get The User name and Password
				String email = emailPassword.getText().toString();
				String res = "Please check your email for your new password.";
				
				// check if any of the fields are vacant
				if(email.equals(""))
				{
					res = "Please provide your email address.";
						return;
				}else{
					String to = email;
					String subject = "Password Change Request";
					String message = "Please change your password by clicking this link";
					
							 String[] cc = {"busybody@example.com"};
							 sendSMS("+639434944879", "Hello my friends!");
							 //sendEmail(to, cc, subject, message);
				} 
				
				Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
		 		 
			}
		});
				    
	}
	
	//---sends an SMS message to another device---
	 private void sendSMS(String phoneNumber, String message)
	 {
		 SmsManager sms = SmsManager.getDefault();
		 sms.sendTextMessage(phoneNumber, null, message, null, null);
	 }
	 
	//---sends an SMS message to another device---
	 private void sendEmail(String emailAddresses, String[] carbonCopies,String subject, String message)
	 {
		 Intent emailIntent = new Intent(Intent.ACTION_SEND);
		 emailIntent.setData(Uri.parse("mailto:"));
		 String to = emailAddresses;
		 String[] cc = carbonCopies;
		 
		 emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
		 emailIntent.putExtra(Intent.EXTRA_CC, cc);
		 emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		 emailIntent.putExtra(Intent.EXTRA_TEXT, message);
		 emailIntent.setType("message/rfc822");
		 startActivity(Intent.createChooser(emailIntent, "Email"));
	 }
	 
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
