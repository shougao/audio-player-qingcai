package com.shougao.Audio;

import android.os.Parcel;
import android.os.RemoteException;

public class MediaService extends com.shougao.Audio.media.IMediaService.Stub {

	@Override
	public String getString() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void play() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("DEBUG>>> MediaService:play()");
		System.out.println("DEBUG>>>  MediaServcie thread:" + Thread.currentThread().getId());
	}

	@Override
	public void stop() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void prev() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void next() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPlayState() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void getRepeatMode() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void getCurrentPlayIndex() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void getDruation() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void getMediaTime() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void getShuffleMode() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void getTotalPlayNum() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] getPlayList() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] setPlayList() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] updatePlayList() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
