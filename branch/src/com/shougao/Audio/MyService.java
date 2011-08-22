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
		System.out.println("debug....=============thread3======"+Thread.currentThread().getId());
	}
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("debug...service:onBind");
		return mediaService;
	}
	public boolean onUnbind(Intent intent){
		super.onUnbind(intent);
		System.out.println("debug...service:onUnbind");
		return true;
	}
	
	public void onDestroy(){
		super.onDestroy();
		System.out.println("debug...service:onDestroy");
	}
	
	public void onStart(){
		
	}
}
