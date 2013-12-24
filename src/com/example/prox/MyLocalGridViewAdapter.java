package com.example.prox;

 
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData.Item;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MyLocalGridViewAdapter extends ArrayAdapter<Item> {
	 Context context;
	 int layoutResourceId;
	 ArrayList<Ebook> data = new ArrayList<Ebook>();

	 public MyLocalGridViewAdapter(Context context, int layoutResourceId, ArrayList<Ebook> data) {
		  super(context, layoutResourceId);
		  this.layoutResourceId = layoutResourceId;
		  this.context = context;
		  this.data = data;
	}
	 
	 @Override
	    public int getCount() {
	        return data.size();
	    }
	 
	 
	 public Ebook get(int position) {
		 
	        return data.get(position);
	    }
	 @Override
	    public long getItemId(int position) {
	        return position;
	    }

	 @SuppressLint({ "NewApi"})
	@Override
	 public View getView(int position, View convertView, ViewGroup parent) {
		  View row = convertView;
		  RecordHolder holder = null;
		  ImageView imageView;

		  if (row == null) {
			   LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			   row = inflater.inflate(layoutResourceId, parent, false);
			   
			   holder = new RecordHolder();
			   holder.txtTitle = (TextView) row.findViewById(R.id.book_title);
			   holder.txtAuthor= (TextView) row.findViewById(R.id.book_author);
			   holder.imageItem = (ImageView) row.findViewById(R.id.book_image);
			   holder.ebookID = (TextView) row.findViewById(R.id.book_id);
			   holder.ebookLocation = (TextView) row.findViewById(R.id.book_filelocation);
			   
			   row.setTag(holder);
		  } else {
			  	holder = (RecordHolder) row.getTag();
		  }

		  int bookcover = 0;
 
		  Ebook item = data.get(position);
		  
		  holder.txtTitle.setText(item.getTitle());
		  holder.txtAuthor.setText(item.getAuthor());
		  String thisurl = item.getCover();
 
		  Drawable bookcover1;
		  SharedPreferences pref = context.getSharedPreferences("MyPref", 1); // 0 - for private mode
		  Editor editor = pref.edit();

          String userFolderName = pref.getString("email", null);
          
		   
		  String bitmapPath = "data/data/com.example.prox/proxbooks/" + userFolderName +"/" + item.getID() + ".jpg";
          Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath);
          bookcover1 = new BitmapDrawable(bitmap);
          Log.d("Ebook", "Cover " + bitmapPath );
          
          if(bitmapPath.equals("")){
        	  holder.imageItem.setBackgroundResource(R.drawable.bookcover2);
          } 
          else{
        	  holder.imageItem.setImageDrawable(bookcover1);
          }
		  holder.ebookID.setText(item.getID());
		  holder.ebookLocation.setText(item.getFilename());
		  return row;
	
	 };
	 
	 public Drawable LoadImageFromURL(String url){
		 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
	    try
	    {
		    InputStream is = (InputStream) new URL(url).getContent();
		    Drawable d = Drawable.createFromStream(is, "src");
		    return d;	
	    }catch (Exception e) {
		    System.out.println(e);
		    return null;
	    }
	}
 
	 static class RecordHolder {
		  TextView txtTitle;
		  TextView txtAuthor;
		  TextView ebookID;
		  TextView ebookLocation;
		  ImageView imageItem;
	 }
}


