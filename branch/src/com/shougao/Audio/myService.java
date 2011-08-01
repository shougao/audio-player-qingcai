package com.shougao.Audio;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class myService extends Service {
	private IBinder ib = null;

	@Override

	
	public void onCreate() {
		System.out.println("DEBUG>>>on create");
		ib = new myBinder();
	}
	
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("DEBUG>>> on bind");
		return ib;
	}
	
	public void onStart(){
		System.out.println("DEBUG>>> on start");
		//play();
	}
	
	public void onStop(){
	}
}
