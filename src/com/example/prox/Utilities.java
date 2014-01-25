package com.example.prox;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.radaee.reader.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class Utilities extends Activity {
 
	public boolean isValidEmail(String email){
		//boolean $valid = true;
		
		Pattern pattern;
	    Matcher matcher;
	    final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	    pattern = Pattern.compile(EMAIL_PATTERN);
	    matcher = pattern.matcher(email);
	    return matcher.matches();
		
	}
	
	public boolean isNumeric(String str){
		String pattern = "[1-9][0-9]{0,8}";
		boolean ret = false;
		if(str.matches(pattern)){
			ret =true;
		}
		Log.d("Util", "IsNumeric "+ret);
		return ret;
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
	
	public static long getAvailableInternalMemorySize() {
		 
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        long size = availableBlocks * blockSize /  (1024);
        
        return size;
    }
	
	
	public long getFreeMemory()
    {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long Free  = (statFs.getAvailableBlocks() *  (long) statFs.getBlockSize()) / 1048576;
        return Free;
    }
	

}
