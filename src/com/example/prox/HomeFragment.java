package com.example.prox;

import java.util.List;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;	 
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class HomeFragment extends Fragment {
		 
	    public HomeFragment(){}
	    
	     
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	    	
	        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
	        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",1); // 0 - for private mode
		     
	        
		    String email = pref.getString("email", null);
		    String fname = pref.getString("fname", null);
		    String lname = pref.getString("lname", null);
		    
		    
		    //Toast.makeText(getActivity(), email, Toast.LENGTH_LONG).show(); 
		    TextView userEmail = (TextView) rootView.findViewById(R.id.userEmail);
		    TextView userName = (TextView) rootView.findViewById(R.id.userName);
		    
		    userName.setText(fname +" " + lname);
		    userEmail.setText("" + email);
		 
		    
	        return rootView;
	    }
	}

