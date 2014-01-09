package com.example.prox;

import java.util.List;

 
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
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
import com.radaee.reader.R;


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
		    TextView textView1 = (TextView) rootView.findViewById(R.id.textView1);
		    TextView textView2 = (TextView) rootView.findViewById(R.id.textView2);
		    TextView textView3 = (TextView) rootView.findViewById(R.id.textView3);
		    TextView userEmail = (TextView) rootView.findViewById(R.id.userEmail);
		    TextView userName = (TextView) rootView.findViewById(R.id.userName);
		    
		    SpannableString s = new SpannableString("Account");
	        s.setSpan(new TypefaceSpan("daddysgirl.ttf"), 0, s.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        
	        // Update the action bar title with the TypefaceSpan instance
	        ActionBar actionBar = getActivity().getActionBar();
	        actionBar.setTitle(s);
	        
		    Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "daddysgirl.ttf");
		    textView1.setTypeface(font);
		    textView2.setTypeface(font);
		    textView3.setTypeface(font);
			userEmail.setTypeface(font);
			userName.setTypeface(font);
			
		    userName.setText(fname +" " + lname);
		    userEmail.setText("" + email);
		 
		    
	        return rootView;
	    }
	}

