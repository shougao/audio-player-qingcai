package com.shougao.Audio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;

public class myBinder extends Binder implements IBinder {
	private Play myPlay = new Play();

	public boolean onTransact(int code, Parcel pc, Parcel pc_reply, int flags) {
		switch (code) {
		case 1:
			System.out.println("DEBUG>>>start mp3play:");
			myPlay.mp3Play();
			System.out.println("DEBUG>>>mybinder:");
			break;
		case 2:
			System.out.println("DEBUG>>>next.");
			myPlay.next();
		}
		return true;
	}
}
