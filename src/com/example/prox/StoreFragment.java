package com.example.prox;

import java.util.ArrayList;
import java.util.List;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class StoreFragment extends Fragment {
	private WebView webView;
 
	 GridView gridView;
	 ArrayList<Item> gridArray = new ArrayList<Item>();
	 CustomGridViewAdapter customGridAdapter;

	Integer[] imageIDs = {
			 R.drawable.userguide,
			 R.drawable.bookcover, 
			 R.drawable.userguide,
			 R.drawable.bookcover
			 };
	
	public StoreFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        final View rootView = inflater.inflate(R.layout.fragment_pages, container, false);
       /*
        webView = (WebView) rootView.findViewById(R.id.storeview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://prox.parseapp.com/Prox/ebooks.html");
        */
        
      //set grid view item
        Bitmap homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.userguide);
        //Bitmap userIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.bookcover);

 
        
        /**  Get data from parse.com **/

    	ParseQuery<ParseObject> query = ParseQuery.getQuery("ebook");
    	query.findInBackground(new FindCallback<ParseObject>() {
			@SuppressLint("NewApi")
			@Override
			public void done(List<ParseObject> ebookslist, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
	    	    	Log.d("ebooks", "Found " + ebookslist.size() + " ebooks");
	    	    	//Toast.makeText(getActivity(), "Ret", Toast.LENGTH_LONG).show(); 

		    	      String mybooktitle, intent, uri;
		    	      for(ParseObject userbooks : ebookslist) {
		    	    	  mybooktitle=(String) userbooks.get("title");
		    	    	  intent = (String) userbooks.get("filename");
		    	    	  uri =  (String) userbooks.get("filename");
		    	    	  Log.d("ebooks", "Retrieved " + mybooktitle);
		    	    	  gridArray.add(new Item(mybooktitle,uri));
		              }
		    	      
		    	      gridView = (GridView) rootView.findViewById(R.id.gridview);
		    	      customGridAdapter = new CustomGridViewAdapter(getActivity(), R.layout.row_grid, gridArray);
		    	      gridView.setAdapter(customGridAdapter);
	    	      
	    	    } else {
	    	      // something went wrong
	    	    	Toast.makeText(getActivity(),"Error", Toast.LENGTH_LONG).show(); 
	    	    	Log.d("ebooks error", "Error");
	    	    }
			}
    	});
    	
    	/**END get data**/
    	
         GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        customGridAdapter = new CustomGridViewAdapter(getActivity(), R.layout.row_grid, gridArray);
        gridView.setAdapter(customGridAdapter);


        gridView.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick(AdapterView parent,View v, int position, long id){	
        		Ebook item = (Ebook) v.getTag();
 
	 			//openBook(position);
        		String filename = item.getFilename();
        		Intent bookdetails = new Intent(v.getContext(), Download.class);
        		bookdetails.putExtra("filename", item.getFilename());
        		bookdetails.putExtra("title", item.getTitle());
        		bookdetails.putExtra("author", item.getAuthor());
        		Toast.makeText(getActivity(),"Ebook " +  filename + " selected", Toast.LENGTH_SHORT).show();
                startActivity(bookdetails);
                
	 			
		 	}
		 });
        
        return rootView;
    }
 
}
