package com.shougao.Audio;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MyService extends Service {

	static int playState; //1. playing  2. pause
	private MediaService mediaService = new MediaService();
	private MediaPlayer mp = new MediaPlayer();

	static {
		playState = -1;
	}

//	MyService() {
//		mediaService = new MediaService();
//	}
	// 使用无参构造函数导致程序异常  ？？？？？？？？？？？？？

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mediaService;
	}
	
	public void onStart(){
		
	}
	
	public void onCreate(){
		
	}

	public void play() {
		mp.reset();
		System.out.println("DEBUG>>>play:");
		try {
			mp.setDataSource(getFile());
			mp.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mp.start();
		playState = 1;
	}

	public String getFile() {
		String path = null;
		FileList fl = new FileList();
		path = fl.getPath().get(0);
		return path;
	}
}
