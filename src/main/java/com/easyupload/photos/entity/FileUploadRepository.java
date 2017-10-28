package com.easyupload.photos.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
	List<FileUpload> findByFileName(String fileName);
}