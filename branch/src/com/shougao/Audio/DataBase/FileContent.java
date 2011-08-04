package com.shougao.Audio.DataBase;

public class FileContent{
	
	private String fileName = null;
	private String filePath = null;
	private String parentsPath = null;
	private AUDIO_TAG tag = null;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getParentsPath() {
		return parentsPath;
	}
	public void setParentsPath(String parentsPath) {
		this.parentsPath = parentsPath;
	}
	public AUDIO_TAG getTag() {
		return tag;
	}
	public void setTag(AUDIO_TAG tag) {
		this.tag = tag;
	}
}
