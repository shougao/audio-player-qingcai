package com.shougao.Audio;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MyService extends Service {

	static int playState; //0. stop 1. playing 2. pause 
	static final int PLAYMODE_ORDER = 1;
	static final int PLAYMODE_SINGLE = 2;
	static final int PLAYMODE_SHUFFLE = 3;
	private MediaService mediaService = new MediaService();
	private MediaPlayer mp = new MediaPlayer();

	static {
		playState = -1;
	}


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

	public void next() {
		// TODO Auto-generated method stub
		if(playState == 1){
			mp.stop();
		}
			MediaService localMediaService = mediaService;
//			int k = localMediaService.getRepeatMode();
//			switch(int k){
//			case PLAYMODE_ORDER:
//			case PLAYMODE_SINGLE:
//				
//			case PLAYMODE_
//			}
//			if(k == )
//			int i = localMediaService.getCurrentPlayIndex();
//			int j = localMediaService.getTotalPlayNum();
			
	}

	public void prev() {
		// TODO Auto-generated method stub
		
	}

	public void pause() {
		// TODO Auto-generated method stub
		
	}
	public void pause1() {
		// TODO Auto-generated method stub
		
	}
	
	public String getFile() {
		String path = null;
		FileList fl = new FileList();
		path = fl.getPath().get(0);
		return path;
	}
}
