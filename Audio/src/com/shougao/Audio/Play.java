package com.shougao.Audio;

import java.io.IOException;
import java.util.ArrayList;

import android.media.MediaPlayer;

public class Play {
	public int PLAY_MODE = 1;
	private MediaPlayer mp = new MediaPlayer();
	private FileList fl = new FileList();
	
	public void mp3Play() {
		mp.reset();
		System.out.println("DEBUG>>>play:");
		try {
			mp.setDataSource(getFile());
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
		mp.start();
	}
	
	public String getFile(){
		String path = null;
		//Ë³Ðò²¥·Å
		if(PLAY_MODE == 1){
			path = fl.getPath().get(0);
			return path;
		}
		if(PLAY_MODE == 2){
			//Ëæ»ú²¥·Å
			int randomNumber = 0;
			randomNumber = (int)(Math.random()*fl.getPath().size())+1;
			return fl.getPath().get(randomNumber);
		}
		return null;
	}

	public void next() {
		// TODO Auto-generated method stub
		mp.stop();
	}
}
