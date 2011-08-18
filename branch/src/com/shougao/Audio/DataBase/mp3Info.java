package com.shougao.Audio.DataBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
/*
 * ��ȡmp3�ļ���Ϣ������ʾ��playlayout��
 * 2011-8-17
 */
public class mp3Info {
	private RandomAccessFile ran = null;
	private String musicTitle = null;
	private String musicArtist = null;
	private String musicAlbum = null;
	private String musicComment = null;
	private File currentFile = null;
	private byte[] buffer = new byte[128];
	
	/*
	 * MediaService����mp3Info����ɶԵ�ǰ�����ļ�����Ϣ����
	 * ���캯����ɶ��ļ������Ľ��ܼ����ļ�����Ϣ����
	 * 2011-8-18
	 */
	public mp3Info(String fileName){
		currentFile = new File(fileName);
		try {
			ran = new RandomAccessFile(currentFile, "r");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ran.seek(ran.length()-128);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ran.read(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//��ȡtital��ϸ����Ϣ,ת����GBK��ȡ��
		
		try {
			setMusicTitle(new String(buffer,3,30,"GBK").trim());
			setMusicArtist(new String(buffer,33,30,"GBK").trim());
			setMusicAlbum(new String(buffer,63,30,"GBK").trim());
			setMusicComment(new String(buffer,97,28,"GBK").trim());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the musicTitle
	 */
	public String getMusicTitle() {
		return musicTitle;
	}

	/**
	 * @param musicTitle the musicTitle to set
	 */
	public void setMusicTitle(String musicTitle) {
		this.musicTitle = musicTitle;
	}

	/**
	 * @return the musicArtist
	 */
	public String getMusicArtist() {
		return musicArtist;
	}

	/**
	 * @param musicArtist the musicArtist to set
	 */
	public void setMusicArtist(String musicArtist) {
		this.musicArtist = musicArtist;
	}

	/**
	 * @return the musicAlbum
	 */
	public String getMusicAlbum() {
		return musicAlbum;
	}

	/**
	 * @param musicAlbum the musicAlbum to set
	 */
	public void setMusicAlbum(String musicAlbum) {
		this.musicAlbum = musicAlbum;
	}

	/**
	 * @return the musicComment
	 */
	public String getMusicComment() {
		return musicComment;
	}

	/**
	 * @param musicComment the musicComment to set
	 */
	public void setMusicComment(String musicComment) {
		this.musicComment = musicComment;
	}
}
