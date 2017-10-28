package com.easyupload.photos;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.easyupload.photos.s3.S3Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;


@SpringBootApplication
public class FileManagerServicesApplication extends SpringBootServletInitializer /*implements CommandLineRunne*/{

	@Autowired
	S3Services s3Services;
	
	@Value("${s3.uploadfile}")
	private String uploadFilePath;
	
	@Value("${s3.key}")
	private String downloadKey;
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FileManagerServicesApplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(FileManagerServicesApplication.class, args);
	}
	
	/*@Override
	public void run(String... args) throws Exception {
		System.out.println("---------------- START UPLOAD FILE ----------------");
		s3Services.uploadFile("file.txt", uploadFilePath);
		System.out.println("---------------- START DOWNLOAD FILE ----------------");
		//s3Services.downloadFile(downloadKey);
	}*/

}
