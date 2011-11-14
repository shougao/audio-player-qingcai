package com.shougao.Audio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class BrowserFile extends Activity implements OnClickListener{
	
	//定义各个变量
	EditText inputEditor;
	private String getPath;
	Button getPathButton;
	Intent iBrowser;
	
	/*
	 * 在oncreate中初始化各个按钮和list
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.browser);

        getPathButton = (Button)findViewById(R.id.browserPlay); 
        inputEditor = (EditText)findViewById(R.id.dirPath);
        getPath = inputEditor.getText().toString();
        iBrowser = new Intent(BrowserFile.this, AudioActivity.class);  
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.browserPlay:
			Bundle b = new Bundle();
			b.putString("str", getPath);
			iBrowser.putExtras(b);
            BrowserFile.this.setResult(RESULT_OK, iBrowser);  
            BrowserFile.this.finish();
			
		}
	}  
}  