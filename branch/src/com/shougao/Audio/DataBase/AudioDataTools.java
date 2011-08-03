package com.shougao.Audio.DataBase;

import java.io.File;

public class AudioDataTools {
	
	public String getFilePath(){
		String dir = null;
		String fileName = null;
		//return the first file path.
		String path = spell(dir, fileName);
		return path;
	}
	
	public String getFilePath(String fileName){
		String dir = null;
		//find this file and get the path.
		String path = spell(dir, fileName);
		return path;
	}
	
	public String getFilePath(String dir, String fileName){
		String path = spell(dir, fileName);
		return path;
	}

	private String spell(String dir, String fileName) {
		String path = dir + File.separator + fileName;
		return path;
	}
}