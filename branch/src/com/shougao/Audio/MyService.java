package com.shougao.Audio;

import java.io.IOException;

import com.shougao.Audio.media.MediaService;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MyService extends Service {

//	static int playState; //0. stop 1. playing 2. pause 
//	static final int PLAYMODE_ORDER = 1;
//	static final int PLAYMODE_SINGLE = 2;
//	static final int PLAYMODE_SHUFFLE = 3;
	private MediaService mediaService = new MediaService();
	
	public void onCreate(){
		
	}
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mediaService;
	}
	public boolean onUnbind(Intent intent){
		System.out.println("debug...onUnbind");
		return true;
	}
	
	public void onDestroy(){
		System.out.println("debug...onDestroy");
	}
	
	public void onStart(){
		
	}
}
