package com.shougao.Audio;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class BrowserFile extends Activity{
	
	//定义各个变量
	private EditText inputEditor;
	private String getPath;
	private Button getPathButton;
	private Bundle b;
	private Intent iBrowser;
	private ListView dirListView;
	private ArrayAdapter<String> dirAdapter = null;
	private ArrayList<String> currentDirList = new ArrayList<String>();
	
	/*
	 * 在oncreate中初始化各个按钮和list
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.browser);

        dirListView = (ListView)findViewById(R.id.dirlist);
        dirListView.setOnItemClickListener(dirListOnItemClickListenerClick);
        getPathButton = (Button)findViewById(R.id.browserPlay); 
        inputEditor = (EditText)findViewById(R.id.dirPath);
        getPath = inputEditor.getText().toString();
        getPathButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iBrowser = new Intent(BrowserFile.this, AudioActivity.class);
				b = new Bundle();
				b.putString("str", getPath);
				iBrowser.putExtras(b);
		        BrowserFile.this.setResult(RESULT_OK, iBrowser);  
		        BrowserFile.this.finish();
		        System.out.println("======browserPlayEnd");
			}
        });
        System.out.print("===========helo");
        currentDirList.add("updir");
        dirAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, currentDirList);        
        dirListView.setAdapter(dirAdapter);
    }
    
    
    OnItemClickListener dirListOnItemClickListenerClick = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			ListView listView = (ListView)parent;
			String selectedPath = (String) listView.getItemAtPosition(position);
			System.out.println("=="+selectedPath);
			inputEditor.setText(selectedPath);
			getPath = selectedPath;
			setNewDirList(selectedPath);
		}

		private void setNewDirList(String selectedPath) {
			// TODO Auto-generated method stub
			
		}
    	
    };
} 












