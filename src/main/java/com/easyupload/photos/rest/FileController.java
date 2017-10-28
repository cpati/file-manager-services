package com.easyupload.photos.rest;

	
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.easyupload.photos.s3.S3Services;
import com.easyupload.photos.util.Utility;
import com.easyupload.photos.entity.FileUpload;
import com.easyupload.photos.entity.FileUploadRepository;
import com.easyupload.photos.model.FileDetail;

@RestController
@RequestMapping("/file-manager-services/")
@MultipartConfig(fileSizeThreshold = 20971520)
public class FileController {

	@Autowired
	private S3Services s3Services;
	
	@Autowired
	private FileUploadRepository fileUploadRepository;
	
	@RequestMapping("/hi")
	 public String sayHi() {
		 return "Hi";
	 }
	
	@RequestMapping(value = "/getfiles")
    public List<FileDetail> getFileNames() {
		return s3Services.getFileNames();
	}
	
	@RequestMapping(value = "/getfiles/{fileName}")
    public FileDetail getFileName(@PathVariable("fileName") String fileName) {
		System.out.println("getFileNames:"+fileName);
		List<FileUpload> fileUpload=fileUploadRepository.findByFileName(fileName+".txt");
		FileDetail fileDetail=null;
		if (fileUpload.size() >0) {
			System.out.println("found");
			fileDetail=new FileDetail(fileName,fileUpload.get(0).getFileDesc(),
									fileUpload.get(0).getFirstName()+" "+fileUpload.get(0).getLastName(),
									fileUpload.get(0).getUpdateDate());
		}
		System.out.println("fileDetail:"+fileDetail.getFileDesc());
		return fileDetail;
	}
	
	@RequestMapping(value = "/upload")
    public String uploadFile(
    			@RequestParam(value="firstName", required=true) String firstName,
            @RequestParam(value="lastName", required=true) String lastName,
            @RequestParam("uploadedFile") MultipartFile uploadedFileRef,
    			@RequestParam(value="fileDesc") String fileDesc){
		FileInputStream uploadedInputStream=null;
		try {
			String fileName=uploadedFileRef.getOriginalFilename();
			fileUploadRepository.save(new FileUpload(firstName,lastName,fileName,fileDesc,Utility.dateFormatter(new Date()),Utility.dateFormatter(new Date())));
			uploadedInputStream =  (FileInputStream) uploadedFileRef.getInputStream();
			s3Services.uploadFile(fileName,uploadedInputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "upload failed!!";
		}
		return "successfully upload";
	}
	
	@RequestMapping(value = "/delete")
    public String deleteFile(@RequestParam("fileName")  String fileName) {
		System.out.println("deleteFile:"+fileName);
		try {
			s3Services.deleteFile(fileName);
		} catch (Exception e) {
			e.printStackTrace();
			return "delete failed!!";
		}
		return "successfully deleted";
	}
	
	@RequestMapping(value = "/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("fileName")  String fileName) {
		System.out.println("downloadFile:"+fileName);
		InputStreamResource resource=null;
		try {
			resource=s3Services.downloadFile(fileName,"CF");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION,
		                  "attachment;filename=" + fileName)
		            .contentType(MediaType.TEXT_PLAIN).contentLength(4)
		            .body(resource);
	}
}
