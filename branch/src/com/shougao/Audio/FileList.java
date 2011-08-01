package com.shougao.Audio;

import java.io.File;
import java.util.ArrayList;

public class FileList {

	private static final String SD_PATH = "/sdcard";
	private ArrayList<String> mp3List = new ArrayList<String>();
	private ArrayList<String> pathList = new ArrayList<String>();

	public void scanFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (file.canRead()) {
			if (file.isDirectory()) {
				String list[] = file.list();
				for (int i = 0; i < list.length; ++i) {
					scanFile(file.getPath() + File.separator + list[i]);
				}
			} else if (file.isFile()) {
				if (file.getName().endsWith("mp3") || file.getName().endsWith("MP3")) {
					for(String music: mp3List){
						if(music.equals(file.getName()))
							return;
					}
					mp3List.add(file.getName());
					pathList.add(file.getAbsolutePath());
				}
			}
		}
	}

	public ArrayList<String> getMp3() {
		scanFile(SD_PATH);
		return mp3List;
	}

	public ArrayList<String> getPath() {
		scanFile(SD_PATH);
		return pathList;
	}
}
