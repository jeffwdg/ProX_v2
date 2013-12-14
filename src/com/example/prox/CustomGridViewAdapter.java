package com.example.prox;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ClipData.Item;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGridViewAdapter extends ArrayAdapter<Item> {
	 Context context;
	 int layoutResourceId;
	 ArrayList<Item> data = new ArrayList<Item>();

	 public CustomGridViewAdapter(Context context, int layoutResourceId, ArrayList<Item> data) {
		  super(context, layoutResourceId, data);
		  this.layoutResourceId = layoutResourceId;
		  this.context = context;
		  this.data = data;
	}
	 
	 @Override
	    public int getCount() {
	        return data.size();
	    }
	 
	 
	 @Override
	    public Item getItem(int position) {
		 
	        return data.get(position);
	    }
	 @Override
	    public long getItemId(int position) {
	        return position;
	    }

	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
		  View row = convertView;
		  RecordHolder holder = null;
	
		  if (row == null) {
			   LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			   row = inflater.inflate(layoutResourceId, parent, false);
			
			   holder = new RecordHolder();
			   holder.txtTitle = (TextView) row.findViewById(R.id.book_title);
			   holder.imageItem = (ImageView) row.findViewById(R.id.book_image);
			   holder.ebookID = (TextView) row.findViewById(R.id.book_id);
			   holder.ebookLocation = (TextView) row.findViewById(R.id.book_filelocation);
			   row.setTag(holder);
		  } else {
			  	holder = (RecordHolder) row.getTag();
		  }
		  
		  int bookcover = 0;
		  Object curObj;
		  
		  Item item = data.get(position);
		  holder.txtTitle.setText(item.getText());
		  holder.imageItem.setBackgroundResource(bookcover);
		  holder.ebookID.setText(item.getText());
		  holder.ebookLocation.setText(item.getHtmlText());
		  return row;
	
	 };

	 static class RecordHolder {
		  TextView txtTitle;
		  TextView ebookID;
		  TextView ebookLocation;
		  ImageView imageItem;
	 }
}
