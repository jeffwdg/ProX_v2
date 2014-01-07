package com.example.prox.note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.prox.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class NoteEdit extends Activity{
	
	public static int numTitle = 1;	
	public static String curDate = "";
	public static String curText = "";	
    private EditText mTitleText;
    private EditText mSubjectText;
    private EditText mBodyText;
    private TextView mDateText;
    private Long mRowId;
    private Spinner mSpinner;

    private Cursor note;

    private NotesDbAdapter mDbHelper;
      
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();        
        
        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Note");
        ab.setIcon(R.drawable.notes);
        
        setContentView(R.layout.note_edit);
        //setTitle(R.string.app_name);

        mTitleText = (EditText) findViewById(R.id.title);
       // mSubjectText = (EditText) findViewById(R.id.subject);
        mSpinner = (Spinner)findViewById(R.id.subject_spinner);
        mBodyText = (EditText) findViewById(R.id.body);
        mDateText = (TextView) findViewById(R.id.notelist_date);
        
        
         
	    
	    
        long msTime = System.currentTimeMillis();  
        Date curDateTime = new Date(msTime);
 	
        SimpleDateFormat formatter = new SimpleDateFormat("d'/'M'/'y");  
        curDate = formatter.format(curDateTime);        
        
        mDateText.setText(""+curDate);
        

        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
                                    : null;
        }

        populateFields();
    
    
	

	loadspinner();
	
	}	
	
	
	private void loadspinner(){
		
		List<String> list;
		
		Spinner spin =(Spinner)findViewById(R.id.subject_spinner);
		
		list = mDbHelper.getAllLabels();
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(dataAdapter);
	}

	

	  public static class LineEditText extends EditText{
			// we need this constructor for LayoutInflater
			public LineEditText(Context context, AttributeSet attrs) {
				super(context, attrs);
					mRect = new Rect();
			        mPaint = new Paint();
			        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			        mPaint.setColor(Color.BLUE);
			}

			private Rect mRect;
		    private Paint mPaint;	    
		    
		    @Override
		    protected void onDraw(Canvas canvas) {
		  
		        int height = getHeight();
		        int line_height = getLineHeight();

		        int count = height / line_height;

		        if (getLineCount() > count)
		            count = getLineCount();

		        Rect r = mRect;
		        Paint paint = mPaint;
		        int baseline = getLineBounds(0, r);

		        for (int i = 0; i < count; i++) {

		            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
		            baseline += getLineHeight();

		        super.onDraw(canvas);
		    }

		}
	  }
	  
	  @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        saveState();
	        outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
	    }
	    
	    @Override
	    protected void onPause() {
	        super.onPause();
	        saveState();
	    }
	    
	    @Override
	    protected void onResume() {
	        super.onResume();
	        populateFields();
	    }
	    
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.noteedit_menu, menu);
			return true;		
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		    switch (item.getItemId()) {
		
		    case R.id.menu_delete:
		    	
		    	mTitleText = (EditText) findViewById(R.id.title);
				  
		        mBodyText = (EditText) findViewById(R.id.body);
		        mDateText = (TextView) findViewById(R.id.notelist_date);
		    	// check if any of the fields are vacant
				if(mTitleText.equals("") )
				{
						Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_LONG).show();
						
				}
				
				else{
		    	
		    	AlertDialog.Builder dialog1 = new AlertDialog.Builder(NoteEdit.this);
		    	dialog1.setTitle("Delete Confirmation");
		    	dialog1.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						deletenote();
						finish();

					}
		    		
		    	});
		    	
		    	dialog1.setNegativeButton("No", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
						
					}
		    		
		    	});
		    	
		    	dialog1.show();
		    	
		    	return true;
		    	
				}
		    	
		    case R.id.menu_save:
	    		saveState();
	    		//finish();	    	
		    default:
		    	return super.onOptionsItemSelected(item);
		    }
		}
	    
		private void deletenote(){
			  
		        
		        
			
			
			//else{
			
		    if(note != null){
			note.close();
			note = null;
			}
			
			if(mRowId != null){
			mDbHelper.deleteNote(mRowId);
			}
			
			finish();
			}	
			
			//finish();
		//}
	    private void saveState() {
	        String title = mTitleText.getText().toString();
	        //String subject = mSubjectText.getText().toString();
	        String body = mBodyText.getText().toString();
	        
	        if(title.equals("") /*|| subject.equals("") || body.equals("")*/ )
			{
					Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_LONG).show();
					return;
			}
			
	       
	        else{
			String subject=mSpinner.getSelectedItem().toString();
	        if(mRowId == null){
	        	
	        	long id = mDbHelper.createNote(title, subject ,body, curDate);
	        	if(id > 0){
	        		mRowId = id;
	        	}else{
	        		Log.e("saveState","failed to create note");
	        	}
	        }else{
	        	if(!mDbHelper.updateNote(mRowId, title, subject, body, curDate)){
	        		Log.e("saveState","failed to update note");
	        	}
	        }
	        finish();
	        }
	    }
	    
	  
	    private void populateFields() {
	        if (mRowId != null) {
	            note = mDbHelper.fetchNote(mRowId);
	            startManagingCursor(note);
	            mTitleText.setText(note.getString(
	    	            note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
//	            mSubjectText.setText(note.getString(
//	            		note.getColumnIndexOrThrow(NotesDbAdapter.KEY_SUBJECT)));
	            mBodyText.setText(note.getString(
	                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
	            curText = note.getString(
	                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY));
	        }
	    }



}
