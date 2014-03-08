package com.example.prox.note;

import java.util.ArrayList;

import com.radaee.reader.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class SubjectList extends ListActivity{
	
	EditText editTextSubject;
	public SubjectDbAdapter mDbHelper;
	public NotesDbAdapter nDbHelper;
	private String delete_id;
	ArrayList<Pair> pairs;
	ArrayList<Pair> scategory;
	 private Long mRowId;
	 private static final int ACTIVITY_CREATE=0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subjectslist);
		mDbHelper = new SubjectDbAdapter (this);
		mDbHelper.open();
		nDbHelper= new NotesDbAdapter(this);
		nDbHelper.open();
		fillData();

		if(mDbHelper.countSubjects()<1)
		{
			Toast.makeText(getApplicationContext(), "No subject(s)", Toast.LENGTH_SHORT).show();
		}
        
	ListView list = getListView();
	
	
    list.setOnItemLongClickListener(new OnItemLongClickListener() {

    @Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
    	Pair temp=(Pair) arg1.getTag();
    	delete_id=temp.getId();
    	showDeleteDialog();
		// TODO Auto-generated method stub
		return false;
	}
    });
    
    
    	

	Button addsubject = (Button)findViewById(R.id.addbuttonsubj);
	
		addsubject.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				addsubject();
				}
		});
		
	}

	 @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
		 
		 Pair item=(Pair) v.getTag();
		 
		 Intent intent;
		 intent= new Intent(this,CategorizeNotes.class);
		 intent.putExtra("subject", item.getDesc());
		 
		 startActivity(intent);
	        
		
   }
	        
	
	 
public void showDeleteDialog()
{	Builder builder = new AlertDialog.Builder(this);
	builder.setMessage("Confirm Delete?");
	builder.setCancelable(true);
	builder.setPositiveButton("Yes", new OkOnClickListener());
	builder.setNegativeButton("No", new CancelOnClickListener());
	AlertDialog dialog = builder.create();
	dialog.show();
	
}

public void showAlertDialog()
{	Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("ERROR DELETION");
	builder.setMessage("Cannot delete, subject is being used. Please delete all notes under subject first.");
	builder.setCancelable(true);
	builder.setNegativeButton("Ok", new CancelOnClickListener());
	AlertDialog dialog = builder.create();
	dialog.show();
	
}

private final class CancelOnClickListener implements DialogInterface.OnClickListener {
	public void onClick(DialogInterface dialog, int which) {
	
	}
}

private final class OkOnClickListener implements DialogInterface.OnClickListener 
{	public void onClick(DialogInterface dialog, int which) 
	{	Cursor subjcursor;
	
	     subjcursor=mDbHelper.fetchSubject(delete_id);
	     String subjval = subjcursor.getString(subjcursor.getColumnIndex("subjectname")).toString();
	    //Toast.makeText(getApplicationContext(), subjval, Toast.LENGTH_SHORT).show();
	    
	    
		if(nDbHelper.findSubjectIfExist(subjval)==true)
		{	
			showAlertDialog();		}
		else
		{
			mDbHelper.deleteSubject(delete_id);
			
		}
		
		fillData();
	}

	
}
public void showErrorDialog()
{
	Builder builder = new AlertDialog.Builder(this);
	builder.setMessage("Subject already exists");
	builder.setCancelable(true);
	builder.setNegativeButton("Ok", new CancelOnClickListener());
	AlertDialog dialog = builder.create();
	dialog.show();
}





 public void addsubject(){
	 
	final Dialog dialog = new Dialog(SubjectList.this);
	dialog.setContentView(R.layout.subject_add);
    dialog.setTitle("Add Subject");

    // get the References of views
    editTextSubject=(EditText)dialog.findViewById(R.id.editTextSubject);
    final Button btnSave=(Button)dialog.findViewById(R.id.savebtn);
    
    editTextSubject.addTextChangedListener(new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        if( s.length()==0)
        {
                           btnSave.setEnabled(false);
        }
         else
         {                   btnSave.setEnabled(true);

        }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {

        	
        }

     

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			if(arg0.toString().length()>0)
			{
			char char_check=arg0.charAt(0);
			if(char_check==' ')
			{
				btnSave.setEnabled(false);
			}	
//			String result = arg0.toString().replaceAll(" ", "");
//		    if (!arg0.toString().equals(result)) {
//		    	btnSave.setEnabled(false);
//		    }
		
			
			else{
				
				btnSave.setEnabled(true);
				btnSave.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						String subj = editTextSubject.getText().toString();
						dialog.dismiss();
						
						//String subj2=subj.toLowerCase();
						
						if(mDbHelper.findSubjectIfExist(subj)==true)
						{	
							showErrorDialog();
							
						}
						else
							mDbHelper.createSubject(subj);
						fillData();
					
						
					}
					
				});
				
			}
			}			
		}
    });
    

	
   	
	Button btnCancel = (Button)dialog.findViewById(R.id.cancelbtn);
	btnCancel.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			dialog.dismiss();
			
		}
	});
	
	dialog.show();
    }
  
    

 
	
 
 private void fillData() {
     
	 
	 Cursor notesCursor = mDbHelper.fetchAllSubject();
	// startManagingCursor(notesCursor);
 
		 
	    pairs=new ArrayList<Pair>();
	   notesCursor.moveToFirst();
	    while(notesCursor.isAfterLast()!=true)
	    {	String id=notesCursor.getString(0);
	    	String desc=notesCursor.getString(1);
	    	Pair temp=new Pair(id,desc);
	    	pairs.add(temp);
	    	notesCursor.moveToNext();
	    }
	    notesCursor.close();
	   
	   

	   setListAdapter(new SubjectAdapter(pairs, this));
	 }
 }
