package com.example.prox;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class EbooksFragment extends Fragment {
	
	 Integer[] imageIDs = {
			 R.drawable.userguide,
			 R.drawable.bookcover, 
			 R.drawable.userguide,
			 R.drawable.bookcover
			 };
 
	 
	 ArrayList<Ebook> myEbook;
	 
	 
	public EbooksFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        
        Parse.initialize(getActivity(), "x9n6KdzqtROdKDXDYF1n5AEoZLZKOih8rIzcbPVP", "JkqOqaHmRCA35t9xTtyoiofgG3IO7E6b82QIIHbF");
    	
        //getActivity().setContentView(R.layout.grid);
       /* 
        Ebook ebook = new Ebook();
        Ebook ebook1 = new Ebook();
        
        myEbook  = new ArrayList<Ebook>();
        
        ebook.filename = "Android 101";
        ebook.author = "ASam";
        
        ebook1.filename = "Android 102";
        ebook1.author = "ASam2";
        
        myEbook.add(0,ebook);
        myEbook.add(1,ebook1);
        
        Log.d("Author 1",ebook1.author);
        Log.d("Author 2",ebook.author);
        
        */
        
   	 
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        //gridView.setAdapter(new ImageAdapter(getActivity()));
	 
        gridView.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick(AdapterView parent,View v, int position, long id){	
	 			//openBook(position);
	 			Toast.makeText(getActivity(),"Ebook" + (position + 1) + " selected", Toast.LENGTH_SHORT).show();
		 	}
		 });
		 
		 
        /**  Get data from parse.com **/
    	//ParseQuery<ParseObject> query = new ParseQuery("userEbooks");  
    	
    	//query.whereEqualTo("userID", "se00mwg0q2");
    	
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("userEbooks");
    	query.whereEqualTo("userID", "se00mwg0q2");
    	//ParseQuery<ParseObject> myquery = ParseQuery.getQuery("ebook");
    	//myquery.whereMatchesQuery("ebook", query);
   
    	
    	query.include("e");
    	
    	query.findInBackground(new FindCallback<ParseObject>() {
    		
    	  public void done(List<ParseObject>  userebookslist, ParseException e) {
    	    if (e == null) {

    	    	Log.d("userEbooks", "Retrieved " + userebookslist.size() + " ebooks");
    	    	Toast.makeText(getActivity(), "Ret", Toast.LENGTH_LONG).show(); 
    	      
    	      String mybooktitle;
    	      
    	      for (ParseObject userbooks : userebookslist) {
                  
				//list1Strings22[iii]=(String) country.get("name");
    	    	  mybooktitle=(String) userbooks.get("title");
    	    	  
    	    	  Log.d("User", "Book  " + mybooktitle );
    	    	  
    	    	  
                  //adapter.add((String) userbooks.get("title"));
              }
    	      


    	    } else {
    	      // something went wrong
    	    	Toast.makeText(getActivity(),"Error", Toast.LENGTH_LONG).show(); 
    	    }
    	  }
    	});
    	

    	
    	/**END get data**/
    	
    	
    	
    	
        return rootView;
    }
	
	public class Ebook{
		
		String id;
		String filename;
		String author;
		String title;
		
	}
	
	/** Image adapter for grid View**/
	
	public class ImageAdapter extends BaseAdapter 
	 {
		 private Context context;
		 public ImageAdapter(Context c){
			 context = c;
		 }
		 
		 //---returns the number of images---
		 public int getCount() {
			 return imageIDs.length;
		 }
		 
		 //---returns the item---
		 public Object getItem(int position) {
			 return position;
		 }
		 
		 //---returns the ID of an item---
		 public long getItemId(int position) {
			 return position;
		 }
		 
		 //---returns an ImageView view---
		 public View getView(int position, View convertView,ViewGroup parent){
			 ImageView imageView;
			 
			 if (convertView == null){
				 imageView = new ImageView(context);
				 imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				 imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				 imageView.setPadding(5, 5, 5, 5);
			 }
			 else{
				 imageView = (ImageView) convertView;
			 }
			 
			 imageView.setImageResource(imageIDs[position]);
			 return imageView;
		 }
	 }
	
}
