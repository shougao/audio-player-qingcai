package com.shougao.Audio.media;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.shougao.Audio.DataBase.AUDIO_TAG;
import com.shougao.Audio.DataBase.AudioDataTools;
import com.shougao.Audio.DataBase.FileList;
import com.shougao.Audio.DataBase.mp3Info;
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
	static int defaultPlayMode = 1;
	static int playIndex = 0;
	static final int PLAYMODE_NORMAL = 1;// 循环播放
	static final int PLAYMODE_ORDER = 2;// 顺序播放
	static final int PLAYMODE_SINGLE = 3;// 单曲重复
	static final int PLAYMODE_SHUFFLE = 4;// 随机播放
	private CurrentPlayMode localPlayMode = new CurrentPlayMode();
	private MediaPlayer mp = new MediaPlayer();
	FileList audioFileList = new FileList();
	private static int markFirstPlay = 1; // 标记是不是第一次播放文件，1. 第一次播放列表第一个文件
	private static int markSelectedPlay = 0; // 标记是不是选择播放，1， 选择播放。0.默认不选择播放。
	private static int intPassSelectedFileIndex = -1;
	private static int durationTime = 0;
	private static int controlPlay = 0;// 控制播放方式是通过上一曲下一曲播放。
	private String filePath = null;

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

		System.out.println("debug.....playState:" + playState);
		if (playState != 0) {// 控制是否从暂停开始播放
			System.out.println("debug.....markFirstPlay:" + markFirstPlay);
			if (markFirstPlay == 1) {// 第一次播放音乐
				System.out.println("debug.......... playIndex:" + playIndex);
				filePath = audioFileList.getFilePath(playIndex);// index为
				System.out.println("debug.......... file Path:" + filePath);
				markFirstPlay = 0;
			}
			// 播放选择的音乐文件
			if (markSelectedPlay == 1) {
				playIndex = intPassSelectedFileIndex;
				filePath = audioFileList.getFilePath(playIndex);
				System.out.println("debug......filePath:" + filePath);
				markSelectedPlay = 0;
			}
			// 上一曲，下一曲控制播放
			if (controlPlay == 1) {
				filePath = audioFileList.getFilePath(playIndex);
				System.out.println("debug.......... file Path:" + filePath);
				controlPlay = 0;
			}
			System.out.println("filePath: " + filePath);
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
		if (playState == 0) {// 0 表示暂停
			setDuration();
			mp.start();
			playState = 1;
		}
		// 一首播放结束调用这个函数
		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				try {
					next();
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
		controlPlay = 1;
		int fileNum = audioFileList.fileNum();
		System.out.println("media.Service......previous.");
		stop();
		int playMode = getRepeatMode();
		System.out.println("playmode:" + playMode);
		if (playMode == PLAYMODE_NORMAL) {// 顺序播放 value = 1.
			playIndex = playIndex - 1;
			if (playIndex < 0) {// 上一曲播放
				playIndex = 0;
			}
		}
		if (playMode == PLAYMODE_ORDER) {// 循环播放 value = 2.
			playIndex = playIndex - 1;
			if (playIndex < 0) {// 上一曲播放
				playIndex = 0;
			}
		}
		if (playMode == PLAYMODE_SINGLE) {// 单曲播放 value = 3.
			playIndex = playIndex;
		}
		if (playMode == PLAYMODE_SHUFFLE) {// 随机播放 value = 4.
			Random r = new Random();
			playIndex = r.nextInt(fileNum);
		}
		play();
	}

	/*
	 * 按照当前播放模式，获得下一首歌曲的index，index为全局int index。 (non-Javadoc)
	 * 
	 * @see com.shougao.Audio.media.IMediaService#next()
	 */
	@Override
	public void next() throws RemoteException {
		// TODO Auto-generated method stub
		controlPlay = 1;
		int fileNum = audioFileList.fileNum();
		System.out.println("media.Service......next.");
		stop();
		int playMode = getRepeatMode();
		System.out.println("playmode:" + playMode);
		if (playMode == PLAYMODE_NORMAL) {// 顺序播放 value = 1.
			playIndex = playIndex + 1;
			if (playIndex > fileNum) {// 下一曲播放
				playIndex = 0;
			}
		}
		if (playMode == PLAYMODE_ORDER) {// 循环播放 value = 2.
			playIndex = playIndex + 1;
			if (playIndex > fileNum) {
				playIndex = 0;
			}
		}
		if (playMode == PLAYMODE_SINGLE) {// 单曲播放 value = 3.
			playIndex = playIndex;
		}
		if (playMode == PLAYMODE_SHUFFLE) {// 随机播放 value = 4.
			Random r = new Random();
			playIndex = r.nextInt(fileNum);
		}
		play();
	}

	@Override
	public int getPlayState() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRepeatMode() throws RemoteException {
		System.out.println("media.Service.debug......getRepeatMode.");
		// localPlayMode.setPlayMode(new OrderPlayMode());
		int i = localPlayMode.getPlayMode();
		// System.out.println("debug...=======playmode:" + i);
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
		System.out.println("=====defaultPlayMode:" + defaultPlayMode);
		defaultPlayMode = defaultPlayMode + 1;
		// CurrentPlayMode localPlayMode = new CurrentPlayMode();
		defaultPlayMode = (defaultPlayMode % 4);
		switch (defaultPlayMode) {
		case 1:
			localPlayMode.setPlayMode(new NormalPlayMode());
			// int a = localPlayMode.getPlayMode();
			// System.out.println("=====defaultPlayMode:" + a);
			break;
		case 2:
			localPlayMode.setPlayMode(new OrderPlayMode());
			// int b = localPlayMode.getPlayMode();
			// System.out.println("=====defaultPlayMode:" + b);
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
		markFirstPlay = 0;
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
		durationTime = mp.getDuration();// mp对象切换后，自动重新获得持续时间。
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
		System.out.println("!");
		mp.seekTo(paramInt);
	}

	@Override
	public List<String> getMp3Info() throws RemoteException {
		// TODO Auto-generated method stub
		if(filePath == null)
			return null;
		System.out.println("====title=========================");
		mp3Info currentInfo = new mp3Info(filePath);
		List<String> mp3Info = new ArrayList<String>();
//		int id = 0;
		if(currentInfo.getMusicTitle() != null){
			mp3Info.add(currentInfo.getMusicTitle());
//			id = id + 1;
		}
		if(currentInfo.getMusicArtist() != null){
			mp3Info.add(currentInfo.getMusicArtist());
//			id = id + 1;
		}
		if(currentInfo.getMusicAlbum() != null){
			mp3Info.add(currentInfo.getMusicAlbum());
//			id = id + 1;
		}
		if(currentInfo.getMusicComment() != null){
			mp3Info.add(currentInfo.getMusicComment());
//			id = id + 1;
		}
		System.out.println(currentInfo.getMusicTitle()+"!====title=========================");
		return mp3Info;
	}
}
