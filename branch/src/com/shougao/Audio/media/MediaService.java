package com.shougao.Audio.media;

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
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.ImageView;

public class MediaService extends com.shougao.Audio.media.IMediaService.Stub {

	static int playState = -1; // 1 play, 0 pause, -1 other
	static final int PLAYING = 1;
	static final int PAUSE = 2;
	static final int STOP = 0;
	static int intPlayMode = 1;
	static int playIndex = 1;
	static final int PLAYMODE_NORMAL = 1;// 循环播放
	static final int PLAYMODE_ORDER = 2;// 顺序播放
	static final int PLAYMODE_SINGLE = 3;// 单曲重复
	static final int PLAYMODE_SHUFFLE = 4;// 随机播放
	private MediaPlayer mp = new MediaPlayer();
	FileList audioFileList = new FileList();
	private static int markFirstPlay = 1; // 标记是不是第一次播放文件，1. 第一次播放列表第一个文件
	private static int markSelectedPlay = 0; // 标记是不是选择播放，1， 选择播放。0.默认不选择播放。
	private static int intPassSelectedFileIndex = 3;
	private static int durationTime = 0;

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
	public String getCurrentPlayAudio() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCurrentPlayIndex() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDuration() throws RemoteException {
		// TODO Auto-generated method stub
		return durationTime;
	}

	@Override
	public int getMediaTime() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getPlayList() throws RemoteException {
		// TODO Auto-generated method stub
		return audioFileList.getFileNameList();
	}

	@Override
	public void play() throws RemoteException {
		System.out.println("debug....play():");
		String fileName = null;
		String filePath = null;
		System.out.println("debug.....playState:" + playState);
		if (playState == -1) {
			// 第一次播放音乐
			System.out.println("debug.....markFirstPlay:" + markFirstPlay);
			if (markFirstPlay == 1) {
				System.out.println("debug.......... playIndex:" + playIndex);
				filePath = audioFileList.getFilePath(playIndex);// index为
				System.out.println("debug.......... file Path:" + filePath);
				markFirstPlay = 0;
			}
			// 播放选择的音乐文件
			if (markSelectedPlay == 1) {
				playIndex = intPassSelectedFileIndex -1;
				filePath = audioFileList.getFilePath(playIndex);
				markSelectedPlay = 0;
				System.out.println("debug......filePath:" + filePath);
			}
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
			setDuration();
			mp.start();
			playState = 1;
		}
		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				try {
					if (getRepeatMode() == PLAYMODE_NORMAL) {
						play();
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	/*
	 * 完成stop和reset。 为再播放新文件做准备。 (non-Javadoc)
	 * 
	 * @see com.shougao.Audio.media.IMediaService#stop()
	 */
	@Override
	public void stop() throws RemoteException {
		// TODO Auto-generated method stub
		mp.stop();
		mp.reset();
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
		CurrentPlayMode pm = new CurrentPlayMode();
		System.out.println("get play Mode pm. new CurrentPlayMode()");
		if(pm == null) System.out.println("pm is null");
		System.out.println("media.Service.debug......next." + pm.getPlayMode());
	}

	/*
	 * 按照当前播放模式，获得下一首歌曲的index，index为全局int index。 
	 * (non-Javadoc)
	 * @see com.shougao.Audio.media.IMediaService#next()
	 */
	@Override
	public void next() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("media.Service.debug......next.");
		stop();
		int playMode = getRepeatMode();
		System.out.println("playmode:" + playMode);
		if (playMode == PLAYMODE_NORMAL) {// 顺序播放 value = 1.
			playIndex = playIndex = 1;
		}
		if (playMode == PLAYMODE_ORDER) {// 顺序播放 value = 2.

		}
		if (playMode == PLAYMODE_SINGLE) {// 顺序播放 value = 3.

		}
		if (playMode == PLAYMODE_SHUFFLE) {// 顺序播放 value = 4.

		}
	}

	@Override
	public int getPlayState() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRepeatMode() throws RemoteException {
		System.out.println("media.Service.debug......getRepeatMode.");
		CurrentPlayMode localPlayMode = new CurrentPlayMode();
		// localPlayMode.setPlayMode(new OrderPlayMode());
		int i = localPlayMode.getPlayMode();
		System.out.println("debug...=======playmode:" + i);
		return i;
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

	@Override
	public void passSelectedFile(int paramInt) throws RemoteException {
		// TODO Auto-generated method stub
		playState = -1;
		markSelectedPlay = 1;
		intPassSelectedFileIndex = paramInt;
		stop();
		play();
	}

	@Override
	public void release() throws RemoteException {
		// TODO Auto-generated method stub
		mp.release();
	}

	@Override
	public void initPlayer() throws RemoteException {
		// TODO Auto-generated method stub
		mp.reset();
		markFirstPlay = 1;
	}

	@Override
	public void setDuration() throws RemoteException {
		// TODO Auto-generated method stub
		durationTime = mp.getDuration();
	}

	@Override
	public int getCurrentProcess() throws RemoteException {
		// TODO Auto-generated method stub
		return mp.getCurrentPosition();
	}

	/*
	 * 通过seekbar拖动获得seek值(non-Javadoc) (non-Javadoc)
	 * 
	 * @see com.shougao.Audio.media.IMediaService#seek(int)
	 */
	@Override
	public void seek(int paramInt) throws RemoteException {
		// TODO Auto-generated method stub
		mp.seekTo(paramInt);
	}
}
