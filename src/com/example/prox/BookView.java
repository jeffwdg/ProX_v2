package com.example.prox;

import com.example.prox.Grid.ImageAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
 
public class BookView extends Activity {
    int bookcounter = 0;

    int mybooks = 45;
    int numCol = 3;
    int numRow = mybooks/numCol;
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.ereader_actionbar, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelfrow);
 

 
        TableLayout tblLayout = (TableLayout) findViewById(R.id.tblLayout);
 
        
 
        for(int i = 0; i < numRow; i++) {
            HorizontalScrollView HSV = new HorizontalScrollView(this);
            HSV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
 
            TableRow tblRow = new TableRow(this);
            tblRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            tblRow.setBackgroundResource(R.drawable.row);
           
            // add exact number of books only	
            
            for(int j = 0; j < numCol; j++) {
            	
            	 if(bookcounter < mybooks){
	            	ImageView imageView = new ImageView(this);
	                imageView.setImageResource(R.drawable.bookcover);
	 
	                TextView textView = new TextView(this);
	                textView.setText("Java Tester");
	                textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 
	                        LayoutParams.WRAP_CONTENT));
	 
	                tblRow.addView(imageView,j);
	                
            	 }
            	 bookcounter++;
            }
 
            HSV.addView(tblRow);
            tblLayout.addView(HSV, i);
            
                  

            
        }
    }
 
    
    
    
    
}