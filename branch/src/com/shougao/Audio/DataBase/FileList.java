package com.shougao.Audio.DataBase;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileList {

	/*
	 * 所有id都从0开始计算，在界面显示的歌曲列表从1开始
	 * 
	 * 2011-8-15
	 */
	private static String SD_PATH = "/mnt/udisk";
	private ArrayList<String> mp3List = new ArrayList<String>();//用于存放显示的歌曲列表
	private HashMap pathMap = new HashMap();//用于存放id - 路径对应表
	private HashMap listMap = new HashMap();//用于存放id - 文件内容对象表
	private int id = 0;
	
	/**
	 * 构造函数只完成默认路径的初始化
	 * 如果需要改变目录可以使用本类的实例，调用initFileList函数。
	 */
	public FileList(){
		
		initFileList(SD_PATH);
	}
	
	public void initFileList(String path) {
		mp3List.clear();//每次新的播放列表都清楚原来的就列表
		// TODO Auto-generated method stub
		
		scanFile(path);
		for(int i=0; i<mp3List.size(); i++){
			System.out.println("file order" + mp3List.get(i));
		}
		orderFile();
	}
	
	private void orderFile() {
		// TODO Auto-generated method stub
		for(int i=0; i<mp3List.size(); i++){
			for(int j=1; j<mp3List.size(); j++){
				if(mp3List.get(i).compareTo(mp3List.get(j))<0){
					
				}else{
					String str = mp3List.get(j);
					mp3List.set(j, mp3List.get(i));
					mp3List.set(i, str);
				}
			}
		}
		for(int i=0; i<mp3List.size(); i++){
			System.out.println("file order:" + mp3List.get(i));
		}
	}

	private void scanFile(String path) {
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
//					mp3List.add((id+1) + "." + file.getName());
					mp3List.add(file.getName());
					fc.setFilePath(file.getAbsolutePath());
					fc.setParentsPath(file.getParent());
					listMap.put(id,fc);
					pathMap.put(id, file.getAbsolutePath());
					id++;
				}
			}
		}
		file = null;
	}
	
	public ArrayList<String> getFileNameList() {
		return mp3List;
	}
	
	public int fileNum(){
		return mp3List.size();
	}
	
	public String getFilePath(int index) {
		// TODO Auto-generated method stub
		FileContent localFileContent = (FileContent) listMap.get(index);
		String filePath = localFileContent.getFilePath();
		System.out.println(filePath);
		return filePath;
	}
}
