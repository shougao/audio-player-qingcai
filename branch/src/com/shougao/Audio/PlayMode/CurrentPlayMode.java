package com.shougao.Audio.PlayMode;

public class CurrentPlayMode {
	IPlayMode localPlayMode = null;
	public void setPlayMode(IPlayMode mode){
		localPlayMode = mode;
	}
	public int getPlayMode(){
		int k = localPlayMode.getPlayMode();
		k = k + 1;
		System.out.println("======playmode =======:" + k);
		return k;//localPlayMode.getPlayMode();
	}
}
