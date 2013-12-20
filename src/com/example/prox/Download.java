package com.example.prox;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.example.prox.Grid.ImageAdapter;
 
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
 
public class Download extends Activity {
 
    Button btnShowProgress;
    TextView booktitle, bookfilename;
 
    private ProgressDialog pDialog;
    ImageView my_image;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0; 
 
    //file url to download
    //private static String file_url = "http://api.androidhive.info/progressdialog/hive.jpg";
    String file_url="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        
        String title = "";
        String author = "";
        String filename = "";
 
        Intent i = getIntent();
        
        // Selected image id
        int position = i.getExtras().getInt("id");
        title = i.getExtras().getString("title");
        //filename = i.getExtras().getString("filename");
        
        /*
        String[] file1 = filename.split("/");
        String[] file2 = file1[4].split("-");
        String myfilename = file2[5];
       */
        //CustomGridViewAdapter adapter = new CustomGridViewAdapter(this, position, null);

        booktitle = (TextView) findViewById(R.id.bookTitle);
        booktitle.setText(title);
        
       // bookfilename = (TextView) findViewById(R.id.book_filelocation);
        //bookfilename.setText(myfilename);
        
        file_url = i.getExtras().getString("filename");
        Toast.makeText(this,"The ebook " +  title + " selected", Toast.LENGTH_SHORT).show();
 
        btnShowProgress = (Button) findViewById(R.id.btnProgressBar);
        
        //Image to show after downloading
        my_image = (ImageView) findViewById(R.id.my_image);
        /**
         * Show Progress bar click event
         * */
        btnShowProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // starting new Async Task
                new DownloadFileFromURL().execute(file_url);
                Log.d("Ebook Download", "Downloading..");
            }
        });
    }
 
    
    /**
     * Showing Dialog
     * */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case progress_bar_type: // we set this to 0
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();
            return pDialog;
        default:
            return null;
        }
    }
 
    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }
 
        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            
            File folder = new File("data/data/com.example.prox/proxbooks");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            
            
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();
 
                
                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                String root = "data/data/com.example.prox/proxbooks";
                
 
                String[] file1 = file_url.split("/");
                String[] file2 = file1[4].split("-");
                String filename = file2[5];
                Log.d("Filename", filename);
                
                String bookfilename = file2[5]; //"ebook.pdf";
                // Output stream
                OutputStream output = new FileOutputStream(root + "/" + bookfilename);
 
                byte data[] = new byte[1024];
 
                long total = 0;
 
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
 
                    // writing data to file
                    output.write(data, 0, count);
                }
 
                // flushing output
                output.flush();
 
                // closing streams
                output.close();
                input.close();
 
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
 
            return null;
        }
 
        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
       }
 
        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            
            
            File file = new File("data/data/com.example.prox/proxbooks/book.pdf"); 

            if (file.exists()) { 
                Uri path = Uri.fromFile(file); 
                Intent intent = new Intent(Intent.ACTION_VIEW); 
                intent.setDataAndType(path, "application/pdf"); 
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 

                try { 
                    startActivity(intent); 
                }  
                catch (ActivityNotFoundException e) { 
                   // Toast.makeText(this,  "No Application Available to View PDF",  Toast.LENGTH_SHORT).show(); 
                }  
            }
            // Displaying downloaded image into image view
            // Reading image path from sdcard
            //String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.pdf";
            // setting downloaded into image view
            //my_image.setImageDrawable(Drawable.createFromPath(imagePath));
        }
 
    }
}