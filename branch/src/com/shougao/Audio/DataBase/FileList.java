package com.shougao.Audio.DataBase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileList {

	private static final String SD_PATH = "/sdcard";
	private ArrayList<String> mp3List = new ArrayList<String>();
	private ArrayList<String> pathList = new ArrayList<String>();
	private HashMap fileMap = new HashMap();
	private int id = 0;
	
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
					fc.setFilePath(file.getAbsolutePath());
					fc.setParentsPath(file.getParent());
					fileMap.put(file.getName(), fc);
					id++;
					System.out.println("=="+id);
				}
			}
		}
	}
	
	public ArrayList<String> getFileNameList() {
		scanFile(SD_PATH);
		Iterator iter = fileMap.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry)iter.next();
			String key = (String) entry.getKey();
			System.out.println(key);
			mp3List.add(key);
			FileContent value = (FileContent) entry.getValue();
		}
		return mp3List;
	}
	
	public String getFilePath(String fileName) {
		// TODO Auto-generated method stub
		FileContent localFileContent = (FileContent) fileMap.get(fileName);
		String filePath = localFileContent.getFilePath();
		System.out.println(filePath);
		return filePath;
	}
}
