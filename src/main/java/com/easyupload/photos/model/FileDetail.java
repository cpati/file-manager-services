package com.easyupload.photos.model;

import java.util.Date;

public class FileDetail {
	private String fileName;
	private String fileDesc;
	private String owner;
	private String lastModified;


	public FileDetail(String fileName, String fileDesc, String owner, String date) {
		super();
		this.fileName = fileName;
		this.fileDesc = fileDesc;
		this.owner = owner;
		this.lastModified = date;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	

	public String getFileDesc() {
		return fileDesc;
	}

	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	
	
}
