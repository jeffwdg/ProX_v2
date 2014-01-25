package com.example.prox;

import java.util.ArrayList;
import java.util.List;
 
import android.content.Context;
import android.content.SharedPreferences;
import android.content.ClipData.Item;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prox.adapter.EbookDatabaseAdapter;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.radaee.reader.R;

public class EbooksFragment extends Fragment {
	
	GridView gridView;
	ArrayList<Item> gridArray = new ArrayList<Item>();
	CustomGridViewAdapter customGridAdapter;
	
	private EbookDatabaseAdapter mDbHelper;
	
	public EbooksFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
 
        final View rootView = inflater.inflate(R.layout.fragment_ebooks, container, false);
        mDbHelper = new EbookDatabaseAdapter(getActivity());
		mDbHelper.open();
		

		
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",1); // 0 - for private mode  
		String viewPreference = pref.getString("view", getTag());
		
		Log.d("View", ""+viewPreference);
		
		if(TextUtils.equals(viewPreference, "list")){
			Intent intent = new Intent(getActivity(), UserBookListView.class);
			startActivity(intent);
		}else{
			Intent intent = new Intent(getActivity(), UserEbookList.class);
			startActivity(intent);
		}
		
        return rootView;
    }

}
