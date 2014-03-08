package com.example.prox;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.radaee.reader.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class Utilities extends Activity {
	FileOutputStream fos;
	
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
	
	public boolean isLoggedIn(Context context){
		boolean x = false;
	     SharedPreferences pref = context.getSharedPreferences("MyPref", MODE_WORLD_READABLE); // 0 - for private mode
	     Editor editor = pref.edit();
	     
	     pref.getString("email", null); 
	     String email = pref.getString("email", null);

	     if(email != null || !TextUtils.isEmpty(email)){
	    	 Log.d("LoggedIn", "true" +email);
	    	 x= true;
	     }       
	    return x;
	}
	
	public static boolean deleteDirectory(File path) {

        if( path.exists() ) {
            File[] files = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
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
	
	/*
	  * This methods saves the downloaded image to sdcard
	  */
	 public void saveImageToSD2(String userFolderName, String objectId, Bitmap bmp) {
		 	//Bitmap bmp = null;
		 	
			 File folder = new File("data/data/com.radaee.reader/proxbooks");
	         boolean success = true;
	         
	         if(!folder.exists()) {
	             success = folder.mkdir(); // create proxbooks folder
	             Log.d("Ebook Folder Creation", "Created main folder.");
	         }else{
	         	Log.d("Ebook Folder Creation", "Failed to create main folder.");
	         }
	         
	         File efolder = new File("data/data/com.radaee.reader/proxbooks/"+userFolderName);
             if(!efolder.exists()) {success = efolder.mkdir();}
             
		 	Log.d("Ebook Save Cover", "Saving cover...");
		 
		    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		    //you can select your preferred CompressFormat and quality.
		    bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		    
		    // create a new file on SD card  
		    File file = new File("data/data/com.radaee.reader/proxbooks/"+userFolderName+"/"+objectId+".jpg");
		    
		    try {
		        file.createNewFile();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		    // create a new FileOutputStream and write bytes to file
		    try {
		        fos = new FileOutputStream(file);
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }
		    
		    try {
		        fos.write(bytes.toByteArray());
		        fos.close();
		        Log.d("Ebook Local Storage", "Cover saved..");
		        //Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
		    } catch (IOException e) {
		    	Log.d("Ebook Local Storage", "Cover not saved.");
		        e.printStackTrace();
		    }

		}
	 
	 public boolean isOnline(Context context) {
	    
		 Log.d("", "Checking if online...");
		 Toast.makeText(context, "Checking internet connection. One moment please.", Toast.LENGTH_LONG).show();
		 
    	 if (android.os.Build.VERSION.SDK_INT > 9) {
    	      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	      StrictMode.setThreadPolicy(policy);
    	    }
    	 
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
            if (cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
                URL url = new URL("http://www.google.com");
                HttpURLConnection urlc = (HttpURLConnection) url .openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close"); 
                urlc.setConnectTimeout(2000); // mTimeout is in seconds
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return false;
    }
	

}
