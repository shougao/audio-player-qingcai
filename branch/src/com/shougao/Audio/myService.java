package com.shougao.Audio;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class myService extends Service {
	private MediaService myMediaService = new MediaService();
	//private IBinder ib = null;
	
	public void onCreate() {
		System.out.println("DEBUG>>>on create");
	}
	
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return myMediaService;
	}
	
	public void onStart(){
		System.out.println("DEBUG>>> on start");
	}
	
	public void onStop(){
	}
	
}
