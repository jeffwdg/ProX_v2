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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;

public class CategorizeNotes extends ListActivity {
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
		setContentView(R.layout.notelist_bysubject);
		
		mDbHelper = new NotesDbAdapter (this);
		mDbHelper.open();
		
		

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
	    
	    extras=getIntent().getExtras();
		if(extras!=null)
		{	displayListOfNotes();
		}
		
		
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
//		Cursor subjcursor;
//		
//		     subjcursor=mDbHelper.fetchNote(rowId);
		     mDbHelper.deleteNote(rowId);
		     displayListOfNotes();
		
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

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notelist_menu, menu);
		return true;		
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
	
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//            ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
//    }
//    
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case DELETE_ID:
//                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//                mDbHelper.deleteNote(info.id);
//                fillData(null);
//                return true;
//        }
//        return super.onContextItemSelected(item);
//    }
//    
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if(resultCode==RESULT_OK&&requestCode==ACTIVITY_CREATE)
//        	fillData(null);
//        else if(resultCode==RESULT_OK && requestCode==ACTIVITY_EDIT)
//        {	//get subject value from the previous activity
//        	String subj=intent.getExtras().getString("subject");
//        	fillData(subj);
//        	
//        }
//        	
//    }   
    
}
