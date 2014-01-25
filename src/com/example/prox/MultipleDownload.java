package com.example.prox;

import com.example.prox.model.DrawableLoader;
import com.radaee.reader.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MultipleDownload extends Activity {
 
 

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.multidl);

    ImageView imgView = (ImageView) findViewById(R.id.imageView);
    DrawableLoader dl = new DrawableLoader();
    
    dl.fetchDrawableOnThread("http://files.parse.com/afc311a3-01af-4e45-ad6a-4ea2f171e17a/0df90e76-3710-449a-945e-03855c61cce0-2.jpg",imgView);
    
}

@Override
protected void onDestroy() {
    super.onDestroy();
    
}

 
}