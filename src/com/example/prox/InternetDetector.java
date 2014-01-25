package com.example.prox;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class InternetDetector{

	 private Context _context;
     
     public InternetDetector(Context context){
             this._context = context;
     }
     
 	public boolean isNetworkAvailable() {
 		boolean x = false;
	    ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected()) {
          x= true;
	    }
 
	    return x;
	}
 	
 	
 	
     public boolean isConnectedToInternet(){
    	 boolean x = false;
         ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
         
         if (connectivity != null) 
         {
        	 NetworkInfo[] info = connectivity.getAllNetworkInfo();
        	 if (info != null) 
        		 for (int i = 0; i < info.length; i++) 
        			 if (info[i].getState() == NetworkInfo.State.CONNECTED)
        			 {	
        				  
        				 HttpURLConnection urlc;
						try {
							urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
						    urlc.setRequestProperty("User-Agent", "Test");
	        			     urlc.setRequestProperty("Connection", "close");
	        			     urlc.setConnectTimeout(1500); 
	        			     urlc.connect();
	        			     if((urlc.getResponseCode() == 200)){
	        			    	 x = true;
	        			     }
						} catch (MalformedURLException e) {
							Log.e("Connection error", "Error checking internet connection", e);
							e.printStackTrace();
						} catch (IOException e) {
							Log.e("Connection error", "Error checking internet connection", e);
							e.printStackTrace();
						}
        			 
        			 }
         }
         return x;
	}
     
}
