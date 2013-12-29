package com.example.prox.note;

import java.util.ArrayList;

import com.example.prox.R;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

public class SubjectList extends ListActivity{
	
	private int mNoteNumber = 1;
    EditText editTextSubject;
	public SubjectDbAdapter mDbHelper;
	private String delete_id;
	ArrayList<Pair> pairs;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subjectslist);
		
		ActionBar ab = getActionBar();
	    ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("My Subjects");
        ab.setIcon(R.drawable.notes);

        
		mDbHelper = new SubjectDbAdapter (this);
		mDbHelper.open();
		fillData();
		//count();
	Button deletesubject = (Button)findViewById(R.id.deletebtn);
	deletesubject.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			
		}
	});
	
	ListView list = getListView();
    list.setOnItemLongClickListener(new OnItemLongClickListener() {

    @Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
    	delete_id=arg1.getTag().toString();
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
public void showDeleteDialog()
{	Builder builder = new AlertDialog.Builder(this);
	builder.setMessage("Confirm Delete?");
	builder.setCancelable(true);
	builder.setPositiveButton("Yes", new OkOnClickListener());
	builder.setNegativeButton("No", new CancelOnClickListener());
	AlertDialog dialog = builder.create();
	dialog.show();
	
}

private final class CancelOnClickListener implements DialogInterface.OnClickListener {
	public void onClick(DialogInterface dialog, int which) {
	
	}
}

private final class OkOnClickListener implements DialogInterface.OnClickListener 
{	public void onClick(DialogInterface dialog, int which) 
	{	mDbHelper.deleteSubject(delete_id);
		
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

//    // get the References of views
    editTextSubject=(EditText)dialog.findViewById(R.id.editTextSubject);
    Button btnSave=(Button)dialog.findViewById(R.id.savebtn);
		
	// Set On ClickListener
	btnSave.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			
		String subj = editTextSubject.getText().toString();
		dialog.dismiss();
			if(mDbHelper.findSubjectIfExist(subj)==true)
			{	
				showErrorDialog();
				
			}
			else
				mDbHelper.createSubject(subj);
			fillData();
		
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
     // Get all of the notes from the database and create the item list 
	 
	 Cursor notesCursor = mDbHelper.fetchAllSubject();
	 startManagingCursor(notesCursor);
	 
	    pairs=new ArrayList<Pair>();
	    notesCursor.moveToFirst();
	    while(notesCursor.moveToNext())
	    {	String id=notesCursor.getString(0);
	    	String desc=notesCursor.getString(1);
	    	Pair temp=new Pair(id,desc);
	    	pairs.add(temp);
	    }
	    notesCursor.close();
	   
	   

	   setListAdapter(new SubjectAdapter(pairs, this));
	 }
 
 }





    

