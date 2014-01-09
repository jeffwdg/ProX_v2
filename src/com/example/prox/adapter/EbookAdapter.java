package com.example.prox.adapter;

import java.util.ArrayList;

import com.example.prox.Ebook;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EbookAdapter extends ArrayAdapter{
	public ArrayList<Ebook> objects;
	
	public EbookAdapter(Context context, int resource, int textViewResourceId,ArrayList<Ebook> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.objects = objects;
	}




	

	 


	public View getView(int position, View convertView, ViewGroup parent){

		// assign the view we are converting to a local variable
		View v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(com.radaee.reader.R.layout.ebooks_row, null);
		}

		Ebook i = objects.get(position);

		if (i != null) {

			TextView title = (TextView) v.findViewById(com.radaee.reader.R.id.mTitle);
			TextView author = (TextView) v.findViewById(com.radaee.reader.R.id.mAuthor);
			TextView objectId = (TextView) v.findViewById(com.radaee.reader.R.id.mObjectId);
			ImageView cover = (ImageView) v.findViewById(com.radaee.reader.R.id.mCover);
			TextView filename = (TextView) v.findViewById(com.radaee.reader.R.id.mFilename);
		 
			if (title != null){
				title.setText(i.getTitle());
			}
			if (author != null){
				author.setText(i.getAuthor());
			}
			if (objectId != null){
				objectId.setText(i.getID());
			}
			
			Drawable bookcover1 = LoadImageFromURL(i.getCover());
			cover.setImageDrawable(bookcover1);
			 
		}

		return v;

	}


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
}
