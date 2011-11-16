package com.shougao.Audio;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
	private Button getPathButton;
	private Bundle b;
	private Intent iBrowser;
	private ListView dirListView;
	private ArrayAdapter<String> dirAdapter = null;
	private ArrayList<String> currentDirList = new ArrayList<String>();
	private HashMap dirPathSet = new HashMap();
	private String selectedPath = "/";
	private String selectedDir = null;
	
	/*
	 * 在oncreate中初始化各个按钮和list
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.browser);

        updateDirList("/");
        dirListView = (ListView)findViewById(R.id.dirlist);
        dirListView.setOnItemClickListener(dirListOnItemClickListenerClick);
        getPathButton = (Button)findViewById(R.id.browserPlay); 
        inputEditor = (EditText)findViewById(R.id.dirPath);
        
        getPathButton.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iBrowser = new Intent(BrowserFile.this, AudioActivity.class);
				b = new Bundle();
				b.putString("str", selectedPath);
				iBrowser.putExtras(b);
		        BrowserFile.this.setResult(RESULT_OK, iBrowser);  
		        BrowserFile.this.finish();
		        System.out.println("======browserPlayEnd");
			}
        });
        dirAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, currentDirList);        
        dirListView.setAdapter(dirAdapter);
    }
    
    
	OnItemClickListener dirListOnItemClickListenerClick = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			ListView listView = (ListView)parent;
			selectedDir = (String) listView.getItemAtPosition(position);
			String strPath =  (String)dirPathSet.get(selectedDir);
//			if(!selectedPath.equals("/")){
//				if(selectedDir.equals("..")){
//					upToDir();
//				}
//			}else{
//				goToDir(strPath);
//			}
			goToDir(strPath);
		}
    };
    
    /**
     * 点击指定目录后，更新新的列表list
     * @param path
     */
    private void updateDirList(String path) {
		// TODO Auto-generated method stub
		File f = new File(path);
		if(!f.exists()){
			return;
		}
		if(f.canRead()){
			if(f.isDirectory()){
				dirPathSet.clear();
				currentDirList.clear();
				String list[] = f.list();
				for(int i=0; i<list.length; i++){
					File checkIsDir = new File(f.getPath()+File.separator+list[i]);
					if(checkIsDir.isDirectory()){
						currentDirList.add(checkIsDir.getName());//只存放名字
						dirPathSet.put(list[i], checkIsDir.getAbsolutePath());//名字和对应的path
						System.out.println("==========get a dir:"+checkIsDir.getName());
					}
				}
			}
		}
	}

    protected void goToDir(String path) {
		// TODO Auto-generated method stub
		inputEditor.setText(path);
		updateDirList(path);
		updateDirAdapter();
	}

	/**
     * 返回到上层目录
     */
    protected void upToDir() {
		// TODO Auto-generated method stub
		File path = new File(selectedPath);
		String parentPath = path.getParentFile().getAbsolutePath();
		goToDir(parentPath);
	}

	/**
     * 根据新的目录列表，把新的列表显示到界面上
     */
	protected void updateDirAdapter() {
		// TODO Auto-generated method stub
		dirAdapter.clear();
		dirAdapter.add("..");
		for(String str: currentDirList){
			dirAdapter.add(str);
		}
		dirAdapter.notifyDataSetChanged();
	}
} 












