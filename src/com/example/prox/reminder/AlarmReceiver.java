package com.example.prox.reminder;

import com.radaee.reader.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
 

public class AlarmReceiver extends BroadcastReceiver
{
         	@Override
            public void onReceive(Context context, Intent i)
            {		
                    String title = i.getExtras().getString("title");
                    String rsound = i.getExtras().getString("ringtone");
                    Toast.makeText(context, "Alarm " + title, Toast.LENGTH_LONG).show();
                    //Log.d("Alarm"," "+title+rsound);
                    
                    Uri soundUri =  Uri.parse(rsound);  
                    Ringtone r = RingtoneManager.getRingtone(context, soundUri);
                    //r.play();

                    Intent intent = new Intent(context, AlarmService.class);
                    intent.putExtra("title", title);
                    intent.putExtra("ringtone",	rsound);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(intent);

             }
 

      
}