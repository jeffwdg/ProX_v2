package com.example.prox.reminder;

import com.radaee.reader.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;



public class AlarmService extends Service
{
     
 
   private NotificationManager mNotificationManager;
   
   public AlarmService(){
	   
   }
   
 
    @Override
    public void onDestroy() 
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    
    public int onStartCommand(Intent intent, int flags, int startId) {
      //TODO do something useful
    	   
    	final String title = intent.getExtras().getString("title");
    	final String rsound = intent.getExtras().getString("ringtone");
        Uri soundUri =  Uri.parse(rsound);
    	Toast.makeText(getApplicationContext(), "Reminder "+title, Toast.LENGTH_LONG).show();
   	 	Log.d("Reminder",""+title);
   	 	
        final Ringtone r = RingtoneManager.getRingtone(getBaseContext(), soundUri);
        r.play();
        
        showNotification(title);
        
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Alarm");
        alertDialog.setMessage(title);
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	r.stop();
            }
        });
        //alertDialog.show();
        
 
        return Service.START_STICKY;
    }
    
    /**
     * Show a notification while this service is running.
     */
    private void showNotification(String title) {
    	NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(this)
	    .setSmallIcon(R.drawable.ic_launcher)
	    .setContentTitle("Reminder")
	    .setContentText(title);

		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this,0,intent,Intent.FLAG_ACTIVITY_NEW_TASK);
		mBuilder.setContentIntent(pi);
		
		mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());
    }

	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		
		
		return null;
	}

}
