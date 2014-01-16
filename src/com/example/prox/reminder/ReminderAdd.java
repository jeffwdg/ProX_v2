package com.example.prox.reminder;

import java.util.Calendar;

import com.radaee.reader.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReminderAdd extends Activity implements OnClickListener{
	ImageButton dateButton,timeButton;
	EditText dateText1,dateText2;
	 static final int DATE_DIALOG_ID = 0;
	 static final int TIME_DIALOG_ID=1;
	 public  int year,month,day,hour,minute; 
	 private int mYear, mMonth, mDay, mHour,mMinute;
	 private EditText title,date,time,desc;
	private DataBaseHelper mHelper;
	public  ReminderDatabaseAdapter dataBase;
	private String stitle,sdate,stime,sdesc;
	private boolean isUpdate;
	private SQLiteDatabase db;
	
	 public ReminderAdd()
     {
                 // Assign current Date and Time Values to Variables
                 final Calendar c = Calendar.getInstance();
                 mYear = c.get(Calendar.YEAR);
                 mMonth = c.get(Calendar.MONTH);
                 mDay = c.get(Calendar.DAY_OF_MONTH);
                 mHour = c.get(Calendar.HOUR_OF_DAY);
                 mMinute = c.get(Calendar.MINUTE);
                
     }
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_add);
 
        // get action bar   
        ActionBar actionBar = getActionBar();
 
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
     		
        	
    		dateButton =(ImageButton)findViewById(R.id.imageButton1);
    		timeButton=(ImageButton)findViewById(R.id.imageButton2);
    		dateText1 = (EditText)findViewById(R.id.editText1);
    		dateText2 = (EditText)findViewById(R.id.editText2);
    		dateButton.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
                         // Show the DatePickerDialog
                          showDialog(DATE_DIALOG_ID);
     
    			}
     
    		});
    		timeButton.setOnClickListener(new View.OnClickListener() {
                
                public void onClick(View v) {
                    // Show the TimePickerDialog
                     showDialog(TIME_DIALOG_ID);
                }
            });
    		
    		 title = (EditText) findViewById(R.id.editTitle);
              date = (EditText) findViewById(R.id.editText1);
              time = (EditText) findViewById(R.id.editText2);
              desc = (EditText) findViewById(R.id.editDesc);
//    		
//    	//database	

// 	         
// 	       isUpdate=getIntent().getExtras().getBoolean("update");
// 	        if(isUpdate)
// 	        {
// 	            stitle=getIntent().getExtras().getString("Title");
// 	            sdate=getIntent().getExtras().getString("Sdate");
// 	            stime=getIntent().getExtras().getString("Stime");
// 	            sdesc=getIntent().getExtras().getString("Sdesc");
// 	            title.setText(stitle);
// 	            date.setText(sdate);
// 	            time.setText(stime);
// 	            desc.setText(sdesc);
// 	            
// 	        }
 	         
 	        
 	         
 	        dataBase = new ReminderDatabaseAdapter(this);
 	        dataBase.open();
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.reminder_add_actions, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
 //action bar menu   
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
       switch (item.getItemId()) {
       case R.id.action_save:
            SaveReminder();
           return true;
      
        case R.id.action_cancel:
           GoToMain();
    
            return true;
       
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private void SaveReminder() {
 
    	 String rTitle =title.getText().toString();
         String rDate =date.getText().toString();
         String rTime =time.getText().toString();
         String rDesc =desc.getText().toString();
         rDate = rDate.trim();
         rTime = rTime.trim();
         Log.d("Test","Title" + rTitle);
         dataBase.insertEntry(rTitle,rDate,rTime,rDesc);
         
         Toast.makeText(getApplicationContext(), "Reminder added successfully.", Toast.LENGTH_LONG).show();
         finish();
         GoToMain();
    }
    
    private void GoToMain() {
        Intent i = new Intent(this, Tablayout.class);
        startActivity(i);
    }
    
    
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
        // the callback received when the user "sets" the Date in the DatePickerDialog
                public void onDateSet(DatePicker view, int yearSelected,
                                      int monthOfYear, int dayOfMonth) {
                   year = yearSelected;
                   month = monthOfYear +1;
                   day = dayOfMonth;
                   // Set the Selected Date in Select date Button
                   dateText1.setText(" "+month+"-"+day+"-"+year);
                }
            };
            
            private TimePickerDialog.OnTimeSetListener mTimeSetListener =
                    new TimePickerDialog.OnTimeSetListener() {
             // the callback received when the user "sets" the TimePickerDialog in the dialog
                        public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                            hour = hourOfDay;
                            minute = min;
                            // Set the Selected Date in Select date Button
                            updateTime(hour,minute);
                          }
                    };
            
            @Override
            protected Dialog onCreateDialog(int id) {
                switch (id) {
                case DATE_DIALOG_ID:
         // create a new DatePickerDialog with values you want to show 
                    return new DatePickerDialog(this,
                                mDateSetListener,
                                mYear, mMonth, mDay);
                case TIME_DIALOG_ID:
                    return new TimePickerDialog(this,
                            mTimeSetListener, mHour, mMinute, false);
                }
                    return null;
                }
            
            private static String utilTime(int value) {
                
                if (value < 10)
                    return "0" + String.valueOf(value);
                else
                    return String.valueOf(value);
            }
             
            // Used to convert 24hr format to 12hr format with AM/PM values
            private void updateTime(int hours, int mins) {
                 
                String timeSet = "";
                if (hours > 12) {
                    hours -= 12;
                    timeSet = "PM";
                } else if (hours == 0) {
                    hours += 12;
                    timeSet = "AM";
                } else if (hours == 12)
                    timeSet = "PM";
                else
                    timeSet = "AM";
         
                 
                String minutes = "";
                if (mins < 10)
                    minutes = "0" + mins;
                else
                    minutes = String.valueOf(mins);
         
                // Append in a StringBuilder
                 String aTime = new StringBuilder().append(hours).append(':')
                        .append(minutes).append(" ").append(timeSet).toString();
         
                  dateText2.setText(aTime);
            }
            
            // saveButton click event 
            public void onClick(View v){
//                switch(v.getId()){
//                    case R.id.action_cancel:
//                    		finish();
//                    	break;
//                    case R.id.action_save:
//		                   
//		                    
//		                    String rTitle =title.getText().toString();
//		                    String rDate =date.getText().toString();
//		                    String rTime =time.getText().toString();
//		                    String rDesc =desc.getText().toString();
//		                  
//		                    Log.d("Test","Title" + rTitle);
//		                    dataBase.insertEntry(rTitle,rDate,rTime,rDesc);
//		                    Toast.makeText(getApplicationContext(), "Reminder added successfully.", Toast.LENGTH_LONG).show();
//		                  //  showDialog(DIALOG_ID);
//	                   
//		                    finish();
//                        break;
//                }
                }
    	    
    	 
    		public void onClick1(View arg0) {
    			// TODO Auto-generated method stub
    			
    		}


       }
