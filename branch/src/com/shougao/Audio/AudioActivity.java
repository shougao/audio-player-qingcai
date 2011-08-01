package com.shougao.Audio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AudioActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	/** Called when the activity is first created. */
	private ImageButton btnPlayMode, btnPlay, btnNext, btnList;
	private ImageView btnMain, vPlayMode;
	private IBinder ib;
	private ListView musicListView;
	private ArrayAdapter<String> adapter;
	private FileList fl = new FileList();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		btnPlayMode = (ImageButton) findViewById(R.id.IndPlayMode);
		btnList = (ImageButton) findViewById(R.id.ImgList);
		musicListView = (ListView) findViewById(R.id.MusicListView);
		vPlayMode = (ImageView) findViewById(R.id.imgPlayMode);
		btnPlay.setOnClickListener(this);
		btnPlayMode.setOnClickListener(this);
		btnList.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		bindService(new Intent("com.shougao.Audio.REMOTE_SERVICE"),
				mServiceConn, Context.BIND_AUTO_CREATE);

	}

	private ServiceConnection mServiceConn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			ib = service;
			System.out.println("DEBUG>>>ServiceConnection.");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
		}
	};

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPlay:
			play();
			break;

		case R.id.btnNext:
			next();
			break;

		case R.id.ImgList:
			System.out.println("DEBUG>>>ImgList");
			adapter = new ArrayAdapter(getApplicationContext(),
					R.layout.my_simple_list_item, fl.getMp3());
			musicListView.setAdapter(adapter);
			break;
		case R.id.IndPlayMode:
			System.out.println("========");
			vPlayMode.setImageResource(R.drawable.icon_playmode_shuffle);
		}
	}

	private void play() {
		// TODO Auto-generated method stub
		Parcel pc = Parcel.obtain();
		Parcel pc_reply = Parcel.obtain();
		try {
			System.out.println("DEBUG>>>pc" + pc.toString());
			System.out.println("DEBUG>>>pc_replay" + pc_reply.toString());
			ib.transact(1, pc, pc_reply, 0);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void next() {
		// TODO Auto-generated method stub
		Parcel pc = Parcel.obtain();
		Parcel pc_reply = Parcel.obtain();
		try {
			System.out.println("DEBUG>>>pc" + pc.toString());
			System.out.println("DEBUG>>>pc_replay" + pc_reply.toString());
			ib.transact(2, pc, pc_reply, 0);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
	}
}
