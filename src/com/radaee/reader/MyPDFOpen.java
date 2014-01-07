package com.radaee.reader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.prox.R;
import com.radaee.pdf.Document;
import com.radaee.pdf.Global;
import com.radaee.pdf.Page;
import com.radaee.pdf.Page.Annotation;
import com.radaee.reader.PDFReader.PDFReaderListener;
import com.radaee.util.PDFGridItem;
import com.radaee.util.PDFGridView;
import com.radaee.util.PDFThumbView;
import com.radaee.view.PDFVPage;
import com.radaee.view.PDFView.PDFViewListener;
import com.radaee.view.PDFViewThumb.PDFThumbListener;

public class MyPDFOpen extends Activity implements OnItemClickListener, OnClickListener, PDFReaderListener, PDFThumbListener{
	
	private PDFGridView m_vFiles = null;
	private PDFReader m_reader = null;
	private PDFThumbView m_thumb = null;
	private RelativeLayout m_layout;
	private Document m_doc = new Document();
	
    private Button btn_prev;
    private Button btn_next;
    private Button btn_gotoPage;
    private EditText txt_find;
    
    private String str_find;
    private boolean m_set = false;
    private PDFVPage m_annot_vpage;
    private Annotation m_annot;
    
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Global.Init( this );
		m_layout = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.reader, null);
		m_reader = (PDFReader)m_layout.findViewById(R.id.view);
		m_thumb = (PDFThumbView)m_layout.findViewById(R.id.thumbs);
		
		Intent i = getIntent();
        
        // Get ebook details
        //int position = i.getExtras().getChar("ebookFile");
        String pdfpath = i.getExtras().getString("ebookFile");
        Log.d("File", "Loc"+pdfpath);
        
		m_doc = new Document();
		String pdf_path = pdfpath; //"/mnt/asec/RobotoSpecimenBook.pdf";
		String password = "";
		m_doc.Open( pdf_path, password );
		m_reader.PDFOpen(m_doc, false, this);
		m_thumb.thumbOpen(m_doc, this);
		
		//m_reader.PDFOpen(m_doc, false, this);
		/*
        m_vFiles = new PDFGridView(this, null);
		m_vFiles.PDFSetRootPath("/mnt");
		m_vFiles.setOnItemClickListener(this);
		*/
		//((ViewGroup) m_thumb.getParent()).removeView(m_thumb);
		//m_thumb.thumbOpen(m_reader.PDFGetDoc(), this);
		setContentView(m_layout);

        LinearLayout bar_cmd = (LinearLayout)m_layout.findViewById(R.id.bar_cmd);
        LinearLayout bar_act = (LinearLayout)m_layout.findViewById(R.id.bar_act);
        LinearLayout bar_find = (LinearLayout)m_layout.findViewById(R.id.bar_find);
    
       
        txt_find = (EditText)bar_find.findViewById(R.id.txt_find);
        btn_prev = (Button)bar_find.findViewById(R.id.btn_prev);
        btn_next = (Button)bar_find.findViewById(R.id.btn_next);
        btn_gotoPage = (Button)bar_find.findViewById(R.id.btn_goToPage);

        
        btn_prev.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_gotoPage.setOnClickListener(this);

  
    }
	
	protected void onDestroy()
    {
    	//m_vFiles.close();
    	if( m_vFiles != null )
    	{
    		m_vFiles.close();
    		m_vFiles = null;
    	}
    	if( m_thumb != null )
    	{
    		m_thumb.thumbClose();
    		m_thumb = null;
    	}
    	if( m_reader != null )
    		m_reader.PDFClose();
    	if( m_doc != null )
    		m_doc.Close();
    	Global.RemoveTmp();
    	super.onDestroy();
    }
    
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		if( arg0 == m_vFiles )
		{
			PDFGridItem item = (PDFGridItem)arg1;
			if( item.is_dir() )
			{
				m_vFiles.PDFGotoSubdir(item.get_name());
			}
			else
			{
				m_doc.Close();
				int ret = item.open_doc(m_doc, null);
				switch( ret )
				{
				case -1://need input password
					finish();
					break;
				case -2://unknown encryption
					finish();
					break;
				case -3://damaged or invalid format
					finish();
					break;
				case -10://access denied or invalid file path
					finish();
					break;
				case 0://succeeded, and continue
					m_doc.SetCache( Global.tmp_path + "/temp.dat" );//set temporary cache for editing.
					/*
					{
						int pageno = 0;
						int pagecnt = m_doc.GetPageCount();
						for( pageno = 0; pageno < pagecnt; pageno++ )
						{
							Page page = m_doc.GetPage(pageno);
							page.ObjsStart();
							int cnt = page.ObjsGetCharCount();
							String txt = page.ObjsGetString(0, cnt);
							page.Close();
						}
					}
					*/
					m_reader.PDFOpen(m_doc, false, this);
					//m_reader.PDFGotoPage(10);
					break;
				default://unknown error
					finish();
					break;
				}
				m_thumb.thumbOpen(m_reader.PDFGetDoc(), this);
	            setContentView(m_layout);
 
			}
		}
		else
		{
		}
	}
	 
	 
	private void onFindPrev()
	{
		String str = txt_find.getText().toString();
		if( str_find != null )
		{
			if( str != null && str.compareTo(str_find) == 0 )
			{
				m_reader.PDFFind(-1);
				return;
			}
		}
		if( str != null && str.length() > 0 )
		{
			m_reader.PDFFindStart(str, false, false);
			m_reader.PDFFind(1);
			str_find = str;
		}
	}
	private void onFindNext()
	{
		String str = txt_find.getText().toString();
		if( str_find != null )
		{
			if( str != null && str.compareTo(str_find) == 0 )
			{
				m_reader.PDFFind(1);
				return;
			}
		}
		if( str != null && str.length() > 0 )
		{
			m_reader.PDFFindStart(str, false, false);
			m_reader.PDFFind(1);
			str_find = str;
		}
	}
	
	private void onGoToPage()
	{
		
		AlertDialog.Builder builder4 =new AlertDialog.Builder(MyPDFOpen.this);
		final EditText thispagenum = new EditText(this);
		builder4.setView(thispagenum);
		
		builder4.setPositiveButton("Go", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which)
			{
				  
				Toast.makeText(getApplicationContext(), "Searching page...", Toast.LENGTH_LONG).show();
				
				String thisPage = thispagenum.getText().toString();
				//Toast.makeText(getApplicationContext(), "Page " + thisPage, Toast.LENGTH_LONG).show();
				int p = Integer.parseInt(thisPage);
				m_reader.PDFGotoPage(p);
				dialog.dismiss();
				 
			}});
		builder4.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				 
			}});
		builder4.setTitle("Go To Page");
		builder4.setCancelable(false);
		 
 
    	builder4.show();
		
		
	}
	
	
	 
	 
	public void onClick(View v)
	{
		switch( v.getId() )
		{

		case R.id.btn_prev:
			onFindPrev();
			break;
		case R.id.btn_next:
			onFindNext();
			break;
		case R.id.btn_goToPage:
			onGoToPage();
			break;
		case R.id.btn_close:
			m_thumb.thumbClose();
    		m_reader.PDFClose();
        	if( m_doc != null ) m_doc.Close();
    		str_find = null;
	    	setContentView(m_vFiles);
			break;
		}
	}
	public void OnPageClicked(int pageno)
	{
		m_reader.PDFGotoPage(pageno);
	}
	public void OnPageChanged(int pageno)
	{
		m_thumb.thumbGotoPage(pageno);
	}
	 
	public void OnOpenURI(String uri)
	{
	}
	public void OnOpenMovie(String path)
	{
	}
	public void OnOpenSound(int[] paras, String path)
	{
	}
	public void OnOpenAttachment(String path)
	{
	}
	public void OnOpen3D(String path)
	{
	}
	public void OnSelectEnd(String text)
	{
		LinearLayout layout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.dlg_text, null);
		final RadioGroup rad_group = (RadioGroup)layout.findViewById(R.id.rad_group);
		final String sel_text = text;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			@SuppressLint("NewApi")
			public void onClick(DialogInterface dialog, int which)
			{
				if( rad_group.getCheckedRadioButtonId() == R.id.rad_copy )
					Toast.makeText(MyPDFOpen.this, "todo copy text:" + sel_text, Toast.LENGTH_SHORT).show();
				else if( m_reader.PDFCanSave() )
				{
					boolean ret = false;
			        if( rad_group.getCheckedRadioButtonId() == R.id.rad_copy )
			    	{
	                    Toast.makeText(MyPDFOpen.this, "todo copy text:" + sel_text, Toast.LENGTH_SHORT).show();
	                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
	                    android.content.ClipData clip = android.content.ClipData.newPlainText("Radaee", sel_text);
	                    clipboard.setPrimaryClip(clip);                    
			        }
			        else if( rad_group.getCheckedRadioButtonId() == R.id.rad_highlight )
						ret = m_reader.PDFSetSelMarkup(0);
					else if( rad_group.getCheckedRadioButtonId() == R.id.rad_underline )
						ret = m_reader.PDFSetSelMarkup(1);
					else if( rad_group.getCheckedRadioButtonId() == R.id.rad_strikeout )
						ret = m_reader.PDFSetSelMarkup(2);
					else if( rad_group.getCheckedRadioButtonId() == R.id.rad_squiggly )
						ret = m_reader.PDFSetSelMarkup(4);
					if( !ret )
						Toast.makeText(MyPDFOpen.this, "add annotation failed!", Toast.LENGTH_SHORT).show();
				}
				else
					Toast.makeText(MyPDFOpen.this, "can't write or encrypted!", Toast.LENGTH_SHORT).show();
				//onSelect();
				dialog.dismiss();
			}});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}});
		builder.setTitle("Process selected text");
		builder.setCancelable(false);
		builder.setView(layout);
		AlertDialog dlg = builder.create();
		dlg.show();
	}
	public void OnPageModified(int pageno)
	{
		m_thumb.thumbUpdatePage(pageno);
	}

	public void OnAnnotClicked(PDFVPage vpage, Annotation annot)
	{
		m_annot_vpage = vpage;
		m_annot = annot;
 

		btn_prev.setEnabled(annot == null);
        btn_next.setEnabled(annot == null);
        txt_find.setEnabled(annot == null);
	}
}
