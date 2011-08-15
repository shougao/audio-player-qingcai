package com.shougao.Audio.DataBase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileList {

	/*
	 * 所有id都从0开始计算，在界面显示的歌曲列表从1开始
	 * 2011-8-15
	 */
	private static final String SD_PATH = "/sdcard";
	private ArrayList<String> mp3List = new ArrayList<String>();//用于存放显示的歌曲列表
	private HashMap pathMap = new HashMap();//用于存放id - 路径对应表
	private HashMap listMap = new HashMap();//用于存放id - 文件内容对象表
	private int id = 1;
	
	public FileList(){
		initFileList();
	}
	private void initFileList() {
		// TODO Auto-generated method stub
		scanFile(SD_PATH);
	}
	
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
					FileContent fc = new FileContent();
					fc.setFileName(file.getName());
					mp3List.add(id + "." + file.getName());
					fc.setFilePath(file.getAbsolutePath());
					fc.setParentsPath(file.getParent());
					listMap.put(id,fc);
					pathMap.put(id, file.getAbsolutePath());
					id++;
				}
			}
		}
	}
	
	public ArrayList<String> getFileNameList() {
		return mp3List;
	}
	
	public String getFilePath(int index) {
		// TODO Auto-generated method stub
		FileContent localFileContent = (FileContent) listMap.get(index);
		String filePath = localFileContent.getFilePath();
		System.out.println(filePath);
		return filePath;
	}
}
