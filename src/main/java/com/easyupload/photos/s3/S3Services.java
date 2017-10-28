package com.easyupload.photos.s3;

import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.InputStreamResource;

import com.easyupload.photos.model.FileDetail;

public interface S3Services {
	public InputStreamResource downloadFile(String keyName);
	public InputStreamResource downloadFile(String keyName,String from);
	public void uploadFile(String keyName, String uploadFilePath);
	public void uploadFile(String keyName,InputStream uploadFile);
	public void deleteFile(String keyName);
	public List<FileDetail> getFileNames();
}
