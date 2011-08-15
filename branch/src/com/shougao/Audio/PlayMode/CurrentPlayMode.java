package com.shougao.Audio.PlayMode;

public class CurrentPlayMode {
	private IPlayMode localPlayMode =new NormalPlayMode();
	public void setPlayMode(IPlayMode mode){
		System.out.println("service.currentPlayMode.");
		localPlayMode = mode;
	}
	public int getPlayMode(){
//		if(localPlayMode.equals(null)) System.out.println("localPlayMode is null.");
		int k = localPlayMode.getPlayMode();
		k = k + 1;
		System.out.println("======playmode =======:" + k);
		return k;//localPlayMode.getPlayMode();
	}
}
