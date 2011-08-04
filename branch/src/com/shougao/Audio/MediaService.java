package com.shougao.Audio;

import java.io.IOException;
import java.util.List;

import com.shougao.Audio.DataBase.AUDIO_TAG;
import com.shougao.Audio.DataBase.AudioDataTools;
import com.shougao.Audio.DataBase.FileList;
import com.shougao.Audio.PlayMode.CurrentPlayMode;
import com.shougao.Audio.PlayMode.IPlayMode;
import com.shougao.Audio.PlayMode.NormalPlayMode;
import com.shougao.Audio.PlayMode.OrderPlayMode;
import com.shougao.Audio.PlayMode.ShufflePlayMode;
import com.shougao.Audio.PlayMode.SinglePlayMode;

import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.ImageView;

public class MediaService extends com.shougao.Audio.media.IMediaService.Stub {

	static int playState = -1; // 1 play, 0 pause, -1 other
	static final int PLAYING = 1;
	static final int PAUSE = 2;
	static final int STOP = 0;
	static int intPlayMode = 1;
	static final int PLAYMODE_NORMAL = 1;
	static final int PLAYMODE_ORDER = 2;
	static final int PLAYMODE_SINGLE = 3;
	static final int PLAYMODE_SHUFFLE = 4;
	private MediaPlayer mp = new MediaPlayer();

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
	public List<String> getPlayList() throws RemoteException {
		// TODO Auto-generated method stub
		FileList localFileList = new FileList();
		
		return localFileList.getFileNameList();
	}

	@Override
	public void play() throws RemoteException {
		// if(playState == 1){
		// return;
		// }
		// if(mp.equals(null)){
		// mp.reset();
		// int i=1;
		// FileList localFile = new FileList();
		// String fileName = localFile.getFileNameList().get(i);
		// String filePath = localFile.getFilePath(fileName);
		// }
		if (playState == -1) {
			mp.reset();
			int i = 1;
			FileList localFile = new FileList();
			String fileName = localFile.getFileNameList().get(i);
			String filePath = localFile.getFilePath(fileName);
			try {
				mp.setDataSource(filePath);
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
			playState = 0;
		}
		if (playState == 0) {
//			mp.reset();
//			int i = 1;
//			FileList localFile = new FileList();
//			String fileName = localFile.getFileNameList().get(i);
//			String filePath = localFile.getFilePath(fileName);
//			try {
//				mp.setDataSource(filePath);
//				mp.prepare();
//			} catch (IllegalArgumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalStateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			mp.start();
			playState = 1;
		}
	}

	@Override
	public void stop() throws RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() throws RemoteException {
		// TODO Auto-generated method stub
		if (playState == 0) {
			return;
		}
		mp.pause();
		playState = 0;
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
		CurrentPlayMode localPlayMode = new CurrentPlayMode();
		// localPlayMode.setPlayMode(new OrderPlayMode());
		int i = localPlayMode.getPlayMode();
		System.out.println("=======:" + i);
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
		System.out.println("=====intPlayMode:" + intPlayMode);
		intPlayMode = intPlayMode + 1;
		CurrentPlayMode localPlayMode = new CurrentPlayMode();
		intPlayMode = (intPlayMode % 4);
		switch (intPlayMode) {
		case 1:
			localPlayMode.setPlayMode(new NormalPlayMode());
			break;
		case 2:
			localPlayMode.setPlayMode(new OrderPlayMode());
			break;
		case 3:
			localPlayMode.setPlayMode(new SinglePlayMode());
			break;
		case 0:
			localPlayMode.setPlayMode(new ShufflePlayMode());
			break;
		}
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
