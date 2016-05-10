package com.sw.plugins.storage.entity;

import org.springframework.web.multipart.MultipartFile;

import com.sw.core.data.entity.MongoEntity;

public class GridFile extends MongoEntity {

	private static final long serialVersionUID = 2080927907430850524L;

	private MultipartFile file;

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public MultipartFile getFile() {
		return file;
	}

}
