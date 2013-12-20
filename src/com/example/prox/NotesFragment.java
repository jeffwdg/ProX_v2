package com.example.prox;

 

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        
        mTabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(getActivity(), false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        mTabHost.setup(mLocalActivityManager);//very important to call this
        
 
        
        TabHost.TabSpec free = mTabHost.newTabSpec("Free");
        free.setIndicator("Free", getResources().getDrawable(R.drawable.free_tab));
        Intent freeIntent = new Intent(getActivity(), FreeBookView.class);
        free.setContent(freeIntent);
        
        TabHost.TabSpec latest = mTabHost.newTabSpec("Latest");
        latest.setIndicator("Latest", getResources().getDrawable(R.drawable.new_tab));
        Intent latestIntent = new Intent(getActivity(), BookView.class);
        latest.setContent(latestIntent);
        
        TabHost.TabSpec best = mTabHost.newTabSpec("Best Seller");
        best.setIndicator("Best Seller", getResources().getDrawable(R.drawable.best_tab));
        Intent bestIntent = new Intent(getActivity(), BookView.class);
        best.setContent(bestIntent);
        
        mTabHost.addTab(free);
        mTabHost.addTab(latest);
        mTabHost.addTab(best);
		 
        
 
		
        return rootView;
    }
}
