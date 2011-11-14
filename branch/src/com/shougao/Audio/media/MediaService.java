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

	int playState = -1; // 1 play, 0 pause, -1 other
	final int PLAYING = 1;
	final int PAUSE = 2;
	final int STOP = 0;
	int defaultPlayMode = 1;
	int playIndex = 0;
	final int PLAYMODE_NORMAL = 1;// ѭ������
	final int PLAYMODE_ORDER = 2;// ˳�򲥷�
	final int PLAYMODE_SINGLE = 3;// �����ظ�
	final int PLAYMODE_SHUFFLE = 4;// �������
	private CurrentPlayMode localPlayMode = new CurrentPlayMode();
	private MediaPlayer mp = new MediaPlayer();
	FileList audioFileList = new FileList();
	private int markFirstPlay = 1; // ����ǲ��ǵ�һ�β����ļ���1. ��һ�β����б��һ���ļ�
	private int markSelectedPlay = 0; // ����ǲ���ѡ�񲥷ţ�1�� ѡ�񲥷š�0.Ĭ�ϲ�ѡ�񲥷š�
	private int intPassSelectedFileIndex = -1;
	private int durationTime = 0;
	private int controlPlay = 0;// ���Ʋ��ŷ�ʽ��ͨ����һ����һ�����š�
	private String filePath = null;
	private String currentPlayAudio = null;
	private List<String> mp3DetailInfo = new ArrayList<String>();//��������ϸ��Ϣ
	public String playPathDir = null;//��ǰ�����ļ��е�·��

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
		currentPlayAudio = String.valueOf(playIndex +1)+"/"+String.valueOf(getPlayList().size());
		return currentPlayAudio;
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

		System.out.println("debug.....playState:" + playState+":"+markFirstPlay+":"+markSelectedPlay+":"+controlPlay);
		if (playState != 0) {// �����Ƿ����ͣ��ʼ����
			System.out.println("debug.....markFirstPlay:" + markFirstPlay);
			// ��һ�β�������
			if (markFirstPlay == 1) {
				System.out.println("debug.......... playIndex:" + playIndex);
				filePath = audioFileList.getFilePath(playIndex);// indexΪ
				System.out.println("debug.......... file Path:" + filePath);
				markFirstPlay = 0;
			}
			// ����ѡ��������ļ�
			if (markSelectedPlay == 1) {
				playIndex = intPassSelectedFileIndex;
				filePath = audioFileList.getFilePath(playIndex);
				System.out.println("debug......filePath:" + filePath);
				markSelectedPlay = 0;
			}
			// ��һ������һ�����Ʋ���
			if (controlPlay == 1) {
				filePath = audioFileList.getFilePath(playIndex);
				System.out.println("debug.......... file Path:" + filePath);
				controlPlay = 0;
			}
			System.out.println("filePath: " + filePath);
			try {
				mp.setDataSource(filePath);
				mp.prepare();
				setDuration();
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
			setDuration();
		}
		
		if (playState == 0) {// 0 ��ʾ��ͣ
			setDuration();
			mp.start();
			playState = 1;
		}
		// һ�ײ��Ž��������������
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
//				try {
//					setDuration();
//				} catch (RemoteException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				//setMp3Info
			}
		});
	}

	/*
	 * ���stop��reset�� Ϊ�ٲ������ļ���׼���� (non-Javadoc)
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
		if (playMode == PLAYMODE_NORMAL) {// ˳�򲥷� value = 1.
			playIndex = playIndex - 1;
			if (playIndex < 0) {// ��һ������
				playIndex = 0;
			}
		}
		if (playMode == PLAYMODE_ORDER) {// ѭ������ value = 2.
			playIndex = playIndex - 1;
			if (playIndex < 0) {// ��һ������
				playIndex = 0;
			}
		}
		if (playMode == PLAYMODE_SINGLE) {// �������� value = 3.
			playIndex = playIndex;
		}
		if (playMode == PLAYMODE_SHUFFLE) {// ������� value = 4.
			Random r = new Random();
			playIndex = r.nextInt(fileNum);
		}
		play();
	}

	/*
	 * ���յ�ǰ����ģʽ�������һ�׸�����index��indexΪȫ��int index�� (non-Javadoc)
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
		if (playMode == PLAYMODE_NORMAL) {// ˳�򲥷� value = 1.
			playIndex = playIndex + 1;
			System.out.println("playIndex:" + playIndex);
			System.out.println("fileNum:" + fileNum);
			if (playIndex == fileNum) {// ��һ������
				playIndex = 0;
			}
		}
		if (playMode == PLAYMODE_ORDER) {// ѭ������ value = 2.
			playIndex = playIndex + 1;
			if (playIndex == fileNum) {
				playIndex = 0;
			}
		}
		if (playMode == PLAYMODE_SINGLE) {// �������� value = 3.
			playIndex = playIndex;
		}
		if (playMode == PLAYMODE_SHUFFLE) {// ������� value = 4.
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
		durationTime = mp.getDuration();// mp�����л����Զ����»�ó���ʱ�䡣
	}

	@Override
	public int getCurrentProcess() throws RemoteException {
		// TODO Auto-generated method stub
		return mp.getCurrentPosition();
	}

	/*
	 * ͨ��seekbar�϶����seekֵ(non-Javadoc) (non-Javadoc)
	 * 
	 * @see com.shougao.Audio.media.IMediaService#seek(int)
	 */
	@Override
	public void seek(int paramInt) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("!");
		mp.seekTo(paramInt);
	}

	/**
	 * ÿ������Activity�е���һ�Σ���ʾ��ǰ��������Ϣ
	 */
	@Override
	public List<String> getMp3Info() throws RemoteException {
		// TODO Auto-generated method stub
		if(filePath == null)
			return null;
//		System.out.println("====title========================="+filePath);
		//ÿ��Ҫ�����һ���ĸ�����Ϣ��ʹ�õ�ǰ����Ϣ���
		mp3DetailInfo.clear();
		mp3Info currentInfo = new mp3Info(filePath);
//		int id = 0;
		if(currentInfo.getMusicTitle() != null){
			mp3DetailInfo.add(currentInfo.getMusicTitle());
//			System.out.println("======!"+currentInfo.getMusicTitle());
//			System.out.println("=====2!"+mp3DetailInfo.get(0));
		}
		if(currentInfo.getMusicArtist() != null){
			mp3DetailInfo.add(currentInfo.getMusicArtist());
		}
		if(currentInfo.getMusicAlbum() != null){
			mp3DetailInfo.add(currentInfo.getMusicAlbum());
		}
		if(currentInfo.getMusicComment() != null){
			mp3DetailInfo.add(currentInfo.getMusicComment());
		}
//		System.out.println(currentInfo.getMusicTitle()+"!====title=========================");
		return mp3DetailInfo;
	}
	
	/**
	 * ���Ŀ¼���²�ɨ�裬��������FileList��ʵ����
	 */
	public void updatePath(String paramStr){
		playPathDir = paramStr;
		System.out.println("=======from server:"+playPathDir);
		audioFileList.initFileList(playPathDir);
	}
	
	public String getPlayPath(){
		
		return playPathDir;
	}
}
