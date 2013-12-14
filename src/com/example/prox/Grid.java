package com.example.prox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class Grid extends Activity{

	//---the images to display---
	 Integer[] imageIDs = {
	 R.drawable.userguide,
	 R.drawable.bookcover,
	 R.drawable.bookcover2,
	 R.drawable.nocover,
	 R.drawable.bookcover2,
	 R.drawable.userguide,
	 R.drawable.bookcover,
	 R.drawable.bookcover2,
	 R.drawable.nocover,
	 R.drawable.bookcover2,
	 R.drawable.userguide,
	 R.drawable.bookcover,
	 R.drawable.bookcover2,
	 R.drawable.nocover,
	 R.drawable.bookcover2,
	 R.drawable.userguide,
	 R.drawable.bookcover,
	 R.drawable.userguide,
	 R.drawable.bookcover,
	 R.drawable.bookcover2,
	 R.drawable.nocover,
	 R.drawable.bookcover2,
	 R.drawable.userguide,
	 R.drawable.bookcover,
	 R.drawable.userguide,
	 R.drawable.bookcover,
	 R.drawable.bookcover2,
	 R.drawable.nocover,
	 R.drawable.bookcover2,
	 R.drawable.userguide,
	 R.drawable.bookcover
	 
	 };
	 
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
		    // Inflate the menu items for use in the action bar
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.ereader_actionbar, menu);
		    return super.onCreateOptionsMenu(menu);
		}
		 
		
	 /** Called when the activity is first created. */
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.grid);
	 
		 GridView gridView = (GridView) findViewById(R.id.gridview);
		 gridView.setAdapter(new ImageAdapter(this));
	 
		 gridView.setOnItemClickListener(new OnItemClickListener(){
	 		public void onItemClick(AdapterView parent,View v, int position, long id){	
	 			//openBook(position);
	 			Toast.makeText(getBaseContext(),"pic" + (position + 1) + " selected", Toast.LENGTH_SHORT).show();
		 	}
		 });
	 }
	 
	public void openBook(int position){
			/// Create Intent for StoreActivity  
			Intent gotostoreIntent=new Intent(getApplicationContext(),StoreActivity.class);
			startActivity(gotostoreIntent);
	}
	 
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
