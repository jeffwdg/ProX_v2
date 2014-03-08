package com.example.prox.note;



import com.radaee.reader.R;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class NoteList extends ListActivity {
	
	private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
	
    private static final int DELETE_ID = Menu.FIRST;
	private int mNoteNumber = 1;
	
	private NotesDbAdapter mDbHelper;
	private SubjectDbAdapter dbhelper;
	private Bundle extras;
	private String delete_id;
	private long rowId;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notelist);
		
		
		mDbHelper = new NotesDbAdapter (this);
		mDbHelper.open();
		
		if(mDbHelper.countNotes()<1)
		{
			Toast.makeText(getApplicationContext(), "No note(s)", Toast.LENGTH_SHORT).show();
		}
		
		ListView list = getListView();
		
		
	    list.setOnItemLongClickListener(new OnItemLongClickListener() {

	    @Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
	    	rowId = arg3;
	    	showDeleteDialog();
			// TODO Auto-generated method stub
			return false;
		}
	    });
		
		Button addnote = (Button)findViewById(R.id.addnotebutton);
		addnote.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				createNote();
				}
		});
        
		mDbHelper = new NotesDbAdapter (this);
		mDbHelper.open();
	    extras=getIntent().getExtras();
		if(extras!=null)
		{	displayListOfNotes();
		}
		else
			displayAllNotes();
		
		
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
		{	
		     mDbHelper.deleteNote(rowId);
		     fillData(null);
		
		}

		
	}
	public void displayListOfNotes()
	{	String subject = extras.getString("subject");
		ActionBar ab = getActionBar();
	    ab.setDisplayHomeAsUpEnabled(true);
	    ab.setTitle(subject+" Notes");
	    ab.setIcon(R.drawable.notes);
	    fillData(subject);
		
	}
	public void displayAllNotes()
	{	ActionBar ab = getActionBar();
	    ab.setDisplayHomeAsUpEnabled(true);
	    ab.setTitle("My Notes");
	    ab.setIcon(R.drawable.notes);
		fillData(null);				
		registerForContextMenu(getListView());
		
		Button btnsubjects = (Button)findViewById(R.id.button_subjects);
		btnsubjects.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				subjectlist();
				
			}
		});
		
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notelist_menu, menu);
		return true;		
	}
	
	
	private void subjectlist(){
		Intent n = new Intent(getApplicationContext(),SubjectList.class);
		startActivity(n);
	}
	
	private void createNote() {
		SubjectDbAdapter subDb=new SubjectDbAdapter(this);
		subDb.open();
		Intent i;
		int subject_rows=subDb.fetchAllSubject().getCount();
		subDb.close();
		if(subject_rows<1)
		{	Toast.makeText(getApplicationContext(), "ERROR! No subjects exist. Add subject first", Toast.LENGTH_LONG).show();
			i=new Intent(this,SubjectList.class);
			startActivityForResult(i, ACTIVITY_CREATE); 
		}
		else
		{	i = new Intent(this, NoteEdit.class);
		startActivityForResult(i, ACTIVITY_EDIT); 
		
		} 	
    }
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, NoteEdit.class);
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        //i.putExtra("subject", )
        startActivityForResult(i, ACTIVITY_EDIT);
    }

	private void fillData(String subject) {
        // Get all of the notes from the database and create the item list
		Cursor notesCursor;
		if(subject!=null)
			notesCursor = mDbHelper.fetchAllNotesUnderSubject(subject);
		else
			notesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(notesCursor);
        

        String[] from = new String[] { NotesDbAdapter.KEY_TITLE ,  NotesDbAdapter.KEY_SUBJECT, NotesDbAdapter.KEY_DATE};
        int[] to = new int[] { R.id.text1 ,R.id.text2, R.id.date_row};
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
            new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor, from, to);
        setListAdapter(notes);
    }
	

    
}