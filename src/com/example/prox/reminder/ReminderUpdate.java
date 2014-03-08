package com.example.prox.reminder;

import java.util.Calendar;

import com.radaee.reader.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReminderUpdate extends Activity implements OnClickListener{
	ImageButton dateButton,timeButton;
	EditText dateText1,dateText2;
	CheckBox setAlarm;
	 static final int DATE_DIALOG_ID = 0;
	 static final int TIME_DIALOG_ID=1;
	 public  int year,month,day,hour,minute; 
	 private int mYear, mMonth, mDay, mHour,mMinute;
	 private EditText title,date,time,desc;
	private DataBaseHelper mHelper;
	public  ReminderDatabaseAdapter dataBase;
	private String stitle,sdate,stime,sdesc;
	int sid;
	public int reminderID;
	private boolean isUpdate;
	private SQLiteDatabase db;
	
	 public ReminderUpdate()
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
    		setAlarm = (CheckBox)findViewById(R.id.setAlarm);
    		
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

              Intent i = getIntent();
              	
              	sid=i.getExtras().getInt("id");
 	            stitle=i.getExtras().getString("title");
 	            sdate=i.getExtras().getString("date");
	            stime=i.getExtras().getString("time");
 	            sdesc=i.getExtras().getString("desc");
 	            
 	    
 	            title.setText(stitle);
	            date.setText(sdate); 	            
	            time.setText(stime);
	            desc.setText(sdesc);
 
 	         
 	        dataBase = new ReminderDatabaseAdapter(this);
 	        dataBase.open();
 	        registerViews();
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
    	   if ( checkValidation () )
    		   UpdateReminder();
           else
               Toast.makeText(ReminderUpdate.this, "Form contains error", Toast.LENGTH_LONG).show();

            UpdateReminder();
           return true;
      
        case R.id.action_cancel:
           GoToMain();
    
            return true;
       
        default:
            return super.onOptionsItemSelected(item);
        }
    }
 
    private void UpdateReminder() {
    	 Intent i = getIntent();
      
    	int rID =i.getExtras().getInt("id");
    	Log.d("Test","Title" + rID);
    	
         
    	 String rTitle =title.getText().toString();
         String rDate =date.getText().toString();
         String rTime =time.getText().toString();
         String rDesc =desc.getText().toString();
         boolean Alarmcheck = setAlarm.isChecked();
         
         rDate = rDate.trim();
         rTime = rTime.trim();
         
         boolean isFuture = scheduleAlarm(rDate,rTime,rTitle,Alarmcheck);
         
         if((isFuture == true && Alarmcheck == true ) ||  (isFuture == false && Alarmcheck == false ) ){
        	  dataBase.updateEntry(rID, rTitle, rDate, rTime, rDesc);
        	  Toast.makeText(getApplicationContext(), "Reminder updated successfully.", Toast.LENGTH_LONG).show();
              finish();
              Intent id = new Intent(this, MainActivity.class);
              startActivity(id);
         }
       
         
         
         //finish();
         //Intent ix = new Intent(this, MainActivity.class);
         //startActivity(ix);
    }
    
    public boolean scheduleAlarm(String alarmdate, String alarmtime, String etitle, boolean Alarmcheck)
    {		boolean isFuture = false;
    
    		Context context;
    		Calendar thatDay = Calendar.getInstance();
    		String[] tdate = alarmdate.split("-");
    		String[] ttime = alarmtime.split(":");
    		String[] ttimes = ttime[1].split(" ");
    		 
    		thatDay.clear(); 
    		thatDay.set(Calendar.MONTH,Integer.parseInt(tdate[0]) - 1); //0-11 so 1 less
    		thatDay.set(Calendar.YEAR, Integer.parseInt(tdate[2]));
    		thatDay.set(Calendar.DATE,Integer.parseInt(tdate[1]));
    		
 
    		
    		Log.d("",""+ttimes[1]);
    		
    		int ampm = 0;
    		Log.d("Morning",""+ttimes[1]+ampm);
            if(TextUtils.equals(ttimes[1], "PM")){
            	ampm = 1;
            	Log.d("Afternoon",""+ttimes[1]+ampm); 
    		}
            
            thatDay.set(Calendar.AM_PM, ampm);
			thatDay.set(Calendar.HOUR,Integer.parseInt(ttime[0]));
			thatDay.set(Calendar.MINUTE,Integer.parseInt(ttimes[0]));
			thatDay.set(Calendar.SECOND, 2);
			thatDay.set(Calendar.MILLISECOND, 5);
			 
			
    		Calendar today = Calendar.getInstance();
    		
    		String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    		String thatdate = java.text.DateFormat.getDateTimeInstance().format(thatDay.getTime());
            long time = thatDay.getTimeInMillis() - System.currentTimeMillis();
            Log.d("Time",""+ thatDay.get(Calendar.MONTH) +thatDay.get(Calendar.DATE)+thatDay.get(Calendar.HOUR)+" "+thatDay.get(Calendar.MINUTE) );
            Log.d("Time",""+ today.get(Calendar.MONTH) +today.get(Calendar.DATE)+today.get(Calendar.HOUR)+" "+today.get(Calendar.MINUTE) );
            Log.d("Today",""+mydate);
    		Log.d("Thatday",""+thatdate);
    		
            if(Alarmcheck == true){
            	
            	long xtime = (time / 1000 );
            	Log.d("Day",""+thatDay.getTimeInMillis()+"- "+System.currentTimeMillis());
            	
                if(thatDay.getTimeInMillis() > today.getTimeInMillis()){
                
                	Intent intent = new Intent(this, AlarmReceiver.class);
                	intent .putExtra("title", etitle);
                	intent .putExtra("ringtone",  "android.resource://com.radaee.reader/raw/ribs");
                	
            		PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, intent, 0);
            		
            		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            		alarmManager.set(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis()+  (xtime * 1000), pendingIntent);
            		
            		String alarm;
            		if(xtime < 60){
            			alarm = xtime + " seconds";
            		}else{
            			xtime = xtime/60;
            			if(xtime > 86400){
            				 
            				long xhour = xtime/60/24;
            				alarm = xhour + " day(s)";
            			}
            			else if(xtime > 3600){
            			
            				long xhour = xtime/60;
            				alarm = xhour + " hour(s)";
            			}else{
            				 
            				alarm = xtime + " minute(s)";
            			}
            			
            		}
            		isFuture = true;
             
            		Toast.makeText(this, "Alarm set in " + alarm,Toast.LENGTH_LONG).show();
                }else{
                	Toast.makeText(this, "Alarm can't be set because selected date is in the past. Select a future date and time.", Toast.LENGTH_LONG).show();
                	 
                }
            }
            
            return isFuture;
    }
    
    private void GoToMain() {
        Intent i = new Intent(this, Tablayout.class);
        i.putExtra("activetab", "1");
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
    		
     		private void registerViews() {
    	        title = (EditText) findViewById(R.id.editTitle);
    	        // TextWatcher would let us check validation error on the fly
    	        title.addTextChangedListener(new TextWatcher() {
    	            public void afterTextChanged(Editable s) {
    	                ReminderValidation.TitleValid(title,true);
    	            }
    	            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
    	            public void onTextChanged(CharSequence s, int start, int before, int count){}
    	        });

    	      
    	    }
    		   private boolean checkValidation() {
       	        boolean ret = true;

       	        if (!ReminderValidation.TitleValid(title,true)) ret = false;
         	        if (!ReminderValidation.DateValid(date, true)) ret = false;
       	        if (!ReminderValidation.TimeValid(time, true)) ret = false;

       	        return ret;
       	    }

       }
