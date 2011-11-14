package com.shougao.Audio.DataBase;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileList {

	/*
	 * ����id����0��ʼ���㣬�ڽ�����ʾ�ĸ����б��1��ʼ
	 * 
	 * 2011-8-15
	 */
	private static String SD_PATH = "/mnt/udisk";
	private ArrayList<String> mp3List = new ArrayList<String>();//���ڴ����ʾ�ĸ����б�
	private HashMap pathMap = new HashMap();//���ڴ��id - ·����Ӧ��
	private HashMap listMap = new HashMap();//���ڴ��id - �ļ����ݶ����
	private int id = 0;
	
	/**
	 * ���캯��ֻ���Ĭ��·���ĳ�ʼ��
	 * �����Ҫ�ı�Ŀ¼����ʹ�ñ����ʵ��������initFileList������
	 */
	public FileList(){
		
		initFileList(SD_PATH);
	}
	
	public void initFileList(String path) {
		mp3List.clear();//ÿ���µĲ����б����ԭ���ľ��б�
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
