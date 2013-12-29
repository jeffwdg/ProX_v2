package com.example.prox.note;

import java.util.ArrayList;

import com.example.prox.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SubjectAdapter extends BaseAdapter{
	  private final Context context;
	  private ArrayList<Pair> pairs;
	  public SubjectAdapter(ArrayList<Pair> pairs,Context context) {
	    this.context = context;
	    this.pairs = pairs;
	  }
	  
	  public View getView(int position, View convertView, ViewGroup parent) {
	        // TODO Auto-generated method stub
	         View v = convertView;
	         if (v == null)
	         {
	            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.subject_row, null);
	         }
	 
	           Pair p = pairs.get(position);
	           TextView name = (TextView)v.findViewById(R.id.subjtxt);
	           v.setTag(p.getId());
	           
	           name.setText(p.getDesc());                             
	                        
	        return v;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pairs.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return pairs.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	  
	  
	} 