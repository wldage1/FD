package com.sw.core.data.entity;

import java.io.Serializable;

public class FileInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7123054650457409388L;

	//文件名
	private String name;
	//FTP文件路径
	private String ftpFilePath;
	//带转换文件路径
	private String convertFilePath;
	//项目根目录（获取字体）
	private String localPath;
	//居间公司水印
	private String orgWatermark;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFtpFilePath() {
		return ftpFilePath;
	}
	public void setFtpFilePath(String ftpFilePath) {
		this.ftpFilePath = ftpFilePath;
	}
	public String getConvertFilePath() {
		return convertFilePath;
	}
	public void setConvertFilePath(String convertFilePath) {
		this.convertFilePath = convertFilePath;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public String getOrgWatermark() {
		return orgWatermark;
	}
	public void setOrgWatermark(String orgWatermark) {
		this.orgWatermark = orgWatermark;
	}
	
}
