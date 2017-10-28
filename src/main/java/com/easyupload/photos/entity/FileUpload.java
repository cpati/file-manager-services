package com.easyupload.photos.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="file_uploads")
public class FileUpload {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	@Column(name = "user_first_name")
    private String firstName;
	@Column(name = "user_last_name")
    private String lastName;
	@Column(name = "file_name")
    private String fileName;
	@Column(name = "file_desc")
    private String fileDesc;
	@Column(name = "creation_date")
    private String creationDate;
	@Column(name = "last_updated_date")
    private String updateDate;
    
    public FileUpload() {
    	
    }
	public FileUpload(String firstName, String lastName, String fileName,String fileDesc, String creationDate,
			String updateDate) {
		this.firstName = firstName;
		this.fileDesc = fileDesc;
		this.lastName = lastName;
		this.fileName = fileName;
		this.creationDate = creationDate;
		this.updateDate = updateDate;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	@Override
	public String toString() {
		return "FileUpload [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", fileName=" + fileName
				+ ", creationDate=" + creationDate + ", updateDate=" + updateDate + "]";
	}
    
    
    
}
