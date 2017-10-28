package com.easyupload.photos.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.easyupload.photos.util.Utility;
import com.easyupload.photos.entity.FileUpload;
import com.easyupload.photos.entity.FileUploadRepository;
import com.easyupload.photos.model.FileDetail;

@Service
public class S3ServicesImpl implements S3Services {

	private Logger logger = LoggerFactory.getLogger(S3ServicesImpl.class);

	@Autowired
	private AmazonS3 s3client;
	
	@Autowired
	private FileUploadRepository fileUploadRepository;

	@Value("${s3.bucket}")
	private String bucketName;

	@Override
	public InputStreamResource downloadFile(String keyName) {
		S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));
		S3ObjectInputStream inputStream = s3object.getObjectContent();
		return new InputStreamResource(inputStream);
	}
	
	@Override
	public InputStreamResource downloadFile(String keyName,String from) {
		InputStreamResource file=null;
		if (from=="S3") {
			file=downloadFile(keyName);
		} else if (from=="CF") {
			URL url;
			try {
				url = new URL("http://d30lkyn29uha7q.cloudfront.net/"+keyName);
				System.out.println("url:"+url);
				InputStream inputStream=url.openStream();
				file=new InputStreamResource(inputStream);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		System.out.println("file:"+file);
		return file;
	}

	@Override
	public void uploadFile(String keyName, String uploadFilePath) {
		try {

			File file = new File(uploadFilePath);
			s3client.putObject(new PutObjectRequest(bucketName, keyName, file));
			logger.info("===================== Upload File - Done! =====================");

		} catch (AmazonServiceException ase) {
			logger.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			logger.info("Caught an AmazonClientException: ");
			logger.info("Error Message: " + ace.getMessage());
		}

	}

	public void uploadFile(String keyName, InputStream uploadFile) {
		logger.info("uploadFile:" + bucketName + ":" + keyName + ":" + uploadFile);
		try {
			// InputStream tmp=uploadFile;
			// Long contentLength = Long.valueOf(IOUtils.toByteArray(tmp).length);
			// System.out.println("contentLength:"+contentLength);

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(4);
			AccessControlList acl = new AccessControlList();
			acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
			s3client.putObject(new PutObjectRequest(bucketName, keyName, uploadFile, metadata).withAccessControlList(acl));
			logger.info("===================== Upload File - Done! =====================");

		} catch (AmazonServiceException ase) {
			logger.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			logger.info("Caught an AmazonClientException: ");
			logger.info("Error Message: " + ace.getMessage());
		}

	}

	@Override
	public void deleteFile(String keyName) {
		logger.info("deleteFile:" + keyName + ":" + bucketName);
		try {
			s3client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
			logger.info("===================== Upload File - Done! =====================");

		} catch (AmazonServiceException ase) {
			logger.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			logger.info("Caught an AmazonClientException: ");
			logger.info("Error Message: " + ace.getMessage());
		}

	}

	public List<FileDetail> getFileNames() {

		List<FileDetail> files = new ArrayList<>();

		try {
			System.out.println("Listing objects");
			final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(2);
			ListObjectsV2Result result;
			String owner = "Chidananda Pati";
			String lastModified = null;
			do {
				result = s3client.listObjectsV2(req);

				for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {

					try {
						lastModified = Utility.dateFormatter(objectSummary.getLastModified());
					} catch (NullPointerException e) {
						lastModified = Utility.dateFormatter(new Date());
					}		
					List<FileUpload> fileUpload=fileUploadRepository.findByFileName(objectSummary.getKey());
					if (fileUpload.size() >0) {
						System.out.println("fileUpload:"+fileUpload.get(0));
						System.out.println(
								" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
						files.add(new FileDetail(objectSummary.getKey(),fileUpload.get(0).getFileDesc(),
												fileUpload.get(0).getFirstName()+" "+fileUpload.get(0).getLastName(),
												fileUpload.get(0).getUpdateDate()));
					}
				}
				System.out.println("Next Continuation Token : " + result.getNextContinuationToken());
				req.setContinuationToken(result.getNextContinuationToken());
			} while (result.isTruncated() == true);

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, " + "which means your request made it "
					+ "to Amazon S3, but was rejected with an error response " + "for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, " + "which means the client encountered "
					+ "an internal error while trying to communicate" + " with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}

		return files;
	}
}