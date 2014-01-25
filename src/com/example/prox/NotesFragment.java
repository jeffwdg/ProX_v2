package com.example.prox;

import com.example.prox.note.NoteList;
import com.radaee.reader.R;

import android.app.Fragment;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class NotesFragment extends Fragment {
	
	private TabHost mTabHost;
	
	public NotesFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        
        Intent intent = new Intent(getActivity(), NoteList.class);
		startActivity(intent);

        return rootView;
    }
}
