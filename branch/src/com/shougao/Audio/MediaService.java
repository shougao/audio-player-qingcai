package com.shougao.Audio;

import com.shougao.Audio.DataBase.AUDIO_TAG;

import android.os.Parcel;
import android.os.RemoteException;

public class MediaService extends com.shougao.Audio.media.IMediaService.Stub {

	@Override
	public AUDIO_TAG[] getAudio() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCurrentOrderIndex() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AUDIO_TAG[] getCurrentPlayAudio() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCurrentPlayIndex() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDruation() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMediaTime() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AUDIO_TAG[] getPlayList() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void play() throws RemoteException {
		// TODO Auto-generated method stub
		MyService myService = new MyService();
		myService.play();
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
	public int getPlayState() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRepeatMode() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getShuffleMode() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalPlayNum() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setRepeatMode() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCurrentPlayIndex() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setShuffleMode(int paramInt) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMediaTime(int paramInt) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AUDIO_TAG[] setPlayList() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStopTimer(long paramInt) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePlayList(AUDIO_TAG[] paramInt) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
