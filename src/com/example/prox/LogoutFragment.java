package com.example.prox;

 
import java.io.File;

import com.example.prox.adapter.EbookDatabaseAdapter;
import com.example.prox.note.NotesDbAdapter;
import com.example.prox.note.SubjectDbAdapter;
import com.example.prox.reminder.ReminderAdd;
import com.example.prox.reminder.ReminderDatabaseAdapter;
import com.example.prox.reminder.ReminderThisMonth;
import com.parse.ParseUser;
import com.radaee.reader.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LogoutFragment extends Fragment {
	Button btnlogOut;
	public LogoutFragment(){}
	Utilities util = new Utilities();
	Context context;
	String userFolderName;
	SharedPreferences pref;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		


	    
		pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",0); // 0 - for private mode
		
		//delete usesr folder and remaingin contents
		pref.getString("email", null); 
	    userFolderName = pref.getString("email", null);
	    
		if( TextUtils.isEmpty(userFolderName) || userFolderName == null){
			
			Log.d("Not","Login");
        	Intent intent = new Intent(getActivity(), MainActivity.class);
			startActivity(intent);
			
			
        }
		else{
			
        View rootView = inflater.inflate(R.layout.fragment_logout, container, false);
         

	        util.showAlertDialog(getActivity(), "Log out", "By clicking yes, all of your data stored locally will be deleted. Please click yes if you wish to proceed. ", false);
	        
	        btnlogOut=(Button) rootView.findViewById(R.id.sureLogout);
				
		    // Set OnClick Listener on SignUp button 
	        btnlogOut.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Toast.makeText(getActivity(), "Logging out..", Toast.LENGTH_SHORT).show();
			    File userFolder = new File("data/data/com.radaee.reader/proxbooks/"+userFolderName); 
			    
				util.deleteDirectory(userFolder);
				
			    EbookDatabaseAdapter ebookDatabaseAdapter = new EbookDatabaseAdapter(getActivity());
				ebookDatabaseAdapter.open();
				
				NotesDbAdapter noteDatabaseAdapter = new NotesDbAdapter(getActivity());
				noteDatabaseAdapter.open();
				
				ReminderDatabaseAdapter reminderDatabaseAdapter = new ReminderDatabaseAdapter(getActivity());
				reminderDatabaseAdapter.open();
				
				SubjectDbAdapter subjectDatabaseAdapter = new SubjectDbAdapter(getActivity());
				subjectDatabaseAdapter.open();
				
				pref.edit().remove("email").commit();
				pref.edit().remove("objectId").commit();
				pref.edit().remove("fname").commit();
				pref.edit().remove("lname").commit();
				

				ParseUser.logOut();
				ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
				
				ebookDatabaseAdapter.emptyUserEbooks();
				noteDatabaseAdapter.emptyUserNotes();
				reminderDatabaseAdapter.emptyUserReminders();
				subjectDatabaseAdapter.emptyUserSubjects();
 
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getActivity().finish();
				
				
				Intent ix = new Intent(getActivity(), MainActivity.class);
				startActivity(ix);
		        System.exit(0);
				//startActivity(intent);
				
				}
			});
        
	        
	        return rootView;
		}
		return container;
    }
		
 
	
}
