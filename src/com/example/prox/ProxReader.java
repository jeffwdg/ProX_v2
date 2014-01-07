package com.example.prox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field; 

import com.sun.pdfview.PDFFile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import net.sf.andpdf.pdfviewer.PdfViewerActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.widget.Toast;


public class ProxReader extends PdfViewerActivity implements OnClickListener{
	
	private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private static final int MEN_PREV_PAGE = 0;
	private static final int MEN_NEXT_PAGE = 0;
	private static final int MEN_GOTO_PAGE = 0;
	private static final int MEN_ZOOM_OUT = 0;
	private static final int MEN_ZOOM_IN = 0;
	private static final int MEN_BACK = 0;
	private static final int MEN_CLEANUP = 0;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    Method m = null;
    PdfViewerActivity  instance;
    
    private PDFFile mPdfFile;
    private int mPage;
	private float mZoom;
    private final static int DIALOG_PAGENUM = 1;
    
    private static final Object[] EMPTY = {};
    
	//PdfViewerActivity pdfActivity =(PdfViewerActivity)getLastNonConfigurationInstance();
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.proxreader_actions, menu);
	   
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	      // Handle item selection
	      switch (item.getItemId()) {
	      case R.id.proxreader_back:  
	      			break;
	      case R.id.proxreader_next:  
			break;
	      default: break;
	      }
	      return false;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.proxreader);
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
 
		
		Button next = (Button) findViewById(R.id.proxreader_next);
		Button back = (Button) findViewById(R.id.proxreader_back);
		Button zoomin = (Button) findViewById(R.id.proxreader_zoomin);
		Button zoomout = (Button) findViewById(R.id.proxreader_zoomout);
		
		 
	   ProxReader pdf = new ProxReader();
	 
		 
		 
	 /*
		
		next.setBackgroundResource(R.drawable.right_arrow);
		back.setBackgroundResource(R.drawable.left_arrow);
		zoomin.setBackgroundResource(R.drawable.zoom_in);
		zoomout.setBackgroundResource(R.drawable.zoom_out);
		

 
		
		
		back.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				//pdfActivity.prevPage();
				//pdfActivity.onMenuItemSelected(3, null);
				String prevPage = "prevPage";
				Method prevPager = null;
				try {
					prevPager = pdfActivity.getClass().getDeclaredMethod(prevPage);
				} catch (NoSuchMethodException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				prevPager.setAccessible(true);
				try {
					Object r =  prevPager.invoke(pdfActivity);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		*/
		
		
		// Gesture detection
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
		
		
	}
	
 
	
 
	
	
    class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(ProxReader.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(ProxReader.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
	 
    }
    
     
	
	public int getPreviousPageImageResource() {
		return R.drawable.left_arrow;
	}

	public int getNextPageImageResource() {
		return R.drawable.right_arrow;
	}

	public int getZoomInImageResource() {
		return R.drawable.zoom_in;
	}

	public int getZoomOutImageResource() {
		return R.drawable.zoom_out;
	}

	public int getPdfPasswordLayoutResource() {
		return R.layout.pdf_file_password;
	}

	public int getPdfPageNumberResource() {
		return R.layout.dialog_pagenumber;
	}

	public int getPdfPasswordEditField() {
		return R.id.etPassword;
	}

	public int getPdfPasswordOkButton() {
		return R.id.btOK;
	}

	public int getPdfPasswordExitButton() {
		return R.id.btExit;
	}

	public int getPdfPageNumberEditField() {
		return R.id.pagenum_edit;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	// @Override
	// public int getNextPageImageResource() {
	// return 0;
	// }
	//
	// @Override
	// public int getPdfPageNumberEditField() {
	// return 0;
	// }
	//
	// @Override
	// public int getPdfPageNumberResource() {
	// return 0;
	// }
	//
	// @Override
	// public int getPdfPasswordEditField() {
	// return 0;
	// }
	//
	// @Override
	// public int getPdfPasswordExitButton() {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	//
	// @Override
	// public int getPdfPasswordLayoutResource() {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	//
	// @Override
	// public int getPdfPasswordOkButton() {
	// // TODO Auto-generated method stub
	// return 0;
	// }

}

 
